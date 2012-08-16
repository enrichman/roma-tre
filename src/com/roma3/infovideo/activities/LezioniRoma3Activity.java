package com.roma3.infovideo.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.*;
import com.roma3.infovideo.utility.*;
import com.roma3.infovideo.activities.menu.MenuActivity;

import com.roma3.infovideo.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.roma3.infovideo.utility.rss.RssTask;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class LezioniRoma3Activity extends MenuActivity {

    private ChangeLog changeLog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        changeLog = new ChangeLog(this);
        if (changeLog.firstRun()) {
            changeLog.getLogDialog().show();
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int lastFaculty = prefs.getInt("lastFaculty", 0);

        Spinner spinner = (Spinner) findViewById(R.id.mySpinner);
        spinner.setSelection(lastFaculty);

        Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Spinner spinner = (Spinner) findViewById(R.id.mySpinner);
                int position = spinner.getSelectedItemPosition();
                String selected = getResources().getStringArray(R.array.facolta)[position];
                String url = getResources().getStringArray(R.array.url)[position];
                String color = getResources().getStringArray(R.array.color)[position];
                String rssUrl = getResources().getStringArray(R.array.rssUrl)[position];

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("lastFaculty", position);
                editor.commit();

                Intent i = new Intent(LezioniRoma3Activity.this,SecondActivity.class);
				i.putExtra("selectedFaculty", selected);
                i.putExtra("color", color);
                i.putExtra("url", url);
                i.putExtra("rssUrl", rssUrl);
				startActivity(i);
			}
		});

        AppRater.app_launched(this);

        new RssTask(this).execute(getString(R.string.rssUrlRomaTre));
	}



    @Override
    protected void onPause() {
        super.onPause();
        changeLog.getLogDialog().dismiss();
        if(AppRater.getDialog() != null) {
            AppRater.getDialog().dismiss();
        }
    }
}