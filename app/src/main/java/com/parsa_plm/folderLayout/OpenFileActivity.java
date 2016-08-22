package com.parsa_plm.folderLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.jointelementinspector.jointelementinspector.MainActivity;
import com.jointelementinspector.jointelementinspector.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class OpenFileActivity extends Activity implements IFolderItemListener {
    private FolderLayout localFolders;
    private final String filePath = "/sdcard/Download";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        localFolders = (FolderLayout) findViewById(R.id.localFolders);
        localFolders.setIFolderItemListener(this);
        localFolders.setDir(filePath);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void OnCannotFileRead(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.viewfile48)
                .setTitle(file.getName() + "folder can't be read!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
    @Override
    public void OnFileClicked(final File file) {
        final ExpandableListData expandableListData = null;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (!file.getName().toLowerCase().endsWith("xml")) {
            adb.setIcon(R.drawable.viewfile48);
            adb.setMessage("Only File with .xml extension is accepted.");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            adb.show();
        } else {
            adb.setIcon(R.drawable.viewfile48);
            adb.setTitle("Open File: " + file.getName());
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // using Parcelable to send custom data
                    DocumentBuilder domBuilder = null;
                    ExpandableListData expandableListDataHeader = null;
                    String partName = null;
                    String partNr = null;
                    String orderNr = null;
                    String inspector = null;
                    String inspectorDate = null;
                    String vehicle = null;
                    String inspectorTimeSpan = null;
                    String frequency = null;
                    try{
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        try {
                            domBuilder = factory.newDocumentBuilder();
                        }catch (ParserConfigurationException e){
                            Log.d("ParserException" , e.toString());
                        }
                        Document dom = domBuilder.parse(file);
                        Element plmXML = dom.getDocumentElement();
                        NodeList occurrence = plmXML.getElementsByTagName("Occurrence");
                        NodeList associatedAttachment = plmXML.getElementsByTagName("AssociatedAttachment");
                        NodeList productRevision = plmXML.getElementsByTagName("ProductRevision");
                        NodeList form = plmXML.getElementsByTagName("Form");
                        String associatedAttachmentRefs = null;
                        String instancedRef = null;
                        String occurrenceRefs = null;
                        // 20160817: neues Verfahren ohne verschachtete Schleife
                        for (int k = 0; k < associatedAttachment.getLength(); k++) {
                            Element firstOccu = (Element) occurrence.item(0);
                            // prepare instanceRef to search product revision
                            instancedRef = firstOccu.getAttribute("instancedRef");
                            // obtain associated attachments to search associated attachment
                            associatedAttachmentRefs = firstOccu.getAttribute("associatedAttachmentRefs");
                            // obtain child occurrence to prepare expand list
                            occurrenceRefs = firstOccu.getAttribute("occurrenceRefs");
                        }
                        // get part name
                        if (notNullAndEmpty(instancedRef)){
                            String idUsed2FindProductRevision = instancedRef.substring(1);
                            for (int l = 0; l < productRevision.getLength(); l++) {
                                Element eleProRev = (Element) productRevision.item(l);
                                String idRevision = eleProRev.getAttribute("id");
                                if (notNullAndEmpty(idUsed2FindProductRevision)) {
                                    if (idUsed2FindProductRevision.trim().equalsIgnoreCase(idRevision.trim())) {
                                        partName = eleProRev.getAttribute("name");
                                        break;
                                    }
                                }
                            }
                        }
                        Log.d("xml part name", partName);
                        String[] associatedAttachmentIds = null;
                        if (notNullAndEmpty(associatedAttachmentRefs)) {
                            associatedAttachmentIds = associatedAttachmentRefs.split("#");
                        }
                        String associatedAttachmentRole = null;
                        String associateAttachmentId = null;
                        String attachmentRef = null;
                        String childRefs = null;
                        if (associatedAttachmentIds.length > 0) {
                            IdLoop:
                            for (String id : associatedAttachmentIds) {
                                for (int k = 0; k < associatedAttachment.getLength(); k ++) {
                                    Element eleAsso = (Element) associatedAttachment.item(k);
                                    associatedAttachmentRole = eleAsso.getAttribute("role");
                                    associateAttachmentId = eleAsso.getAttribute("id");
                                    if (id.equalsIgnoreCase(associateAttachmentId) && associatedAttachmentRole.equalsIgnoreCase("IMAN_master_form")) {
                                        attachmentRef = eleAsso.getAttribute("attachmentRef");
                                        // für weitere Attribute
                                        childRefs = eleAsso.getAttribute("childRefs");
                                        break IdLoop;
                                    }
                                }
                            }
                        }
                        if (notNullAndEmpty(attachmentRef)) {
                            attachmentRef = attachmentRef.substring(1);
                        }
                        /*
                        funktion auslagern spaeter
                        String formName = findFormNameById(form, attachmentRef);
                        if (notNullAndEmpty(formName)){
                            Log.d("xml form", formName);
                        }else Log.d("xml", "form ist null oder empty");
                        */
                        formLoop:
                        for (int k = 0; k < form.getLength(); k++) {
                            Element eleForm = (Element) form.item(k);
                            if (notNullAndEmpty(attachmentRef)) {
                                if (attachmentRef.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                                    partNr = eleForm.getAttribute("name");
                                    Log.d("xml partNr", partNr);
                                    NodeList nodeOfForm = eleForm.getElementsByTagName("UserValue");
                                    for (int i = 0; i < nodeOfForm.getLength(); i++) {
                                        Element eleNode = (Element) nodeOfForm.item(i);
                                        String nodeTitle = eleNode.getAttribute("title");
                                        switch (nodeTitle){
                                            case "a2_InspDate":
                                                inspectorDate = eleNode.getAttribute("value");
                                            case "a2_Inspector":
                                                inspector = eleNode.getAttribute("value");
                                            case "a2_OrderNr":
                                                orderNr = eleNode.getAttribute("value");
                                            case "project_id":
                                                vehicle = eleNode.getAttribute("value");
                                        }
                                    }
                                    break formLoop;
                                }
                            }
                        }
                        Log.d("xml", inspectorDate);
                        Log.d("xml", inspector);
                        Log.d("xml", orderNr);
                        Log.d("xml", vehicle + "leer");
                        // obtain attribute time span and frequency
                        String idOfFormForMoreAttri = null;
                        if (notNullAndEmpty(childRefs)) {
                            // first spilt string is empty
                            // idFindAttachment contains blank!!!!!, use TRIM() for sure
                            String id2FindAttachment = childRefs.split("#")[1];
                            for (int k = 0; k < associatedAttachment.getLength(); k++) {
                                Element eleAssociated = (Element) associatedAttachment.item(k);
                                // idCompareTo contains blank
                                String idCompareTo = eleAssociated.getAttribute("id");
                                if (notNullAndEmpty(idCompareTo)) {
                                    if (idCompareTo.trim().equalsIgnoreCase(id2FindAttachment.trim())) {
                                        idOfFormForMoreAttri = eleAssociated.getAttribute("attachmentRef");
                                        if (notNullAndEmpty(idOfFormForMoreAttri)){
                                            idOfFormForMoreAttri = idOfFormForMoreAttri.substring(1);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        // use id of form to find more attribute
                        if (notNullAndEmpty(idOfFormForMoreAttri)) {
                            for (int k = 0; k < form.getLength(); k++) {
                                Element eleForm = (Element) form.item(k);
                                // better to use TRIM() for sure
                                if (idOfFormForMoreAttri.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                                    NodeList nodeOfForm = eleForm.getElementsByTagName("UserValue");
                                    for (int i = 0; i < nodeOfForm.getLength(); i++) {
                                        Element eleNode = (Element) nodeOfForm.item(i);
                                        String nodeTitle = eleNode.getAttribute("title");
                                        switch (nodeTitle) {
                                            case "a2_InspCycle":
                                                frequency = eleNode.getAttribute("value");
                                            case "a2_InspectionTimespan":
                                                inspectorTimeSpan = eleNode.getAttribute("value");
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("xml", frequency);
                        Log.d("xml", inspectorTimeSpan);
                        if (partName != null && partNr != null && inspector != null && inspectorDate != null && inspectorTimeSpan != null
                                && vehicle != null && frequency != null && orderNr != null) {
                            expandableListDataHeader = new ExpandableListData(partName, partNr, orderNr, inspector, inspectorDate, vehicle, inspectorTimeSpan, frequency);
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    // pass expandable list data to main activity for later use
                    Intent intent = new Intent(OpenFileActivity.this, MainActivity.class);
                    //wenn man die selbe Datei mehrmals einliest, sollte kein neues Object erzeuget werden, TODO vermeid duplicate
                    if (expandableListDataHeader != null) {
                        intent.putExtra("com.ExpandableListData", expandableListDataHeader);
                        //intent.putExtra("objectID",  2);
                        setResult(Activity.RESULT_OK, intent);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        }
    }
    /*
    this getter should return list data to display in the expand list view for the over view fragment
     */
    public ExpandableListData getExpandableListData(){
        return null;
    }

    public ExpandableListData parseXML(File file) {
        return null;
    }

    public boolean notNullAndEmpty(String s){
        return s != null && !s.isEmpty();
    }
    // TRIM() !!!
    public String findFormNameById(NodeList nodeList, String id){
        String formName = null;

        if (notNullAndEmpty(id)) return null;
        if (nodeList.getLength() == 0) return null;
        for (int k = 0; k < nodeList.getLength(); k++) {
            Element ele = (Element) nodeList.item(k);
            String idOfForm = ele.getAttribute("id");
            if (notNullAndEmpty(idOfForm)) {
                if (id.trim().equalsIgnoreCase(idOfForm.trim())){
                    Log.d("xml", "form gefunden");
                    formName = ele.getAttribute("name");
                    break;
                }
            }
        }
        return formName;
    }
}
