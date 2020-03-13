package lastie_wangechian_Final.com.Vendor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lastie_wangechian_Final.com.R;

public class VendorAddItems extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference mStorageRef;
    CollectionReference documentReference;
    String userID;
    private Toolbar toolbar;
    private EditText editText_containerName;
    private EditText editText_continerPrice;
    private Button button_containerImage;
    private Button button_save;
    private ImageView imageView_container;
    private TextView textView_imageUrl;
    private TextView textView_vendorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_items);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        toolbar = findViewById(R.id.add_item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Items For Sale");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        editText_containerName = findViewById(R.id.editText_container);
        editText_continerPrice = findViewById(R.id.editText_price);
        button_containerImage = findViewById(R.id.choose_container);
        button_save = findViewById(R.id.button_save);
        textView_imageUrl = findViewById(R.id.textView_imageUrl);
        textView_vendorname = findViewById(R.id.textView_vendorname);
        imageView_container = findViewById(R.id.image_container);

        button_containerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                } catch (Exception e) {

                    Toast.makeText(VendorAddItems.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        documentReference = fStore.collection("Items");

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (validateContainerName() | validateContainerPrice()) {

                        String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference_getName = fStore.collection("Vendor").document(userID);
                        documentReference_getName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {

                                    String username = documentSnapshot.getString("Username");
                                    Toast.makeText(VendorAddItems.this, username, Toast.LENGTH_LONG).show();
                                    textView_vendorname.setText(username);
                                }

                            }
                        });


                        String container_name = editText_containerName.getText().toString().trim();
                        String vendor_name = textView_vendorname.getText().toString().trim();
                        String container_price = editText_continerPrice.getText().toString().trim();
                        String image_url = textView_imageUrl.getText().toString().trim();

                        Map<String, String> container_details = new HashMap<>();
                        container_details.put("Vendor_name", vendor_name);
                        container_details.put("Container_name", container_name);
                        container_details.put("Container_price", container_price);
                        container_details.put("Vendor_userID", UserId);
                        container_details.put("Container_image", image_url);

                        documentReference.add(container_details).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(VendorAddItems.this, "Data wasn't saved " + e.toString().trim(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        return;
                    }

                } catch (Exception e) {
                    Toast.makeText(VendorAddItems.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                        .start(this);

                Bitmap objectImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //String image_path = objectImage.toString().trim();
                imageView_container.setImageBitmap(objectImage);

                //Toast.makeText(VendorAddItems.this, image_path, Toast.LENGTH_LONG).show();


            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(VendorAddItems.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            //Toast.makeText(AddDetails.this, "going to cropping ", Toast.LENGTH_LONG).show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();


                final StorageReference filepath = mStorageRef.child("Vendor_Items").child(userID + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Uri download_url = uri;
                                String image_link = download_url.toString().trim();

                                Toast.makeText(VendorAddItems.this, image_link, Toast.LENGTH_LONG).show();
                                textView_imageUrl.setVisibility(View.VISIBLE);
                                textView_imageUrl.setText(image_link);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(VendorAddItems.this, e.getMessage().trim(), Toast.LENGTH_LONG).show();
                    }
                });


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(VendorAddItems.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }


    }

    public boolean validateContainerName() {

        String container_name = editText_containerName.getText().toString().trim();

        if (TextUtils.isEmpty(container_name)) {

            editText_containerName.requestFocus();
            editText_containerName.setError("container name required");
            return false;

        } else if (container_name.length() > 15) {

            editText_containerName.requestFocus();
            editText_containerName.setError("container name too long");
            return false;

        } else {

            editText_containerName.setError(null);
            return true;
        }
    }

    public boolean validateContainerPrice() {

        String container_price = editText_continerPrice.getText().toString().trim();

        if (TextUtils.isEmpty(container_price)) {

            editText_continerPrice.requestFocus();
            editText_continerPrice.setError("container price required");
            return false;
        } else if (container_price.equals(0)) {

            editText_continerPrice.requestFocus();
            editText_continerPrice.setError("invalid price");
            return false;
        } else {

            editText_continerPrice.setError(null);
            return true;
        }
    }
}
