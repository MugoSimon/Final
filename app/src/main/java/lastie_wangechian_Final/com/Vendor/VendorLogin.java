package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import lastie_wangechian_Final.com.R;

public class VendorLogin extends AppCompatActivity {

    CollectionReference documentReference;
    private Toolbar toolbar;
    private EditText editText_phoneNumber;
    private TextInputLayout textInputLayout_password;
    private CountryCodePicker countryCodePicker_login;
    private Button button_login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private TextView textView_shifter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        editText_phoneNumber = findViewById(R.id.vendorLogin_phoneNumber);
        countryCodePicker_login = findViewById(R.id.vendorLogin_ccp);
        textInputLayout_password = findViewById(R.id.vendorLogin_password);
        button_login = findViewById(R.id.button_login);
        countryCodePicker_login.registerCarrierNumberEditText(editText_phoneNumber);
        textView_shifter = findViewById(R.id.textView_shifter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            mAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            documentReference = fStore.collection("Vendor");

            Toast.makeText(VendorLogin.this, documentReference.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(VendorLogin.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    documentReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot objectDocsnapshot : queryDocumentSnapshots) {

                                String db_phoneNumber = objectDocsnapshot.getString("Phone");
                                String db_password = objectDocsnapshot.getString("Password");
                                Toast.makeText(VendorLogin.this, db_phoneNumber + " " + db_password, Toast.LENGTH_LONG).show();

                                if (validatePassword() | validatePhonenumber()) {

                                    String input_phoneNumber = countryCodePicker_login.getFullNumberWithPlus();
                                    String input_password = textInputLayout_password.getEditText().getText().toString();

                                    try {

                                        if (input_phoneNumber.equals(db_phoneNumber)) {

                                            if (input_password.equals(db_password)) {

                                                startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                                                finish();
                                            } else {

                                                Toast.makeText(VendorLogin.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                                                return;

                                            }

                                        } else {
                                            return;
                                        }

                                    } catch (Exception e) {


                                    }


                                } else {
                                    return;
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(VendorLogin.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {

                    Toast.makeText(VendorLogin.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
                }
            }
        });

        textView_shifter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), VendorRegister.class));
                finish();
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

    private boolean validatePassword() {

        String buyer_password = textInputLayout_password.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buyer_password)) {

            textInputLayout_password.requestFocus();
            textInputLayout_password.setError("password required");
            return false;

        } else {

            textInputLayout_password.setError(null);
            return true;
        }
    }
}
