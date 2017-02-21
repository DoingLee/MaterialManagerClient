package com.material.materialmanager.presenter;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.Bean.WeightResult;
import com.material.materialmanager.model.WeightSocket;
import com.material.materialmanager.ui.IWeightView;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Doing on 2017/2/21 0021.
 */
public class WeightPresenter {

    private WeightSocket weightSocket;
    private IWeightView weightView;

    public WeightPresenter(int weight, String weighterName, IWeightView weightView) {
        this.weightView = weightView;

        weightSocket = new WeightSocket(weight, weighterName);
    }

    public void weight() {
        Observable.create(new Observable.OnSubscribe<WeightResult>() {
            @Override
            public void call(Subscriber<? super WeightResult> subscriber) {
                WeightResult weightResult = weightSocket.startWeight();
                subscriber.onNext(weightResult);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeightResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WeightResult weightResult) {
                        weightView.weightResult(weightResult.isSuccess(), weightResult.getMsg());
                    }
                });
    }
}
