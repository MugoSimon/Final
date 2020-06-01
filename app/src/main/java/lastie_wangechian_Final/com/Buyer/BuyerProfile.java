package lastie_wangechian_Final.com.Buyer;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class BuyerProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_phonenumber;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    private Button button_next;
    ProgressDialog progressDialog;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buyer Profile");

        textView_name = findViewById(R.id.textview_name);
        textView_email = findViewById(R.id.textview_email);
        button_next = findViewById(R.id.button_next);
        textView_phonenumber = findViewById(R.id.textview_phoneNumber);
        circleImageView = findViewById(R.id.buyer_image);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Loading info");
        progressDialog.setMessage("please wait while we load your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        try {

            mAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            userID = FirebaseAuth.getInstance().getUid();

            DocumentReference documentReference = fStore.collection("Buyer").document(userID);
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

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relLayout), "Unable to load content", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(BuyerProfile.this, "Offline", Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();


                    return;
                }
            });

        } catch (Exception e) {

            Toast.makeText(BuyerProfile.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();

        }



        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BuyerMainActivity.class));
                finish();
            }
        });

    }
}

