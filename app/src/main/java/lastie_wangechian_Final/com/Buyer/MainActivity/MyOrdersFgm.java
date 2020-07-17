package lastie_wangechian_Final.com.Buyer.MainActivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import lastie_wangechian_Final.com.R;

public class MyOrdersFgm extends Fragment {


    public MyOrdersFgm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders_fgm, container, false);
    }

}
