package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.VendorSelect;

public class BuyerSelect extends AppCompatActivity {

    private Button button_register;
    private Button button_login;
    private TextView textView_shifter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_select);

        button_register = findViewById(R.id.btn_BuyerRegister);
        button_login = findViewById(R.id.btn_BuyerLogin);
        textView_shifter = findViewById(R.id.buyer_textViewShifter);
        toolbar = findViewById(R.id.buyer_Toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choose Authentication");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerBuyer_intent = new Intent(BuyerSelect.this, BuyerRegister.class);
                startActivity(registerBuyer_intent);

            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginBuyer_intent = new Intent(BuyerSelect.this, BuyerLogin.class);
                startActivity(loginBuyer_intent);

            }
        });

        textView_shifter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shift_to_Buyer_intent = new Intent(BuyerSelect.this, VendorSelect.class);
                startActivity(shift_to_Buyer_intent);

            }
        });
    }
}
