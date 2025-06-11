package com.iyuba.camstory.adpater;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class DetailViewPager extends FragmentPagerAdapter {

	private FragmentManager fragmentManager;
	private ArrayList<Fragment> fragments;

	public DetailViewPager(FragmentManager paramFragmentManager,
						   ArrayList<Fragment> paramArrayList) {
		super(paramFragmentManager);
		this.fragmentManager = paramFragmentManager;
		this.fragments = paramArrayList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if (this.fragments != null)
			return this.fragments.get(arg0);
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (this.fragments != null)
			return this.fragments.size();
		return 0;
	}

}
