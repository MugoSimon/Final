package lastie_wangechian_Final.com.Buyer.WhileOrdering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import lastie_wangechian_Final.com.Buyer.MainActivity.BuyerMainActivity;
import lastie_wangechian_Final.com.R;

public class RateUs extends AppCompatActivity {

    private Toolbar rateUs_toolbar;
    private RatingBar ratingBar;
    private Button button_submit;
    private TextView textView_thanking, textView_heading, textView_ratings;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(userID);

        //casting
        rateUs_toolbar = findViewById(R.id.rate_toolbar);
        ratingBar = findViewById(R.id.rating_us);
        button_submit = findViewById(R.id.button_submit);
        textView_heading = findViewById(R.id.textview_heading);
        textView_ratings = findViewById(R.id.textview_ratings);
        textView_thanking = findViewById(R.id.textview_thanking);

        //toolbar
        setSupportActionBar(rateUs_toolbar);
        getSupportActionBar().setTitle("Rate Us");

        //progressDialog
        progressDialog = new ProgressDialog(this);

        //ratingBar
        ratingBar.animate();
        ratingBar.requestFocus();


        //button
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    final float ratings = ratingBar.getRating();
                    progressDialog.setTitle("Concluding...");
                    progressDialog.setMessage("Thank you very much :)");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    saveRating(ratings);

                } catch (UnsupportedOperationException error) {

                    throw new UnsupportedOperationException(error.getMessage());

                } catch (Exception e) {

                    try {
                        throw new Exception(e.getMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

    }

    private void saveRating(float ratings) {

        HashMap<String, String> hashMap_this = new HashMap<>();
        hashMap_this.put("ratings", String.valueOf(ratings));
        databaseReference.push().setValue(hashMap_this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    Toast.makeText(RateUs.this, "Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BuyerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage().trim(), Toast.LENGTH_LONG).show();
                    return;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
