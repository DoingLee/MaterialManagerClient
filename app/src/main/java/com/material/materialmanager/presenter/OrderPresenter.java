package com.material.materialmanager.presenter;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.model.ProductProcessModel;
import com.material.materialmanager.ui.IOrderView;
import com.material.materialmanager.utils.LogUtils;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Doing on 2016/12/26 0026.
 */
public class OrderPresenter {

    private IOrderView orderView;
    private ProductProcessModel productProcessModel;

    public OrderPresenter(IOrderView orderView) {
        this.orderView = orderView;
        productProcessModel = new ProductProcessModel();
    }

    public void getUnsolvedOrder() {

        Observable.create(new Observable.OnSubscribe<Order>() {
            @Override
            public void call(Subscriber<? super Order> subscriber) {
                try {
                    Order order = productProcessModel.getUnsolvedOrder();
                    subscriber.onNext(order);
                } catch (IOException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Order>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(e.getMessage());
                        orderView.orderError("网络出错！");
                    }

                    @Override
                    public void onNext(Order order) {
                        if (order != null) {
                            orderView.orderResult(true, order);
                        }else {
                            orderView.orderResult(false, null);
                        }
                    }
                });
    }

}
