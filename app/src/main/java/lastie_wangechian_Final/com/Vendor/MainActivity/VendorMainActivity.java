package lastie_wangechian_Final.com.Vendor.MainActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.VendorSelect;

public class VendorMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Toolbar toolbar_mainVendor;
    private ViewPager mViewpager;
    private TabLayout tabLayout_mainVendor;
    private FragmentSectionAdapter fragmentSectionAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        mAuth = FirebaseAuth.getInstance();
        toolbar_mainVendor = findViewById(R.id.vendor_mainToolbar);
        tabLayout_mainVendor = findViewById(R.id.vendor_tabs);
        setSupportActionBar(toolbar_mainVendor);
        getSupportActionBar().setTitle("Mtaani Order Maji");

        //tabs
        mViewpager = findViewById(R.id.vendor_viewPager);
        fragmentSectionAdpater = new FragmentSectionAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(fragmentSectionAdpater);
        tabLayout_mainVendor.setupWithViewPager(mViewpager);
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

            mAuth.signOut();
            sendToStart();

        }
        if (item.getItemId() == R.id.Add_item) {


            Intent intent = new Intent(VendorMainActivity.this, VendorAddItems.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.vendor_viewItems) {

            //ItemsFragment fragment_viewItems = new ItemsFragment();
            //loadFragment(fragment_viewItems);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave?");

        //setting listeners for the dialog buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.create().show();
    }

}