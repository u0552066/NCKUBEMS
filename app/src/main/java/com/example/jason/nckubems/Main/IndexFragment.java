package com.example.jason.nckubems.Main;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.Charge.EvHistoryFragment;
import com.example.jason.nckubems.GreenEnergy.SolarHistoryFragment;
import com.example.jason.nckubems.GreenEnergy.WindHistoryFragment;
import com.example.jason.nckubems.Home.BuildingHistoryFragment;
import com.example.jason.nckubems.Home.HvFragment;
import com.example.jason.nckubems.R;
import com.example.jason.nckubems.Schedule.EssHistoryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class IndexFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private String url_weather = "http://140.116.163.19:10107/epslab_ems/api/weather.php";
    private String url = "http://140.116.163.19:10107/epslab_ems/api/general.php";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    String User,Token;
    private TextView Power_Grid, Power_Ev, Power_Wind, Power_ESS, Power_Solar, Power_Building;
    private Button btn_Grid, btn_ESS, btn_Solar, btn_Ev, btn_Wind, btn_Building;
    private TextView Date, Weather, Temperature, Probability, Accumulate_consumption, Electricity_fee;
    private ImageView Imgweather, arrow5;
    Timer timer;
    TimerTask timerTask;


    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
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
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        settingedit = setting.edit();
        Accumulate_consumption = getView().findViewById(R.id.tv_accumulate_consumption); //累積用電
        Electricity_fee = getView().findViewById(R.id.tv_electrity_fee); // 累積電費
        Power_Grid = (TextView) getView().findViewById(R.id.tv_grid_p); //電網
        Power_Building = (TextView) getView().findViewById(R.id.tv_buildings_p); //大樓
        Power_Ev = (TextView) getView().findViewById(R.id.tv_ev_p); //充電樁
        Power_Wind = (TextView) getView().findViewById(R.id.tv_wind_p); //風機
        Power_ESS = (TextView) getView().findViewById(R.id.tv_ess_p); //儲能櫃
        Power_Solar = (TextView) getView().findViewById(R.id.tv_pv_p); //太陽能
        btn_Grid = (Button) getView().findViewById(R.id.btn_grid);
        btn_ESS = (Button) getView().findViewById(R.id.btn_ess);
        btn_Solar = (Button) getView().findViewById(R.id.btn_pv);
        btn_Ev = (Button) getView().findViewById(R.id.btn_charger);
        btn_Wind = (Button) getView().findViewById(R.id.btn_wind_generate);
        btn_Building = (Button) getView().findViewById(R.id.btn_buildings);
        Imgweather = (ImageView) getView().findViewById(R.id.img_weather); //天氣圖
        Date = (TextView) getView().findViewById(R.id.tv_date);
        Weather = (TextView) getView().findViewById(R.id.tv_weather);
        Temperature = (TextView) getView().findViewById(R.id.tv_temperature);
        Probability = (TextView) getView().findViewById(R.id.tv_probability);
        getWeather(); // 天氣資訊
        getInformation(); // 取得首頁資訊
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //getPowerFlows();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,10000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日"/*+"HH:mm:ss"*/);
        java.util.Date curDate = new Date() ; // 獲取當前時間
        String str = formatter.format(curDate);
        Date.setText(str);
        //-----------------
        // 取得 UI 介面中的 View 物件
        // 取得 View 物件後，再透過轉換成實際的物件
        ImageView arrow1 = (ImageView) getView().findViewById(R.id.img_arrow1);
        ImageView arrow2 = (ImageView) getView().findViewById(R.id.img_arrow2);
        ImageView arrow3 = (ImageView) getView().findViewById(R.id.img_arrow3);
        ImageView arrow4 = (ImageView) getView().findViewById(R.id.img_arrow4);
        ImageView arrow5 = (ImageView) getView().findViewById(R.id.img_arrow5);
        ImageView arrow6 = (ImageView) getView().findViewById(R.id.img_arrow6);
        //arrow5 = (ImageView) getView().findViewById(R.id.img_arrow6);
        // 設定 ImageView 的圖片來源
        //iv.setImageResource( R.drawable.icon );
        // 動畫設定 (指定移動動畫) (x1, x2, y1, y2)
        Animation am = new TranslateAnimation( 0, 0, 0, 30 );
        // 動畫開始到結束的執行時間 (1000 = 1 秒)
        am.setDuration( 1000 );
        // 動畫重複次數 (-1 表示一直重複)
        am.setRepeatCount( -1 );
        // 圖片配置動畫
        arrow1.setAnimation(am);
        arrow2.setAnimation(am);
        arrow3.setAnimation(am);
        arrow4.setAnimation(am);
        arrow5.setAnimation(am);
        arrow6.setAnimation(am);
        // 動畫開始
        am.startNow();
        //-----------------
        btn_Grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction HvHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment HvHistory = getFragmentManager().findFragmentByTag("HvFragment");
                if (HvHistory != null) {
                    HvHistoryFragmentTransaction.remove(HvHistory);
                }
                HvHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new HvFragment();
                newFragment.show(HvHistoryFragmentTransaction, "HvFragment");

            }
        });
        btn_Building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction BuildingHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment BuildingHistory = getFragmentManager().findFragmentByTag("BuildingHistoryFragment");
                if (BuildingHistory != null) {
                    BuildingHistoryFragmentTransaction.remove(BuildingHistory);
                }
                BuildingHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new BuildingHistoryFragment();
                newFragment.show(BuildingHistoryFragmentTransaction, "BuildingHistoryFragment");
            }
        });
        btn_ESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction EssHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment EssHistory = getFragmentManager().findFragmentByTag("EssHistoryFragment");
                if (EssHistory != null) {
                    EssHistoryFragmentTransaction.remove(EssHistory);
                }
                EssHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new EssHistoryFragment();
                newFragment.show(EssHistoryFragmentTransaction, "EssHistoryFragment");
            }
        });
        btn_Ev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction EvHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment EvHistory = getFragmentManager().findFragmentByTag("EvHistoryFragment");
                if (EvHistory != null) {
                    EvHistoryFragmentTransaction.remove(EvHistory);
                }
                EvHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new EvHistoryFragment();
                newFragment.show(EvHistoryFragmentTransaction, "EvHistoryFragment");

            }
        });
        btn_Solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction SolarHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment SolarHistory = getFragmentManager().findFragmentByTag("SolarHistoryFragment");
                if (SolarHistory != null) {
                    SolarHistoryFragmentTransaction.remove(SolarHistory);
                }
                SolarHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Solar = new SolarHistoryFragment();
                newFragment_Solar.show(SolarHistoryFragmentTransaction, "SolarHistoryFragment");

            }
        });
        btn_Wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction WindHistoryFragmentTransaction = getFragmentManager().beginTransaction();
                Fragment WindHistory = getFragmentManager().findFragmentByTag("WindHistoryFragment");
                if (WindHistory != null) {
                    WindHistoryFragmentTransaction.remove(WindHistory);
                }
                WindHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Wind = new WindHistoryFragment();
                newFragment_Wind.show(WindHistoryFragmentTransaction, "WindHistoryFragment");

            }
        });

    }

    private void getInformation() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getBemsHomepageInformation");
            body.put("field","NCKU");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("Home_Information", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    String accumulate_consumption = data.getJSONObject(0).getString("accumulate_consumption"); //當月累積用電量
                    String electricity_fee = data.getJSONObject(0).getString("electricity_fee"); //當月累積電費
                    String grid = data.getJSONObject(0).getString("grid"); // 電網
                    String pv = data.getJSONObject(0).getString("pv"); // 太陽能
                    String wind_turbine = data.getJSONObject(0).getString("wind_turbine"); // 風機
                    String building = data.getJSONObject(0).getString("building"); // 大樓用電
                    String ess = data.getJSONObject(0).getString("ess"); // 儲能櫃
                    String ev = data.getJSONObject(0).getString("ev"); // 充電樁
                    Accumulate_consumption.setText(accumulate_consumption+" 度");
                    Electricity_fee.setText(electricity_fee+" 元");
                    Power_Grid.setText(grid+" kW");
                    Power_Solar.setText(pv+" kW");
                    Power_Wind.setText(wind_turbine+" kW");
                    Power_Building.setText(building+" kW");
                    Power_ESS.setText(ess+" kW");
                    Power_Ev.setText(ev+" kW");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_weather", error.toString());
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
        requestQueue.add(postRequest);
    }

    private void getWeather() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getBemsWeatherForecast");
            body.put("field","NCKU");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_weather, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("Weather", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    String temp = data.getString("temperature");
                    String weather = data.getString("weather");
                    String probability = data.getString("probability");
                    Temperature.setText(String.format("%.01f", Float.valueOf(temp)));
                    Probability.setText(String.format("%.01f", Float.valueOf(probability)));
                    switch(weather){
                        case "1":
                            Imgweather.setImageResource(R.drawable.sun);
                            Weather.setText("晴");
                            break;
                        case "2":
                            Imgweather.setImageResource(R.drawable.cloudy);
                            Weather.setText("陰");
                            break;
                        case "3":
                            Imgweather.setImageResource(R.drawable.rain);
                            Weather.setText("雨");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_weather", error.toString());
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
        requestQueue.add(postRequest);
    }
}
