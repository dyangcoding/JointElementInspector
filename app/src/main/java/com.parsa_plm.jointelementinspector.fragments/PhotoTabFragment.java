package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.MainActivity;
import com.jointelementinspector.main.R;
import com.parsa_plm.Layout.ImageDisplayActivity;
import com.parsa_plm.Layout.ImageListAdapter;

import java.io.File;

public class PhotoTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private OverviewTabFragment.onFragmentInteractionListener listener;
    private GridView mGridView;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View photoView = inflater.inflate(R.layout.tab_fragment_photo, container, false);
        mGridView = (GridView) photoView.findViewById(R.id.image_gridview);
        return photoView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Activity mainActivity = null;
        try {
            if (context instanceof Activity) {
                mainActivity = (Activity) context;
            }
            listener = (OverviewTabFragment.onFragmentInteractionListener) mainActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mainActivity.toString() + "must implement onFragmentInteractionListener");
        }
        if (listener != null) headerData = listener.onFragmentCreated();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (headerData != null) {
            String imagePath = headerData.getFileDirectory();
            File imageDirectory = new File(imagePath);
            if (imageDirectory.isDirectory() && imageDirectory.exists()) {
                File file = new File(imagePath);
                // 20161214 TODO should not obtain all files, only images
                File[] files = file.listFiles();
                File[] images = getImages(files);
                ImageListAdapter adapter = new ImageListAdapter(mContext, images);
                if (mGridView != null) {
                    mGridView.setAdapter(adapter);
                }
            }
            else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.attention48)
                        .setTitle("Image Path not correct")
                        .setMessage("The image path where all image to be loaded is not correct.")
                        .create().show();
            }
        }
    }
    // 20161214: wir only need images
    private File[] getImages(File[] files) {
        File[] images = null;
        if (files.length > 0) {
            images = new File[files.length];
            int i = 0;
            for (File f: files) {
                if (f.getName().toLowerCase().endsWith("jpg") || f.getName().toLowerCase().endsWith("png")) {
                    images[i] = f;
                    ++i;
                }
            }
        }
        return images;
    }
}

