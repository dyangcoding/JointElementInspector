package parsa_plm.com.jointelementinspector.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/*
 20161216: we do not need this adapter any more, use recycler view for better usability
 */
public class ImageListAdapter extends BaseAdapter {
    private Context mContext;
    // 20161214: use array for later document view
    private File[] images;
    public ImageListAdapter(Context context, File[] imageUrls) {
        this.mContext = context;
        images = imageUrls;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (null == convertView) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }else
            imageView = (ImageView) convertView;
        imageView.setImageResource(position);
        return imageView;
    }

    private void setUpClickListener(final File image, ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("path-to-document"));
                intent.setType("application/pdf");
                PackageManager pm = mContext.getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
                if (activities.size() > 0) {
                    mContext.startActivity(intent);
                } else {
                    // Do something else here. Maybe pop up a Dialog or Toast
                }
            }
        });
    }
}
