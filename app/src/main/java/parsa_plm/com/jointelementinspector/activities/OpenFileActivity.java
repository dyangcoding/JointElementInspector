package parsa_plm.com.jointelementinspector.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import parsa_plm.com.jointelementinspector.helpers.ParseXMLFileTask;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;

import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.customLayout.FolderLayout;
import parsa_plm.com.jointelementinspector.interfaces.IFolderItemListener;
import parsa_plm.com.jointelementinspector.utils.AppConstants;

import java.io.File;

public class OpenFileActivity extends Activity implements IFolderItemListener {
    // 20170106: we use external storage path to open xml file
    // 20170611: use preference for activity persistent state
    private final String TAG = getClass().getName();
    private SharedPreferences mPrefs;
    private boolean backPressed = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        FolderLayout localFolders = (FolderLayout) findViewById(R.id.localFolders);
        localFolders.setIFolderItemListener(this);
        Button mBtnCancel = (Button) localFolders.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 20170611: onPause, onStop, onDestroy
                backPressed = true;
                finish();
            }
        });
        // 20161223: test internal storage path
        File path = this.getExternalFilesDir(null);
        localFolders.setDir(path.toString());
        mPrefs = getSharedPreferences(AppConstants.JOINT_ELEMENT_PREF, MODE_PRIVATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 20170611: reopen last activity
        String lastActivity = mPrefs.getString(AppConstants.LAST_ACTIVITY, null);
        if (lastActivity != null) {
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.clear();
            edit.apply();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!backPressed) {
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putString(AppConstants.LAST_ACTIVITY, TAG);
            edit.apply();
        }
    }
    @Override
    public void OnCannotFileRead(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_viewfile)
                .setTitle(file.getName() + "folder can't be read!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
    @Override
    public void OnFileClicked(final File file) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (!file.getName().toLowerCase().endsWith("xml")) {
            adb.setIcon(R.mipmap.ic_viewfile);
            adb.setMessage("Only File with .xml extension is accepted.");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            adb.show();
        } else {
            popSelectedFileDialog(file, adb);
        }
    }
    private void popSelectedFileDialog(final File file, AlertDialog.Builder adb) {
        adb.setIcon(R.mipmap.ic_viewfile);
        adb.setTitle("Open File: " + file.getName());
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 20161125: use async task to process file
                backPressed = true;
                new ParseXMLFileTask(OpenFileActivity.this, file.getPath()).execute();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adb.show();
    }
    /*20170611
     onPause is still been called after onBackPressed, so we need a flag
     lifecycle same as finish()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true;
    }
    private static void doKeepDialog(Dialog dialog){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }
}
