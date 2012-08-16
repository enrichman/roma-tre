package com.roma3.infovideo.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        dialog = new Dialog(mContext);
        dialog.setTitle("Valuta " + APP_TITLE + "!");

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("Se " + APP_TITLE + " ti è piaciuta prenditi un secondo per scrivere una recensione.\n\n" +
                "Grazie mille!\n");
        tv.setTextColor(Color.GRAY);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Resources resources = mContext.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, resources.getDisplayMetrics());
        tv.setWidth((int) px);
        tv.setPadding(8, 0, 8, 4);
        ll.addView(tv);

        Button b2 = new Button(mContext);
        b2.setText("Vota " + APP_TITLE);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        LinearLayout buttonsLayout = new LinearLayout(mContext);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setWeightSum(2);
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
        );


        Button b3 = new Button(mContext);
        b3.setText("Più tardi");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        b3.setLayoutParams(buttonLayout);
        buttonsLayout.addView(b3);

        Button b4 = new Button(mContext);
        b4.setText("Non mostrarlo più");
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        b4.setLayoutParams(buttonLayout);
        buttonsLayout.addView(b4);

        ll.addView(buttonsLayout);

        dialog.setContentView(ll);
        dialog.show();
    }

    public static Dialog getDialog() {
        return dialog;
    }
}