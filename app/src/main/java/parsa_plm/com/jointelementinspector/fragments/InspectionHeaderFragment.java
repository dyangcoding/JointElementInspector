package parsa_plm.com.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;

import com.jointelementinspector.main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import parsa_plm.com.jointelementinspector.utils.AppConstants;

import static parsa_plm.com.jointelementinspector.utils.AppConstants.PARCELABLE;

public class InspectionHeaderFragment extends Fragment {
    // 20161023: use butter knife
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
    private Unbinder mUnbinder;
    private ExpandableListHeader headerData;
    private boolean isVisible = true;
    // 20161023: string builder
    private StringBuilder sb = new StringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            headerData = bundle.getParcelable(PARCELABLE);
    }
    public InspectionHeaderFragment() {
    }
    public static InspectionHeaderFragment newInstance(ExpandableListHeader headerData) {
        InspectionHeaderFragment headerFragment = new InspectionHeaderFragment();
        Bundle bundle = new Bundle();
        if (headerData != null)
            bundle.putParcelable(PARCELABLE, headerData);
        headerFragment.setArguments(bundle);
        return headerFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View headerView = inflater.inflate(R.layout.fragment_inspectionheader, container, false);
        mUnbinder = ButterKnife.bind(this, headerView);
        return headerView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (headerData != null) {
            setUpOnClick();
            fillTextView();
        }
    }
    private void fillTextView() {
        if (!CheckNullView()) {
            sb.append(AppConstants.PART_NAME).append(headerData.getPartName());
            mPartName.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.PART_Nr).append(headerData.getPartNr());
            mPartNr.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.ORDER_Nr).append(headerData.getOrderNr());
            mOrderNr.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR).append(headerData.getInspector());
            mInspector.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR_DATE).append(headerData.getInspectorDate());
            mInspectorDate.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.VEHICLE).append(headerData.getVehicle());
            mVehicle.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR_TIMESPAN).append(headerData.getInspectorTimeSpan());
            mInspectorTimeSpan.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.FREQUENCY).append(headerData.getFrequency());
            mFrequency.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR_METHOD).append(headerData.getInspectorMethod());
            mInspectorMethod.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR_SCOPE).append(headerData.getInspectorScope());
            mInspectorScope.setText(sb.toString());
            clearStringBuilder(sb);
            sb.append(AppConstants.INSPECTOR_NORM).append(headerData.getInspectorNorm());
            mInspectorNorm.setText(sb.toString());
            clearStringBuilder(sb);
        }
    }
    private void clearStringBuilder(StringBuilder sb) {
        if (sb != null) {
            sb.setLength(0);
            sb.trimToSize();
        }
    }
    private boolean CheckNullView() {
        boolean nullView = false;
        if (mPartName == null || mPartNr == null || mOrderNr == null
                || mInspector == null || mInspectorDate == null || mVehicle == null
                || mInspectorTimeSpan == null || mFrequency == null || mInspectorMethod == null
                || mInspectorScope == null || mInspectorNorm == null) {
            nullView = true;
        }
        return nullView;
    }
    private void setUpOnClick() {
        if (mRowHeaderIcon == null)
            return;
        mRowHeaderIcon.setOnClickListener((view) -> {
            if (mTableRow2 == null || mTableRow3 == null || mTableRow4 == null || mTableRow5 == null)
                return;
            if (isVisible) {
                mRowHeaderIcon.setImageResource(R.mipmap.ic_expand);
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
                mRowHeaderIcon.setImageResource(R.mipmap.ic_collapse);
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
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
