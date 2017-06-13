package parsa_plm.com.jointelementinspector.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import parsa_plm.com.jointelementinspector.models.ExpandableListHeader;
import parsa_plm.com.jointelementinspector.models.ExpandableListItem;
import parsa_plm.com.jointelementinspector.models.Occurrence;
import parsa_plm.com.jointelementinspector.utils.Utility;

public class WebViewInterface {
    private static final String TAG = "WebViewInterface";
    private Context mContext;
    private ExpandableListHeader mListHeader;
    private int weldPointCount = 0;

    public WebViewInterface(Context context, ExpandableListHeader listHeader) {
        this.mContext = context;
        this.mListHeader = listHeader;
    }
    // check if list header has weld points as children, and add sphere points
    // based on amount of points in visual fragment, just for test
    @org.xwalk.core.JavascriptInterface
    public boolean hasWeldPoint() {
        boolean hasWeldPoint = false;
        for (ExpandableListItem item : this.mListHeader.getChildOfOccurrence()) {
            if (item.getChildItemList().size() > 0) {
                hasWeldPoint = true;
                weldPointCount = item.getChildItemList().size();
            }
        }
        return hasWeldPoint;
    }
    @org.xwalk.core.JavascriptInterface
    public String getWeldPointsJson() throws IOException {
        for (ExpandableListItem item: this.mListHeader.getChildOfOccurrence()) {
            if (item.getChildItemList().size() > 0) {
                Log.i(TAG, "getWeldPointsJson: " + Utility.convertList2Json(item.getChildItemList()));
                return Utility.convertList2Json(item.getChildItemList());
            }
        }
        return null;
    }
    @org.xwalk.core.JavascriptInterface
    public int getWeldPointCount() {
        return weldPointCount;
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
    @org.xwalk.core.JavascriptInterface
    public String getFileName() {
        return this.mListHeader.getPartName();
    }
}
