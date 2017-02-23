package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.ExpandableListHeader;


public class OverviewTabFragment extends Fragment{
    private ExpandableListHeader headerData;
    // 20170221: get reference of parent view pager
    private ViewPager mViewPager;
    // 20170223: vertical scroll view should automatically scroll down when structure list get expanded
    private ScrollView mScrollView;
    // 20161020: not for sure if this is needed, cheek it out later maybe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewPager = (ViewPager) container;
        View view = inflater.inflate(R.layout.tab_fragment_overview, container, false);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollView_overview);
        return view;
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
            // 20170223: weld fragment is now child fragment of structure fragment
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

    //20170221: get reference to parent view pager so that we could active visual view pager
    public ViewPager getParentViewPager() {
        return mViewPager != null ? mViewPager : null;
    }

    // 20170223: get reference of scroll view to set listener in child fragment
    public ScrollView getScrollView() {
        return mScrollView;
    }
}
