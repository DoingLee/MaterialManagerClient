package com.material.materialmanager.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class Order implements Parcelable{
    private String productName;
    private int count;
    private String orderId;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
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

    public Order(Parcel parcel){
        readFromParcel(parcel);
    }

    //读取 还原对象
    public void readFromParcel(Parcel in){
        productName = in.readString();
        count = in.readInt();
        orderId = in.readString();
    }

    public static final Parcelable.Creator CREATOR=new Parcelable.Creator<Order>(){

        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
    /**
     *写入对象，相当于封装
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeInt(count);
        dest.writeString(orderId);
    }
}
