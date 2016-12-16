package com.parsa_plm.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jointelementinspector.main.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder> {
    private Context mContext;
    private List<File> images;

    public ImageGridAdapter(Context context, List<File> images) {
        this.mContext = context;
        this.images = images;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        public GridViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.imagegrid_item, parent, false);
        GridViewHolder viewHolder = new GridViewHolder(itemView);
        viewHolder.mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        itemView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Picasso
                .with(mContext)
                .load(images.get(position))
                .placeholder(R.drawable.imageplaceholder48)
                .error(R.drawable.imageerror48)
                .resize(150,150)
                .onlyScaleDown()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
