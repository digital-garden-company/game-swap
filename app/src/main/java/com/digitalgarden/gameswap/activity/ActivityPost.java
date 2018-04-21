package com.digitalgarden.gameswap.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Post;

public class ActivityPost extends Activity_Base {

    public static String KEY_POST = "KEY_POST";

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post);

        post = (Post) getIntent().getExtras().getSerializable(KEY_POST);

        ((TextView) findViewById(R.id.layout_post_name)).setText(post.name);
        ((TextView) findViewById(R.id.layout_post_price)).setText(post.price);
        ((TextView) findViewById(R.id.layout_post_location)).setText(post.location);
        ((TextView) findViewById(R.id.layout_post_description)).setText(post.description);

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
                }
            }
        });

    }
}
