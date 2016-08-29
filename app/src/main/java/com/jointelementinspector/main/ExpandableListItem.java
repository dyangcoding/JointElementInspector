package com.jointelementinspector.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*
    this class represents items of the list structure, also child occurrence, which been displayed
    in fragment ProductStructure and contains a list of class named weld points, this class could
    be different later, e.g weld seam. muss be parcelable for data transport
 */
public class ExpandableListItem implements Parcelable {
    // attribute represents part name
    private String itemName;
    // attribute represents part Nr
    private String itemNr;
    // attribute represents Type
    private String itemType;
    // list of child items, could be different class, null value excepted
    private List<WeldPoint> itemOfChild;

    // null value excepted for itemOfChild
    public ExpandableListItem(String itemName, String itemNr, String itemType, List<WeldPoint> itemOfChild) {
        this.itemName = itemName;
        this.itemNr = itemNr;
        this.itemType = itemType;
        this.itemOfChild = itemOfChild;
    }

    public static final Parcelable.Creator<ExpandableListItem> CREATOR = new Parcelable.Creator<ExpandableListItem>(){
        @Override
        public ExpandableListItem createFromParcel(Parcel parcel) {
            return new ExpandableListItem(parcel);
        }

        @Override
        public ExpandableListItem[] newArray(int i) {
            return new ExpandableListItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.itemName);
        parcel.writeString(this.itemNr);
        parcel.writeString(this.itemType);
        parcel.writeTypedList(this.itemOfChild);
    }

    public ExpandableListItem(Parcel parcel) {
        this.itemName = parcel.readString();
        this.itemNr = parcel.readString();
        this.itemType = parcel.readString();
        // place holder for child item list
        List<WeldPoint> itemList = new ArrayList<>();
        parcel.readTypedList(itemList, WeldPoint.CREATOR);
        this.itemOfChild = itemList;
    }

    public String getItemName(){ return this.itemName; }

    public String getItemNr() { return this.itemNr; }

    public String getItemType() {return this.itemType; }

    public List<WeldPoint> getChildItemList() { return this.itemOfChild; }
}
