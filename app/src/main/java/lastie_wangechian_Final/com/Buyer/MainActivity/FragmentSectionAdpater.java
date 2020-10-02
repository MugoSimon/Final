package lastie_wangechian_Final.com.Buyer.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lastie_wangechian_Final.com.Buyer.Orders.MyOrdersFgm;

public class FragmentSectionAdpater extends FragmentPagerAdapter {

    //empty constructor required
    public FragmentSectionAdpater(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AvailableVendorsFgm availableVendorsFgm = new AvailableVendorsFgm();
                return availableVendorsFgm;

            case 1:
                MyOrdersFgm myOrdersFgm = new MyOrdersFgm();
                return myOrdersFgm;

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
                return "Available Vendors";

            case 1:
                return "My Orders";

            default:
                return null;

        }
    }
}
