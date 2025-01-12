package com.svu.sts;

public class CommissionModel {
    private int id;
    private int salespersonId;
    private int year;
    private int month;
    private long amount;

    public CommissionModel(int id, int salespersonId, int year, int month, long amount) {
        this.id = id;
        this.salespersonId = salespersonId;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() { return id; }

    public long getAmount() { return amount; }

    public void setId(int id) { this.id = id; }

}
