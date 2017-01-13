package com.parsa_plm.Layout;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.jointelementinspector.main.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDisplayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayimage);
        Context mContext = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 20170112: get the screen width and set high of resize to 0 to keep aspect ratio
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        String file = getIntent().getExtras().getString("path");
        if (file != null) {
            File f = new File(file);
            ImageView imageView = (ImageView) findViewById(R.id.activity_displayImageView);
            Picasso picasso = Picasso.with(mContext);
            picasso.setIndicatorsEnabled(true);
            picasso
                    .load(f)
                    .resize(width, 0)
                    .onlyScaleDown()
                    .into(imageView);
        }
    }
}
