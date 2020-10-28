package lastie_wangechian_Final.com.Vendor.ViewRequestedOrders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import lastie_wangechian_Final.com.Buyer.Orders.MyOrdersFgm;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorMainActivity;

public class ViewOrder extends AppCompatActivity {

    ProgressDialog progressDialog;
    String buyer_name, buyer_phone, item_name, item_image, item_price, item_type, item_quantity, item_deliveryAddress, item_time;
    private Toolbar viewOrder_toolbar;
    private TextView textView_buyerName, textView_buyerPhone, textView_itemName, textView_itemPrice, textView_itemType, textView_itemQuatity, textView_itemDeliveryAddress, textView_itemTime;
    private Button button_Approve, button_Disapprove;
    private ImageView viewOrder_Imageviewer;
    private FirebaseAuth mAuth;
    private DatabaseReference ApprovedRerefence;
    private DatabaseReference requestedOrdReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        mAuth = FirebaseAuth.getInstance();

        //casting
        textView_buyerName = findViewById(R.id.viewOrder_buyerName);
        textView_buyerPhone = findViewById(R.id.viewOrder_buyerPhone);
        textView_itemName = findViewById(R.id.viewOrder_ItemName);
        textView_itemPrice = findViewById(R.id.viewOrder_ItemPrice);
        textView_itemType = findViewById(R.id.viewOrder_ItemType);
        textView_itemQuatity = findViewById(R.id.viewOrder_ItemQuantity);
        textView_itemDeliveryAddress = findViewById(R.id.viewOrder_ItemDeliveryAddress);
        textView_itemTime = findViewById(R.id.viewOrder_ItemTime);
        viewOrder_Imageviewer = findViewById(R.id.viewOrder_Imageview);
        button_Approve = findViewById(R.id.viewOrder_buttonApprove);
        button_Disapprove = findViewById(R.id.viewOrder_buttonDisapprove);

        viewOrder_toolbar = findViewById(R.id.viewOrder_toolbar);
        setSupportActionBar(viewOrder_toolbar);
        getSupportActionBar().setTitle("View Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_kurudinyuma);
        progressDialog = new ProgressDialog(this);

        //buttons
        button_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_Disapprove.setEnabled(false);
                progressDialog.setTitle("Saving this");
                progressDialog.setMessage("please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                FirebaseUser current_user = mAuth.getCurrentUser();
                String user_id = current_user.getUid();
                ApprovedRerefence = FirebaseDatabase.getInstance().getReference().child("Approved Orders").child(user_id);

                HashMap<String, String> hashMap_approvedOrder = new HashMap<>();
                hashMap_approvedOrder.put("buyer_name", buyer_name);
                hashMap_approvedOrder.put("buyer_phone", buyer_phone);
                hashMap_approvedOrder.put("item_name", item_name);
                hashMap_approvedOrder.put("item_image", item_image);
                hashMap_approvedOrder.put("item_price", item_price);
                hashMap_approvedOrder.put("item_type", item_type);
                hashMap_approvedOrder.put("item_quantity", item_quantity);
                hashMap_approvedOrder.put("item_deliveryAddress", item_deliveryAddress);
                hashMap_approvedOrder.put("item_time", item_time);

                ApprovedRerefence.push().setValue(hashMap_approvedOrder)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                try {

                                    if (task.isSuccessful()) {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), VendorMainActivity.class);
                                        notify_approval();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    } else {

                                        progressDialog.hide();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                } catch (final DatabaseException e) {

                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.viewOrder_layout), "Task Isn't Successfully", Snackbar.LENGTH_LONG)
                                            .setAction("View Details", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    snackbar.show();
                                    return;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull final Exception e) {

                        Snackbar snackbar = Snackbar.make(findViewById(R.id.viewOrder_layout), "Database Exception Found", Snackbar.LENGTH_LONG)
                                .setAction("View Details", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        snackbar.show();
                        return;
                    }
                });

            }
        });

        button_Disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_Approve.setEnabled(false);
                Toast.makeText(getApplicationContext(), "YOU CANCELLED AN ORDER", Toast.LENGTH_LONG).show();
                Intent this_intent = new Intent(getApplicationContext(), RequestedOrders.class);
                this_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(this_intent);

            }
        });

    }

    private void notify_approval() {

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

    @Override
    protected void onStart() {
        try {
            super.onStart();

            buyer_name = getIntent().getStringExtra("order_BuyerName");
            buyer_phone = getIntent().getStringExtra("order_Phone");
            item_name = getIntent().getStringExtra("order_ItemName");
            item_price = getIntent().getStringExtra("order_ItemPrice");
            item_type = getIntent().getStringExtra("order_ItemType");
            item_quantity = getIntent().getStringExtra("order_ItemQuantity");
            item_deliveryAddress = getIntent().getStringExtra("order_ItemAddress");
            item_time = getIntent().getStringExtra("order_ItemTime_of_Order");
            item_image = getIntent().getStringExtra("order_ItemImageUrl");

            //Toast.makeText(getApplicationContext(), buyer_name +item_deliveryAddress +item_price, Toast.LENGTH_SHORT).show();

            textView_buyerName.setText(buyer_name);
            textView_buyerPhone.setText(buyer_phone);
            textView_itemName.setText(item_name);
            textView_itemPrice.setText(item_price);
            textView_itemType.setText(item_type);
            textView_itemQuatity.setText(item_quantity);
            textView_itemDeliveryAddress.setText(item_deliveryAddress);
            textView_itemTime.setText(item_time);
            Picasso.get().load(item_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(viewOrder_Imageviewer, new Callback() {
                @Override
                public void onSuccess() {
                    //hakna shida ikiload
                }

                @Override
                public void onError(Exception e) {

                    //load here
                    Picasso.get().load(item_image).into(viewOrder_Imageviewer);
                }
            });

        } catch (RuntimeException er) {

            throw new RuntimeException(er.getMessage());
        }
    }
}
