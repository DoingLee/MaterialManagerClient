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
    public static final String hostName = "192.168.3.180";
    public static final String port = ":8080";

    public static final String URL_GET_UNSOLVED_ORDERS = "http://" + hostName + port +  "/order/unsolved/";
    public static final String URL_GET_PRODUCT_PLAN = "http://" + hostName +  port + "/plan/";
    public static final String URL_POST_ORDER_TRACK = "http://" + hostName +  port + "/order_track/";
    public static final String URL_POST_LOGIN_IN = "http://" + hostName +  port + "/login/";

    //全局变量
    public static List<Order> orderList;
    public static List<ProductProcess> productProcesses;

    public static String userName;
}
