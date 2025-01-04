package com.svu.sts;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SalesPersonRVHolder extends  RecyclerView.ViewHolder{

    ImageView iv_Image;
    TextView tv_Name, tv_Number, tv_MainLocation;
    public SalesPersonRVHolder(View item){
        super(item);
        iv_Image = item.findViewById(R.id.iv_Image);
        tv_Name = item.findViewById(R.id.tv_Name);
        tv_Number = item.findViewById(R.id.tv_Number);
        tv_MainLocation = item.findViewById(R.id.tv_Location);
    }
}
