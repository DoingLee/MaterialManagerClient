package com.material.materialmanager.utils;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class Constants {
    public static final String hostName = "172.18.150.122";
    public static final String port = ":8080";

    public static final String URL_GET_UNSOLVED_ORDER = "http://" + hostName + port +  "/order/1/unsolved/";
    public static final String URL_GET_PRODUCT_PLAN = "http://" + hostName +  port + "/plan/";
    public static final String URL_POST_ORDER_TRACK = "http://" + hostName +  port + "/order_track/";
    public static final String URL_POST_LOGIN_IN = "http://" + hostName +  port + "/login/";


}
