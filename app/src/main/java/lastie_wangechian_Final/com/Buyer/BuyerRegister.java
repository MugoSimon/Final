package lastie_wangechian_Final.com.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import lastie_wangechian_Final.com.R;

public class BuyerRegister extends AppCompatActivity {

    private Toolbar toolbar;
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
    private FirebaseUser firebaseUser;
    private DatabaseReference buyer_DatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_register);

        mAuth = FirebaseAuth.getInstance();
        button_register = findViewById(R.id.registerBuyer_Button);
        textInputLayout_username = findViewById(R.id.registerBuyer_username);
        textInputLayout_email = findViewById(R.id.registerBuyer_email);
        textInputLayout_password = findViewById(R.id.registerBuyer_password);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registering Buyer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reg_progressDialog = new ProgressDialog(this);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
    }

    private void register_user(final String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String user_id = current_user.getUid();
                            Toast.makeText(BuyerRegister.this, user_id, Toast.LENGTH_SHORT).show();

                            buyer_DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Buyer").child(user_id);

                            HashMap<String, String> buyer_map = new HashMap<>();
                            buyer_map.put("username", username);
                            buyer_map.put("image", "default");
                            buyer_map.put("thumbnail", "thumbnail");
                            buyer_map.put("phone_number", "123457890");

                            buyer_DatabaseReference.setValue(buyer_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        reg_progressDialog.dismiss();
                                        Intent next_intent = new Intent(BuyerRegister.this, BuyerMainActivity.class);
                                        startActivity(next_intent);
                                        finish();

                                    } else {
                                        Toast.makeText(BuyerRegister.this, "Error storing data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {

                            reg_progressDialog.hide();
                            Toast.makeText(BuyerRegister.this, "Error registering user", Toast.LENGTH_SHORT).show();

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
