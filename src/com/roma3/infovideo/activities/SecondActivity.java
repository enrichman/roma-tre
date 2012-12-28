package com.roma3.infovideo.activities;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.TimePickerDialog;
import org.holoeverywhere.app.DatePickerDialog;
import org.holoeverywhere.widget.DatePicker;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roma3.infovideo.R;

import java.util.Calendar;

import com.roma3.infovideo.utility.ThemeManager;
import com.roma3.infovideo.utility.rss.RssTask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class SecondActivity extends Activity {

    private String selectedFaculty;
    private String url;
    private String color;

    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;
    private int ora;
    private int minuti;
    private int anno;
    private int mese;
    private int giorno;

    public void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Bundle bundle = getIntent().getExtras();
        this.selectedFaculty = bundle.getString("selectedFaculty");
        this.url = bundle.getString("url");
        this.color = bundle.getString("color");
        String rssUrl = bundle.getString("rssUrl");
        //int sdk = Build.VERSION.SDK_INT;
        //if(sdk < 11) {
        //LinearLayout title = (LinearLayout) findViewById(R.id.title);
        //title.setBackgroundColor(Color.parseColor(this.color));
        //TextView titleText = (TextView) findViewById(R.id.title_text);
        //titleText.setText("Facolt√† di " + this.selectedFaculty);
        //TitleColorManager.white(selectedFaculty, titleText, this);
        //}

        //TextView facolta_selezionata = (TextView) findViewById(R.id.facolta_selezionata);
        //facolta_selezionata.setText("Facolt√† di " + this.selectedFaculty);

        Button lezioniOggi = (Button) findViewById(R.id.btn_lezioni_giorno);
        Button auleLibere = (Button) findViewById(R.id.btn_aule_libere);
        Button altreLezioni = (Button) findViewById(R.id.btn_lezioni_altri_gg);


        lezioniOggi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, LezioniDiOggiActivity.class);
                i.putExtra("selectedFaculty", selectedFaculty);
                i.putExtra("color", color);
                i.putExtra("url", url);
                startActivity(i);
            }
        });


        auleLibere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);
            }
        });

        Calendar cal = Calendar.getInstance();
        ora = cal.get(Calendar.HOUR_OF_DAY);
        minuti = cal.get(Calendar.MINUTE);
        anno = cal.get(Calendar.YEAR);
        mese = cal.get(Calendar.MONTH);
        giorno = cal.get(Calendar.DAY_OF_MONTH);


        altreLezioni.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        new RssTask(this).execute(rssUrl);

    }

    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case TIME_DIALOG_ID:
                TimePickerDialog tmp = new TimePickerDialog(this, mTimeSetListener, ora, minuti, true);
                tmp.setTitle("Mostra le aule libere alle ore:");
                tmp.setButton(-1, "Trova!", tmp);
                return tmp;
            case DATE_DIALOG_ID:
                DatePickerDialog dtp = new DatePickerDialog(this, mDateSetListener, anno, mese, giorno);
                dtp.setTitle("Mostra le lezioni del:");
                dtp.setButton(-1, "Trova!", dtp);
                return dtp;
        }
        return null;
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
        Intent i = new Intent(SecondActivity.this, SettingsActivity.class);
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


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(org.holoeverywhere.widget.TimePicker view, int hourOfDay, int minute) {
                    ora = hourOfDay;
                    minuti = minute;
                    searchAuleLibere();
                }

                private void searchAuleLibere() {
                    Intent i = new Intent(SecondActivity.this, AuleLibereActivity.class);
                    i.putExtra("selectedFaculty", selectedFaculty);
                    i.putExtra("color", color);
                    i.putExtra("url", url);
                    i.putExtra("ora", ora);
                    i.putExtra("minuti", minuti);
                    startActivity(i);
                }
            };


    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    anno = year;
                    mese = monthOfYear + 1;
                    giorno = dayOfMonth;
                    searchLezioniData();
                }

                private void searchLezioniData() {
                    Intent i = new Intent(SecondActivity.this, AltreLezioniActivity.class);
                    i.putExtra("selectedFaculty", selectedFaculty);
                    i.putExtra("color", color);
                    i.putExtra("url", url);
                    i.putExtra("anno", anno);
                    i.putExtra("mese", mese);
                    i.putExtra("giorno", giorno);
                    startActivity(i);

                }
            };
}
