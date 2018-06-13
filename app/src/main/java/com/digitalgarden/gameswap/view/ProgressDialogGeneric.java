package com.digitalgarden.gameswap.view;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogGeneric extends ProgressDialog {

    public ProgressDialogGeneric(Context context) {
        super(context);

        //setTitle("Title");
        setMessage("Loading...");
        setIndeterminate(false);
        setCancelable(true);
    }
}
