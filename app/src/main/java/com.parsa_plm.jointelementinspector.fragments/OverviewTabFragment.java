package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.ExpandableListHeader;


public class OverviewTabFragment extends Fragment{
    private ExpandableListHeader headerData;
    private onFragmentInteractionListener listener;
    private boolean tableVisibility = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_overview, container, false);
        //FrameLayout headerLayout = (FrameLayout) rootView.findViewById(R.id.fragment_placeHolder_inspectionHeader);
        //LinearLayout overviewLayout = (LinearLayout) rootView.findViewById(R.id.overviewLayout);
        //LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) overviewLayout.getLayoutParams();
        //if (!tableVisibility) {
            //headerLayout.setLayoutParams(lParams);
        //}
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivity = null;
        try {
            if (context instanceof Activity) {
                mainActivity = (Activity) context;
            }
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
            tableVisibility = headerFragment.getVisibilityForTableRow();
            childFragTrans.add(R.id.fragment_placeHolder_inspectionHeader, headerFragment, "inspectorHeaderFragment");
            // 20160824: add product structure fragment
            ProductStructureFragment productStructureFragment = ProductStructureFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_productStructure, productStructureFragment, "productStructureFragment");
            // 20160829: later fragment for more weld points attribute
            childFragTrans.commit();
        }
    }
    public interface onFragmentInteractionListener{
        public ExpandableListHeader onFragmentCreated ();
    }
}
