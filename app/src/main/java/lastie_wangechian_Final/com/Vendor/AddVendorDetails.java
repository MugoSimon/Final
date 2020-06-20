package lastie_wangechian_Final.com.Vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

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
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private CountryCodePicker cpp;
    private TextInputLayout textInputLayout_phoneNumber;
    private TextInputLayout textInputLayout_address;
    private TextInputLayout textInputLayout_buildingName;
    private TextView textView_link;
    private Button button_save;
    private ImageView imageView_changeImg;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_details);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        textInputLayout_phoneNumber = findViewById(R.id.registerVendor_phoneNumber);
        textInputLayout_address = findViewById(R.id.registerVendor_address);
        textInputLayout_buildingName = findViewById(R.id.registerVendor_buildingName);
        textView_link = findViewById(R.id.link);
        imageView_changeImg = findViewById(R.id.change_image);
        circleImageView = findViewById(R.id.vendor_image);
        cpp = findViewById(R.id.country_codePicker);
        button_save = findViewById(R.id.button_save);
        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendor Additional Details");

        cpp.registerCarrierNumberEditText(textInputLayout_phoneNumber.getEditText());

        FirebaseUser current_user = mAuth.getCurrentUser();
        final String user_id = current_user.getUid();

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validatePhoneNumber() | !validateAddress() | validateBuildingName()) {

                        return;

                    } else {

                        progressDialog.setTitle("Saving user profile");
                        progressDialog.setMessage("please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        String username = getIntent().getStringExtra("username");
                        String phone_number = cpp.getFullNumberWithPlus();
                        String address = textInputLayout_address.getEditText().getText().toString();
                        String building_name = textInputLayout_buildingName.getEditText().getText().toString();
                        String vendor_image = textView_link.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Vendor_Profile").child(user_id);

                        HashMap<String, String> vendor_profile = new HashMap<>();
                        vendor_profile.put("username", username);
                        vendor_profile.put("phone_number", phone_number);
                        vendor_profile.put("address", address);
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

                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                } catch (Exception e) {

                    Toast.makeText(AddVendorDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            try {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

                    Uri imageUri = data.getData();

                    CropImage.activity(imageUri)
                            .setAspectRatio(1, 1)
                            .setMaxCropResultSize(500, 500)
                            .start(this);

                    Bitmap objectImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    circleImageView.setImageBitmap(objectImage);

                }

            } catch (IOException e) {
                e.printStackTrace();

                Toast.makeText(AddVendorDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                //Toast.makeText(AddDetails.this, "going to cropping ", Toast.LENGTH_LONG).show();
                CropImage.ActivityResult result = CropImage.getActivityResult(data);


                if (resultCode == RESULT_OK) {


                    Uri resultUri = result.getUri();
                    FirebaseUser current_user = mAuth.getCurrentUser();
                    final String user_id = current_user.getUid();

                    //Toast.makeText(AddDetails.this, resultUri.toString(), Toast.LENGTH_LONG).show();

                    final StorageReference filepath = mStorageRef.child("Vendor_Images").child(user_id + ".jpg");

                    filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Uri download_url = uri;
                                    String image_link = download_url.toString().trim();
                                    //Toast.makeText(AddDetails.this, image_link, Toast.LENGTH_LONG).show();

                                    textView_link.setText(image_link);
                                }

                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //progressDialog.dismiss();
                            Toast.makeText(AddVendorDetails.this, "error in uploading image " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                    Toast.makeText(AddVendorDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AddVendorDetails.this, "error in image retrieval ", Toast.LENGTH_LONG).show();
            }

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


    //validate address
    private boolean validateAddress() {

        String address = textInputLayout_address.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(address)) {

            textInputLayout_address.requestFocus();
            textInputLayout_address.setError("field can't be left empty");
            textInputLayout_address.getEditText().setText(null);
            return false;

        } else if (TextUtils.getTrimmedLength(address) > 20) {

            textInputLayout_address.requestFocus();
            textInputLayout_address.setError("address too long");
            textInputLayout_address.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_address.setError(null);
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
