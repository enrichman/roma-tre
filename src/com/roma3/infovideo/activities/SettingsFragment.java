package com.roma3.infovideo.activities;

import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.Preference.OnPreferenceClickListener;
import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.SharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.roma3.infovideo.R;


/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference myPref = (Preference) findPreference("restart");
        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                     public boolean onPreferenceClick(Preference preference) {
                    	 Intent i = new Intent(preference.getContext(), LezioniRoma3Activity.class);
                     	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         ((Activity)preference.getContext()).finish();
                         startActivity(i);
                         return true;
                     }
                 });
	}

    @Override
	public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
	public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("theme") || s.equals("rss_autoscroll")) {
        	Preference myPref = (Preference) findPreference("restart");
        	myPref.setSummary("-- Riavvia l'app! --");
        }
    }
}
