package com.material.materialmanager.Bean;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class ProductProcess {
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
}
