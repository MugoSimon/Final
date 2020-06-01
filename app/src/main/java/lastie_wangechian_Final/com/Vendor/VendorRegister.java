package lastie_wangechian_Final.com.Vendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import lastie_wangechian_Final.com.R;

public class VendorRegister extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private Toolbar toolbar;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //atleast one digit
                    "(?=.*[a-z])" +   //atleast one letter either upercase ama lowercase
                    "(?=\\S+$)" +     //no whitespace in the password
                    "(?=.*[@#&*^+$])" +  // atleast one special characters
                    ".{6,}" +         //minimum of 6 characters
                    "$");
    private Button button_register;
    private TextInputLayout textInputLayout_username;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textInputLayout_username = findViewById(R.id.registerVendor_username);
        textInputLayout_email = findViewById(R.id.registerVendor_email);
        textInputLayout_password = findViewById(R.id.registerVendor_password);
        button_register = findViewById(R.id.registerVendor_Button);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendor Side");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateUsername() | validateEmail() | validatePassword()) {

                    String username = textInputLayout_username.getEditText().getText().toString().trim();
                    String email = textInputLayout_email.getEditText().getText().toString().trim();
                    String password = textInputLayout_password.getEditText().getText().toString().trim();

                    register_vendor(username, email, password);

                }
            }
        });

    }

    private void register_vendor(String username, String email, String password) {


    }

    private boolean validateUsername() {

        String buyer_username = textInputLayout_username.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buyer_username)) {

            textInputLayout_username.requestFocus();
            textInputLayout_username.setError("enter username");
            return false;

        } else if (buyer_username.length() > 15) {

            textInputLayout_username.requestFocus();
            textInputLayout_username.getEditText().setText(null);
            textInputLayout_username.setError("username too long");
            return false;

        } else {

            textInputLayout_username.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {

        String buyer_email = textInputLayout_email.getEditText().getText().toString().trim();

        if (buyer_email.isEmpty()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(buyer_email).matches()) {

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

        } else if (!PASSWORD_PATTERN.matcher(buyer_password).matches()) {

            textInputLayout_password.requestFocus();
            textInputLayout_password.getEditText().setText(null);
            textInputLayout_password.setError("password too weak");
            return false;

        } else {

            textInputLayout_password.setError(null);
            return true;
        }
    }

}
