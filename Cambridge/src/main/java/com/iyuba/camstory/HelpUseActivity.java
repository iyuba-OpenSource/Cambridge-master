package com.iyuba.camstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;

import com.iyuba.camstory.adpater.HelpViewAdapter;
import com.iyuba.camstory.widget.PageIndicator;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class HelpUseActivity extends Activity {
	private static final String TAG = HelpUseActivity.class.getSimpleName();

	private ViewPager viewPager;
	private PageIndicator pi;
	private HelpViewAdapter hvAdapter;
	private ArrayList<Integer> idlist;

	private int lastIntoCount;
	private int goInfo = 0;// 0=第一次使用程序 1=从设置界面进入

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.help_use);
		goInfo = this.getIntent().getIntExtra("isFirstInfo", 0);
		pi = findViewById(R.id.pageIndicator);
		idlist = new ArrayList<Integer>();
		idlist.add(R.mipmap.help0);
		idlist.add(R.mipmap.help1);
		idlist.add(R.mipmap.help2);
		idlist.add(R.mipmap.help3);
		idlist.add(R.mipmap.help4);
		hvAdapter = new HelpViewAdapter(idlist);
		viewPager = findViewById(R.id.viewpager1);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				pi.setCurrIndicator(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.e(TAG, "state : " +state);
				switch (state) { // state chain when scrolling : 1-->2-->0
				case 0: //什么都没做
					if (viewPager.getCurrentItem() == idlist.size() - 1) {
						lastIntoCount = lastIntoCount + 1;
					}
					break;
				case 1: //正在滑动
					Log.e(TAG, "current : " + viewPager.getCurrentItem());
					break;
				case 2: //滑动完毕
					lastIntoCount = 0;
					break;
				}
				Log.e(TAG, "lastIntoCount : " + lastIntoCount);
				if (lastIntoCount > 1) {
					if (state == 0) {
						if (goInfo == 0) {
							Intent intent = new Intent();
							//intent.setClass(HelpUseActivity.this, MainActivity1.class);
//							intent.setClass(HelpUseActivity.this, MainActivity.class);
							//startActivity(intent);
						}
						finish();
					}
				}
			}
		});
		
		viewPager.setAdapter(hvAdapter);
		viewPager.setOffscreenPageLimit(1);
		pi.setIndicator(idlist.size());
		pi.setCurrIndicator(0);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}