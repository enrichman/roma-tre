package com.roma3.infovideo.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.*;
import com.roma3.infovideo.R;

import java.util.Calendar;

import com.roma3.infovideo.utility.TitleColorManager;
import com.roma3.infovideo.utility.rss.RssTask;
import com.roma3.infovideo.activities.menu.MenuActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class SecondActivity extends MenuActivity {

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

    private String rssUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Bundle bundle = getIntent().getExtras();
        this.selectedFaculty = bundle.getString("selectedFaculty");
        this.url = bundle.getString("url");
        this.color = bundle.getString("color");
        rssUrl = bundle.getString("rssUrl");
        //int sdk = Build.VERSION.SDK_INT;
        //if(sdk < 11) {
        LinearLayout title = (LinearLayout) findViewById(R.id.title);
        title.setBackgroundColor(Color.parseColor(this.color));
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("Facoltà di " + this.selectedFaculty);
        TitleColorManager.white(selectedFaculty, titleText, this);
        //}

        //TextView facolta_selezionata = (TextView) findViewById(R.id.facolta_selezionata);
        //facolta_selezionata.setText("Facoltà di " + this.selectedFaculty);

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

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            LinearLayout errorContainer = (LinearLayout) findViewById(R.id.rss_error_container);
            errorContainer.setVisibility(View.VISIBLE);
            errorContainer.bringToFront();
            errorContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout errorContainer = (LinearLayout) findViewById(R.id.rss_error_container);
                    errorContainer.setVisibility(View.INVISIBLE);
                    LinearLayout container = (LinearLayout) findViewById(R.id.rss_loading_container);
                    container.setVisibility(View.VISIBLE);
                    container.bringToFront();
                    launch();
                }
            });
        } else {
            LinearLayout container = (LinearLayout) findViewById(R.id.rss_loading_container);
            container.setVisibility(View.VISIBLE);
            container.bringToFront();
            launch();
        }
    }

    private void launch() {
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


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
