package com.parsa_plm.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InspectionHeaderFragment extends Fragment {
    // 20161023: use butter knife for injection
    @BindView(R.id.RowHeaderInfo)
    TextView mRowHeaderInfo;
    @BindView(R.id.RowHeaderIcon)
    ImageView mRowHeaderIcon;
    @BindView(R.id.header_partName)
    TextView mPartName;
    @BindView(R.id.header_orderNr)
    TextView mOrderNr;
    @BindView(R.id.header_inspector)
    TextView mInspector;
    @BindView(R.id.tableRow2)
    TableRow mTableRow2;
    @BindView(R.id.header_partNr)
    TextView mPartNr;
    @BindView(R.id.header_inspectorDate)
    TextView mInspectorDate;
    @BindView(R.id.header_inspectorTimeSpan)
    TextView mInspectorTimeSpan;
    @BindView(R.id.tableRow3)
    TableRow mTableRow3;
    @BindView(R.id.header_vehicle)
    TextView mVehicle;
    @BindView(R.id.header_frequency)
    TextView mFrequency;
    @BindView(R.id.header_inspectorMethod)
    TextView mInspectorMethod;
    @BindView(R.id.tableRow4)
    TableRow mTableRow4;
    @BindView(R.id.header_inspectorScope)
    TextView mInspectorScope;
    @BindView(R.id.header_inspectorNorm)
    TextView mInspectorNorm;
    @BindView(R.id.tableRow5)
    TableRow mTableRow5;
    private ExpandableListHeader headerData;
    private boolean isVisible = true;
    // 20161023: string builder
    private StringBuilder sb = new StringBuilder();
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
        if (bundle != null)
            headerData = bundle.getParcelable("com.ExpandableListData");
    }

    public InspectionHeaderFragment() {
    }

    public static InspectionHeaderFragment newInstance(ExpandableListHeader headerData) {
        InspectionHeaderFragment headerFragment = new InspectionHeaderFragment();
        Bundle bundle = new Bundle();
        if (headerData != null)
            bundle.putParcelable("com.ExpandableListData", headerData);
        headerFragment.setArguments(bundle);
        return headerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View headerView = inflater.inflate(R.layout.fragment_inspectionheader, container, false);
        ButterKnife.bind(this, headerView);
        if (headerData != null) {
            mRowHeaderInfo.setText(ROWHEADER);
            mRowHeaderIcon.setImageResource(R.drawable.collapsearrow48);
            setUpOnClick();
            fillTextView();
        }
        return headerView;
    }

    private void fillTextView() {
        sb.append(PART_NAME).append(headerData.getPartName());
        mPartName.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(PART_Nr).append(headerData.getPartNr());
        mPartNr.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(ORDER_Nr).append(headerData.getOrderNr());
        mOrderNr.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR).append(headerData.getInspector());
        mInspector.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR_DATE).append(headerData.getInspectorDate());
        mInspectorDate.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(VEHICLE).append(headerData.getVehicle());
        mVehicle.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR_TIMESPAN).append(headerData.getInspectorTimeSpan());
        mInspectorTimeSpan.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(FREQUENCY).append(headerData.getFrequency());
        mFrequency.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR_METHOD).append(headerData.getInspectorMethod());
        mInspectorMethod.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR_SCOPE).append(headerData.getInspectorScope());
        mInspectorScope.setText(sb.toString());
        clearStringBuilder(sb);
        sb.append(INSPECTOR_NORM).append(headerData.getInspectorNorm());
        mInspectorNorm.setText(sb.toString());
        clearStringBuilder(sb);
    }

    private void clearStringBuilder(StringBuilder sb) {
        if (sb != null) {
            sb.setLength(0);
            sb.trimToSize();
        }
    }

    private void setUpOnClick() {
        mRowHeaderIcon.setOnClickListener((view) -> {
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
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
