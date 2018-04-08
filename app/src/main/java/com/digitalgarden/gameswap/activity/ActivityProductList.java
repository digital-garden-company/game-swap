package com.digitalgarden.gameswap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterListProductDeprecated;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductList extends Activity_Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ListView listView = (ListView) findViewById(R.id.activity_listview);
        List<String> strings = new ArrayList<>();
        strings.add("Apple");
        strings.add("Banana");
        strings.add("Carrot");
        strings.add("Digimon");

        AdapterListProductDeprecated adapter = new AdapterListProductDeprecated(getContext(), strings);
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
