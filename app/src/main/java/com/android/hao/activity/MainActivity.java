package com.android.hao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hao.R;
import com.android.hao.utils.OkHttpUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn, btn1, btn2,btn3, btn4, btn5, btn6, btn7;
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
    private HashMap<String, String> map;

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
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        TextView marquee = findViewById(R.id.marquee);
        marquee.setSelected(true);
        uri = et.getText().toString().trim();
        map = new HashMap<>();
        map.put("CarId","1");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataStringRequest();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.getDataFromIntent("GetAllSense", map, new OkHttpUtils.CallBack() {
                    @Override
                    public void successful(final String string) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(string);
                            }
                        });
                    }

                    @Override
                    public void exception(Exception e) {

                    }
                });
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllSenseActivity.class));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TestDBActivity.class));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewTrafficLightActivity.class));
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RedioViewActivity.class));
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BusActivity.class));
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotifyActivity.class));
            }
        });
    }

    /**
     * volley获取数据
     */
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
}
