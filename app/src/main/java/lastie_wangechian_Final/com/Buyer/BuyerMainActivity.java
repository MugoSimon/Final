package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lastie_wangechian_Final.com.R;

public class BuyerMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Toolbar toolbar_mainBuyer;
    private ViewPager mViewpager;
    private TabLayout tabLayout_mainBuyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        mAuth = FirebaseAuth.getInstance();

        toolbar_mainBuyer = findViewById(R.id.buyer_mainToolbar);
        tabLayout_mainBuyer = findViewById(R.id.buyer_tabs);
        setSupportActionBar(toolbar_mainBuyer);
        getSupportActionBar().setTitle("Mtaani Order Maji");



    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            FirebaseUser currrentUser = mAuth.getCurrentUser();
            if (currrentUser == null) {
                sendToStart();

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "You are Offline...", Toast.LENGTH_LONG).show();
        }

    }

    private void sendToStart() {

        Intent reverse_intent = new Intent(this, BuyerSelect.class);
        startActivity(reverse_intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.buyer_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.buyer_logOut) {

            mAuth.signOut();
            sendToStart();

        }


        if (item.getItemId() == R.id.buyer_orders) {

            //Intent order_intent = new Intent(BuyerMainActivity.this, MyOrders.class);
            //startActivity(order_intent);

        }
        return true;
    }
}
