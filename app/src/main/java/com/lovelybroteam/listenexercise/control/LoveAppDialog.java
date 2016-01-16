package com.lovelybroteam.listenexercise.control;

import android.app.AlertDialog;
import android.content.Context;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 1/16/2016.
 */
public class LoveAppDialog extends AlertDialog {
    public LoveAppDialog(Context context) {
        super(context);
        setContentView(R.layout.love_app_dialog_layout);
        setCancelable(true);
    }
}
