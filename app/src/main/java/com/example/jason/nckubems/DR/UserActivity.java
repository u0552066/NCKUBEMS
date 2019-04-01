package com.example.jason.nckubems.DR;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.nckubems.MainActivity;
import com.example.jason.nckubems.PowerInformation;
import com.example.jason.nckubems.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title, txtP, kw;
    private RequestQueue requestQueue;
    private String DR = "xlii_hems_api/dr.php";
    private SharedPreferences setting;
    private String token, ip;
    private StringRequest request;
    private TextView txtWinningprice;
    private Timer timer;
    private int mYear, mMonth, mDay;
    private TimerTask task;
    private EditText txtTime;
    private int num = 0;
    private ListView lvProduct;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        requestQueue = Volley.newRequestQueue(this);
        setting = this.getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        ip = setting.getString("ip","");
        int Comparesurl = DR.indexOf("//");
        if(Comparesurl == -1) {
            DR = ip + DR;
            DR = "http://59.125.180.237/xlii_bems_api/dr.php";
        }
        txtTime = (EditText)findViewById(R.id.tv_time);
        //------初始設定-----
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        title = (TextView)findViewById(R.id.toolbarTitle);
        lvProduct = (ListView) findViewById(R.id.lv_products);
        title.setText("用戶歷史事件");
        setTitle(null);  //取消原本的標題
        setSupportActionBar(toolbar);
        //------到這結束-----
        Button btnDay = (Button) findViewById(R.id.btn_day);
        btnDay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                datas.clear();
                UserAdapter adapter = new UserAdapter(UserActivity.this, datas);
                lvProduct.setAdapter(adapter);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(UserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year,month,day);
                        txtTime.setText(format);
                        getDREventInDay(year, month, day);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
    }

    private void getDREventInDay(int year, int month, int day) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getDREventInDay");
            body.put("token","b8722a4431d6e749314dae271ec8c8f");
            body.put("field","xinglong2");
            body.put("year", String.valueOf(year));
            body.put("month", String.valueOf(month + 1));
            body.put("day", String.valueOf(day));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new StringRequest(Request.Method.POST, DR,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("getDREventInDay", response);
                    JSONObject object = null;
                    object = new JSONObject(response);
                    num = response.length();
                    Log.d("num", String.valueOf(response.length()));

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatter.setLenient(false);
                    Date now = new Date() ; // 獲取當前時間
                    /*for(int i = 0; i < num; i++){
                        String[] start = object.getJSONObject(String.valueOf(i)).getString("start_at").split(" ");
                        String[] end = object.getJSONObject(String.valueOf(i)).getString("end_at").split(" ");
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put("tv_user_name", object.getJSONObject(String.valueOf(i)).getString("address"));
                        item.put("tv_user_start", start[1]);
                        item.put("tv_user_end", end[1]);
                        item.put("tv_user_p", object.getJSONObject(String.valueOf(i)).getString("amount"));
                        item.put("tv_user_real_p", object.getJSONObject(String.valueOf(i)).getString("duration"));
                        item.put("tv_user_use_p", object.getJSONObject(String.valueOf(i)).getString("actual_load_shedding"));
                        item.put("tv_user_load_p", object.getJSONObject(String.valueOf(i)).getString("expected_reward"));
                        item.put("tv_user_money", object.getJSONObject(String.valueOf(i)).getString("actual_reward"));
                        item.put("tv_user_real_money", object.getJSONObject(String.valueOf(i)).getString("baseline"));
                        item.put("tv_user_result", String.format("%.2f",Double.parseDouble(object.getJSONObject(String.valueOf(i)).getString("result"))) + " %");
                        datas.add(item);
                    }*/        HashMap<String, String> item = new HashMap<String, String>();
                    item.put("tv_user_name", "92702室");
                    item.put("tv_user_start", "15:00");
                    item.put("tv_user_end", "16:00");
                    item.put("tv_user_p", "20");
                    item.put("tv_user_real_p", "23");
                    item.put("tv_user_use_p", "69");
                    item.put("tv_user_load_p", "46");
                    item.put("tv_user_money", "160");
                    item.put("tv_user_real_money", "160");
                    item.put("tv_user_result", "成功");
                    datas.add(item);
                    HashMap<String, String> item1 = new HashMap<String, String>();
                    item1.put("tv_user_name", "92710室");
                    item1.put("tv_user_start", "15:00");
                    item1.put("tv_user_end", "16:00");
                    item1.put("tv_user_p", "20");
                    item1.put("tv_user_real_p", "23");
                    item1.put("tv_user_use_p", "69");
                    item1.put("tv_user_load_p", "46");
                    item1.put("tv_user_money", "160");
                    item1.put("tv_user_real_money", "0");
                    item1.put("tv_user_result", "失敗");
                    datas.add(item1);
                    HashMap<String, String> item2 = new HashMap<String, String>();
                    item2.put("tv_user_name", "俊銘講堂");
                    item2.put("tv_user_start", "15:00");
                    item2.put("tv_user_end", "16:00");
                    item2.put("tv_user_p", "20");
                    item2.put("tv_user_real_p", "23");
                    item2.put("tv_user_use_p", "69");
                    item2.put("tv_user_load_p", "46");
                    item2.put("tv_user_money", "160");
                    item2.put("tv_user_real_money", "160");
                    item2.put("tv_user_result", "成功");
                    datas.add(item2);
                    UserAdapter adapter = new UserAdapter(UserActivity.this, datas);
                    lvProduct.setAdapter(adapter);
                } catch (Exception  e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent reit = new Intent();
            reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            reit.setClass(UserActivity.this,MainActivity.class);
            startActivity(reit);
            UserActivity.this.finish();
        }
        return true;
    }
}
