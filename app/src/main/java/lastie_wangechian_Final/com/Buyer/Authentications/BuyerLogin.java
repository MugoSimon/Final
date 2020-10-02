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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import lastie_wangechian_Final.com.Buyer.MainActivity.BuyerMainActivity;
import lastie_wangechian_Final.com.Essentials.ForgotPassword;
import lastie_wangechian_Final.com.R;

public class BuyerLogin extends AppCompatActivity {

    private Toolbar login_toolbar;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private TextView textView_forgotPassword;
    private Button button_login;
    private ProgressDialog login_progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_login);

        mAuth = FirebaseAuth.getInstance();
        login_toolbar = findViewById(R.id.buyer_appBar_login);
        textInputLayout_email = findViewById(R.id.loginBuyer_email);
        textInputLayout_password = findViewById(R.id.buyerLogin_password);
        textView_forgotPassword = findViewById(R.id.textView_shifter);
        button_login = findViewById(R.id.button_login);


        setSupportActionBar(login_toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login_progressDialog = new ProgressDialog(this);

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next_intent = new Intent(BuyerLogin.this, ForgotPassword.class);
                startActivity(next_intent);
                finish();

            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateEmail() | !validatePassword()) {

                        return;

                    } else {

                        String email = textInputLayout_email.getEditText().getText().toString().trim();
                        String password = textInputLayout_password.getEditText().getText().toString().trim();

                        login_progressDialog.setTitle("Authenticating credentials");
                        login_progressDialog.setMessage("please wait...");
                        login_progressDialog.setCanceledOnTouchOutside(false);
                        login_progressDialog.show();

                        login_user(email, password);

                    }

                } catch (Exception e) {


                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relLayout), "Something went wrong", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getApplicationContext(), "Button isn't working", Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();

                    return;
                }

            }
        });

    }


    private void login_user(String email, String password) {

        try {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                login_progressDialog.dismiss();
                                Intent next_intent = new Intent(BuyerLogin.this, BuyerMainActivity.class);
                                startActivity(next_intent);
                                finish();

                            } else {

                                login_progressDialog.hide();
                                Toast.makeText(BuyerLogin.this, "Check your credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relLayout), "Something went wrong", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getApplicationContext(), "Trying to configure settings", Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();
                    return;
                }
            });

        } catch (Exception e) {

            Toast.makeText(BuyerLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;

        }


    }

    private boolean validateEmail() {

        String buyer_email = textInputLayout_email.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buyer_email)) {

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

        } else {

            textInputLayout_password.setError(null);
            return true;
        }
    }
}
