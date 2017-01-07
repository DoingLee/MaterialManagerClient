package com.material.materialmanager.utils;

import android.util.Log;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class Constants {
//    public static final String hostName = "172.18.150.122";
    public static final String hostName = "172.19.92.124";
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
}
