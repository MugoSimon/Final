package lastie_wangechian_Final.com.Vendor.Authenticatons;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorMainActivity;

public class AddVendorDetails extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //atleast one digit
                    "(?=.*[a-z])" +   //atleast one letter either upercase ama lowercase
                    "(?=\\S+$)" +     //no whitespace in the password
                    "(?=.*[@#&*^+$])" +  // atleast one special characters
                    ".{6,}" +         //minimum of 6 characters
                    "$");
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private Toolbar addDetails_toolbar;
    private CircleImageView circleImageView;
    private CountryCodePicker cpp;
    private TextInputLayout textInputLayout_phoneNumber;
    private TextInputLayout textInputLayout_location;
    private TextInputLayout textInputLayout_buildingName;
    private TextView textView_link;
    private Button button_save;
    private ImageView imageView_changeImg;
    private ProgressDialog progressDialog;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_details);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        textInputLayout_phoneNumber = findViewById(R.id.registerVendor_phoneNumber);
        textInputLayout_location = findViewById(R.id.registerVendor_location);
        textInputLayout_buildingName = findViewById(R.id.registerVendor_buildingName);
        textView_link = findViewById(R.id.link);
        imageView_changeImg = findViewById(R.id.change_image);
        circleImageView = findViewById(R.id.vendor_image);
        cpp = findViewById(R.id.country_codePicker);
        button_save = findViewById(R.id.button_save);
        progressDialog = new ProgressDialog(this);

        addDetails_toolbar = findViewById(R.id.vendor_appBar_addDetail);
        setSupportActionBar(addDetails_toolbar);
        getSupportActionBar().setTitle("Vendor Additional Details");

        cpp.registerCarrierNumberEditText(textInputLayout_phoneNumber.getEditText());

        FirebaseUser current_user = mAuth.getCurrentUser();
        final String user_id = current_user.getUid();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validatePhoneNumber() | !validateLocation() | !validateBuildingName()) {

                        return;

                    } else {

                        progressDialog.setTitle("Saving user profile");
                        progressDialog.setMessage("please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        String username = getIntent().getStringExtra("username");
                        String phone_number = cpp.getFullNumberWithPlus();
                        String location = textInputLayout_location.getEditText().getText().toString();
                        String building_name = textInputLayout_buildingName.getEditText().getText().toString();
                        String vendor_image = textView_link.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Vendors").child(user_id);

                        HashMap<String, String> vendor_profile = new HashMap<>();
                        vendor_profile.put("username", username);
                        vendor_profile.put("phone_number", phone_number);
                        vendor_profile.put("location", location);
                        vendor_profile.put("building_name", building_name);
                        vendor_profile.put("vendor_image", vendor_image);

                        mDatabase.setValue(vendor_profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                try {

                                    if (task.isSuccessful()) {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), VendorMainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        progressDialog.hide();
                                        Toast.makeText(getApplicationContext(), "Error saving user's profile: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                } catch (final DatabaseException e) {

                                    progressDialog.hide();
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addDetails), "Database Exception Found", Snackbar.LENGTH_LONG)
                                            .setAction("View Details", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    snackbar.show();
                                    return;
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {

                                progressDialog.hide();
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addDetails), "Failure Listener Initialized", Snackbar.LENGTH_LONG)
                                        .setAction("View Details", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                snackbar.show();
                                return;

                            }
                        });
                    }

                } catch (final RuntimeException e) {

                    progressDialog.hide();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addDetails), "Runtime exception", Snackbar.LENGTH_LONG)
                            .setAction("View Details", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                    snackbar.show();
                    return;
                }

            }
        });

        imageView_changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    CropImage.startPickImageActivity(AddVendorDetails.this);

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                    && resultCode == Activity.RESULT_OK) {

                Uri image_uri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, image_uri)) {

                    uri = image_uri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

                } else {

                    startCrop(image_uri);
                }

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Picasso.get().load(result.getUri()).into(circleImageView);
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                    //String image_link = result.getUri().toString();
                    //textView_link.setText(image_link);

                    //                                        Toast.makeText(getApplicationContext(), "No downloaded url", Toast.LENGTH_LONG).show();
                    try {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String userID = currentUser.getUid();

                        Uri resultUri = result.getUri();

                        final StorageReference image_path = mStorageRef.child("Vendor_profile").child(userID + ".jpg");
                        image_path.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();

                                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        textView_link.setText(String.valueOf(uri));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getApplicationContext(), "No downloaded url", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {

                                Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addDetails), "Image Failure To Upload.", Snackbar.LENGTH_LONG)
                                        .setAction("View Details", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                snackbar.show();
                                return;

                            }
                        });

                    } catch (DatabaseException e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;

                    }


                } else {

                    Toast.makeText(getApplicationContext(), "Nope nope nope", Toast.LENGTH_LONG).show();
                    return;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;

        }

    }

    //cropping image according to user requirement
    private void startCrop(Uri image_uri) {

        CropImage.activity(image_uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .setMaxCropResultSize(500, 500)
                .start(this);

    }

    //validate phone_number
    private boolean validatePhoneNumber() {

        String phone_number = textInputLayout_phoneNumber.getEditText().getText().toString().trim();

        if (phone_number.isEmpty()) {

            textInputLayout_phoneNumber.requestFocus();
            textInputLayout_phoneNumber.setError("field can't be left empty");
            textInputLayout_phoneNumber.getEditText().setText(null);
            return false;

        } else if (phone_number.length() != 9) {

            textInputLayout_phoneNumber.requestFocus();
            textInputLayout_phoneNumber.setError("invalid number");
            textInputLayout_phoneNumber.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_phoneNumber.setError(null);
            return true;
        }
    }

    //validate location
    private boolean validateLocation() {

        String address = textInputLayout_location.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(address)) {

            textInputLayout_location.requestFocus();
            textInputLayout_location.setError("field can't be left empty");
            textInputLayout_location.getEditText().setText(null);
            return false;

        } else if (TextUtils.getTrimmedLength(address) > 20) {

            textInputLayout_location.requestFocus();
            textInputLayout_location.setError("address too long");
            textInputLayout_location.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_location.setError(null);
            return true;

        }

    }

    //validate building_name
    private boolean validateBuildingName() {

        String buildingName = textInputLayout_buildingName.getEditText().getText().toString().trim();

        if (buildingName.equals("")) {

            textInputLayout_buildingName.requestFocus();
            textInputLayout_buildingName.setError("field can't be left empty");
            textInputLayout_buildingName.getEditText().setText(null);
            return false;

        } else if (buildingName.length() > 20) {

            textInputLayout_buildingName.requestFocus();
            textInputLayout_buildingName.setError("building name too long");
            textInputLayout_buildingName.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_buildingName.setError(null);
            return true;

        }
    }

}
