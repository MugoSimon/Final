package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import lastie_wangechian_Final.com.Buyer.BuyerSelect;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.Authenticatons.VendorLogin;
import lastie_wangechian_Final.com.Vendor.Authenticatons.VendorRegister;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorMainActivity;

public class VendorSelect extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button button_register;
    private Button button_login;
    private TextView textView_shifter;
    private Toolbar vendorSelect_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_select);

        button_register = findViewById(R.id.btn_VendorRegister);
        button_login = findViewById(R.id.btn_VendorLogin);
        textView_shifter = findViewById(R.id.vendor_textViewShifter);
        vendorSelect_toolbar = findViewById(R.id.vendor_appBar_select);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(vendorSelect_toolbar);
        getSupportActionBar().setTitle("Vendor Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerBuyer_intent = new Intent(VendorSelect.this, VendorRegister.class);
                startActivity(registerBuyer_intent);

            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginBuyer_intent = new Intent(VendorSelect.this, VendorLogin.class);
                startActivity(loginBuyer_intent);

            }
        });

        textView_shifter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shift_to_Buyer_intent = new Intent(VendorSelect.this, BuyerSelect.class);
                startActivity(shift_to_Buyer_intent);

            }
        });
    }

    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

            Intent intent = new Intent(VendorSelect.this, VendorMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}
