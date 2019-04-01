package com.example.jason.nckubems;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ChangePWDActivity extends AppCompatActivity {
    private EditText oldPWD, newPWD, checkPWD;
    private Button pwdReturn, pwdConfirm;
    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        oldPWD = (EditText) findViewById(R.id.et_oldpwd);
        newPWD = (EditText) findViewById(R.id.et_newpwd);
        checkPWD = (EditText) findViewById(R.id.et_checkpwd);
        pwdConfirm = (Button) findViewById(R.id.btn_cpwd_confirm);
        pwdReturn = (Button) findViewById(R.id.btn_cpwd_return);
        requestQueue = Volley.newRequestQueue(this);
        pref = getSharedPreferences("auto", 0);

        pwdReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(ChangePWDActivity.this,MainActivity.class);
                startActivity(reit);
                ChangePWDActivity.this.finish();
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent reit1 = new Intent();
        reit1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        reit1.setClass(ChangePWDActivity.this,MainActivity.class);
        startActivity(reit1);
        ChangePWDActivity.this.finish();
        return true;
    }
}
