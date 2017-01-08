package com.material.materialmanager.utils;

import android.util.Log;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class Constants {

    private static final String TAG = Constants.class.getSimpleName() + " ============= ";

    public static final String hostName = getServerIp();
    public static final String port = ":8080";

    public static final String URL_GET_UNSOLVED_ORDERS = "http://" + hostName + port +  "/order/unsolved/";
    public static final String URL_GET_PRODUCT_PLAN = "http://" + hostName +  port + "/plan/";
    public static final String URL_POST_ORDER_TRACK = "http://" + hostName +  port + "/order_track/";
    public static final String URL_POST_LOGIN_IN = "http://" + hostName +  port + "/login/";
    public static final String URL_HOST = "http://" + hostName +  port;

    //全局变量
    public static List<Order> orderList;
    public static List<ProductProcess> productProcesses;

    public static String orderId; //投料操作全局订单号
    public static String productName; //投料操作全局产品名

    public static String userName;

    private static String getServerIp() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        String ip;
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("X-LC-Id", "2XiE4JVFsWR5dcWs5wHIp5Xb-gzGzoHsz");
            headers.put("X-LC-Key", "Gh5otQyvdYEjeMAYKlobiYmV");
            String result = httpUtils.synGetWithHeader(
                    "https://leancloud.cn:443/1.1/classes/material_manager/5871a218da2f60494e5340c3", headers);
            ip = parseIp(result);
            return ip;
        } catch (IOException e) {
            LogUtils.e(TAG + "获取服务器IP网络出错！");
            e.printStackTrace();
        }
        return null;
    }

    private static String parseIp(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String ip = jsonObject.getString("server_ip");
            return ip;
        } catch (JSONException e) {
            LogUtils.e(TAG + "解析服务器IP网络出错！");
            e.printStackTrace();
        }
        return null;
    }
}
