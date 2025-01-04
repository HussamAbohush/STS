package com.svu.sts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

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

        DBHelper = new DatabaseHelper(SalespersonsActivity.this);
        data = new ArrayList<>();
        data = DBHelper.getSalesPersons();
        BtnAddSalesperson = findViewById(R.id.fab_add_salesperson);
        rv_SalesPersons = findViewById(R.id.rv_salespersons);

        BtnAddSalesperson.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), CreateOrEditSalesPersonActivity.class);
            startActivity(intent);
        });

        ap = new SalesPersonRVAdapter(data, details -> {
            Intent intent = new Intent(getApplicationContext(), CreateOrEditSalesPersonActivity.class);
            intent.putExtras(details.getAsBundle());
            startActivity(intent);
        });
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