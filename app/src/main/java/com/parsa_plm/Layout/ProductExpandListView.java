package com.parsa_plm.Layout;

import android.content.Context;
import android.widget.ExpandableListView;

public class ProductExpandListView extends ExpandableListView{
    public ProductExpandListView(Context context) {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
