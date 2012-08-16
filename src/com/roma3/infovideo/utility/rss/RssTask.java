package com.roma3.infovideo.utility.rss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.roma3.infovideo.model.RssItem;
import com.roma3.infovideo.activities.adapter.RssAdapter;
import com.roma3.infovideo.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class RssTask extends AsyncTask<String, Void, ArrayList<RssItem>> {

    private Timer timer;

    private int scroll;

    private Activity activity;

    public RssTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<RssItem> doInBackground(String... params) {
        ArrayList<RssItem> rss = null;
        try {
            rss = new RssDownloader().downloadRss(params[0]);
        } catch (RssDownloadingException e) {
            Log.e("LEZIONI ROMA TRE", e.getMessage());
        }
        return rss;
    }

    @Override
    protected void onPostExecute(ArrayList<RssItem> rssItems) {
        final ListView listView = (ListView) activity.findViewById(R.id.rss_list);
        if (rssItems != null) {
            listView.setAdapter(new RssAdapter(this.activity, R.layout.rss_item, rssItems));

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
            boolean autoscroll = pref.getBoolean("rss_autoscroll", true);

            if (autoscroll) {
                scroll = 0;
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (scroll == listView.getCount() - 1) {
                            scroll = 0;
                            listView.smoothScrollToPosition(scroll);
                        } else {
                            scroll++;
                            listView.smoothScrollToPosition(scroll);
                        }
                    }
                }, 2000, 3500);
            }
        } else {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo == null) {
                TextView textView = (TextView) activity.findViewById(R.id.rss_connection);
                textView.bringToFront();
            }
        }
    }

}
