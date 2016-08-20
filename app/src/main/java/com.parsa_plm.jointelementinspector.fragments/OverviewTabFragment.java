package com.parsa_plm.jointelementinspector.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointelementinspector.jointelementinspector.R;
import com.parsa_plm.folderLayout.ExpandableListData;


public class OverviewTabFragment extends Fragment{
    private ExpandableListData headerData;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_overview, container, false);
        FragmentManager childFragmentManager = getFragmentManager();
        FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
        // get data from main activity
        Bundle args = getArguments();
        if (args != null) {
            headerData = args.getParcelable("com.ExpandableListData");
        }
        InspectionHeaderFragment headerFragment = InspectionHeaderFragment.newInstance(headerData);
        childFragTrans.add(R.id.fragment_placeHolder_inspectionHeader, headerFragment, "inspectorHeaderData");
        childFragTrans.commit();

        return rootView;
    }
}
