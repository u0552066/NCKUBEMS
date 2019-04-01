package com.example.jason.nckubems;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
//顯示數值
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private String y;

    public MyMarkerView(Context context, int layoutResource, String yUnit) {
        super(context, layoutResource);
        this.y = yUnit;

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + " " + y);
            Log.d("XXX",Utils.formatNumber(ce.getHigh(), 0, true).toString());
        } else {

            tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true) + " " + y);
            Log.d("YYY",Utils.formatNumber(e.getY(), 0, true).toString());
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
