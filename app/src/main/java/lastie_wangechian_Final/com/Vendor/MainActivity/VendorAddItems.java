package lastie_wangechian_Final.com.Vendor.MainActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import lastie_wangechian_Final.com.R;

public class VendorAddItems extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int GALLERY_PICK = 1;
    String[] item_types = {"water bottle", "5l jerry can", "10l jerry can", "20l jerry can", "40l jerry can", "50l skyplast", "75l skyplast", "100l skyplast", "water truck"};
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Toolbar addItems_toolbar;
    private DatabaseReference mDatabase;
    private TextInputLayout textInputLayout_itemName;
    private TextInputLayout textInputLayout_itemPrice;
    private Button button_containerImage;
    private Button button_save;
    private ImageView imageView_container;
    private TextView textView_imageUrl;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_items);

        //firebase and its requirement.
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseUser current_user = mAuth.getCurrentUser();
        String user_id = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Items").child(user_id);


        //toolbar and its stuffs
        addItems_toolbar = findViewById(R.id.vendor_appBar_addDetails);
        setSupportActionBar(addItems_toolbar);
        getSupportActionBar().setTitle("Add Items For Sale");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //progressDialog
        progressDialog = new ProgressDialog(this);

        //others
        textInputLayout_itemName = findViewById(R.id.item_name);
        textInputLayout_itemPrice = findViewById(R.id.item_price);
        button_containerImage = findViewById(R.id.choose_container);
        button_save = findViewById(R.id.button_save);
        textView_imageUrl = findViewById(R.id.textView_imageUrl);
        imageView_container = findViewById(R.id.image_container);

        //spinner and its stuffs
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, item_types);
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(itemsAdapter);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateContainerName() || !validateContainerPrice()) {

                        return;

                    } else {

                        progressDialog.setTitle("Saving item");
                        progressDialog.setMessage("please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        insertData();


                    }

                } catch (final RuntimeException e) {

                    progressDialog.hide();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addItems), "Runtime Exception:", Snackbar.LENGTH_LONG)
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


        button_containerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    CropImage.startPickImageActivity(VendorAddItems.this);

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "startPick Image_Activity error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

    }

    private void insertData() {

        try {
            String item_name = textInputLayout_itemName.getEditText().getText().toString().trim();
            String item_price = textInputLayout_itemPrice.getEditText().getText().toString().trim();
            String item_type = spinner.getSelectedItem().toString();
            String item_image = textView_imageUrl.getText().toString();
            String id = mDatabase.push().getKey();

            //InsertData insertData = new InsertData(item_name,item_price,item_type,item_image,id);
            HashMap<String, String> insertData = new HashMap<>();
            insertData.put("item_name", item_name);
            insertData.put("item_price", item_price);
            insertData.put("item_type", item_type);
            insertData.put("item_image", item_image);
            insertData.put("id", id);

            mDatabase.child(id).setValue(insertData).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addItems), "Database Exception Found", Snackbar.LENGTH_LONG)
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
                public void onFailure(@NonNull Exception e) {

                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;

                }
            });
        } catch (Exception e) {
            throw new Exception();
        } finally {
            return;
        }

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

                    imageView_container.setVisibility(View.VISIBLE);
                    Picasso.get().load(result.getUri()).into(imageView_container);
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();

                    try {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String userID = currentUser.getUid();

                        Uri resultUri = result.getUri();

                        final StorageReference item_storagePath = mStorageRef.child("Items").child(userID).child(item_types.toString());
                        item_storagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();

                                item_storagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        textView_imageUrl.setText(String.valueOf(uri));
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

                                Snackbar snackbar = Snackbar.make(findViewById(R.id.vendor_addItems), "Image Failure To Upload.", Snackbar.LENGTH_LONG)
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

                    Toast.makeText(getApplicationContext(), "No media data was picked", Toast.LENGTH_LONG).show();
                    return;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;

        }

    }

    private void startCrop(Uri image_uri) {

        CropImage.activity(image_uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 2)
                .start(this);

    }

    public boolean validateContainerName() {

        String container_name = textInputLayout_itemName.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(container_name)) {

            textInputLayout_itemName.requestFocus();
            textInputLayout_itemName.setError("item name required");
            textInputLayout_itemName.getEditText().setText(null);
            return false;

        } else if (container_name.length() > 12) {

            textInputLayout_itemName.requestFocus();
            textInputLayout_itemName.setError("item name too long");
            textInputLayout_itemName.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_itemName.setError(null);
            return true;
        }
    }

    public boolean validateContainerPrice() {

        String container_price = textInputLayout_itemPrice.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(container_price)) {

            textInputLayout_itemPrice.requestFocus();
            textInputLayout_itemPrice.setError("item price required");
            textInputLayout_itemPrice.getEditText().setText(null);
            return false;

        } else if (container_price.equals(0)) {

            textInputLayout_itemPrice.requestFocus();
            textInputLayout_itemPrice.setError("invalid price");
            textInputLayout_itemPrice.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_itemPrice.setError(null);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String item_selected = spinner.getSelectedItem().toString();
        //Toast.makeText(getApplicationContext(), item_selected + " " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        spinner.requestFocus();
        return;
    }
}
