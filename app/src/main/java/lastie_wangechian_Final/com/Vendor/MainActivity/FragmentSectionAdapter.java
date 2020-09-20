package lastie_wangechian_Final.com.Vendor.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lastie_wangechian_Final.com.Vendor.AddItems.AddItems;
import lastie_wangechian_Final.com.Vendor.VendorItems.ItemsFragment;
import lastie_wangechian_Final.com.Vendor.ViewRequestedOrders.RequestedOrders;

public class FragmentSectionAdapter extends FragmentPagerAdapter {

    public FragmentSectionAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemsFragment itemsFragmentFgm = new ItemsFragment();
                return itemsFragmentFgm;

            case 1:
                RequestedOrders requestedOrders = new RequestedOrders();
                return requestedOrders;

            case 2:
                AddItems addItems = new AddItems();
                return addItems;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "My Items";

            case 1:
                return "Requested Orders";

            case 2:
                return "Add Items";

            default:
                return null;

        }
    }
}
