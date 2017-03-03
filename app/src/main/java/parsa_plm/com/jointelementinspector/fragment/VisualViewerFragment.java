package parsa_plm.com.jointelementinspector.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import com.jointelementinspector.main.R;

import org.xwalk.core.XWalkView;

public class VisualViewerFragment extends Fragment {
    private XWalkView xWalkWebView;
    private ExpandableListHeader headerData;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View visualView = inflater.inflate(R.layout.tab_fragment_visualviewer, container, false);
        xWalkWebView =(XWalkView)visualView.findViewById(R.id.xwalkWebView);
        //xWalkWebView.addJavascriptInterface();
        return visualView;
    }

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
        if (listener != null) headerData = listener.onFragmentCreated();
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
