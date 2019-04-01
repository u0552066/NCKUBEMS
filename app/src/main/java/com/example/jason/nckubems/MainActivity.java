package com.example.jason.nckubems;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jason.nckubems.Charge.EvHistoryFragment;
import com.example.jason.nckubems.DR.UserActivity;
import com.example.jason.nckubems.GreenEnergy.SolarHistoryFragment;
import com.example.jason.nckubems.GreenEnergy.WindHistoryFragment;
import com.example.jason.nckubems.Main.ChargeFragment;
import com.example.jason.nckubems.Main.DRFragment;
import com.example.jason.nckubems.Main.GreenEnergyFragment;
import com.example.jason.nckubems.Main.IndexFragment;
import com.example.jason.nckubems.Main.ScheduleFragment;
import com.example.jason.nckubems.Schedule.EssHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    private SharedPreferences pref,setting;             //使用SharedPreferences進行讀取
    private SharedPreferences.Editor editor,settingedit; //使用SharedPreferences.Editor進行儲存
    private Toolbar toolbar;
    private TextView title;
    String user;
    private int[] TollBarTitle = {R.string.title_home,R.string.title_schedule,R.string.title_green_energy,R.string.title_charge,R.string.title_dr};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("LoginInfo",MODE_PRIVATE);
        editor = pref.edit();
        setting = this.getSharedPreferences("auto",0);
        settingedit = setting.edit();
        user = setting.getString("User", ""); //取出使用者帳號
        Log.d("User","使用者: "+user);
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        setTitle(null); //取消原本的標題
        title = findViewById(R.id.toolbarTitle);
        title.setText(TollBarTitle[0]);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //toolbar.getMenu().clear();
                        switch (item.getItemId()) {
                            //點擊首頁
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                title.setText(TollBarTitle[0]);
                                break;
                            //點擊儲能系統
                            case R.id.navigation_schedule:
                                viewPager.setCurrentItem(1);
                                title.setText(TollBarTitle[1]);
                                break;
                            //點擊綠能發電
                            case R.id.navigation_green_energy:
                                viewPager.setCurrentItem(2);
                                title.setText(TollBarTitle[2]);
                                break;
                            //點擊儲能櫃
                            case R.id.navigation_charge:
                                viewPager.setCurrentItem(3);
                                title.setText(TollBarTitle[3]);
                                break;
                            //點擊需量競價
                            case R.id.navigation_dr:
                                viewPager.setCurrentItem(4);
                                title.setText(TollBarTitle[4]);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                toolbar.getMenu().clear();
                switch (position) {
                    case 0:
                        toolbar.inflateMenu(R.menu.menu_home);
                        title.setText(TollBarTitle[0]);
                        break;
                    case 1:
                        toolbar.inflateMenu(R.menu.menu_schedule);
                        title.setText(TollBarTitle[1]);
                        break;
                    case 2:
                        toolbar.inflateMenu(R.menu.menu_green_energy);
                        title.setText(TollBarTitle[2]);
                        break;
                    case 3:
                        toolbar.inflateMenu(R.menu.menu_charge);
                        title.setText(TollBarTitle[3]);
                        break;
                    case 4:
                        toolbar.inflateMenu(R.menu.menu_dr);
                        title.setText(TollBarTitle[4]);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 如果想禁止滑動，可以把下面的代碼取消註解
     /* viewPager.setOnTouchListener(new View.OnTouchListener() {
           @Override
          public boolean onTouch(View v, MotionEvent event) {
               return true;
          }
        });*/

        //設定fragment分頁頁數
        setViewPager();
    }
    //設置Activity初始的Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    //toolbar按鈕事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.ItemChargehistory: //充電樁歷史資訊
                FragmentTransaction EvHistoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment EvHistory = getSupportFragmentManager().findFragmentByTag("EvHistoryFragment");
                if (EvHistory != null) {
                    EvHistoryFragmentTransaction.remove(EvHistory);
                }
                EvHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Ev = new EvHistoryFragment();
                newFragment_Ev.show(EvHistoryFragmentTransaction, "EvHistoryFragment");
                break;
            case R.id.Information_solar: //太陽能資訊
                FragmentTransaction SolarHistoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment SolarHistory = getSupportFragmentManager().findFragmentByTag("SolarHistoryFragment");
                if (SolarHistory != null) {
                    SolarHistoryFragmentTransaction.remove(SolarHistory);
                }
                SolarHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Solar = new SolarHistoryFragment();
                newFragment_Solar.show(SolarHistoryFragmentTransaction, "SolarHistoryFragment");
                break;
            case R.id.Information_wind: //風力資訊
                FragmentTransaction WindHistoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment WindHistory = getSupportFragmentManager().findFragmentByTag("WindHistoryFragment");
                if (WindHistory != null) {
                    WindHistoryFragmentTransaction.remove(WindHistory);
                }
                WindHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Wind = new WindHistoryFragment();
                newFragment_Wind.show(WindHistoryFragmentTransaction, "WindHistoryFragment");
                break;
            case R.id.HistoryInformation: //儲能系統歷史資訊
                FragmentTransaction EssHistoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment EssHistory = getSupportFragmentManager().findFragmentByTag("EssHistoryFragment");
                if (EssHistory != null) {
                    EssHistoryFragmentTransaction.remove(EssHistory);
                }
                EssHistoryFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_Ess = new EssHistoryFragment();
                newFragment_Ess.show(EssHistoryFragmentTransaction, "EssHistoryFragment");
                break;
            case R.id.EnergyUsed: //用戶用電資訊
                Intent reit1 = new Intent();
                reit1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit1.setClass(MainActivity.this,PowerInformation.class);
                startActivity(reit1);
                MainActivity.this.finish();
                break;
            case R.id.ItemUser: //用戶歷史資訊
                Intent reit2 = new Intent();
                reit2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit2.setClass(MainActivity.this,UserActivity.class);
                startActivity(reit2);
                MainActivity.this.finish();
                break;
            case R.id.changePWD: //變更密碼
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(MainActivity.this,ChangePWDActivity.class);
                startActivity(reit);
                MainActivity.this.finish();
                break;
            case R.id.ItemLogout: //登出
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認視窗")
                        .setMessage("確定要登出嗎?")
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //File file = new File("/data/data/net.ddns.b505.xlbems/shared_prefs","auto.xml");
                                        //file.delete();
                                        editor.putBoolean("auto_check",false).commit();
                                        Intent reit2 = new Intent();
                                        reit2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        reit2.setClass(MainActivity.this,LoginActivity.class);
                                        startActivity(reit2);
                                        MainActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).show();
                break;

            default: return false;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
        }
        return true;
    }

    private void setViewPager(){
        IndexFragment Home = new IndexFragment();
        ScheduleFragment Schedule = new ScheduleFragment();
        GreenEnergyFragment GEnergy = new GreenEnergyFragment();
        ChargeFragment Charge = new ChargeFragment();
        DRFragment Reaction = new DRFragment();
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(Home);
        fragmentList.add(Schedule);
        fragmentList.add(GEnergy);
        fragmentList.add(Charge);
        fragmentList.add(Reaction);
        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myFragmentAdapter);
    }
}
