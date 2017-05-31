package parsa_plm.com.jointelementinspector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.models.ExpandableListItem;
import parsa_plm.com.jointelementinspector.models.Occurrence;
import com.jointelementinspector.main.R;

import java.util.List;
/*
    we don't use this class any more
 */
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
        if (parentItem.getChildItemList().size() > 0)
            childCount = parentItem.getChildItemList().size();
        else
            Toast.makeText(this.context, " Es sind keine Daten vorhanden. ", Toast.LENGTH_LONG).show();
        return childCount;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListData.getChildOfOccurrence().get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Occurrence> childList = null;
        childList = this.expandableListData.getChildOfOccurrence().get(groupPosition).getChildItemList();
        if (childList != null && childList.size() > 0)
            return childList.get(childPosition);
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
                icon.setImageResource(R.mipmap.ic_design_obj_16);
                break;
            case "A2_JGCRevision":
                icon.setImageResource(R.mipmap.ic_jgc_rev_16);
                break;
        }
        TextView listHeader = (TextView) view.findViewById(R.id.expandListHeader_second);
        listHeader.setText(headerTitle);
        // 20160902: add type
        TextView listHeaderType = (TextView)view.findViewById(R.id.expandListHeader_second_itemType);
        listHeaderType.setText(headerType);
        return view;
    }
    // item type should also be created, later
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        Occurrence occurrence = (Occurrence) getChild(groupPosition, childPosition);
        String itemTitle = null;
        String itemType = null;
        if (occurrence != null) {
            itemTitle = occurrence.getName();
            //itemType = occurrence.
        }
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_item, viewGroup, false);
        }
        // add image view
        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        icon.setImageResource(R.mipmap.ic_weld_rev_16);
        icon.setPadding(15, 0, 10, 0);
        TextView listItem = (TextView) view.findViewById(R.id.expandListItem);
        listItem.setText(itemTitle);
        // 20160902: test version for item type
        TextView listItemType = (TextView) view.findViewById(R.id.expandListItem_type);
        listItemType.setText("Occurrence");
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
