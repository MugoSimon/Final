package lastie_wangechian_Final.com.Buyer.WhileOrdering;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import lastie_wangechian_Final.com.R;

public class ChangeLocation extends AppCompatDialogFragment {

    private TextInputLayout textInputLayout_address;
    private TextInputLayout textInputLayout_buildingName;
    private Button buttonUpdate;
    private ChangeLocationListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_location, null);

        builder.setView(view)
                .setTitle("Changing Location");

        textInputLayout_address = view.findViewById(R.id.dialog_TextInputLayoutAddress);
        textInputLayout_buildingName = view.findViewById(R.id.dialog_TextInputLayoutPrice);
        buttonUpdate = view.findViewById(R.id.changeLocation_button);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_address = textInputLayout_address.getEditText().getText().toString();
                String new_buildingName = textInputLayout_buildingName.getEditText().getText().toString();
                listener.applyText(new_address, new_buildingName);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ChangeLocationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement changeLocationListener");

        }
    }

    public interface ChangeLocationListener {

        void applyText(String new_address, String new_buildingName);

    }
}
