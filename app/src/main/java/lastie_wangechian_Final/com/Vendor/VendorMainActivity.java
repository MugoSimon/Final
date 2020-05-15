package lastie_wangechian_Final.com.Vendor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import lastie_wangechian_Final.com.R;

public class VendorMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Toolbar toolbar_mainVendor;
    private TabLayout tabLayout_mainVendor;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference itemsReference = firebaseFirestore.collection("Orders");
    private OrderVendorNoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        mAuth = FirebaseAuth.getInstance();
        toolbar_mainVendor = findViewById(R.id.vendor_mainToolbar);
        tabLayout_mainVendor = findViewById(R.id.vendor_tabs);
        setSupportActionBar(toolbar_mainVendor);
        getSupportActionBar().setTitle("Mtaani Order Maji");


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        try {

            Query query = itemsReference.orderBy("time_of_order", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<OrderVendorNote> options = new FirestoreRecyclerOptions.Builder<OrderVendorNote>()
                    .setQuery(query, OrderVendorNote.class)
                    .build();

            adapter = new OrderVendorNoteAdapter(options);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    adapter.deleteItem(viewHolder.getAdapterPosition());
                }
            }).attachToRecyclerView(recyclerView);

            adapter.setOnItemClicked(new OrderVendorNoteAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DocumentSnapshot documentSnapshot, int position) {

                    String id = documentSnapshot.getId();
                    String path = documentSnapshot.getReference().getPath();

                    Intent forward_intent = new Intent(VendorMainActivity.this, VendorViewOrder.class);
                    forward_intent.putExtra("id", id);
                    forward_intent.putExtra("path", path);
                    startActivity(forward_intent);
                    Toast.makeText(VendorMainActivity.this, "path: " + path + "id: " + id, Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {

            Toast.makeText(VendorMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
