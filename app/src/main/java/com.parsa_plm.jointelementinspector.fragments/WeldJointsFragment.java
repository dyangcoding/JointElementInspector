package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.Occurrence;
import com.parsa_plm.Layout.VerticalTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeldJointsFragment extends Fragment {
    private ViewPager mViewPager;
    @BindView(R.id.weldJoints_Crack)
    VerticalTextView mCrack;
    @BindView(R.id.weldJoints_CraterCrack)
    VerticalTextView mCraterCrack;
    @BindView(R.id.weldJoints_SurfacePore)
    VerticalTextView mSurfacePore;
    @BindView(R.id.weldJoints_EndCraterPipe)
    VerticalTextView mEndCraterPipe;
    @BindView(R.id.weldJoints_LackOfFusion)
    VerticalTextView mLackOfFusion;
    @BindView(R.id.weldJoints_IncRootPenetration)
    VerticalTextView mIncRootPenetration;
    @BindView(R.id.weldJoints_ContinousUndercut)
    VerticalTextView mContinousUndercut;
    @BindView(R.id.weldJoints_IntUndercut)
    VerticalTextView mIntUndercut;
    @BindView(R.id.weldJoints_ShrinkGrooves)
    VerticalTextView mShrinkGrooves;
    @BindView(R.id.weldJoints_ExcWeldMetal)
    VerticalTextView mExcWeldMetal;
    @BindView(R.id.weldJoints_ExcConvex)
    VerticalTextView mExcConvex;
    @BindView(R.id.weldJoints_ExcPenetration)
    VerticalTextView mExcPenetration;
    @BindView(R.id.weldJoints_IncWeldToe)
    VerticalTextView mIncWeldToe;
    @BindView(R.id.weldJoints_Overlap)
    VerticalTextView mOverlap;
    @BindView(R.id.weldJoints_Sagging)
    VerticalTextView mSagging;
    @BindView(R.id.weldJoints_BurnThrough)
    VerticalTextView mBurnThrough;
    @BindView(R.id.weldJoints_IncFilledGroove)
    VerticalTextView mIncFilledGroove;
    @BindView(R.id.weldJoints_ExcAsymFilledWeld)
    VerticalTextView mExcAsymFilledWeld;
    @BindView(R.id.weldJoints_RootConcavity)
    VerticalTextView mRootConcavity;
    @BindView(R.id.weldJoints_RootPorosity)
    VerticalTextView mRootPorosity;
    @BindView(R.id.weldJoints_PoorRestart)
    VerticalTextView mPoorRestart;
    @BindView(R.id.weldJoints_InsThroatThick)
    VerticalTextView mInsThroatThick;
    @BindView(R.id.weldJoints_ExcThoratThick)
    VerticalTextView mExcThoratThick;
    @BindView(R.id.weldJoints_ArcStrike)
    VerticalTextView mArcStrike;
    @BindView(R.id.weldJoints_Spatter)
    VerticalTextView mSpatter;
    @BindView(R.id.weldJoints_TempColours)
    VerticalTextView mTempColours;
    @BindView(R.id.tableRow1)
    TableRow mTableRow1;
    private ExpandableListHeader mHeaderData;
    private TextView jointsHeader;
    private ListView mListView;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            mHeaderData = bundle.getParcelable("com.ExpandableListData");
    }

    public WeldJointsFragment() {
    }

    public static WeldJointsFragment newInstance(ExpandableListHeader mHeaderData) {
        WeldJointsFragment weldJointsFragment = new WeldJointsFragment();
        Bundle bundle = new Bundle();
        if (mHeaderData != null)
            bundle.putParcelable("com.ExpandableListData", mHeaderData);
        weldJointsFragment.setArguments(bundle);
        return weldJointsFragment;
    }

    // fast version
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weldjoints, container, false);
        jointsHeader = (TextView) view.findViewById(R.id.weldJointsHeaderText);
        jointsHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        setUpTableRow();
        mListView = (ListView) view.findViewById(R.id.listview);
        List<Occurrence> dataList = null;
        if (mHeaderData != null) {
            List<ExpandableListItem> list = mHeaderData.getChildOfOccurrence();
            for (ExpandableListItem item : list) {
                if (item.getChildItemList() != null) {
                    dataList = item.getChildItemList();
                }
            }
        }
        WeldPointAdapter wa = new WeldPointAdapter(dataList, getContext());
        mListView.setAdapter(wa);
        setListViewHeightBasedOnChildren(mListView);
        setViewPager();
        setUpListItemClick();
        ButterKnife.bind(this, view);
        return view;
    }
    // 20170221: on item click should change current view to visual view pager,
    // we need reference to viewpager from main activity
    private void setUpListItemClick() {
        mListView.setClickable(true);
        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
                if (mViewPager != null)
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        });
    }

    // soll Werte should be set from xml file
    private void setUpTableRow() {
    }
    // 20170210: view.measure is now given correct value to make list height suitable
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setViewPager() {
        ProductStructureFragment fragment = (ProductStructureFragment) getParentFragment();
        if (fragment != null)
            mViewPager = fragment.getViewPagerFromOverview();
    }
}
