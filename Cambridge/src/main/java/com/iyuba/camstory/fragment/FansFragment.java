package com.iyuba.camstory.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.iyuba.camstory.MessageLetterContentActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.FansGridViewAdapter;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.manager.SocialDataManager;

public class FansFragment extends Fragment implements OnItemClickListener{
	
	private GridView fansGridView;
	private FansGridViewAdapter fansGridViewAdapter;
	private Context mContext;
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		mContext = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.fragment_fans, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO 自动生成的方法存根
		fansGridView = view.findViewById(R.id.fansGridView);
		fansGridView.setOnItemClickListener(this);
		if (SocialDataManager.getInstance().fans.isEmpty()) {
			SocialDataManager.getInstance().getFansList(new CommonCallBack() {
				@Override
				public void onPositive(Object object) {
					// TODO 自动生成的方法存根
					fansGridViewAdapter = new FansGridViewAdapter(mContext,SocialDataManager.getInstance().fans);
					handler.sendEmptyMessage(0);
				}
				@Override
				public void onNegative(Object object) {
					// TODO 自动生成的方法存根
				}
			});
		}
		else {
			fansGridViewAdapter = new FansGridViewAdapter(mContext,SocialDataManager.getInstance().fans);
			handler.sendEmptyMessage(0);
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				fansGridView.setAdapter(fansGridViewAdapter);
				fansGridView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO 自动生成的方法存根
						if (scrollState == SCROLL_STATE_IDLE
								&&view.getLastVisiblePosition() == view.getCount()-1) {
							SocialDataManager.getInstance().getFansList(
									new CommonCallBack() {
										@Override
										public void onPositive(Object object) {
											// TODO 自动生成的方法存根
											fansGridView.post(new Runnable() {
												
												@Override
												public void run() {
													// TODO 自动生成的方法存根
													fansGridViewAdapter.notifyDataSetChanged();
												}
											});
										}
										@Override
										public void onNegative(Object object) {
											// TODO 自动生成的方法存根
											
										}
									});
						}
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO 自动生成的方法存根
					}
				});
			}
		}
    };

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		FansBean.DataBean fans = SocialDataManager.getInstance().fans.get(position);
		Intent intent=new Intent();
		intent.putExtra("friendid",fans.getUid());
		intent.putExtra("currentname",fans.getUsername());
//		intent.putExtra("mutualAttention","mutualAttention");
		intent.setClass(mContext,MessageLetterContentActivity.class);
		startActivity(intent);
	}
	
	

	
}
