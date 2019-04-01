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
import android.widget.ListView;
import android.widget.TabHost;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.Charge.EvViewAdapter;
import com.example.jason.nckubems.Chart;
import com.example.jason.nckubems.ChartDataAdapter;
import com.example.jason.nckubems.R;
import com.example.jason.nckubems.listviewitems.BarChartItem;
import com.example.jason.nckubems.listviewitems.ChartItem;
import com.example.jason.nckubems.listviewitems.LineChartItem;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;


public class ChargeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private SharedPreferences setting;
    private String token;
    private RequestQueue requestQueue;
    //private String EV = "xlii_bems_api/ev.php";
    private String EV = "http://59.125.180.237/xlii_bems_api/ev.php";
    private StringRequest request;
    private ListView lvView, powerlv, powerlv2, powerlv3, powerlv4, soclv, soclv2, soclv3, soclv4;
    private TabHost tabHost;
    private int num = 0, i = 0;
    private Chart chart;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();
    private StringRequest ESSSrequest;


    public static ChargeFragment newInstance(String param1, String param2) {
        ChargeFragment fragment = new ChargeFragment();
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
        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        String ip = setting.getString("ip","");
        chart = new Chart(getActivity());
        int Comparesurl = EV.indexOf("//");
        if(Comparesurl == -1) {
            EV = ip + EV;
        }
        lvView = (ListView) getView().findViewById(R.id.lv_view);
        powerlv = (ListView) getView().findViewById(R.id.powerList);
        //powerlv2 = (ListView) getView().findViewById(R.id.powerList2);
        //powerlv3 = (ListView) getView().findViewById(R.id.powerList3);
        //powerlv4 = (ListView) getView().findViewById(R.id.powerList4);
        soclv = (ListView) getView().findViewById(R.id.socList);
        //soclv2 = (ListView) getView().findViewById(R.id.socList2);
        //soclv3 = (ListView) getView().findViewById(R.id.socList3);
        //soclv4 = (ListView) getView().findViewById(R.id.socList4);
        tabHost = (TabHost) getView().findViewById(R.id.tabs_ev_result);
        tabHostSet();
        getEV();
        getEVSchedule1();

    }

    private void getEV() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getEV");
            body.put("token","b8722a4431d6e749314dae271ec8c8f");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new StringRequest(Request.Method.POST, EV,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = null;
                    Log.d("getEV", response);
                    String status = "";
                    object = new JSONArray(response);
                    while (i < 1) {

                        switch (object.getJSONObject(i).getString("status")) {
                            case "1":
                                status = "閒置中";
                                break;
                            case "2":
                                status = "充電中";
                                break;
                            case "3":
                                status = "Finish Charging (充電結束)";
                                break;
                            case "4":
                                status = "Reservation Charging (預約充電中)";
                                break;
                            case "5":
                                status = "Watting Reservation (等待預約中)";
                                break;
                            case "6":
                                status = "Certification (認證中)";
                                break;
                            case "7":
                                status = "異常";
                                break;
                            case "8":
                                status = "Closed (關閉中)";
                                break;
                        }
                        HashMap<String, String> item = new HashMap<String, String>();
                        String EVname = "";
                        switch (i) {
                            case 0:
                                EVname = "成大充電樁資訊";
                                break;
                            case 1:
                                EVname = "充電樁2資訊";
                                break;
                            case 2:
                                EVname = "充電樁3資訊";
                                break;
                            case 3:
                                EVname = "充電樁4資訊";
                                break;
                        }
                        item.put("ev_num", EVname);
                        item.put("ev_status", status);
                        item.put("ev_voltage", object.getJSONObject(i).getString("voltage"));
                        item.put("ev_current", object.getJSONObject(i).getString("current"));
                        item.put("ev_p", object.getJSONObject(i).getString("power"));
                        item.put("ev_time",
                                change(Integer.valueOf(object.getJSONObject(i).getString("pass_time")))
                        );
                        item.put("ev_elc", object.getJSONObject(i).getString("total_energy"));
                        item.put("ev_cluster", object.getJSONObject(i).getString("cluster"));
                        datas.add(item);
                        i++;
                    }
                    EvViewAdapter adapter = new EvViewAdapter(getActivity(), datas);
                    lvView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        requestQueue.add(request);
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
        spec.setContent(R.id.tab_power);
        spec.setIndicator("Power");
        tabHost.addTab(spec);
        //tab2
        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.tab_soc);
        spec.setIndicator("SOC");
        tabHost.addTab(spec);
    }

    private void getEVSchedule1() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getEVSchedule");
            body.put("field","xinglong2");
            body.put("token","b8722a4431d6e749314dae271ec8c8f");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ESSSrequest = new StringRequest(Request.Method.POST, EV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ChartItem> powerlist = new ArrayList<ChartItem>(), powerlist2 = new ArrayList<ChartItem>(), powerlist3 = new ArrayList<ChartItem>(), powerlist4 = new ArrayList<ChartItem>()
                        , soclist = new ArrayList<ChartItem>(), soclist2 = new ArrayList<ChartItem>(), soclist3 = new ArrayList<ChartItem>(), soclist4 = new ArrayList<ChartItem>();
                String[] Chart_id = {"Power"};
                String[] Chart_id2 = {"SOC"};
                String EVname = "";
                try {
                    JSONObject object = null;
                    object = new JSONObject(response);
                    Log.d("getEVSchedule", response.toString());
                    num = object.getJSONObject("3").getJSONArray("power").length();
                    for (int i = 1; i <= 4; i++){
                        ArrayList<BarEntry> BarEntries = new ArrayList<BarEntry>();
                        ArrayList<Entry> LineEntries = new ArrayList<Entry>();
                        for (int j = 0;j < num;j++){
                            String power = object.getJSONObject(String.valueOf(i)).getJSONArray("power").getString(j);
                            BarEntries.add(new BarEntry(j, Float.parseFloat(power)));
                            String soc = object.getJSONObject(String.valueOf(i)).getJSONArray("soc").getString(j);
                            LineEntries.add(new Entry(j, Float.parseFloat(soc)));
                        }
                        switch (i) {
                            case 1:
                                EVname = "成大充電樁資訊";
                                powerlist.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), EVname, Time(), "kW", "時間(分鐘)"));
                                soclist.add(new LineChartItem(chart.generateDataLine(Chart_id2, LineEntries), getActivity(), EVname, Time(), "%", "時間(分鐘)"));
                                break;
                            case 2:
                                EVname = "充電樁2資訊";
                                powerlist2.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), EVname, Time(), "kW", "時間(分鐘)"));
                                soclist2.add(new LineChartItem(chart.generateDataLine(Chart_id2, LineEntries), getActivity(), EVname, Time(), "%", "時間(分鐘)"));
                                break;
                            case 3:
                                EVname = "充電樁3資訊";
                                powerlist3.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), EVname, Time(), "kW", "時間(分鐘)"));
                                soclist3.add(new LineChartItem(chart.generateDataLine(Chart_id2, LineEntries), getActivity(), EVname, Time(), "%", "時間(分鐘)"));
                                break;
                            case 4:
                                EVname = "充電樁4資訊";
                                powerlist4.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), EVname, Time(), "kW", "時間(分鐘)"));
                                soclist4.add(new LineChartItem(chart.generateDataLine(Chart_id2, LineEntries), getActivity(), EVname, Time(), "%", "時間(分鐘)"));
                                break;
                        }
                    }
                    //--因為遇到不知名的Bug，所以出此下策做出四個清單--
                    ChartDataAdapter powerData = new ChartDataAdapter(getActivity(), powerlist);
                    //ChartDataAdapter powerData2 = new ChartDataAdapter(getActivity(), powerlist2);
                    //ChartDataAdapter powerData3 = new ChartDataAdapter(getActivity(), powerlist3);
                    //ChartDataAdapter powerData4 = new ChartDataAdapter(getActivity(), powerlist4);
                    ChartDataAdapter socData = new ChartDataAdapter(getActivity(), soclist);
                    //ChartDataAdapter socData2 = new ChartDataAdapter(getActivity(), soclist2);
                    //ChartDataAdapter socData3 = new ChartDataAdapter(getActivity(), soclist3);
                    //ChartDataAdapter socData4 = new ChartDataAdapter(getActivity(), soclist4);
                    powerlv.setAdapter(powerData);
                    //powerlv2.setAdapter(powerData2);
                    //powerlv3.setAdapter(powerData3);
                    //powerlv4.setAdapter(powerData4);
                    soclv.setAdapter(socData);
                    //soclv2.setAdapter(socData2);
                    //soclv3.setAdapter(socData3);
                    //soclv4.setAdapter(socData4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        requestQueue.add(ESSSrequest);
    }

    /* * 將秒數轉為時分秒 * */
    public String change(int second) {
        int h = 0, d = 0, s = 0;
        String H ="", D, S;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (h < 10) H = "0" + String.valueOf(h);
        else H = String.valueOf(h);
        if (d < 10) D = "0" + String.valueOf(d);
        else D = String.valueOf(d);
        if (s < 10) S = "0" + String.valueOf(s);
        else S = String.valueOf(s);
        return H + ":" + D + ":" + S + "";
    }

}
