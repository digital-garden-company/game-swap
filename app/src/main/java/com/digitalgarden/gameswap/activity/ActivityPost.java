package com.digitalgarden.gameswap.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Post;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;

import org.json.JSONArray;
import org.json.JSONException;

public class ActivityPost extends Activity_Base {

    public static String KEY_POST = "KEY_POST";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    Post post;
    private Object imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post);

        post = (Post) getIntent().getExtras().getSerializable(KEY_POST);
        Toolbox.log(TAG, post.toString());

        ((TextView) findViewById(R.id.layout_post_name)).setText(post.name);
        ((TextView) findViewById(R.id.layout_post_price)).setText(post.price);
        ((TextView) findViewById(R.id.layout_post_location)).setText(post.location);
        ((TextView) findViewById(R.id.layout_post_description)).setText(post.description);

        if(post.imageFileName != null && !post.imageFileName.isEmpty()) {
            ImageView imageView = findViewById(R.id.layout_post_iamge);

            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + post.imageFileName);

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            Glide.with(this /* context */)
                    .load(imageRef)
                    .into(imageView);

        }

        findViewById(R.id.layout_post_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailto = "mailto:" + post.contactEmail +
                        "?&subject=" + "I would like to purchase your game" +
                        "&body=" + String.format("I would like to purchase %s for the price of %s", post.name, post.price);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                    Toolbox.showToast(getContext(), "No email is available");
                }
            }
        });


    }

    public void getImageUri() {
        if (post.imageFileName != null && !post.imageFileName.isEmpty()) {
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/" + post.imageFileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toolbox.log(TAG, "onSuccess: " + uri.toString());
                    // Got the download URL for 'users/me/profile.png'
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toolbox.log(TAG, "onFailure: " + exception.getMessage());
                }
            });
        }
    }
}
