package com.digitalgarden.gameswap.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.digitalgarden.gameswap.R;

public class ActivityProduct extends Activity_Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_product_share:
                return true;
            case R.id.menu_product_save:
                return true;
            case R.id.menu_product_edit:
                return true;
            case R.id.menu_product_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}