package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.ExpandableListHeader;


public class OverviewTabFragment extends Fragment{
    private ExpandableListHeader headerData;
    // 20161020: not for sure if this is needed, cheek it out later maybe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_overview, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivity = null;
        onFragmentInteractionListener listener;
        try {
            if (context instanceof Activity)
                mainActivity = (Activity) context;
            listener = (onFragmentInteractionListener) mainActivity;
        }catch (ClassCastException e) {
            throw new ClassCastException(mainActivity.toString() + "must implement onFragmentInteractionListener");
        }
        if (listener != null) headerData = listener.onFragmentCreated();
        // 20160902: no data no layout
        if (headerData != null) {
            // 20160824: getChildFragmentManager instead of getFragmentManager
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
            InspectionHeaderFragment headerFragment = InspectionHeaderFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_inspectionHeader, headerFragment, "inspectorHeaderFragment");
            // 20160824: add product structure fragment
            ProductStructureFragment productStructureFragment = ProductStructureFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_productStructure, productStructureFragment, "productStructureFragment");
            childFragTrans.commit();
            // 20160829: later fragment for more weld points attribute
            // 20161021: now add weld joints fragment
            /*
            WeldJointsFragment weldJointsFragment = WeldJointsFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_weldJoints, weldJointsFragment, "weldJointsFragment");
            childFragTrans.commit();
            */
        }
    }
    public interface onFragmentInteractionListener{
        public ExpandableListHeader onFragmentCreated ();
    }
}
