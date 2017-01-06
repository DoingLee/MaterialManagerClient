package com.material.materialmanager.presenter;

import com.material.materialmanager.model.HangUpOrderModel;
import com.material.materialmanager.ui.produce.IHangUpOrderResult;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.LogUtils;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Doing on 2017/1/6 0006.
 */
public class HangUpOrderPresenter {

    IHangUpOrderResult hangUpOrderResult;
    HangUpOrderModel hangUpOrderModel;

    public HangUpOrderPresenter(IHangUpOrderResult hangUpOrderResult) {
        this.hangUpOrderResult = hangUpOrderResult;
        hangUpOrderModel = new HangUpOrderModel();
    }

    public void hangUpOrder(final String orderId)  {

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean success = hangUpOrderModel.handUpOrder(orderId);
                    subscriber.onNext(success);
                } catch (IOException e) {
                    LogUtils.i(e.getMessage());
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(e.getMessage());
                        hangUpOrderResult.hangUpError("网络出错！");
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            hangUpOrderResult.hangUpSuccess();
                        }else {
                            hangUpOrderResult.hangUpError("服务器挂单失败！");
                        }
                    }
                });

    }
}
