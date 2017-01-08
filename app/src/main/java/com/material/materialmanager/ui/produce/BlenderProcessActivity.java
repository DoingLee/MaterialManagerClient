package com.material.materialmanager.ui.produce;

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
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.ScanBarCodeActivity;
import com.material.materialmanager.ui.collect.GetOrderActivity;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BlenderProcessActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btnScanBlender;
    private Button btnScanMaterial;
    private TextView tvBlenderName;
    private TextView tvProduceOrderId;
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
        orderTrackPoster.postOrderTrack(Constants.orderId, "开始投料");

        productProcesses = Constants.productProcesses;
        curProcess = 0;
        allProcess = productProcesses.size();

        tvProduceOrderId = $(R.id.tv_produce_order_id);
        btnScanBlender = $(R.id.btn_scan_blender);
        btnScanMaterial = $(R.id.btn_scan_material);
        tvBlenderName = $(R.id.tv_blender_name);
        tvMaterialName = $(R.id.tv_material_name);

        tvProduceOrderId.setText("配置订单：" + Constants.orderId);

        btnScanBlender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String action = "投料开始:" +
                        Constants.productName + ":" +
                        productProcesses.get(curProcess).getMaterialName();
                orderTrackPoster.postOrderTrack(Constants.orderId, action);

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
        tvMaterialName.setText(productProcess.getMaterialName() + productProcess.getWeight() + "g");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String blenderName = data.getStringExtra("result");
                handleBlenderQRCodeResult(blenderName);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                String[] s = scanQRCodeResult.split(":");

                if (s.length == 3) {
                    handleMaterialQRCodeResult(s[0], s[1], s[2]);

                }else{
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
                    sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
                    sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
                    sweetAlertDialog.setTitleText("请扫描正确的物料二维码！")
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

    private void handleBlenderQRCodeResult(String blenderName) {
        if (blenderName.equals(productProcesses.get(curProcess).getBlenderName())) {
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

    private void handleMaterialQRCodeResult(String orderId, String materialName, String weight) {
        //原料名称和标准质量均一致才能投料
        ProductProcess productProcess = productProcesses.get(curProcess);
        if (materialName.equals(productProcess.getMaterialName()) &&
                weight.equals(productProcess.getWeight() + "") &&
                orderId.equals(Constants.orderId)) {
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
                                String action = "投料完成:" +
                                        Constants.productName + ":" +
                                        productProcesses.get(curProcess - 1).getMaterialName();
                                orderTrackPoster.postOrderTrack(Constants.orderId, action);

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

                                String action = "投料完成:" +
                                        Constants.productName + ":" +
                                        productProcesses.get(curProcess - 1).getMaterialName();
                                orderTrackPoster.postOrderTrack(Constants.orderId, action);

                                orderTrackPoster.postOrderTrack(Constants.orderId, "完成投料");

                                orderTrackPoster.postOrderTrack(Constants.orderId, "完成订单");

                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(BlenderProcessActivity.this, ScanOrderActivity.class);
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
