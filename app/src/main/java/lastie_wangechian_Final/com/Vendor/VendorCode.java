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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import lastie_wangechian_Final.com.R;

public class VendorCode extends AppCompatActivity {

    FirebaseFirestore fStore;
    String verficationId;
    PhoneAuthProvider.ForceResendingToken token;
    private EditText editText_code;
    private Button button_verify;
    private TextView textView_didntGet;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                editText_code.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(VendorCode.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();

            } else if (e instanceof FirebaseTooManyRequestsException) {

                Toast.makeText(VendorCode.this, "Sms Quota exceed", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verficationId = s;
            token = forceResendingToken;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            super.onCodeAutoRetrievalTimeOut(s);

            Toast.makeText(VendorCode.this, "OTP expired...Try again", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_code);

        editText_code = findViewById(R.id.edtVendor_code);
        button_verify = findViewById(R.id.btnVerify_vendorCode);
        textView_didntGet = findViewById(R.id.didnt_getCode);
        mAuth = FirebaseAuth.getInstance();

        //getting information from previous intent
        String phone_number = getIntent().getStringExtra("vendor_phonenumber");
        sendVerificationCode(phone_number);

        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String code = editText_code.getText().toString().trim();

                    if (TextUtils.isEmpty(code) || code.length() != 6) {

                        editText_code.requestFocus();
                        editText_code.setText(null);
                        editText_code.setError("invalid code");
                        return;

                    } else {

                        editText_code.setError(null);
                        verifyCode(code);

                    }

                } catch (Exception e) {

                    Toast.makeText(VendorCode.this, "Offline", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), VendorRegister.class));
                    finish();
                }
            }
        });

        textView_didntGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), VendorRegister.class));
                finish();
            }
        });
    }

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationId, code);
        signInWithCredentials(credential);
    }

    private void sendVerificationCode(String phone_number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {

        try {
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String phone_number = getIntent().getStringExtra("vendor_phonenumber");
                        Intent next_intent = new Intent(VendorCode.this, AddVendorDetails.class);
                        next_intent.putExtra("vendor_phonenumber", phone_number);
                        startActivity(next_intent);
                        finish();

                    } else {
                        Toast.makeText(VendorCode.this, "Authentication failed " + task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception e) {

            Toast.makeText(VendorCode.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
