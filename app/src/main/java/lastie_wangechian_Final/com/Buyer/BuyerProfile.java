package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.MainActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buyer Profile");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        textView_name = findViewById(R.id.textview_name);
        textView_email = findViewById(R.id.textview_email);
        button_next = findViewById(R.id.button_next);
        textView_phonenumber = findViewById(R.id.textview_phoneNumber);
        circleImageView = findViewById(R.id.buyer_image);

        DocumentReference documentReference = fStore.collection("Buyer").document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    String username = documentSnapshot.getString("Username");
                    String email = documentSnapshot.getString("Email");
                    String phonenumber = mAuth.getCurrentUser().getPhoneNumber();

                    textView_name.setText(username);
                    textView_email.setText(email);
                    textView_phonenumber.setText(phonenumber);


                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }
}

