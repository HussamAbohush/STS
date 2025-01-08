package com.svu.sts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.kal.rackmonthpicker.MonthType;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddSalesActivity extends AppCompatActivity {

    Button BSave;
    ImageButton BChooseMonth;

    TextView TVId,TVName, TVPhoneNumber, TVRegion,TVMonth,TVYear;
    DatabaseHelper DBHelper;
    ImageView IVPreviewImage;
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

        IVPreviewImage = findViewById(R.id.iv_salesperson_pic);
        BSave = findViewById(R.id.btn_save_sales);
        TVId = findViewById(R.id.tv_id);
        TVRegion = findViewById(R.id.tv_main_location);
        TVPhoneNumber = findViewById(R.id.tv_phone_number);
        TVName = findViewById(R.id.tv_name);
        BChooseMonth = findViewById(R.id.btn_choose_month);
        TVMonth = findViewById(R.id.tv_month);
        TVYear = findViewById(R.id.tv_year);
        String currentMonth = new SimpleDateFormat("M", Locale.getDefault()).format(new Date());
        TVMonth.setText(currentMonth);
        String currentYear = new SimpleDateFormat("YYYY", Locale.getDefault()).format(new Date());
        TVYear.setText(currentYear);

        final RackMonthPicker rackMonthPicker = new RackMonthPicker(this)
                .setMonthType(MonthType.TEXT)
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    TVMonth.setText(String.valueOf(month));
                    TVYear.setText(String.valueOf(year));
                })
                .setNegativeButton(AppCompatDialog::dismiss)
                .setColorTheme(R.style.Theme_STS)
                ;


        BChooseMonth.setOnClickListener(v ->{
            rackMonthPicker.show();
        });

//        DBHelper = new DatabaseHelper(AddSalesActivity.this);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            SalesPersonModel p = new SalesPersonModel(b);
            TVName.setText(p.getName());
            TVId.setText(String.valueOf(p.getId()) );
            TVPhoneNumber.setText(p.getPhoneNumber());
            TVRegion.setText(p.getMainLocation());
            IVPreviewImage.setImageBitmap(p.getImageBitmap());
        }
    }
}