package com.material.materialmanager.model;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.HttpUtils;
import com.material.materialmanager.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class ProductProcessModel {

    private static final String TAG = ProductProcessModel.class.getSimpleName() + "======";

    private HttpUtils httpUtils;

    public ProductProcessModel() {
        this.httpUtils = HttpUtils.getInstance();
    }

    /**
     *
     * @return null：服务器没有未处理订单； 非null：一个未处理订单
     * @throws IOException
     */
    public Order getUnsolvedOrder() throws IOException {
        String result = httpUtils.synGet(Constants.URL_GET_UNSOLVED_ORDER);
        try {
            return parseOrder(result);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i(TAG + "JSON解析出错！");
            return null;
        }
    }

    private Order parseOrder(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        boolean success = jsonObject.getBoolean("success");
        if (success) {
            JSONObject data = jsonObject.getJSONObject("data");
            Order order = new Order();
            order.setProductName(data.getString("productName"));
            order.setCount(data.getInt("count"));
            order.setCount(data.getInt("count"));
            order.setOrderId(data.getInt("orderId"));
            return order;
        }else {
            return null;
        }
    }

    /**
     *
     * @param productName
     * @return null：服务器没有该产品的流程信息； 非null：产品流程信息
     * @throws IOException
     */
    public List<ProductProcess> getProductProcess(String productName) throws IOException {
        String result = httpUtils.synGet(Constants.URL_GET_PRODUCT_PLAN + productName + "/");
        try {
            return parseProductProcess(result);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i(TAG + "JSON解析出错！");
            return null;
        }
    }

    private List<ProductProcess> parseProductProcess(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        boolean success = jsonObject.getBoolean("success");
        if (success) {
            List<ProductProcess> productProcesses = new ArrayList<>();
            JSONArray datas = jsonObject.getJSONArray("data");
            for (int i = 0; i < datas.length(); i++) {
                JSONObject data = datas.getJSONObject(i);
                ProductProcess productProcess = new ProductProcess();
                productProcess.setProductName(data.getString("productName"));
                productProcess.setProcessOrder(data.getInt("processOrder"));
                productProcess.setMaterialName(data.getString("materialName"));
                productProcess.setBlenderName(data.getString("blenderName"));
                productProcess.setWeight(data.getInt("weight"));
                productProcess.setLocation(data.getString("location"));
                productProcesses.add(productProcess);
            }
            return productProcesses;
        }else {
            return null;
        }
    }

}
