package com.material.materialmanager.presenter;

import android.text.SpannableString;
import android.util.Log;

import com.material.materialmanager.model.LoginModel;
import com.material.materialmanager.ui.ILoginView;
import com.material.materialmanager.utils.LogUtils;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Doing on 2016/12/24 0024.
 */
public class LoginPresenter {

    private ILoginView loginView;
    private LoginModel loginModel;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModel();
    }

    /**
     *
     * @param accountId
     * @param password
     */
    public void login(final String accountId, final String password)  {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String userName = loginModel.login(accountId, password);
                    subscriber.onNext(userName);
                } catch (IOException e) {
                    LogUtils.i(e.getMessage());
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(e.getMessage());
                        loginView.loginError("网络出错！");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null) {
                            loginView.loginResult(true, s);
                        }else {
                            loginView.loginResult(false, "密码错误！");
                        }
                    }
                });

    }


}
