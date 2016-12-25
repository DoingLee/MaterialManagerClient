package com.material.materialmanager.presenter;

import com.material.materialmanager.Bean.ProductProcess;
import com.material.materialmanager.ui.IPlanView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class ProductProcessPresenter {

    private IPlanView planView;

    public ProductProcessPresenter(IPlanView planView) {
        this.planView = planView;
    }

    public void getPlan() {
        List<ProductProcess> productProcesses = new ArrayList<>();
        ProductProcess productProcess = new ProductProcess();
        productProcess.setProcessOrder(1);
        productProcess.setProductName("产品名111");
        productProcess.setWeight(100);
        productProcess.setBlenderName("混料罐111");
        productProcess.setMaterialName("原料111");
        productProcesses.add(productProcess);

        planView.showPlan(productProcesses);
    }
}
