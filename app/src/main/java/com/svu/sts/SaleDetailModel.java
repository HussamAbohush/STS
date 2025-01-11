package com.svu.sts;
public class SaleDetailModel {
    private final int id;
    private final int regionId;
    private final long amount;

    // Constructor
    public SaleDetailModel(int id, int regionId, long amount) {
        this.id = id;
        this.regionId = regionId;
        this.amount = amount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRegionId() {
        return regionId;
    }

    public long getAmount() {
        return amount;
    }
}
