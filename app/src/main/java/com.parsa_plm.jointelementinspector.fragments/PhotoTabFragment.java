package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private OverviewTabFragment.onFragmentInteractionListener listener;
    private GridView mImageView;
    private ProgressDialog mProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View photoView = inflater.inflate(R.layout.tab_fragment_photo, container, false);
        mImageView = (GridView) photoView.findViewById(R.id.image_gridview);
        return photoView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivity = null;
        try {
            if (context instanceof Activity) {
                mainActivity = (Activity) context;
            }
            listener = (OverviewTabFragment.onFragmentInteractionListener) mainActivity;
        }catch (ClassCastException e) {
            throw new ClassCastException(mainActivity.toString() + "must implement onFragmentInteractionListener");
        }
        if (listener != null) headerData = listener.onFragmentCreated();
        // 20160902: no data no layout
        if (headerData != null) {
            /*
            mProgressDialog = new ProgressDialog(context);
            this.mProgressDialog.setMessage("Loading Photos ...");
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.show();
            // image view is null!!!
            if (mImageView != null) {
                Picasso.with(context).load(new File(headerData.getFileDirectory()))
                        .into(mImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                            @Override
                            public void onError() {

                            }
                        });
            */
            }
        }
    }

