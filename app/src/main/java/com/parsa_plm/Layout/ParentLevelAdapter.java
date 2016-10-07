package com.parsa_plm.Layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.parsa_plm.jointelementinspector.fragments.ProductStructureFragment;

import org.w3c.dom.Text;

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
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final ProductExpandListView secondLevelExpListView = new ProductExpandListView(this.context);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.context, this.expandableListData));
        // custom icons later
        secondLevelExpListView.setGroupIndicator(null);
        secondLevelExpListView.setPadding(15, 0, 0, 0);
        //secondLevelExpListView.setChildIndicator(d);
        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelExpListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        return secondLevelExpListView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
