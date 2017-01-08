package com.material.materialmanager.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class HttpUtils {

    private static String TAG = HttpUtils.class.getSimpleName() + "========";

    private volatile static  HttpUtils httpUtils;

    private HttpUtils(){
    }

    public static HttpUtils getInstance(){
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    //支持cookie。cookie path：host
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new LinkedList<Cookie>();
        }
    }).build();

    public String synGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()){
            throw new IOException("网络请求失败：" + response);
        }
        return response.body().string();
    }

    public String synPostForm(String url, Map<String, String> formDatas) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> formData : formDatas.entrySet()) {
            formBuilder.add(formData.getKey(), formData.getValue());
        }
        RequestBody requestBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("网络请求失败：" + response);
        return response.body().string();
    }

    public String synPutForm(String url, Map<String, String> formDatas) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> formData : formDatas.entrySet()) {
            formBuilder.add(formData.getKey(), formData.getValue());
        }
        RequestBody requestBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("网络请求失败：" + response);
        return response.body().string();
    }

    public String synGetWithHeader(String url, HashMap<String, String> headers) throws IOException {
        LogUtils.i(url);

        Request.Builder builder = new Request.Builder();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        builder.url(url);
        Request request = builder.build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("网络请求失败：" + response);
        }
        return response.body().string();
    }

}
