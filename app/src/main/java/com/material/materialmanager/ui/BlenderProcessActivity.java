package com.material.materialmanager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderTrackPoster;
import com.material.materialmanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BlenderProcessActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btnScanBlender;
    private Button btnScanMaterial;
    private TextView tvBlenderName;
    private TextView tvMaterialName;

    private OrderTrackPoster orderTrackPoster;

    private List<ProductProcess> productProcesses;
    private int curProcess;
    private int allProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blender_process);
        initToolBar();
        init();

        showProcessMsg();
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

    private void init() {
        orderTrackPoster = new OrderTrackPoster();
        productProcesses = Constants.productProcesses;
        curProcess = 0;
        allProcess = productProcesses.size();

        btnScanBlender = $(R.id.btn_scan_blender);
        btnScanMaterial = $(R.id.btn_scan_material);
        tvBlenderName = $(R.id.tv_blender_name);
        tvMaterialName = $(R.id.tv_material_name);

        btnScanBlender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((BlenderProcessActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnScanMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((BlenderProcessActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void showProcessMsg() {
        ProductProcess productProcess = productProcesses.get(curProcess);
        tvBlenderName.setText(productProcess.getBlenderName());
        tvMaterialName.setText(productProcess.getMaterialName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                handleBlenderQRCodeResult(scanQRCodeResult);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                handleMaterialQRCodeResult(scanQRCodeResult);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleBlenderQRCodeResult(String scanQRCodeResult) {
        if (scanQRCodeResult.equals(productProcesses.get(curProcess).getBlenderName())) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
            sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
            sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
            sweetAlertDialog.setTitleText("混料罐正确！")
                    .setConfirmText("请扫描原料二维码~")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            btnScanBlender.setBackgroundResource(R.drawable.scan_unable);
                            btnScanBlender.setClickable(false);
                        }
                    })
                    .show();
        } else {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
            sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
            sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
            sweetAlertDialog.setTitleText("混料罐错误！")
                    .setConfirmText("请重新扫描混料罐~")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void handleMaterialQRCodeResult(String scanQRCodeResult) {
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
                                showProcessMsg();
                                btnScanBlender.setBackgroundResource(R.drawable.scan);
                                btnScanBlender.setClickable(true);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
                sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
                sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
                sweetAlertDialog.setTitleText("原料正确！")
                        .setConfirmText("完成一个订单！")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
//                                orderTrackPoster.postOrderTrack("完成投料");
//                                orderTrackPoster.postOrderTrack("完成订单");

                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(BlenderProcessActivity.this, GetOrderActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        } else {
            new SweetAlertDialog(this)
                    .setTitleText("原料错误！")
                    .setConfirmText("请重新选择原料~")
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
