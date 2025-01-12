package com.svu.sts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChooseSalespersonActivity extends AppCompatActivity {


    RecyclerView rv_SalesPersons;
    DatabaseHelper DBHelper;
    TextView TVTitle;
    ArrayList<SalesPersonModel> data;
    SalesPersonRVAdapter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_salesperson);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String activity = getIntent().getStringExtra("Activity");
        DBHelper = new DatabaseHelper(ChooseSalespersonActivity.this);
        data = new ArrayList<>();
        data = DBHelper.getSalesPersons();
        rv_SalesPersons = findViewById(R.id.rv_salespersons);
        TVTitle = findViewById(R.id.tv_choose);
        if(activity.equals("Sales")){
            ap = new SalesPersonRVAdapter(data, details -> {
                Intent intent = new Intent(getApplicationContext(), AddSalesActivity.class);
                intent.putExtras(details.getAsBundle());
                startActivity(intent);
            });
        }else{
            TVTitle.setText(R.string.choose_to_calculate_commissions);
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
}