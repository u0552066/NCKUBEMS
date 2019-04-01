package com.example.jason.nckubems;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Yan on 2018/3/8/008.
 */

public class Chart implements OnChartGestureListener {
    private Context context;

    public Chart(Context context) {
        super();
        this.context = context;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public LineData generateDataLine(String[] DataSet, ArrayList<Entry> array) {
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        for(int i = 0; i < DataSet.length; i++){
            Log.d("DataSet", String.valueOf(DataSet.length));
            LineDataSet d1 = new LineDataSet(array, DataSet[i]);
            d1.setLineWidth(2.5f);
            d1.setCircleRadius(4.5f);
            d1.setColors(Color.rgb(65, 105, 225));
            d1.setCircleColors(Color.rgb(65, 105, 225));
            d1.setHighLightColor(Color.rgb(244, 117, 117));
            d1.setDrawValues(false);
            sets.add(d1);
        }

        LineData cd = new LineData(sets);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public BarData generateDataBar(String[] DataSet, ArrayList<BarEntry> array) {
        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for(int i = 0; i < DataSet.length; i++) {
            Log.d("DataSet", String.valueOf(DataSet.length));
            BarDataSet d = new BarDataSet(array, DataSet[i]);
       // d.setColors(ColorTemplate.VORDIPLOM_COLORS);
            d.setColors(Color.rgb(65, 105, 225));
            d.setDrawValues(false); // 是否在點上加數值
            d.setHighLightAlpha(255);
            sets.add(d);
        }
        BarData cd = new BarData(sets);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    public PieData generateDataPie(String[] DataSet, String[] array) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < DataSet.length; i++) {
            entries.add(new PieEntry(Float.valueOf(array[i]), DataSet[i]));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d("Chart", "onChartGestureStart");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d("Chart", "onChartGestureEnd");
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.d("Chart", "onChartLongPressed");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.d("Chart", "onChartDoubleTapped");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.d("Chart", "onChartSingleTapped");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.d("Chart", "onChartFling");
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.d("Chart", "onChartScale");
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.d("Chart", "onChartTranslate");
    }
}
