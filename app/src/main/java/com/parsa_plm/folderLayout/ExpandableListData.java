package com.parsa_plm.folderLayout;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ExpandableListData implements Parcelable{
    private String partName;
    private String partNr;
    private String orderNr;
    private String inspector;
    private String inspectorDate;
    private String vehicle;
    private String inspectorTimeSpan;
    private String frequency;
    // item Typ
    private String itemType;
    // this list contains child items of occurrence
    private List<ExpandableListData> childOfOccurrence;
    // new class for weld point for later user
    private List<WeldPoint> weldPointsOfOccurrence;

    /*expandable list data constructor, later may changed
      empty string accepted, not null
     */
    public ExpandableListData(String partName, String partNr, String orderNr, String inspector,
                              String inspectorDate, String vehicle, String inspectorTimeSpan, String frequency) {
        this.partName = partName;
        this.partNr = partNr;
        this.orderNr = orderNr;
        this.inspector = inspector;
        this.inspectorDate = inspectorDate;
        this.vehicle = vehicle;
        this.inspectorTimeSpan = inspectorTimeSpan;
        this.frequency = frequency;
    }

    public static final Parcelable.Creator<ExpandableListData> CREATOR = new Parcelable.Creator<ExpandableListData>(){
        @Override
        public ExpandableListData createFromParcel(Parcel parcel) {
            return new ExpandableListData(parcel);
        }

        @Override
        public ExpandableListData[] newArray(int i) {
            return new ExpandableListData[i];
        }
    };

    public ExpandableListData(Parcel parcel) {
        this.partName = parcel.readString();
        this.partNr = parcel.readString();
        this.orderNr = parcel.readString();
        this.inspector = parcel.readString();
        this.inspectorDate = parcel.readString();
        this.vehicle = parcel.readString();
        this.inspectorTimeSpan = parcel.readString();
        this.frequency = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getPartName());
        parcel.writeString(getPartNr());
        parcel.writeString(getOrderNr());
        parcel.writeString(getInspector());
        parcel.writeString(getInspectorDate());
        parcel.writeString(getVehicle());
        parcel.writeString(getInspectorTimeSpan());
        parcel.writeString(getFrequency());
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
    @Override
    public String toString() {
        return "Part Name: " + getPartName() + "Part Nr: " + getPartNr() + "Order Nr:" + getOrderNr() + "Inspector: "
                + getInspector() + "Inspector Date: " + getInspectorDate() + "Vehicle: " + getVehicle() + "Inspector Time Span"
                + getInspectorTimeSpan() + "Frequency: " + getFrequency();
    }
}
