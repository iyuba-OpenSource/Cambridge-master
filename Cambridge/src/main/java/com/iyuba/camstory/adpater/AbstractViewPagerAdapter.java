package com.iyuba.camstory.adpater;

import java.util.List;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

public abstract class AbstractViewPagerAdapter<T> extends PagerAdapter {
	protected List<T> mData;
	private SparseArray<View> mViews;

	public AbstractViewPagerAdapter(List<T> data) {
		mData = data;
		mViews = new SparseArray<View>(data.size());
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position);
		if (view == null){
			view = newView(position);
			mViews.put(position, view);
		}
		container.addView(view);
		return view;
	}

	public abstract View newView(int position);
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}
	
	public T getItem(int position){
		return mData.get(position);
	}
	
}
