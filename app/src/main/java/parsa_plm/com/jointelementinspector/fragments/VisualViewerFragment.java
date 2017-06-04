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
import parsa_plm.com.jointelementinspector.helpers.WebViewInterface;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import com.jointelementinspector.main.R;

import org.xwalk.core.XWalkView;

public class VisualViewerFragment extends Fragment {
    private final String TAG = getClass().toString();
    private static int loadCounter = 0;
    @BindView(R.id.xwalkWebView)
    XWalkView xWalkWebView;
    private ExpandableListHeader headerData;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Activity mainActivity = null;
        OverviewTabFragment.onFragmentInteractionListener listener;
        try {
            if (context instanceof Activity)
                mainActivity = (Activity) context;
            listener = (OverviewTabFragment.onFragmentInteractionListener) mainActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mainActivity.toString() + "must implement onFragmentInteractionListener");
        }
        if (listener != null)
            headerData = listener.onFragmentCreated();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View visualView = inflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        ButterKnife.bind(this, visualView);
        if (xWalkWebView != null) {
            xWalkWebView.addJavascriptInterface(new WebViewInterface(mContext, headerData), "Android");
            xWalkWebView.load("file:///android_asset/crosswalkWeb/three.js-dev/editor/index.html", null);
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
