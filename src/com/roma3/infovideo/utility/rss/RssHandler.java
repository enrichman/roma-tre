package com.roma3.infovideo.utility.rss;

import android.text.Html;
import android.util.Log;
import com.roma3.infovideo.model.RssItem;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;
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
public class RssHandler extends DefaultHandler {

    private ArrayList<RssItem> rss = new ArrayList<RssItem>();
    private RssItem item;
    private boolean inItem;

    private String value;
    private StringBuffer buffer;

    @Override
    public void startElement(
            String nameSpaceURI,
            String localName,
            String qName,
            Attributes atts
    ) {

        buffer = new StringBuffer();

        if (localName.equals("item")) {
            item = new RssItem();
            inItem = true;
        }
    }

    public void endElement(
            String uri,
            String localName,
            String qName) {

        value = buffer.toString();
        buffer.setLength(0);

        value = Html.fromHtml(value).toString();

        if (localName.equals("pubDate") && inItem) {
            item.setPublishedDate(value);
        } else if (localName.equals("title") && inItem) {
            item.setTitle(value);
        } else if (localName.equals("link") && inItem) {
            URL url = null;
            try {
                url = new URL(value);
            } catch (MalformedURLException e) {
                Log.e("LEZIONI ROMA TRE", "error while creating url from [" + value + "]");
            }
            if (url != null) {
                item.setLink(url);
            }
        } else if (localName.equals("description") && inItem) {
            item.setDescription(value);
        } else if (localName.equals("item")) {
            rss.add(item);
            inItem = false;
        }
    }

    public void characters(char[] ch, int start, int end) {
        buffer.append(new String(ch, start, end));
    }

    public ArrayList<RssItem> getRss() {
        return this.rss;
    }

}
