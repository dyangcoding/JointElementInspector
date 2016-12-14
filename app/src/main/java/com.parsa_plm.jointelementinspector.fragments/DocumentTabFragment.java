package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.parsa_plm.Layout.ImageListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private OverviewTabFragment.onFragmentInteractionListener listener;
    private GridView mGridView;
    private Context mContext;
    private List<String> filePath = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View documentView = inflater.inflate(R.layout.tab_fragment_document, container, false);
        mGridView = (GridView) documentView.findViewById(R.id.image_gridview);
        return documentView;
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
            String documentPath = headerData.getFileDirectory();
            File documentDirectory = new File(documentPath);
            if (documentDirectory.isDirectory() && documentDirectory.exists()) {
                File[] files = documentDirectory.listFiles();
                this.filePath = getPDFFiles(files);
                setUpDocumentAdapter();
            }
            else {
                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.attention48)
                        .setTitle("Document Path not correct")
                        .setMessage("The document path where all pdf files to be loaded is not correct.")
                        .create().show();
            }
        }
    }

    private void setUpDocumentAdapter() {
        File[] images = null;
        ImageListAdapter adapter = null;
        if (this.filePath.size() > 0) {
            images = new File[filePath.size()];
            for (String filePath: this.filePath) {
                int i = 0;
                File f = new File(filePath);
                images[i] = f;
                ++i;
            }
            // 20161214: this may not working, pdf may be converted to bitmap, check it out later
            adapter = new ImageListAdapter(mContext, images);
            if (mGridView != null) {
                mGridView.setAdapter(adapter);
            }
        }
        else
            Toast.makeText(mContext, "Keine Dokument vorhanden.", Toast.LENGTH_LONG).show();
    }

    private List<String> getPDFFiles(File[] files) {
        List<String> pdfFiles = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.getName().toLowerCase().endsWith(".pdf"))
                    pdfFiles.add(f.toString());
            }
        }
        return  pdfFiles;
    }
}
