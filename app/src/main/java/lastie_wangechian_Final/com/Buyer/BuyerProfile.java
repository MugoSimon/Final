package lastie_wangechian_Final.com.Buyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class BuyerProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView textView_name;
    private TextView textView_email;
    private TextView textView_phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buyer Profile");

        textView_name = findViewById(R.id.textview_name);
        textView_email = findViewById(R.id.textview_email);
        textView_phonenumber = findViewById(R.id.textview_phoneNumber);
        circleImageView = findViewById(R.id.buyer_image);



    }
}

