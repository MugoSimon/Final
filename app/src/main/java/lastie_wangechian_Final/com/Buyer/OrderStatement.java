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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import lastie_wangechian_Final.com.R;

public class OrderStatement extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    CollectionReference documentReference;
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textView_containerName;
    private TextView textView_noOfContainer;
    private TextView textView_totalPrice;
    private EditText editText_streetName;
    private EditText editText_buildingName;
    private EditText editText_additionalInfo;
    private Button button_cashOnDelivery;
    private Button button_lipaNaMpesa;
    private Button button_cancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_statement);

        toolbar = findViewById(R.id.toolbar_orderStatement);
        imageView = findViewById(R.id.imageview_statement);
        textView_containerName = findViewById(R.id.container_name);
        textView_noOfContainer = findViewById(R.id.number_ofContainers);
        textView_totalPrice = findViewById(R.id.total_price);
        editText_streetName = findViewById(R.id.street_name);
        editText_buildingName = findViewById(R.id.building_name);
        editText_additionalInfo = findViewById(R.id.addittional_info);
        button_cashOnDelivery = findViewById(R.id.button_cashOnDelivery);
        button_lipaNaMpesa = findViewById(R.id.button_lipaNaMpesa);
        button_cancelOrder = findViewById(R.id.cancel_order);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Statement");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (validateBuilding() | validateStreet()) {


                        methodSaveOrder();
                    } else {

                        return;
                    }

                } catch (Exception e) {
                    Toast.makeText(OrderStatement.this, "Error while button cash on delivery cliked " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                }
            }
        });

        button_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(OrderStatement.this, "you have cancelled you order", Toast.LENGTH_LONG).show();
                //TODO add alertDialog for confirmation on cancel
                startActivity(new Intent(getApplicationContext(), BuyerMainActivity.class));
                finish();
            }
        });
    }

    private void methodSaveOrder() {

        try {

            String street_name = editText_streetName.getText().toString().trim();
            String building_name = editText_buildingName.getText().toString().trim();
            String container_name = textView_containerName.getText().toString().trim();
            String container_image = getIntent().getStringExtra("container_image");
            String no_of_containers = textView_noOfContainer.getText().toString().trim();
            String total_amount = textView_totalPrice.getText().toString().trim();
            String additional_info = editText_additionalInfo.getText().toString().trim();


            documentReference = fStore.collection("Orders");
            Map<String, String> buyer_order = new HashMap<>();
            buyer_order.put("street_name", street_name);
            buyer_order.put("building_name", building_name);
            buyer_order.put("container_name", container_name);
            buyer_order.put("container_image", container_image);
            buyer_order.put("no_of_containers", no_of_containers);
            buyer_order.put("total_amount", total_amount);
            buyer_order.put("additional_info", additional_info);
            // buyer_order.put("time_of_order",time_of_order);


            documentReference.add(buyer_order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    startActivity(new Intent(getApplicationContext(), BuyerOrders.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(OrderStatement.this, "Error storing buyer's order in firestore: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {

            Toast.makeText(OrderStatement.this, "Error opening database" + e.getMessage().trim(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            String container_name = getIntent().getStringExtra("container_name");
            String container_image = getIntent().getStringExtra("container_image");
            String total_amount = getIntent().getStringExtra("total_amount");
            String user_input = getIntent().getStringExtra("user_input");

            textView_containerName.setText(container_name);
            Picasso.get().load(container_image).into(imageView);
            textView_noOfContainer.setText(user_input);
            textView_totalPrice.setText(total_amount);

        } catch (Exception e) {

            Toast.makeText(OrderStatement.this, "Error loading data: " + e.getMessage().trim(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean validateStreet() {

        String street_name = editText_streetName.getText().toString().trim();

        if (TextUtils.isEmpty(street_name)) {

            editText_streetName.requestFocus();
            editText_streetName.setError("street name required");
            return false;

        } else if (street_name.length() <= 5) {

            editText_streetName.requestFocus();
            editText_streetName.setError("kindly indicate with renown road/street");
            return false;

        } else {

            editText_streetName.setError(null);
            return true;
        }
    }

    public boolean validateBuilding() {

        String building_name = editText_buildingName.getText().toString().trim();

        if (TextUtils.isEmpty(building_name)) {

            editText_buildingName.requestFocus();
            editText_buildingName.setError("name of the building is necessary");
            return false;
        } else {

            editText_buildingName.setError(null);
            return true;
        }
    }
}
