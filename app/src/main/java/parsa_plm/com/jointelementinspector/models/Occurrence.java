package parsa_plm.com.jointelementinspector.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * this class contains weld points infos which could be edited
 * 20160826: test class version, more attribute later
 * 20170203: we change now the class typ to occurrence because we have now Act Weld as child occurrence
 * and it should make no difference for parent expand adapter to show child view
 */
public class Occurrence implements Parcelable{
    private String mName;
    // 20161028: item type needed
    private String mItemType;
    // 20161021: this map hold key value in form (attributes, value)
    // needed to be initialized
    private Map<String, String> mCharacter = new HashMap<>();

    // this is a test constructor, later more attribute
    public Occurrence(String name, String itemType, Map<String, String> character)
    {
        this.mName = name;
        this.mItemType = itemType;
        this.mCharacter = character;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName);
        parcel.writeString(this.mItemType);
        // so this is test version, do not know if it works
        parcel.writeInt(this.mCharacter.size());
        for (Map.Entry<String, String> entry: this.mCharacter.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }
    }

    public static final Parcelable.Creator<Occurrence> CREATOR = new Parcelable.Creator<Occurrence>(){
        @Override
        public Occurrence createFromParcel(Parcel parcel) {
            return new Occurrence(parcel);
        }

        @Override
        public Occurrence[] newArray(int i) {
            return new Occurrence[i];
        }
    };

    private Occurrence(Parcel parcel)
    {
        this.mName = parcel.readString();
        this.mItemType = parcel.readString();
        // map size
        int size = parcel.readInt();
        for (int k = 0; k < size; ++k) {
            String key = parcel.readString();
            String value = parcel.readString();
            this.mCharacter.put(key, value);
        }
    }

    public String getName() { return this.mName; }
    public String getItemType() { return this.mItemType; }
    public Map<String, String> getCharacter() { return this.mCharacter; }
    // this to String should be expanded later
    @Override
    public String toString() {
        return "Weld Point Name: " + this.mName + "Weld Point Item type" + this.mItemType;
    }
}
