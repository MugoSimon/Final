package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;

public class PlaceOrder extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference Buyer_databaseReference;
    private Toolbar placeOrder_toolbar;
    private Button button_placeOrder;
    private TextView textView_buyerName, textView_buyerPhone, textView_buyerStreet, textView_BuyerBuildingName, textView_changeAddress;
    private RadioGroup radioGroup;
    private ImageView placeOrder_ImageView;
    private TextView textView_itemName, textView_itemType, textView_itemTotalPrice, textView_itemQuantity, textView_totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //casting and initializing
        placeOrder_toolbar = findViewById(R.id.placeOrder_toolbar);
        button_placeOrder = findViewById(R.id.button_placeOrder);
        textView_buyerName = findViewById(R.id.placeOrder_buyerName);
        textView_buyerPhone = findViewById(R.id.placeOrder_buyerPhone);
        textView_buyerStreet = findViewById(R.id.placeOrder_Street);
        textView_BuyerBuildingName = findViewById(R.id.placeOrder_buildingName);
        textView_changeAddress = findViewById(R.id.placeOrder_useOtherAddress);
        radioGroup = findViewById(R.id.radioGroup);
        placeOrder_ImageView = findViewById(R.id.placeOrder_itemImage);
        textView_itemName = findViewById(R.id.placeOrder_itemName);
        textView_itemType = findViewById(R.id.placeOrder_itemType);
        textView_itemTotalPrice = findViewById(R.id.placeOrder_itemPrice);
        textView_itemQuantity = findViewById(R.id.placeOrder_itemQuantity);
        textView_totalPrice = findViewById(R.id.placeOrder_priceThereDown);

        setSupportActionBar(placeOrder_toolbar);
        getSupportActionBar().setTitle("Place Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_kurudinyuma);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radiobutton_Mpesa:
                        button_placeOrder.setEnabled(true);
                        String mpesa_used;
                        break;

                    case R.id.radiobutton_Delivery:

                        break;
                }
            }
        });


        textView_changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent change_intent = new Intent(getApplicationContext(), ChangeLocation.class);
                startActivity(change_intent);
                textView_buyerStreet.setText(null);
                textView_BuyerBuildingName.setText(null);

                String new_buildingName = getIntent().getStringExtra("buildingName");
                String new_address = getIntent().getStringExtra("address");
                textView_buyerStreet.setText(new_address);
                textView_BuyerBuildingName.setText(new_buildingName);
            }
        });


        button_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            String itemName = getIntent().getStringExtra("export_name");
            final String itemImage = getIntent().getStringExtra("export_image");
            String itemPrice = getIntent().getStringExtra("export_price");
            String itemType = getIntent().getStringExtra("export_type");
            String itemQuantity = getIntent().getStringExtra("export_quantity");

            textView_itemName.setText(itemName);
            textView_itemType.setText(itemType);
            textView_totalPrice.setText(itemPrice);
            textView_itemQuantity.setText(itemQuantity);
            textView_itemTotalPrice.setText(itemPrice);
            Picasso.get().load(itemImage).networkPolicy(NetworkPolicy.OFFLINE).into(placeOrder_ImageView, new Callback() {
                @Override
                public void onSuccess() {

                    //hapo uko sawa
                }

                @Override
                public void onError(Exception e) {

                    Picasso.get().load(itemImage).into(placeOrder_ImageView);
                }
            });

            try {

                FirebaseUser current_user = mAuth.getCurrentUser();
                String user_id = current_user.getUid();
                Buyer_databaseReference = FirebaseDatabase.getInstance().getReference().child("Buyers").child(user_id);

                Buyer_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot != null) {

                            String imported_name = (String) snapshot.child("username").getValue();
                            String imported_phone = (String) snapshot.child("phone_number").getValue();
                            String imported_street = (String) snapshot.child("address").getValue();
                            String imported_building = (String) snapshot.child("building_name").getValue();

                            textView_buyerName.setText(imported_name);
                            textView_buyerPhone.setText(imported_phone);
                            textView_buyerStreet.setText(imported_street);
                            textView_BuyerBuildingName.setText(imported_building);
                        } else {

                            Toast.makeText(getApplicationContext(), "Datasnapshot empty", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        //uncertain still
                    }
                });

            } catch (DatabaseException e) {

                throw new Exception(e.getMessage());
            }

        } catch (Exception error) {

            try {
                throw new Exception(error.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
