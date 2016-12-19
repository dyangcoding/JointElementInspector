package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.parsa_plm.Layout.ImageGridAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private OverviewTabFragment.onFragmentInteractionListener listener;
    // 20161216: use recycler view
    private RecyclerView mGridView;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View photoView = inflater.inflate(R.layout.tab_fragment_photo, container, false);
        mGridView = (RecyclerView) photoView.findViewById(R.id.image_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(mContext, 4);
        mGridView.setLayoutManager(glm);
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
                // 20161214 should not obtain all files, only images
                File[] files = file.listFiles();
                List<File> images = getImages(files);
                //ImageListAdapter adapter = new ImageListAdapter(mContext, images);
                // 20161216: new adapter for better usability
                ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, images);
                if (mGridView != null) {
                    mGridView.setAdapter(gridAdapter);
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
    // 20161216: change signature
    private List<File> getImages(File[] files) {
        List<File> images = new ArrayList<>();
        if (files.length > 0) {
            for (File f: files) {
                if (f.getName().toLowerCase().endsWith("jpg") || f.getName().toLowerCase().endsWith("png"))
                    images.add(f);
            }
        }
        return images;
    }
}

