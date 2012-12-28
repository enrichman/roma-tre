package com.roma3.infovideo.activities;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;

import android.content.DialogInterface;
import android.widget.*;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roma3.infovideo.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;

import com.roma3.infovideo.utility.*;
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
public class LezioniRoma3Activity extends Activity {

    private ChangeLog changeLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changeLog = new ChangeLog(this);
        if (changeLog.firstRun()) {
            changeLog.getLogDialog().show();
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.edit().putBoolean("rss_autoscroll", prefs.getBoolean("rss_autoscroll", false)).commit();        
        int lastFaculty = prefs.getInt("lastFaculty", 0);

        org.holoeverywhere.widget.Spinner spinner = (org.holoeverywhere.widget.Spinner) findViewById(R.id.mySpinner);
        spinner.setSelection(lastFaculty);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                org.holoeverywhere.widget.Spinner spinner = (org.holoeverywhere.widget.Spinner) findViewById(R.id.mySpinner);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case(R.id.settings) :
                openSettings();
                return true;
            case(R.id.credits) :
                openInfoDialog();
                return true;
        }
        return true;
    }

    private void openSettings() {
        Intent i = new Intent(LezioniRoma3Activity.this, SettingsActivity.class);
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
        //textView.setTextColor(Color.LTGRAY);
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

    @Override
    protected void onPause() {
        super.onPause();
        changeLog.getLogDialog().dismiss();
        if(AppRater.getDialog() != null) {
            AppRater.getDialog().dismiss();
        }
    }
}