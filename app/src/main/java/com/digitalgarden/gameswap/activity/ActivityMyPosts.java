package com.digitalgarden.gameswap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterListPost;
import com.digitalgarden.gameswap.model.Post;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.digitalgarden.gameswap.view.ProgressDialogGeneric;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyPosts extends Activity_Base {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        getMyPosts();
    }

    private void getMyPosts() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Toolbox.showToast(getContext(), "Firebase user is null. Please close and reopen app.");
            return;
        }

        final ProgressDialogGeneric dialog = new ProgressDialogGeneric(getContext());
        dialog.show();

        firestore.collection("posts")
                .whereEqualTo("userUid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toolbox.log(TAG, "getMyPosts() - onComplete()");
                        dialog.dismiss();

                        if (task.isSuccessful()) {
                            List<Post> posts = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                //Toolbox.log(TAG, document.getId() + " => " + document.getData());
                                Post post = document.toObject(Post.class);
                                //Toolbox.log(TAG, "post: " + post);
                                posts.add(post);
                            }
                            updateUI(posts);
                        } else {
                            Toolbox.warn(TAG, "Error getting documents.", task.getException());
                        }
                    }
            });
    }

    private void updateUI(List<Post> posts) {
        ListView listView = findViewById(R.id.activity_listview);
        final AdapterListPost adapter = new AdapterListPost(getContext(), posts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityMyPost.class);
                intent.putExtra(ActivityMyPost.KEY_POST, adapter.getItem(position));
                startActivity(intent);
            }
        });
    }
}
