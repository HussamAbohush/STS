package com.svu.sts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SalespersonsActivity extends AppCompatActivity {

    RecyclerView rv_SalesPersons;
    FloatingActionButton BtnAddSalesperson;
    TextView TVTitle;
    DatabaseHelper DBHelper;
    ArrayList<SalesPersonModel> data;
    SalesPersonRVAdapter ap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salespersons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String activity = getIntent().getStringExtra("Activity");
        DBHelper = new DatabaseHelper(SalespersonsActivity.this);
        data = new ArrayList<>();
        data = DBHelper.getSalesPersons();
        BtnAddSalesperson = findViewById(R.id.fab_add_salesperson);
        rv_SalesPersons = findViewById(R.id.rv_salespersons);
        TVTitle = findViewById(R.id.tv_title);

        BtnAddSalesperson.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), CreateOrEditSalesPersonActivity.class);
            startActivity(intent);
        });
        assert activity != null;
        if(activity.equals("Salespersons")){
            ap = new SalesPersonRVAdapter(data, details -> {
                Intent intent = new Intent(getApplicationContext(), CreateOrEditSalesPersonActivity.class);
                intent.putExtras(details.getAsBundle());
                startActivity(intent);
            });
        } else if(activity.equals("Sales")){
            TVTitle.setText(R.string.choose_to_add_sales_record);
            BtnAddSalesperson.setVisibility(View.INVISIBLE);
            ap = new SalesPersonRVAdapter(data, details -> {
                Intent intent = new Intent(getApplicationContext(), SalesActivity.class);
                intent.putExtras(details.getAsBundle());
                startActivity(intent);
            });
        }else{
            TVTitle.setText(R.string.choose_to_calculate_commissions);
            BtnAddSalesperson.setVisibility(View.INVISIBLE);
            ap = new SalesPersonRVAdapter(data, details -> {
                Intent intent = new Intent(getApplicationContext(), CommissionsActivity.class);
                intent.putExtras(details.getAsBundle());
                startActivity(intent);
            });
        }

        rv_SalesPersons.setAdapter(ap);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        rv_SalesPersons.setLayoutManager(lm);
        rv_SalesPersons.setHasFixedSize(true);

    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume(){
        super.onResume();
        ap.dataList = DBHelper.getSalesPersons();
        ap.notifyDataSetChanged();

    }


}