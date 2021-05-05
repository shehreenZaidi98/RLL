package com.wmsweb.model;

public class PurchaseModel {
    private  String sku;
    private int qty;


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public PurchaseModel(String sku, int qty) {
        this.sku = sku;
        this.qty = qty;
    }
}
