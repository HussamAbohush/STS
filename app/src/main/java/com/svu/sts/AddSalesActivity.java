package com.svu.sts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddSalesActivity extends AppCompatActivity {

    Button BSave;
    TextView TVId,TVName, TVPhoneNumber, TVRegion,TVCommission;
    DatabaseHelper DBHelper;
    ImageView IVPreviewImage;
    int currentYear, currentMonth;
    EditText ETLebanon, ETCoastal,ETNorthern,ETSouthern,ETEastern;
    NumberPicker NpYear,NpMonth;
    SalesPersonModel p;
    List<SaleDetailModel> sdl;
    long commission = 0;
    int mainRegionID =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_sales);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBHelper = new DatabaseHelper(AddSalesActivity.this);

        Bundle b = getIntent().getExtras();
        p = new SalesPersonModel(b);
        initViews();
        mainRegionID = DBHelper.getRegionIdByName(p.getMainLocation());

        sdl = DBHelper.getSaleDetailsBySaleDate(currentYear,currentMonth,p.getId());
        updateEditTextValues(sdl);
        TVCommission.setText(String.valueOf( calculateCommission(p,sdl)));
        NpMonth.setOnValueChangedListener((picker, oldVal, newVal) -> {
            currentMonth = NpMonth.getValue();
            sdl = DBHelper.getSaleDetailsBySaleDate(currentYear,currentMonth,p.getId());
            updateEditTextValues(sdl);
            TVCommission.setText(String.valueOf( calculateCommission(p,sdl)));
        });

        NpYear.setOnValueChangedListener((picker, oldVal, newVal) -> {
            currentYear = NpYear.getValue();
            sdl = DBHelper.getSaleDetailsBySaleDate(currentYear,currentMonth,p.getId());
            updateEditTextValues(sdl);
            TVCommission.setText(String.valueOf( calculateCommission(p,sdl)));
        });
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        updateSaleAndDetails(p);
                        Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                        sdl = DBHelper.getSaleDetailsBySaleDate(currentYear,currentMonth,p.getId());
                        calculateCommission(p,sdl);
                        DBHelper.addOrUpdateCommission(p.getId(),currentMonth,currentYear,commission);
                        TVCommission.setText(String.valueOf( commission));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    updateEditTextValues(sdl);
                    break;
            }
        };
        BSave.setOnClickListener(v ->{
            try{
                addSaleAndDetails(p);
                sdl = DBHelper.getSaleDetailsBySaleDate(currentYear,currentMonth,p.getId());
                calculateCommission(p,sdl);
                DBHelper.addOrUpdateCommission(p.getId(),currentMonth,currentYear,commission);
                TVCommission.setText(String.valueOf( commission));
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do You Want to Update The Sales For This Month  \"" + currentYear+"-"+currentMonth +"\" \nBE CAREFUL! this operation can't be undone").setPositiveButton("Update", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }

        });


    }

    private void initViews(){
        ETLebanon = findViewById(R.id.et_lebanon);
        ETSouthern = findViewById(R.id.et_southern);
        ETCoastal = findViewById(R.id.et_coastal);
        ETEastern = findViewById(R.id.et_eastern);
        ETNorthern = findViewById(R.id.et_northern);
        IVPreviewImage = findViewById(R.id.iv_salesperson_pic);
        BSave = findViewById(R.id.btn_save_sales);
        TVId = findViewById(R.id.tv_id);
        TVRegion = findViewById(R.id.tv_main_location);
        TVPhoneNumber = findViewById(R.id.tv_phone_number);
        TVName = findViewById(R.id.tv_name);
        TVCommission = findViewById(R.id.tv_commission);
        NpMonth = findViewById(R.id.np_month);
        NpYear =findViewById(R.id.np_year);
        NpYear.setMaxValue(2030);
        NpYear.setMinValue(2010);
        NpMonth.setMinValue(1);
        NpMonth.setMaxValue(12);
        currentMonth = Integer.parseInt( new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        NpMonth.setValue(currentMonth);
        NpYear.setValue(currentYear);
        TVName.setText(p.getName());
        TVId.setText(String.valueOf(p.getId()) );
        TVPhoneNumber.setText(p.getPhoneNumber());
        TVRegion.setText(p.getMainLocation());
        IVPreviewImage.setImageBitmap(p.getImageBitmap());
    }

    private void updateSaleAndDetails(SalesPersonModel p) throws Exception {
        try {
            // Update Sale record (if you want to update the sale information like date)
            int saleId = DBHelper.getSaleId(p.getId(), currentYear, currentMonth);

            // Update Sale_Details records based on regions and their respective amounts
            // Check if EditText is empty, and set the value to 0 if it is
            String lebanonAmount = ETLebanon.getText().toString().isEmpty() ? "0" : ETLebanon.getText().toString();
            String coastalAmount = ETCoastal.getText().toString().isEmpty() ? "0" : ETCoastal.getText().toString();
            String northernAmount = ETNorthern.getText().toString().isEmpty() ? "0" : ETNorthern.getText().toString();
            String southernAmount = ETSouthern.getText().toString().isEmpty() ? "0" : ETSouthern.getText().toString();
            String easternAmount = ETEastern.getText().toString().isEmpty() ? "0" : ETEastern.getText().toString();

            // Update Sale_Detail for each region with the corresponding amount
            DBHelper.updateSaleDetail(saleId, 1, Long.parseLong(lebanonAmount));
            DBHelper.updateSaleDetail(saleId, 2, Long.parseLong(coastalAmount));
            DBHelper.updateSaleDetail(saleId, 3, Long.parseLong(northernAmount));
            DBHelper.updateSaleDetail(saleId, 4, Long.parseLong(southernAmount));
            DBHelper.updateSaleDetail(saleId, 5, Long.parseLong(easternAmount));

        } catch (Exception e) {
            // Throw a generic exception if anything fails
            throw new Exception("Failed to update sale and details", e);
        }
    }

    private long calculateCommission(SalesPersonModel p,List<SaleDetailModel> sdl){
        commission = 0 ;
    for (SaleDetailModel sdm:sdl) {
        long amount =sdm.getAmount();
        if(sdm.getRegionId() == mainRegionID){
            if(amount > 100000000){
                commission += 5000000;
                amount -= 100000000;
                commission += (long) (amount * 0.07);
            }else{
                commission += (long) (amount * 0.05);
            }
        }else{
            if(amount > 100000000){
                commission += 3000000;
                amount -= 100000000;
                commission += (long) (amount * 0.04);
            }else{
                commission += (long) (amount * 0.03);
            }
        }
    }
return commission;
}

    private void addSaleAndDetails(SalesPersonModel p) throws Exception{
        try {
            int saleId = DBHelper.addSale(p.getId(),currentYear,currentMonth);
            if (!ETLebanon.getText().toString().isEmpty())
                DBHelper.addSaleDetail(saleId,1,Long.parseLong(ETLebanon.getText().toString()));
            if (!ETCoastal.getText().toString().isEmpty())
                DBHelper.addSaleDetail(saleId,2,Long.parseLong(ETCoastal.getText().toString()));
            if (!ETNorthern.getText().toString().isEmpty())
                DBHelper.addSaleDetail(saleId,3,Long.parseLong(ETNorthern.getText().toString()));
            if (!ETSouthern.getText().toString().isEmpty())
                DBHelper.addSaleDetail(saleId,4,Long.parseLong(ETSouthern.getText().toString()));
            if (!ETEastern.getText().toString().isEmpty())
                DBHelper.addSaleDetail(saleId,5,Long.parseLong(ETEastern.getText().toString()));
        }catch (Exception e){
            throw new Exception();
        }

    }

    private void updateEditTextValues(List<SaleDetailModel> sdl){
        ETLebanon.setText("");
        ETCoastal.setText("");
        ETNorthern.setText("");
        ETSouthern.setText("");
        ETEastern.setText("");
        for (SaleDetailModel sdm:sdl) {
            if(sdm.getRegionId() == 1) ETLebanon.setText(String.valueOf(sdm.getAmount()));
            if(sdm.getRegionId() == 2) ETCoastal.setText(String.valueOf(sdm.getAmount()));
            if(sdm.getRegionId() == 3) ETNorthern.setText(String.valueOf(sdm.getAmount()));
            if(sdm.getRegionId() == 4) ETSouthern.setText(String.valueOf(sdm.getAmount()));
            if(sdm.getRegionId() == 5) ETEastern.setText(String.valueOf(sdm.getAmount()));

        }
    }

}
