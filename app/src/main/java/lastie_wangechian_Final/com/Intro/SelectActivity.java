package lastie_wangechian_Final.com.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import lastie_wangechian_Final.com.Buyer.BuyerSelect;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.VendorSelect;

public class SelectActivity extends AppCompatActivity {

    //FirebaseAuth mAuth;
    private Button btn_waterVendor;
    private Button btn_waterBuyer;
    private Toolbar select_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //variables from the layouts
        btn_waterBuyer = findViewById(R.id.btn_water_buyer);
        btn_waterVendor = findViewById(R.id.btn_water_vendor);
        select_toolbar = findViewById(R.id.rest_appBar);
        //mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(select_toolbar);
        getSupportActionBar().setTitle("Select Side");

        //when the water buyer button is clicked
        btn_waterBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent waterBuyer_intent = new Intent(SelectActivity.this, BuyerSelect.class);
                startActivity(waterBuyer_intent);

            }
        });


        //when the water vendor button is clicked
        btn_waterVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent waterVendor_intent = new Intent(SelectActivity.this, VendorSelect.class);
                startActivity(waterVendor_intent);

            }
        });

    }


}
