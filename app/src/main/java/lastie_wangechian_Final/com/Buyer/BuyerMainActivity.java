package lastie_wangechian_Final.com.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import lastie_wangechian_Final.com.R;

public class BuyerMainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private Toolbar toolbar_mainBuyer;
    private ViewPager mViewpager;
    private TabLayout tabLayout_mainBuyer;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference itemsReference = firebaseFirestore.collection("Items");
    private BuyerNoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);


        mAuth = FirebaseAuth.getInstance();

        toolbar_mainBuyer = findViewById(R.id.buyer_mainToolbar);
        tabLayout_mainBuyer = findViewById(R.id.buyer_tabs);
        setSupportActionBar(toolbar_mainBuyer);
        getSupportActionBar().setTitle("Mtaani Order Maji");

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        try {

            Query query = itemsReference.orderBy("vendor_name", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<BuyerNote> options = new FirestoreRecyclerOptions.Builder<BuyerNote>()
                    .setQuery(query, BuyerNote.class)
                    .build();

            adapter = new BuyerNoteAdapter(options);

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClicked(new BuyerNoteAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(DocumentSnapshot documentSnapshot, int position) {

                    BuyerNote note = documentSnapshot.toObject(BuyerNote.class);
                    String id = documentSnapshot.getId();
                    String path = documentSnapshot.getReference().getPath();

                    Intent forward_intent = new Intent(BuyerMainActivity.this, MakeOrder.class);
                    forward_intent.putExtra("id", id);
                    forward_intent.putExtra("path", path);
                    startActivity(forward_intent);
                    //Toast.makeText(BuyerMainActivity.this, "path: "+path+ "id: "+id, Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {

            Toast.makeText(BuyerMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            FirebaseUser currrentUser = mAuth.getCurrentUser();
            if (currrentUser == null) {
                sendToStart();

            } else {
                adapter.startListening();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "You are Offline...", Toast.LENGTH_LONG).show();
        }

        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
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
