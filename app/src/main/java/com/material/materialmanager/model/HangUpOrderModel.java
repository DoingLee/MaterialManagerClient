package com.material.materialmanager.model;

import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.HttpUtils;
import com.material.materialmanager.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Doing on 2017/1/6 0006.
 */
public class HangUpOrderModel {

    private HttpUtils httpUtils;

    public HangUpOrderModel() {
        httpUtils = HttpUtils.getInstance();
    }

    public boolean handUpOrder(String orderId) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("status", "unsolved");
        String url = Constants.URL_HOST + "/order/" + orderId + "/status/";
        LogUtils.i(url);
        String result = httpUtils.synPostForm(url, map);
        LogUtils.i(result);
        try {
            boolean success = parseResult(result);
            return success;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean parseResult(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        boolean success = jsonObject.getBoolean("success");
        return success;
    }

}
