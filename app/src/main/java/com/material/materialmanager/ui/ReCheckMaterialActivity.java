package com.material.materialmanager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderTrackPoster;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReCheckMaterialActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btnScan;
    private TextView tvMaterial;
    private TextView tvLocation;

    private List<ProductProcess> productProcesses;
    private OrderTrackPoster orderTrackPoster;

    private int count;

    private int curProcess;
    private int allProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrerial_process);

        initToolBar();
        init();

        productProcesses = Constants.productProcesses;
//        count = Constants.order.getCount();
        curProcess = 0;
        allProcess = productProcesses.size();

        showProcess();
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
        toolbar.setTitle("取料");
    }

    @Override
    public void onBackPressed() {
        //disable back button
    }

    private void init() {
        orderTrackPoster = new OrderTrackPoster();

        btnScan = $(R.id.btn_scan);
        tvMaterial = $(R.id.tv_material);
        tvLocation = $(R.id.tv_location);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((ReCheckMaterialActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void showProcess() {
        ProductProcess productProcess = productProcesses.get(curProcess);
        showMaterialMsg(productProcess.getMaterialName(), productProcess.getWeight(), productProcess.getLocation());
    }

    private void showMaterialMsg(String materialName, int weight, String location) {
        StringBuilder sb = new StringBuilder(materialName);
        sb.append(" ");
        sb.append(weight + "");
        sb.append("g");
        String s = materialName + " " + weight + "g";
        tvMaterial.setText(s);
        tvLocation.setText("位置：" + location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                handleQRCodeResult(scanQRCodeResult);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleQRCodeResult(String scanQRCodeResult) {
        if (scanQRCodeResult.equals(productProcesses.get(curProcess).getMaterialName())) {
            curProcess = curProcess + 1;
            if (curProcess < allProcess) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
                sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
                sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
                sweetAlertDialog.setTitleText("原料正确！")
                        .setConfirmText("进入下一步~")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                showProcess();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
                sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
                sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
                sweetAlertDialog.setTitleText("原料正确！")
                        .setConfirmText("开始投料操作~")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
//                                orderTrackPoster.postOrderTrack("完成取料");

                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(ReCheckMaterialActivity.this, BlenderProcessActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        } else {
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
    }

}