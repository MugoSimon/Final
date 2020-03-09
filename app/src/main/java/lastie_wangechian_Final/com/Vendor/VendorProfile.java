package lastie_wangechian_Final.com.Vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class VendorProfile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;
    String userID;
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_phonenumber;
    private Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendor Profile");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getUid();

        textView_name = findViewById(R.id.vendor_name);
        textView_email = findViewById(R.id.vendor_email);
        textView_phonenumber = findViewById(R.id.vendor_phone);
        button_next = findViewById(R.id.button_next);
        circleImageView = findViewById(R.id.vendor_image);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading info");
        progressDialog.setMessage("please wait while we load your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        try {

            DocumentReference documentReference = fStore.collection("Vendor").document(userID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        String username = documentSnapshot.getString("Username");
                        String email = documentSnapshot.getString("Email");
                        String image = documentSnapshot.getString("Image");
                        String phonenumber = mAuth.getCurrentUser().getPhoneNumber();

                        textView_name.setText(username);
                        textView_email.setText(email);
                        textView_phonenumber.setText(phonenumber);
                        progressDialog.dismiss();
                        Picasso.get().load(image).into(circleImageView);

                        if (image.equals("default")) {
                            Picasso.get().load(R.drawable.default_avatar_1).into(circleImageView);
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.hide();
                    Toast.makeText(VendorProfile.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
                    return;
                }
            });

        } catch (Exception e) {

            Toast.makeText(VendorProfile.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();

        }

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                finish();

            }
        });

    }


}
