package parsa_plm.com.jointelementinspector.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;

public class WebViewInterface {
    private static final String TAG = "WebViewInterface";
    private Context mContext;
    private ExpandableListHeader mListHeader;
    public WebViewInterface(Context context, ExpandableListHeader listHeader) {
        this.mContext = context;
        this.mListHeader = listHeader;
    }

    // check if list header has weld points as children, and add sphere points
    // based on amount of points in visual fragment, just for test
    @org.xwalk.core.JavascriptInterface
    public boolean hasWeldPoint() {
        Toast.makeText(mContext, "from javascript interface", Toast.LENGTH_LONG).show();
        return this.mListHeader.getChildOfOccurrence().size() > 0;
    }

    @org.xwalk.core.JavascriptInterface
    public String getFilePath() {
        Log.i(TAG, "getFilePath: " + this.mListHeader.getFileDirectory());
        String filePath = this.mListHeader.getFileDirectory();
        Log.i(TAG, "getFilePath: " + filePath + File.separator + "MA.stl");
        if (filePath != null) {
            return filePath + File.separator + "MA.stl";
        }
        return null;
    }
}
