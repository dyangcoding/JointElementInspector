package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.ExpandableListItem;
import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;

import java.util.List;

public class WeldJointsFragment extends Fragment{
    private ExpandableListHeader mHeaderData;
    private TextView jointsHeader;
    private ListView mListView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mHeaderData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public WeldJointsFragment(){}
    public static WeldJointsFragment newInstance(ExpandableListHeader mHeaderData){
        WeldJointsFragment weldJointsFragment = new WeldJointsFragment();
        Bundle bundle = new Bundle();
        if (mHeaderData != null) {
            bundle.putParcelable("com.ExpandableListData", mHeaderData);
        }
        weldJointsFragment.setArguments(bundle);
        return weldJointsFragment;
    }
    // fast version
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_weldjoints, container, false);
        jointsHeader = (TextView) view.findViewById(R.id.weldJointsHeaderText);
        jointsHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        mListView = (ListView) view.findViewById(R.id.listview);
        List<WeldPoint> dataList = null;
        if (mHeaderData != null) {
            List<ExpandableListItem> list = mHeaderData.getChildOfOccurrence();
            for (ExpandableListItem item: list) {
                if (item.getChildItemList() != null) {
                    dataList = item.getChildItemList();
                }
            }
        }
        WeldPointAdapter wa = new WeldPointAdapter(dataList, getContext());
        mListView.setAdapter(wa);

        return view;
    }
}
