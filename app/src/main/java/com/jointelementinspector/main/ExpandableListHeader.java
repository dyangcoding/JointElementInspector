package com.jointelementinspector.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListHeader implements Parcelable{
    private String partName;
    private String partNr;
    private String orderNr;
    private String inspector;
    private String inspectorDate;
    private String vehicle;
    private String inspectorTimeSpan;
    private String frequency;
    // 20161007 inspector method
    private String inspectorMethod;
    // 20160829: type for header
    private String type;
    // this list contains child items of occurrence
    // 20160824: class ExpandablelistData changed to ExpandableListHeader, which represents the header data
    // and contains a list von ExpandableListItem, which represents the item of the list structure
    private List<ExpandableListItem> childOfOccurrence;
    // new class for weld point for later user, maybe use generic Type later
    // no longer used
    // private List<WeldPoint> weldPointsOfOccurrence;

    /*expandable list data constructor, later may changed
      empty string accepted, not null
     */
    public ExpandableListHeader(String partName, String partNr, String orderNr, String inspector,
                                String inspectorDate, String vehicle, String inspectorTimeSpan, String frequency, String type, String inspectorMethod, List<ExpandableListItem> childOfOccurrence) {
        this.partName = partName;
        this.partNr = partNr;
        this.orderNr = orderNr;
        this.inspector = inspector;
        this.inspectorDate = inspectorDate;
        this.vehicle = vehicle;
        this.inspectorTimeSpan = inspectorTimeSpan;
        this.frequency = frequency;
        // 20160829 typ child occurrence added
        this.type = type;
        this.inspectorMethod = inspectorMethod;
        this.childOfOccurrence = childOfOccurrence;
    }

    public static final Parcelable.Creator<ExpandableListHeader> CREATOR = new Parcelable.Creator<ExpandableListHeader>(){
        @Override
        public ExpandableListHeader createFromParcel(Parcel parcel) {
            return new ExpandableListHeader(parcel);
        }

        @Override
        public ExpandableListHeader[] newArray(int i) {
            return new ExpandableListHeader[i];
        }
    };

    public ExpandableListHeader(Parcel parcel) {
        this.partName = parcel.readString();
        this.partNr = parcel.readString();
        this.orderNr = parcel.readString();
        this.inspector = parcel.readString();
        this.inspectorDate = parcel.readString();
        this.vehicle = parcel.readString();
        this.inspectorTimeSpan = parcel.readString();
        this.frequency = parcel.readString();
        // 20160829 typ added
        this.type = parcel.readString();
        this.inspectorMethod = parcel.readString();
        // local variable for child occurrence
        // not sure about this if it working
        List<ExpandableListItem> childItem = new ArrayList<>();
        parcel.readTypedList(childItem, ExpandableListItem.CREATOR);
        this.childOfOccurrence = childItem;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.partName);
        parcel.writeString(this.partNr);
        parcel.writeString(this.orderNr);
        parcel.writeString(this.inspector);
        parcel.writeString(this.inspectorDate);
        parcel.writeString(this.vehicle);
        parcel.writeString(this.inspectorTimeSpan);
        parcel.writeString(this.frequency);
        // 20160829 typ added
        parcel.writeString(this.type);
        parcel.writeString(this.inspectorMethod);
        parcel.writeTypedList(this.childOfOccurrence);
    }

    public String getPartName() {
        return this.partName;
    }

    public String getPartNr() {
        return this.partNr;
    }

    public String getOrderNr() {
        return this.orderNr;
    }

    public String getInspector() {
        return this.inspector;
    }

    public String getInspectorDate() {
        return this.inspectorDate;
    }

    public String getVehicle() {
        return this.vehicle;
    }

    public String getInspectorTimeSpan() {
        return this.inspectorTimeSpan;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public String getType() { return this.type; }

    public String getInspectorMethod() {return this.inspectorMethod; }
    public List<ExpandableListItem> getChildOfOccurrence() { return this.childOfOccurrence; }
    @Override
    public String toString() {
        return "Part Name: " + getPartName() + "Part Nr: " + getPartNr() + "Order Nr:" + getOrderNr() + "Inspector: "
                + getInspector() + "Inspector Date: " + getInspectorDate() + "Vehicle: " + getVehicle() + "Inspector Time Span"
                + getInspectorTimeSpan() + "Frequency: " + getFrequency() + "Type: " + getType() + "InspectorMethod" + getInspectorMethod();
    }
}
