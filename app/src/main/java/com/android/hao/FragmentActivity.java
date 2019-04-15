package com.android.hao;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends AppCompatActivity {

    private GridView gv;
    private List<String> key, value, yu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_5);
        initView();
    }

    private void initView() {
        gv = findViewById(R.id.gv);
        initData();
        gv.setAdapter(new MyAdapter());
    }

    private void initData() {
        key = new ArrayList<>();
        key.add("温度");
        key.add("湿度");
        key.add("光照");
        key.add("CO2");
        key.add("PM2.5");
        key.add("道路状态");

        value = new ArrayList<>();
        value.add("32");
        value.add("60");
        value.add("321");
        value.add("223");
        value.add("331");
        value.add("4");

        yu = new ArrayList<>();
        yu.add("30");
        yu.add("80");
        yu.add("500");
        yu.add("222");
        yu.add("400");
        yu.add("3");

    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return key.size();
        }

        @Override
        public Object getItem(int position) {
            return key.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_fragment5, null);
                viewHolder = new ViewHolder();

                viewHolder.key = view.findViewById(R.id.key);
                viewHolder.value = view.findViewById(R.id.value);
                viewHolder.rl = view.findViewById(R.id.rl);

                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.key.setText(key.get(position));
            viewHolder.value.setText(value.get(position));

            if (Integer.parseInt(value.get(position)) > Integer.parseInt(yu.get(position))) {
                view.setBackgroundColor(Color.RED);
            } else {
                view.setBackgroundColor(Color.GREEN);
            }
            return view;
        }
    }

    class ViewHolder{
        TextView key;
        TextView value;
        RelativeLayout rl;
    }
}
