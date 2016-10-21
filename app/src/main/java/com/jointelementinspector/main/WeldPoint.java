package com.jointelementinspector.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * this class contains weld points infos which could be edited
 * 20160826: test class version, more attribute later
 */
public class WeldPoint implements Parcelable{
    private String mName;
    // 20161021: this map hold key value in form (attributes, value)
    // needed to be initialized
    private Map<String, String> mCharacter = new HashMap<>();

    // this is a test constructor, later more attribute
    public WeldPoint(String name, Map<String, String> character)
    {
        this.mName = name;
        this.mCharacter = character;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName);
        // so this is test version, do not know if it works
        parcel.writeInt(this.mCharacter.size());
        for (Map.Entry<String, String> entry: this.mCharacter.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }
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

    public WeldPoint(Parcel parcel)
    {
        this.mName = parcel.readString();
        // map size
        int size = parcel.readInt();
        for (int k = 0; k < size; ++k) {
            String key = parcel.readString();
            String value = parcel.readString();
            this.mCharacter.put(key, value);
        }
    }

    public String getName() { return this.mName; }

    public Map<String, String> getCharacter() { return this.mCharacter; }
    // this to String should be expanded later
    @Override
    public String toString() {
        return "Weld Point Name: " + this.mName;
    }
}
