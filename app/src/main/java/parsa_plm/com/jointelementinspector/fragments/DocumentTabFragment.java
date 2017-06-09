package parsa_plm.com.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.base.BaseTabFragment;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.adapters.DocumentGridAdapter;
import parsa_plm.com.jointelementinspector.utils.AppConstants;
import parsa_plm.com.jointelementinspector.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentTabFragment extends BaseTabFragment {
    @BindView(R.id.document_recycler_view)
    RecyclerView mGridView;
    private Context mContext;
    // 20170108: add swipe refresh layout
    @BindView(R.id.document_swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View documentView = inflater.inflate(R.layout.tab_fragment_document, container, false);
        setUnBinder(ButterKnife.bind(this, documentView));
        mContext = getContext();
        if (mGridView != null) {
            mGridView.setHasFixedSize(true);
            mGridView.setItemViewCacheSize(30);
            mGridView.setDrawingCacheEnabled(true);
            mGridView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            int columns = Utility.calculateColumns(mContext);
            GridLayoutManager gl = new GridLayoutManager(mContext, columns);
            mGridView.setLayoutManager(gl);
        }
        if (mSwipeRefreshLayout != null)
            // 20170108: swipe refresh layout
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        return documentView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExpandableListHeader headerData = getHeaderData();
        if (headerData != null) {
            String mDocumentPath = headerData.getFileDirectory();
            List<File> filePath = getPDFFiles(mDocumentPath);
            if (filePath != null)
                setUpDocumentAdapter(filePath, mDocumentPath);
        }
    }
    // 20161223: add listener
    private void setUpDocumentAdapter(List<File> documents, String documentPath) {
        DocumentGridAdapter adapter = new DocumentGridAdapter(mContext, documents, (view, position) -> {
            setUpOnClickListener(position, documents, documentPath);
        });
        setSwipeRefresh(adapter, documentPath);
        if (mGridView != null)
            mGridView.setAdapter(adapter);
    }
    // 20170113: use notifyDataSetChanged which update all items by data inserted, removed, bad performance
    // should use more specific method to update items as notifyDItemRangeChanged etc.
    private void setSwipeRefresh(final DocumentGridAdapter adapter, final String documentPath) {
        // 20170108: swipe refresh, with clear and addAll notify works
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                // 20170108: hold reference of the old documents
                int oldDocumentsCount = adapter.getItemCount();
                adapter.clear();
                List<File> refreshPdfs = getPDFFiles(documentPath);
                if (refreshPdfs != null) {
                    adapter.addAll(refreshPdfs);
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (oldDocumentsCount != refreshPdfs.size()) {
                        int updatedItemCount = 0;
                        updatedItemCount = refreshPdfs.size() - oldDocumentsCount;
                        if (updatedItemCount > 0)
                            Toast.makeText(mContext, updatedItemCount + AppConstants.ITEM_ADDED, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(mContext, Math.abs(updatedItemCount) + AppConstants.ITEM_REMOVED, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    // 20170108: should check if the file exists, which has been clicked
    private void setUpOnClickListener(int position, List<File> documents, String documentPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = documents.get(position);
        // 20161220: now the correct path
        File f = new File(documentPath + File.separator + file.getName());
        if (f.exists()) {
            intent.setDataAndType(Uri.fromFile(f), AppConstants.INTENT_DATA_TYPE);
            PackageManager pm = mContext.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (activities.size() > 0)
                mContext.startActivity(intent);
            else
                Toast.makeText(mContext, AppConstants.NO_INSTALLED_PROGRAM, Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(mContext, " Can not access file " + f.toString() + " probably been removed.", Toast.LENGTH_LONG).show();
    }
    private List<File> getPDFFiles(String documentPath) {
        List<File> pdfFiles = new ArrayList<>();
        File documentDir = null;
        if (documentPath != null && !documentPath.isEmpty()) {
            documentDir = new File(documentPath);
            if (documentDir.isDirectory() && documentDir.exists()) {
                File[] files = documentDir.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        if (f.getName().toLowerCase().endsWith(".pdf"))
                            pdfFiles.add(f);
                    }
                } else
                    Toast.makeText(mContext, AppConstants.NO_DOCUMENT, Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.mipmap.ic_attention)
                        .setTitle(AppConstants.DOC_PATH_INCORRECT)
                        .setMessage(AppConstants.DOC_PATH_FAILED_MESSAGE)
                        .create().show();
                return null;
            }
        }
        return pdfFiles;
    }
}
