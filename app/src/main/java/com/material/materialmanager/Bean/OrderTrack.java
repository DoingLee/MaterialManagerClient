package com.material.materialmanager.Bean;

/**
 * Created by Doing on 2016/12/26 0026.
 */
public class OrderTrack {
    private String userName;
    private int orderId;
    private String action;

    public OrderTrack() {
    }

    public OrderTrack(String userName, int orderId, String action) {
        this.userName = userName;
        this.orderId = orderId;
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "OrderTrack{" +
                "userName='" + userName + '\'' +
                ", orderId=" + orderId +
                ", action='" + action + '\'' +
                '}';
    }
}
