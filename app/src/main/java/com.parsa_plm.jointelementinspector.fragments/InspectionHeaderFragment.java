package com.parsa_plm.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InspectionHeaderFragment extends Fragment {
    // 20161023: uses butter knife for injection
    @Bind(R.id.RowHeaderInfo)
    TextView mRowHeaderInfo;
    @Bind(R.id.RowHeaderIcon)
    ImageView mRowHeaderIcon;
    @Bind(R.id.tableRow1)
    TableRow mTableRow1;
    @Bind(R.id.header_partName)
    TextView mPartName;
    @Bind(R.id.header_orderNr)
    TextView mOrderNr;
    @Bind(R.id.header_inspector)
    TextView mInspector;
    @Bind(R.id.tableRow2)
    TableRow mTableRow2;
    @Bind(R.id.header_partNr)
    TextView mPartNr;
    @Bind(R.id.header_inspectorDate)
    TextView mInspectorDate;
    @Bind(R.id.header_inspectorTimeSpan)
    TextView mInspectorTimeSpan;
    @Bind(R.id.tableRow3)
    TableRow mTableRow3;
    @Bind(R.id.header_vehicle)
    TextView mVehicle;
    @Bind(R.id.header_frequency)
    TextView mFrequency;
    @Bind(R.id.header_inspectorMethod)
    TextView mInspectorMethod;
    @Bind(R.id.tableRow4)
    TableRow mTableRow4;
    @Bind(R.id.header_inspectorScope)
    TextView mInspectorScope;
    @Bind(R.id.header_inspectorNorm)
    TextView mInspectorNorm;
    @Bind(R.id.tableRow5)
    TableRow mTableRow5;
    private ExpandableListHeader headerData;
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
        ButterKnife.bind(this, headerView);
        if (headerData != null) {
            mRowHeaderInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            mRowHeaderInfo.setText(ROWHEADER);
            mRowHeaderIcon.setImageResource(R.drawable.collapsearrow48);
            setUpOnClick();
            mPartName.setText(PART_NAME + headerData.getPartName());
            mPartNr.setText(PART_Nr + headerData.getPartNr());
            mOrderNr.setText(ORDER_Nr + headerData.getOrderNr());
            mInspector.setText(INSPECTOR + headerData.getInspector());
            mInspectorDate.setText(INSPECTOR_DATE + headerData.getInspectorDate());
            mVehicle.setText(VEHICLE + headerData.getVehicle());
            mInspectorTimeSpan.setText(INSPECTOR_TIMESPAN + headerData.getInspectorTimeSpan());
            mFrequency.setText(FREQUENCY + headerData.getFrequency());
            mInspectorMethod.setText(INSPECTOR_METHOD + headerData.getInspectorMethod());
            mInspectorScope.setText(INSPECTOR_SCOPE + headerData.getInspectorScope());
            mInspectorNorm.setText(INSPECTOR_NORM + headerData.getInspectorNorm());
        }
        return headerView;
    }

    private void setUpOnClick() {
        mRowHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTableRow2 != null && mTableRow3 != null && mTableRow4 != null && mTableRow5 != null) {
                    if (isVisible) {
                        mRowHeaderIcon.setImageResource(R.drawable.expandarrow48);
                        mTableRow2.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        mTableRow2.setVisibility(View.GONE);
                        mTableRow3.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        mTableRow3.setVisibility(View.GONE);
                        mTableRow4.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        mTableRow4.setVisibility(View.GONE);
                        mTableRow5.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                        mTableRow5.setVisibility(View.GONE);
                        isVisible = false;

                    } else {
                        mRowHeaderIcon.setImageResource(R.drawable.collapsearrow48);
                        mTableRow2.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                        mTableRow2.setVisibility(View.VISIBLE);
                        mTableRow3.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                        mTableRow3.setVisibility(View.VISIBLE);
                        mTableRow4.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                        mTableRow4.setVisibility(View.VISIBLE);
                        mTableRow5.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                        mTableRow5.setVisibility(View.VISIBLE);
                        isVisible = true;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
