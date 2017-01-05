package com.material.materialmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.material.materialmanager.Bean.Order;
import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.OrderTrackPoster;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.LogUtils;

import java.util.List;

public class OrderListActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tvOrderProductName;
    private BootstrapButton btnCheckMaterial;
    private RecyclerView recyclerViewOrders;
    private FloatingActionButton fabInOrdersView;

    private OrderTrackPoster orderTrackPoster;

    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderList = Constants.orderList;
        orderTrackPoster = new OrderTrackPoster();
        for (Order order : orderList) {
            orderTrackPoster.postOrderTrack(order.getOrderId(), "获取订单");
        }

        initBar();
        init();

    }

    private void init() {
        tvOrderProductName = $(R.id.tv_order_product_name);
        btnCheckMaterial = $(R.id.btn_check_materials);
        recyclerViewOrders = $(R.id.recycler_view_orders);
        fabInOrdersView = $(R.id.fab_in_orders_view);

        tvOrderProductName.setText(orderList.get(0).getProductName());
        btnCheckMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, PlanActivity.class);
                startActivity(intent);
            }
        });
        fabInOrdersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, ScanOrderActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setItemAnimator(new DefaultItemAnimator());
        MyAdapter myAdapter = new MyAdapter(this, orderList);
        recyclerViewOrders.setAdapter(myAdapter);
    }

    private void initBar() {
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
        toolbar.setTitle("合并订单");
    }

    @Override
    public void onBackPressed() {
        //disable back button
    }

    public static class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        private List<Order> orderList;
        private Context mContext;
        public MyAdapter( Context context , List<Order> orderList)
        {
            this.mContext = context;
            this.orderList = orderList;
        }
        ///////////////////////////////////////////布局与控件////////////////////////////////////////////////////////////////
        @Override
        public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType )
        {  //单个lRecyclerView里的布局
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_order, viewGroup, false);
            return new ViewHolder(v);
        }

        public static class ViewHolder  extends RecyclerView.ViewHolder
        {  //布局里的组件
            public TextView tvOrderId;
            public TextView tvCount;
            public View  view;

            public ViewHolder( View v )
            {
                super(v);
                view = v;
                tvOrderId = (TextView) v.findViewById(R.id.tv_order_id);
                tvCount = (TextView) v.findViewById(R.id.tv_count);
            }
        }
        ////////////////////////////////////控件--数据Adapter///////////////////////////////////////////////////////
        @Override
        public void onBindViewHolder( ViewHolder viewHolder, int i )
        {
            Order order = orderList.get(i);
            String countString = order.getCount() + "  " + "份";
            viewHolder.tvCount.setText(countString);
            viewHolder.tvOrderId.setText(order.getOrderId() + "");
        }

        @Override
        public int getItemCount()
        {
            return orderList == null ? 0 : orderList.size();
        }
    }
}
