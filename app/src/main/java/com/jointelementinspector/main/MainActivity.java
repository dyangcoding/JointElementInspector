package com.jointelementinspector.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.parsa_plm.Layout.OpenFileActivity;
import com.parsa_plm.jointelementinspector.fragments.*;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OverviewTabFragment.onFragmentInteractionListener {
    private ActionMenuView amvMenu;
    private ExpandableListHeader headerData;
    // 20161101: make it global
    TabLayout tabLayout;
    private static final int REQUEST_CODE = 1;
    private static final String TITLE_OVERVIEW = "Overview";
    private static final String TITLE_Document = "Document";
    private static final String TITLE_Photos = "Photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 20161023: this code only for test
        //if (LeakCanary.isInAnalyzerProcess(this)) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        //return;
        //}
        //LeakCanary.install(getApplication());
        setContentView(R.layout.menu_toolbar);
        Toolbar menuToolBar = (Toolbar) findViewById(R.id.menu_toolbar);
        if (menuToolBar != null) {
            // hide the tool bar title
            menuToolBar.setTitle("");
            setSupportActionBar(menuToolBar);
            amvMenu = (ActionMenuView) menuToolBar.findViewById(R.id.amvMenu);
        }
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_OVERVIEW));
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_Document));
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_Photos));
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#3B0B17")));
        }
        setUpViewPager(tabLayout);
        // data transport from open file activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    // 20161031: this one should be used to obtain data
    // 20161101: debug result: get data successful from open file activity, but we still have to pass
    // data to view pager, maybe later check it out
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle data = intent.getExtras();
            headerData = (ExpandableListHeader) data.getParcelable("com.ExpandableListData");
        }
    }

    private void setUpViewPager(TabLayout tabLayout) {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        // add animation for view pager, test later
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainmenu, amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                Toast.makeText(getApplicationContext(), "Setting coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_openFromServer:
                Intent openFileIntent = new Intent(this, OpenFileActivity.class);
                startActivityForResult(openFileIntent, REQUEST_CODE);
                return true;
            case R.id.menu_save:
                Toast.makeText(getApplicationContext(), "Save coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_saveAs:
                Toast.makeText(getApplicationContext(), "SaveAs coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_JTViewer:
                Toast.makeText(getApplicationContext(), "JTViewer coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_openPDFFromServer:
                Toast.makeText(getApplicationContext(), "open PDF from Server coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_saveReport:
                Toast.makeText(getApplicationContext(), "Save Report coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_takePhoto:
                Toast.makeText(getApplicationContext(), "Take Photos coming soon", Toast.LENGTH_LONG).show();
                return true;
        }
        return true;
    }

    @Override
    public ExpandableListHeader onFragmentCreated() {
        return headerData != null ? headerData : null;
    }
}
