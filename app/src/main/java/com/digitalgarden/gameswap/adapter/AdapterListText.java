package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuejianyu on 12/17/16.
 */
public class AdapterListText extends BaseAdapter {

    Context context;
    List<String> strings = new ArrayList<>();

    public AdapterListText(Context context) {
        this.context = context;
    }

    public AdapterListText(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    public void add(String value) {
        strings.add(value);
    }


    public void remove(int position) {
        strings.remove(position);
    }

    @Override
    public int getCount() {
        if(strings == null) {
            return 0;}

        return strings.size();
    }

    @Override
    public String getItem(int position) {
        if(strings == null) {
            return "";}

        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listiem_text, parent, false); }

        String currentString = getItem(position);

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(currentString);

        return convertView;
    }
}
