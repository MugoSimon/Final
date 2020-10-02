package lastie_wangechian_Final.com.Essentials;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import lastie_wangechian_Final.com.Intro.SelectActivity;
import lastie_wangechian_Final.com.R;

public class ForgotPassword extends AppCompatActivity {

    private Toolbar forgot_toolbar;
    private TextInputLayout textInputLayout_email;
    private Button button_submit;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        textInputLayout_email = findViewById(R.id.forgotPassword_email);
        button_submit = findViewById(R.id.button_submit);

        forgot_toolbar = findViewById(R.id.forgot_toolbar);
        setSupportActionBar(forgot_toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        progressDialog = new ProgressDialog(this);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmail()) {

                    return;

                } else {

                    final String email = textInputLayout_email.getEditText().getText().toString().trim();

                    progressDialog.setTitle("Sending request");
                    progressDialog.setMessage("please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "A link was sent to " + email, Toast.LENGTH_LONG).show();
                                Intent next_intent = new Intent(ForgotPassword.this, SelectActivity.class);
                                startActivity(next_intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull final Exception e) {

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.relLayout), "Failure Error", Snackbar.LENGTH_LONG)
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

            }
        });
    }

    private boolean validateEmail() {

        String email = textInputLayout_email.getEditText().getText().toString().trim();

        if (email.isEmpty()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("invalid email");
            textInputLayout_email.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_email.setError(null);
            return true;
        }
    }
}
