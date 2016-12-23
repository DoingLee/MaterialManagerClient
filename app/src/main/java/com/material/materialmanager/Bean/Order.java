package com.material.materialmanager.Bean;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class Order {
    private String productName;
    private int count;
    private int orderId;

    public Order() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "productName='" + productName + '\'' +
                ", count=" + count +
                ", orderId=" + orderId +
                '}';
    }
}
