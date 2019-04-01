package com.example.jason.nckubems.DR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jason.nckubems.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yan on 2018/3/12/012.
 */

public class UserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position。是控制 item 展开的核心

    public UserAdapter(Context context,
                       ArrayList<HashMap<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
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
        UserAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_user_event, parent, false);
            holder = new UserAdapter.ViewHolder();
            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_user_start = (TextView) convertView.findViewById(R.id.tv_user_start);
            holder.tv_user_end = (TextView) convertView.findViewById(R.id.tv_user_end);
            holder.tv_user_p = (TextView) convertView.findViewById(R.id.tv_user_p);
            holder.tv_user_real_p = (TextView) convertView.findViewById(R.id.tv_user_real_p);
            holder.tv_user_use_p = (TextView) convertView.findViewById(R.id.tv_user_use_p);
            holder.tv_user_load_p = (TextView) convertView.findViewById(R.id.tv_user_load_p);
            holder.tv_user_money = (TextView) convertView.findViewById(R.id.tv_user_money);
            holder.tv_user_real_money = (TextView) convertView.findViewById(R.id.tv_user_real_money);
            holder.tv_user_result = (TextView) convertView.findViewById(R.id.tv_user_result);
            holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);

            convertView.setTag(holder);
        } else {
            holder = (UserAdapter.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的样例里是 showArea 的线性布局）加入Tag。为了记录点击的 position。我们正好用 position 设置 Tag
        holder.showArea.setTag(position);
        holder.tv_user_name.setText(item.get("tv_user_name"));
        holder.tv_user_start.setText(item.get("tv_user_start"));
        holder.tv_user_end.setText(item.get("tv_user_end"));
        holder.tv_user_p.setText(item.get("tv_user_p"));
        holder.tv_user_real_p.setText(item.get("tv_user_real_p"));
        holder.tv_user_use_p.setText(item.get("tv_user_use_p"));
        holder.tv_user_load_p.setText(item.get("tv_user_load_p"));
        holder.tv_user_money.setText(item.get("tv_user_money"));
        holder.tv_user_real_money.setText(item.get("tv_user_real_money"));
        holder.tv_user_result.setText(item.get("tv_user_result"));

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
        /*holder.tv_history_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "hehe", Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }

    private static class ViewHolder {
        private LinearLayout showArea;
        private TextView tv_user_name, tv_user_start, tv_user_end, tv_user_p, tv_user_real_p, tv_user_use_p,
                tv_user_load_p, tv_user_money, tv_user_real_money, tv_user_result;
        private LinearLayout hideArea;
    }
}
