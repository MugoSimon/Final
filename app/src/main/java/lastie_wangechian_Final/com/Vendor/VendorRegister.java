package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import lastie_wangechian_Final.com.R;

public class VendorRegister extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private Toolbar toolbar;
    private EditText editText_phoneNumber;
    private Button button_next;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        button_next = findViewById(R.id.registerVendor_Button);
        editText_phoneNumber = findViewById(R.id.registerVendor_phoneNumber);
        countryCodePicker = findViewById(R.id.registerVendor_ccp);
        countryCodePicker.registerCarrierNumberEditText(editText_phoneNumber);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendor Side");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (!validatePhonenumber()) {

                        return;

                    } else {

                        String vendor_phonenumber = countryCodePicker.getFullNumberWithPlus();
                        //Toast.makeText(BuyerRegister.this, buyer_phonenumber, Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getApplicationContext(),BuyerCode.class));
                        Intent next_intent = new Intent(VendorRegister.this, VendorCode.class);
                        next_intent.putExtra("vendor_phonenumber", vendor_phonenumber);
                        startActivity(next_intent);
                        finish();

                    }

                } catch (Exception e) {

                    Toast.makeText(VendorRegister.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    //method validate phoneNumber
    private boolean validatePhonenumber() {

        String buyer_phonenumber = editText_phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(buyer_phonenumber)) {

            editText_phoneNumber.requestFocus();
            editText_phoneNumber.setError("field can't be empty");
            return false;

        } else if (buyer_phonenumber.length() != 9) {

            editText_phoneNumber.requestFocus();
            editText_phoneNumber.setError("invalid phonenumber");
            editText_phoneNumber.setText(null);

            return false;

        } else {

            editText_phoneNumber.setError(null);
            return true;
        }

    }
}
