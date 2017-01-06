package com.material.materialmanager.ui.collect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderTrackPoster;
import com.material.materialmanager.presenter.ProductProcessPresenter;
import com.material.materialmanager.ui.BaseActivity;
import com.material.materialmanager.ui.IPlanView;
import com.material.materialmanager.utils.Constants;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PlanActivityForCollect extends BaseActivity implements IPlanView {

    private Toolbar toolbar;
    private TextView tvProductTitle;
    private SweetAlertDialog pDialog ;
    private FloatingActionButton fab;

    private ProductProcessPresenter productProcessPresenter;
    private OrderTrackPoster orderTrackPoster;

    private List<Order> orderList;
    private List<ProductProcess> productProcesses;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_for_collect);

        init();
        initToolBar();

//        orderTrackPoster.postOrderTrack("获取订单");

        orderList = Constants.orderList;
        Order order = orderList.get(0);
        //产品名称标题
        StringBuilder productTitle = new StringBuilder(order.getProductName());
        tvProductTitle.setText(productTitle.toString());
        //产品流程
        productProcessPresenter.getPlan(order.getProductName());
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#5abfdb"));
        pDialog.getProgressHelper().setRimColor(Color.parseColor("#0677d4"));
        pDialog.setTitleText("正在获取产品流程...");
        pDialog.setCancelable(false);
        pDialog.show();

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
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("配料流程");
    }

    @Override
    public void onBackPressed() {
        //disable back button
    }

    private void init() {
        orderTrackPoster = new OrderTrackPoster();
        tvProductTitle = $(R.id.tv_product_title);
        pDialog = new SweetAlertDialog(PlanActivityForCollect.this);
        fab = $(R.id.fab);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_process);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        productProcessPresenter = new ProductProcessPresenter(this);
    }

    @Override
    public void planResult(final List<ProductProcess> productProcesses) {
        pDialog.dismiss();
        MyAdapter myAdapter = new MyAdapter(this, productProcesses);
        mRecyclerView.setAdapter(myAdapter);
        Constants.productProcesses = productProcesses;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivityForCollect.this, PrintOrderQRCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void planError(String errorMsg) {
        pDialog.dismiss();
        new SweetAlertDialog(this)
                .setTitleText("网络出错！")
                .show();
    }


    public static class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        private List<ProductProcess> productProcesses;
        private Context mContext;
        public MyAdapter( Context context , List<ProductProcess> productProcesses)
        {
            this.mContext = context;
            this.productProcesses = productProcesses;
        }
        ///////////////////////////////////////////布局与控件////////////////////////////////////////////////////////////////
        @Override
        public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType )
        {  //单个lRecyclerView里的布局
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_material, viewGroup, false);
            return new ViewHolder(v);
        }

        public static class ViewHolder  extends RecyclerView.ViewHolder
        {  //布局里的组件
            public TextView tvMaterial;
            public TextView tvBlender;
            public View  view;

            public ViewHolder( View v )
            {
                super(v);
                view = v;
                tvBlender = (TextView) v.findViewById(R.id.tv_blender);
                tvMaterial = (TextView) v.findViewById(R.id.tv_material);
            }
        }
        ////////////////////////////////////控件--数据Adapter///////////////////////////////////////////////////////
        @Override
        public void onBindViewHolder( ViewHolder viewHolder, int i )
        {
            ProductProcess p = productProcesses.get(i);
            String materialMsg = p.getMaterialName() + "  " + p.getWeight() + "g";
            viewHolder.tvMaterial.setText(materialMsg);
            viewHolder.tvBlender.setText(p.getBlenderName());
        }

        @Override
        public int getItemCount()
        {
            return productProcesses == null ? 0 : productProcesses.size();
        }
    }
}
