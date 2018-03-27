package com.digitalgarden.gameswap.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.digitalgarden.gameswap.R;
import com.digitalgarden.gameswap.adapter.AdapterListProduct;
import com.digitalgarden.gameswap.adapter.AdapterListText;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends Activity_Base {

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

        AdapterListProduct adapter = new AdapterListProduct(getContext(), strings);
        listView.setAdapter(adapter);
    }

}
