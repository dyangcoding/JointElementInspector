package parsa_plm.com.jointelementinspector.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    // 20161008 inspector scope and norm
    private String inspectorScope;
    private String inspectorNorm;
    // 20160829: type for header
    private String type;
    // 20161125: file directory to be passed in fragment for further data access
    private String fileDirectory;
    // this list contains child items of occurrence
    // 20160824: class ExpandablelistData changed to ExpandableListHeader, which represents the header data
    // and contains a list von ExpandableListItem, which represents the item of the list structure
    private List<ExpandableListItem> childOfOccurrence;
    // new class for weld point for later user, maybe use generic Type later
    // no longer used
    // private List<Occurrence> weldPointsOfOccurrence;
    // 20161214: use Builder Pattern now
    public static class Builder{
        private String partName;
        private String partNr;
        private String orderNr;
        private String inspector;
        private String inspectorDate;
        private String vehicle;
        private String inspectorTimeSpan;
        private String frequency;
        private String inspectorMethod;
        private String inspectorScope;
        private String inspectorNorm;
        private String type;
        private String fileDirectory;
        private List<ExpandableListItem> childOfOccurrence;

        public ExpandableListHeader build() {
            return new ExpandableListHeader(this);
        }
        public Builder setPartName(String partName){
            this.partName = partName;
            return this;
        }
        public Builder setPartNr(String partNr) {
            this.partNr = partNr;
            return this;
        }
        public Builder setOrderNr(String orderNr) {
            this.orderNr = orderNr;
            return this;
        }
        public Builder setInspector(String inspector) {
            this.inspector = inspector;
            return this;
        }
        public Builder setInspectorDate(String inspectorDate) {
            this.inspectorDate = inspectorDate;
            return this;
        }
        public Builder setVehicle(String vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public Builder setInspectorTimeSpan(String inspectorTimeSpan) {
            this.inspectorTimeSpan = inspectorTimeSpan;
            return this;
        }
        public Builder setFrequency(String frequency) {
            this.frequency = frequency;
            return this;
        }
        public Builder setInspectorMethod(String inspectorMethod) {
            this.inspectorMethod = inspectorMethod;
            return this;
        }
        public Builder setInspectorScope(String inspectorScope) {
            this.inspectorScope = inspectorScope;
            return this;
        }
        public Builder setInspectorNorm(String inspectorNorm) {
            this.inspectorNorm = inspectorNorm;
            return this;
        }
        public Builder setType(String type) {
            this.type = type;
            return this;
        }
        public Builder setFileDirectory(String fileDirectory) {
            this.fileDirectory = fileDirectory;
            return this;
        }
        public Builder setChildOfOccurrenceList(List<ExpandableListItem> childOfOccurrence) {
            this.childOfOccurrence = childOfOccurrence;
            return this;
        }
    }
    /*expandable list data constructor, later may changed
      empty string accepted, not null
      20161214: change to builder pattern
     */
    private ExpandableListHeader(Builder builder) {
        this.partName = builder.partName;
        this.partNr = builder.partNr;
        this.orderNr = builder.orderNr;
        this.inspector = builder.inspector;
        this.inspectorDate = builder.inspectorDate;
        this.vehicle = builder.vehicle;
        this.inspectorTimeSpan = builder.inspectorTimeSpan;
        this.frequency = builder.frequency;
        // 20160829 typ child occurrence added
        this.type = builder.type;
        this.inspectorMethod = builder.inspectorMethod;
        this.inspectorScope = builder.inspectorScope;
        this.inspectorNorm = builder.inspectorNorm;
        this.fileDirectory = builder.fileDirectory;
        this.childOfOccurrence = builder.childOfOccurrence;
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
        this.inspectorScope = parcel.readString();
        this.inspectorNorm = parcel.readString();
        this.fileDirectory = parcel.readString();
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
        parcel.writeString(this.inspectorScope);
        parcel.writeString(this.inspectorNorm);
        parcel.writeString(this.fileDirectory);
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
    public String getInspector() { return this.inspector; }
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
    public String getInspectorScope() {return this.inspectorScope; }
    public String getInspectorNorm() {return this.inspectorNorm; }
    public String getFileDirectory() {return this.fileDirectory; }
    public String getInspectorMethod() {return this.inspectorMethod; }
    public List<ExpandableListItem> getChildOfOccurrence() { return this.childOfOccurrence; }

    public void objToJsonFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(this.fileDirectory + File.separator + new Date() +"_Parsa_plm.json"), this);
    }

    public String objToJsonString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
    @Override
    public String toString() {
        return "Part Name: " + getPartName() + "Part Nr: " + getPartNr() + "Order Nr:" + getOrderNr() + "Inspector: "
                + getInspector() + "Inspector Date: " + getInspectorDate() + "Vehicle: " + getVehicle() + "Inspector Time Span"
                + getInspectorTimeSpan() + "Frequency: " + getFrequency() + "Type: " + getType()
                + "Inspector Method: " + getInspectorMethod() + "Inspector Scope: " + getInspectorScope()
                + "Inspector Norm: " + getInspectorNorm() + "File Directory: " + getFileDirectory();
    }
}
