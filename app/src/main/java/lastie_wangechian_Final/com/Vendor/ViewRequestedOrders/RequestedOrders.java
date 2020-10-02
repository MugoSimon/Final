package lastie_wangechian_Final.com.Vendor.ViewRequestedOrders;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import lastie_wangechian_Final.com.R;

public class RequestedOrders extends Fragment {

    String order_BuyerName, order_ItemName, order_BuyerPhone, order_ItemImage, order_ItemPrice, order_ItemQuantity, order_ItemAddress, order_ItemType, order_ItemTime_of_Order;
    private ImageView frg_ImageView;
    private TextView textView_frgOffline;
    private RecyclerView recyclerView;
    private View View_RequestOrderFrg;
    private DatabaseReference requestedOrdReference;
    private FirebaseAuth mAuth;

    public RequestedOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View_RequestOrderFrg = inflater.inflate(R.layout.fragment_requested_orders, container, false);

        recyclerView = View_RequestOrderFrg.findViewById(R.id.fgm_recyclerViewerOrder);
        frg_ImageView = View_RequestOrderFrg.findViewById(R.id.ord_ImageView);
        textView_frgOffline = View_RequestOrderFrg.findViewById(R.id.ord_textView_offline);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return View_RequestOrderFrg;
    }

    @Override
    public void onStart() {
        try {
            super.onStart();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser current_user = mAuth.getCurrentUser();
            String vendor_id = current_user.getUid();
            requestedOrdReference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Vendor").child(vendor_id);
            requestedOrdReference.keepSynced(true);

            //kuquery kutoka database
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<OrderItems>()
                    .setQuery(requestedOrdReference, OrderItems.class)
                    .build();

            FirebaseRecyclerAdapter<OrderItems, RequestOrderHolder> adapter =
                    new FirebaseRecyclerAdapter<OrderItems, RequestOrderHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final RequestOrderHolder holder, int position, @NonNull final OrderItems model) {

                            requestedOrdReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    if (snapshot.hasChild("item_name")) {

                                        order_ItemName = snapshot.getValue().toString();
                                        holder.ord_TextView_itemName.setText(order_ItemName);
                                    }
                                    if (snapshot.hasChild("phone")) {

                                        order_BuyerPhone = snapshot.getValue().toString();
                                        holder.ord_TextView_phone.setText(order_BuyerPhone);
                                    }
                                    if (snapshot.hasChild("username")) {

                                        order_BuyerName = snapshot.getValue().toString();
                                        holder.ord_TextView_buyerName.setText(order_BuyerName);
                                    }
                                    if (snapshot.hasChild("item_type")) {

                                        order_ItemType = snapshot.getValue().toString();
                                        holder.ord_TextView_itemType.setText(order_ItemType);
                                    }
                                    if (snapshot.hasChild("item_price")) {

                                        order_ItemPrice = snapshot.getValue().toString();
                                        holder.ord_TextView_itemPrice.setText(order_ItemPrice);
                                    }
                                    if (snapshot.hasChild("item_quantity")) {

                                        order_ItemQuantity = snapshot.getValue().toString();
                                        holder.ord_TextView_itemQuantity.setText(order_ItemQuantity);
                                    }
                                    if (snapshot.hasChild("item_address")) {

                                        order_ItemAddress = snapshot.getValue().toString();
                                        holder.ord_TextView_itemAddress.setText(order_ItemAddress);
                                    }
                                    if (snapshot.hasChild("time_of_order")) {

                                        order_ItemTime_of_Order = snapshot.getValue().toString();
                                        holder.ord_TextView_Time.setText(order_ItemTime_of_Order);
                                    }
                                    if (snapshot.hasChild("item_image")) {

                                        Picasso.get().load(order_ItemImage).networkPolicy(NetworkPolicy.OFFLINE).into(holder.ord_ImageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                //hii iko sawa
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(order_ItemImage).into(holder.ord_ImageView);
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

                            holder.ord_TextView_buyerName.setText(model.getUsername());
                            holder.ord_TextView_phone.setText(model.getPhone());
                            holder.ord_TextView_itemName.setText(model.getItem_name());
                            holder.ord_TextView_itemPrice.setText(model.getItem_price());
                            holder.ord_TextView_itemQuantity.setText(model.getItem_quantity());
                            holder.ord_TextView_itemType.setText(model.getItem_type());
                            holder.ord_TextView_itemAddress.setText(model.getItem_address());
                            holder.ord_TextView_Time.setText(model.getTime_of_order());
                            Picasso.get().load(model.getItem_image()).into(holder.ord_ImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //hamna shaka hapa

                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(model.getItem_image()).into(holder.ord_ImageView);
                                }
                            });

                        }

                        @NonNull
                        @Override
                        public RequestOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int viewType) {

                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_requested_order,
                                    viewGroup, false);
                            RequestOrderHolder holder = new RequestOrderHolder(view);
                            return holder;
                        }
                    };

            recyclerView.setAdapter(adapter);
            adapter.startListening();

        } catch (RuntimeException error) {

            throw new RuntimeException(error.getMessage());
        }
    }

    public class RequestOrderHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final View itemView;
        ImageView ord_ImageView;
        TextView ord_TextView_buyerName, ord_TextView_itemName,
                ord_TextView_itemQuantity, ord_TextView_itemPrice, ord_TextView_itemType,
                ord_TextView_itemAddress, ord_TextView_Time, ord_TextView_phone;
        View request_OrderView;


        public RequestOrderHolder(@NonNull final View itemView) {
            super(itemView);
            this.itemView = request_OrderView;

            ord_ImageView = itemView.findViewById(R.id.ord_ImageView);
            ord_TextView_buyerName = itemView.findViewById(R.id.buyer_orderName);
            ord_TextView_phone = itemView.findViewById(R.id.buyer_orderPhone);
            ord_TextView_itemName = itemView.findViewById(R.id.buyer_orderItemName);
            ord_TextView_itemQuantity = itemView.findViewById(R.id.buyer_orderItemQuantity);
            ord_TextView_itemPrice = itemView.findViewById(R.id.buyer_orderItemPrice);
            ord_TextView_itemType = itemView.findViewById(R.id.buyer_orderItemType);
            ord_TextView_Time = itemView.findViewById(R.id.buyer_orderItemTime);
            ord_TextView_itemAddress = itemView.findViewById(R.id.buyer_orderItemAddress);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent Intent_see_order = new Intent(v.getContext(), ViewOrder.class);
                    Intent_see_order.putExtra("order_ItemImage", order_ItemImage);
                    Intent_see_order.putExtra("order_BuyerName", order_BuyerName);
                    Intent_see_order.putExtra("order_Phone", order_BuyerPhone);
                    Intent_see_order.putExtra("order_ItemImage", order_ItemImage);
                    Intent_see_order.putExtra("order_ItemName", order_ItemName);
                    Intent_see_order.putExtra("order_ItemPrice", order_ItemPrice);
                    Intent_see_order.putExtra("order_ItemType", order_ItemType);
                    Intent_see_order.putExtra("order_ItemQuantity", order_ItemQuantity);
                    Intent_see_order.putExtra("order_ItemAddress", order_ItemAddress);
                    Intent_see_order.putExtra("order_ItemTime_of_Order", order_ItemTime_of_Order);

                    //todo check if its best this way
                    //itemView.setBackgroundColor(getResources().getColor(R.color.faded));
                    //itemView.setEnabled(false);

                    startActivity(Intent_see_order);
                }
            });


        }
    }
}
