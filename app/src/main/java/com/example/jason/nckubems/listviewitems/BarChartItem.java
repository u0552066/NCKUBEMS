package com.example.jason.nckubems.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.example.jason.nckubems.MyMarkerView;
import com.example.jason.nckubems.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class BarChartItem extends ChartItem {

    private Typeface mTf;
    private String title, x, y;
    private Context context;
    private ArrayList<String> label;
    protected String[] values = new String[]{
            "1"
    };

    public BarChartItem(ChartData<?> cd, Context c, String title, ArrayList<String> label, String yUnit, String xUnit) {
        super(cd);
        this.title = title;
        this.label = label;
        this.context = c;
        this.x = xUnit;
        this.y = yUnit;

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.y_unit = (TextView) convertView.findViewById(R.id.tv_y_unit);
            holder.x_unit = (TextView) convertView.findViewById(R.id.tv_x_unit);
            holder.y_unit.setText(y);
            holder.x_unit.setText(x);
            RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
            ranim.setFillAfter(true); //For the button to remain at the same place after the rotation
            holder.y_unit.setAnimation(ranim);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(title);
        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(2);
        xAxis.setLabelCount(5, false);
        //自定义x轴显示
        MyXFormatter formatter = new MyXFormatter(values);
//        xAxis.setValueFormatter(formatter);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if( value%1 == 0)
                    return label.get((int)value);
                else
                    return " ";
            }
        });

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setSpaceTop(20f);
        //y軸最小值
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        //y軸最小值
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChartData.setValueTypeface(mTf);


        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view_layout, y);
        mv.setChartView(holder.chart);
        holder.chart.setMarker(mv);
        // set data
        holder.chart.setData((BarData) mChartData);
        holder.chart.setFitBars(true);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
        TextView tv_title, y_unit, x_unit;
    }
}
