package lastie_wangechian_Final.com.Buyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class AddDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextInputLayout textInputLayout_username;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private Button button_save;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //atleast one digit
                    "(?=.*[a-z])" +   //atleast one letter either upercase ama lowercase
                    "(?=\\S+$)" +     //no whitespace in the password
                    "(?=.*[@#&*^+$])" +  // atleast one special characters
                    ".{6,}" +         //minimum of 6 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        textInputLayout_username = findViewById(R.id.registerBuyer_username);
        textInputLayout_email = findViewById(R.id.registerBuyer_email);
        textInputLayout_password = findViewById(R.id.registerBuyer_password);
        circleImageView = findViewById(R.id.buyer_image);
        button_save = findViewById(R.id.button_save);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Additional Details");


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
