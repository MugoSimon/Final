package lastie_wangechian_Final.com.Buyer.MainActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.Buyer.Grid_View.AvailableItems;
import lastie_wangechian_Final.com.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableVendorsFgm extends Fragment {

    private View AvailableVendors;
    private RecyclerView recyclerView_vendorList;
    private DatabaseReference vendorListRef;
    private FirebaseAuth mAuth;
    private TextView textView_offline;
    private ImageView imageView;

    public AvailableVendorsFgm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AvailableVendors = inflater.inflate(R.layout.fragment_available_vendors_fgm, container, false);

        recyclerView_vendorList = AvailableVendors.findViewById(R.id.fgm_vendorList);
        textView_offline = AvailableVendors.findViewById(R.id.textView_offline);
        imageView = AvailableVendors.findViewById(R.id.imageView);

        recyclerView_vendorList.setLayoutManager(new LinearLayoutManager(getContext()));

        //recyclerView_vendorList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //route to the database in firebase

        return AvailableVendors;
    }

    @Override
    public void onStart() {

        super.onStart();
        //not necessary of offline vs online

        vendorListRef = FirebaseDatabase.getInstance().getReference().child("Vendors");
        vendorListRef.keepSynced(true);

        //kuquery the request ya database reference.
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<VendorList>()
                .setQuery(vendorListRef, VendorList.class)
                .build();

        FirebaseRecyclerAdapter<VendorList, VendorListHolder> adapter =
                new FirebaseRecyclerAdapter<VendorList, VendorListHolder>(options) {

                    //sourcing info from database and bind them to their respective holders
                    @Override
                    protected void onBindViewHolder(@NonNull final VendorListHolder holder, final int position, @NonNull VendorList model) {

                        final String user_id = getRef(position).getKey();
                        if (user_id.equals(null)) {

                            textView_offline.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);

                        }

                        vendorListRef.child(user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild("vendor_image")) {

                                    final String profile_image = (String) dataSnapshot.child("vendor_image").getValue();
                                    String vendor_username = (String) dataSnapshot.child("username").getValue();
                                    String vendor_location = (String) dataSnapshot.child("location").getValue();

                                    holder.vendor_username.setText(vendor_username);
                                    holder.vendor_location.setText(vendor_location);
                                    Picasso.get().load(profile_image).placeholder(R.drawable.default_avatar_3).networkPolicy(NetworkPolicy.OFFLINE).into(holder.vendor_image, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {

                                            Picasso.get().load(profile_image).placeholder(R.drawable.default_avatar_3).into(holder.vendor_image);
                                        }
                                    });

                                } else {

                                    String vendor_username = (String) dataSnapshot.child("username").getValue();
                                    String vendor_location = (String) dataSnapshot.child("location").getValue();

                                    holder.vendor_username.setText(vendor_username);
                                    holder.vendor_location.setText(vendor_location);
                                }

                                holder.my_view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String Us_id = getRef(position).getKey();
                                        Intent intent = new Intent(getContext(), AvailableItems.class);
                                        intent.putExtra("user_id", Us_id);
                                        startActivity(intent);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });


                    }

                    //basically the single item ya available vendor ndio iingie kwa viewpager ya fragment.
                    @NonNull
                    @Override
                    public VendorListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int viewType) {

                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.available_vendor, viewGroup, false);

                        VendorListHolder viewholder = new VendorListHolder(view);
                        return viewholder;
                    }

                };

        recyclerView_vendorList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class VendorListHolder extends RecyclerView.ViewHolder {

        TextView vendor_username, vendor_location;
        CircleImageView vendor_image;
        View my_view;

        public VendorListHolder(@NonNull final View itemView) {

            super(itemView);
            my_view = itemView;

            vendor_username = itemView.findViewById(R.id.loaded_vendorName);
            vendor_location = itemView.findViewById(R.id.loaded_location);
            vendor_image = itemView.findViewById(R.id.loaded_image);

        }


    }

}
