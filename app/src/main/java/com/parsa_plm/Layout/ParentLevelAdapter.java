package com.parsa_plm.Layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.jointelementinspector.main.ExpandableListHeader;

/**
 * Created by fabian082 on 29.08.2016.
 */
public class ParentLevelAdapter extends BaseExpandableListAdapter{
    private ExpandableListHeader expandableListData;

    public ParentLevelAdapter(ExpandableListHeader expandableListData) {
        this.expandableListData = expandableListData;
    }
    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListData.getChildOfOccurrence().size();
    }

    @Override
    public Object getGroup(int i) {
        return this.expandableListData;
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandableListData.getChildOfOccurrence().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
