package com.material.materialmanager.ui.collect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderPresenter;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.IOrderView;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GetOrderActivity extends BaseActivity implements IOrderView {

    private Toolbar toolbar;
    private BootstrapButton btnGetOrder;

    private OrderPresenter orderPresenter;

    private SweetAlertDialog pDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_order);

        initToolBar();
        init();
    }

    private void init() {
        orderPresenter = new OrderPresenter(this);

        btnGetOrder = $(R.id.btn_get_order);
        btnGetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
                pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
                pDialog.setTitleText("正在获取订单...");
                pDialog.setCancelable(false);
                pDialog.show();

                orderPresenter.getUnsolvedOrder();
            }
        });

        pDialog = new SweetAlertDialog(GetOrderActivity.this);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle("获取订单");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void orderResult(boolean hasUnsolvedOrder, List<Order> orderList) {
        pDialog.dismiss();
        if (hasUnsolvedOrder && orderList.size() > 0) {
            Constants.orderList = orderList;
            Intent intent = new Intent(GetOrderActivity.this, OrderListActivity.class);
            startActivity(intent);
        }else {
            new SweetAlertDialog(this)
                    .setTitleText("当前没有未处理的订单")
                    .show();
        }
    }

    @Override
    public void orderError(String errorMsg) {
        pDialog.dismiss();
        new SweetAlertDialog(this)
                .setTitleText("网络出错！")
                .show();
    }

}
