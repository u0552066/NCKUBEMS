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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.DR.DrHistoryAdapter;
import com.example.jason.nckubems.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class DRFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private String token,ip;
    private SharedPreferences setting;
    private int num = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private String getDREvents = "xlii_bems_api/dr.php";
    private ListView lv_History;
    private LinearLayout LLnow, LLHnow, LLfuture, LLHfuture, LLhistory, LLHhistory;
    private TextView tvCount1, tvCount2, tvCount3;
    private TextView tv_Now_date,tv_Now_price,tv_Now_content,tv_Now_start,tv_Now_end,tv_Now_p,tv_Now_money; // 當前
    private TextView tv_Future_date,tv_Future_price,tv_Future_content,tv_Future_start,tv_Future_end,tv_Future_p,tv_Future_money; // 未來
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();


    public static DRFragment newInstance(String param1, String param2) {
        DRFragment fragment = new DRFragment();
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
        return inflater.inflate(R.layout.fragment_dr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        pref = this.getActivity().getSharedPreferences("auto",0);
        editor = pref.edit();
        token = pref.getString("TOKEN","");
        ip = setting.getString("ip","");
        Log.d("IP",ip+" "+token);
        int Comparesurl = getDREvents.indexOf("//");
        if(Comparesurl == -1) {
            getDREvents = ip + getDREvents;
            getDREvents = "http://59.125.180.237/xlii_bems_api/dr.php";
        }
        findViewById();
        getDREvents();

    }

    private void getDREvents() {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("history_date", "2019-1-3");
        item.put("history_price", "8.0");
        item.put("history_content", "20");
        item.put("history_get", "是");
        item.put("history_start", "15:00");
        item.put("history_end","16:00");
        item.put("history_p","20");
        item.put("history_real_p","23");
        item.put("history_use_p","69");
        item.put("history_load_p","46");
        item.put("history_money","160");
        item.put("history_real_money","160");
        item.put("history_result","成功");
        datas.add(item);
        //DrHistoryAdapter adapter = new DrHistoryAdapter(getActivity(), datas);
        //lv_History.setAdapter(adapter);
        HashMap<String, String> item1 = new HashMap<String, String>();
        item1.put("history_date", "2019-1-2");
        item1.put("history_price", "8.0");
        item1.put("history_content", "20");
        item1.put("history_get", "是");
        item1.put("history_start", "15:00");
        item1.put("history_end","16:00");
        item1.put("history_p","20");
        item1.put("history_real_p","23");
        item1.put("history_use_p","69");
        item1.put("history_load_p","46");
        item1.put("history_money","160");
        item1.put("history_real_money","160");
        item.put("history_result","成功");
        datas.add(item1);
        DrHistoryAdapter adapter = new DrHistoryAdapter(getActivity(), datas);
        lv_History.setAdapter(adapter);
    }

    private void findViewById() {
        LLnow = getView().findViewById(R.id.layout_event_now);
        LLHnow = getView().findViewById(R.id.layout_hide_event_now);
        LLfuture = getView().findViewById(R.id.layout_event_future);
        LLHfuture = getView().findViewById(R.id.layout_hide_event_future);
        LLhistory = getView().findViewById(R.id.layout_event_history);
        LLHhistory = getView().findViewById(R.id.layout_hide_event_history);
        tvCount1 = getView().findViewById(R.id.tv_count1);
        tvCount2 = getView().findViewById(R.id.tv_count2);
        tvCount3 = getView().findViewById(R.id.tv_count3);
        lv_History = getView().findViewById(R.id.lv_history);
        //當前事件
        tv_Now_date = getView().findViewById(R.id.tv_now_date);
        tv_Now_price = getView().findViewById(R.id.tv_now_price);
        tv_Now_content = getView().findViewById(R.id.tv_now_content);
        tv_Now_start = getView().findViewById(R.id.tv_now_start);
        tv_Now_end = getView().findViewById(R.id.tv_now_end);
        tv_Now_p = getView().findViewById(R.id.tv_now_p);
        tv_Now_money = getView().findViewById(R.id.tv_now_money);
        //未來事件
        tv_Future_date = getView().findViewById(R.id.tv_future_date);
        tv_Future_price = getView().findViewById(R.id.tv_future_price);
        tv_Future_content = getView().findViewById(R.id.tv_future_content);
        tv_Future_start = getView().findViewById(R.id.tv_future_start);
        tv_Future_end = getView().findViewById(R.id.tv_future_end);
        tv_Future_p = getView().findViewById(R.id.tv_future_p);
        tv_Future_money = getView().findViewById(R.id.tv_future_money);
        //顯示or隱藏
        LLnow.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHnow.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount1.setText(visible ? "-" : "+");
            }
        });
        LLfuture.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHfuture.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount2.setText(visible ? "-" : "+");
            }
        });
        LLhistory.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHhistory.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount3.setText(visible ? "-" : "+");
            }
        });

    }

}
