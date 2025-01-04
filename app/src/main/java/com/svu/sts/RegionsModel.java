package com.svu.sts;

public class RegionsModel {
    private final int id;
    private final String regionName;

    public RegionsModel(int id, String regionName){
        this.id = id;
        this.regionName = regionName;
    }
    public int getId() {return id;}
    public String getName(){return regionName;}

}
