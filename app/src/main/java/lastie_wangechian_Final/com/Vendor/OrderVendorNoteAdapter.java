package lastie_wangechian_Final.com.Vendor;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import lastie_wangechian_Final.com.R;

public class OrderVendorNoteAdapter extends FirestoreRecyclerAdapter<OrderVendorNote, OrderVendorNoteAdapter.OrderVendorNoteHolder> {

    private OnItemClickListener listener;

    public OrderVendorNoteAdapter(@NonNull FirestoreRecyclerOptions<OrderVendorNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderVendorNoteHolder holder, int position, @NonNull OrderVendorNote model) {

        //hiyo buyer_name method cjui kama itafanya ju buyer name haiko kwa hiyo collection

        holder.textView_containerName.setText(model.getContainer_name());
        holder.textView_noOfContainers.setText(model.getNo_of_containers());
        holder.textView_buyerName.setText(model.getBuyer_name());
        holder.textView_totalAmount.setText(model.getTotal_amount());
        holder.textView_timeOfOrder.setText(String.valueOf(model.getTime_of_order()));
        holder.Img_containerImage.setImageURI(Uri.parse(model.getContainer_image()));

    }

    @NonNull
    @Override
    public OrderVendorNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_single_order, parent,
                false);

        return new OrderVendorNoteHolder(view);
    }

    public void deleteItem(int position) {

        getSnapshots().getSnapshot(position).getReference().delete();

    }

    public void setOnItemClicked(OnItemClickListener listener) {

        this.listener = listener;

    }

    public interface OnItemClickListener {

        void onItemClicked(DocumentSnapshot documentSnapshot, int position);

    }

    class OrderVendorNoteHolder extends RecyclerView.ViewHolder {

        ImageView Img_containerImage;
        TextView textView_containerName;
        TextView textView_noOfContainers;
        TextView textView_totalAmount;
        TextView textView_buyerName;
        TextView textView_timeOfOrder;

        public OrderVendorNoteHolder(@NonNull View itemView) {
            super(itemView);

            Img_containerImage = itemView.findViewById(R.id.ImageView_image);
            textView_containerName = itemView.findViewById(R.id.loaded_location);
            textView_noOfContainers = itemView.findViewById(R.id.loaded_numberOfContainers);
            textView_totalAmount = itemView.findViewById(R.id.loaded_totalPrice);
            textView_buyerName = itemView.findViewById(R.id.loaded_buyerName);
            textView_timeOfOrder = itemView.findViewById(R.id.loaded_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //have to create another interface to implement the item-onclick by first checking the position
                    int position = getAdapterPosition();

                    //check whether the item was previously deleted
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClicked(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
}

///

