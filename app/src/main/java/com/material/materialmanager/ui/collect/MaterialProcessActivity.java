package com.material.materialmanager.ui.collect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.ScanBarCodeActivity;
import com.material.materialmanager.utils.Constants;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MaterialProcessActivity extends BaseActivity {

    private Button btnScan;
    private TextView tvOrderIdTitle;
    private TextView tvMaterial;
    private TextView tvLocation;
    private Toolbar toolbar;

    private SweetAlertDialog pDialog ;

    private List<ProductProcess> productProcessList;
    private List<Order> orderList;
    private int curOrderNum;
    private int totalOrder; //订单数
    private int curCountNum;
    private int totalCount; //当前订单产品数
    private int curProcess;
    private int totalProcess; //步骤总数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_process);

        initToolBar();
        init();
    }

    private void init() {
        btnScan = $(R.id.btn_scan);
        tvMaterial = $(R.id.tv_material);
        tvLocation = $(R.id.tv_location);
        tvOrderIdTitle = $(R.id.tv_order_id_title);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((MaterialProcessActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        productProcessList = Constants.productProcesses;
        orderList = Constants.orderList;
        curOrderNum = 0;
        curCountNum = 0;
        curProcess = 0;
        totalOrder = orderList.size();
        totalCount = orderList.get(0).getCount();
        totalProcess = productProcessList.size();

        showCurrentOrderId();
        showCurrentProcessMaterial();

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
        toolbar.setTitle("投料");
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
                handleMaterialQRCodeResult(scanQRCodeResult);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleMaterialQRCodeResult(String scanQRCodeResult) {
        if (scanQRCodeResult.equals(productProcessList.get(curProcess).getMaterialName())) {
            //原料正确
            curCountNum++;
            if (curCountNum < totalCount) {
                //当前原料、当前订单
                showProcessSuccess();
            } else {
                //当前原料、下一个订单
                curOrderNum++;
                curCountNum = 0;
                if (curOrderNum < totalOrder) {
                    //当前原料、当前订单
                    totalCount = orderList.get(curOrderNum).getCount();
                    showProcessSuccess();
                } else {
                    //下一个原料、下一个原料的第一个订单
                    curProcess++;
                    curOrderNum = 0;
                    curCountNum = 0;
                    totalCount = orderList.get(curOrderNum).getCount();
                    if (curProcess < totalProcess) {
                        //当前原料、当前订单
                        showProcessSuccess();
                    } else {
                        //取料完毕
                        showProcessFinish();
                    }
                }
            }

        } else {
            //原料错误
            showProcessError();
        }
    }

    private void showProcessSuccess() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
        sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
        sweetAlertDialog.setTitleText("原料正确！")
                .setConfirmText("开始称量~")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
                        pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
                        pDialog.setTitleText("正在等待称重通过...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        handleWeighting();
                    }
                })
                .show();
    }

    private void showProcessFinish() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
        sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
        sweetAlertDialog.setTitleText("原料正确！")
                .setConfirmText("取料操作完成~")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        orderTrackPoster.postOrderTrack("完成取料");
                        sDialog.dismissWithAnimation();

                        Intent intent = new Intent(MaterialProcessActivity.this, GetOrderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    private void showProcessError() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
        sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
        sweetAlertDialog.setTitleText("原料错误！")
                .setConfirmText("请重新取料~")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void handleWeighting() {

        //模拟蓝牙发送称重信息（需要称重的质量）和等待称重通过
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            public void run() {

                //计时结束后do something
                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(MaterialProcessActivity.this)
                                .setTitleText("称重通过！")
                                .setConfirmText("开始打印称重二维码~")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        pDialog.setTitleText("正在等待打印称重二维码...");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        handlePrintMaterialQRCode();
                                    }
                                })
                                .show();
                    }
                });
            }

        };
        timer.schedule(task, 1000);  //5秒

    }

    private void handlePrintMaterialQRCode() {

        //模拟蓝牙发送称重二维码信息图片（"订单号:物料名称:物料标准质量"）到无线打印机
        String qrCodeMsg = orderList.get(curOrderNum).getOrderId() + ":" +
                productProcessList.get(curProcess).getMaterialName() + ":" +
                productProcessList.get(curProcess).getWeight();
        String url = "http://qr.liantu.com/api.php?text=" + qrCodeMsg;
        //1、使用url获取二维码图片
        //2、发送该二维码图片到无线打印机打印

        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            public void run() {

                //计时结束后do something
                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(MaterialProcessActivity.this)
                                .setTitleText("原料二维码打印完成!")
                                .setConfirmText("进入下一个取料环节~")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        //显示下一个取料信息
                                        showCurrentProcessMaterial();
                                        showCurrentOrderId();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                });
            }

        };
        timer.schedule(task, 1000);  //5秒

    }

    private void showCurrentProcessMaterial() {
        ProductProcess productProcess = productProcessList.get(curProcess);
        String materialName = productProcess.getMaterialName();
        int weight = productProcess.getWeight();
        String location = productProcess.getLocation();

        StringBuilder sb = new StringBuilder(materialName);
        sb.append(" ");
        sb.append(weight + "");
        sb.append("g");
        String s = materialName + " " + weight + "g";
        tvMaterial.setText(s);
        tvLocation.setText("位置：" + location);
    }

    private void showCurrentOrderId() {
        int currentOrderId = orderList.get(curOrderNum).getOrderId();
        tvOrderIdTitle.setText("正在处理订单号：" + currentOrderId);
    }

}
