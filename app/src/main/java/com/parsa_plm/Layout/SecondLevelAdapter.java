package com.parsa_plm.Layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;

import org.w3c.dom.Text;

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

    // if has no children, give user feed back
    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        ExpandableListItem parentItem = (ExpandableListItem) getGroup(groupPosition);
        if (parentItem.getChildItemList().size() > 0) {
            childCount = parentItem.getChildItemList().size();
        }else {
            Toast.makeText(this.context, " i got no children. ", Toast.LENGTH_LONG).show();
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
        if (childList != null && childList.size() > 0) {
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
        String headerTitle = headerItem.getItemNr() + " ; " + headerItem.getItemName();
        String headerType = headerItem.getItemType();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_group_second, viewGroup, false);
        }
        // add image view for group icons
        ImageView icon = (ImageView) view.findViewById(R.id.group_icon);
        switch (headerType.trim()) {
            case "Design Revision":
                icon.setImageResource(R.drawable.design_obj_16);
            case "A2_JGCRevision":
                icon.setImageResource(R.drawable.jgc_rev_16);
        }
        TextView listHeader = (TextView) view.findViewById(R.id.expandListHeader_second);
        listHeader.setText(headerTitle);
        listHeader.setPadding(10, 0, 0, 0);
        // listHeader.setMinWidth(100);
        // 20160902: add type
        TextView listHeaderType = (TextView)view.findViewById(R.id.expandListHeader_second_itemType);
        listHeaderType.setText(headerType);
        return view;
    }

    // item type should also be created, later
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        WeldPoint weldPoint = (WeldPoint) getChild(groupPosition, childPosition);
        String itemTitle = null;
        String itemType = null;
        if (weldPoint != null) {
            itemTitle = weldPoint.getName();
            //itemType = weldPoint.
        }
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_item, viewGroup, false);
        }
        // add image view
        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        icon.setImageResource(R.drawable.weld_rev_16);
        icon.setPadding(15, 0, 10, 0);
        TextView listItem = (TextView) view.findViewById(R.id.expandListItem);
        listItem.setText(itemTitle);
        //listItem.setPadding(30, 0, 0, 0);
        // 20160902: test version for item type
        TextView listItemType = (TextView) view.findViewById(R.id.expandListItem_type);
        listItemType.setText("WeldPoint");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
