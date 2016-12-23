package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;
import com.parsa_plm.Layout.VerticalTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeldJointsFragment extends Fragment {
    @Bind(R.id.weldJoints_Crack)
    VerticalTextView mCrack;
    @Bind(R.id.weldJoints_CraterCrack)
    VerticalTextView mCraterCrack;
    @Bind(R.id.weldJoints_SurfacePore)
    VerticalTextView mSurfacePore;
    @Bind(R.id.weldJoints_EndCraterPipe)
    VerticalTextView mEndCraterPipe;
    @Bind(R.id.weldJoints_LackOfFusion)
    VerticalTextView mLackOfFusion;
    @Bind(R.id.weldJoints_IncRootPenetration)
    VerticalTextView mIncRootPenetration;
    @Bind(R.id.weldJoints_ContinousUndercut)
    VerticalTextView mContinousUndercut;
    @Bind(R.id.weldJoints_IntUndercut)
    VerticalTextView mIntUndercut;
    @Bind(R.id.weldJoints_ShrinkGrooves)
    VerticalTextView mShrinkGrooves;
    @Bind(R.id.weldJoints_ExcWeldMetal)
    VerticalTextView mExcWeldMetal;
    @Bind(R.id.weldJoints_ExcConvex)
    VerticalTextView mExcConvex;
    @Bind(R.id.weldJoints_ExcPenetration)
    VerticalTextView mExcPenetration;
    @Bind(R.id.weldJoints_IncWeldToe)
    VerticalTextView mIncWeldToe;
    @Bind(R.id.weldJoints_Overlap)
    VerticalTextView mOverlap;
    @Bind(R.id.weldJoints_Sagging)
    VerticalTextView mSagging;
    @Bind(R.id.weldJoints_BurnThrough)
    VerticalTextView mBurnThrough;
    @Bind(R.id.weldJoints_IncFilledGroove)
    VerticalTextView mIncFilledGroove;
    @Bind(R.id.weldJoints_ExcAsymFilledWeld)
    VerticalTextView mExcAsymFilledWeld;
    @Bind(R.id.weldJoints_RootConcavity)
    VerticalTextView mRootConcavity;
    @Bind(R.id.weldJoints_RootPorosity)
    VerticalTextView mRootPorosity;
    @Bind(R.id.weldJoints_PoorRestart)
    VerticalTextView mPoorRestart;
    @Bind(R.id.weldJoints_InsThroatThick)
    VerticalTextView mInsThroatThick;
    @Bind(R.id.weldJoints_ExcThoratThick)
    VerticalTextView mExcThoratThick;
    @Bind(R.id.weldJoints_ArcStrike)
    VerticalTextView mArcStrike;
    @Bind(R.id.weldJoints_Spatter)
    VerticalTextView mSpatter;
    @Bind(R.id.weldJoints_TempColours)
    VerticalTextView mTempColours;
    @Bind(R.id.tableRow1)
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
        List<WeldPoint> dataList = null;
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
        ButterKnife.bind(this, view);
        return view;
    }
    // soll Werte should be set from xml file
    private void setUpTableRow() {
    }

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
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight;
        params.height = 100;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
