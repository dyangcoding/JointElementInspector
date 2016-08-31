package com.parsa_plm.Layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;

import java.util.List;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    // 20160831: we need context for somehow
    private Context context;
    private ExpandableListHeader expandableListData;

    public SecondLevelAdapter(Context context, ExpandableListHeader expandableListData) {
        this.context = context;
        this.expandableListData = expandableListData;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListData.getChildOfOccurrence().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        for (ExpandableListItem item : this.expandableListData.getChildOfOccurrence()) {
            if (item.getChildItemList().size() > 0) {
                childCount += item.getChildItemList().size();
            }
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListData.getChildOfOccurrence().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<WeldPoint> childList = null;
        childList = this.expandableListData.getChildOfOccurrence().get(groupPosition).getChildItemList();
        if (childList != null) {
            return childList.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        ExpandableListItem headerItem = (ExpandableListItem) getGroup(groupPosition);
        String headerTitle = headerItem.getItemNr() + headerItem.getItemName();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_group_second, viewGroup, false);
        }
        TextView listHeader = (TextView) view.findViewById(R.id.expandListHeader_second);
        listHeader.setText(headerTitle);
        listHeader.setTextColor(Color.YELLOW);
        listHeader.setPadding(7, 0, 0, 0);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        WeldPoint weldPoint = (WeldPoint) getChild(groupPosition, childPosition);
        String itemTitle = null;
        if (weldPoint != null) itemTitle = weldPoint.getName();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_item, viewGroup, false);
        }
        TextView listItem = (TextView) view.findViewById(R.id.expandListItem);
        listItem.setText(itemTitle);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
