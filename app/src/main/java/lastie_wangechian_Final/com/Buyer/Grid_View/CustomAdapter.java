package lastie_wangechian_Final.com.Buyer.Grid_View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lastie_wangechian_Final.com.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.myViewHolder> {

    Context mContext;
    ArrayList<Model> modelItems;

    public CustomAdapter(Context mContext, ArrayList<Model> modelItems) {
        this.mContext = mContext;
        this.modelItems = modelItems;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.single_available_item, parent, false);
        myViewHolder this_holder = new myViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "clicked", Toast.LENGTH_LONG).show();
            }
        });

        return this_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.display_itemName.setText(modelItems.get(position).getItem_name());
        holder.display_itemPrice.setText(modelItems.get(position).getItem_price());
        Picasso.get().load(modelItems.get(position).getItem_image()).into(holder.display_imageView);
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        private ImageView display_imageView;
        private TextView display_itemName;
        private TextView display_itemPrice;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            display_imageView = (ImageView) itemView.findViewById(R.id.single_itemImage);
            display_itemName = (TextView) itemView.findViewById(R.id.single_itemName);
            display_itemPrice = (TextView) itemView.findViewById(R.id.single_itemPrice);

        }
    }
}
