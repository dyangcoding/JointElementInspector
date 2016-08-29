package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.parsa_plm.Layout.ParentLevelAdapter;

public class ProductStructureFragment extends Fragment{
    // this contains the data to be displayed
    private ExpandableListHeader headerData;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    public ProductStructureFragment(){}
    public static ProductStructureFragment newInstance(ExpandableListHeader headerData){
        ProductStructureFragment productStructureFragment = new ProductStructureFragment();
        Bundle bundle = new Bundle();
        if (headerData != null) {
            bundle.putParcelable("com.ExpandableListData", headerData);
        }
        productStructureFragment.setArguments(bundle);
        return productStructureFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productstructure, container, false);
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.parentLevel);
        expandableListView.setAdapter(new ParentLevelAdapter(headerData));
        return view;
    }
}
