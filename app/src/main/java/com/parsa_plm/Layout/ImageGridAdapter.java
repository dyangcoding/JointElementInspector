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
    private List<File> mImages;
    // 20161223: add listener
    private CustomItemClickListener mItemClickListener;
    public ImageGridAdapter(Context context, List<File> images, CustomItemClickListener listener) {
        this.mContext = context;
        this.mImages = images;
        this.mItemClickListener = listener;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        public GridViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(8, 8, 8, 8);
        }
    }
    // 20161223: bind listener on view holder for better performance
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.imagegrid_item, parent, false);
        final GridViewHolder viewHolder = new GridViewHolder(itemView);
        itemView.setTag(viewHolder);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Picasso
                .with(mContext)
                .load(mImages.get(position))
                .placeholder(R.drawable.imageplaceholder48)
                .error(R.drawable.imageerror48)
                .resize(180,180)
                .onlyScaleDown()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void clear() {
        mImages.clear();
    }

    public void addAll(List<File> images) {
        mImages.addAll(images);
    }
}
