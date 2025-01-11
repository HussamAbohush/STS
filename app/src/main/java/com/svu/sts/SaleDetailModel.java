package com.svu.sts;
public class SaleDetailModel {
    private int id;
    private int saleId;
    private int regionId;
    private long amount;

    // Constructor
    public SaleDetailModel(int id, int saleId, int regionId, long amount) {
        this.id = id;
        this.saleId = saleId;
        this.regionId = regionId;
        this.amount = amount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSaleId() {
        return saleId;
    }

    public int getRegionId() {
        return regionId;
    }

    public long getAmount() {
        return amount;
    }

    // toString() for easy printing
    @Override
    public String toString() {
        return "SaleDetail{id=" + id + ", saleId=" + saleId + ", regionId=" + regionId + ", amount=" + amount + "}";
    }
}
