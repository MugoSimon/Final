package lastie_wangechian_Final.com.Vendor.VendorItems;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorAddItems;

public class ItemsFragment extends Fragment {

    String items_name, items_price, items_type, items_image;
    private View ItemFrgm;
    private DatabaseReference dbReference_items;
    private DatabaseReference db_ReferenceDelete;
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

                Intent intent = new Intent(getContext(), VendorAddItems.class);
                startActivity(intent);
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
            dbReference_items = FirebaseDatabase.getInstance().getReference("Items").child(vendor_id);
            dbReference_items.keepSynced(true);

            //kuquery the request ya database reference.
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ImportItems>()
                    .setQuery(dbReference_items, ImportItems.class)
                    .build();

            FirebaseRecyclerAdapter<ImportItems, ItemListHolder> adapter
                    = new FirebaseRecyclerAdapter<ImportItems, ItemListHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ItemListHolder holder, int position, @NonNull final ImportItems model) {

                    dbReference_items.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChildren()) {


                                items_name = (String) snapshot.child("item_name").getValue();
                                items_price = (String) snapshot.child("item_price").getValue();
                                items_type = (String) snapshot.child("item_type").getValue();
                                items_image = (String) snapshot.child("item_image").getValue();

                                holder.textView_itemName.setText(model.getItem_name());
                                holder.textView_itemPrice.setText(model.getItem_price());
                                holder.textView_itemType.setText(model.getItem_type());
                                Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(holder.imageView_itemImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        //iko sawa hapa
                                        Toast.makeText(getContext(), items_image, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                        Picasso.get().load(model.getItem_image()).into(holder.imageView_itemImage);
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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



                    items_name = model.getItem_name();
                    items_price = model.getItem_price();
                    items_type = model.getItem_type();
                    items_image = model.getItem_name();

                    holder.textView_itemName.setText(items_name);
                    holder.textView_itemPrice.setText(items_price);
                    holder.textView_itemType.setText(items_type);
                    Picasso.get().load(items_image).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView_itemImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            //its alright there
                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(model.getItem_image()).into(holder.imageView_itemImage);
                        }
                    });


                     */
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

    public class ItemListHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(my_view.getContext(), itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.delete_item) {

                                showDialogDelete();
                                //Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }

                            if (item.getItemId() == R.id.edit_item) {

                                showDialogEdit();
                                //Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }
                            return false;
                        }
                    });
                    //showing the menu
                    popupMenu.show();
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialogView();
                }
            });

        }

        private void showDialogEdit() {

            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View editDialogView = inflater.inflate(R.layout.update_dialog, null);
            dialogBuilder.setView(editDialogView);

            final TextInputLayout textInputLayoutContainerName = editDialogView.findViewById(R.id.dialog_TextInputLayoutName);
            final TextInputLayout textInputLayoutContainerPrice = editDialogView.findViewById(R.id.dialog_TextInputLayoutPrice);
            final Spinner spinnerType = editDialogView.findViewById(R.id.dialog_spinner);
            final Button buttonUpdate = editDialogView.findViewById(R.id.dialog_ButtonUpdate);

            //loading data into the alertdialog
            dialogBuilder.setTitle("Updating Item: " + items_name);

            FirebaseUser current_user = mAuth.getCurrentUser();
            String vendor_id = current_user.getUid();
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items").child(vendor_id);
            /*
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChildren()) {

                        String gotten_itemName = (String) snapshot.child("item_name").getValue();
                        String gotten_itemPrice = (String) snapshot.child("item_price").getValue();
                        String gotten_itemType = (String) snapshot.child("item_type").getValue();

                        textInputLayoutContainerName.getEditText().setText(gotten_itemName);
                        textInputLayoutContainerPrice.getEditText().setText(gotten_itemPrice);
                        spinnerType.setPrompt(gotten_itemType);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            */
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChildren()) {

                        String gotten_itemName = (String) snapshot.child("item_name").getValue();
                        String gotten_itemPrice = (String) snapshot.child("item_price").getValue();
                        String gotten_itemType = (String) snapshot.child("item_type").getValue();

                        textInputLayoutContainerName.getEditText().setText(gotten_itemName);
                        textInputLayoutContainerPrice.getEditText().setText(gotten_itemPrice);

                        String compareValue = "some value";
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.vendor_types, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(adapter);
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(compareValue);
                            spinnerType.setSelection(spinnerPosition);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!checkOnName() | !checkOnPrice() | !checkOnSpinner()) {

                        return;
                    } else {

                        final String update_itemName = textInputLayoutContainerName.getEditText().getText().toString().trim();
                        final String update_itemPrice = textInputLayoutContainerPrice.getEditText().getText().toString().trim();
                        final String update_itemType = (String) spinnerType.getSelectedItem();

                        //we need to update this input

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                snapshot.getRef().child("item_name").setValue(update_itemName);
                                snapshot.getRef().child("item_price").setValue(update_itemPrice);
                                snapshot.getRef().child("item_type").setValue(update_itemType);

                                Toast.makeText(getContext(), "Item successfully updated", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

                private boolean checkOnName() {

                    String update_itemName = textInputLayoutContainerName.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(update_itemName)) {

                        textInputLayoutContainerName.requestFocus();
                        textInputLayoutContainerName.setError("field cannot be left empty");
                        textInputLayoutContainerName.getEditText().setText(null);
                        return false;

                    } else {

                        textInputLayoutContainerName.setError(null);
                        return true;
                    }

                }

                private boolean checkOnPrice() {

                    String update_itemPrice = textInputLayoutContainerPrice.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(update_itemPrice)) {

                        textInputLayoutContainerPrice.requestFocus();
                        textInputLayoutContainerPrice.setError("field cannot be left empty");
                        textInputLayoutContainerPrice.getEditText().setText(null);
                        return false;

                    } else {

                        textInputLayoutContainerPrice.setError(null);
                        return true;
                    }
                }

                private boolean checkOnSpinner() {
                    boolean checked = spinnerType.requestFocus();
                    if (checked == false) {

                        spinnerType.requestFocus();
                        return false;
                    } else {

                        return true;
                    }
                }
            });

        }

        private void showDialogView() {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View viewDialogView = inflater.inflate(R.layout.view_dialog, null);
            dialogBuilder.setView(viewDialogView);

            final ImageView dialogimageView_ItemImage = viewDialogView.findViewById(R.id.dialogView_ItemImage);
            final TextView textView_ItemName = viewDialogView.findViewById(R.id.dialogView_ItemName);
            final TextView textView_ItemPrice = viewDialogView.findViewById(R.id.dialogView_ItemPrice);
            final TextView textView_ItemType = viewDialogView.findViewById(R.id.dialogView_ItemType);
            final Button button_back = viewDialogView.findViewById(R.id.dialogView_ButtonBack);

            final ImportItems model = new ImportItems();
            textView_ItemName.setText(model.getItem_name());
            textView_ItemPrice.setText(model.getItem_price());
            textView_ItemType.setText(model.getItem_type());
            Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(dialogimageView_ItemImage, new Callback() {
                @Override
                public void onSuccess() {
                    //ni fiu
                }

                @Override
                public void onError(Exception e) {

                    Picasso.get().load(model.getItem_image()).into(dialogimageView_ItemImage);
                }
            });

            dialogBuilder.setTitle("Viewing Item: " + items_name);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });


        }

        private void showDialogDelete() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setCancelable(false);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to delete?");

            //setting listeners for the dialog buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //delete logics hapa kutoka kwa firebase
                    FirebaseUser user = mAuth.getCurrentUser();
                    String vendor_id = user.getUid();
                    dbReference_items.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChildren()) {

                                snapshot.child("item_name").getRef().setValue(null);
                                snapshot.child("item_price").getRef().setValue(null);
                                snapshot.child("item_type").getRef().setValue(null);
                                snapshot.child("item_image").getRef().setValue(null);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

}
