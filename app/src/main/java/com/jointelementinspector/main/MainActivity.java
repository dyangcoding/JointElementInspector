package com.jointelementinspector.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import com.parsa_plm.Layout.OpenFileActivity;
import com.parsa_plm.jointelementinspector.fragments.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OverviewTabFragment.onFragmentInteractionListener {
    private ActionMenuView amvMenu;
    private ExpandableListHeader headerData;
    private Context mContext;
    // 20161101: make it global
    TabLayout tabLayout;
    private static final int REQUEST_CODE = 1;
    private final int CAMERA_CAPTURE = 2;
    // TODO all final string should replaced for multi language support
    private static final String TITLE_OVERVIEW = "Overview";
    private static final String TITLE_DOCUMENT = "Document";
    private static final String TITLE_PHOTOS = "Photo";
    private boolean inSpecificFolder = false;
    private String mSpecificFolder;
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
        mContext = this;
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
        // 2016114: extract method
        setUpTab();
        setUpViewPager(tabLayout);
        // data transport from open file activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            headerData = bundle.getParcelable("com.ExpandableListData");
        }
    }

    private void setUpTab() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_OVERVIEW));
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_DOCUMENT));
            tabLayout.addTab(tabLayout.newTab().setText(TITLE_PHOTOS));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#3B0B17")));
            tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        }
    }

    // 20161031: this one should be used to obtain data
    // 20161101: debug result: get data successful from open file activity, but we still have to pass
    // data to view pager, maybe later check it out
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        File photoFile = null;
        FileOutputStream outputStream = null;
        Bitmap bitmap = null;
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CAPTURE) {
            try {
                photoFile = createImageFile();
                outputStream = new FileOutputStream(photoFile);
                bitmap = (Bitmap) intent.getExtras().get("data");
                if (bitmap != null)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException ex) {
                Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            // 20170108: inform user where the photo was stored
            if (inSpecificFolder)
                Toast.makeText(mContext, " Photo is stored in: " + mSpecificFolder, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mContext, " Photo is stored in: " + mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpViewPager(final TabLayout tabLayout) {
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
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.setSelectedTabIndicatorColor(Color.BLUE);
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(Color.RED);
                        break;
                    case 2:
                        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
                        break;
                }
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
                Toast.makeText(mContext, "Setting coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_openFromServer:
                Intent openFileIntent = new Intent(this, OpenFileActivity.class);
                startActivityForResult(openFileIntent, REQUEST_CODE);
                return true;
            case R.id.menu_save:
                Toast.makeText(mContext, "Save coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_saveAs:
                Toast.makeText(mContext, "SaveAs coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_JTViewer:
                Toast.makeText(mContext, "JTViewer coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_openPDFFromServer:
                Toast.makeText(mContext, "open PDF from Server coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_saveReport:
                Toast.makeText(mContext, "Save Report coming soon", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_takePhoto:
                onPrepareCapturePhoto();
                return true;
        }
        return true;
    }

    // 20161215: should check if device has camera and inform user
    private void onPrepareCapturePhoto() {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(mContext, "Your Device has no camera, break. ", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isExternalStorageWritable()) {
            Toast.makeText(mContext, "External Storage is not available, break.", Toast.LENGTH_LONG).show();
            return;
        }
        onCapturePhoto();
    }

    private void onCapturePhoto() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (this.headerData == null) {
            adb.setIcon(R.drawable.attention48);
            adb.setTitle("Photograhieren ohne Anzeigen");
            adb.setMessage("Sie haben noch keine XML Datei geöffnet, wenn Sie " +
                    "fortfahren, sind neu gemachte Bilder nicht in diesem Programm anzuzeigen. ");
            adb.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    captureImage();
                }
            });
            adb.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        } else {
            // capture image and update grid view for result
            // should check if the specific folder exist
            // communication between main activity and tab photo fragment,
            // also between tab document fragment
            String externalDir = mContext.getExternalFilesDir(null).toString();
            String[] xmlFileDir = headerData.getFileDirectory().split("/");
            String specificFolder = externalDir + File.separator + xmlFileDir[xmlFileDir.length - 1];
            File file = new File(specificFolder);
            if (file.isDirectory() && file.exists()) {
                mSpecificFolder = specificFolder;
                inSpecificFolder = true;
                captureImage();
            } else {
                adb.setIcon(R.drawable.attention48);
                adb.setTitle("keine Ordner gefunden ! ");
                adb.setMessage("Der Ordner, in dem Bilder zu speichern sind, existiert nicht. Stellen Sie sicher," +
                        "dass XML Dateien aus Teamcenter korreckt exportiert werden. ");
                adb.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }
    }

    // store images in specific folder if withSpecificFolder is true, argument file could be null
    // 20161215: TODO should create image file
    // 20170107: just use intent to capture photos und push data in the file by onActivityResult
    private void captureImage() {
        try {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        }
        catch (ActivityNotFoundException e) {
            String errorMessage = " your device doesn't support capturing images! ";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // 20161220 image now successfully stored in specific folder
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        String storageDir = null;
        if (inSpecificFolder)
            storageDir = mSpecificFolder;
        else
            storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File f = new File(storageDir);
        File image = File.createTempFile(imageFileName, ".jpg", f);
        return image;
    }

    @Override
    public ExpandableListHeader onFragmentCreated() {
        return headerData != null ? headerData : null;
    }

    // check if external storage available for write und inform user
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.attention48)
                .setTitle("Programm Beenden")
                .setMessage("Möchten Sie wirklich das Programm beenden ? ")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
