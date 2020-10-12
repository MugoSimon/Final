package lastie_wangechian_Final.com.Buyer.WhileOrdering;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import lastie_wangechian_Final.com.Buyer.Orders.MyOrdersFgm;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.ViewRequestedOrders.RequestedOrders;

import static lastie_wangechian_Final.com.R.id.radiobutton_Mpesa;

public class PlaceOrder extends AppCompatActivity implements ChangeLocation.ChangeLocationListener {

    String imported_name, imported_phone, imported_street, imported_building, totalPrice_plus_deliveryFee, vendor_id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference Buyer_databaseReference, PlaceOrder_databaseReference, PlaceOrder_buyerReference;
    private Toolbar placeOrder_toolbar;
    private Button button_placeOrder;
    private TextView textView_buyerName, textView_buyerPhone, textView_buyerStreet, textView_BuyerBuildingName, textView_changeAddress;
    private RadioGroup radioGroup, radioGroupFee;
    private ImageView placeOrder_ImageView;
    private TextView textView_itemName, textView_itemType, textView_itemprice, textView_itemQuantity, textView_totalPrice;
    String itemName, itemImage, itemType, itemQuantity, itemPrice;


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
        radioGroupFee = findViewById(R.id.radioGroup_fee);
        placeOrder_ImageView = findViewById(R.id.placeOrder_itemImage);
        textView_itemName = findViewById(R.id.placeOrder_itemName);
        textView_itemType = findViewById(R.id.placeOrder_itemType);
        textView_itemprice = findViewById(R.id.placeOrder_itemPrice);
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
                    case radiobutton_Mpesa:
                        button_placeOrder.setEnabled(true);
                        mpesaUsed();
                        break;

