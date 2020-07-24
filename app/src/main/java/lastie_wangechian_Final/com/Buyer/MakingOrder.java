package lastie_wangechian_Final.com.Buyer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import lastie_wangechian_Final.com.R;


public class MakingOrder extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_order);

        textView = findViewById(R.id.textView_testing);

        String info = getIntent().getStringExtra("info");

        textView.setText(info);
    }
}
