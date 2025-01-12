package com.svu.sts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button BAddSalesperson, BSales,BCommissions;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new DatabaseHelper(MainActivity.this);
        db.init();
        BAddSalesperson = findViewById(R.id.addSalesperson);
        BSales = findViewById(R.id.btn_sales);
        BCommissions = findViewById(R.id.btn_commissions);
        BAddSalesperson.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SalespersonsActivity.class);
            intent.putExtra("Activity","Salespersons");
            startActivity(intent);

        });
        BSales.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), SalespersonsActivity.class);
            intent.putExtra("Activity","Sales");
            startActivity(intent);
        });
        BCommissions.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SalespersonsActivity.class);
            intent.putExtra("Activity","Commissions");
            startActivity(intent);
        });

    }
}