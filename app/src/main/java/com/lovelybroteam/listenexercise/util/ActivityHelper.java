package com.lovelybroteam.listenexercise.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 1/28/2016.
 */
public class ActivityHelper {
    public static void showModalDialog(Activity activity, int dialogTitleTextId, int dialogContentTextId, final Runnable onCancelled){
        showModalDialog(activity, activity.getString(dialogTitleTextId), activity.getString(dialogContentTextId), onCancelled);
    }
    public static void showModalDialog(final Activity activity, final String dialogTitle, final String dialogContent, final Runnable onCancelled){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder
                        .setTitle(dialogTitle)
                        .setMessage(dialogContent)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onCancelled.run();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}
