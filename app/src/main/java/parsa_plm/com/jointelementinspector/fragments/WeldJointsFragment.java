package parsa_plm.com.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.models.ExpandableListItem;
import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.models.Occurrence;
import parsa_plm.com.jointelementinspector.customLayout.VerticalTextView;
import parsa_plm.com.jointelementinspector.adapters.WeldPointAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import parsa_plm.com.jointelementinspector.utils.AppConstants;

public class WeldJointsFragment extends Fragment {
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
    @BindView(R.id.weldJointsHeaderText)
    TextView jointsHeader;
    @BindView(R.id.listview)
    ListView mListView;
    private ViewPager mViewPager;
    private ExpandableListHeader mHeaderData;
    private Unbinder mUnbinder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            mHeaderData = bundle.getParcelable(AppConstants.PARCELABLE);
    }
    public WeldJointsFragment() {
    }
    public static WeldJointsFragment newInstance(ExpandableListHeader mHeaderData) {
        WeldJointsFragment weldJointsFragment = new WeldJointsFragment();
        Bundle bundle = new Bundle();
        if (mHeaderData != null)
            bundle.putParcelable(AppConstants.PARCELABLE, mHeaderData);
        weldJointsFragment.setArguments(bundle);
        return weldJointsFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weldjoints, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    // soll Werte should be set from xml file, later
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 60;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    // 20170223: get reference of viewpager so that we could switch to tab visual view
    private void setViewPager() {
        ProductStructureFragment fragment = (ProductStructureFragment) getParentFragment();
        if (fragment != null)
            mViewPager = fragment.getViewPagerFromOverview();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
