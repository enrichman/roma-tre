package com.roma3.infovideo.activities;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	protected String[] CONTENT;

	// fragments to instantiate in the viewpager
	private List<Fragment> fragments;

	// constructor
	public PagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
		List<String> temp = new ArrayList<String>();
		for(Fragment f : fragments) {
			f.getActivity();
			temp.add(((LezioniFragment)f).getTitle());
		}
		CONTENT = temp.toArray(new String[temp.size()]);
	}

	// return access to fragment from position, required override
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	// number of fragments in list, required override
	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
	public String[] getContent() {
		return CONTENT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(CONTENT==null) {
			return "";
		}
		return CONTENT[position % CONTENT.length];
	}
}