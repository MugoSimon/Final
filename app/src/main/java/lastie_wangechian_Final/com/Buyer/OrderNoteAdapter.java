package lastie_wangechian_Final.com.Buyer;

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

import lastie_wangechian_Final.com.R;

public class OrderNoteAdapter extends FirestoreRecyclerAdapter<OrderNote, OrderNoteAdapter.OrderNoteHolder> {


    public OrderNoteAdapter(@NonNull FirestoreRecyclerOptions<OrderNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderNoteHolder holder, int position, @NonNull OrderNote model) {

        holder.textView_containerName.setText(model.getContainer_name());
        holder.textView_noOfContainers.setText(model.getNo_of_containers());
        holder.textView_totalAmount.setText(model.getTotal_amount());
        holder.textView_vendorName.setText(model.getVendor_name());
        holder.textView_timeOfOrder.setText(String.valueOf(model.getTime_of_order()));
        //holder.textView_timeOfOrder.setText(model.getTime_of_order().toString());
        holder.Img_containerImage.setImageURI(Uri.parse(model.getContainer_image()));
    }

    @NonNull
    @Override
    public OrderNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_single_order, parent,
                false);

        return new OrderNoteHolder(view);
    }

    public void deleteItem(int position) {

        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class OrderNoteHolder extends RecyclerView.ViewHolder {

        ImageView Img_containerImage;
        TextView textView_containerName;
        TextView textView_noOfContainers;
        TextView textView_totalAmount;
        TextView textView_vendorName;
        TextView textView_timeOfOrder;


        public OrderNoteHolder(@NonNull View itemView) {
            super(itemView);

            Img_containerImage = itemView.findViewById(R.id.loaded_image);
            textView_containerName = itemView.findViewById(R.id.loaded_itemName);
            textView_noOfContainers = itemView.findViewById(R.id.loaded_numberOfContainers);
            textView_totalAmount = itemView.findViewById(R.id.loaded_itemPrice);
            textView_vendorName = itemView.findViewById(R.id.loaded_vendorName);
            textView_timeOfOrder = itemView.findViewById(R.id.loaded_time);

        }
    }
}
