package parsa_plm.com.jointelementinspector.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jointelementinspector.main.R;
import parsa_plm.com.jointelementinspector.interfaces.ICustomItemClickListener;

import java.io.File;
import java.util.List;

public class DocumentGridAdapter extends RecyclerView.Adapter<DocumentGridAdapter.GridViewHolder> {
    private Context mContext;
    private List<File> mDocuments;
    // 20161223: add custom listener, avoid bind listener in onBindViewHolder
    private ICustomItemClickListener mItemClickListener;

    public DocumentGridAdapter(Context context, List<File> documents, ICustomItemClickListener listener) {
        this.mContext = context;
        this.mDocuments = documents;
        this.mItemClickListener = listener;
    }
    static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        GridViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.document_image_view);
            mTextView = (TextView) itemView.findViewById(R.id.document_text_view);
        }
    }
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.document_griditem, parent, false);
        final GridViewHolder viewHolder = new GridViewHolder(itemView);
        itemView.setTag(viewHolder);
        itemView.setOnClickListener(view ->
                mItemClickListener.onItemClick(view, viewHolder.getAdapterPosition()));
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        String file = mDocuments.get(position).getName();
        holder.mTextView.setText(file);
        holder.mImageView.setImageResource(R.mipmap.ic_pdf);
        // 20161223: bad practice, should not bind listener for every object here
        // setUpClickListener(mDocuments.get(position), holder.mImageView);
    }
    @Override
    public int getItemCount() {
        return mDocuments.size();
    }
    // 20170108 : methods to support swipe refresh layout
    public void clear() {
        mDocuments.clear();
    }
    public void addAll(List<File> list) {
        mDocuments.addAll(list);
    }
}
