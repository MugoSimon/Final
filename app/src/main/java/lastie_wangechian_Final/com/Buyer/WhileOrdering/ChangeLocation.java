package lastie_wangechian_Final.com.Buyer.WhileOrdering;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import lastie_wangechian_Final.com.R;

public class ChangeLocation extends AppCompatActivity {

    private Button button_save;
    private TextInputLayout textInputLayout_address;
    private TextInputLayout textInputLayout_building;
    private Toolbar changeLocation_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        //casting
        button_save = findViewById(R.id.changeLocation_button);
        textInputLayout_address = findViewById(R.id.changeLocation_address);
        textInputLayout_building = findViewById(R.id.changeLocation_buildingName);
        changeLocation_toolbar = findViewById(R.id.changeLocation_toolbar);

        setSupportActionBar(changeLocation_toolbar);
        getSupportActionBar().setTitle("Entering New Location Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateAddress() | !validateBuilding()) {

                        return;

                    } else {

                        String buildingName = textInputLayout_building.getEditText().getText().toString().trim();
                        String address = textInputLayout_address.getEditText().getText().toString().trim();

                        Intent pass_intent = new Intent(getApplicationContext(), PlaceOrder.class);
                        pass_intent.putExtra("buildingName", buildingName);
                        pass_intent.putExtra("address", address);
                        startActivity(pass_intent);
                        finish();

                    }

                } catch (Exception error) {

                    try {
                        throw new Exception(error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


    }

    private boolean validateBuilding() {

        String buildingName = textInputLayout_building.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buildingName)) {

            textInputLayout_building.requestFocus();
            textInputLayout_building.setError("field can't be left empty");
            textInputLayout_building.getEditText().setText(null);
            return false;

        } else if (buildingName.length() > 15) {

            textInputLayout_building.requestFocus();
            textInputLayout_building.setError("building name too long");
            textInputLayout_building.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_building.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {

        String address = textInputLayout_address.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(address)) {

            textInputLayout_address.requestFocus();
            textInputLayout_address.setError("field can't be left empty");
            textInputLayout_address.getEditText().setText(null);
            return false;

        } else if (address.length() > 15) {

            textInputLayout_building.requestFocus();
            textInputLayout_building.setError("address too long");
            textInputLayout_building.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_building.setError(null);
            return true;

        }
    }
}
