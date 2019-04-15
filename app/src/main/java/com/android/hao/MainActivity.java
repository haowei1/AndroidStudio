package com.android.hao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private Button btn, btn1, btn2;
    private EditText et;
    private TextView tv;
    private String uri;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String data = (String) msg.obj;
            updateUI(data);
        }
    };

    private void updateUI(String data) {
        tv.setText(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);

        uri = et.getText().toString().trim();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataStringRequest();
//                initDataJsonRequest();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataOkHttp();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FragmentActivity.class));
            }
        });
    }

    private void initDataStringRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("Volley请求成功"+ s);
                        Message message = new Message();
                        message.obj = s;
                        handler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("请求失败" + volleyError);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("CarId","1");
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        }).start();

    }

    private void initDataOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Map<String,Integer> map = new HashMap<>();
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
                            Message message = new Message();
                            message.obj = body;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();
    }
}
