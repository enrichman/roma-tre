package com.roma3.infovideo.utility;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;


/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 */
public class AppRater {
    private final static String APP_TITLE = "Roma TRE";

    private final static String APP_PNAME = "com.roma3.infovideo";

    private final static int DAYS_UNTIL_PROMPT = 1;
    private final static int LAUNCHES_UNTIL_PROMPT = 1;

    private static Dialog dialog;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            } else {
                if(launch_count % 7 == 0)
                    showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Valuta " + APP_TITLE + "!");
        builder.setIcon(R.drawable.star_on);
        builder.setMessage("Se " + APP_TITLE + " ti è piaciuta prenditi un secondo per scrivere una recensione.\n\n" +
                "Grazie mille!\n");
        builder.setNegativeButton("Vota " + APP_TITLE, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface v, int i) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
            }
        });
        builder.setNeutralButton("Più tardi", null);
        builder.setPositiveButton("Non mostrarlo più", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface v, int i) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
            }
        });
        builder.create().show();
    }

    public static Dialog getDialog() {
        return dialog;
    }
}