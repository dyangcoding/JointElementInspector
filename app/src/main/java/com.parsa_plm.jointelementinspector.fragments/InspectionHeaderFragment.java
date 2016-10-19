package com.parsa_plm.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.ExpandableListHeader;

public class InspectionHeaderFragment extends Fragment {
    private ExpandableListHeader headerData;
    // 20161014: row header for expand
    private TextView rowHeader;
    private ImageView rowHeaderIcon;
    private TextView partName;
    private TextView partNr;
    private TextView orderNr;
    private TextView inspector;
    private TextView inspectorDate;
    private TextView vehicle;
    private TextView inspectorTimeSpan;
    private TextView frequency;
    // 20161007: add inspector method
    private TextView inspectorMethod;
    // 20161008: add inspector Scope and Norm
    private TextView inspectorScope;
    private TextView inspectorNorm;
    // 20161014: table row
    private TableRow row1;
    private TableRow row2;
    private TableRow row3;
    private TableRow row4;
    private TableRow row5;
    private boolean isVisible = true;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public InspectionHeaderFragment() {
    }

    public static InspectionHeaderFragment newInstance(ExpandableListHeader headerData) {
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
        final View headerView = inflater.inflate(R.layout.fragment_inspectionheader, container, false);
        rowHeader = (TextView) headerView.findViewById(R.id.RowHeaderInfo);
        rowHeaderIcon = (ImageView) headerView.findViewById(R.id.RowHeaderIcon);
        partName = (TextView) headerView.findViewById(R.id.header_partName);
        partNr = (TextView) headerView.findViewById(R.id.header_partNr);
        orderNr = (TextView) headerView.findViewById(R.id.header_orderNr);
        inspector = (TextView) headerView.findViewById(R.id.header_inspector);
        inspectorDate = (TextView) headerView.findViewById(R.id.header_inspectorDate);
        vehicle = (TextView) headerView.findViewById(R.id.header_vehicle);
        inspectorTimeSpan = (TextView) headerView.findViewById(R.id.header_inspectorTimeSpan);
        frequency = (TextView) headerView.findViewById(R.id.header_frequency);
        inspectorMethod = (TextView) headerView.findViewById(R.id.header_inspectorMethod);
        inspectorScope = (TextView) headerView.findViewById(R.id.header_inspectorScope);
        inspectorNorm = (TextView) headerView.findViewById(R.id.header_inspectorNorm);
        row1 = (TableRow) headerView.findViewById(R.id.tableRow1);
        row2 = (TableRow) headerView.findViewById(R.id.tableRow2);
        row3 = (TableRow) headerView.findViewById(R.id.tableRow3);
        row4 = (TableRow) headerView.findViewById(R.id.tableRow4);
        row5 = (TableRow) headerView.findViewById(R.id.tableRow5);
        if (headerData != null) {
            rowHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            rowHeader.setText(ROWHEADER);
            rowHeaderIcon.setImageResource(R.drawable.collapsearrow48);
            rowHeaderIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (row2 != null && row3 != null && row4 != null && row5 != null) {
                        if (isVisible) {
                            rowHeaderIcon.setImageResource(R.drawable.expandarrow48);
                            row2.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                            row2.setVisibility(View.GONE);
                            row3.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                            row3.setVisibility(View.GONE);
                            row4.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                            row4.setVisibility(View.GONE);
                            row5.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                            row5.setVisibility(View.GONE);
                            isVisible = false;

                        }else {
                            rowHeaderIcon.setImageResource(R.drawable.collapsearrow48);
                            row2.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                            row2.setVisibility(View.VISIBLE);
                            row3.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                            row3.setVisibility(View.VISIBLE);
                            row4.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                            row4.setVisibility(View.VISIBLE);
                            row5.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                            row5.setVisibility(View.VISIBLE);
                            isVisible = true;
                        }
                    }
                }
            });
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
        return headerView;
    }
}
