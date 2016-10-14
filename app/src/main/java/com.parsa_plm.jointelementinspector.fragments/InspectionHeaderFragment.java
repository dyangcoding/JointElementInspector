package com.parsa_plm.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.ExpandableListHeader;

public class InspectionHeaderFragment extends Fragment{
    private ExpandableListHeader headerData;
    // 20161014: row header for expand
    private TextView rowHeader = null;
    private ImageView rowHeaderIcon = null;
    private TextView partName = null;
    private TextView partNr = null;
    private TextView orderNr = null;
    private TextView inspector = null;
    private TextView inspectorDate = null;
    private TextView vehicle = null;
    private TextView inspectorTimeSpan = null;
    private TextView frequency = null;
    // 20161007: add inspector method
    private TextView inspectorMethod = null;
    // 20161008: add inspector Scope and Norm
    private TextView inspectorScope = null;
    private TextView inspectorNorm = null;
    // 20161014: add header Info for expand
    private static final String ROWHEADER = "Inspector Header Information";
    private static final String PART_NAME = "Part Name: ";
    private static final String PART_Nr = "Part Number: ";
    private static final String ORDER_Nr = "Order Number: ";
    private static final String INSPECTOR = "Inspector: ";
    private static final String INSPECTOR_DATE = "Inspector Date: ";
    private static final String VEHICLE = "Vehicle: ";
    private static final String INSPECTOR_TIMESPAN = "Inspector Time Span: ";
    private static final String FREQUENCY = "Frequency: ";
    private static final String INSPECTOR_METHOD = "Inspector Method: ";
    private static final String INSPECTOR_SCOPE = "Inspector Scope: ";
    private static final String INSPECTOR_NORM = "Inspector Norm: ";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public InspectionHeaderFragment(){}
    public static InspectionHeaderFragment newInstance(ExpandableListHeader headerData){
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
        rowHeader = (TextView) view.findViewById(R.id.RowHeaderInfo);
        rowHeaderIcon = (ImageView) view.findViewById(R.id.RowHeaderIcon);
        partName = (TextView) view.findViewById(R.id.header_partName);
        partNr = (TextView) view.findViewById(R.id.header_partNr);
        orderNr = (TextView) view.findViewById(R.id.header_orderNr);
        inspector = (TextView) view.findViewById(R.id.header_inspector);
        inspectorDate = (TextView) view.findViewById(R.id.header_inspectorDate);
        vehicle = (TextView) view.findViewById(R.id.header_vehicle);
        inspectorTimeSpan = (TextView) view.findViewById(R.id.header_inspectorTimeSpan);
        frequency = (TextView) view.findViewById(R.id.header_frequency);
        inspectorMethod = (TextView) view.findViewById(R.id.header_inspectorMethod);
        inspectorScope = (TextView) view.findViewById(R.id.header_inspectorScope);
        inspectorNorm = (TextView) view.findViewById(R.id.header_inspectorNorm);
        if (headerData != null) {
            rowHeader.setText(ROWHEADER);
            rowHeaderIcon.setImageResource(R.drawable.expandarrow48);
            partName.setText(PART_NAME + headerData.getPartName());
            partNr.setText(PART_Nr + headerData.getPartNr());
            orderNr.setText(ORDER_Nr + headerData.getOrderNr());
            inspector.setText(INSPECTOR + headerData.getInspector());
            inspectorDate.setText(INSPECTOR_DATE + headerData.getInspectorDate());
            vehicle.setText(VEHICLE + headerData.getVehicle());
            inspectorTimeSpan.setText(INSPECTOR_TIMESPAN + headerData.getInspectorTimeSpan());
            frequency.setText(FREQUENCY + headerData.getFrequency());
            inspectorMethod.setText(INSPECTOR_METHOD + headerData.getInspectorMethod());
            inspectorScope.setText(INSPECTOR_SCOPE + headerData.getInspectorScope());
            inspectorNorm.setText(INSPECTOR_NORM + headerData.getInspectorNorm());
        }
        return view;
    }
}
