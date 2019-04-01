package com.example.jason.nckubems.GreenEnergy;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.Chart;
import com.example.jason.nckubems.ChartDataAdapter;
import com.example.jason.nckubems.R;
import com.example.jason.nckubems.listviewitems.BarChartItem;
import com.example.jason.nckubems.listviewitems.ChartItem;
import com.example.jason.nckubems.listviewitems.LineChartItem;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WindHistoryFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int mYear, mMonth, mDay;
    private EditText txtTime;
    private TextView title;
    private SharedPreferences setting;
    private String token;
    private RequestQueue requestQueue;
    private String getPV = "xlii_bems_api/pv.php";
    private StringRequest Objectrequest;
    private ListView lv,lv2;
    private Chart chart;
    // TODO: Rename and change types of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隱藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wind_history, container, false);
    }
    @Override
    public void onStart() {
        //設置對話框的寬高
        getDialog().getWindow().getAttributes().width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().getAttributes().height = getResources().getDisplayMetrics().heightPixels - 500;
        super.onStart();
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //進入含有EditText的Activity時，不自動彈出虛擬鍵盤
        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        String ip = setting.getString("ip","");
        int Comparesurl = getPV.indexOf("//");
        chart = new Chart(getActivity());
        if(Comparesurl == -1) {
            getPV = ip + getPV;
            getPV = "http://59.125.180.237/xlii_bems_api/pv.php";

        }
        lv = (ListView) getView().findViewById(R.id.listView_wind1);
        lv2 = (ListView) getView().findViewById(R.id.listView_wind2);

        txtTime = (EditText) getView().findViewById(R.id.tv_time);
        TextView title = (TextView) getView().findViewById(R.id.tv_chart_title);
        title.setText("風力累計發電圖表");
        Button btnNow = (Button) getView().findViewById(R.id.btn_now);
        Button btnDay = (Button) getView().findViewById(R.id.btn_day);
        Button btnMonth = (Button) getView().findViewById(R.id.btn_month);
        Button btnYear = (Button) getView().findViewById(R.id.btn_year);
        btnNow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
                String[] date = formatter.format(curDate).split("-");
                Log.d("now", formatter.format(curDate));
                txtTime.setVisibility(View.GONE);
                getPVHistoryChart("getHistoryInNow", Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
            }
        });
        btnNow.callOnClick();
        btnDay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year,month,day);
                        txtTime.setText(format);
                        txtTime.setVisibility(View.VISIBLE);
                        getPVHistoryChart("getHistoryInDay", year, month, day);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
        btnMonth.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setMonthFormat(year,month);
                        txtTime.setText(format);
                        txtTime.setVisibility(View.VISIBLE);
                        getPVHistoryChart("getHistoryInMonth", year, month, 0);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
        btnYear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = String.valueOf(year);
                        txtTime.setText(format);
                        txtTime.setVisibility(View.VISIBLE);
                        getPVHistoryChart("getHistoryInYear", year, 0, 0);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
    }

    private String setDateFormat(int year, int monthOfYear, int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }
    private String setMonthFormat(int year, int monthOfYear){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1);
    }
    public void getPVHistoryChart(final String button, int year, int month, int day) {
        final JSONObject body = new JSONObject();
        switch (button) {
            case "getHistoryInYear":
                try {
                    body.put("action", button);
                    body.put("field", "xinglong2");
                    body.put("cluster", "2");
                    body.put("token", "b8722a4431d6e749314dae271ec8c8f");
                    body.put("year", String.valueOf(year));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getHistoryInMonth":
                try {
                    body.put("action", button);
                    body.put("field", "xinglong2");
                    body.put("cluster", "2");
                    body.put("token", "b8722a4431d6e749314dae271ec8c8f");
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month + 1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getHistoryInDay":
                try {
                    body.put("action", button);
                    body.put("field", "xinglong2");
                    body.put("cluster", "2");
                    body.put("token", "b8722a4431d6e749314dae271ec8c8f");
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month + 1));
                    body.put("day", String.valueOf(day));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getHistoryInNow":
                try {
                    body.put("action", button);
                    body.put("field", "xinglong2");
                    body.put("cluster", "2");
                    body.put("token", "b8722a4431d6e749314dae271ec8c8f");
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month + 1));
                    body.put("day", String.valueOf(day));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
            Objectrequest = new StringRequest(Request.Method.POST, getPV, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<ChartItem> list = new ArrayList<ChartItem>(),list2 = new ArrayList<ChartItem>();
                    ArrayList<BarEntry> BarEnergy = new ArrayList<BarEntry>();
                    ArrayList<Entry> LinePower = new ArrayList<Entry>();
                    ArrayList<String> array = new ArrayList<String>();
                    String[] Chart_id = {"Power"};
                    try {
                        JSONObject object = null;
                        object = new JSONObject(response);
                        Log.d("getPVHistoryChart", response.toString());
                        int num = object.getJSONArray("label").length();
                        Log.d("num", String.valueOf(num));
                        String xUnit ="";
                        for (int i = 0; i < num; i++) {
                            String label = object.getJSONArray("label").getString(i);
                            String power = object.getJSONArray("power").getString(i);
                            String energy = object.getJSONArray("energy").getString(i);
                            LinePower.add(new Entry(i, Float.parseFloat(power)));
                            BarEnergy.add(new BarEntry(i, Float.parseFloat(energy)));
                            array.add(new String(label));
                        }
                        switch (button) {
                            case "getHistoryInYear":
                                xUnit = "時間(月份)";
                                list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功 (kW)", array, "kW",xUnit));
                                list2.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEnergy), getActivity(), "發電量 (kWh)", array, "kWh", xUnit));
                                break;
                            case "getHistoryInMonth":
                                xUnit = "時間(日)";
                                list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功 (kW)", array, "kW",xUnit));
                                list2.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEnergy), getActivity(), "發電量 (kWh)", array, "kWh", xUnit));
                                break;
                            case "getHistoryInDay":
                                xUnit = "時間(小時)";
                                list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功 (kW)", array, "kW",xUnit));
                                list2.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEnergy), getActivity(), "發電量 (kWh)", array, "kWh", xUnit));
                                break;
                            case "getHistoryInNow":
                                xUnit = "時間(時:分)";
                                list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功 (kW)", array, "kW",xUnit));
                                list2.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEnergy), getActivity(), "發電量 (kWh)", array, "kWh", xUnit));
                                break;
                        }
                        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
                        lv.setAdapter(cda);
                        ChartDataAdapter cda2 = new ChartDataAdapter(getActivity(), list2);
                        lv2.setAdapter(cda2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Tag", "回傳錯誤");
                }
            })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            requestQueue.add(Objectrequest);
        }
    }

