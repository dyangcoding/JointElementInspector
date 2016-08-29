package com.jointelementinspector.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * this class contains weld points infos which could be edited
 * 20160826: test class version, more attribute later
 */
public class WeldPoint implements Parcelable{
    private String name;

    // this is a test constructor, later more attribute
    public WeldPoint(String name) {
        this.name = name;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
    }

    public static final Parcelable.Creator<WeldPoint> CREATOR = new Parcelable.Creator<WeldPoint>(){
        @Override
        public WeldPoint createFromParcel(Parcel parcel) {
            return new WeldPoint(parcel);
        }

        @Override
        public WeldPoint[] newArray(int i) {
            return new WeldPoint[i];
        }
    };

    public WeldPoint(Parcel parcel) {
        this.name = parcel.readString();
    }

    public String getName() { return this.name; }

    // this to String should be expanded later
    @Override
    public String toString() {
        return "Weld Point : " + this.name;
    }
}
