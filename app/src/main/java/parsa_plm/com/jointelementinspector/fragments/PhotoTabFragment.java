package parsa_plm.com.jointelementinspector.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import parsa_plm.com.jointelementinspector.base.BaseTabFragment;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.activities.ImageDisplayActivity;
import parsa_plm.com.jointelementinspector.adapters.ImageGridAdapter;
import parsa_plm.com.jointelementinspector.utils.AppConstants;
import parsa_plm.com.jointelementinspector.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoTabFragment extends BaseTabFragment {
    // 20161216: use recycler view
    @BindView(R.id.image_recycler_view)
    RecyclerView mRecyclerView;
    private Context mContext;
    @BindView(R.id.photo_swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String mImagePath;
    public PhotoTabFragment() { setArguments(new Bundle()); }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View photoView = inflater.inflate(R.layout.tab_fragment_photo, container, false);
        mContext = getContext();
        setUnBinder(ButterKnife.bind(this, photoView));
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemViewCacheSize(30);
            mRecyclerView.setDrawingCacheEnabled(true);
            mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            int columns = Utility.calculateColumns(mContext);
            GridLayoutManager glm = new GridLayoutManager(mContext, columns);
            mRecyclerView.setLayoutManager(glm);
        }
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        return photoView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExpandableListHeader headerData = getHeaderData();
        if (headerData != null) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.mipmap.ic_attention)
                        .setTitle(AppConstants.EXTERNAL_STORAGE)
                        .setMessage(AppConstants.EXTERNAL_STORAGE_FAILED_MESSAGE)
                        .create().show();
                return;
            }
            String storageDir = mContext.getExternalFilesDir(null).toString();
            String[] xmlFileDir = headerData.getFileDirectory().split("/");
            String specificDir = storageDir + File.separator + xmlFileDir[xmlFileDir.length - 1];
            setUpPhotoAdapter(specificDir);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String savedFilePath = (String) bundle.get(AppConstants.IMAGE_FILE_PATH);
            onOpenImage(savedFilePath);
            // 20170611: release
            bundle.clear();
            if (mImagePath != null)
                mImagePath = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mImagePath != null)
            getArguments().putString(AppConstants.IMAGE_FILE_PATH, mImagePath);
    }
    private void setUpPhotoAdapter(String specificDir) {
        final List<File> images = getImages(specificDir);
        // 20161216: new adapter for better usability
        ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, images, (view, position) -> {
            setUpClickListener(position, images);
        });
        //gridAdapter.setHasStableIds(true);
        setSwipeRefresh(gridAdapter, specificDir);
        if (mRecyclerView != null)
            mRecyclerView.setAdapter(gridAdapter);
    }
    // 20170113: use notifyDataSetChanged which update all items by data inserted, removed, bad performance
    // should use more specific method to update items as notifyDItemRangeChanged etc.
    private void setSwipeRefresh(final ImageGridAdapter adapter, final String folderPath) {
        // 20170108: swipe refresh, with clear and addAll notify works
        if (mSwipeRefreshLayout == null)
            return;
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            // 20170108: hold reference of the old documents
            int oldPhotosCount = adapter.getItemCount();
            adapter.clear();
            List<File> refreshPhotos = getImages(folderPath);
            if (refreshPhotos == null) {
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            adapter.addAll(refreshPhotos);
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
            if (oldPhotosCount != refreshPhotos.size()) {
                int updatedItemCount = refreshPhotos.size() - oldPhotosCount;
                if (updatedItemCount > 0)
                    Toast.makeText(mContext, updatedItemCount + AppConstants.IMAGE_UPDATED, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(mContext, Math.abs(updatedItemCount) + AppConstants.IMAGE_REMOVED, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setUpClickListener(int position, List<File> images) {
        mImagePath = images.get(position).getAbsolutePath();
        // 20170108: should check if the file is available
        onOpenImage(mImagePath);
    }
    private void onOpenImage(String filePath) {
        if (filePath != null) {
            File f = new File(mImagePath);
            if (!f.exists()) {
                Toast.makeText(mContext, " Can not access file " + f.toString() + " probably been removed.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(mContext, ImageDisplayActivity.class);
            intent.putExtra("path", filePath);
            mContext.startActivity(intent);
        }
    }
    // 20161214: wir only need images
    // 20161216: change signature
    private List<File> getImages(String imagePath) {
        List<File> images = new ArrayList<>();
        File photoDir = null;
        if (imagePath == null || imagePath.isEmpty())
            return null;
        photoDir = new File(imagePath);
        if (!photoDir.isDirectory() || !photoDir.exists()) {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_attention)
                    .setTitle(AppConstants.PHOTO_PATH_INCORRECT)
                    .setMessage(AppConstants.PHOTO_PATH_FAILED_MESSAGE)
                    .create().show();
            return null;
        }
        File[] files = photoDir.listFiles();
        if (files.length == 0) {
            Toast.makeText(mContext, AppConstants.NO_PHOTO, Toast.LENGTH_LONG).show();
            return null;
        }
        for (File f : files) {
            if (f.getName().toLowerCase().endsWith("jpg") || f.getName().toLowerCase().endsWith("png"))
                images.add(f);
        }
        return images;
    }
}

