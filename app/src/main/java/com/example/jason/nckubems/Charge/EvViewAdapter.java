package com.example.jason.nckubems.Charge;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.nckubems.MainActivity;
import com.example.jason.nckubems.PowerInformation;
import com.example.jason.nckubems.R;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/11/011.
 */

public class EvViewAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position。是控制 item 展开的核心
    private BroadcastReceiver mBroadcast =  new BroadcastReceiver() {
        private final static String MY_MESSAGE = "com.givemepass.sendmessage";
        @Override
        public void onReceive(Context mContext, Intent mIntent) {

        }
    };
    public EvViewAdapter(Activity context,
                         ArrayList<HashMap<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
        this.mActivity= context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_ev_view, parent, false);
            holder = new ViewHolder();
            holder.showArea = (LinearLayout) convertView
                    .findViewById(R.id.layout_showArea);
            holder.ev_set = (ImageButton) convertView
                    .findViewById(R.id.imgbtn_ev_set);
            holder.tv_ev_num = (TextView) convertView
                    .findViewById(R.id.tv_ev_num);
            holder.tv_ev_status = (TextView) convertView
                    .findViewById(R.id.tv_ev_status);
            holder.tv_ev_voltage = (TextView) convertView
                    .findViewById(R.id.tv_ev_voltage);
            holder.tv_ev_current = (TextView) convertView
                    .findViewById(R.id.tv_ev_current);
            holder.tv_ev_p = (TextView) convertView
                    .findViewById(R.id.tv_ev_p);
            holder.tv_ev_time = (TextView) convertView
                    .findViewById(R.id.tv_ev_time);
            holder.tv_ev_elc = (TextView) convertView
                    .findViewById(R.id.tv_ev_elc);
            holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的样例里是 showArea 的线性布局）加入Tag。为了记录点击的 position。我们正好用 position 设置 Tag
        switch (item.get("ev_status")) {
            case "閒置中":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.violet));
                break;
            case "充電中":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.limegreen));
                break;
            case "Finish Charging (充電結束)":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));
                break;
            case "Reservation Charging (預約充電中)":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.Green));
                break;
            case "Watting Reservation (等待預約中)":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.violet));
                break;
            case "Certification (認證中)":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.violet));
                break;
            case "異常":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));
                break;
            case "Closed (關閉中)":
                holder.tv_ev_status.setTextColor(ContextCompat.getColor(context, R.color.White));
                holder.tv_ev_status.setBackgroundColor(ContextCompat.getColor(context, R.color.violet));
                break;
        }
        holder.showArea.setTag(position);
        holder.tv_ev_num.setText(item.get("ev_num"));
        holder.tv_ev_status.setText(item.get("ev_status"));
        holder.tv_ev_voltage.setText(item.get("ev_voltage"));
        holder.tv_ev_current.setText(item.get("ev_current"));
        holder.tv_ev_p.setText(item.get("ev_p"));
        holder.tv_ev_time.setText(item.get("ev_time"));
        holder.tv_ev_elc.setText(item.get("ev_elc"));


        //依据 currentItem 记录的点击位置来设置"相应Item"的可见性（在list依次载入列表数据时，每载入一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }

        holder.showArea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                }
                //通知adapter数据改变须要又一次载入
                notifyDataSetChanged(); //必须有的一步
            }
        });
        holder.ev_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "AAA", Toast.LENGTH_LONG).show();
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(mActivity,EvSetActivity.class);
                mActivity.startActivity(reit);
                mActivity.finish();
                /*FragmentTransaction ft1 = mActivity.getFragmentManager().beginTransaction();
                Fragment prev1 = mActivity.getFragmentManager().findFragmentByTag("ChargeSet");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                ft1.addToBackStack(null);
                // Create and show the dialog.
                ChargeSetFragment newFragment1 = new ChargeSetFragment();
                newFragment1.show(ft1, list.get(position).get("ev_cluster"));
                Log.d("ev_cluster", list.get(position).get("ev_cluster"));*/
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        private LinearLayout showArea;
        private TextView
                tv_ev_num, tv_ev_status, tv_ev_voltage, tv_ev_current, tv_ev_p, tv_ev_time,
                tv_ev_elc;
        private ImageButton ev_set;
        private LinearLayout hideArea;
    }
    private void SetHeight(ListView list) {

    }
}