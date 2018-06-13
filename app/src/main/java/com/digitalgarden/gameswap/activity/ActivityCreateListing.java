package com.digitalgarden.gameswap.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.digitalgarden.gameswap.view.ProgressDialogGeneric;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityCreateListing extends Activity_Base {


    Uri fileUri;
    ImageView imageView;

    // Get a non-default Storage bucket
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    ProgressDialogGeneric dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        imageView = findViewById(R.id.layout_image);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.layout_button_takepicture).setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


        findViewById(R.id.button_takepicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void submit() {
        uploadImage();
    }

    private void uploadImage() {
        StorageReference storageRef = storage.getReference();
        //StorageReference imageRef = storageRef.child("images/picture.jpg");
        final String fileName = UUID.randomUUID() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + fileName);

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        if((imageView.getDrawable()) == null) {
            uploadListing();
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        dialog = new ProgressDialogGeneric(getContext());
        dialog.show();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toolbox.log(TAG, "onFailure()");
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Toolbox.log(TAG, "onSuccess()");
                uploadListing(fileName);
            }
        });
    }

    private void uploadListing() {
        uploadListing("");
    }

    private void uploadListing(String imageFileName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Toolbox.showToast(getContext(), "Firebase user is null. Please close and reopen app.");
            return;
        }

        // Create a new user with a first and last name
        Map<String, Object> post = new HashMap<>();
        post.put("userUid", user.getUid());
        post.put("name", ((EditText) findViewById(R.id.edittext_name)).getText().toString().trim());
        post.put("price", ((EditText) findViewById(R.id.edittext_price)).getText().toString().trim());
        post.put("location", ((EditText) findViewById(R.id.edittext_location)).getText().toString().trim());
        post.put("description", ((EditText) findViewById(R.id.edittext_description)).getText().toString().trim());
        post.put("contactEmail", user.getEmail());
        if(imageFileName != null && !imageFileName.isEmpty()) {
            post.put("imageFileName", imageFileName);
        }

        // Add a new document with a generated ID
        firestore.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toolbox.log(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toolbox.warn(TAG, "Error adding document", e);
                        Toolbox.showToast(getContext(), "Something went wrong.");
                        dialog.dismiss();
                    }
                });
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //fileUri = Uri.fromFile(getOutputMediaFile());
        fileUri = FileProvider.getUriForFile(getContext(), getApplicationContext().getPackageName() + ".com.digitalgarden.gameswap.provider", getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                findViewById(R.id.layout_button_takepicture).setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(fileUri);
            }
        }
    }
}