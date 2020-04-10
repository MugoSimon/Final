package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import lastie_wangechian_Final.com.R;

public class VendorViewOrder extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference itemsReference = firebaseFirestore.collection("Orders");
    CollectionReference approvedReference = firebaseFirestore.collection("Appproved");
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textView_containerName;
    private TextView textView_price;
    private TextView textView_noOfcontainers;
    private TextView textView_buyerName;
    private TextView textView_time;
    private TextView textView_streetName;
    private TextView textView_buildingName;
    private Button button_approve;
    private Button button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_view_order);

        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.Img_containerImage);
        textView_containerName = findViewById(R.id.textView_containerName);
        textView_price = findViewById(R.id.textView_price);
        textView_noOfcontainers = findViewById(R.id.textView_numberContainers);
        textView_buyerName = findViewById(R.id.textView_buyerName);
        textView_streetName = findViewById(R.id.textView_streetName);
        textView_buildingName = findViewById(R.id.textView_buildingName);
        textView_time = findViewById(R.id.textView_time);
        button_approve = findViewById(R.id.button_approve);
        button_cancel = findViewById(R.id.button_disapprove);

        setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle(buyer_name + "'s Available order");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String container_name = textView_containerName.getText().toString().trim();
                    String buyer_name = textView_buyerName.getText().toString().trim();
                    String no_of_containers = textView_noOfcontainers.getText().toString().trim();
                    String total_amount = textView_price.getText().toString().trim();
                    String building_name = textView_buildingName.getText().toString().trim();
                    String street_name = textView_streetName.getText().toString().trim();
                    String time_of_approval;

                    Map<String, String> approved_items = new HashMap<>();
                    approved_items.put("container_name", container_name);
                    approved_items.put("buyer_name", buyer_name);
                    approved_items.put("no_of_containers", no_of_containers);
                    approved_items.put("total_amount", total_amount);
                    approved_items.put("building_name", building_name);
                    approved_items.put("street_name", street_name);
                    //approved_items.put("time_of_approval",time_of_approval);

                    approvedReference.add(approved_items).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                            finish();
                            //TODO apply notification of the buyer that the order has been approved.
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(VendorViewOrder.this, "Approved Reference got problems: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {

                    Toast.makeText(VendorViewOrder.this, "Button approve has got problems: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            String vendor_id = getIntent().getStringExtra("id");
            String vendor_path = getIntent().getStringExtra("path");
            try {

                DocumentReference itemsReference = firebaseFirestore.collection("Items").document(vendor_id);
                itemsReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {

                            if (documentSnapshot.exists()) {

                                String street_name = documentSnapshot.getString("street_name");
                                String building_name = documentSnapshot.getString("building_name");
                                String container_name = documentSnapshot.getString("container_name");
                                String container_image = documentSnapshot.getString("container_image");
                                String no_of_containers = documentSnapshot.getString("no_of_containers");
                                String total_amount = documentSnapshot.getString("total_amount");
                                String additional_info = documentSnapshot.getString("additional_info");
                                String time = documentSnapshot.getString("time_of_order");

                                textView_buildingName.setText(building_name);
                                textView_containerName.setText(container_name);
                                textView_noOfcontainers.setText(no_of_containers);
                                textView_price.setText(total_amount);
                                textView_streetName.setText(street_name);
                                textView_time.setText(time);
                                Picasso.get().load(container_image).into(imageView);

                            }
                        } catch (Exception e) {

                            Toast.makeText(VendorViewOrder.this, "DocumentSnapShot doesn't exists: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(VendorViewOrder.this, "Error listening to the documentReference: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception e) {

                Toast.makeText(VendorViewOrder.this, "Error opening the whole collection: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {

            Toast.makeText(VendorViewOrder.this, "Error onStart: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();

        }

    }
}
