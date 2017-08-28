package com.yueding.mobilesafe.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yueding on 2017/8/17.
 *
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4000, TimeUnit.MILLISECONDS)  //请求超时设置
                .build();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
