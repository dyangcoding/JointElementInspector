package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointelementinspector.main.ExpandableListHeader;

public class WeldJointsFragment extends Fragment{
    private ExpandableListHeader mHeaderData;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return null;
    }
}
