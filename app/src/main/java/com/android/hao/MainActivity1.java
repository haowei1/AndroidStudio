package com.android.hao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity1 extends AppCompatActivity {

    private Button btn;
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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btn = findViewById(R.id.btn);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);

        uri = et.getText().toString().trim();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataOkHttp();
            }
        });
    }

    private void initDataOkHttp() {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        RequestBody requestBody = new FormBody.Builder().add("CarId","2").build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        final Request request = new Request.Builder().url(uri).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    System.out.println("请求成功" + body);
                    Message message = new Message();
                    message.obj = body;
                    handler.sendMessage(message);
                }
            }
        });
    }

}
