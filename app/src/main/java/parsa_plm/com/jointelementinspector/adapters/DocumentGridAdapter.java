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
    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        public GridViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.document_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(8, 8, 8, 8);
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
        holder.mImageView.setImageResource(R.drawable.pdf96);
        // 20161223: bad practice, should not bind listener for every object here
        // setUpClickListener(mDocuments.get(position), holder.mImageView);
    }
    // 20161223 not used any more
    private void setUpClickListener(final File file, ImageView imageview) {
        imageview.setOnClickListener(view -> {
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String fileParent = file.getParentFile().getName();
                File externalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                // 20161220: now the correct path
                File f = new File(externalPath + File.separator + fileParent + File.separator + file.getName());
                intent.setDataAndType(Uri.fromFile(f), "application/pdf");
                PackageManager pm = mContext.getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
                if (activities.size() > 0)
                    mContext.startActivity(intent);
                else
                    Toast.makeText(mContext, "There is no program installed to open pdf.", Toast.LENGTH_LONG).show();
            }
        });
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
