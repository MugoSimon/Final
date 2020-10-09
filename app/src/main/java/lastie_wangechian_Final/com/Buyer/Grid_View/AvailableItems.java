package lastie_wangechian_Final.com.Buyer.Grid_View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.Buyer.WhileOrdering.ActualOrder;
import lastie_wangechian_Final.com.R;

public class AvailableItems extends AppCompatActivity {

    String[] item_types = {"water bottle", "5l jerry can", "10l jerry can", "20l jerry can", "40l jerry can", "50l skyplast", "75l skyplast", "100l skyplast", "water truck"};
    String export_price, export_image, export_name, export_type;
    private DatabaseReference db_ItemsRefer;
    private RecyclerView availableItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_items);

        //casting
        availableItemsRecyclerView = findViewById(R.id.gridLayout_recycler);
        availableItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    protected void onStart() {

        try {
            super.onStart();

            //firebase
            String user_id = getIntent().getStringExtra("user_id");
            Toast.makeText(AvailableItems.this, user_id, Toast.LENGTH_LONG).show();

            db_ItemsRefer = FirebaseDatabase.getInstance().getReference("Items").child(user_id);
            db_ItemsRefer.keepSynced(true);

            //FirebaseRecyclerOptions
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>()
                    .setQuery(db_ItemsRefer, Model.class)
                    .build();

            FirebaseRecyclerAdapter<Model, myViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Model, myViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final myViewHolder holder, int position, @NonNull final Model model) {

                            //try fetching from the database
                            db_ItemsRefer.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.hasChildren()) {

                                        export_image = (String) model.getItem_image();
                                        export_name = (String) model.getItem_name();
                                        export_price = (String) model.getItem_price();
                                        export_type = (String) model.getItem_type();

                                        holder.display_itemName.setText(model.getItem_name());
                                        holder.display_itemPrice.setText(model.getItem_price());
                                        Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.display_imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                                //nothing happens since its successful...
                                                Toast.makeText(getApplicationContext(), export_image, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(model.getItem_image()).into(holder.display_imageView);
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
/*
                            db_ItemsRefer.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                                    if (dataSnapshot.hasChild("item_image")){

                                        export_image = dataSnapshot.getValue().toString();
                                        Picasso.get().load(export_image).networkPolicy(NetworkPolicy.OFFLINE).into(holder.display_imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                                //nothing happens since its successful...
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(export_image).into(holder.display_imageView);
                                            }
                                        });
                                    }

                                    if (dataSnapshot.hasChild("item_name")){

                                        export_name = dataSnapshot.getValue().toString();
                                        holder.display_itemName.setText(export_name);
                                    }

                                    if (dataSnapshot.hasChild("item_price")){

                                        export_price = dataSnapshot.getValue().toString();
                                        holder.display_itemPrice.setText(export_price);
                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }


                            });


                                //done kule juu

                            //export the variables to next intent
                            export_name = model.getItem_name();
                            export_price = model.getItem_price();
                            export_image = model.getItem_image();
                            export_name = model.getItem_type();

                            holder.display_itemName.setText(model.getItem_name());
                            holder.display_itemPrice.setText(model.getItem_price());
                            Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.display_imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                    //nothing happens since its successful...
                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(model.getItem_image()).into(holder.display_imageView);
                                }
                            });
                            */
                        }

                        @NonNull
                        @Override
                        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.single_available_item, parent, false);

                            return new myViewHolder(view);
                        }
                    };


            availableItemsRecyclerView.setAdapter(adapter);
            adapter.startListening();

        } catch (DatabaseException error) {

            Toast.makeText(AvailableItems.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            return;

        } catch (RuntimeException runtime_error) {

            Toast.makeText(AvailableItems.this, runtime_error.getMessage().toString(), Toast.LENGTH_LONG).show();
            return;
        }

    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private ImageView display_imageView;
        private TextView display_itemName;
        private TextView display_itemPrice;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            display_imageView = itemView.findViewById(R.id.single_itemImage);
            display_itemName = itemView.findViewById(R.id.single_itemName);
            display_itemPrice = itemView.findViewById(R.id.single_itemPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), ActualOrder.class);

                    String user_id = getIntent().getStringExtra("user_id");
                    Toast.makeText(AvailableItems.this, user_id, Toast.LENGTH_LONG).show();
                    intent.putExtra("export_name", export_name);
                    intent.putExtra("export_image", export_image);
                    intent.putExtra("export_price", export_price);
                    intent.putExtra("export_type", export_type);
                    intent.putExtra("vendor_id", user_id);

                    startActivity(intent);
                }
            });

        }
    }

}
