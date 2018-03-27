package com.digitalgarden.gameswap.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xuejianyu on 2/15/18.
 */

public class Activity_Base extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();


    protected Context getContext() {
        return this;
    }
}
