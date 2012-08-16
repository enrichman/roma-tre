package com.roma3.infovideo.activities.menu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import com.roma3.infovideo.activities.SettingsActivity;
import com.roma3.infovideo.R;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class MenuListActivity extends ListActivity {

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0,0,0,"Impostazioni").setIcon(R.drawable.ic_menu_manage);
		menu.add(0,1,1,"Info").setIcon(R.drawable.ic_menu_info_details);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case 0: openSettings();
			return true;
			case 1: openInfoDialog();
			return true;
	}
	
	return false;
		
	}

	private void openSettings() {
		Intent i = new Intent(MenuListActivity.this,SettingsActivity.class);
		startActivity(i);
	}

    private void openInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informazioni");
        final ScrollView s_view = new ScrollView(this);
        final TextView textView = new TextView(this);
        final SpannableString spannableText = new SpannableString(getText(R.string.informazioni));
        Linkify.addLinks(spannableText, Linkify.WEB_URLS);
        textView.setText(spannableText);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setTextSize(14);
        textView.setTextColor(Color.LTGRAY);
        textView.setPadding(5, 5, 5, 15);
        s_view.addView(textView);
        builder.setView(s_view);
        builder.setCancelable(false);
        builder.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
