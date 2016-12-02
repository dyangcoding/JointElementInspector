package com.parsa_plm.Layout;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.jointelementinspector.main.R;

import java.io.File;

public class ImageDisplayActivity extends Activity{
    private String file;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayimage);
        // Get Image Path
        file = getIntent().getExtras().getString("path");
        ImageView imageView = (ImageView) findViewById(R.id.activity_displayImageView);

        // Get Image
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}
