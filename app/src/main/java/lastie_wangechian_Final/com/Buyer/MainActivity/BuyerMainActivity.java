package lastie_wangechian_Final.com.Buyer.MainActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lastie_wangechian_Final.com.Buyer.AddtoCart.AddtoCart;
import lastie_wangechian_Final.com.Buyer.BuyerSelect;
import lastie_wangechian_Final.com.R;

public class BuyerMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Toolbar toolbar_mainBuyer;
    private ViewPager mViewpager;
    private TabLayout tabLayout_mainBuyer;
    private FragmentSectionAdpater fragmentSectionAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //toolbar and its content
        toolbar_mainBuyer = findViewById(R.id.buyer_mainToolbar);
        tabLayout_mainBuyer = findViewById(R.id.buyer_tabs);
        setSupportActionBar(toolbar_mainBuyer);
        getSupportActionBar().setTitle("Mtaani Order Maji");

        //getSupportFragmentManager().beginTransaction().add(R.id.fgm_vendorList, new AvailableVendorsFgm()).commit();

        //tabs
        mViewpager = findViewById(R.id.buyer_viewPager);
        fragmentSectionAdpater = new FragmentSectionAdpater(getSupportFragmentManager());
        mViewpager.setAdapter(fragmentSectionAdpater);
        tabLayout_mainBuyer.setupWithViewPager(mViewpager);

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

            Snackbar snackbar = Snackbar.make(findViewById(R.id.relLayout), "Unable to load content", Snackbar.LENGTH_LONG)
                    .setAction("View Details", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getApplicationContext(), "You are Offline...", Toast.LENGTH_LONG).show();
                        }
                    });
            snackbar.show();

        }


    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    private void sendToStart() {

        Intent reverse_intent = new Intent(this, BuyerSelect.class);
        reverse_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        if (item.getItemId() == R.id.added_to_cart) {

            Intent cart_intent = new Intent(BuyerMainActivity.this, AddtoCart.class);
            startActivity(cart_intent);

        }

        if (item.getItemId() == R.id.buyer_orders) {

            //MyOrdersFgm fragment_viewOrders = new MyOrdersFgm();
            //loadFragmentOrder(fragment_viewOrders);

        }

        if (item.getItemId() == R.id.available_vendors) {

            //AvailableVendorsFgm fragment_viewVendors = new AvailableVendorsFgm();
            //loadFragmentVendor(fragment_viewVendors);
        }

        return true;
    }

    //todo still this
    /*
    private void loadFragmentVendor(AvailableVendorsFgm fragment_viewVendors) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fgm_vendorList, fragment_viewVendors);
        transaction.commit();


    }

    private void loadFragmentOrder(MyOrdersFgm fragment_viewOrders) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.buyer_viewPager, fragment_viewOrders);
        transaction.commit();


    }

*/
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
                finishAndRemoveTask();
                System.exit(0);

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
