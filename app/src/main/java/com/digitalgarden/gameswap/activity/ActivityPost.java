package com.digitalgarden.gameswap.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Post;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.igdb.api_android_java.callback.onSuccessCallback;
import com.igdb.api_android_java.model.APIWrapper;
import com.igdb.api_android_java.model.Parameters;

import org.json.JSONArray;
import org.json.JSONException;

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


//        APIWrapper wrapper = new APIWrapper(getContext(), "d8a1627fe5be8b5f56ed9d32330182bb");
//        Parameters params = new Parameters()
//                .addIds("1942");
////                .addFields("*")
////                .addOrder("published_at:desc");
//
//        wrapper.games(params, new onSuccessCallback(){
//            @Override
//            public void onSuccess(JSONArray result) {
//                Toolbox.log(TAG, "onSuccess()");
//                try {
//                    Toolbox.log(TAG, result.toString(4));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                Toolbox.log(TAG, "onError()");
//                // Do something on error
//            }
//        });

    }
}
