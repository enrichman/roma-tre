package com.roma3.infovideo.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.roma3.infovideo.model.RssItem;
import com.roma3.infovideo.R;

import java.util.ArrayList;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class RssAdapter extends ArrayAdapter<RssItem> {

    private ArrayList<RssItem> rss;

    public RssAdapter(Context context, int textViewResourceId, ArrayList<RssItem> rss) {
        super(context, textViewResourceId, rss);
        this.rss = rss;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.rss_item, null);
        }

        RssItem item = this.rss.get(position);

        if (item != null) {
            if (item.getPublishedDate() != null) {
                TextView date = (TextView) v.findViewById(R.id.rss_date);
                date.setText(item.getPublishedDate());
            }
            if (item.getTitle() != null) {
                TextView title = (TextView) v.findViewById(R.id.rss_title);
                title.setText(item.getTitle());
            }
            if (item.getDescription() != null) {
                TextView description = (TextView) v.findViewById(R.id.rss_description);
                description.setText(item.getDescription());
            }
            if (item.getLink() != null) {
                TextView link = (TextView) v.findViewById(R.id.rss_link);
                link.setText(item.getLink().toString());
            }
        }

        return v;
    }
}
