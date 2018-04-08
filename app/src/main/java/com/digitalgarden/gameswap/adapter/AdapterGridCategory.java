package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.model.Category;

import java.util.List;

public class AdapterGridCategory extends BaseAdapter {

    Context context;
    List<Category> categories;

    public AdapterGridCategory(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.griditem_category, null);
        }

        Category category = getItem(position);
        ((TextView) convertView.findViewById(R.id.griditem_category_text)).setText(category.name);
        //((ImageView) convertView.findViewById(R.id.griditem_category_image)).setImageResource(category.resourceId);

        return convertView;
    }
}