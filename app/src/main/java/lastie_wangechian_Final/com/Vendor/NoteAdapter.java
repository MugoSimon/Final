package lastie_wangechian_Final.com.Vendor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);

        //empty constructor
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {

        holder.textView_containerName.setText(model.getContainer_name());
        holder.textView_containerprice.setText(model.getContainer_price());
        holder.textView_vendorName.setText(model.getVendor_name());
        holder.textView_imageUrl.setText(model.getImageURl());
        //Picasso.get().load(image).into(circleImageView);
        //String image_url = (String) holder.textView_imageUrl.getText();
        //Picasso.get().load(image_url).into(holder.circleImageView_containerImage);
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

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textView_vendorName;
        //TextView textView_vendor_UserId;
        TextView textView_imageUrl;
        TextView textView_containerName;
        TextView textView_containerprice;
        CircleImageView circleImageView_containerImage;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);


            textView_containerName = itemView.findViewById(R.id.loaded_itemName);
            textView_containerprice = itemView.findViewById(R.id.loaded_itemPrice);
            textView_vendorName = itemView.findViewById(R.id.loaded_vendorName);
            circleImageView_containerImage = itemView.findViewById(R.id.loaded_image);


        }
    }
}
