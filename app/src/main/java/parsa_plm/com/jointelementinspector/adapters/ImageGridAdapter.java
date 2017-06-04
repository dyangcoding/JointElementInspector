package parsa_plm.com.jointelementinspector.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jointelementinspector.main.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import parsa_plm.com.jointelementinspector.interfaces.ICustomItemClickListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder> {
    private Context mContext;
    private List<File> mImages;
    // 20161223: add listener
    private ICustomItemClickListener mItemClickListener;
    public ImageGridAdapter(Context context, List<File> images, ICustomItemClickListener listener) {
        this.mContext = context;
        this.mImages = images;
        this.mItemClickListener = listener;
    }
    static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        GridViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
        }
    }
    // 20161223: bind listener on view holder for better performance
    // 20170206: use lambda expression now
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.imagegrid_item, parent, false);
        final GridViewHolder viewHolder = new GridViewHolder(itemView);
        itemView.setTag(viewHolder);
        itemView.setOnClickListener(v ->
                mItemClickListener.onItemClick(v, viewHolder.getAdapterPosition()));
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Picasso
                .with(mContext)
                .load(mImages.get(position))
                .placeholder(R.mipmap.ic_loadimageplaceholder)
                .error(R.mipmap.ic_loadimageerror)
                .resize(350,350)
                .centerCrop()
                .into(holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mImages != null ? mImages.size() : 0;
    }
    // 20170108 : methods to support swipe refresh layout
    public void clear() {
        mImages.clear();
    }
    public void addAll(List<File> images) {
        mImages.addAll(images);
    }
}
