package lastie_wangechian_Final.com.Buyer;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class BuyerNoteAdapter extends FirestoreRecyclerAdapter<BuyerNote, BuyerNoteAdapter.BuyerNoteHolder> {

    private OnItemClickListener listener;

    public BuyerNoteAdapter(@NonNull FirestoreRecyclerOptions<BuyerNote> options) {
        super(options);
        //empty constructor
    }

    @Override
    protected void onBindViewHolder(@NonNull BuyerNoteHolder holder, int position, @NonNull BuyerNote model) {

        holder.textView_vendorName.setText(model.getVendor_name());
        holder.textView_containerName.setText(model.getContainer_name());
        holder.circleImageView.setImageURI(Uri.parse(model.getContainer_image()));
    }

    @NonNull
    @Override
    public BuyerNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_vendor,
                parent, false);

        return new BuyerNoteHolder(v);
    }

    public void setOnItemClicked(OnItemClickListener listener) {

        this.listener = listener;

    }

    public interface OnItemClickListener {

        void onItemClicked(DocumentSnapshot documentSnapshot, int position);

    }

    class BuyerNoteHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView textView_vendorName;
        TextView textView_containerName;

        public BuyerNoteHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.loaded_image);
            textView_vendorName = itemView.findViewById(R.id.loaded_vendorName);
            textView_containerName = itemView.findViewById(R.id.loaded_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {

                        listener.onItemClicked(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
}
