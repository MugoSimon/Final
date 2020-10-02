package lastie_wangechian_Final.com.Buyer.Authentications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import lastie_wangechian_Final.com.R;

public class BuyerRegister extends AppCompatActivity {

    private Toolbar register_toolbar;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //atleast one digit
                    "(?=.*[a-z])" +   //atleast one letter either upercase ama lowercase
                    "(?=\\S+$)" +     //no whitespace in the password
                    "(?=.*[@#&*^+$])" +  // atleast one special characters
                    ".{6,}" +         //minimum of 6 characters
                    "$");
    private ProgressDialog reg_progressDialog;
    private Button button_register;
    private TextInputLayout textInputLayout_username;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private FirebaseAuth mAuth;
    private TextView textView_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_register);

        mAuth = FirebaseAuth.getInstance();
        button_register = findViewById(R.id.registerBuyer_Button);
        textInputLayout_username = findViewById(R.id.registerBuyer_username);
        textInputLayout_email = findViewById(R.id.registerBuyer_email);
        textInputLayout_password = findViewById(R.id.registerBuyer_password);
        textView_login = findViewById(R.id.login);


        register_toolbar = findViewById(R.id.buyer_appBar_register);
        setSupportActionBar(register_toolbar);
        getSupportActionBar().setTitle("Registering Buyer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reg_progressDialog = new ProgressDialog(this);

        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next_intent = new Intent(BuyerRegister.this, BuyerLogin.class);
                startActivity(next_intent);
                finish();
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateUsername() | !validateEmail() | !validatePassword()) {

                        return;

                    } else {

                        String username = textInputLayout_username.getEditText().getText().toString();
                        String email = textInputLayout_email.getEditText().getText().toString();
                        String password = textInputLayout_password.getEditText().getText().toString();

                        reg_progressDialog.setTitle("Registering User");
                        reg_progressDialog.setMessage("kindly wait as we register you");
                        reg_progressDialog.setCanceledOnTouchOutside(false);
                        reg_progressDialog.show();

                        register_user(username, email, password);

                    }

                } catch (NullPointerException e) {

                    return;

                }


            }
        });
    }

    private void register_user(final String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        try {

                            if (task.isSuccessful()) {

                                reg_progressDialog.dismiss();
                                Intent next_intent = new Intent(BuyerRegister.this, AddBuyerDetails.class);
                                next_intent.putExtra("username", username);
                                next_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(next_intent);
                                finish();

                            } else {

                                reg_progressDialog.hide();
                                Toast.makeText(BuyerRegister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } catch (final RuntimeException e) {

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.reg_buyer), "Runtime Error", Snackbar.LENGTH_LONG)
                                    .setAction("View Details", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                            snackbar.show();
                        }

                    }
                });

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
