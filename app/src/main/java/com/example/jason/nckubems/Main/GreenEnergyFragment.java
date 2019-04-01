package com.example.jason.nckubems.Main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Date;


public class GreenEnergyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;
    private StringRequest Solar_requestQueue,Solarrequest_power,Windrequest_power;
    private SharedPreferences setting;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String token, ip;
    private TextView txtInformation_wind,txtInformation_anemometer,txtInformation_solar,tv_power_produce;
    private LinearLayout Layout_wind, Layout_anemometer, Layout_solar,Layout_power_produce;
    private TextView solar_power,solar_voltage,solar_current,solar_frequency,solar_amount,panels_temp,environment_temp; //太陽能資訊
    private Context context;
    private String getPV = "xlii_bems_api/pv.php";
    private TabHost tabHost;
    private ListView wind_powerList,solar_powerList;
    private Chart chart;


    public static GreenEnergyFragment newInstance(String param1, String param2) {
        GreenEnergyFragment fragment = new GreenEnergyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_green_energy, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        chart = new Chart(getActivity());
        tabHost = getView().findViewById(R.id.tabs_power_immediate);
        wind_powerList = getView().findViewById(R.id.Wind_powerList);
        solar_powerList = getView().findViewById(R.id.Solar_powerList);
        txtInformation_wind = getView().findViewById(R.id.tv_btn_wind_Information);
        txtInformation_anemometer = getView().findViewById(R.id.tv_btn_anemometer_information);
        txtInformation_solar = getView().findViewById(R.id.tv_btn_solar_information);
        tv_power_produce = getView().findViewById(R.id.tv_btn_power_produce);
        Layout_wind = getView().findViewById(R.id.Layout_wind_data);
        Layout_anemometer = getView().findViewById(R.id.Layout_anemometer_data);
        Layout_solar = getView().findViewById(R.id.Layout_solar_data);
        Layout_power_produce = getView().findViewById(R.id.Layout_power);
        solar_power = getView().findViewById(R.id.tv_data_solar_power);
        solar_voltage = getView().findViewById(R.id.tv_data_solar_voltage);
        solar_current = getView().findViewById(R.id.tv_data_solar_current);
        solar_frequency = getView().findViewById(R.id.tv_data_solar_frequency);
        solar_amount = getView().findViewById(R.id.tv_data_solar_amount);
        panels_temp = getView().findViewById(R.id.tv_data_Solarpanels_temp);
        environment_temp = getView().findViewById(R.id.tv_data_environment_temp);
        //--------------------------
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        ip = setting.getString("ip","");
        int Comparesurl = getPV.indexOf("//");
        if(Comparesurl == -1) {
            getPV = ip + getPV;
            getPV = "http://59.125.180.237/xlii_bems_api/pv.php";
        }
        //---------------------
        txtInformation_wind.setOnClickListener(new Button.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                Layout_wind.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtInformation_anemometer.setOnClickListener(new Button.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                Layout_anemometer.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtInformation_solar.setOnClickListener(new Button.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                Layout_solar.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        tv_power_produce.setOnClickListener(new Button.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                Layout_power_produce.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String[] date = formatter.format(curDate).split("-");
        Log.d("now", formatter.format(curDate));
        tabHostSet();
        getPowerInformation_solar("getHistoryInNow",Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
        getPowerInformation_wind("getHistoryInNow",Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
        getSolarInformation();
        getWindInformation();

    }

    private void getWindInformation() {
    }
    private void getSolarInformation() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getPVDisplay");
            body.put("field","xinglong2");
            body.put("cluster","1");
            body.put("token","b8722a4431d6e749314dae271ec8c8f");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Solar_requestQueue = new StringRequest(Request.Method.POST, getPV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("getPVDisplay", response);
                    JSONObject object = null;
                    object = new JSONObject(response);
                    solar_power.setText(object.getString("power"));
                    solar_amount.setText(object.getString("irradiation"));
                    solar_voltage.setText(object.getString("voltage"));
                    solar_current.setText(object.getString("current"));
                    solar_frequency.setText(object.getString("frequency_a"));
                    panels_temp.setText(object.getString("template_temperature"));
                    environment_temp.setText(object.getString("environment_temperature"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        requestQueue.add(Solar_requestQueue);
    }
    private ArrayList<String> Time() {
        ArrayList<String> m = new ArrayList<String>();
        m.add("00:00");
        m.add("00:15");
        m.add("00:30");
        m.add("00:45");
        m.add("01:00");
        m.add("01:15");
        m.add("01:30");
        m.add("01:45");
        m.add("02:00");
        m.add("02:15");
        m.add("02:30");
        m.add("02:45");
        m.add("03:00");
        m.add("03:15");
        m.add("03:30");
        m.add("03:45");
        m.add("04:00");
        m.add("04:15");
        m.add("04:30");
        m.add("04:45");
        m.add("05:00");
        m.add("05:15");
        m.add("05:30");
        m.add("05:45");
        m.add("06:00");
        m.add("06:15");
        m.add("06:30");
        m.add("06:45");
        m.add("07:00");
        m.add("07:15");
        m.add("07:30");
        m.add("07:45");
        m.add("08:00");
        m.add("08:15");
        m.add("08:30");
        m.add("08:45");
        m.add("09:00");
        m.add("09:15");
        m.add("09:30");
        m.add("09:45");
        m.add("10:00");
        m.add("10:15");
        m.add("10:30");
        m.add("10:45");
        m.add("11:00");
        m.add("11:15");
        m.add("11:30");
        m.add("11:45");
        m.add("12:00");
        m.add("12:15");
        m.add("12:30");
        m.add("12:45");
        m.add("13:00");
        m.add("13:15");
        m.add("13:30");
        m.add("13:45");
        m.add("14:00");
        m.add("14:15");
        m.add("14:30");
        m.add("14:45");
        m.add("15:00");
        m.add("15:15");
        m.add("15:30");
        m.add("15:45");
        m.add("16:00");
        m.add("16:15");
        m.add("16:30");
        m.add("16:45");
        m.add("17:00");
        m.add("17:15");
        m.add("17:30");
        m.add("17:45");
        m.add("18:00");
        m.add("18:15");
        m.add("18:30");
        m.add("18:45");
        m.add("19:00");
        m.add("19:15");
        m.add("19:30");
        m.add("19:45");
        m.add("20:00");
        m.add("20:15");
        m.add("20:30");
        m.add("20:45");
        m.add("21:00");
        m.add("21:15");
        m.add("21:30");
        m.add("21:45");
        m.add("22:00");
        m.add("22:15");
        m.add("22:30");
        m.add("22:45");
        m.add("23:00");
        m.add("23:15");
        m.add("23:30");
        m.add("23:45");
        return m;
    }
    private void tabHostSet() {
        tabHost.setup();
        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.wind_power);
        spec.setIndicator("風力");
        tabHost.addTab(spec);
        //tab2
        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.solar_power);
        spec.setIndicator("太陽能");
        tabHost.addTab(spec);
    }

    private void getPowerInformation_solar(final String button, int year, int month, int day) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", button);
            body.put("field", "xinglong2");
            body.put("cluster", "1");
            body.put("token", "b8722a4431d6e749314dae271ec8c8f");
            body.put("year", String.valueOf(year));
            body.put("month", String.valueOf(month + 1));
            body.put("day", String.valueOf(day));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Solarrequest_power = new StringRequest(Request.Method.POST, getPV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ChartItem> list = new ArrayList<ChartItem>();
                ArrayList<Entry> LinePower = new ArrayList<Entry>(); //實功輸出(折線圖)
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
                        LinePower.add(new Entry(i, Float.parseFloat(power)));
                        array.add(new String(label));
                    }
                    list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功輸出 (kW)", array, "kW", "時間(分鐘)"));
                    ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
                    solar_powerList.setAdapter(cda);

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
        requestQueue.add(Solarrequest_power);
    }
    private void getPowerInformation_wind(final String button, int year, int month, int day) {
        final JSONObject body = new JSONObject();
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
        Windrequest_power = new StringRequest(Request.Method.POST, getPV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ChartItem> list = new ArrayList<ChartItem>();
                ArrayList<Entry> LinePower = new ArrayList<Entry>(); //實功輸出(折線圖)
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
                        LinePower.add(new Entry(i, Float.parseFloat(power)));
                        array.add(new String(label));
                    }
                    list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower), getActivity(), "實功輸出 (kW)", array, "kW", "時間(分鐘)"));
                    ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
                    wind_powerList.setAdapter(cda);

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
        requestQueue.add(Windrequest_power);
    }

}
