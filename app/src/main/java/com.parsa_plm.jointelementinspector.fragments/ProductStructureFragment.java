package com.parsa_plm.jointelementinspector.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;
import com.parsa_plm.Layout.ParentLevelAdapter;

import java.util.List;

public class ProductStructureFragment extends Fragment{
    // this contains the data to be displayed
    private ExpandableListHeader headerData;
    private static final String PRODUCT_STRUCTURE = "Product Structure";
    private static final String PRODUCT_STRUCTURE_PART_NAME = "Part Name";
    private static final String PRODUCT_STRUCTURE_ITEM_TYPE = "Item Type";
    private WeldJointsFragment weldJointsFragment;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public ProductStructureFragment(){}
    public static ProductStructureFragment newInstance(ExpandableListHeader headerData){
        ProductStructureFragment productStructureFragment = new ProductStructureFragment();
        Bundle bundle = new Bundle();
        if (headerData != null) {
            bundle.putParcelable("com.ExpandableListData", headerData);
        }
        productStructureFragment.setArguments(bundle);
        return productStructureFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productstructure, container, false);
        TextView productStructureHeader = (TextView) view.findViewById(R.id.productStructureHeader);
        productStructureHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        productStructureHeader.setText(PRODUCT_STRUCTURE);
        // 20160902: list structure item header
        TextView productStructurePartName = (TextView) view.findViewById(R.id.productStructure_partName);
        productStructurePartName.setText(PRODUCT_STRUCTURE_PART_NAME);
        TextView productStructureItemType = (TextView) view.findViewById(R.id.productStructure_itemType);
        productStructureItemType.setText(PRODUCT_STRUCTURE_ITEM_TYPE);
        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.parentLevel);
        // 20160831: check null value
        // 20160902: show proper indicator
        int width = getResources().getDisplayMetrics().widthPixels;
        if (expandableListView != null) {
            // 20160831: use get Activity to obtain the context, and this is working
            expandableListView.setAdapter(new ParentLevelAdapter(getActivity(), headerData));
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setChildIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int i) {
                    if (i != previousGroup) {
                        expandableListView.collapseGroup(previousGroup);
                        previousGroup = i;
                    }
                }
            });
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    setExpandListViewHeight(parent, groupPosition);
                    return false;
                }
            });
            final List<ExpandableListItem> list = this.headerData.getChildOfOccurrence();
            // not for sure if this gonna work
            weldJointsFragment = (WeldJointsFragment) getParentFragment().getChildFragmentManager()
                    .findFragmentByTag("weldJointsFragment");
            // 20161022: handel children click to make weld points fragment visible
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view,
                                            int group_position, int child_position, long id) {
                    List<WeldPoint> listTest = list.get(group_position).getChildItemList();
                    if (listTest != null) {
                        Toast.makeText(getContext(), " i am from structure fragment ", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        }
        return view;
    }
    private int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
    protected void setExpandListViewHeight(ExpandableListView listView,
                                         int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); ++i) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
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
}
