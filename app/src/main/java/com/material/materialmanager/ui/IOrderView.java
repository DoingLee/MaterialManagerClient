package com.material.materialmanager.ui;

import com.material.materialmanager.Bean.Order;

import java.util.List;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public interface IOrderView {

    /**
     *
     * @param hasUnsolvedOrder true:order非null， false:order为null
     * @param order null：无unsolved订单
     */
    void orderResult(boolean hasUnsolvedOrder, Order order);

    void orderError(String errorMsg);

}
