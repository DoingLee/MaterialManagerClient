package com.material.materialmanager.ui;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public interface ILoginView {

    void loginResult(boolean success, String msg);

    void loginError(String errorMsg);
}
