package lastie_wangechian_Final.com.Vendor.VendorItems;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        //add top margin only item to avoid double spacing
        if (parent.getChildLayoutPosition(view) == 0) {

            outRect.top = 0;
        } else {
            outRect.top = 0;
        }
    }
}
