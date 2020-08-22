package lastie_wangechian_Final.com.Buyer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import lastie_wangechian_Final.com.R;

public class ActualOrder extends AppCompatActivity {

    private TextView textView_price, textView_name, textView_type;
    private NumberPicker numberPicker;
    private ImageView imageView;
    private TextView textView_totalPrice;
    private RatingBar ratingBar;
    private Button button_AddtoCart, button_BuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_order);

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

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                int quantity = picker.getValue();

                //textView_totalPrice.setText(oldVal);

            }
        });

        //ratingBar and its content
        //todo generate random float values for the rating bar
        Double d = (Math.random()) * (5 - 2) + 2;
        float ratings = d.floatValue();
        ratingBar.animate();
        ratingBar.setRating(ratings);

        //buttons
        button_BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //action when its clicked
            }
        });

        button_AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add to cart activity
            }
        });

    }


    @Override
    protected void onStart() {
        try {
            super.onStart();

            String export_name = getIntent().getStringExtra("export_name");
            String export_image = getIntent().getStringExtra("export_image");
            String export_price = getIntent().getStringExtra("export_price");
            String export_type = getIntent().getStringExtra("export_type");

            textView_name.setText(export_name);
            textView_type.setText(export_type);
            textView_price.setText(export_price);

            Toast.makeText(getApplicationContext(), export_image, Toast.LENGTH_LONG).show();

        } catch (RuntimeException errro) {

            return;
        }
    }
}
