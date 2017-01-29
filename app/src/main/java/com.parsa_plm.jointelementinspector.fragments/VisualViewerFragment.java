package com.parsa_plm.jointelementinspector.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jointelementinspector.main.R;

import org.xwalk.core.XWalkView;

public class VisualViewerFragment extends Fragment {
    private XWalkView xWalkWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View visualView = inflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        xWalkWebView =(XWalkView)visualView.findViewById(R.id.xwalkWebView);
        return visualView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (xWalkWebView != null)
            xWalkWebView.load("http://www.parsa-plm.de/editor/index.html", null);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("visual fragment", "onResume: been resumed. ");
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onShow();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("visual fragment", "onPause: been paused. ");
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("visual fragment", "onDetach: been detached. ");
        if (xWalkWebView != null)
            xWalkWebView.onDestroy();
    }

}
