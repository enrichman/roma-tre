package com.roma3.infovideo.activities;

import android.graphics.Color;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.roma3.infovideo.model.Lezione;
import com.roma3.infovideo.R;
import com.roma3.infovideo.utility.AuleLibereUtility;

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
import android.widget.ArrayAdapter;
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
public class AuleLibereActivity extends MenuListActivity {

    private ArrayList<Lezione> lezioni;
    private ArrayList<String> auleLibere;
    private int ora;
    private int minuti;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ThemeManager.setTheme(this);
        //int sdk = Build.VERSION.SDK_INT;

        Bundle bundle = getIntent().getExtras();
        this.ora = bundle.getInt("ora");
        this.minuti = bundle.getInt("minuti");
        String selectedFaculty = bundle.getString("selectedFaculty");
        String url = bundle.getString("url");

        //if (sdk < 11) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.aule_libere);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        LinearLayout title = (LinearLayout) findViewById(R.id.title);
        title.setBackgroundColor(Color.parseColor(bundle.getString("color")));
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("Facoltà di " + selectedFaculty);
        TitleColorManager.white(selectedFaculty, titleText, this);
        //}

        new DownloadXmlTask(this).execute(selectedFaculty, url);
    }


    private class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<String>> {

        private ListActivity activity;
        private ProgressDialog dialog;

        public DownloadXmlTask(ListActivity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Cerco le aule libere..");
            this.dialog.show();
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> aule = new ArrayList<String>();
            String faculty = params[0];
            String url = params[1];

            try {
                LessonsDownloader downloader = new LessonsDownloader();
                lezioni = downloader.donwloadLessons(faculty, url, new Date(), activity);
                aule = downloader.downloadAule(faculty, url, new Date(), activity);
            } catch (LessonsDownloadingException e) {
                Log.e("LEZIONI ROMA TRE", e.getMessage());
            }
            return aule;
        }

        protected void onPostExecute(ArrayList<String> result) {

            if (dialog.isShowing())
                dialog.dismiss();

            if (result == null) {
                Toast.makeText(AuleLibereActivity.this, "Errore!", Toast.LENGTH_LONG).show();

            } else if (result.size() == 0) {
                Toast.makeText(AuleLibereActivity.this, "Non sono presenti aule registrate!", Toast.LENGTH_LONG).show();
            } else {
                AuleLibereUtility utility = new AuleLibereUtility();
                auleLibere = utility.findAuleLibere(result, lezioni, ora, minuti);

                String[] datas = auleLibere.toArray(new String[auleLibere.size()]);

                Toast.makeText(AuleLibereActivity.this, "Lettura completata!", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> auleAdapter = new ArrayAdapter<String>(this.activity, R.layout.custom_list_item, datas);
                setListAdapter(auleAdapter);
            }

        }
    }
}
