package com.digitalgarden.gameswap.toolbox;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Toolbox {
    public static void log(String tag, String message) {
        Log.d(tag, message);
    }

    public static void warn(String tag, String message, Throwable throwable) {
        Log.w(tag, message, throwable);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
