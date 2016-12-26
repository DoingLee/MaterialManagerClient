package com.material.materialmanager.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class ProductProcess implements Parcelable{
    private String productName;
    private int processOrder;
    private String materialName;
    private int weight;
    private String blenderName;

    public ProductProcess() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(int processOrder) {
        this.processOrder = processOrder;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBlenderName() {
        return blenderName;
    }

    public void setBlenderName(String blenderName) {
        this.blenderName = blenderName;
    }

    @Override
    public String toString() {
        return "ProductProcess{" +
                "productName='" + productName + '\'' +
                ", processOrder=" + processOrder +
                ", materialName='" + materialName + '\'' +
                ", weight=" + weight +
                ", blenderName='" + blenderName + '\'' +
                '}';
    }

    public ProductProcess(Parcel parcel){
        readFromParcel(parcel);
    }

    //读取 还原对象
    public void readFromParcel(Parcel in){
        productName = in.readString();
        processOrder = in.readInt();
        materialName = in.readString();
        weight = in.readInt();
        blenderName = in.readString();
    }

    public static final Parcelable.Creator CREATOR=new Parcelable.Creator<ProductProcess>(){

        @Override
        public ProductProcess createFromParcel(Parcel source) {
            return new ProductProcess(source);
        }

        @Override
        public ProductProcess[] newArray(int size) {
            return new ProductProcess[size];
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
        dest.writeInt(processOrder);
        dest.writeInt(weight);
        dest.writeString(materialName);
        dest.writeString(blenderName);
    }
}
