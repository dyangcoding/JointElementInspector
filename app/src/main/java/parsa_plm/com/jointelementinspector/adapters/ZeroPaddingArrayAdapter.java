package parsa_plm.com.jointelementinspector.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ZeroPaddingArrayAdapter<T> extends ArrayAdapter{
    private ZeroPaddingArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }

    public static ArrayAdapter<CharSequence> createFromResource(Context context,
                                                                int textArrayResId, int textViewResId) {
        CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ZeroPaddingArrayAdapter<CharSequence>(context, textViewResId, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setPadding(0, view.getPaddingTop(), 0, view.getPaddingBottom()
        );
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View v = super.getDropDownView(position, convertView,parent);
        ((TextView) v).setGravity(Gravity.CENTER_HORIZONTAL);
        return v;
    }
}
