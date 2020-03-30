package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lastie_wangechian_Final.com.R;

public class VendorMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Toolbar toolbar_mainVendor;
    private TabLayout tabLayout_mainVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        mAuth = FirebaseAuth.getInstance();
        toolbar_mainVendor = findViewById(R.id.vendor_mainToolbar);
        tabLayout_mainVendor = findViewById(R.id.vendor_tabs);
        setSupportActionBar(toolbar_mainVendor);
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

        Intent reverse_intent = new Intent(this, VendorSelect.class);
        startActivity(reverse_intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.vendor_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.vendor_logOut) {
/*
            mAuth.signOut();
            sendToStart();
*/
        }
        if (item.getItemId() == R.id.Add_item) {

            Intent intent = new Intent(VendorMainActivity.this, VendorAddItems.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.vendor_viewItems) {

            Intent intent = new Intent(VendorMainActivity.this, VendorViewItems.class);
            startActivity(intent);
        }

        return true;
    }
}
