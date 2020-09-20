package lastie_wangechian_Final.com.Vendor.VendorItems;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;

public class ItemsFragment extends Fragment {

    String items_name, items_price, items_type, items_image;
    private View ItemFrgm;
    private DatabaseReference dbReference_items;
    private RecyclerView ItemsRecyclerView;
    private FirebaseAuth mAuth;
    private FloatingActionButton floatinButton;

    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ItemFrgm = inflater.inflate(R.layout.fragment_items, container, false);
        ItemsRecyclerView = ItemFrgm.findViewById(R.id.fgm_vendorItemList);
        floatinButton = ItemFrgm.findViewById(R.id.add_button);
        ItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        floatinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo mpeleke kwa add item fragment
            }
        });

        return ItemFrgm;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser current_user = mAuth.getCurrentUser();
            String vendor_id = current_user.getUid();
            dbReference_items = FirebaseDatabase.getInstance().getReference().child("Items").child(vendor_id);
            dbReference_items.keepSynced(true);

            //kuquery the request ya database reference.
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ImportItems>()
                    .setQuery(dbReference_items, ImportItems.class)
                    .build();

            FirebaseRecyclerAdapter<ImportItems, ItemListHolder> adapter
                    = new FirebaseRecyclerAdapter<ImportItems, ItemListHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ItemListHolder holder, int position, @NonNull final ImportItems model) {

                    /*
                    dbReference_items.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                            if (snapshot.hasChild("item_name")) {

                                items_name = snapshot.getValue().toString();
                                holder.textView_itemName.setText(items_name);
                            }

                            if (snapshot.hasChild("item_price")) {

                                items_price = snapshot.getValue().toString();
                                holder.textView_itemPrice.setText(items_price);
                            }

                            if (snapshot.hasChild("item_type")) {

                                items_type = snapshot.getValue().toString();
                                holder.textView_itemType.setText(items_type);
                            }

                            if (snapshot.hasChild("item_image")) {

                                items_image = snapshot.getValue().toString();
                                Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView_itemImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        //iko sawa hapa
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                        Picasso.get().load(items_image).into(holder.imageView_itemImage);
                                    }
                                });
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


                     */
                    holder.textView_itemName.setText(model.getItem_name());
                    holder.textView_itemPrice.setText(model.getItem_price());
                    holder.textView_itemType.setText(model.getItem_type());
                    Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView_itemImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            //its alright there
                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(model.getItem_image()).into(holder.imageView_itemImage);
                        }
                    });

                }

                @NonNull
                @Override
                public ItemListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                    View view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.single_itemfragment, viewGroup, false);

                    ItemListHolder itemListHolder = new ItemListHolder(view);
                    return itemListHolder;
                }
            };

            ItemsRecyclerView.setAdapter(adapter);
            adapter.startListening();

        } catch (RuntimeException e) {

            throw new RuntimeException(e.getMessage());
        }


    }

    public static class ItemListHolder extends RecyclerView.ViewHolder {
        TextView textView_itemName, textView_itemPrice, textView_itemType;
        ImageView imageView_itemImage;
        View my_view;

        public ItemListHolder(@NonNull final View itemView) {
            super(itemView);
            my_view = itemView;

            textView_itemName = itemView.findViewById(R.id.single_itemName);
            textView_itemPrice = itemView.findViewById(R.id.single_itemPrice);
            textView_itemType = itemView.findViewById(R.id.single_itemType);
            imageView_itemImage = itemView.findViewById(R.id.single_itemImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(my_view.getContext(), itemView);
                    //now inflate the pop-up menu using the popup menu
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

                    //registering popup with onMenuClickListener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.delete_item) {

                                //TOdo fix the actual menu to thr respective item
                                Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }
                            if (item.getItemId() == R.id.view_item) {

                                //TOdo fix the actual menu to thr respective item
                                Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }
                            if (item.getItemId() == R.id.edit_item) {

                                //TOdo fix the actual menu to thr respective item
                                Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }
                            return false;
                        }
                    });

                    //showing the menu
                    popupMenu.show();
                }
            });
        }

    }
}
