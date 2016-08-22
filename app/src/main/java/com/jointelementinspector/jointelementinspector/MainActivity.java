package com.jointelementinspector.jointelementinspector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parsa_plm.folderLayout.ExpandableListData;
import com.parsa_plm.folderLayout.OpenFileActivity;
import com.parsa_plm.jointelementinspector.fragments.*;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private ActionMenuView amvMenu;
    private ExpandableListData headerData;
    private List<ExpandableListData> list;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_toolbar);
        Toolbar menuToolBar = (Toolbar) findViewById(R.id.menu_toolbar);
        if (menuToolBar != null) {
            setSupportActionBar(menuToolBar);
            amvMenu = (ActionMenuView) menuToolBar.findViewById(R.id.amvMenu);
        }
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText("Overview"));
            tabLayout.addTab(tabLayout.newTab().setText("Report"));
            tabLayout.addTab(tabLayout.newTab().setText("Photo"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#3B0B17")));
        }
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (headerData != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("com.ExpandableListData", headerData);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    pagerAdapter.getItem(0).setArguments(bundle);
                    fragmentManager.findFragmentById(R.id.fragment_placeHolder_inspectionHeader).setArguments(bundle);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // data transport from open file activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ExpandableListData data = bundle.getParcelable("com.ExpandableListData");
            if (data != null) {
                Toast.makeText(this, "Result: " + data.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainmenu, amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // test case
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Toast.makeText(getApplicationContext(), "Setting coming soon", Toast.LENGTH_LONG).show();

            return true;
        }

        if (id == R.id.menu_openFromServer) {
            Intent openFileIntent = new Intent(MainActivity.this, OpenFileActivity.class);
            startActivityForResult(openFileIntent, REQUEST_CODE);
            return true;
        }

        if (id == R.id.menu_save) {
            Toast.makeText(getApplicationContext(), "Save comming soon", Toast.LENGTH_LONG).show();

            return true;
        }

        if (id == R.id.menu_saveAs) {
            Toast.makeText(getApplicationContext(), "SaveAs comming soon", Toast.LENGTH_LONG).show();

            return true;
        }
        return true;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            headerData = intent.getParcelableExtra("com.ExpandableListData");
            if (headerData != null) {
                Toast.makeText(this, "Result: " + "result is null" , Toast.LENGTH_LONG).show();
            }
        }
        android.support.v4.app.Fragment inspectorHeader = getSupportFragmentManager().findFragmentById(R.id.fragment_placeHolder_inspectionHeader);
        inspectorHeader.onActivityResult(requestCode, resultCode, intent);
    }
    */
}
