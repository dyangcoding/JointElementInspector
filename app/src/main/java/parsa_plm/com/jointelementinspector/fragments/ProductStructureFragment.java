package parsa_plm.com.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.models.ExpandableListItem;
import parsa_plm.com.jointelementinspector.models.Occurrence;
import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.adapters.ParentLevelAdapter;
import parsa_plm.com.jointelementinspector.utils.AppConstants;

import java.util.List;

public class ProductStructureFragment extends Fragment implements View.OnLayoutChangeListener {
    // this contains the data to be displayed
    private ExpandableListHeader headerData;
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.productStructureHeader)
    TextView productStructureHeader;
    @BindView(R.id.parentLevel)
    ExpandableListView expandableListView;
    private Unbinder mUnbinder;
    //private ParentFragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            headerData = bundle.getParcelable(AppConstants.PARCELABLE);
    }
    public ProductStructureFragment() {
    }
    public static ProductStructureFragment newInstance(ExpandableListHeader headerData) {
        ProductStructureFragment productStructureFragment = new ProductStructureFragment();
        Bundle bundle = new Bundle();
        if (headerData != null)
            bundle.putParcelable(AppConstants.PARCELABLE, headerData);
        productStructureFragment.setArguments(bundle);
        return productStructureFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productstructure, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 20160831: check null value
        if (expandableListView != null) {
            // 20160831: use get Activity to obtain the context, and this is working
            expandableListView.setAdapter(new ParentLevelAdapter(getActivity(), headerData));
            int width = getResources().getDisplayMetrics().widthPixels;
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(320), width - GetPixelFromDips(5));
            setUpOnGroupExpandListener(expandableListView);
            setUpOnGroupClickListener(expandableListView);
            setUpChildClick(expandableListView);
        }
    }
    private void setUpOnGroupClickListener(ExpandableListView expandableListView) {
        expandableListView.setOnGroupClickListener((parent, view, groupPosition, id) -> {
            if (mNestedScrollView != null) {
                mNestedScrollView.removeOnLayoutChangeListener(this);
            }
            setExpandListViewHeight(parent, groupPosition);
            return false;
        });
    }
    private void setUpOnGroupExpandListener(final ExpandableListView expandableListView) {
        expandableListView.setOnGroupExpandListener(i -> {
            if (mNestedScrollView != null) {
                FragmentManager fragmentManager = getChildFragmentManager();
                WeldJointsFragment fragment = (WeldJointsFragment) fragmentManager.findFragmentByTag(AppConstants.WeldJOINTS_FRAGMENT);
                if (fragment == null)
                    mNestedScrollView.addOnLayoutChangeListener(this);
            }
            int previousGroup = -1;
            if (i != previousGroup) {
                expandableListView.collapseGroup(previousGroup);
                previousGroup = i;
            }
        });
    }
    // 20170129: Done: by multi times onclick should not add the same fragment
    // 20170517: same fragment is only loaded once
    private void setUpChildClick(ExpandableListView expandableListView) {
        final List<ExpandableListItem> childList = this.headerData.getChildOfOccurrence();
        // 20161022: handel children click to make weld points fragment visible
        expandableListView.setOnChildClickListener((listView, view, group_position, child_position, id) ->{
                List<Occurrence> dataInNestedFragment = childList.get(child_position).getChildItemList();
                FragmentManager childFragmentManager = getChildFragmentManager();
                Fragment fragment = childFragmentManager.findFragmentByTag(AppConstants.WeldJOINTS_FRAGMENT);
            if (dataInNestedFragment.size() > 0) {
                    if (fragment == null) {
                        FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
                        // add AW fragment to replace weld points fragment
                        WeldJointsFragment weldJointsFragment = WeldJointsFragment.newInstance(headerData);
                        childFragTrans.add(R.id.fragment_placeHolder_weldJoints, weldJointsFragment, AppConstants.WeldJOINTS_FRAGMENT);
                        childFragTrans.commit();
                    }
                } else {
                    // 20170127: may adds more fragment later to be removed
                    if (fragment != null) {
                        FragmentTransaction removeFragment = childFragmentManager.beginTransaction();
                        removeFragment.remove(fragment);
                        removeFragment.commit();
                    }
                    Toast.makeText(getContext(), AppConstants.NO_FURTHER_DATA, Toast.LENGTH_LONG).show();
                }
                return true;
        });
    }
    private int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
    // make height of expand list view suitable
    protected void setExpandListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); ++i) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    // 20170221: get reference of parent view pager from overview fragment
    protected ViewPager getViewPagerFromOverview() {
        OverviewTabFragment fragmentOverview = (OverviewTabFragment) getParentFragment();
        if (fragmentOverview != null)
            return fragmentOverview.getParentViewPager();
        return null;
    }
    // 20170223: we use now nested scroll view in this fragment, so that layout change is only
    // been detected through this fragment, header fragment has no effect on the layout change listener
    protected ScrollView getScrollView() {
        OverviewTabFragment fragmentOverview = (OverviewTabFragment) getParentFragment();
        if (fragmentOverview != null)
            return fragmentOverview.getScrollView();
        return null;
    }
    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldright, int oldBottom) {
        // 20170223: should only automatically scroll down by expanding structure list item
        ScrollView scrollView = getScrollView();
        FragmentManager fragmemtManager = getChildFragmentManager();
        WeldJointsFragment weldJointsFragment = (WeldJointsFragment) fragmemtManager.findFragmentByTag(AppConstants.WeldJOINTS_FRAGMENT);
        if (weldJointsFragment != null) {
            if (scrollView != null)
                scrollView.smoothScrollTo(0, scrollView.getBottom());
        }else {
            scrollView.smoothScrollTo(0, scrollView.getBottom());
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
