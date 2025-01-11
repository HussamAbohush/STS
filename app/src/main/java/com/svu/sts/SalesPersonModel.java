package com.svu.sts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class SalesPersonModel {
    private  int id;
    private String name;
    private String phoneNumber;
    private String mainLocation;
    private byte[] Image;

    public SalesPersonModel(int id, String name, String phoneNumber, byte[] image, String mainLocation){
        this.id =id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.Image = image;
        this.mainLocation = mainLocation;
    }
    public SalesPersonModel(Bundle b){
        this.id =b.getInt("id");
        this.name = b.getString("name");
        this.phoneNumber = b.getString("phoneNumber");
        this.Image = b.getByteArray("image");
        this.mainLocation = b.getString("region");
    }
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeByteArray(Image, 0, Image.length);
    }
    public String getMainLocation(){
        return mainLocation;
    }

    public Bundle getAsBundle(){
        Bundle b = new Bundle();
        b.putInt("id",id);
        b.putString("name",name);
        b.putString("phoneNumber",phoneNumber);
        b.putString("region", mainLocation);
        b.putByteArray("image", Image);
        return b;
    }


    public void setName(String name) {
        this.name = name;
    }

}
