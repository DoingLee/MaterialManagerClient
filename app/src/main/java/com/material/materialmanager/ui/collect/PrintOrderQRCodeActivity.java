package com.material.materialmanager.ui.collect;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.R;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.LogUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrintOrderQRCodeActivity extends BaseActivity {

    private BootstrapButton btnPrintOrderQRCode;
    private LinearLayout layoutOrderQRCode;
    private Toolbar toolbar;

    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_order_qrcode);

        initToolBar();
        init();
        loadQRCodeView();
    }

    private void init() {
        btnPrintOrderQRCode = $(R.id.btn_print_order_qr_code);
        layoutOrderQRCode = $(R.id.layout_order_qr_code);

        orderList = Constants.orderList;

        btnPrintOrderQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printOrderQRCode();  //蓝牙控制无线打印机打印订单二维码
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.back_arrow);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("配料流程");
    }

    @Override
    public void onBackPressed() {
        //disable back button
    }

    private void loadQRCodeView() {
        for (Order order : orderList) {
            for (int i = 0; i < order.getCount(); i++) {
                String url = "http://qr.liantu.com/api.php?text=" + order.getOrderId();
                String text = "订单号：" + order.getOrderId();

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 10);
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(layoutParams1);
                Glide.with(this)
                        .load(url)
                        .override(600, 600)
                        .into(imageView);

                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(0, 0, 0, 20);
                TextView textView = new TextView(this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(text);

                layoutOrderQRCode.addView(imageView);
                layoutOrderQRCode.addView(textView);
            }
        }
    }

    private void printOrderQRCode() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
        pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
        pDialog.setTitleText("正在打印订单二维码...");
        pDialog.setCancelable(false);
        pDialog.show();

        //模拟蓝牙控制打印订单二维码("订单号:产品")
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            public void run() {
                //计时结束后do something
                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(PrintOrderQRCodeActivity.this)
                                .setTitleText("订单二维码打印完成")
                                .setConfirmText("开始选择原料")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        Intent intent = new Intent(PrintOrderQRCodeActivity.this, MaterialProcessActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    }
                });

            }

        };
        timer.schedule(task, 5000);  //5秒

    }


}
