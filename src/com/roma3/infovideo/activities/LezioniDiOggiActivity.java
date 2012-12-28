package com.roma3.infovideo.activities;

import org.holoeverywhere.app.ProgressDialog;

import com.roma3.infovideo.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.roma3.infovideo.model.Lezione;
import com.roma3.infovideo.utility.ThemeManager;
import com.roma3.infovideo.utility.lessons.LessonsDownloader;
import com.roma3.infovideo.utility.lessons.LessonsDownloadingException;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
public class LezioniDiOggiActivity extends CercaLezioni {

    // list contains fragments to instantiate in the viewpager
    List<Fragment> fragments = new Vector<Fragment>();
    // page adapter between fragment list and view pager
    private PagerAdapter mPagerAdapter;
    // view pager
    private ViewPager mPager;

    public void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);

        //int sdk = Build.VERSION.SDK_INT;

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        String selectedFaculty = bundle.getString("selectedFaculty");

        //if (sdk < 11) {
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.second);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
//        LinearLayout title = (LinearLayout) findViewById(R.id.title);
//        title.setBackgroundColor(Color.parseColor(bundle.getString("color")));
//        TextView titleText = (TextView) findViewById(R.id.title_text);
//        titleText.setText("Facolt√† di " + selectedFaculty);
//        TitleColorManager.white(selectedFaculty, titleText, this);
//        }



        new DownloadXmlTask(this).execute(selectedFaculty, url);

    }

    public FragmentPagerAdapter getFragmentPagerAdapter() {
        return this.mPagerAdapter;
    }

    protected class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<Lezione>> {

        private FragmentActivity activity;
        private ProgressDialog dialog;

        public DownloadXmlTask(FragmentActivity activity) {
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
                LezioniDiOggiActivity.this.result = result;

                aula2lezioni = new HashMap<String, List<Lezione>>();
                for(Lezione l : result) {
                    List<Lezione> list = aula2lezioni.get(l.getAula());
                    if(list == null) {
                        list = new ArrayList<Lezione>();
                        LezioniFragment f = (LezioniFragment) Fragment.instantiate(activity,LezioniFragment.class.getName());
                        f.setTitle(l.getAula());
                        fragments.add(f);
                    }
                    list.add(l);
                    aula2lezioni.put(l.getAula(), list);
                }

                mPagerAdapter = new PagerAdapter(activity.getSupportFragmentManager(), fragments);
                mPager = (ViewPager)activity.findViewById(R.id.pager);
                mPager.setAdapter(mPagerAdapter);

                //Bind the title indicator to the adapter
                TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
                titleIndicator.setViewPager(mPager);
            } else {
                Toast.makeText(LezioniDiOggiActivity.this, "Errore!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

