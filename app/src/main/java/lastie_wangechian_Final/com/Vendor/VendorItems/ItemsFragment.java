package lastie_wangechian_Final.com.Vendor.VendorItems;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.HashMap;

import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorAddItems;

public class ItemsFragment extends Fragment {

    String items_name, items_price, items_type, items_image;
    ArrayList<ImportItems> importItemsList;
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
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.ccp_padding);
        ItemsRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        importItemsList = new ArrayList<>();
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
                protected void onBindViewHolder(@NonNull final ItemListHolder holder, final int position, @NonNull final ImportItems model) {

                    dbReference_items.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            importItemsList.clear();
                            int counter = 0;
                            for (DataSnapshot Items_dataSnapshot : snapshot.getChildren()) {

                                if (counter == position) {

                                    final ImportItems importItems = Items_dataSnapshot.getValue(ImportItems.class);
                                    importItemsList.add(importItems);

                                    holder.textView_itemPrice.setText(importItems.getItem_price());
                                    holder.textView_itemName.setText(importItems.getItem_name());
                                    holder.textView_inv.setText(importItems.getId());
                                    holder.textView_itemType.setText(importItems.getItem_type());
                                    Picasso.get().load(importItems.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(holder.imageView_itemImage, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {

                                            Picasso.get().load(importItems.getItem_image()).placeholder(R.drawable.no_image_found).into(holder.imageView_itemImage);
                                        }
                                    });
                                }
                                counter++;


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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

    public class ItemListHolder extends RecyclerView.ViewHolder {
        TextView textView_itemName, textView_itemPrice, textView_inv, textView_itemType;
        ImageView imageView_itemImage;
        View my_view;

        public ItemListHolder(@NonNull final View itemView) {
            super(itemView);
            my_view = itemView;

            textView_itemName = itemView.findViewById(R.id.single_itemName);
            textView_itemPrice = itemView.findViewById(R.id.single_itemPrice);
            textView_itemType = itemView.findViewById(R.id.single_itemType);
            textView_inv = itemView.findViewById(R.id.inv_imageURL);
            imageView_itemImage = itemView.findViewById(R.id.single_itemImage);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(my_view.getContext(), itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            if (item.getItemId() == R.id.edit_item) {

                                showDialogEdit();
                                //Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }

                            if (item.getItemId() == R.id.view_item) {

                                showDialogView();
                                //Toast.makeText(my_view.getContext(), "You clicked: " + item.getTitle(), Toast.LENGTH_LONG).show();
                                return true;
                            }

                            if (item.getItemId() == R.id.delete_item) {

                                showDialogDelete();
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

            //Toast.makeText(getContext(),textView_inv.getText(),Toast.LENGTH_LONG).show();
            dbReference_items.child((String) textView_inv.getText()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {

                    if (snapshot.hasChildren()) {

                        textView_ItemName.setText((String) snapshot.child("item_name").getValue());
                        textView_ItemPrice.setText((String) snapshot.child("item_price").getValue());
                        textView_ItemType.setText((String) snapshot.child("item_type").getValue());
                        Picasso.get().load((String) snapshot.child("item_image").getValue()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.no_image_found).into(dialogimageView_ItemImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                //ni fiu
                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load((String) snapshot.child("item_image").getValue()).into(dialogimageView_ItemImage);
                            }
                        });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            dialogBuilder.setTitle("Viewing Item: " + textView_ItemName.getText());
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }, 5000);
        }

        private void showDialogDelete() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setCancelable(false);
            builder.setTitle("Deleting Record: " + textView_itemName.getText());
            builder.setMessage("Are you sure you want to delete?");

            //setting listeners for the dialog buttons
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //delete logics hapa kutoka kwa firebase

                    dbReference_items.child((String) textView_inv.getText()).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(getContext(), "Record deleted", Toast.LENGTH_LONG).show();
                                }
                            });

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });

            builder.create().show();
        }

        private void showDialogEdit() {

            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View updateDialogView = inflater.inflate(R.layout.update_dialog, null);
            dialogBuilder.setView(updateDialogView);

            final TextInputLayout textInputLayout_name = updateDialogView.findViewById(R.id.dialog_TextInputLayoutName);
            final TextInputLayout textInputLayout_price = updateDialogView.findViewById(R.id.dialog_TextInputLayoutPrice);
            final Spinner spinner_typeUpdate = updateDialogView.findViewById(R.id.dialog_spinner);
            final Button buttonUpdate = updateDialogView.findViewById(R.id.dialog_ButtonUpdate);

            dbReference_items.child((String) textView_inv.getText()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {

                    if (snapshot.hasChildren()) {

                        textInputLayout_name.getEditText().setText((String) snapshot.child("item_name").getValue());
                        textInputLayout_price.getEditText().setText((String) snapshot.child("item_price").getValue());
                        String compareValue = "some value";
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.vendor_types, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_typeUpdate.setAdapter(adapter);
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(textView_itemType.getText());
                            spinner_typeUpdate.setSelection(spinnerPosition);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            dialogBuilder.setTitle("Updating Record:");
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String container_name = textInputLayout_name.getEditText().getText().toString();
                    String container_Price = textInputLayout_price.getEditText().getText().toString();

                    if (container_name.isEmpty()) {
                        textInputLayout_name.requestFocus();
                        textInputLayout_name.setError("field is empty");
                        return;
                    } else if (container_Price.isEmpty()) {

                        textInputLayout_price.requestFocus();
                        textInputLayout_price.setError("field is empty");
                        return;
                    } else {

                        textInputLayout_name.setError(null);
                        textInputLayout_price.setError(null);
                        String container_type = (String) spinner_typeUpdate.getSelectedItem();
                        updateRecord(container_name, container_Price, container_type);

                    }
                }

                private void updateRecord(String container_name, String container_price, String container_type) {

                    HashMap<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("item_name", container_name);
                    userUpdates.put("item_price", container_price);
                    userUpdates.put("item_type", container_type);
                    dbReference_items.child((String) textView_inv.getText()).updateChildren(userUpdates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(getContext(), "Record Updated", Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();
                                }
                            });
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }, 60000);
        }

    }

}