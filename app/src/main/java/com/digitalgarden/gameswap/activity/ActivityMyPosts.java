package com.digitalgarden.gameswap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterListProduct;
import com.digitalgarden.gameswap.adapter.AdapterListProductDeprecated;
import com.digitalgarden.gameswap.toolbox.Toolbox;
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

        firestore.collection("posts")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Toolbox.log(TAG, "getMyPosts() - onComplete()");

                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documentSnapshots = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            //Toolbox.log(TAG, document.getId() + " => " + document.getData());
                            documentSnapshots.add(document);
                        }
                        updateUI(documentSnapshots);
                    } else {
                        Toolbox.warn(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    private void updateUI(List<DocumentSnapshot> documentSnapshots) {
        ListView listView = findViewById(R.id.activity_listview);
        AdapterListProduct adapter = new AdapterListProduct(getContext(), documentSnapshots);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityProduct.class);
                startActivity(intent);
            }
        });
    }
}
