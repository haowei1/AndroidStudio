package com.android.hao;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FragmentActivity extends AppCompatActivity {

    private GridView gv;
    private List<String> key, value, yu;
    private String uri = "http://192.168.1.3:8890/transportservice/type/jason/action/GetAllSense";
    private static final String TAG = "FragmentActivity";
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initTimer();
        setContentView(R.layout.fragment_5);
        initView();
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initData();
            }
        }, 5000);
    }

    private void initView() {
        gv = findViewById(R.id.gv);

        gv.setAdapter(new MyAdapter());
    }

    private void initData() {

        getAllSence(new callBack() {
            @Override
            public void success(List<String> result) {
                value = result;
                Log.i(TAG, "success: result" + result.toString());
                Log.i(TAG, "success: value" + value.toString());
            }
        });

        key = new ArrayList<>();
        key.add("温度");
        key.add("湿度");
        key.add("光照");
        key.add("CO2");
        key.add("PM2.5");
        key.add("道路状态");

//        value = new ArrayList<>();
//        value.add("32");
//        value.add("60");
//        value.add("321");
//        value.add("223");
//        value.add("331");
//        value.add("4");

        yu = new ArrayList<>();
        yu.add("30");
        yu.add("80");
        yu.add("500");
        yu.add("222");
        yu.add("400");
        yu.add("3");

    }

    private void getAllSence(final callBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Map<String, Integer> map = new HashMap<>();
                map.put("CarId", 1);
                String json = gson.toJson(map);

                MediaType Json = MediaType.parse("application/json;charset=utf-8");
                //1.创建OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
//                RequestBody requestBody = new FormBody.Builder().add("CarId", 1).build();
                RequestBody body = RequestBody.create(Json, json);
                //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
                final okhttp3.Request request = new Request.Builder().url(uri).post(body).build();
                //4.创建一个call对象,参数就是Request请求对象
                Call call = okHttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("请求失败" + e);
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            System.out.println("OkHttp请求成功" + body);
                            List<String> value = parseJson(body);
                            callBack.success(value);
                        }
                    }
                });
            }
        }).start();
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


    interface callBack {
        void success(List<String> result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
