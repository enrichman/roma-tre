package com.roma3.infovideo.utility.rss;

import android.util.Log;

import com.roma3.infovideo.model.RssItem;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
public class RssDownloader {

    public ArrayList<RssItem> downloadRss(String url) throws RssDownloadingException {
        return download(url).getRss();
    }

    private RssHandler download(String urlStr) throws RssDownloadingException {
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            final String errMsg = "the url is not well formed [" + urlStr + "]";
            Log.e("LEZIONI ROMA TRE", errMsg);
            throw new RssDownloadingException(errMsg, e);
        }
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        XMLReader reader;
        try {
            sp = spf.newSAXParser();
            reader = sp.getXMLReader();
        } catch (ParserConfigurationException e) {
            throw new RssDownloadingException(
                    "The creation of the SAXParser went wrong!", e
            );
        } catch (SAXException e) {
            throw new RssDownloadingException(
                    "The creation of the SAXParser went wrong!", e
            );
        }
        RssHandler handler = new RssHandler();
        reader.setContentHandler(handler);

        URLConnection conn;
        try {
            conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
        } catch (IOException e) {
            throw new RssDownloadingException(
                    "Error while opening the connection", e
            );
        }
        InputStream in;
        try {
            in = conn.getInputStream();
        } catch (IOException e) {
            throw new RssDownloadingException(
                    "Error while getting the InputStream", e
            );
        }
        try {
            reader.parse(new InputSource(new InputStreamReader(in)));
        }
        catch (IOException e) {
            throw new RssDownloadingException(
                    "Error while parsing the rss feed", e
            );
        }
        catch (SAXException e) {
            throw new RssDownloadingException(
                    "Error while parsing the rss feed", e
            );
        }
        return handler;
    }
}
