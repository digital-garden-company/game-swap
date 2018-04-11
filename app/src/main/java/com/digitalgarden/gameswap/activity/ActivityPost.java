package com.digitalgarden.gameswap.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Post;

public class ActivityPost extends Activity_Base {

    public static String KEY_POST = "KEY_POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post);

        Post post = (Post) getIntent().getExtras().getSerializable(KEY_POST);

        ((TextView) findViewById(R.id.layout_post_name)).setText(post.name);
        ((TextView) findViewById(R.id.layout_post_price)).setText(post.price);
        ((TextView) findViewById(R.id.layout_post_location)).setText(post.location);
        ((TextView) findViewById(R.id.layout_post_description)).setText(post.description);

    }
}
