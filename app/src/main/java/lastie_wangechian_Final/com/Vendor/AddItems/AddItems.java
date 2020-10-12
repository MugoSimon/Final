package lastie_wangechian_Final.com.Vendor.AddItems;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.Objects;

import lastie_wangechian_Final.com.R;
import lastie_wangechian_Final.com.Vendor.MainActivity.VendorMainActivity;

import static android.app.Activity.RESULT_OK;

public class AddItems extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int GALLERY_PICK = 1;
    String[] item_types = {"water bottle", "5l jerry can", "10l jerry can", "20l jerry can", "40l jerry can", "50l skyplast", "75l skyplast", "100l skyplast", "water truck"};
    private TextInputLayout frg_textinputLayoutContainerName, frg_textinputLayoutContainerPrice;
    private Spinner frg_spinner;
    private Button frg_buttonSave, frg_buttonAddImage;
    private ImageView frg_imageViewer;
    private TextView frg_textviewImageUrl;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference fgm_mStorageRef;
    private View addItemsView;
    private Uri uri;

    public AddItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        addItemsView = inflater.inflate(R.layout.fragment_add_items, container, false);
        frg_textinputLayoutContainerName = addItemsView.findViewById(R.id.fragment_itemName);
        frg_textinputLayoutContainerPrice = addItemsView.findViewById(R.id.fragment_itemPrice);
        frg_spinner = addItemsView.findViewById(R.id.fragment_spinner);
        frg_buttonAddImage = addItemsView.findViewById(R.id.fragment_chooseContainer);
        frg_buttonSave = addItemsView.findViewById(R.id.fragment_buttonSave);
        frg_textviewImageUrl = addItemsView.findViewById(R.id.fragment_textview);
        frg_imageViewer = addItemsView.findViewById(R.id.fragment_imageviewerContainer);

        //progressDialog
        progressDialog = new ProgressDialog(getContext());

        //firebase and its content
        mAuth = FirebaseAuth.getInstance();
        fgm_mStorageRef = FirebaseStorage.getInstance().getReference();

        //spinner and its compoments
        frg_spinner.setOnItemSelectedListener(this);
        ArrayAdapter items_za_dropDown = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, item_types);
        items_za_dropDown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frg_spinner.setAdapter(items_za_dropDown);

        //butttons
        frg_buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!validateContainerName() | !validateContainerPrice()) {

                        return;

                    } else {

                        progressDialog.setTitle("Saving item");
                        progressDialog.setMessage("please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        try {

                            String container_name = frg_textinputLayoutContainerName.getEditText().getText().toString().trim();
                            String container_price = frg_textinputLayoutContainerPrice.getEditText().getText().toString().trim();
                            String container_type = frg_spinner.getSelectedItem().toString();
                            String container_image = frg_textviewImageUrl.getText().toString();

                            FirebaseUser current_user = mAuth.getCurrentUser();
                            String user_id = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Items").child(user_id);
                            HashMap<String, String> container_hashMap = new HashMap<>();
                            container_hashMap.put("item_name", container_name);
                            container_hashMap.put("item_price", container_price);
                            container_hashMap.put("item_type", container_type);
                            container_hashMap.put("item_image", container_image);

                            mDatabase.push().setValue(container_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    try {

                                        if (task.isSuccessful()) {

                                            progressDialog.dismiss();
                                            Intent intent = new Intent(getContext(), VendorMainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        } else {

                                            progressDialog.hide();
                                            Toast.makeText(getContext(), "Error saving user's profile: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }

                                    } catch (final DatabaseException e) {

                                        progressDialog.hide();
                                        Snackbar snackbar = Snackbar.make(addItemsView.findViewById(R.id.frg_addItems), "Database Exception Found", Snackbar.LENGTH_LONG)
                                                .setAction("View Details", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                        snackbar.show();
                                        return;
                                    }
                                }


                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    try {
                                        throw new Exception(e.getMessage());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return;
                                }
                            });

                        } catch (NullPointerException error) {

                            throw new NullPointerException(error.getMessage());

                        }
                    }

                } catch (RuntimeException erro) {

                    throw new RuntimeException(erro.getMessage());
                }
            }
        });

        frg_buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    CropImage.startPickImageActivity((Activity) getContext());

                } catch (Exception e) {

                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        return addItemsView;
    }

    //Todo angalia kwa niny the cropper isnt croping as it suppose to

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {

                Uri image_uri = CropImage.getPickImageResultUri(getContext(), data);

                if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), image_uri)) {

                    uri = image_uri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

                } else {

                    startCrop(image_uri);
                }

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    frg_imageViewer.setVisibility(View.VISIBLE);
                    Picasso.get().load(result.getUri()).into(frg_imageViewer);
                    Toast.makeText(getContext(), "Successfully", Toast.LENGTH_LONG).show();

                    try {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String userID = currentUser.getUid();

                        Uri resultUri = result.getUri();

                        final StorageReference container_storagePath = fgm_mStorageRef.child("Items").child(userID).child(item_types.toString());
                        container_storagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(getContext(), "Successfully", Toast.LENGTH_LONG).show();

                                container_storagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        frg_textviewImageUrl.setText(String.valueOf(uri));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getContext(), "No downloaded url", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {


                                Snackbar snackbar = Snackbar.make(addItemsView.findViewById(R.id.frg_addItems), "Image Failure To Upload.", Snackbar.LENGTH_LONG)
                                        .setAction("View Details", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                snackbar.show();
                                return;

                            }
                        });
                    } catch (DatabaseException e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;

                    }
                } else {

                    Toast.makeText(getContext(), "No media data was picked", Toast.LENGTH_LONG).show();
                    return;

                }
            }

        } catch (RuntimeException e) {

            throw new RuntimeException(e.getMessage());

        } catch (Exception er) {

            try {
                throw new Exception(er.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //Crop Image method
    private void startCrop(Uri image_uri) {

        CropImage.activity(image_uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 2)
                .setMaxCropResultSize(1000, 600)
                .start(Objects.requireNonNull(getContext()), this);

    }

    //validating the container name
    private boolean validateContainerName() {

        String containerName = frg_textinputLayoutContainerName.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(containerName)) {

            frg_textinputLayoutContainerName.requestFocus();
            frg_textinputLayoutContainerName.setError("field is empty");
            frg_textinputLayoutContainerName.getEditText().setText(null);
            return false;

        } else if (containerName.length() > 15) {

            frg_textinputLayoutContainerName.requestFocus();
            frg_textinputLayoutContainerName.setError("name too long");
            frg_textinputLayoutContainerName.getEditText().setText(null);
            return false;

        } else {

            frg_textinputLayoutContainerName.setError(null);
            return true;
        }
    }

    //validating the container price
    private boolean validateContainerPrice() {

        String container_price = frg_textinputLayoutContainerPrice.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(container_price)) {

            frg_textinputLayoutContainerPrice.requestFocus();
            frg_textinputLayoutContainerPrice.setError("field is empty");
            frg_textinputLayoutContainerPrice.getEditText().setText(null);
            return false;

        } else if (container_price.length() > 6) {

            frg_textinputLayoutContainerPrice.requestFocus();
            frg_textinputLayoutContainerPrice.setError("price undefined");
            frg_textinputLayoutContainerPrice.getEditText().setText(null);
            return false;

        } else {

            frg_textinputLayoutContainerPrice.setError(null);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item_typeSeleted = frg_spinner.getSelectedItem().toString();
        //Toast.makeText(getContext(), item_typeSeleted + " " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        frg_spinner.requestFocus();
        return;
    }
}
