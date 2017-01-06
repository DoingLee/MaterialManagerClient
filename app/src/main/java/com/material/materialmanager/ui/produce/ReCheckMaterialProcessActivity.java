package com.material.materialmanager.ui.produce;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.model.HangUpOrderModel;
import com.material.materialmanager.presenter.HangUpOrderPresenter;
import com.material.materialmanager.presenter.OrderTrackPoster;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.ScanBarCodeActivity;
import com.material.materialmanager.ui.collect.GetOrderActivity;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReCheckMaterialProcessActivity extends BaseActivity implements IHangUpOrderResult {

    private Toolbar toolbar;
    private Button btnScan;
    private TextView tvRecheckOrderId;
    private TextView tvRecheckProductName;
    private TextView tvMaterial;
    private BootstrapButton btnDisableOrder;
    private SweetAlertDialog pDialog ;

    private List<ProductProcess> productProcesses;
    private HangUpOrderPresenter hangUpOrderPresenter;
    private OrderTrackPoster orderTrackPoster;

    private int curProcess;
    private int totalProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrerial_process);

        initToolBar();
        init();
    }

    private void init() {
        orderTrackPoster = new OrderTrackPoster();
        hangUpOrderPresenter = new HangUpOrderPresenter(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        btnScan = $(R.id.btn_scan);
        tvRecheckOrderId = $(R.id.tv_recheck_order_id);
        tvRecheckProductName = $(R.id.tv_recheck_product_name);
        tvMaterial = $(R.id.tv_material);
        btnDisableOrder = $(R.id.btn_disable_order);

        tvRecheckOrderId.setText("复核订单：" + Constants.orderId);
        tvRecheckProductName.setText("产品名称：" + Constants.productName);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((ReCheckMaterialProcessActivity.this), ScanBarCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnDisableOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangUpOrderWarning();
            }
        });

        productProcesses = Constants.productProcesses;
        curProcess = 0;
        totalProcess = productProcesses.size();
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
        toolbar.setTitle("复核");
    }

    @Override
    public void onBackPressed() {
        //disable back button
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                //user cancel
            } else {
                String scanQRCodeResult = data.getStringExtra("result");
                String[] s = scanQRCodeResult.split(":");
                if (s.length == 3) {
                    handleQRCodeResult(s[0], s[1], s[2]);
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

    private void handleQRCodeResult(String orderId, String materialName, String weight) {
        ProductProcess productProcess = productProcesses.get(curProcess);
        if (materialName.equals(productProcess.getMaterialName()) &&
                weight.equals(productProcess.getWeight() + "") &&
                orderId.equals(Constants.orderId)) {
            curProcess = curProcess + 1;
            if (curProcess < totalProcess) {
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
                                Intent intent = new Intent(ReCheckMaterialProcessActivity.this, BlenderProcessActivity.class);
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
                    .setConfirmText("请重新选料~")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    //挂单处理提醒
    private void hangUpOrderWarning() {
        new SweetAlertDialog(ReCheckMaterialProcessActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提醒")
                .setContentText("是否确定挂单退料？")
                .setCancelText("否")
                .setConfirmText("是")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        hangUpOrder();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    //挂单处理
    private void hangUpOrder() {
        hangUpOrderPresenter.hangUpOrder(Constants.orderId);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
        pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
        pDialog.setTitleText("正在挂单...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void hangUpSuccess() {
        pDialog.dismissWithAnimation();

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
        sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
        sweetAlertDialog.setTitleText("挂单成功，请退料！")
                .setConfirmText("复核下一个订单~")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        orderTrackPoster.postOrderTrack("完成取料");
                        sDialog.dismissWithAnimation();

                        Intent intent = new Intent(ReCheckMaterialProcessActivity.this, ScanOrderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void hangUpError(String errorMsg) {
        pDialog.dismissWithAnimation();

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setCancelable(false); //prevent dialog box from getting dismissed on back key pressed
        sweetAlertDialog.setCanceledOnTouchOutside(false); // prevent dialog box from getting dismissed on outside
        sweetAlertDialog.setTitleText(errorMsg)
                .setConfirmText("请稍后重新挂单~")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
