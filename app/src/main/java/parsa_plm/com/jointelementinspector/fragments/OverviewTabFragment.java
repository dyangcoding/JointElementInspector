package parsa_plm.com.jointelementinspector.fragments;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jointelementinspector.main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import parsa_plm.com.jointelementinspector.base.BaseTabFragment;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.utils.AppConstants;


public class OverviewTabFragment extends BaseTabFragment {
    private final String TAG = getClass().toString();
    // 20170221: get reference of parent view pager
    private ViewPager mViewPager;
    // 20170223: vertical scroll view should automatically scroll down when structure list get expanded
    @BindView(R.id.scrollView_overview)
    ScrollView mScrollView;
    private Unbinder mUnbinder;

    public OverviewTabFragment() {
        setArguments(new Bundle());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewPager = (ViewPager) container;
        View view = inflater.inflate(R.layout.tab_fragment_overview, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        ButterKnife.setDebug(true);
        ExpandableListHeader headerData = getHeaderData();
        // 20160902: no data no layout
        if (headerData != null) {
            // 20160824: getChildFragmentManager instead of getFragmentManager
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
            InspectionHeaderFragment headerFragment = InspectionHeaderFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_inspectionHeader, headerFragment, AppConstants.INSPECTOR_HEADER_FRAGMENT);
            // 20160824: add product structure fragment
            ProductStructureFragment productStructureFragment = ProductStructureFragment.newInstance(headerData);
            childFragTrans.add(R.id.fragment_placeHolder_productStructure, productStructureFragment, AppConstants.PRODUCT_STRUCTURE_FRAGMENT);
            childFragTrans.commit();
        }
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        int verticalPosition = bundle.getInt(AppConstants.OVERVIEW_VERTIVAL_POSITION);
        if (mScrollView != null)
          mScrollView.scrollTo(0, verticalPosition);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mScrollView != null)
            outState.putInt(AppConstants.OVERVIEW_VERTIVAL_POSITION, mScrollView.getScrollY());
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mScrollView != null)
            getArguments().putInt(AppConstants.OVERVIEW_VERTIVAL_POSITION, mScrollView.getScrollY());
        Log.i(TAG, "onPause: " + AppConstants.OVERVIEW_VERTIVAL_POSITION + String.valueOf(mScrollView.getScrollY()));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            int verticalPosition = getArguments() != null ? getArguments().getInt(AppConstants.OVERVIEW_VERTIVAL_POSITION) : 0;
            if (mScrollView != null)
                mScrollView.smoothScrollTo(0, verticalPosition);
        }
    }
    public interface onFragmentInteractionListener {
        public ExpandableListHeader onFragmentCreated();
    }
    //20170221: get reference to parent view pager so that we could active visual view pager
    public ViewPager getParentViewPager() {
        return mViewPager != null ? mViewPager : null;
    }
    // 20170223: get reference of scroll view to set listener in child fragment
    public ScrollView getScrollView() {
        return mScrollView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
