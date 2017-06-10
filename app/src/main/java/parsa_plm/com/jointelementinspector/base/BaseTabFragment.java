package parsa_plm.com.jointelementinspector.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.fragments.OverviewTabFragment;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;


public class BaseTabFragment extends Fragment {
    private ExpandableListHeader headerData;
    private Unbinder mUnBinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity hostedActivity = null;
        OverviewTabFragment.onFragmentInteractionListener listener;
        try {
            if (context instanceof Activity)
                hostedActivity = (Activity) context;
            listener = (OverviewTabFragment.onFragmentInteractionListener) hostedActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(hostedActivity.toString() + "must implement onFragmentInteractionListener");
        }
        if (listener != null)
            headerData = listener.onFragmentCreated();
    }
    // 20170609: enable sub fragment to access header data
    public final ExpandableListHeader getHeaderData() {
        return headerData;
    }
    public void setUnBinder(Unbinder unBinder) { mUnBinder = unBinder; }
    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }
}
