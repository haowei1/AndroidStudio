package com.android.hao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hao.R;
import com.android.hao.utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentActivity extends AppCompatActivity {

    private GridView gv;
    private List<String> key, value, yu;
    private static final String TAG = "FragmentActivity";
    private Timer timer;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            gv.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initTimer();
        setContentView(R.layout.fragment_5);
        initView();
//        SpUtils.setBoolean(getApplicationContext(),"isfirst", true);
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initData();
                handler.sendEmptyMessage(0);
            }
        }, 0, 5000);
    }

    private void initView() {
        gv = findViewById(R.id.gv);

        gv.setAdapter(new MyAdapter());
    }

    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("CarId", "1");
        OkHttpUtils.getDataFromIntent("GetCarSpeed", map, new OkHttpUtils.CallBack() {
            @Override
            public void successful(String string) {
                value = parseJson(string);
            }
        });

        key = new ArrayList<>();
        key.add("温度");
        key.add("湿度");
        key.add("光照");
        key.add("CO2");
        key.add("PM2.5");
        key.add("道路状态");

        yu = new ArrayList<>();
        yu.add("30");
        yu.add("80");
        yu.add("500");
        yu.add("222");
        yu.add("400");
        yu.add("3");

    }

    private List<String> parseJson(String body) {
        ArrayList<String> json = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(body);
            String wd = jo.getString("temperature");
            String sd = jo.getString("humidity");
            String gz = jo.getString("LightIntensity");
            String co2 = jo.getString("co2");
            String pm = jo.getString("pm2.5");
            Log.i(TAG, "parseJson: wd" + wd +""+ sd + gz + co2 + pm);
            json.add(wd);
            json.add(sd);
            json.add(gz);
            json.add(co2);
            json.add(pm);
            json.add("4");
            Log.i(TAG, "parseJson: " + json.toString());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return key.size();
        }

        @Override
        public String getItem(int position) {
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
            if (key != null) {
                viewHolder.key.setText(key.get(position));
            }
            if (value != null) {
                Log.i(TAG, "getView: value" + value.toString());
                viewHolder.value.setText(value.get(position));
                if (Integer.parseInt(value.get(position)) > Integer.parseInt(yu.get(position))) {
                    view.setBackgroundColor(Color.RED);
                } else {
                    view.setBackgroundColor(Color.GREEN);
                }
            }
            return view;
        }
    }

    class ViewHolder {
        TextView key;
        TextView value;
        RelativeLayout rl;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
