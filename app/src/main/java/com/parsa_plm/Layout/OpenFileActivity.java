package com.parsa_plm.Layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
    // 20170106: we use external storage path to open xml file
    // private final String filePath = "/sdcard/Download";
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        this.mContext = getApplicationContext();
        localFolders = (FolderLayout) findViewById(R.id.localFolders);
        localFolders.setIFolderItemListener(this);
        // 20161223: test internal storage path
        File path = this.getExternalFilesDir(null);
        localFolders.setDir(path.toString());
    }

    @Override
    protected void onPause() {
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
                    // 20161125: use async task to process file
                    new ParseXMLFileTask(OpenFileActivity.this, file.getPath()).execute();
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

    public boolean notNullAndEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    // TRIM() !!!
    public String findFormNameById(NodeList nodeList, String id) {
        String formName = null;

        if (notNullAndEmpty(id)) return null;
        if (nodeList.getLength() == 0) return null;
        for (int k = 0; k < nodeList.getLength(); k++) {
            Element ele = (Element) nodeList.item(k);
            String idOfForm = ele.getAttribute("id");
            if (notNullAndEmpty(idOfForm)) {
                if (id.trim().equalsIgnoreCase(idOfForm.trim())) {
                    Log.d("xml", "form gefunden");
                    formName = ele.getAttribute("name");
                    break;
                }
            }
        }
        return formName;
    }

    private class ParseXMLFileTask extends AsyncTask<File, Void, ExpandableListHeader> {
        private ProgressDialog mProgressDialog;
        private String mFilePath;
        private Context mContext;

        ParseXMLFileTask(Context context, String filePath) {
            this.mFilePath = filePath;
            this.mContext = context;
        }

        protected void onPreExecute() {
            this.mProgressDialog = new ProgressDialog(this.mContext);
            this.mProgressDialog.setMessage("   wird geladen ...   ");
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.mProgressDialog.show();
        }

        // 20161214 need to expand later, method exacting for better maintain later
        protected ExpandableListHeader doInBackground(File... file) {
            // using Parcelable to send custom data
            // set default parameter to NotFound, if there is some thing wrong with xml parse
            // "Not Found" should be displayed to user
            DocumentBuilder domBuilder = null;
            ExpandableListHeader expandableListHeader = null;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    domBuilder = factory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    Log.d("ParserException", e.toString());
                }
                File fileToParse = new File(this.mFilePath);
                // 20161125: pass file directory to fragment to load more data
                String fileDirectory = fileToParse.getAbsolutePath();
                String imagePath = fileDirectory.substring(0, fileDirectory.lastIndexOf('.'));
                Document dom = domBuilder.parse(fileToParse);
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

                expandableListHeader = constructHeader(instancedRef, associatedAttachmentRefs, occurrenceRefs, imagePath, occurrence, associatedAttachment
                        , designRevision, productRevision, form);
            } catch (Exception e) {
                System.out.println(e);
            }
            return expandableListHeader;
        }

        protected void onPostExecute(ExpandableListHeader expandableListHeader) {
            if (expandableListHeader != null && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
                Intent intent = new Intent(OpenFileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("com.ExpandableListData", expandableListHeader);
                setResult(Activity.RESULT_OK, intent);
                this.mContext.startActivity(intent);
            }
        }

        private ExpandableListHeader constructHeader(String instancedRef, String associatedAttachmentRefs, String occurrenceRefs, String imagePath,
                                                     NodeList occurrence, NodeList associatedAttachment, NodeList designRevision, NodeList productRevision, NodeList form) {
            String partName = "NotFound";
            String partNr = "NotFound";
            String orderNr = "NotFound";
            String inspector = "NotFound";
            String inspectorDate = "NotFound";
            String vehicle = "NotFound";
            String inspectorTimeSpan = "NotFound";
            String frequency = "NotFound";
            String inspectorMethod = "NotFound";
            String inspectorScope = "NotFound";
            String inspectorNorm = "NotFound";
            // item type for header
            String type = "NotFound";
            String formRole = "IMAN_master_form";
            // get part name
            if (notNullAndEmpty(instancedRef)) {
                String id4ProductRevision = instancedRef.substring(1);
                for (int l = 0; l < productRevision.getLength(); ++l) {
                    Element eleProRev = (Element) productRevision.item(l);
                    String idRevision = eleProRev.getAttribute("id");
                    if (notNullAndEmpty(id4ProductRevision)) {
                        if (id4ProductRevision.trim().equalsIgnoreCase(idRevision.trim())) {
                            partName = eleProRev.getAttribute("name");
                            type = eleProRev.getAttribute("subType");
                            break;
                        }
                    }
                }
            }
            String[] associatedAttachmentIds = null;
            if (notNullAndEmpty(associatedAttachmentRefs))
                associatedAttachmentIds = associatedAttachmentRefs.split("#");

            String associatedAttachmentRole = null;
            String associateAttachmentId = null;
            String attachmentRef = null;
            String childRefs = null;
            if (associatedAttachmentIds.length > 0) {
                IdLoop:
                for (String id : associatedAttachmentIds) {
                    for (int k = 0; k < associatedAttachment.getLength(); ++k) {
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
            if (notNullAndEmpty(attachmentRef))
                attachmentRef = attachmentRef.substring(1);

            formLoop:
            for (int k = 0; k < form.getLength(); ++k) {
                Element eleForm = (Element) form.item(k);
                if (notNullAndEmpty(attachmentRef)) {
                    if (attachmentRef.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                        partNr = eleForm.getAttribute("name");
                        NodeList nodeOfForm = eleForm.getElementsByTagName("UserValue");
                        for (int i = 0; i < nodeOfForm.getLength(); i++) {
                            Element eleNode = (Element) nodeOfForm.item(i);
                            String nodeTitle = eleNode.getAttribute("title");
                            switch (nodeTitle) {
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
            String idOfForm4MoreAttri = null;
            if (notNullAndEmpty(childRefs))
                idOfForm4MoreAttri = findId4MoreAttri(childRefs, associatedAttachment);

            // use id of form to find more attribute
            if (notNullAndEmpty(idOfForm4MoreAttri)) {
                for (int k = 0; k < form.getLength(); ++k) {
                    Element eleForm = (Element) form.item(k);
                    // better to use TRIM() for sure
                    if (idOfForm4MoreAttri.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
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
            List<ExpandableListItem> childOfOccurrence = getChildOccurrenceList(occurrenceRefs, formRole, occurrence, associatedAttachment,
                    productRevision, designRevision, form);
            return new ExpandableListHeader.Builder()
                    .setPartName(partName)
                    .setPartNr(partNr)
                    .setOrderNr(orderNr)
                    .setInspector(inspector)
                    .setInspectorDate(inspectorDate)
                    .setVehicle(vehicle)
                    .setInspectorTimeSpan(inspectorTimeSpan)
                    .setFrequency(frequency)
                    .setType(type)
                    .setInspectorMethod(inspectorMethod)
                    .setInspectorScope(inspectorScope)
                    .setInspectorNorm(inspectorNorm)
                    .setFileDirectory(imagePath)
                    .setChildOfOccurrenceList(childOfOccurrence)
                    .build();
        }

        private String findId4MoreAttri(String childRefs, NodeList associatedAttachment) {
            String idOfForm4MoreAttri = null;
            // first spilt string is empty
            // idFindAttachment contains blank!!!!!, use TRIM() for sure
            String id2FindAttachment = childRefs.split("#")[1];
            for (int k = 0; k < associatedAttachment.getLength(); ++k) {
                Element eleAssociated = (Element) associatedAttachment.item(k);
                // idCompareTo contains blank
                String idCompareTo = eleAssociated.getAttribute("id");
                if (notNullAndEmpty(idCompareTo)) {
                    if (idCompareTo.trim().equalsIgnoreCase(id2FindAttachment.trim())) {
                        idOfForm4MoreAttri = eleAssociated.getAttribute("attachmentRef");
                        if (notNullAndEmpty(idOfForm4MoreAttri)) {
                            idOfForm4MoreAttri = idOfForm4MoreAttri.substring(1);
                            break;
                        }
                    }
                }
            }
            return idOfForm4MoreAttri;
        }

        private List<ExpandableListItem> getChildOccurrenceList(String occurrenceRefs, String formRole, NodeList occurrence, NodeList associatedAttachment,
                                                                NodeList productRevision, NodeList designRevision, NodeList form) {
            List<ExpandableListItem> childOfOccurrence = new ArrayList<>();
            // 20160826: move on with child occurrence
            String[] idsOfChildOccu = null;
            if (notNullAndEmpty(occurrenceRefs)) {
                idsOfChildOccu = occurrenceRefs.split(" ");
            }
            // find child occurrence and create ExpandableListItem
            // 20160829: child of child occurrence found and create WeldPoint
            for (String id : idsOfChildOccu) {
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
                for (int k = 0; k < occurrence.getLength(); ++k) {
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
                    for (int k = 0; k < designRevision.getLength(); ++k) {
                        Element eleDesignRevison = (Element) designRevision.item(k);
                        if (idOfRevisions.trim().equalsIgnoreCase(eleDesignRevison.getAttribute("id").trim())) {
                            itemName = eleDesignRevison.getAttribute("name");
                            itemType = eleDesignRevison.getAttribute("subType");
                            if (itemName != null && itemType != null) itemFound = true;
                        }
                    }
                    if (!itemFound) {
                        for (int k = 0; k < productRevision.getLength(); ++k) {
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
                    String idOfForm4ItemNr = null;
                    childAssocitedLoop:
                    for (String idOfChild : idsOfChildAttachment) {
                        for (int k = 0; k < associatedAttachment.getLength(); ++k) {
                            Element eleChildAssociated = (Element) associatedAttachment.item(k);
                            if (idOfChild.trim().equalsIgnoreCase(eleChildAssociated.getAttribute("id").trim())
                                    && formRole.equalsIgnoreCase(eleChildAssociated.getAttribute("role"))) {
                                idOfForm4ItemNr = eleChildAssociated.getAttribute("attachmentRef");
                                break childAssocitedLoop;
                            }
                        }
                    }
                    // now use forms id to find form and then get item number
                    if (notNullAndEmpty(idOfForm4ItemNr)) {
                        String idSplitted = idOfForm4ItemNr.split("#")[1];
                        for (int k = 0; k < form.getLength(); k++) {
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
                    for (String id2FindWeldPoint : ids) {
                        if (notNullAndEmpty(id2FindWeldPoint)) {
                            String name = null;
                            String joints_itemType = null;
                            WeldPoint weldPoint = null;
                            // 20161021: associated attachments to find characters
                            String associatedARs = null;
                            occuLoop:
                            for (int k = 0; k < occurrence.getLength(); k++) {
                                Element eleOccu = (Element) occurrence.item(k);
                                if (id2FindWeldPoint.trim().equalsIgnoreCase(eleOccu.getAttribute("id").trim())) {
                                    associatedARs = eleOccu.getAttribute("associatedAttachmentRefs");
                                    NodeList nodes = eleOccu.getElementsByTagName("UserValue");
                                    for (int i = 0; i < nodes.getLength(); i++) {
                                        Element eleNode = (Element) nodes.item(i);
                                        String nodeTitle = eleNode.getAttribute("title");
                                        if (nodeTitle.equalsIgnoreCase("OccurrenceName")) {
                                            name = eleNode.getAttribute("value");
                                            break occuLoop;
                                        }
                                    }
                                }
                            }
                            String id4Form = null;
                            // check associated attachments
                            if (notNullAndEmpty(associatedARs)) {
                                String[] aRs = associatedARs.split("#");
                                attachmentsLoop:
                                for (String id4Character : aRs) {
                                    if (notNullAndEmpty(id4Character)) {
                                        for (int k = 0; k < associatedAttachment.getLength(); ++k) {
                                            Element eleAss = (Element) associatedAttachment.item(k);
                                            if (id4Character.trim().equalsIgnoreCase(eleAss.getAttribute("id").trim())
                                                    && "TC_Feature_Form_Relation".equalsIgnoreCase(eleAss.getAttribute("role"))) {
                                                id4Form = eleAss.getAttribute("attachmentRef");
                                                break attachmentsLoop;
                                            }
                                        }
                                    }
                                }
                            }
                            // 20161021: map which hold all key value paar for wild points
                            Map<String, String> character = new HashMap<>();
                            if (notNullAndEmpty(id4Form)) {
                                String id4FormSplitted = id4Form.split("#")[1];
                                for (int k = 0; k < form.getLength(); ++k) {
                                    Element eleForm = (Element) form.item(k);
                                    if (id4FormSplitted.trim().equalsIgnoreCase(eleForm.getAttribute("id").trim())) {
                                        joints_itemType = eleForm.getAttribute("subType");
                                        NodeList nodes = eleForm.getElementsByTagName("UserValue");
                                        for (int i = 0; i < nodes.getLength(); ++i) {
                                            Element eleNode = (Element) nodes.item(i);
                                            String nodeTitle = eleNode.getAttribute("title");
                                            String nodeValue;
                                            switch (nodeTitle) {
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
                            if (notNullAndEmpty(name) && notNullAndEmpty(joints_itemType)) {
                                String joins_it = joints_itemType.split(" ")[0];
                                weldPoint = new WeldPoint(name, joins_it.trim(), character);
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
            return childOfOccurrence;
        }
    }
}
