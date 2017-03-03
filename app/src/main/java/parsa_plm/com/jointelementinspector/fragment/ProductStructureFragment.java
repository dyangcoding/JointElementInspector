package parsa_plm.com.jointelementinspector.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.models.ExpandableListItem;
import parsa_plm.com.jointelementinspector.models.Occurrence;
import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.adapter.ParentLevelAdapter;

import java.util.List;

public class ProductStructureFragment extends Fragment implements View.OnLayoutChangeListener {
    // this contains the data to be displayed
    private ExpandableListHeader headerData;
    private static final String PRODUCT_STRUCTURE = "Product Structure";
    private static final String PRODUCT_STRUCTURE_PART_NAME = "Part Name";
    private static final String PRODUCT_STRUCTURE_ITEM_TYPE = "Item Type";
    private NestedScrollView mNestedScrollView;

    //private ParentFragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            headerData = bundle.getParcelable("com.ExpandableListData");
    }

    public ProductStructureFragment() {
    }

    public static ProductStructureFragment newInstance(ExpandableListHeader headerData) {
        ProductStructureFragment productStructureFragment = new ProductStructureFragment();
        Bundle bundle = new Bundle();
        if (headerData != null)
            bundle.putParcelable("com.ExpandableListData", headerData);
        productStructureFragment.setArguments(bundle);
        return productStructureFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productstructure, container, false);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        TextView productStructureHeader = (TextView) view.findViewById(R.id.productStructureHeader);
        productStructureHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        productStructureHeader.setText(PRODUCT_STRUCTURE);
        // 20160902: list structure item header
        // 20161111: don not need it any more
        //TextView productStructurePartName = (TextView) view.findViewById(R.id.productStructure_partName);
        //productStructurePartName.setText(PRODUCT_STRUCTURE_PART_NAME);
        //TextView productStructureItemType = (TextView) view.findViewById(R.id.productStructure_itemType);
        //productStructureItemType.setText(PRODUCT_STRUCTURE_ITEM_TYPE);
        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.parentLevel);
        // 20160831: check null value
        // 20160902: show proper indicator
        int width = getResources().getDisplayMetrics().widthPixels;
        if (expandableListView != null) {
            // 20160831: use get Activity to obtain the context, and this is working
            expandableListView.setAdapter(new ParentLevelAdapter(getActivity(), headerData));
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(5));
            //expandableListView.setChildIndicatorBounds(width - GetPixelFromDips(40), width - GetPixelFromDips(10));
            setUpOnGroupExpandListener(expandableListView);
            setUpOnGroupClickListener(expandableListView);
            setUpChildClick(expandableListView);
        }
        return view;
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
                WeldJointsFragment fragment = (WeldJointsFragment) fragmentManager.findFragmentByTag("weldJointsFragment");
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

    // 20170129: TODO: by multi times onclick should not add the same fragment
    // 20170223: TODO: refactor
    private void setUpChildClick(ExpandableListView expandableListView) {
        final List<ExpandableListItem> childList = this.headerData.getChildOfOccurrence();
        // 20161022: handel children click to make weld points fragment visible
        expandableListView.setOnChildClickListener((listView, view, group_position, child_position, id) ->{
                List<Occurrence> dataInNestedFragment = childList.get(child_position).getChildItemList();
                FragmentManager childFragmentManager = getChildFragmentManager();
                if (dataInNestedFragment.size() > 0) {
                    FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
                    // add AW fragment to replace weld points fragment
                    WeldJointsFragment weldJointsFragment = WeldJointsFragment.newInstance(headerData);
                    childFragTrans.add(R.id.fragment_placeHolder_weldJoints, weldJointsFragment, "weldJointsFragment");
                    childFragTrans.commit();
                } else {
                    // 20170127: may adds more fragment later to be removed
                    Fragment fragment = childFragmentManager.findFragmentByTag("weldJointsFragment");
                    if (fragment != null) {
                        FragmentTransaction removeFragment = childFragmentManager.beginTransaction();
                        removeFragment.remove(fragment);
                        removeFragment.commit();
                    }
                    Toast.makeText(getContext(), " There is no data. ", Toast.LENGTH_LONG).show();
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
        WeldJointsFragment weldJointsFragment = (WeldJointsFragment) fragmemtManager.findFragmentByTag("weldJointsFragment");
        if (weldJointsFragment != null) {
            if (scrollView != null)
                scrollView.smoothScrollTo(0, scrollView.getBottom());
        }else {
            scrollView.smoothScrollTo(0, scrollView.getBottom());
        }
    }
}
