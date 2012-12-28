package com.roma3.infovideo.activities;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockListFragment;
import com.roma3.infovideo.R;
import com.roma3.infovideo.activities.adapter.LezioneAdapter;
import com.roma3.infovideo.model.Lezione;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LezioniFragment extends SherlockListFragment {
	
	private ArrayList<Lezione> lezioni;
	public void setLessons(ArrayList<Lezione> lezioni) {	this.lezioni = lezioni; }
	
	private String title;
	public String getTitle() {	return title;	}
	public void setTitle(String title) {	this.title = title;	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		CercaLezioni lez = (CercaLezioni)this.getActivity();
		if(lez.result!=null) {
			lezioni = (ArrayList<Lezione>) lez.aula2lezioni.get(title);
			LezioneAdapter lezioneAdapter = new LezioneAdapter(this.getActivity(), R.layout.row, lezioni);
        	setListAdapter(lezioneAdapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		View view = (LinearLayout)inflater.inflate(R.layout.fragment,container,false);
		return view;
	}

}
