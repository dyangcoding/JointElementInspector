package com.parsa_plm.Layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jointelementinspector.main.R;
import com.squareup.picasso.Picasso;

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;

    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.tab_fragment_photo, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.tab_fragment_photo, parent, false);
        }
        Picasso
                .with(context)
                .load(imageUrls[position])
                .resize(50,50)
                .placeholder(R.drawable.imageplaceholder48)
                .error(R.drawable.imageerror48)
                .centerCrop()
                .into((ImageView) convertView);
        return convertView;
    }
}
