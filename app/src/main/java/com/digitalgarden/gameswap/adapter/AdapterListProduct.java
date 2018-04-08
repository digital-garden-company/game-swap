package com.digitalgarden.gameswap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.toolbox.Toolbox;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Created by xuejianyu on 12/17/16.
 */
public class AdapterListProduct extends BaseAdapter {

    public final String TAG = getClass().getSimpleName();

    Context context;
    List<DocumentSnapshot> documentSnapshots;

    public AdapterListProduct(Context context) {
        this.context = context;
    }

    public AdapterListProduct(Context context, List<DocumentSnapshot> documentSnapshots) {
        this.context = context;
        this.documentSnapshots = documentSnapshots;
    }

    @Override
    public int getCount() {
        if(documentSnapshots == null) {
            return 0;}

        return documentSnapshots.size();
    }

    @Override
    public DocumentSnapshot getItem(int position) {
        if(documentSnapshots == null) {
            return null;}

        return documentSnapshots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toolbox.log(TAG, "getView() - position: " + position);

        if(convertView == null) {
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listiem_product, parent, false); }

        DocumentSnapshot documentSnapshot = getItem(position);

        ((TextView) convertView.findViewById(R.id.product_name)).setText(documentSnapshot.getString("name"));
        ((TextView) convertView.findViewById(R.id.product_price)).setText(documentSnapshot.getString("price"));
        ((TextView) convertView.findViewById(R.id.product_description)).setText(documentSnapshot.getString("description"));
        ((TextView) convertView.findViewById(R.id.product_location)).setText(documentSnapshot.getString("location"));

        return convertView;
    }
}
