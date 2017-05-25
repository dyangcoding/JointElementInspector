package parsa_plm.com.jointelementinspector.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import parsa_plm.com.jointelementinspector.fragments.DocumentTabFragment;
import parsa_plm.com.jointelementinspector.fragments.OverviewTabFragment;
import parsa_plm.com.jointelementinspector.fragments.PhotoTabFragment;
import parsa_plm.com.jointelementinspector.fragments.VisualViewerFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    // 20170113: add visual viewer for 3d
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new OverviewTabFragment();
            case 1:
                return new VisualViewerFragment();
            case 2:
                return new DocumentTabFragment();
            case 3:
                return new PhotoTabFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
