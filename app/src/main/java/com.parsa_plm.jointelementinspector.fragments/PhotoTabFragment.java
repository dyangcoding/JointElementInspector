package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    // 20161216: use recycler view
    private RecyclerView mGridView;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View photoView = inflater.inflate(R.layout.tab_fragment_photo, container, false);
        mGridView = (RecyclerView) photoView.findViewById(R.id.image_recycler_view);
        mGridView.setHasFixedSize(true);
        mGridView.setItemViewCacheSize(30);
        mGridView.setDrawingCacheEnabled(true);
        mGridView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GridLayoutManager glm = new GridLayoutManager(mContext, 4);
        mGridView.setLayoutManager(glm);
        mSwipeRefreshLayout = (SwipeRefreshLayout) photoView.findViewById(R.id.photo_swipeContainer);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        return photoView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Activity mainActivity = null;
        OverviewTabFragment.onFragmentInteractionListener listener = null;
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
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                String storageDir = mContext.getExternalFilesDir(null).toString();
                String[] xmlFileDir = headerData.getFileDirectory().split("/");
                String specificDir = storageDir + File.separator + xmlFileDir[xmlFileDir.length - 1];
                setUpPhotoAdapter(specificDir);
            } else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.attention48)
                        .setTitle("External Storage not available")
                        .setMessage("Can not reach external storage, probably has been removed.")
                        .create().show();
            }
        }
    }

    private void setUpPhotoAdapter(String specificDir) {
        final List<File> images = getImages(specificDir);
        //ImageListAdapter adapter = new ImageListAdapter(mContext, images);
        // 20161216: new adapter for better usability
        ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, images, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                setUpClickListener(position, images);
            }
        });
        //gridAdapter.setHasStableIds(true);
        setSwipeRefresh(gridAdapter, specificDir);
        if (mGridView != null)
            mGridView.setAdapter(gridAdapter);
    }
    // 20170113: use notifyDataSetChanged which update all items by data inserted, removed, bad performance
    // should use more specific method to update items as notifyDItemRangeChanged etc.
    private void setSwipeRefresh(final ImageGridAdapter adapter, final String folderPath) {
        // 20170108: swipe refresh, with clear and addAll notify works
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // 20170108: hold reference of the old documents
                    int oldPhotosCount = adapter.getItemCount();
                    adapter.clear();
                    List<File> refreshPhotos = getImages(folderPath);
                    if (refreshPhotos != null) {
                        adapter.addAll(refreshPhotos);
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (oldPhotosCount != refreshPhotos.size()) {
                            int updatedItemCount = 0;
                            updatedItemCount = refreshPhotos.size() - oldPhotosCount;
                            if (updatedItemCount > 0)
                                Toast.makeText(mContext, updatedItemCount + " item added.", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(mContext, Math.abs(updatedItemCount) + " item removed. ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void setUpClickListener(int position, List<File> images) {
        Intent intent = new Intent(mContext, ImageDisplayActivity.class);
        String filePath = images.get(position).getAbsolutePath();
        // 20170108: should check if the file is available
        File f = new File(filePath);
        if (f.exists()) {
            intent.putExtra("path", filePath);
            mContext.startActivity(intent);
        }
        else
            Toast.makeText(mContext, " Can not access file " + f.toString() + " probably been removed.", Toast.LENGTH_LONG).show();
    }

    // 20161214: wir only need images
    // 20161216: change signature
    private List<File> getImages(String imagePath) {
        List<File> images = new ArrayList<>();
        File photoDir = null;
        if (imagePath != null && !imagePath.isEmpty()) {
            photoDir = new File(imagePath);
            if (photoDir.isDirectory() && photoDir.exists()) {
                File[] files = photoDir.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        if (f.getName().toLowerCase().endsWith("jpg") || f.getName().toLowerCase().endsWith("png"))
                            images.add(f);
                    }
                } else
                    Toast.makeText(mContext, "There is no photo files to show.", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.attention48)
                        .setTitle("Photo Path not correct")
                        .setMessage("The path where all photo files to be loaded is not correct.")
                        .create().show();
                return null;
            }
        }
        return images;
    }
}

