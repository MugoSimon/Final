package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;

public class MakeOrder extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference itemsReference = firebaseFirestore.collection("Items");
    private Toolbar toolbar;
    private ImageView Imgcontainer_image;
    private TextView Txtcontainer_name;
    private TextView Txtcontainer_price;
    private FloatingActionButton button_minus;
    private FloatingActionButton button_plus;
    private EditText editText;
    private Button button_orderNow;
    private TextView TxtImage_url, TxtVendor_name, textView_totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        toolbar = findViewById(R.id.toolbar_makeOrder);
        Imgcontainer_image = findViewById(R.id.container_image);
        Txtcontainer_name = findViewById(R.id.container_name);
        Txtcontainer_price = findViewById(R.id.container_price);
        TxtImage_url = findViewById(R.id.image_url);
        TxtVendor_name = findViewById(R.id.vendor_name);
        textView_totalPrice = findViewById(R.id.textview_totalAmount);
        button_minus = findViewById(R.id.floatingActionButton_minus);
        button_plus = findViewById(R.id.floatingActionButton_plus);
        button_orderNow = findViewById(R.id.button_makeOrder);

        String vendor_name = TxtVendor_name.getText().toString().trim();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(vendor_name + "'s Available order");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {

                    String input = editText.getText().toString().trim();
                    int final_input = Integer.parseInt(input) - 1;
                    editText.setText(final_input);

                    String container_price = Txtcontainer_price.getText().toString().trim();
                    int price_of_container = Integer.parseInt(container_price);
                    int total_amount = final_input * price_of_container;
                    textView_totalPrice.setText(total_amount);


                } else {
                    return;
                }
            }
        });

        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {

                    String input = editText.getText().toString().trim();
                    int final_input = Integer.parseInt(input) + 1;
                    editText.setText(final_input);

                    String container_price = Txtcontainer_price.getText().toString().trim();
                    int price_of_container = Integer.parseInt(container_price);
                    int total_amount = final_input * price_of_container;
                    textView_totalPrice.setText(total_amount);

                } else {

                    return;
                }
            }
        });

        button_orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (validateInput()) {

                        String user_input = editText.getText().toString().trim();
                        String container_name = Txtcontainer_name.getText().toString().trim();
                        String container_image = TxtImage_url.getText().toString().trim();
                        String total_amount = textView_totalPrice.getText().toString().trim();

                        Intent forward_intent = new Intent(MakeOrder.this, OrderStatement.class);
                        forward_intent.putExtra("user_input", user_input);
                        forward_intent.putExtra("container_name", container_name);
                        forward_intent.putExtra("container_image", container_image);
                        forward_intent.putExtra("total_amount", total_amount);
                        startActivity(forward_intent);

                    } else {
                        return;
                    }

                } catch (Exception e) {

                    Toast.makeText(MakeOrder.this, "Error when button order clicked: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validateInput() {

        String input = editText.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            editText.requestFocus();
            editText.setError("input required");
            return false;

        } else if (input.length() > 2 || input.equals(0)) {
            editText.requestFocus();
            editText.setError("invalid input");
            return false;

        } else {
            editText.setError(null);
            return true;

        }
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            //getting the path and id from the previous activity
            String vendor_id = getIntent().getStringExtra("id");
            String vendor_path = getIntent().getStringExtra("path");

            try {

                DocumentReference itemsReference = firebaseFirestore.collection("Items").document(vendor_id);
                itemsReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        try {

                            if (documentSnapshot.exists()) {

                                String container_image = documentSnapshot.getString("container_image");
                                String container_name = documentSnapshot.getString("container_name");
                                String container_price = documentSnapshot.getString("container_price");
                                String vendor_name = documentSnapshot.getString("vendor_name");


                                Txtcontainer_name.setText(container_name);
                                Txtcontainer_price.setText(container_price);
                                TxtImage_url.setText(container_image);
                                TxtVendor_name.setText(vendor_name);
                                Picasso.get().load(container_image).into(Imgcontainer_image);

                            }
                        } catch (Exception e) {

                            Toast.makeText(MakeOrder.this, "Error while retrieving data from firestore: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MakeOrder.this, "Firestore failed: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {

                Toast.makeText(MakeOrder.this, "Error while retrieving data from firestore: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {

            Toast.makeText(MakeOrder.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
        }


    }
}
