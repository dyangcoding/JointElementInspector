package com.parsa_plm.folderLayout;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpandableListData implements Parcelable{
    private String partName;
    private String partNr;
    private String orderNr;
    private String inspector;
    private String inspectorDate;
    private String vehicle;
    private String inspectorTimeSpan;
    private String frequency;

    /*
        expandable list data constructor, later may changed
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
}
