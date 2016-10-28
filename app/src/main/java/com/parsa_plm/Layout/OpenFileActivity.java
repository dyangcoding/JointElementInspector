package com.parsa_plm.Layout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.MainActivity;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final ExpandableListHeader expandableListHeader = null;
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
                    ExpandableListHeader expandableListHeader = OpenFileActivity.this.parseXML(file);
                    // pass expandable list data to main activity for later use
                    Intent intent = new Intent(OpenFileActivity.this, MainActivity.class);
                    //wenn man die selbe Datei mehrmals einliest, sollte kein neues Object erzeuget werden, TODO vermeid duplicate
                    if (expandableListHeader != null) {
                        intent.putExtra("com.ExpandableListData", expandableListHeader);
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
    //20161019: extract method parseXML
    private ExpandableListHeader parseXML(File file) {
        // using Parcelable to send custom data
        DocumentBuilder domBuilder = null;
        ExpandableListHeader expandableListHeader = null;
        String partName = null;
        String partNr = null;
        String orderNr = null;
        String inspector = null;
        String inspectorDate = null;
        String vehicle = null;
        String inspectorTimeSpan = null;
        String frequency = null;
        String inspectorMethod = null;
        String inspectorScope = null;
        String inspectorNorm = null;
        // item type for header
        String type = null;
        String formRole = "IMAN_master_form";
        // contains child of the first occurrence, should be displayed as second level
        List<ExpandableListItem> childOfOccurrence = new ArrayList<>();
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
            // 20160826: design revision node list
            NodeList designRevision = plmXML.getElementsByTagName("DesignRevision");
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
                            type = eleProRev.getAttribute("subType");
                            break;
                        }
                    }
                }
            }
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
                        if (id.equalsIgnoreCase(associateAttachmentId) && associatedAttachmentRole.equalsIgnoreCase(formRole)) {
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
            formLoop:
            for (int k = 0; k < form.getLength(); k++) {
                Element eleForm = (Element) form.item(k);
                if (notNullAndEmpty(attachmentRef)) {
                    if (attachmentRef.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                        partNr = eleForm.getAttribute("name");
                        NodeList nodeOfForm = eleForm.getElementsByTagName("UserValue");
                        for (int i = 0; i < nodeOfForm.getLength(); i++) {
                            Element eleNode = (Element) nodeOfForm.item(i);
                            String nodeTitle = eleNode.getAttribute("title");
                            switch (nodeTitle){
                                case "a2_InspDate":
                                    inspectorDate = eleNode.getAttribute("value");
                                    break;
                                case "a2_Inspector":
                                    inspector = eleNode.getAttribute("value");
                                    break;
                                case "a2_OrderNr":
                                    orderNr = eleNode.getAttribute("value");
                                    break;
                                case "project_id":
                                    vehicle = eleNode.getAttribute("value");
                                    break;
                            }
                        }
                        break formLoop;
                    }
                }
            }
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
                                    break;
                                case "a2_InspMethod":
                                    inspectorMethod = eleNode.getAttribute("value");
                                    break;
                                case "a2_InspScope":
                                    inspectorScope = eleNode.getAttribute("value");
                                    break;
                                case "a2_InspectionTimespan":
                                    inspectorTimeSpan = eleNode.getAttribute("value");
                                    break;
                                case "a2_InspRegNORM":
                                    inspectorNorm = eleNode.getAttribute("value");
                                    break;
                            }
                        }
                    }
                }
            }
            // 20160826: move on with child occurrence
            String[] idsOfChildOccu = null;
            if (notNullAndEmpty(occurrenceRefs)) {
                idsOfChildOccu = occurrenceRefs.split(" ");
            }
            // find child occurrence and create ExpandableListItem
            // 20160829: child of child occurrence found and create WeldPoint
            for (String id :idsOfChildOccu) {
                // local variable ExpandlistItem, recreate for every loop
                ExpandableListItem item = null;
                // local variable associated AttachmentRefs
                String childAssociatedAttachmentRefs = null;
                // local variable instanceRef
                String childInstancedRef = null;
                // ids of weld points of the child occurrence
                String idsOfItemWeldPoint = null;
                // item name for expandableListItem
                String itemName = null;
                // item type
                String itemType = null;
                // item Nr
                String itemNr = null;
                // child of child occurrence, could be null
                List<WeldPoint> itemOfChild = null;
                for (int k = 0; k < occurrence.getLength(); k ++) {
                    Element eleChildOcc = (Element) occurrence.item(k);
                    if (id.trim().equalsIgnoreCase(eleChildOcc.getAttribute("id").trim())) {
                        childAssociatedAttachmentRefs = eleChildOcc.getAttribute("associatedAttachmentRefs");
                        childInstancedRef = eleChildOcc.getAttribute("instancedRef");
                        idsOfItemWeldPoint = eleChildOcc.getAttribute("occurrenceRefs");
                    }
                }
                // first find item name and item type, if id not in design revision, muss be in product revision
                if (notNullAndEmpty(childInstancedRef)) {
                    String idOfRevisions = childInstancedRef.split("#")[1];
                    boolean itemFound = false;
                    for (int k = 0; k < designRevision.getLength(); k ++) {
                        Element eleDesignRevison = (Element) designRevision.item(k);
                        if (idOfRevisions.trim().equalsIgnoreCase(eleDesignRevison.getAttribute("id").trim())) {
                            itemName = eleDesignRevison.getAttribute("name");
                            itemType = eleDesignRevison.getAttribute("subType");
                            if (itemName != null && itemType != null) itemFound = true;
                        }
                    }
                    if (!itemFound) {
                        for (int k = 0; k < productRevision.getLength(); k++) {
                            Element eleProductRevision = (Element) productRevision.item(k);
                            if (idOfRevisions.trim().equalsIgnoreCase(eleProductRevision.getAttribute("id").trim())) {
                                itemName = eleProductRevision.getAttribute("name");
                                itemType = eleProductRevision.getAttribute("subType");
                            }
                        }
                    }
                }
                // now use associated attachment to find item number
                if (notNullAndEmpty(childAssociatedAttachmentRefs)) {
                    String[] idsOfChildAttachment = childAssociatedAttachmentRefs.split("#");
                    String idOfFormForItemNr = null;
                    childAssocitedLoop:
                    for (String idOfChild: idsOfChildAttachment) {
                        for (int k = 0; k < associatedAttachment.getLength(); k++) {
                            Element eleChildAssociated = (Element) associatedAttachment.item(k);
                            if (idOfChild.trim().equalsIgnoreCase(eleChildAssociated.getAttribute("id").trim())
                                    && formRole.equalsIgnoreCase(eleChildAssociated.getAttribute("role"))) {
                                idOfFormForItemNr = eleChildAssociated.getAttribute("attachmentRef");
                                break childAssocitedLoop;
                            }
                        }
                    }
                    // now use forms id to find form and then get item number
                    if (notNullAndEmpty(idOfFormForItemNr)) {
                        String idSplitted = idOfFormForItemNr.split("#")[1];
                        for (int k = 0 ; k < form.getLength(); k++) {
                            Element eleChildForm = (Element) form.item(k);
                            if (idSplitted.trim().equalsIgnoreCase(eleChildForm.getAttribute("id").trim())) {
                                itemNr = eleChildForm.getAttribute("name");
                            }
                        }
                    }
                }
                // last use idsItemWeldPoint to find weld point
                // first version, obtain weld point name to display list structure, late more attribute
                if (notNullAndEmpty(idsOfItemWeldPoint)) {
                    itemOfChild = new ArrayList<>();
                    String[] ids = idsOfItemWeldPoint.split(" ");
                    for (String id2FindWeldPoint: ids) {
                        if (notNullAndEmpty(id2FindWeldPoint)) {
                            String name = null;
                            String joints_itemType = null;
                            WeldPoint weldPoint = null;
                            // 20161021: associated attachments to find characters
                            String associatedARs = null;
                            occuLoop:
                            for (int k = 0; k < occurrence.getLength(); k ++) {
                                Element eleOccu = (Element) occurrence.item(k);
                                if (id2FindWeldPoint.trim().equalsIgnoreCase(eleOccu.getAttribute("id").trim())) {
                                    associatedARs = eleOccu.getAttribute("associatedAttachmentRefs");
                                    NodeList nodes = eleOccu.getElementsByTagName("UserValue");
                                    for (int i = 0; i < nodes.getLength(); i ++) {
                                        Element eleNode = (Element) nodes.item(i);
                                        String nodeTitle = eleNode.getAttribute("title");
                                        if (nodeTitle.equalsIgnoreCase("OccurrenceName")) {
                                            name = eleNode.getAttribute("value");
                                            break occuLoop;
                                        }
                                    }
                                }
                            }
                            String idForForm = null;
                            // check associated attachments
                            if (notNullAndEmpty(associatedARs)) {
                                String[] aRs = associatedARs.split("#");
                                attachmentsLoop:
                                for (String idForCharacter: aRs) {
                                    if (notNullAndEmpty(idForCharacter)) {
                                        for(int k = 0; k < associatedAttachment.getLength(); ++k) {
                                            Element eleAss = (Element) associatedAttachment.item(k);
                                            if (idForCharacter.trim().equalsIgnoreCase(eleAss.getAttribute("id").trim())
                                                    && "TC_Feature_Form_Relation".equalsIgnoreCase(eleAss.getAttribute("role"))) {
                                                idForForm = eleAss.getAttribute("attachmentRef");
                                                break attachmentsLoop;
                                            }
                                        }
                                    }
                                }
                            }
                            // 20161021: map which hold all key value paar for wild points
                            Map<String, String> character = new HashMap<>();
                            if (notNullAndEmpty(idForForm)) {
                                String idForFormSplitted = idForForm.split("#")[1];
                                for (int k = 0; k < form.getLength(); ++k) {
                                    Element eleForm = (Element) form.item(k);
                                    if (idForFormSplitted.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                                        //joints_itemType = eleForm.getAttribute("id");
                                        Log.d("xml", joints_itemType);
                                        NodeList nodes = eleForm.getElementsByTagName("UserValue");
                                        for (int i =0; i < nodes.getLength(); ++i) {
                                            Element eleNode = (Element) nodes.item(i);
                                            String nodeTitle = eleNode.getAttribute("title");
                                            String nodeValue;
                                            switch (nodeTitle){
                                                case "a2_100_Crack":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("Crack", nodeValue);
                                                    break;
                                                case "a2_104_CraterCrack":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("CraterCrack", nodeValue);
                                                    break;
                                                case "a2_2017_SurfacePore":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("SurfacePore", nodeValue);
                                                    break;
                                                case "a2_2025_EndCraterPipe":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("EndCraterPipe", nodeValue);
                                                    break;
                                                case "a2_401_LackOfFusion":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("LackOfFusion", nodeValue);
                                                    break;
                                                case "a2_4021_IncRootPenetration":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("IncRootPenetration", nodeValue);
                                                    break;
                                                case "a2_5011_ContinousUndercut":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ContinousUndercut", nodeValue);
                                                    break;
                                                case "a2_5012_IntUndercut":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("IntUndercut", nodeValue);
                                                    break;
                                                case "a2_5013_ShrinkGrooves":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ShrinkGrooves", nodeValue);
                                                    break;
                                                case "a2_502_ExcWeldMetal":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ExcWeldMetal", nodeValue);
                                                    break;
                                                case "a2_503_ExcConvex":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ExcConvex", nodeValue);
                                                    break;
                                                case "a2_504_ExcPenetration":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ExcPenetration", nodeValue);
                                                    break;
                                                case "a2_505_IncWeldToe":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("IncWeldToe", nodeValue);
                                                    break;
                                                case "a2_506_Overlap":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("Overlap", nodeValue);
                                                    break;
                                                case "a2_509_Sagging":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("Sagging", nodeValue);
                                                    break;
                                                case "a2_510_BurnThrough":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("BurnThrough", nodeValue);
                                                    break;
                                                case "a2_511_IncFilledGroove":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("IncFilledGroove", nodeValue);
                                                    break;
                                                case "a2_512_ExcAsymFilledWeld":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ExcAsymFilledWeld", nodeValue);
                                                    break;
                                                case "a2_515_RootConcavity":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("RootConcavity", nodeValue);
                                                    break;
                                                case "a2_516_RootPorosity":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("RootPorosity", nodeValue);
                                                    break;
                                                case "a2_517_PoorRestart":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("PoorRestart", nodeValue);
                                                    break;
                                                case "a2_5213_InsThroatThick":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("InsThroatThick", nodeValue);
                                                    break;
                                                case "a2_5214_ExcThoratThick":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ExcThoratThick", nodeValue);
                                                    break;
                                                case "a2_601_ArcStrike":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("ArcStrike", nodeValue);
                                                    break;
                                                case "a2_602_Spatter":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("Spatter", nodeValue);
                                                    break;
                                                case "a2_610_TempColours":
                                                    nodeValue = eleNode.getAttribute("value");
                                                    character.put("TempColours", nodeValue);
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (notNullAndEmpty(name)) {
                                //String joins_it = joints_itemType.split(" ")[1];
                                //Log.d("xml", joins_it);
                                weldPoint = new WeldPoint(name, "SpotWeld", character);
                                itemOfChild.add(weldPoint);
                            }
                        }
                    }
                }
                // now time to create ExpandablelistItem
                if (itemName != null && itemNr != null && itemType != null) {
                    item = new ExpandableListItem(itemName, itemNr, itemType, itemOfChild != null ? itemOfChild : null);
                    childOfOccurrence.add(item);
                }
            }
            //20160829: type and child occurrence added, null value accepted for child occurrence, in this case we have no second level data to display
            if (partName != null && partNr != null && inspector != null && inspectorDate != null && inspectorTimeSpan != null
                    && vehicle != null && frequency != null && orderNr != null
                    && type != null && inspectorMethod != null && inspectorScope != null && inspectorNorm != null) {
                expandableListHeader = new ExpandableListHeader(partName, partNr, orderNr, inspector, inspectorDate, vehicle,
                        inspectorTimeSpan, frequency, type, inspectorMethod, inspectorScope, inspectorNorm, childOfOccurrence);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return expandableListHeader;
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
