package com.material.materialmanager.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.R;
import com.material.materialmanager.presenter.ProductProcessPresenter;

import java.util.List;

public class PlanActivity extends AppCompatActivity implements IPlanView {

    private Toolbar toolbar;

    private ProductProcessPresenter productProcessPresenter;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        init();
        initToolBar();
        productProcessPresenter.getPlan();
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
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_process);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        productProcessPresenter = new ProductProcessPresenter(this);
    }

    @Override
    public void showPlan(List<ProductProcess> productProcesses) {
        MyAdapter myAdapter = new MyAdapter(this, productProcesses);
        mRecyclerView.setAdapter(myAdapter);
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
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
