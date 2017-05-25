package parsa_plm.com.jointelementinspector.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import parsa_plm.com.jointelementinspector.helpers.ParseXMLFileTask;
import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;

import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.customLayout.FolderLayout;
import parsa_plm.com.jointelementinspector.interfaces.IFolderItemListener;

import java.io.File;

public class OpenFileActivity extends Activity implements IFolderItemListener {
    private FolderLayout localFolders;
    // 20170106: we use external storage path to open xml file
    // private final String filePath = "/sdcard/Download";
    private Context mContext;
    private Button mBtnCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);
        this.mContext = getApplicationContext();
        localFolders = (FolderLayout) findViewById(R.id.localFolders);
        localFolders.setIFolderItemListener(this);
        mBtnCancel = (Button) localFolders.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 20161223: test internal storage path
        File path = this.getExternalFilesDir(null);
        localFolders.setDir(path.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void OnCannotFileRead(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.viewfile48)
                .setTitle(file.getName() + "folder can't be read!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void OnFileClicked(final File file) {
        final ExpandableListHeader expandableListHeader = null;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (!file.getName().toLowerCase().endsWith("xml")) {
            adb.setIcon(R.drawable.viewfile48);
            adb.setMessage("Only File with .xml extension is accepted.");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            adb.show();
        } else {
            adb.setIcon(R.drawable.viewfile48);
            adb.setTitle("Open File: " + file.getName());
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // 20161125: use async task to process file
                    new ParseXMLFileTask(OpenFileActivity.this, OpenFileActivity.this, file.getPath()).execute();
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        }
    }
}
