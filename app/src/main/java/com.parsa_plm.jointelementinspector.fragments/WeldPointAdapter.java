package com.parsa_plm.jointelementinspector.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jointelementinspector.main.R;
import com.jointelementinspector.main.WeldPoint;
import com.parsa_plm.Layout.ZeroPaddingArrayAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeldPointAdapter extends BaseAdapter implements ListAdapter {
    private List<WeldPoint> items;
    private static Context mContext;

    public WeldPointAdapter(List<WeldPoint> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.weldpoints_row_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        WeldPoint wp = (WeldPoint) getItem(i);
        viewHolder.itemName.setText(wp.getName());
        // test view, itemType should be there
        viewHolder.itemType.setText(wp.getItemType());
        return view;
    }

    static class ViewHolder {
        TextView itemName;
        TextView itemType;
        @Bind(R.id.feld_Crack)
        Spinner mFeldCrack;
        @Bind(R.id.feld_CraterCrack)
        Spinner mFeldCraterCrack;
        @Bind(R.id.feld_SurfacePore)
        Spinner mFeldSurfacePore;
        @Bind(R.id.feld_EndCraterPipe)
        Spinner mFeldEndCraterPipe;
        @Bind(R.id.feld_LackOfFusion)
        Spinner mFeldLackOfFusion;
        @Bind(R.id.feld_IncRootPenetration)
        Spinner mFeldIncRootPenetration;
        @Bind(R.id.feld_ContinousUndercut)
        Spinner mFeldContinousUndercut;
        @Bind(R.id.feld_IntUndercut)
        Spinner mFeldIntUndercut;
        @Bind(R.id.feld_ShrinkGrooves)
        Spinner mFeldShrinkGrooves;
        @Bind(R.id.feld_ExcWeldMetal)
        Spinner mFeldExcWeldMetal;
        @Bind(R.id.feld_ExcConvex)
        Spinner mFeldExcConvex;
        @Bind(R.id.feld_ExcPenetration)
        Spinner mFeldExcPenetration;
        @Bind(R.id.feld_IncWeldToe)
        Spinner mFeldIncWeldToe;
        @Bind(R.id.feld_Overlap)
        Spinner mFeldOverlap;
        @Bind(R.id.feld_Sagging)
        Spinner mFeldSagging;
        @Bind(R.id.feld_BurnThrough)
        Spinner mFeldBurnThrough;
        @Bind(R.id.feld_IncFilledGroove)
        Spinner mFeldIncFilledGroove;
        @Bind(R.id.feld_ExcAsymFilledWeld)
        Spinner mFeldExcAsymFilledWeld;
        @Bind(R.id.feld_RootConcavity)
        Spinner mFeldRootConcavity;
        @Bind(R.id.feld_RootPorosity)
        Spinner mFeldRootPorosity;
        @Bind(R.id.feld_PoorRestart)
        Spinner mFeldPoorRestart;
        @Bind(R.id.feld_InsThroatThick)
        Spinner mFeldInsThroatThick;
        @Bind(R.id.feld_ExcThoratThick)
        Spinner mFeldExcThoratThick;
        @Bind(R.id.feld_ArcStrike)
        Spinner mFeldArcStrike;
        @Bind(R.id.feld_Spatter)
        Spinner mFeldSpatter;
        @Bind(R.id.feld_TempColours)
        Spinner mFeldTempColours;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            itemName = (TextView) view.findViewById(R.id.weldPoints_name);
            itemType = (TextView) view.findViewById(R.id.weldPoints_itemType);
            ArrayAdapter<CharSequence> adapter = ZeroPaddingArrayAdapter.createFromResource(mContext,
                    R.array.joinsValue_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setUpSpinner(adapter);
        }

        private void setUpSpinner(ArrayAdapter<CharSequence> adapter) {
            mFeldArcStrike.setPadding(0,0,0,0);
            mFeldArcStrike.setAdapter(adapter);
            mFeldBurnThrough.setAdapter(adapter);
            mFeldContinousUndercut.setAdapter(adapter);
            mFeldCrack.setAdapter(adapter);
            mFeldCraterCrack.setAdapter(adapter);
            mFeldEndCraterPipe.setAdapter(adapter);
            mFeldExcAsymFilledWeld.setAdapter(adapter);
            mFeldExcConvex.setAdapter(adapter);
            mFeldExcPenetration.setAdapter(adapter);
            mFeldExcThoratThick.setAdapter(adapter);
            mFeldExcWeldMetal.setAdapter(adapter);
            mFeldIncFilledGroove.setAdapter(adapter);
            mFeldIncRootPenetration.setAdapter(adapter);
            mFeldIntUndercut.setAdapter(adapter);
            mFeldOverlap.setAdapter(adapter);
            mFeldLackOfFusion.setAdapter(adapter);
            mFeldPoorRestart.setAdapter(adapter);
            mFeldTempColours.setAdapter(adapter);
            mFeldSurfacePore.setAdapter(adapter);
            mFeldSpatter.setAdapter(adapter);
            mFeldShrinkGrooves.setAdapter(adapter);
            mFeldSagging.setAdapter(adapter);
            mFeldRootPorosity.setAdapter(adapter);
            mFeldRootConcavity.setAdapter(adapter);
            mFeldInsThroatThick.setAdapter(adapter);
            mFeldIncWeldToe.setAdapter(adapter);
        }
    }
}
