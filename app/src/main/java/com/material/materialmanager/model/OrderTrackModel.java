package com.material.materialmanager.model;

import com.material.materialmanager.Bean.OrderTrack;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Doing on 2016/12/26 0026.
 */
public class OrderTrackModel {

    HttpUtils httpUtils;

    public OrderTrackModel() {
        httpUtils = HttpUtils.getInstance();
    }

    public void postOrderTrack(OrderTrack orderTrack) throws IOException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", orderTrack.getUserName());
        formData.put("orderId", orderTrack.getOrderId() + "");
        formData.put("action", orderTrack.getAction());
        httpUtils.synPostForm(Constants.URL_POST_ORDER_TRACK, formData);
    }

    public void postOrderTrack(String orderId, String name, String action) throws IOException {
        Map<String, String> formData = new HashMap<>();
        formData.put("userName", name);
        formData.put("orderId", orderId + "");
        formData.put("action", action);
        httpUtils.synPostForm(Constants.URL_POST_ORDER_TRACK, formData);
    }
}
