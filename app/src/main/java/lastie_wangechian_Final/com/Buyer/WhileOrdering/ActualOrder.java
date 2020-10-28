package lastie_wangechian_Final.com.Buyer.WhileOrdering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import lastie_wangechian_Final.com.Buyer.MainActivity.BuyerMainActivity;
import lastie_wangechian_Final.com.R;

public class ActualOrder extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    String export_price, export_name, export_type, export_image, vendor_id;
    private TextView textView_price, textView_name, textView_type;
    private NumberPicker numberPicker;
    private ImageView imageView;
    private TextView textView_totalPrice;
    private RatingBar ratingBar;
    private Button button_AddtoCart, button_BuyNow;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_order);

        //firebase and its requirement.
        mAuth = FirebaseAuth.getInstance();

        textView_price = findViewById(R.id.textView_price);
        textView_name = findViewById(R.id.textView_name);
        textView_type = findViewById(R.id.textView_type);
        textView_totalPrice = findViewById(R.id.textView_totalPrice);
        numberPicker = findViewById(R.id.numberPicker);
        imageView = findViewById(R.id.imageView_ItemImage);
        ratingBar = findViewById(R.id.ratingBar);
        button_AddtoCart = findViewById(R.id.button_addCart);
        button_BuyNow = findViewById(R.id.button_buyNow);

        //numberpicker reading
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);

        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        numberPicker.setOnValueChangedListener(this);
        //ratingBar and its content
        Double d = (Math.random()) * (5 - 2) + 2;
        float ratings = d.floatValue();
        ratingBar.animate();
        ratingBar.setRating(ratings);

        //buttons
        button_BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //action when its clicked
                try {

                    String total_price_here = textView_totalPrice.getText().toString();
                    int number_pickerValue = numberPicker.getValue();
                    String value_ofNumberPicker = String.valueOf(number_pickerValue);

                    Intent intent = new Intent(getApplicationContext(), PlaceOrder.class);
                    intent.putExtra("export_name", export_name);
                    intent.putExtra("export_image", export_image);
                    intent.putExtra("export_price", total_price_here);
                    intent.putExtra("export_type", export_type);
                    intent.putExtra("export_quantity", value_ofNumberPicker);
                    intent.putExtra("vendor_id", vendor_id);
                    startActivity(intent);

                    //Toast.makeText(getApplicationContext(), textView_totalPrice.getText(), Toast.LENGTH_LONG).show();

                } catch (RuntimeException errro) {

                    throw new RuntimeException(errro.getMessage());
                }

            }
        });

        button_AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add to cart activity
                try {

                    int number_listener = Integer.parseInt(String.valueOf(NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL));

                    if (number_listener == 0) {

                        button_AddtoCart.setBackgroundColor(getResources().getColor(R.color.faded));
                        Toast.makeText(getApplicationContext(), "Choose the quantity at numberpicker", Toast.LENGTH_LONG).show();
                        numberPicker.requestFocus();

                    } else {

                        button_AddtoCart.setBackgroundColor(getResources().getColor(R.color.not_faded));
                        button_AddtoCart.setEnabled(true);
                        FirebaseUser current_user = mAuth.getCurrentUser();
                        String user_id = current_user.getUid();
                        String buyer_price = textView_totalPrice.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Cart").child(user_id);
                        String list_id = mDatabase.push().getKey();

                        HashMap<String, String> cart_hashMap = new HashMap<>();
                        cart_hashMap.put("export_name", export_name);
                        cart_hashMap.put("export_image", export_image);
                        cart_hashMap.put("export_price", buyer_price);
                        cart_hashMap.put("export_type", export_type);
                        cart_hashMap.put("list_id", list_id);
                        cart_hashMap.put("vendor_id", vendor_id);

                        mDatabase.push().setValue(cart_hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getApplicationContext(), "added to cart", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), BuyerMainActivity.class));
                                        button_AddtoCart.setEnabled(false);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        button_AddtoCart.setEnabled(false);
                                        try {
                                            throw new Exception(e.getMessage());

                                        } catch (Exception ex) {

                                            ex.printStackTrace();
                                        }
                                    }
                                });

                        //startActivity(new Intent(getApplicationContext(), PlaceOrder.class));

                    }

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


    @Override
    protected void onStart() throws RuntimeException {
        try {
            super.onStart();

            export_name = getIntent().getStringExtra("export_name");
            export_image = getIntent().getStringExtra("export_image");
            export_price = getIntent().getStringExtra("export_price");
            export_type = getIntent().getStringExtra("export_type");
            vendor_id = getIntent().getStringExtra("vendor_id");

            textView_name.setText(export_name);
            textView_type.setText(export_type);
            textView_price.setText(export_price);
            Picasso.get().load(export_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    //its good here
                }

                @Override
                public void onError(Exception e) {

                    Picasso.get().load(export_image).placeholder(R.drawable.no_image_found).into(imageView);
                }
            });

            //Toast.makeText(getApplicationContext(), export_name, Toast.LENGTH_LONG).show();

        } catch (UnsupportedOperationException errro) {

            throw new RuntimeException(errro.getMessage());

        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        try {

            int this_price = Integer.parseInt(export_price);
            int total_price_cal = this_price * newVal;

            textView_totalPrice.setText(String.valueOf(total_price_cal));

        } catch (ArithmeticException error) {

            throw new ArithmeticException(error.getMessage());

        } catch (Exception e) {

            try {
                throw new Exception(e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }
}
