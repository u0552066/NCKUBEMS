package com.example.jason.nckubems.Charge;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.R;

import java.util.Calendar;


public class ChargeSetFragment extends DialogFragment {
    private Spinner SLMaAu, SLPWM, SLSpeed;
    private Button close1,slsave;
    private LinearLayout LayoutSLMa, LayoutSLAu;
    private RequestQueue requestQueue;
    //private static final String setEV = "http://192.168.1.100/sl_demo_api/ev.php";
    private String setEV = "xlii_bems_api/ev.php";
    private String url = "xlii_bems/action.php";
    private StringRequest postRequest;
    private TextView evname;
    private JsonObjectRequest request;
    private SharedPreferences setting;
    private String token, item;
    private EditText EDslstart, EDslend, EDslsocstart, EDslsocend;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隱藏title
        getDialog().requestWindowFeature(getDialog().getWindow().FEATURE_NO_TITLE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge_set, container, false);
    }
    @Override
    public void onStart() {
        //設置對話框的寬高
        getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels;
       // getDialog().getWindow().getAttributes().height = getResources().getDisplayMetrics().heightPixels -500;
        super.onStart();
    }
   public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        evname = (TextView) getView().findViewById(R.id.tv_ev_name);
        //取得點選的充電樁id
        item = getTag();

        switch (item) {
            case "1":
                evname.setText("成大充電樁資訊：");
                break;
            case "2":
                evname.setText("充電樁1R資訊：");
                break;
            case "3":
                evname.setText("充電樁2L資訊：");
                break;
            case "4":
                evname.setText("充電樁2R資訊：");
                break;
        }
        String ip = setting.getString("ip","");
        int Comparesurl = setEV.indexOf("//");
        if(Comparesurl == -1) {
            setEV = ip + setEV;
        }
        int Comparesurl2 = url.indexOf("//");
        if(Comparesurl2 == -1) {
            url = ip + url;
        }
        final ViewGroup evcharge = (ViewGroup) getView().findViewById(R.id.Layout_charge_set);
        LayoutSLMa = (LinearLayout) evcharge.findViewById(R.id.layout_SLisManually);
        LayoutSLAu = (LinearLayout) evcharge.findViewById(R.id.layout_SLisAuto);
        close1 = (Button) evcharge.findViewById(R.id.btn_close1);
        slsave = (Button) evcharge.findViewById(R.id.btn_sl_save);
        SLMaAu = (Spinner)getView().findViewById(R.id.sp_sl_mode);
        SLPWM = (Spinner)getView().findViewById(R.id.sp_sl_current);
        SLSpeed = (Spinner)getView().findViewById(R.id.sp_sl_speed);
        EDslstart = (EditText)getView().findViewById(R.id.ed_sl_start);
        EDslend = (EditText)getView().findViewById(R.id.ed_sl_end);
        EDslsocstart = (EditText)getView().findViewById(R.id.ed_sl_soc_start);
        EDslsocend = (EditText)getView().findViewById(R.id.ed_sl_soc_end);
        EDslstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        EDslstart.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false).show();
            }

        });
        EDslend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        EDslend.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false).show();
            }

        });
        close1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(item);
                if (prev != null) {
                    ft.remove(prev).commit();
                }
            }
        });
        slsave.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] whatMode = getResources().getStringArray(R.array.ManuallyAuto);
                String[] whatPWM = getResources().getStringArray(R.array.EVcurrent);
                String[] whatSpeed = getResources().getStringArray(R.array.ChargingSpeed);
                int getMode = SLMaAu.getSelectedItemPosition();
                int getPWM = SLPWM.getSelectedItemPosition();
                int getSpeed = SLSpeed.getSelectedItemPosition();
                String cluster = "1", pwm_setting = "", socstart = EDslsocstart.getText().toString()
                        , socend = EDslsocend.getText().toString(), charging_speed = "";
                if(whatPWM[getPWM].equals("關閉")){
                    pwm_setting = "0";
                } else if(whatPWM[getPWM].equals("6A")){
                    pwm_setting = "6";
                } else if(whatPWM[getPWM].equals("16A")){
                    pwm_setting = "16";
                } else if(whatPWM[getPWM].equals("32A")){
                    pwm_setting = "32";
                } else if(whatPWM[getPWM].equals("48A")){
                    pwm_setting = "48";
                } else if(whatPWM[getPWM].equals("64A")){
                    pwm_setting = "64";
                } else if(whatPWM[getPWM].equals("80A")){
                    pwm_setting = "80";
                }
                if(whatPWM[getSpeed].equals("關閉")){
                    charging_speed = "0";
                } else if(whatPWM[getSpeed].equals("6A")){
                    charging_speed = "6";
                } else if(whatPWM[getSpeed].equals("16A")){
                    charging_speed = "16";
                } else if(whatPWM[getSpeed].equals("32A")){
                    charging_speed = "32";
                } else if(whatPWM[getSpeed].equals("48A")){
                    charging_speed = "48";
                } else if(whatPWM[getSpeed].equals("64A")){
                    charging_speed = "64";
                } else if(whatPWM[getPWM].equals("80A")){
                    pwm_setting = "80";
                }
                String[] starttime = EDslstart.getText().toString().split(":"), endtime = EDslend.getText().toString().split(":");
                if(whatMode[getMode].equals("一般充電")) {
                    onSetEVManual(cluster, pwm_setting);
                } else if(whatMode[getMode].equals("最佳化充電")) {
                    try {
                        if(Integer.parseInt(starttime[0]) > 23 || Integer.parseInt(starttime[1]) > 60 || Integer.parseInt(endtime[0]) > 23 ||Integer.parseInt(endtime[1]) > 60
                                || Float.parseFloat(socstart) > Float.parseFloat(socend) || Float.parseFloat(socstart) > 100 || Float.parseFloat(socend) > 100){
                            Toast.makeText(getActivity(), "輸入錯誤" , Toast.LENGTH_SHORT).show();
                        } else {
                            onSetEVAuto(cluster, starttime[0], starttime[1], endtime[0], endtime[1], socstart, socend, charging_speed);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();
                    }
                }

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("ChargeSetFragment");
                if (prev != null) {
                    ft.remove(prev).commit();
                }
            }
        });
        SLMaAu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] whatMode = getResources().getStringArray(R.array.ManuallyAuto);
                if(whatMode[position].equals("一般充電")) {
                    LayoutSLAu.setVisibility(View.GONE);
                    LayoutSLMa.setVisibility(View.VISIBLE);
                } else if(whatMode[position].equals("最佳化充電")) {
                    LayoutSLMa.setVisibility(View.GONE);
                    LayoutSLAu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //設置EV充電手動模式
    public void onSetEVManual(String... params) {
        /*JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("setEVDisplay");
        mJsonStr.setCharging_speed(params[1]);
        mJsonStr.setMode("1");
        mJsonStr.setCluster(item);
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        request = new JsonObjectRequest(Request.Method.POST, setEV, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getActivity(),"Result： "+response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);*/
    }
    //設置EV充電自動模式
    public void onSetEVAuto(String... params) {
        /*JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("setEVDisplay");
        mJsonStr.setStart_hour(params[1]);
        mJsonStr.setStart_min(params[2]);
        mJsonStr.setEnd_hour(params[3]);
        mJsonStr.setEnd_min(params[4]);
        mJsonStr.setMode("0");
        mJsonStr.setRemote_charging_setting("1");
        mJsonStr.setManual_initial_soc(String.valueOf(Float.parseFloat(params[5])/100));
        mJsonStr.setManual_final_soc(String.valueOf(Float.parseFloat(params[6])/100));
        mJsonStr.setCharging_speed(params[7]);
        mJsonStr.setCluster(item);
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        request = new JsonObjectRequest(Request.Method.POST, setEV, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    EV_Change();
                    Toast.makeText(getActivity(),"Result： "+response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(request);*/
    }

    public void EV_Change() {
        //以下透過API以JSON格式傳輸資料
       /* postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("EV_Change", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("EV_Change.Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "EV"+item+"_Change");
                params.put("ip", "172.16.0.5");
                params.put("port", "8805");

                return params;
            }
        };
        requestQueue.add(postRequest);*/
    }
}