package com.example.jason.nckubems.Main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private SharedPreferences setting;
    private String token, ip;
    private RequestQueue requestQueue;
    private double i =0;
    private TabHost tabHost;
    private EditText active;
    private ImageButton btnadd,btnsub;
    private Button  SaveESS;
    private Spinner spinner_Au; // 手動or自動
    private String mode = "";
    private TextView txtInformation,txtSet,txtChart, txtManualpower,tv_powerset;
    private LinearLayout  Layoutdata, Layoutset, Layoutresult;
    private ListView powerlv, soclv;
    private String ESS = "xlii_bems_api/ess.php";
    private String url = "xlii_bems/action.php";
    private StringRequest ESSSrequest;
    private Chart chart;


    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        return inflater.inflate(R.layout.fragment_schedule, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        tabHost = (TabHost)getView().findViewById(R.id.tabs_sch_result);
        txtInformation = getView().findViewById(R.id.tv_btn_information);
        txtSet = getView().findViewById(R.id.tv_btn_set);
        txtChart = getView().findViewById(R.id.tv_btn_chart);
        txtManualpower = getView().findViewById(R.id.tv_manual_power); //手動輸出值
        tv_powerset = getView().findViewById(R.id.tv_powerset);
        Layoutdata = getView().findViewById(R.id.Layout_en_data);
        Layoutset = getView().findViewById(R.id.Layout_en_set);
        Layoutresult = getView().findViewById(R.id.Layout_en_result);
        powerlv = (ListView) getView().findViewById(R.id.powerList);
        soclv = (ListView) getView().findViewById(R.id.socList);
        active = (EditText)getView().findViewById(R.id.ed_activepower);
        btnadd = (ImageButton) getView().findViewById(R.id.imgbtn_active_add); // 加
        btnsub = (ImageButton) getView().findViewById(R.id.imgbtn_active_sub); //減
        SaveESS = (Button) getView().findViewById(R.id.btn_saveess);
        spinner_Au = (Spinner)getView().findViewById(R.id.sp_ess_mode);
        chart = new Chart(getActivity());
        //////////////
        tabHostSet();
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        ip = setting.getString("ip","");
        int Comparesurl = ESS.indexOf("//");
        if(Comparesurl == -1) {
            ESS = ip + ESS;
            ESS = "http://59.125.180.237/xlii_bems_api/ess.php";
        }
        int Comparesurl2 = url.indexOf("//");
        if(Comparesurl2 == -1) {
            url = ip + url;
        }
        txtInformation.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
                //TransitionManager.beginDelayedTransition(transitionsContainer);
                visible = !visible;
                Layoutdata.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtSet.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
               // TransitionManager.beginDelayedTransition(transitionsContainer);
                visible = !visible;
                Layoutset.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtChart.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
                //TransitionManager.beginDelayedTransition(transitionsContainer);
                visible = !visible;
                Layoutresult.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });
        btnadd.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(active.getText().toString() != null)
                    i = Double.valueOf(active.getText().toString());
                i++;
                active.setText(String.format("%.2f", i));
            }
        });
        btnsub.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(active.getText().toString() != null)
                    i = Double.valueOf(active.getText().toString());
                i--;
                active.setText(String.format("%.2f", i));
            }
        });
        spinner_Au.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] whatMode = getResources().getStringArray(R.array.ESSManuallyAuto);
                if(whatMode[position].equals("手動模式")) {
                    mode = "1";
                    tv_powerset.setText("操作模式："+whatMode[position]);
                } else if(whatMode[position].equals("自動模式")) {
                    mode = "0";
                    tv_powerset.setText("操作模式："+whatMode[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SaveESS.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(active.getText().length() == 0){
                    Toast.makeText(getActivity(), "數值不能為空！", Toast.LENGTH_LONG).show();
                }else if(Double.parseDouble(active.getText().toString()) <= 15 &&Double.parseDouble(active.getText().toString()) >= -15){
                    setESS("1", mode, active.getText().toString());
                    Toast.makeText(getActivity(), "儲存", Toast.LENGTH_LONG).show();
                    txtManualpower.setText("手動輸出設定值："+active.getText().toString()+"0"+" kW");
                }else{
                    Toast.makeText(getActivity(), "數值有誤！", Toast.LENGTH_LONG).show();
                }
            }
        });
        getESS();
        getSchedule();

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

    private void getESS() {
    }
    private void getSchedule() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getESSSchedule");
            body.put("field","xinglong2");
            body.put("cluster","1");
            body.put("token","b8722a4431d6e749314dae271ec8c8f");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ESSSrequest = new StringRequest(Request.Method.POST, ESS,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ChartItem> powerlist = new ArrayList<ChartItem>(), soclist = new ArrayList<ChartItem>();
                String[] Chart_id = {"Power"};
                String[] Chart_id2 = {"SOC"};
                ArrayList<BarEntry> BarEntries = new ArrayList<BarEntry>();
                ArrayList<Entry> LineEntries = new ArrayList<Entry>();
                try {
                    JSONObject object = null;
                    object = new JSONObject(response);
                    Log.d("getEVSchedule", response.toString());
                    int num = object.getJSONArray("power").length();
                    Log.d("getESSSchedule", response);
                    Log.d("num", String.valueOf(response.length()));
                    for (int i = 0;i < num;i++){
                        String power = object.getJSONArray("power").getString(i);
                        BarEntries.add(new BarEntry(i, Float.parseFloat(power)));
                        String soc = object.getJSONArray("soc").getString(i);
                        LineEntries.add(new Entry(i, Float.parseFloat(soc)*100));
                    }
                    powerlist.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), "Power (kW)", Time(), "kW", "時間(分鐘)"));
                    soclist.add(new LineChartItem(chart.generateDataLine(Chart_id2, LineEntries), getActivity(), "SOC (％)", Time(), "%", "時間(分鐘)"));
                    ChartDataAdapter powerData = new ChartDataAdapter(getActivity(), powerlist);
                    ChartDataAdapter socData = new ChartDataAdapter(getActivity(), soclist);
                    powerlv.setAdapter(powerData);
                    soclv.setAdapter(socData);
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
        requestQueue.add(ESSSrequest);
    }

    private void setESS(String s, String mode, String s1) {
    }

    public void tabHostSet() {
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

}
