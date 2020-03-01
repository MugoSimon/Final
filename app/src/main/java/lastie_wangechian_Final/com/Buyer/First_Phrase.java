package lastie_wangechian_Final.com.Buyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import lastie_wangechian_Final.com.R;

public class First_Phrase extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText_phoneNumber;
    private Button button_next;
    private CountryCodePicker countryCodePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__phrase);

        button_next = findViewById(R.id.registerBuyer_Button);
        editText_phoneNumber = findViewById(R.id.registerBuyer_phoneNumber);
        countryCodePicker = findViewById(R.id.registerBuyer_ccp);
        countryCodePicker.registerCarrierNumberEditText(editText_phoneNumber);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buyer Side");

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validatePhonenumber()){

                    return;

                }else {
                    String buyer_phonenumber = countryCodePicker.getFullNumberWithPlus();
                    //Toast.makeText(First_Phrase.this, buyer_phonenumber, Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),BuyerCode.class));
                    Intent next_intent = new Intent(First_Phrase.this, BuyerCode.class);
                    next_intent.putExtra("buyer_phonenumber",buyer_phonenumber);
                    startActivity(next_intent);
                    finish();

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
