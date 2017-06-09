package parsa_plm.com.jointelementinspector.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import parsa_plm.com.jointelementinspector.fragments.OverviewTabFragment;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;


public class BaseTabFragment extends Fragment {
    private ExpandableListHeader headerData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public ExpandableListHeader getHeaderData() {
        return headerData;
    }
}
