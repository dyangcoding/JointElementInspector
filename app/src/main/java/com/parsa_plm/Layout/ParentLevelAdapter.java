package com.parsa_plm.Layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;


public class ParentLevelAdapter extends BaseExpandableListAdapter{
    // 20160831: we need context for somehow
    private Context context;
    private ExpandableListHeader expandableListData;

    public ParentLevelAdapter(Context context, ExpandableListHeader expandableListData) {
        this.context = context;
        this.expandableListData = expandableListData;
    }
    @Override
    public int getGroupCount() {
        return 1;
    }

    // 20160831: should be 1, not the size of the child occurrence
    // 20170127: without using second level expand list view, here should return the size
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListData.getChildOfOccurrence().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListData.getChildOfOccurrence().get(childPosition);
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
        String headerTitle = null;
        String headerType = null;
        if (this.expandableListData != null) {
            headerTitle = this.expandableListData.getPartNr() + " ; " + this.expandableListData.getPartName();
            headerType = this.expandableListData.getType();
        }
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_group, viewGroup, false);
        }
        //ImageView groupIndicator = (ImageView) view.findViewById(R.id.group_indicator);
        //groupIndicator.setImageResource(R.drawable.groupindicator);
        // add image view to display icons
        ImageView icon = (ImageView) view.findViewById(R.id.header_icon);
        icon.setImageResource(R.drawable.io_rev_16);
        TextView listHeader = (TextView) view.findViewById(R.id.expandListHeader);
        listHeader.setText(headerTitle);
        // 20160902: add item type
        TextView listHeaderType = (TextView) view.findViewById(R.id.expandListHeader_itemType);
        listHeaderType.setText(headerType);
        return view;
    }
    // 20170127: we don't need second expand list view any more, make new child view here
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_group_second, viewGroup, false);
        }
        ExpandableListItem item = (ExpandableListItem) getChild(groupPosition, childPosition);
        if (item != null) {
            TextView itemHeader = (TextView) view.findViewById(R.id.expandListHeader_second);
            itemHeader.setText(item.getItemName());
            TextView itemHeaderType = (TextView)view.findViewById(R.id.expandListHeader_second_itemType);
            String itemType = item.getItemType();
            itemHeaderType.setText(itemType);
            //ImageView arrowIndicator = (ImageView) view.findViewById(R.id.arrow_indicator);
            //arrowIndicator.setImageResource(R.drawable.arrowrechts64);
            ImageView icon = (ImageView) view.findViewById(R.id.group_icon);
            switch (itemType.trim()) {
                case "Design Revision":
                    icon.setImageResource(R.drawable.design_obj_16);
                    break;
                case "A2_JGCRevision":
                    icon.setImageResource(R.drawable.jgc_rev_16);
                    break;
            }

        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
