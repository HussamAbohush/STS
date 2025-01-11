package com.svu.sts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CreateOrEditSalesPersonActivity extends AppCompatActivity {

    Button BSelectImage,BSave,BDelete;
    String[] Regions;
    ImageView IVPreviewImage;
    Spinner SRegions;
    DatabaseHelper DBHelper;
    EditText ETPhoneNumber;
    EditText ETName;
    SalesPersonModel p = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_or_edit_salesperson);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ETPhoneNumber = findViewById(R.id.et_salesperson_phone_number);
        ETName = findViewById(R.id.et_salesperson_name);
        SRegions =findViewById(R.id.spr_regions);
        BSelectImage = findViewById(R.id.btn_add_photo);
        BSave = findViewById(R.id.btn_save_salesperson);
        BDelete = findViewById(R.id.btn_delete);
        IVPreviewImage = findViewById(R.id.iv_salesperson_pic);
        IVPreviewImage.setImageResource(R.drawable.baseline_account_box_24);

        DBHelper = new DatabaseHelper(CreateOrEditSalesPersonActivity.this);
        ArrayList<String> reg = DBHelper.getRegions();
        Regions =new String[reg.size()];
        Regions = reg.toArray(Regions);
        SRegions.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Regions));


        Bundle b = getIntent().getExtras();

        if (b != null){
            p = new SalesPersonModel(b);

            BSelectImage.setText(R.string.update_photo);
            BSave.setText(R.string.update);
            BDelete.setVisibility(View.VISIBLE);
            ETName.setText(p.getName());
            ETPhoneNumber.setText(p.getPhoneNumber());
            SRegions.setSelection(reg.indexOf(p.getMainLocation()));
            IVPreviewImage.setImageBitmap(p.getImageBitmap());

        }

        BSelectImage.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            selectImageLauncher.launch(i);
        });

        BSave.setOnClickListener(v -> {
            String phoneNumber = ETPhoneNumber.getText().toString();
            String name = ETName.getText().toString();
            int region = SRegions.getSelectedItemPosition()+1;
            IVPreviewImage.buildDrawingCache();
            Bitmap imageBitmap = IVPreviewImage.getDrawingCache();
            byte[] image = getBitmapAsByteArray(imageBitmap);
            if(b != null ) {
                DBHelper.updateSalesperson(p.getId(), name, phoneNumber, region, image);
                Toast.makeText(getApplicationContext(), "Salesperson \" "+name + " \" is Updated!",
                        Toast.LENGTH_LONG).show();
            }
            else{
                DBHelper.addSalesperson(name, phoneNumber, region, image);
                Toast.makeText(getApplicationContext(), "Salesperson \" "+name + " \" is Saved!",
                        Toast.LENGTH_LONG).show();
            }
            CreateOrEditSalesPersonActivity.this.finish();
        });

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if (b != null) DBHelper.deleteSalesperson(p.getId());
                    Toast.makeText(getApplicationContext(), "Salesperson \" "+p.getName() + " \" is Deleted!",
                            Toast.LENGTH_LONG).show();
                    CreateOrEditSalesPersonActivity.this.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };
        BDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Salesperson \"" + p.getName() +"\" Will be Deleted \nBE CAREFUL! this operation can't be undone").setPositiveButton("Delete", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        });
    }


    ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            IVPreviewImage.setImageURI(selectedImageUri);
                        }
                    }
            });

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
