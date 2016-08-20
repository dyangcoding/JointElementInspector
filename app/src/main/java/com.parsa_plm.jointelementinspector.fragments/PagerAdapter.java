package com.parsa_plm.jointelementinspector.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OverviewTabFragment overviewTabFragment = new OverviewTabFragment();
                return overviewTabFragment;
            case 1:
                ReportTabFragment reportTabFragment = new ReportTabFragment();
                return reportTabFragment;
            case 2:
                PhotoTabFragment photoTabFragment = new PhotoTabFragment();
                return photoTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
