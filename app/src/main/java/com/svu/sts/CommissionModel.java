package com.svu.sts;

public class CommissionModel {
    private int id;
    private final long amount;

    public CommissionModel(int id, long amount) {
        this.id = id;

        this.amount = amount;
    }

    // Getters and setters
    public int getId() { return id; }

    public long getAmount() { return amount; }

    public void setId(int id) { this.id = id; }

}
