package com.parsa_plm.jointelementinspector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jointelementinspector.jointelementinspector.R;
import com.parsa_plm.folderLayout.ExpandableListData;

public class InspectionHeaderFragment extends Fragment{
    private ExpandableListData headerData;
    private TextView partName = null;
    private TextView partNr = null;
    private TextView orderNr = null;
    private TextView inspector = null;
    private TextView inspectorDate = null;
    private TextView vehicle = null;
    private TextView inspectorTimeSpan = null;
    private TextView frequency = null;
    private static final String PART_NAME = "Part Name: ";
    private static final String PART_Nr = "Part Number: ";
    private static final String ORDER_Nr = "Order Number: ";
    private static final String INSPECTOR = "Inspector: ";
    private static final String INSPECTOR_DATE = "Inspector Date: ";
    private static final String VEHICLE = "Vehicle: ";
    private static final String INSPECTOR_TIMESPAN = "Inspector Time Span: ";
    private static final String FREQUENCY = "Frequency: ";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public InspectionHeaderFragment(){}
    public static InspectionHeaderFragment newInstance(ExpandableListData headerData){
        InspectionHeaderFragment headerFragment = new InspectionHeaderFragment();
        Bundle bundle = new Bundle();
        if (headerData != null) {
            bundle.putParcelable("com.ExpandableListData", headerData);
        }
        headerFragment.setArguments(bundle);
        return headerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspectionheader, container, false);
        partName = (TextView) view.findViewById(R.id.header_partName);
        partNr = (TextView) view.findViewById(R.id.header_partNr);
        orderNr = (TextView) view.findViewById(R.id.header_orderNr);
        inspector = (TextView) view.findViewById(R.id.header_inspector);
        inspectorDate = (TextView) view.findViewById(R.id.header_inspectorDate);
        vehicle = (TextView) view.findViewById(R.id.header_vehicle);
        inspectorTimeSpan = (TextView) view.findViewById(R.id.header_inspectorTimeSpan);
        frequency = (TextView) view.findViewById(R.id.header_frequency);
        if (headerData != null) {
            partName.setText(PART_NAME + headerData.getPartName());
            partNr.setText(PART_Nr + headerData.getPartNr());
            orderNr.setText(ORDER_Nr + headerData.getOrderNr());
            inspector.setText(INSPECTOR + headerData.getInspector());
            inspectorDate.setText(INSPECTOR_DATE + headerData.getInspectorDate());
            vehicle.setText(VEHICLE + headerData.getVehicle());
            inspectorTimeSpan.setText(INSPECTOR_TIMESPAN + headerData.getInspectorTimeSpan());
            frequency.setText(FREQUENCY + headerData.getFrequency());
        }
        return view;
    }
}
