package com.material.materialmanager.ui.produce;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderPresenter;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.IOrderView;
import com.material.materialmanager.ui.ScanBarCodeActivity;
import com.material.materialmanager.ui.collect.OrderListActivity;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ScanOrderActivity extends BaseActivity  {

    private Toolbar toolbar;
    private Button btnScanOrderId;
    private SweetAlertDialog pDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_order);

        initToolBar();
        init();

    }

    private void init() {
        btnScanOrderId = $(R.id.btn_scan_order_id);

        btnScanOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((ScanOrderActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void initToolBar() {
        pDialog = new SweetAlertDialog(ScanOrderActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("复核");
    }

    @Override
    public void onBackPressed() {
        //disable back button
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                String[] s = scanQRCodeResult.split(":");
                if (s.length == 2) {
                    Constants.orderId = s[0];
                    Constants.productName = s[1];

                    Intent intent = new Intent(ScanOrderActivity.this, PlanActivityForProduce.class);
                    startActivity(intent);
                }else{
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
                    sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
                    sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
                    sweetAlertDialog.setTitleText("请扫描正确的订单二维码！")
                            .setConfirmText("重新扫码~")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
