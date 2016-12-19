package com.parsa_plm.jointelementinspector.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jointelementinspector.main.ExpandableListHeader;
import com.jointelementinspector.main.R;
import com.parsa_plm.Layout.DocumentGridAdapter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private OverviewTabFragment.onFragmentInteractionListener listener;
    private RecyclerView mGridView;
    private Context mContext;
    private List<File> filePath = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View documentView = inflater.inflate(R.layout.tab_fragment_document, container, false);
        mGridView = (RecyclerView) documentView.findViewById(R.id.document_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(mContext, 4);
        mGridView.setLayoutManager(glm);
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
                setUpDocumentAdapter(filePath);
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

    private void setUpDocumentAdapter(List<File> documents) {
        if (documents != null && documents.size() > 0) {
            DocumentGridAdapter adapter = new DocumentGridAdapter(mContext, documents);
            if (mGridView != null) {
                mGridView.setAdapter(adapter);
            }
        }
        else
            Toast.makeText(mContext, "Keine Dokument vorhanden.", Toast.LENGTH_LONG).show();
    }

    private List<File> getPDFFiles(File[] files) {
        List<File> pdfFiles = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.getName().toLowerCase().endsWith(".pdf"))
                    pdfFiles.add(f);
            }
        }
        return  pdfFiles;
    }
}
