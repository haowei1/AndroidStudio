package com.android.hao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.hao.R;
import com.android.hao.pojo.AllSense;
import com.android.hao.utils.OkHttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AllSenseActivity extends AppCompatActivity {

    private GridView gv;
    private String path = "GetAllSense";
    private static final String TAG = "AllSenseActivity";
    private List<Integer> list = new ArrayList<>();
    private List<String> key;
    private List<Integer> yu;
    private Timer timer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AllSense allsense = parserJson((String) msg.obj);
            switch (msg.what) {
                case 1:
                    list.add(allsense.getPm());
                    list.add(allsense.getTemperature());
                    list.add(allsense.getCo2());
                    list.add(allsense.getHumidity());
                    list.add(allsense.getLightIntensity());
                    break;
                case 2:
                    list.add(allsense.getStatus());
                    break;
            }
            Log.e(TAG, "handleMessage: list" + list);
            gv.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_5);

        initView();
        initTimer();
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                list.clear();
                initData();
            }
        }, 0, 3000);
    }

    private void initData() {
        key = new ArrayList<>();
        key.add("温度");
        key.add("湿度");
        key.add("光照");
        key.add("CO2");
        key.add("PM2.5");
        key.add("道路状态");

        yu = new ArrayList<>();
        yu.add(30);
        yu.add(33);
        yu.add(221);
        yu.add(531);
        yu.add(331);
        yu.add(3);

        Map<String, String> map = new HashMap<>();
        OkHttpUtils.getDataFromIntent(path, map, new OkHttpUtils.CallBack() {
            @Override
            public void successful(String string) {
                Log.e(TAG, "successful: body:" + string);
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("RoadId", "1");
                OkHttpUtils.getDataFromIntent("GetRoadStatus", map1, new OkHttpUtils.CallBack() {
                    @Override
                    public void successful(String string) {
                        Log.e(TAG, "successful: road:" + string);
                        Message msg = new Message();
                        msg.obj = string;
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void exception(Exception e) {

                    }
                });
                Message msg = new Message();
                msg.what = 1;
                msg.obj = string;
                handler.sendMessage(msg);
            }

            @Override
            public void exception(Exception e) {
                Log.e(TAG, "exception: e" + e);
            }
        });

    }

    private AllSense parserJson(String result) {
        return new Gson().fromJson(result, AllSense.class);
    }

    private void initView() {
        gv = findViewById(R.id.gv);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Integer getItem(int position) {
            return list.size();
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
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (key != null) {
                viewHolder.key.setText(key.get(position));
            }
            if (list != null) {
                viewHolder.value.setText(list.get(position)+"");

                if (list.get(position) > yu.get(position)) {
                    view.setBackgroundColor(Color.RED);
                }else {
                    view.setBackgroundColor(Color.GREEN);
                }
             }
            return view;
        }
    }

    class ViewHolder{
        TextView key, value;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
