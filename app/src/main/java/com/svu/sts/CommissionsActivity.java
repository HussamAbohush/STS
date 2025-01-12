package com.svu.sts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommissionsActivity extends AppCompatActivity {

    TextView TVId,TVName, TVPhoneNumber, TVRegion,TVCommission;
    NumberPicker NpFromYear,NpFromMonth,NpToYear,NpToMonth ;
    Button BCalculate;
    ImageView IVPreviewImage;
    SalesPersonModel p;
    List<CommissionModel> cml;
    DatabaseHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_commissions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBHelper = new DatabaseHelper(CommissionsActivity.this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            p = new SalesPersonModel(b);
        }
        initViews();
        BCalculate.setOnClickListener(v->{
            int fromYear = NpFromYear.getValue();
            int fromMonth = NpFromMonth.getValue();
            int toYear = NpToYear.getValue();
            int toMonth = NpToMonth.getValue();
            if(fromYear <= toYear && fromMonth <= toMonth){
                cml = DBHelper.getCommissionsByDate(fromYear,fromMonth,toYear,toMonth,p.getId());
                long commissions = CalculateCommissions(cml);
                TVCommission.setText(String.valueOf(commissions));
            }
        });


    }

    private void initViews(){
        IVPreviewImage = findViewById(R.id.iv_salesperson_pic);
        BCalculate = findViewById(R.id.btn_Calculate);
        TVId = findViewById(R.id.tv_id);
        TVRegion = findViewById(R.id.tv_main_location);
        TVPhoneNumber = findViewById(R.id.tv_phone_number);
        TVName = findViewById(R.id.tv_name);
        TVCommission = findViewById(R.id.tv_commission);
        NpFromMonth = findViewById(R.id.np_from_month);
        NpFromYear =findViewById(R.id.np_from_year);
        NpFromYear.setMaxValue(2030);
        NpFromYear.setMinValue(2010);
        NpFromMonth.setMinValue(1);
        NpFromMonth.setMaxValue(12);
        NpToMonth = findViewById(R.id.np_to_month);
        NpToYear =findViewById(R.id.np_to_year);
        NpToYear.setMaxValue(2030);
        NpToYear.setMinValue(2010);
        NpToMonth.setMinValue(1);
        NpToMonth.setMaxValue(12);
        int currentMonth = Integer.parseInt( new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        NpFromMonth.setValue(currentMonth);
        NpFromYear.setValue(currentYear);
        NpToMonth.setValue(currentMonth);
        NpToYear.setValue(currentYear);
        TVName.setText(p.getName());
        TVId.setText(String.valueOf(p.getId()) );
        TVPhoneNumber.setText(p.getPhoneNumber());
        TVRegion.setText(p.getMainLocation());
        IVPreviewImage.setImageBitmap(p.getImageBitmap());
    }

private long CalculateCommissions(List<CommissionModel> cml){
        long sum = 0;
    for (CommissionModel cm:cml) {
        sum += cm.getAmount();
    }
    return sum;
}
}