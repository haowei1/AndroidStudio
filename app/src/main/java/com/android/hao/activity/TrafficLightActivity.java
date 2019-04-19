package com.android.hao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

public class TrafficLightActivity extends AppCompatActivity {

    private static final String TAG = "TrafficLightActivity";
    private String path = "GetTrafficLightConfigAction";
    private Spinner spinner;
    private Button btn;
    private ListView lv;
    private List<List<Integer>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_light);

        initData();
        initView();
    }

    private synchronized void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("TrafficLightId", "1");
        OkHttpUtils.getDataFromIntent(path, map, new OkHttpUtils.CallBack() {
            @Override
            public void successful(final String string) {
                Log.i(TAG, "successful: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.add(praseJson(string, 1));
                    }
                });
            }
        });
        Map<String, String> map2 = new HashMap<>();
        map2.put("TrafficLightId", "2");
        OkHttpUtils.getDataFromIntent(path, map2, new OkHttpUtils.CallBack() {
            @Override
            public void successful(final String string) {
                Log.i(TAG, "successful: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.add(praseJson(string, 2));
                    }
                });
            }
        });
        Map<String, String> map3 = new HashMap<>();
        map3.put("TrafficLightId", "3");
        OkHttpUtils.getDataFromIntent(path, map3, new OkHttpUtils.CallBack() {
            @Override
            public void successful(final String string) {
                Log.i(TAG, "successful: " + string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.add(praseJson(string, 3));
                    }
                });
            }
        });
    }

    private void initView() {
        spinner = findViewById(R.id.spinner);
        btn = findViewById(R.id.btn);
        lv = findViewById(R.id.lv);

        lv.addHeaderView(View.inflate(getApplicationContext(),R.layout.item_traffic_hader, null));
        if (list != null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv.setAdapter(new MyAdapter());
                        }
                    });
                }
            }, 0,3000);
            lv.setAdapter(new MyAdapter());
        }
    }

    private List<Integer> praseJson(String string, int i) {
        List<Integer> l1 = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(string);
            int yellowTime = jo.getInt("YellowTime");
            int greenTime = jo.getInt("GreenTime");
            int redTime = jo.getInt("RedTime");
            l1.add(i);
            l1.add(redTime);
            l1.add(yellowTime);
            l1.add(greenTime);
            return l1;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    class MyAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(),R.layout.item_traffic_hader,null);
                viewHolder = new ViewHolder();
                viewHolder.id = view.findViewById(R.id.id);
                viewHolder.red = view.findViewById(R.id.red);
                viewHolder.yellow = view.findViewById(R.id.yellow);
                viewHolder.green = view.findViewById(R.id.green);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.id.setText(list.get(position).get(0)+"");
            viewHolder.red.setText(list.get(position).get(1)+"");
            viewHolder.yellow.setText(list.get(position).get(2)+"");
            viewHolder.green.setText(list.get(position).get(3)+"");

            return view;
        }
    }
    class ViewHolder {
        TextView id, red, yellow, green;
    }
}
