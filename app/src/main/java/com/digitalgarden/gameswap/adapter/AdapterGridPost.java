package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Category;
import com.digitalgarden.gameswap.model.Post;

import java.util.List;

public class AdapterGridPost extends BaseAdapter {

    Context context;
    List<Post> posts;

    public AdapterGridPost(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.griditem_post, null);
        }

        Post post = getItem(position);
        ((TextView) convertView.findViewById(R.id.griditem_post_name)).setText(post.name);
        ((TextView) convertView.findViewById(R.id.griditem_post_price)).setText(post.price);
        ((TextView) convertView.findViewById(R.id.griditem_post_description)).setText(post.description);

        return convertView;
    }
}