package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
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
import com.parsa_plm.Layout.CustomItemClickListener;
import com.parsa_plm.Layout.ImageDisplayActivity;
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
            if (context instanceof Activity)
                mainActivity = (Activity) context;
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
            String specificDir = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                String storageDir = mContext.getExternalFilesDir(null).toString();
                String[] xmlFileDir = headerData.getFileDirectory().split("/");
                specificDir = storageDir + File.separator + xmlFileDir[xmlFileDir.length - 1];
            } else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.attention48)
                        .setTitle("External Storage not available")
                        .setMessage("Can not reach external storage, probably has been removed.")
                        .create().show();
                return;
            }
            File f = new File(specificDir);
            if (f.isDirectory() && f.exists()) {
                File file = new File(f.toString());
                // 20161214 should not obtain all files, only images
                File[] files = file.listFiles();
                final List<File> images = getImages(files);
                //ImageListAdapter adapter = new ImageListAdapter(mContext, images);
                // 20161216: new adapter for better usability
                ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, images, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        setUpClickListener(position, images);
                    }
                });
                if (mGridView != null)
                    mGridView.setAdapter(gridAdapter);
            }
        }
    }

    private void setUpClickListener(int position, List<File> images) {
        Intent intent = new Intent(mContext, ImageDisplayActivity.class);
        String filePath = images.get(position).getAbsolutePath();
        intent.putExtra("path", filePath);
        mContext.startActivity(intent);
    }

    // 20161214: wir only need images
    // 20161216: change signature
    private List<File> getImages(File[] files) {
        List<File> images = new ArrayList<>();
        if (files.length > 0) {
            for (File f : files) {
                if (f.getName().toLowerCase().endsWith("jpg") || f.getName().toLowerCase().endsWith("png"))
                    images.add(f);
            }
        }
        return images;
    }
}

