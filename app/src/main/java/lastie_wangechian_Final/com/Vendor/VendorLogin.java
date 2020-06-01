package lastie_wangechian_Final.com.Vendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lastie_wangechian_Final.com.R;

public class VendorLogin extends AppCompatActivity {

    CollectionReference documentReference;
    private Toolbar toolbar;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private TextView textView_forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        textInputLayout_email = findViewById(R.id.loginVendor_email);
        textInputLayout_password = findViewById(R.id.vendorLogin_password);
        button_login = findViewById(R.id.button_login);
        textView_forgotPassword = findViewById(R.id.textView_forgotPassword);

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


            }
        });

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo open the forgot password activity
                finish();
            }
        });


    }

    private boolean validateEmail() {

        String vendor_email = textInputLayout_email.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(vendor_email)) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(vendor_email).matches()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("invalid email");
            textInputLayout_email.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_email.setError(null);
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
