package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import lastie_wangechian_Final.com.Buyer.Authentications.BuyerLogin;
import lastie_wangechian_Final.com.Buyer.Authentications.BuyerRegister;
import lastie_wangechian_Final.com.Buyer.MainActivity.BuyerMainActivity;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.VendorSelect;

public class BuyerSelect extends AppCompatActivity {

    private Button button_register;
    private Button button_login;
    private TextView textView_shifter;
    private Toolbar buyerSelect_toolbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_select);

        button_register = findViewById(R.id.btn_BuyerRegister);
        button_login = findViewById(R.id.btn_BuyerLogin);
        textView_shifter = findViewById(R.id.buyer_textViewShifter);
        buyerSelect_toolbar = findViewById(R.id.buyerselect_toolbar);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(buyerSelect_toolbar);
        getSupportActionBar().setTitle("Buyer Side");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

            Intent intent = new Intent(BuyerSelect.this, BuyerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}
