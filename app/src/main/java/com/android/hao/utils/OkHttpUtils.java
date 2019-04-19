package com.android.hao.utils;

import android.util.Log;

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
import okhttp3.Response;

public class OkHttpUtils {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("CarId", 1);
        getDataFromIntent(uri, map, new CallBack() {
            @Override
            public void successful(String string) {
                System.out.println(string);
            }
        });
    }


    private static final String TAG = "Test";
    private static final String ip = "192.168.1.240:8080";
    private static final String temp = "/transportservice/type/jason/action/";
    private static String path = "GetCarSpeed";
    private static String uri = "http://" + ip + temp + path +".do";

    private static void getDataFromIntent(final String uri, final Map<String, Integer> map, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendOkHttpRequest(map, uri, callBack);
            }

            private void sendOkHttpRequest(Map<String, Integer> map, String uri, final CallBack callBack) {
                //0.创建MediaType类型 说明参数是一个字符串
                MediaType Json = MediaType.parse("application/json;charset=utf-8");
                //初始化参数
                Gson gson = new Gson();
                String json = gson.toJson(map);
                //1.创建OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
//               RequestBody requestBody = new FormBody.Builder().add("CarId", 1).build();
                RequestBody body = RequestBody.create(Json, json);
                //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
                final Request request = new Request.Builder().url(uri).post(body).build();
                //4.创建一个call对象,参数就是Request请求对象
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
//                        Log.i(TAG, "onFailure: request failed" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
//                            Log.i(TAG, "onResponse: requsert success");
                            callBack.successful(response.body().string());
                        }
                    }
                });
            }
        }).start();
    }

    interface CallBack{
        void successful(String string);
    }

}
