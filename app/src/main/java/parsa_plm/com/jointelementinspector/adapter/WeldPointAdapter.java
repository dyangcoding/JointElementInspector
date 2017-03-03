package parsa_plm.com.jointelementinspector.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import parsa_plm.com.jointelementinspector.models.Occurrence;
import com.jointelementinspector.main.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeldPointAdapter extends BaseAdapter implements ListAdapter {
    private List<Occurrence> items;
    private static Context mContext;
    public WeldPointAdapter(List<Occurrence> items, Context context) {
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
        } else
            viewHolder = (ViewHolder) view.getTag();
        Occurrence wp = (Occurrence) getItem(i);
        viewHolder.itemName.setText(wp.getName());
        // test view, itemType should be there
        viewHolder.itemType.setText(wp.getItemType());
        viewHolder.result.setText("   OK  ");
        return view;
    }

    static class ViewHolder {
        static boolean allValid = true;
        TextView itemName;
        TextView itemType;
        @BindView(R.id.weldPoints_resultText)
        TextView result;
        @BindView(R.id.feld_Crack)
        Spinner mFeldCrack;
        @BindView(R.id.feld_CraterCrack)
        Spinner mFeldCraterCrack;
        @BindView(R.id.feld_SurfacePore)
        Spinner mFeldSurfacePore;
        @BindView(R.id.feld_EndCraterPipe)
        Spinner mFeldEndCraterPipe;
        @BindView(R.id.feld_LackOfFusion)
        Spinner mFeldLackOfFusion;
        @BindView(R.id.feld_IncRootPenetration)
        Spinner mFeldIncRootPenetration;
        @BindView(R.id.feld_ContinousUndercut)
        Spinner mFeldContinousUndercut;
        @BindView(R.id.feld_IntUndercut)
        Spinner mFeldIntUndercut;
        @BindView(R.id.feld_ShrinkGrooves)
        Spinner mFeldShrinkGrooves;
        @BindView(R.id.feld_ExcWeldMetal)
        Spinner mFeldExcWeldMetal;
        @BindView(R.id.feld_ExcConvex)
        Spinner mFeldExcConvex;
        @BindView(R.id.feld_ExcPenetration)
        Spinner mFeldExcPenetration;
        @BindView(R.id.feld_IncWeldToe)
        Spinner mFeldIncWeldToe;
        @BindView(R.id.feld_Overlap)
        Spinner mFeldOverlap;
        @BindView(R.id.feld_Sagging)
        Spinner mFeldSagging;
        @BindView(R.id.feld_BurnThrough)
        Spinner mFeldBurnThrough;
        @BindView(R.id.feld_IncFilledGroove)
        Spinner mFeldIncFilledGroove;
        @BindView(R.id.feld_ExcAsymFilledWeld)
        Spinner mFeldExcAsymFilledWeld;
        @BindView(R.id.feld_RootConcavity)
        Spinner mFeldRootConcavity;
        @BindView(R.id.feld_RootPorosity)
        Spinner mFeldRootPorosity;
        @BindView(R.id.feld_PoorRestart)
        Spinner mFeldPoorRestart;
        @BindView(R.id.feld_InsThroatThick)
        Spinner mFeldInsThroatThick;
        @BindView(R.id.feld_ExcThoratThick)
        Spinner mFeldExcThoratThick;
        @BindView(R.id.feld_ArcStrike)
        Spinner mFeldArcStrike;
        @BindView(R.id.feld_Spatter)
        Spinner mFeldSpatter;
        @BindView(R.id.feld_TempColours)
        Spinner mFeldTempColours;
        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
            itemName = (TextView) view.findViewById(R.id.weldPoints_name);
            itemType = (TextView) view.findViewById(R.id.weldPoints_itemType);
            // test version
            result = (TextView) view.findViewById(R.id.weldPoints_resultText);
            ArrayAdapter<CharSequence> adapter = ZeroPaddingArrayAdapter.createFromResource(mContext,
                    R.array.joinsValue_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setUpSpinner(adapter);
        }
        AdapterView.OnItemSelectedListener mListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "i.O":
                        view.setBackgroundColor(Color.GREEN);
                        break;
                    case "b.i.O":
                        view.setBackgroundColor(Color.YELLOW);
                        break;
                    case "n.i.O":
                        view.setBackgroundColor(Color.RED);
                        allValid = false;
                        break;
                    case "NA":
                        break;
                }
                // inform use about data changed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        private void setUpSpinner(ArrayAdapter<CharSequence> adapter) {
            mFeldArcStrike.setAdapter(adapter);
            mFeldArcStrike.setOnItemSelectedListener(mListener);
            mFeldBurnThrough.setAdapter(adapter);
            mFeldBurnThrough.setOnItemSelectedListener(mListener);
            mFeldContinousUndercut.setAdapter(adapter);
            mFeldContinousUndercut.setOnItemSelectedListener(mListener);
            mFeldCrack.setAdapter(adapter);
            mFeldCrack.setOnItemSelectedListener(mListener);
            mFeldCraterCrack.setAdapter(adapter);
            mFeldCraterCrack.setOnItemSelectedListener(mListener);
            mFeldEndCraterPipe.setAdapter(adapter);
            mFeldEndCraterPipe.setOnItemSelectedListener(mListener);
            mFeldExcAsymFilledWeld.setAdapter(adapter);
            mFeldExcAsymFilledWeld.setOnItemSelectedListener(mListener);
            mFeldExcConvex.setAdapter(adapter);
            mFeldExcConvex.setOnItemSelectedListener(mListener);
            mFeldExcPenetration.setAdapter(adapter);
            mFeldExcPenetration.setOnItemSelectedListener(mListener);
            mFeldExcThoratThick.setAdapter(adapter);
            mFeldExcThoratThick.setOnItemSelectedListener(mListener);
            mFeldExcWeldMetal.setAdapter(adapter);
            mFeldExcWeldMetal.setOnItemSelectedListener(mListener);
            mFeldIncFilledGroove.setAdapter(adapter);
            mFeldIncFilledGroove.setOnItemSelectedListener(mListener);
            mFeldIncRootPenetration.setAdapter(adapter);
            mFeldIncRootPenetration.setOnItemSelectedListener(mListener);
            mFeldIntUndercut.setAdapter(adapter);
            mFeldIntUndercut.setOnItemSelectedListener(mListener);
            mFeldOverlap.setAdapter(adapter);
            mFeldOverlap.setOnItemSelectedListener(mListener);
            mFeldLackOfFusion.setAdapter(adapter);
            mFeldLackOfFusion.setOnItemSelectedListener(mListener);
            mFeldPoorRestart.setAdapter(adapter);
            mFeldPoorRestart.setOnItemSelectedListener(mListener);
            mFeldTempColours.setAdapter(adapter);
            mFeldTempColours.setOnItemSelectedListener(mListener);
            mFeldSurfacePore.setAdapter(adapter);
            mFeldSurfacePore.setOnItemSelectedListener(mListener);
            mFeldSpatter.setAdapter(adapter);
            mFeldSpatter.setOnItemSelectedListener(mListener);
            mFeldShrinkGrooves.setAdapter(adapter);
            mFeldShrinkGrooves.setOnItemSelectedListener(mListener);
            mFeldSagging.setAdapter(adapter);
            mFeldSagging.setOnItemSelectedListener(mListener);
            mFeldRootPorosity.setAdapter(adapter);
            mFeldRootPorosity.setOnItemSelectedListener(mListener);
            mFeldRootConcavity.setAdapter(adapter);
            mFeldRootConcavity.setOnItemSelectedListener(mListener);
            mFeldInsThroatThick.setAdapter(adapter);
            mFeldInsThroatThick.setOnItemSelectedListener(mListener);
            mFeldIncWeldToe.setAdapter(adapter);
            mFeldIncWeldToe.setOnItemSelectedListener(mListener);
        }
    }
}
