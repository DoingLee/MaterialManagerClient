package com.material.materialmanager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.LoginPresenter;
import com.material.materialmanager.ui.collect.GetOrderActivity;
import com.material.materialmanager.ui.produce.ScanOrderActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends BaseActivity implements ILoginView {

    private BootstrapEditText etAccountId;
    private BootstrapEditText etPassword;
    private BootstrapButton btnLogin;
    private Spinner spinnerUserType;

    SweetAlertDialog pDialog;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        init();
    }

    private void init() {
        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        etAccountId = $(R.id.et_account_id);
        etPassword = $(R.id.et_password);
        btnLogin = $(R.id.btn_login);
        spinnerUserType = $(R.id.spinner_user_type);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
                pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
                pDialog.setTitleText("正在登录...");
                pDialog.setCancelable(false);
                pDialog.show();

                String userType = spinnerUserType.getSelectedItem().toString();
                loginPresenter.login(etAccountId.getText().toString(), etPassword.getText().toString(), userType);
            }
        });

        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void loginResult(boolean success, String msg) {
        if (success) {
            String userType = spinnerUserType.getSelectedItem().toString();
            if (userType.equals("取料操作员")) { //取料操作员
                pDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, GetOrderActivity.class);
                startActivity(intent);
            }else { //投料操作员
                pDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, ScanOrderActivity.class);
                startActivity(intent);
            }
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(this)
                    .setTitleText("登录失败")
                    .setContentText("密码错误！")
                    .show();
        }
    }

    @Override
    public void loginError(String errorMsg) {
        pDialog.dismiss();
        new SweetAlertDialog(this)
                .setTitleText("登录失败")
                .setContentText("网络出错！")
                .show();
    }

}
