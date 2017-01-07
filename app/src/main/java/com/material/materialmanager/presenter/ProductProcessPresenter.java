package com.material.materialmanager.presenter;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.model.ProductProcessModel;
import com.material.materialmanager.ui.IPlanView;
import com.material.materialmanager.utils.LogUtils;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class ProductProcessPresenter {

    private IPlanView planView;
    private ProductProcessModel productProcessModel;


    public ProductProcessPresenter(IPlanView planView) {
        this.planView = planView;
        productProcessModel = new ProductProcessModel();
    }

    public void getPlan(final String productName) {

        Observable.create(new Observable.OnSubscribe<List<ProductProcess>>() {
            @Override
            public void call(Subscriber<? super List<ProductProcess>> subscriber) {
                try {
                    List<ProductProcess> productProcess = productProcessModel.getProductProcess(productName);
                    subscriber.onNext(productProcess);
                } catch (IOException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProductProcess>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        planView.planError("网络出错！");
                    }

                    @Override
                    public void onNext(List<ProductProcess> productProcesses) {
                        planView.planResult(productProcesses);
                    }
                });
    }

}
