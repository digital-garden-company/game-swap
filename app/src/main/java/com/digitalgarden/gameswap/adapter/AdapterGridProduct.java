package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;

import java.util.List;

public class AdapterGridProduct extends BaseAdapter {

    Context context;
    List<String> items;

    public AdapterGridProduct(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.griditem_product, parent, false);
        }

        String string = getItem(position);

        return convertView;
    }
}