package lastie_wangechian_Final.com.Buyer.MainActivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lastie_wangechian_Final.com.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableVendorsFgm extends Fragment {

    private View AvailableVendors;
    private RecyclerView recyclerView_vendorList;

    public AvailableVendorsFgm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AvailableVendors = inflater.inflate(R.layout.fragment_available_vendors_fgm, container, false);

        recyclerView_vendorList = AvailableVendors.findViewById(R.id.fgm_vendorList);
        recyclerView_vendorList.setLayoutManager(new LinearLayoutManager(getContext()));

        return AvailableVendors;
    }

    @Override
    public void onStart() {

        super.onStart();

    }
}
