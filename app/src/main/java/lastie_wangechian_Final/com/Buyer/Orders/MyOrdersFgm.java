package lastie_wangechian_Final.com.Buyer.Orders;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class MyOrdersFgm extends Fragment {


    public MyOrdersFgm() {
        // Required empty public constructor
    }

    String item_name, item_price, item_type, item_image, item_quantity, item_time;
    private RecyclerView myOrderRecycler;
    private FirebaseAuth mAuth;
    private DatabaseReference myOrderReference;
    private View myOrderView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myOrderView = inflater.inflate(R.layout.fragment_my_orders_fgm, container, false);
        myOrderRecycler = myOrderView.findViewById(R.id.fgm_recyclerViewerMyOrder);
        myOrderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return myOrderView;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userID = user.getUid();
            myOrderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Buyer").child(userID);
            myOrderReference.keepSynced(true);

            //kusaka vitu kwa database sasa
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<OrderList>()
                    .setQuery(myOrderReference, OrderList.class)
                    .build();
            FirebaseRecyclerAdapter<OrderList, myOrderViewHolder> adapter =
                    new FirebaseRecyclerAdapter<OrderList, myOrderViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final myOrderViewHolder holder, int position, @NonNull final OrderList model) {

                            myOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.hasChild("item_name")) {
                                        item_name = snapshot.getValue().toString();
                                        holder.myOrder_textViewItemName.setText(item_name);
                                    }

                                    if (snapshot.hasChild("item_type")) {
                                        item_type = snapshot.getValue().toString();
                                        holder.myOrder_textViewItemType.setText(item_type);
                                    }

                                    if (snapshot.hasChild("item_price")) {
                                        item_price = snapshot.getValue().toString();
                                        holder.myOrder_textViewItemPrice.setText(item_price);
                                    }

                                    if (snapshot.hasChild("item_quantity")) {
                                        item_quantity = snapshot.getValue().toString();
                                        holder.myOrder_textViewItemQuantity.setText(item_quantity);
                                    }

                                    if (snapshot.hasChild("time_of_order")) {
                                        item_time = snapshot.getValue().toString();
                                        holder.myOrder_textViewItemTime.setText(item_time);
                                    }

                                    if (snapshot.hasChild("item_image")) {
                                        item_image = snapshot.getValue().toString();
                                        Picasso.get().load(item_image).networkPolicy(NetworkPolicy.OFFLINE).into(holder.myOrder_imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                //twe seo
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(item_image).into(holder.myOrder_imageView);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                            holder.myOrder_textViewItemName.setText(model.getItem_name());
                            holder.myOrder_textViewItemType.setText(model.getItem_type());
                            holder.myOrder_textViewItemPrice.setText(model.getItem_price());
                            holder.myOrder_textViewItemQuantity.setText(model.getItem_quantity());
                            holder.myOrder_textViewItemTime.setText(model.getTime_of_order());
                            Picasso.get().load(model.getItem_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.myOrder_imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //ni fiu
                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(model.getItem_image()).into(holder.myOrder_imageView);
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public myOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_buyer_order,
                                    parent, false);
                            myOrderViewHolder holder = new myOrderViewHolder(view);
                            return holder;
                        }
                    };

            myOrderRecycler.setAdapter(adapter);
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

    public class myOrderViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final View itemView;
        ImageView myOrder_imageView;
        TextView myOrder_textViewItemName, myOrder_textViewItemPrice, myOrder_textViewItemQuantity, myOrder_textViewItemType, myOrder_textViewItemTime;
        View myOrder_View;

        public myOrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.itemView = itemView;

            myOrder_imageView = itemView.findViewById(R.id.myOrder_image);
            myOrder_textViewItemName = itemView.findViewById(R.id.myOrder_itemName);
            myOrder_textViewItemPrice = itemView.findViewById(R.id.myOrder_itemPrice);
            myOrder_textViewItemQuantity = itemView.findViewById(R.id.myOrder_itemQuantity);
            myOrder_textViewItemType = itemView.findViewById(R.id.myOrder_itemType);
            myOrder_textViewItemTime = itemView.findViewById(R.id.myOrder_itemTime);


        }
    }
}
