package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Post;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by xuejianyu on 12/17/16.
 */
public class AdapterListPost extends BaseAdapter {

    public final String TAG = getClass().getSimpleName();

    Context context;
    List<Post> posts;

    public AdapterListPost(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        if(posts == null) {
            return 0;}

        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        if(posts == null) {
            return null;}

        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Toolbox.log(TAG, "getView() - position: " + position);

        if(convertView == null) {
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listiem_product, parent, false); }

        Post post = getItem(position);
        ((TextView) convertView.findViewById(R.id.product_name)).setText(post.name);
        ((TextView) convertView.findViewById(R.id.product_price)).setText(post.price);
        ((TextView) convertView.findViewById(R.id.product_description)).setText(post.description);
        ((TextView) convertView.findViewById(R.id.product_location)).setText(post.location);

        return convertView;
    }
}
