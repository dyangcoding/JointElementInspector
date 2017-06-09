package parsa_plm.com.jointelementinspector.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import parsa_plm.com.jointelementinspector.base.BaseTabFragment;
import parsa_plm.com.jointelementinspector.helpers.WebViewInterface;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import com.jointelementinspector.main.R;

import org.xwalk.core.XWalkView;

import static parsa_plm.com.jointelementinspector.utils.AppConstants.URL;

public class VisualViewerFragment extends BaseTabFragment {
    private final String TAG = getClass().toString();
    private static int loadCounter = 0;
    @BindView(R.id.xwalkWebView)
    XWalkView xWalkWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View visualView = inflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        setUnBinder(ButterKnife.bind(this, visualView));
        if (xWalkWebView != null) {
            ExpandableListHeader headerData = getHeaderData();
            xWalkWebView.addJavascriptInterface(new WebViewInterface(getContext(), headerData), "Android");
            xWalkWebView.load(URL, null);
            Log.i(TAG, "onCreateView: load Counter" + String.valueOf(loadCounter++));
        }
        return visualView;
    }
    @Override
    public void onResume(){
        super.onResume();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onShow();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if (xWalkWebView != null)
            xWalkWebView.onDestroy();
    }
}
