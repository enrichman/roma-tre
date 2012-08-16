package com.roma3.infovideo.activities;

import android.graphics.Color;
import android.view.Window;
import android.widget.LinearLayout;
import com.roma3.infovideo.model.Lezione;
import com.roma3.infovideo.activities.adapter.LezioneAdapter;
import com.roma3.infovideo.R;

import java.util.ArrayList;
import java.util.Date;

import com.roma3.infovideo.utility.TitleColorManager;
import com.roma3.infovideo.utility.lessons.LessonsDownloadingException;
import com.roma3.infovideo.utility.lessons.LessonsDownloader;
import com.roma3.infovideo.utility.ThemeManager;
import com.roma3.infovideo.activities.menu.MenuListActivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class LezioniDiOggiActivity extends MenuListActivity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ThemeManager.setTheme(this);
        //int sdk = Build.VERSION.SDK_INT;

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        String selectedFaculty = bundle.getString("selectedFaculty");

        //if (sdk < 11) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.second);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        LinearLayout title = (LinearLayout) findViewById(R.id.title);
        title.setBackgroundColor(Color.parseColor(bundle.getString("color")));
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("Facoltà di " + selectedFaculty);
        TitleColorManager.white(selectedFaculty, titleText, this);
        //}

        new DownloadXmlTask(this).execute(selectedFaculty, url);

    }

    protected class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<Lezione>> {

        private ListActivity activity;
        private ProgressDialog dialog;

        public DownloadXmlTask(ListActivity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
        }

        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Cerco le lezioni..");
            this.dialog.show();
        }


        @Override
        protected ArrayList<Lezione> doInBackground(String... params) {

            ArrayList<Lezione> lessons = null;

            String faculty = params[0];
            String url = params[1];
            Date data = new Date();

            try {
                LessonsDownloader downloader = new LessonsDownloader();
                lessons = downloader.donwloadLessons(faculty, url, data, activity);
            } catch (LessonsDownloadingException e) {
                Log.e(e.getMessage(), e.toString());
            }

            return lessons;

        }


        protected void onPostExecute(ArrayList<Lezione> result) {

            if (dialog.isShowing())
                dialog.dismiss();

            if (result != null) {
                if (result.size() == 0)
                    Toast.makeText(LezioniDiOggiActivity.this, "Nessuna lezione trovata!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(LezioniDiOggiActivity.this, "Lettura completata!", Toast.LENGTH_LONG).show();
                LezioneAdapter lezioneAdapter = new LezioneAdapter(this.activity, R.layout.row, result);
                setListAdapter(lezioneAdapter);
            } else {
                Toast.makeText(LezioniDiOggiActivity.this, "Errore!", Toast.LENGTH_LONG).show();
            }
        }

    }

}

