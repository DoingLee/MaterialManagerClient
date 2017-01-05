package com.material.materialmanager.presenter;

import com.material.materialmanager.model.OrderTrackModel;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Doing on 2016/12/26 0026.
 */
public class OrderTrackPoster {

    OrderTrackModel orderTrackModel;

    public OrderTrackPoster() {
        orderTrackModel = new OrderTrackModel();
    }

    public void postOrderTrack(final int orderId, final String action) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    orderTrackModel.postOrderTrack(orderId, Constants.userName, action);
                } catch (IOException e) {
                    LogUtils.i("OrderTrackPoster ====== 提交order track网络出错！");
                    e.printStackTrace();
                }
            }
        });
    }
}
