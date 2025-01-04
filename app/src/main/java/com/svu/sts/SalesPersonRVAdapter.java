package com.svu.sts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SalesPersonRVAdapter extends RecyclerView.Adapter<SalesPersonRVHolder>{
    ArrayList<SalesPersonModel> dataList = new ArrayList<SalesPersonModel>();
    private ItemClickListener itemListener;
    public SalesPersonRVAdapter(ArrayList<SalesPersonModel> dataList, ItemClickListener itemListener){
        this.dataList = dataList;
        this.itemListener = itemListener;
    }
    @NonNull
    @Override
    public SalesPersonRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salesperson,null,
                false);
        SalesPersonRVHolder vh = new SalesPersonRVHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull SalesPersonRVHolder holder, int position) {
        SalesPersonModel dc= dataList.get(position);

        if (dc.getImageBitmap() == null){
            holder.iv_Image.setImageResource(R.drawable.baseline_account_box_24 );

        }else {
            holder.iv_Image.setImageBitmap(dc.getImageBitmap());
        }
        holder.tv_Name.setText(dc.getName());
        holder.tv_Number.setText(dc.getPhoneNumber());
        holder.tv_MainLocation.setText(dc.getMainLocation());
        holder.itemView.setOnClickListener(view ->{
            itemListener.onItemClick(dc);
        });

    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface ItemClickListener{
        void onItemClick(SalesPersonModel details);
    }
}
