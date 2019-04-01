package com.example.jason.nckubems;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LoginActivity extends AppCompatActivity {
    private TextView tvUser, tvPass,tvCheck,tvRandom;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private CheckBox check_auto,check_remember;
    String username,password,ver;
    private SharedPreferences pref,setting;             //使用SharedPreferences進行讀取
    private SharedPreferences.Editor editor,settingedit; //使用SharedPreferences.Editor進行儲存
    //private String url = "http://140.116.163.19:10107/sl_new/sl_demo_api/login.php";
    private String url = "http://140.116.163.19:10107/epslab_ems_api/login.php";
    public static File file;
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        tvUser = findViewById(R.id.tv_user);
        tvPass = findViewById(R.id.tv_pass);
        tvCheck = findViewById(R.id.tv_check);
        tvRandom = findViewById(R.id.tv_random);
        check_auto = findViewById(R.id.check_auto);
        check_remember = findViewById(R.id.check_remember);
        pref = getSharedPreferences("LoginInfo",MODE_PRIVATE);
        setting = getSharedPreferences("auto",MODE_PRIVATE);
        settingedit = setting.edit();
        editor = pref.edit();
        int num = (int) (Math.random()*8999)+1000;
        tvRandom.setText(String.valueOf(num)); //驗證碼

        boolean autoisRemember = pref.getBoolean("auto_check",false);  //判斷LoginInfo內記錄的自動登入是否有被勾選
        Log.d("AUTO",String.valueOf(autoisRemember));
        //建立一個file，有實體的SharedPreferences才可在不同的Activity存取資料
         file = new File("/data/data/net.ddns.b505.xlbems/shared_prefs","auto.xml");
        //如果自動登入有被勾選
        if(autoisRemember){
            //讀取已被儲存的使用者資料
            username = pref.getString("UserName","");
            //假如user不為空值，代表先前已有登入成功過
            if(!username.equals("")){
                SendIntent(); //直接跳轉至至主頁面(MainActivity)
            }
        }
        //查看app中是否已經儲存過帳號密碼，有的話直接顯示出來
        boolean memoryisRemember = pref.getBoolean("login_check",false);
        Log.d("Memory",String.valueOf(memoryisRemember));
        //如果記住帳號有被勾選
        if(memoryisRemember) {
            if(autoisRemember) {
                check_auto.setChecked(true);
            }
            //取出已被儲存的使用者資料
            String user = pref.getString("UserName", "");
            String password = pref.getString("Password", "");
            tvUser.setText(user);
            tvPass.setText(password);
            check_remember.setChecked(true);
        }
    }
    public void Reset(View v){
        int num = (int) (Math.random()*8999)+1000;
        tvRandom.setText(String.valueOf(num));
        Log.d("驗證碼", String.valueOf(num));
    }
    public void OnLogin(View v) {
        username = tvUser.getText().toString();
        password = tvPass.getText().toString();
        ver = tvCheck.getText().toString();
        if(!(ver.equals(tvRandom.getText().toString()))){
            Toast.makeText(getApplicationContext(),"驗證碼錯誤!",Toast.LENGTH_SHORT).show();
            tvCheck.setText("");
        }else {

            final JSONObject body = new JSONObject();
            try {
                body.put("action", "login");
                body.put("account", username);
                body.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("body", body.toString());
                    // response
                    try {
                        //JSONArray array = new JSONArray(response);
                        JSONObject object = new JSONObject(response);
                        Log.d("Login", object.toString());
                        Token = object.getJSONObject("data").getString("token");
                        if (object.getBoolean("status") == true) {
                            settingedit.putString("TOKEN",Token).commit();
                            settingedit.putString("User",username).commit();
                            //是否自動登入
                            if (check_auto.isChecked()) {
                                editor.putBoolean("auto_check", true);
                            } else {
                                editor.putBoolean("auto_check", false);
                            }
                            //記住密碼
                            if (check_remember.isChecked()) {
                                editor.putBoolean("login_check", true);
                                editor.putString("UserName", username);
                                editor.putString("Password", password);
                            } else {
                                editor.clear();
                            }
                            //使用commit將添加的數據提交
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "登入成功！", Toast.LENGTH_SHORT).show();
                            SendIntent();
                        } else if (object.getBoolean("status") == false) {
                            Toast.makeText(getApplicationContext(), "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        Log.e("Login", e.toString());
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
            requestQueue.add(postRequest);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 攔截返回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //跳出結束對話框
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    username = tvUser.getText().toString();
                                    password = tvPass.getText().toString();
                                    editor.putString("UserName",username);
                                    if(check_remember.isChecked()){
                                        editor.putBoolean("login_check",true);
                                        editor.putString("Password",password);
                                    }else{
                                        editor.clear();
                                    }
                                    editor.commit();
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // 點選取消就不做任何事
                                }
                            }).show();
        }
        return true;
    }
    //跳轉至主頁面
    public void SendIntent() {
        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(it);
        LoginActivity.this.finish();
    }
}
