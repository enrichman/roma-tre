package com.roma3.infovideo.utility.lessons;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.roma3.infovideo.model.Lezione;

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
import java.util.Calendar;
import java.util.Date;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class LessonsDownloader {

    public ArrayList<String> downloadAule(String faculty, String url, Date date, Context context)
            throws LessonsDownloadingException {
        return download(faculty, url, date, context).getStringAule();
    }

    public ArrayList<Lezione> donwloadLessons(String faculty, String url, Date date, Context context)
            throws LessonsDownloadingException {
        return download(faculty, url, date, context).getLessons(date);
    }

    private LessonsHandler download(String faculty, String baseUrl, Date date, Context context)
            throws LessonsDownloadingException {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean saveOnSd = pref.getBoolean("saveOnSd", false);

        String urlXml = getUrl(baseUrl, date);
        URL url;

        try {
            url = new URL(urlXml);
        } catch (MalformedURLException e) {
            throw new LessonsDownloadingException(
                    "The URL is not well formed [" + urlXml + "]", e
            );
        }
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        XMLReader reader;
        try {
            sp = spf.newSAXParser();
            reader = sp.getXMLReader();
        } catch (ParserConfigurationException e) {
            throw new LessonsDownloadingException(
                    "The creation of the SAXParser went wrong!", e
            );
        } catch (SAXException e) {
            throw new LessonsDownloadingException(
                    "The creation of the SAXParser went wrong!", e
            );
        }
        LessonsHandler handler = new LessonsHandler();
        reader.setContentHandler(handler);

        String fileName = getFileName(date);
        File dir;

        if (saveOnSd && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();
            dir = new File(sdcard.getAbsolutePath() + "/Lezioni Roma Tre/" + faculty);
            if (dir.mkdirs()) {
                Log.i("LEZIONI ROMA TRE", "Directory [" + faculty + "] created!");
            }
        } else {
            dir = context.getDir(faculty, Context.MODE_PRIVATE);
        }
        File f = new File(dir, fileName);
        if (!f.exists()) {
            Log.i("LEZIONI ROMA TRE","The file [" + fileName + "] does not exist!");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                throw new LessonsDownloadingException(
                        "Error while opening the stream for [" + fileName + "]", e
                );
            }
            URLConnection conn;
            try {
                conn = url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(10000);
            } catch (IOException e) {
                f.delete();
                try{
                    fos.close();
                } catch (IOException e2) {
                    f.delete();
                    throw new LessonsDownloadingException("Error while closing the InputStream", e2);
                }
                throw new LessonsDownloadingException("Error while opening the connection", e);
            }
            InputStream in;
            try {
                in = conn.getInputStream();//context.getAssets().open("xml/ing.xml");
            } catch (IOException e) {
                f.delete();
                try{
                    fos.close();
                } catch (IOException e2) {
                    f.delete();
                    throw new LessonsDownloadingException("Error while closing the InputStream", e2);
                }
                throw new LessonsDownloadingException("Error while getting the InputStream", e);
            }
            byte[] buffer = new byte[1024];
            int len = 0;
            try {
                while ((len = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                f.delete();
                throw new LessonsDownloadingException("Error while reading the InputStream", e);
            }
            try{
                fos.close();
                in.close();
            } catch (IOException e) {
                f.delete();
                throw new LessonsDownloadingException("Error while closing the InputStream", e);
            }
            Log.i("LEZIONI ROMA TRE","File [" + fileName + "] created!");
        } else {
            Log.i("LEZIONI ROMA TRE","The file [" + fileName + "] already exist!");
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new LessonsDownloadingException(
                    "Error while opening the file [" + fileName + "] - FILE NOT FOUND", e
            );
        }
        try {
            reader.parse(new InputSource(new InputStreamReader(fis)));
        }
        catch (IOException e) {
            throw new LessonsDownloadingException(
                    "Error while parsing the lessons", e
            );
        }
        catch (SAXException e) {
            throw new LessonsDownloadingException(
                    "Error while parsing the lessons", e
            );
        }
        Log.i("LEZIONI ROMA TRE","File [" + fileName + "] parsed!");
        try {
            fis.close();
        } catch (IOException e) {
            throw new LessonsDownloadingException(
                    "Error while closing the InputStreamReader", e
            );
        }
        return handler;
    }

    private String getUrl(String baseUrl, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = "&from_Month=" + prepare(calendar.get(Calendar.MONTH)+1);
        String day = "&from_Day=" + prepare(calendar.get(Calendar.DAY_OF_MONTH));
        String year = "&from_Year=" + String.valueOf((calendar.get(Calendar.YEAR)));
        // TODO (mid) add the day of downloads as a property!
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String toMonth = "&to_Month=" + prepare(calendar.get(Calendar.MONTH)+1);
        String toDay = "&to_Day=" + prepare(calendar.get(Calendar.DAY_OF_MONTH));
        String toYear = "&to_Year=" + String.valueOf(calendar.get(Calendar.YEAR));
        String end_uri = "&export_type=xml&save_entry=Esporta+calendario";
        return  baseUrl + "?" + month + day + year + toMonth + toDay + toYear + end_uri;
    }

    private String prepare(int date) {
        if(date<10)
            return "0" + String.valueOf(date);
        return String.valueOf(date);
    }

    private String getFileName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return prepare(calendar.get(Calendar.DAY_OF_MONTH)) + "-" +
                prepare(calendar.get(Calendar.MONTH)+1) + "-" +
                calendar.get(Calendar.YEAR) + ".xml";
    }
}
