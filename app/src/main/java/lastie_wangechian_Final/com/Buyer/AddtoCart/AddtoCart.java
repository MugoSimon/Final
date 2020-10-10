package lastie_wangechian_Final.com.Buyer.AddtoCart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;

public class AddtoCart extends AppCompatActivity {

    String itemName, itemPrice, itemType, vendorName, itemImage, specfic_ID;
    private Toolbar toolbar_addToCart;
    private RecyclerView recyclerView_addToCart;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, vendorReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        //casting
        toolbar_addToCart = findViewById(R.id.addToCart_toolbar);

        setSupportActionBar(toolbar_addToCart);
        getSupportActionBar().setTitle("Items On Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_kurudinyuma);

        recyclerView_addToCart = findViewById(R.id.addToCart_recycler);
        recyclerView_addToCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {

        try {
            super.onStart();
            //firebase
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String user_id = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(user_id);
            databaseReference.keepSynced(true);

            //querying the database
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CartList>()
                    .setQuery(databaseReference, CartList.class)
                    .build();

            FirebaseRecyclerAdapter<CartList, AddtoCartHolder> adapter =
                    new FirebaseRecyclerAdapter<CartList, AddtoCartHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final AddtoCartHolder holder, int position, @NonNull final CartList model) {

                            try {
                                specfic_ID = getRef(position).getKey();
                                databaseReference.child(specfic_ID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        holder.addToCart_textViewItemName.setText(model.getExport_name());
                                        holder.addToCart_textViewItemType.setText(model.getExport_type());
                                        holder.addToCart_textViewItemPrice.setText(model.getExport_price());
                                        Picasso.get().load(model.getExport_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.addToCart_imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                //hapa tiwe seo
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(model.getExport_image()).into(holder.addToCart_imageView);
                                            }
                                        });

                                        vendorReference = FirebaseDatabase.getInstance().getReference().child("Vendors").child(model.getVendor_id());
                                        vendorReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.hasChild("username")) {
                                                    vendorName = snapshot.child("username").getValue().toString();
                                                    holder.addToCart_textViewVendorName.setText(vendorName);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } catch (DatabaseException db_error) {

                                Toast.makeText(getApplicationContext(), db_error.getMessage(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        @NonNull
                        @Override
                        public AddtoCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.single_added_to_cart, parent, false);

                            return new AddtoCartHolder(view);
                        }
                    };

            recyclerView_addToCart.setAdapter(adapter);
            adapter.startListening();

        } catch (UnsupportedOperationException erro) {

            throw new UnsupportedOperationException(erro.getMessage());

        } catch (Exception e) {

            try {
                throw new Exception(e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public class AddtoCartHolder extends RecyclerView.ViewHolder {

        ImageView addToCart_imageView;
        TextView addToCart_textViewItemName, addToCart_textViewItemPrice,
                addToCart_textViewItemType, addToCart_textViewVendorName;
        Button addToCart_buttonDelete;
        private View addedToCart_view;

        public AddtoCartHolder(@NonNull View itemView) {
            super(itemView);
            this.addedToCart_view = itemView;

            addToCart_imageView = itemView.findViewById(R.id.added_itemImage);
            addToCart_textViewItemName = itemView.findViewById(R.id.added_itemName);
            addToCart_textViewItemPrice = itemView.findViewById(R.id.added_itemPrice);
            addToCart_textViewItemType = itemView.findViewById(R.id.added_itemType);
            addToCart_textViewVendorName = itemView.findViewById(R.id.added_vendorName);
            addToCart_buttonDelete = itemView.findViewById(R.id.added_buttonDelete);

            addToCart_buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //done
                    databaseReference.child(specfic_ID).removeValue();
                }
            });
        }
    }
}
