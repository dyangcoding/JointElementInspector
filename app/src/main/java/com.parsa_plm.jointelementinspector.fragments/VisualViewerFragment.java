package com.parsa_plm.jointelementinspector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jointelementinspector.main.R;

import org.apache.cordova.Config;
import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.engine.SystemWebView;
import org.xwalk.core.XWalkView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class VisualViewerFragment extends Fragment implements CordovaInterface {
    private XWalkView xWalkWebView;
    private CordovaWebView myWebView;;
    protected CordovaPreferences preferences;
    protected String launchUrl;
    protected ArrayList<PluginEntry> pluginEntries;
    protected CordovaInterfaceImpl cordovaInterface;

    protected void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(getActivity());
        preferences = parser.getPreferences();
        preferences.setPreferencesBundle(getActivity().getIntent().getExtras());
        //preferences.copyIntoIntentExtras(getActivity());
        launchUrl = parser.getLaunchUrl();
        pluginEntries = parser.getPluginEntries();
        // Config.parser = parser;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View visualView = inflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        //xWalkWebView =(XWalkView)visualView.findViewById(R.id.xwalkWebView);
        //xWalkWebView.load("https://threejs.org/examples/#webgl_geometry_teapot", null);
        /*
        cwv = (SystemWebView) visualView.findViewById(R.id.cordovaView);
        Config.init(getActivity());
        cwv.loadUrl("https://threejs.org/examples/#webgl_geometry_teapot");

        LayoutInflater localInflater = inflater.cloneInContext(new CordovaContext(getActivity(), this));
        View rootView = localInflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        myWebView = (CordovaWebView) rootView.findViewById(R.id.myWebView);
        Config.init(getActivity());
        myWebView.loadUrl(Config.getStartUrl());
        */
        return visualView;
        /*
        LayoutInflater localInflater = inflater.cloneInContext(new CordovaContext(getActivity(), this));

        View v = localInflater.inflate(R.layout.tab_fragment_visualviewer, container, false);

        cordovaInterface =  new CordovaInterfaceImpl(getActivity());
        if(savedInstanceState != null)
            cordovaInterface.restoreInstanceState(savedInstanceState);

        loadConfig();

        myWebView = new CordovaWebViewImpl(CordovaWebViewImpl.createEngine(getActivity(), preferences));

        //myWebView.getView().setId(100);
        RelativeLayout.LayoutParams wvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        //wvlp.addRule(RelativeLayout.BELOW,R.id.DialogTopBar);
        myWebView.getView().setLayoutParams(wvlp);

        if (!myWebView.isInitialized()) {
            myWebView.init(cordovaInterface, pluginEntries, preferences);
        }
        cordovaInterface.onCordovaInit(myWebView.getPluginManager());
        // webView = (SystemWebView)v.findViewById(R.id.myWebView);

        // Config.init(getActivity());
        ((RelativeLayout)v).addView(myWebView.getView());

        return v;
        */
    }

    @Override
    public void startActivityForResult(CordovaPlugin cordovaPlugin, Intent intent, int i) {

    }

    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {

    }

    @Override
    public Object onMessage(String s, Object o) {
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return null;
    }

    @Override
    public void requestPermission(CordovaPlugin cordovaPlugin, int i, String s) {

    }

    @Override
    public void requestPermissions(CordovaPlugin cordovaPlugin, int i, String[] strings) {

    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }
}
