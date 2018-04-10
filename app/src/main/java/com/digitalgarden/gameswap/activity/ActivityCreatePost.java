package com.digitalgarden.gameswap.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.digitalgarden.gameswap.view.ProgressDialogGeneric;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityCreatePost extends Activity_Base {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ((EditText) findViewById(R.id.edittext_name)).setText("Naruto");
        ((EditText) findViewById(R.id.edittext_price)).setText("$50.00");
        ((EditText) findViewById(R.id.edittext_location)).setText("78705");
        ((EditText) findViewById(R.id.edittext_description)).setText("Engage in ultimate ninja battles");

        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPostToDatabase();
            }
        });
    }

    private void addPostToDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Toolbox.showToast(getContext(), "Firebase user is null. Please close and reopen app.");
            return;
        }

        // Create a new user with a first and last name
        Map<String, Object> post = new HashMap<>();
        post.put("userUid", user.getUid());
        post.put("name", ((EditText) findViewById(R.id.edittext_name)).getText().toString());
        post.put("price", ((EditText) findViewById(R.id.edittext_price)).getText().toString());
        post.put("location", ((EditText) findViewById(R.id.edittext_location)).getText().toString());
        post.put("description", ((EditText) findViewById(R.id.edittext_description)).getText().toString());

        final ProgressDialogGeneric dialog = new ProgressDialogGeneric(getContext());
        dialog.show();

        // Add a new document with a generated ID
        firestore.collection("posts")
            .add(post)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toolbox.log(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    dialog.dismiss();
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
}