package lastie_wangechian_Final.com.Vendor.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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


            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "My Items";

            case 1:
                return "Requested Orders";

            default:
                return null;

        }
    }
}
