package parsa_plm.com.jointelementinspector.activities;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.jointelementinspector.main.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import parsa_plm.com.jointelementinspector.utils.AppConstants;

public class ImageDisplayActivity extends Activity {
    private boolean onBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayimage);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 20170112: get the screen width and set high of resize to 0 to keep aspect ratio
        int width = metrics.widthPixels;
        String file = getIntent().getExtras().getString("path");
        if (file != null) {
            File f = new File(file);
            ImageView imageView = (ImageView) findViewById(R.id.activity_displayImageView);
            Picasso picasso = Picasso.with(getBaseContext());
            picasso.setIndicatorsEnabled(true);
            picasso
                    .load(f)
                    .resize(width, 0)
                    .onlyScaleDown()
                    .into(imageView);
        }
    }
    /*
        20170615:
        basically we do not need to set preference in onPause, cause default value of getBoolean
        from preferences is false, however that default value works not always by tests, so we do
        it here once again
    */
    @Override
    protected void onPause() {
        super.onPause();
        if (!onBackPressed)
            setPreference(false);
    }
    /*
        20170615:
        we need global flag to check if this activity was paused from onBackPressed
        or because of other activity switched to foreground and make this one paused
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackPressed = true;
        setPreference(true);
    }
    private void setPreference(boolean value) {
        SharedPreferences prefs = getSharedPreferences(AppConstants.JOINT_ELEMENT_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(AppConstants.PAUSED_ON_BACK_PRESSED, value);
        edit.apply();
    }
}
