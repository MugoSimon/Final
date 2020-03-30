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

//import com.bumptech.glide.annotation.GlideOption;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    private OnItemClickListener listener;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);

        //empty constructor
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {

        holder.textView_containerName.setText(model.getContainer_name());
        holder.textView_containerprice.setText(model.getContainer_price());
        holder.textView_vendorName.setText(model.getVendor_name());
        holder.imageView.setImageURI(Uri.parse(model.getContainer_image()));

        String usid = model.getVendor_userID();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_single_item,
                parent, false);

        return new NoteHolder(view);
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

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textView_vendorName;
        //TextView textView_vendor_UserId;
        TextView textView_imageUrl;
        TextView textView_containerName;
        TextView textView_containerprice;
        ImageView imageView;



        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textView_containerName = itemView.findViewById(R.id.loaded_itemName);
            textView_containerprice = itemView.findViewById(R.id.loaded_itemPrice);
            textView_vendorName = itemView.findViewById(R.id.loaded_vendorName);
            imageView = itemView.findViewById(R.id.loaded_image);


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