                    case R.id.radiobutton_Delivery:
                        button_placeOrder.setEnabled(true);
                        lipaOnDelivery();
                        break;
                }
            }
        });


        textView_changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressChanged();

            }
        });

        button_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (lipaOnDelivery()) {

                        //time and date
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        String time = format.format(calendar.getTime());
                        String currentDate = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());

                        //continue as normal
                        String username = textView_buyerName.getText().toString().trim();
                        String item_name = textView_itemName.getText().toString().trim();
                        String item_type = textView_itemType.getText().toString().trim();
                        String item_price = textView_totalPrice.getText().toString().trim();
                        String item_quantity = textView_itemQuantity.getText().toString().trim();
                        String phone = textView_buyerPhone.getText().toString().trim();
                        String item_image = getIntent().getStringExtra("export_image");
                        String street = textView_buyerStreet.getText().toString().trim();
                        String building = textView_BuyerBuildingName.getText().toString().trim();
                        String item_address = street + ", " + building;
                        String time_of_order = time + ", " + currentDate;

                        PlaceOrder_databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Vendor").child(vendor_id);
                        HashMap<String, String> hashMap_placeOrder = new HashMap<>();
                        hashMap_placeOrder.put("username", username);
                        hashMap_placeOrder.put("item_name", item_name);
                        hashMap_placeOrder.put("item_type", item_type);
                        hashMap_placeOrder.put("item_price", item_price);
                        hashMap_placeOrder.put("item_quantity", item_quantity);
                        hashMap_placeOrder.put("phone", phone);
                        hashMap_placeOrder.put("item_image", item_image);
                        hashMap_placeOrder.put("item_address", item_address);
                        hashMap_placeOrder.put("time_of_order", time_of_order);

                        PlaceOrder_databaseReference.push().setValue(hashMap_placeOrder)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        try {

                                            if (task.isSuccessful()) {

                                                notifyVendor();
                                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), RateUs.class));
                                                finish();

                                            } else {

                                            }

                                        } catch (UnsupportedOperationException cption) {

                                            throw new UnsupportedOperationException(cption.getMessage());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        });

                        FirebaseUser current_user = mAuth.getCurrentUser();
                        String currentUser_ID = current_user.getUid();
                        PlaceOrder_buyerReference = FirebaseDatabase.getInstance().getReference("Orders").child("Buyer").child(currentUser_ID);
                        HashMap<String, String> hashMap_buyer_placeOrder = new HashMap<>();
                        hashMap_buyer_placeOrder.put("username", username);
                        hashMap_buyer_placeOrder.put("item_name", item_name);
                        hashMap_buyer_placeOrder.put("item_type", item_type);
                        hashMap_buyer_placeOrder.put("item_price", item_price);
                        hashMap_buyer_placeOrder.put("item_quantity", item_quantity);
                        hashMap_buyer_placeOrder.put("phone", phone);
                        hashMap_buyer_placeOrder.put("item_image", item_image);
                        hashMap_buyer_placeOrder.put("item_address", item_address);
                        hashMap_buyer_placeOrder.put("time_of_order", time_of_order);

                        PlaceOrder_buyerReference.push().setValue(hashMap_buyer_placeOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Also successful", Toast.LENGTH_LONG).show();
                                    notifyBuyer();
                                    startActivity(new Intent(getApplicationContext(), RateUs.class));
                                    finish();
                                }
                            }
                        });

                    }

                    if (mpesaUsed()) {

                        //load the mpesa code on lipa na mpesa thingy
                    }
                } catch (UnsupportedOperationException er) {

                    throw new UnsupportedOperationException(er.getMessage());
                }

            }
        });

    }

    public void addressChanged() {
        ChangeLocation changeLocation = new ChangeLocation();
        changeLocation.show(getSupportFragmentManager(), "changing the location and address");

    }

    private void notifyBuyer() {

        int notificationID = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.deliv_1)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.deliv_1))
                .setContentTitle("You just Shopped")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("click view to visit site."))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //intents and hope they do work
        Intent intent = new Intent(getApplicationContext(), MyOrdersFgm.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.addAction(R.drawable.ic_menu_view, "VIEW", pendingIntent);

        //set a message notification
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        //call notification manager to build and deliver the notification to the OS
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //android eight plus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelID = "Your Channel";
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        notificationManager.notify(notificationID, builder.build());
    }

    private void notifyVendor() {

        int notificationID = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.deliv_1)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.deliv_1))
                .setContentTitle("You just Shopped")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("click view to visit site."))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //intents and hope they do work
        Intent intent = new Intent(getApplicationContext(), RequestedOrders.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.addAction(R.drawable.ic_menu_view, "VIEW", pendingIntent);

        //set a message notification
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        //call notification manager to build and deliver the notification to the OS
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //android eight plus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelID = "Your Channel";
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }
        notificationManager.notify(notificationID, builder.build());

    }

    private boolean lipaOnDelivery() {
        return true;
    }

    private boolean mpesaUsed() {
        return true;
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            String itemName = getIntent().getStringExtra("export_name");
            final String itemImage = getIntent().getStringExtra("export_image");
            int itemPrice = Integer.parseInt(getIntent().getStringExtra("export_price"));
            String itemType = getIntent().getStringExtra("export_type");
            String itemQuantity = getIntent().getStringExtra("export_quantity");
            vendor_id = getIntent().getStringExtra("vendor_id");

            textView_itemName.setText(itemName);
            textView_itemType.setText(itemType);
            textView_itemQuantity.setText(itemQuantity);
            textView_itemprice.setText(String.valueOf(itemPrice));
            int ttp = itemPrice + 50;
            totalPrice_plus_deliveryFee = String.valueOf(ttp);
            textView_totalPrice.setText(totalPrice_plus_deliveryFee);
            Picasso.get().load(itemImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(placeOrder_ImageView, new Callback() {
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

                        //try .exists()
                        if (snapshot.hasChildren()) {

                            imported_name = (String) snapshot.child("username").getValue();
                            imported_phone = (String) snapshot.child("phone_number").getValue();
                            imported_street = (String) snapshot.child("address").getValue();
                            imported_building = (String) snapshot.child("building_name").getValue();

                            textView_buyerName.setText(imported_name);
                            textView_buyerPhone.setText(imported_phone);
                            textView_buyerStreet.setText(imported_street);
                            textView_BuyerBuildingName.setText(imported_building);
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

        } catch (UnsupportedOperationException ror) {

            throw new UnsupportedOperationException(ror.getMessage());

        } catch (Exception error) {

            try {
                throw new Exception(error.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //startActivity(new Intent(getApplicationContext(),));
    }

    @Override
    public void applyText(String new_address, String new_buildingName) {

        textView_buyerStreet.setText(new_address);
        textView_BuyerBuildingName.setText(new_buildingName);
    }
}
