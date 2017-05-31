package parsa_plm.com.jointelementinspector.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

import parsa_plm.com.jointelementinspector.models.Occurrence;

public class Utility {
    public static int calculateColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / 180);
    }
    //20170504: generate matrix for transform form one row array of weld point
    public static double[][] generateMatrix(String array) {
        if (array == null || array.isEmpty())
            return null;
        double[][] result = new double[4][4];
        String[] subArray = array.split(" ");
        int laufSubArray = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[j][i] = Double.valueOf(subArray[laufSubArray]);
                ++laufSubArray;
            }
        }
        return result;
    }
    // 20170504: deal with weld point transform matrix multiplication and return the position of
    // weld points
    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                for(int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }
    // 20170504: convert list of occurrence to json
    public static String convertList2Json(List<Occurrence> list) throws IOException {
        if (list == null || list.size() == 0)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(list);
    }
    // 20170531: used to scale the position of weld points
    public static int scalePosition(double position) {
        double scale = position * 1000;
        return Double.valueOf(scale).intValue();
    }
}
