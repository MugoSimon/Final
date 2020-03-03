package lastie_wangechian_Final.com.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import lastie_wangechian_Final.com.R;

public class AddDetails extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference mStorageRef;
    DocumentReference documentReference;
    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextInputLayout textInputLayout_username;
    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private TextView textView_link;
    String userID;
    private Button button_save;
    private ImageView imageView_changeImg;
    private ProgressDialog progressDialog;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +   //atleast one digit
                    "(?=.*[a-z])" +   //atleast one letter either upercase ama lowercase
                    "(?=\\S+$)" +     //no whitespace in the password
                    "(?=.*[@#&*^+$])" +  // atleast one special characters
                    ".{6,}" +         //minimum of 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        textInputLayout_username = findViewById(R.id.registerBuyer_username);
        textInputLayout_email = findViewById(R.id.registerBuyer_email);
        textInputLayout_password = findViewById(R.id.registerBuyer_password);
        textView_link = findViewById(R.id.link);
        imageView_changeImg = findViewById(R.id.change_image);
        circleImageView = findViewById(R.id.buyer_image);
        button_save = findViewById(R.id.button_save);
        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Additional Details");


        documentReference = fStore.collection("Buyer").document(userID);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmail() | validateUsername() | validatePassword()) {

                    String username = textInputLayout_username.getEditText().getText().toString();
                    String email = textInputLayout_email.getEditText().getText().toString();
                    String image_link = textView_link.getText().toString();
                    String password = textInputLayout_password.getEditText().getText().toString();
                    String phone_number = getIntent().getStringExtra("buyer_phonenumber");

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("Username", username);
                    userMap.put("Email", email);
                    userMap.put("Image", image_link);
                    userMap.put("Phone", phone_number);
                    userMap.put("Thumbnail", "default");
                    userMap.put("Password", password);

                    documentReference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), BuyerProfile.class));
                                finish();
                            } else {

                                Toast.makeText(AddDetails.this, "Data wasn't saved " + task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {

                    return;
                }
            }
        });


        //change image
        imageView_changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                } catch (Exception e) {

                    Toast.makeText(AddDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                circleImageView.setImageBitmap(objectImage);

            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(AddDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            //Toast.makeText(AddDetails.this, "going to cropping ", Toast.LENGTH_LONG).show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                //Toast.makeText(AddDetails.this, resultUri.toString(), Toast.LENGTH_LONG).show();

                final StorageReference filepath = mStorageRef.child("Buyer_Images").child(userID + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri download_url = uri;
                                String image_link = download_url.toString().trim();
                                Toast.makeText(AddDetails.this, image_link, Toast.LENGTH_LONG).show();

                                textView_link.setText(image_link);
                            }

                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //progressDialog.dismiss();
                        Toast.makeText(AddDetails.this, "error in uploading image " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }


        //Toast.makeText(AddDetails.this, imageUri, Toast.LENGTH_LONG).show();
    }

    private boolean validateUsername() {

        String buyer_username = textInputLayout_username.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buyer_username)) {

            textInputLayout_username.requestFocus();
            textInputLayout_username.setError("enter username");
            return false;

        } else if (buyer_username.length() > 15) {

            textInputLayout_username.requestFocus();
            textInputLayout_username.getEditText().setText(null);
            textInputLayout_username.setError("username too long");
            return false;

        } else {

            textInputLayout_username.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {

        String buyer_email = textInputLayout_email.getEditText().getText().toString().trim();

        if (buyer_email.isEmpty()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("Field can't be empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(buyer_email).matches()) {

            textInputLayout_email.requestFocus();
            textInputLayout_email.setError("invalid email");
            textInputLayout_email.getEditText().setText(null);
            return false;

        } else {

            textInputLayout_email.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {

        String buyer_password = textInputLayout_password.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(buyer_password)) {

            textInputLayout_password.requestFocus();
            textInputLayout_password.setError("password required");
            return false;

        } else if (!PASSWORD_PATTERN.matcher(buyer_password).matches()) {

            textInputLayout_password.requestFocus();
            textInputLayout_password.getEditText().setText(null);
            textInputLayout_password.setError("password too weak");
            return false;

        } else {

            textInputLayout_password.setError(null);
            return true;
        }
    }
}
