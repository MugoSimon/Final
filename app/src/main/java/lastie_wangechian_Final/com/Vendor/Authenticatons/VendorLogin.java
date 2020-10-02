package lastie_wangechian_Final.com.Vendor.Authenticatons;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import lastie_wangechian_Final.com.Essentials.ForgotPassword;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorMainActivity;

public class VendorLogin extends AppCompatActivity {

    private Toolbar login_toolbar;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_login;
    private FirebaseAuth mAuth;
    private TextView textView_forgotPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);

        mAuth = FirebaseAuth.getInstance();
        login_toolbar = findViewById(R.id.vendor_appBar_login);
        textInputLayout_email = findViewById(R.id.loginVendor_email);
        textInputLayout_password = findViewById(R.id.vendorLogin_password);
        button_login = findViewById(R.id.button_login);
        textView_forgotPassword = findViewById(R.id.textView_shifter);

        setSupportActionBar(login_toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateEmail() | !validatePassword()) {

                        return;

                    } else {

                        String email = textInputLayout_email.getEditText().getText().toString().trim();
                        String password = textInputLayout_password.getEditText().getText().toString().trim();

                        progressDialog.setTitle("Authenticating");
                        progressDialog.setMessage("please wait as you check your credentials");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        vendor_login(email, password);

                    }
                } catch (Exception e) {

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_login), "Something went wrong", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getApplicationContext(), "Button isn't working", Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();

                }

            }
        });

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void vendor_login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), VendorMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    progressDialog.hide();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_login), "Authentication Failed", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {

                Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_login), "Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("View Details", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                snackbar.show();

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
