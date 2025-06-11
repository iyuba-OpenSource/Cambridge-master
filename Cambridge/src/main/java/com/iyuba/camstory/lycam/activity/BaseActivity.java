package com.iyuba.camstory.lycam.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by su on 16/5/9.
 * Acitivty基类
 */
public class BaseActivity extends AppCompatActivity {
	protected static final int READ_PHONE_STATE = 1;
	protected static final int RECORD_PERMISSION = 2;
	protected static final int WRITE_SETTINGS = 3;
	protected String mCurrentFragmentTag;
	protected FragmentManager fManager;
	protected Context context;
	//是否启用沉侵式状态栏
	protected boolean showSystemTintBackground = true;

	//请求权限
	public boolean requestPermissions() {
		//这里直接屏蔽掉，需要的时候在进行读取
		//已进入app就读取，直接过不了合规审核
//		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//				!= PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
//					READ_PHONE_STATE);
//			return false;
//		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == READ_PHONE_STATE) {
			for (int grant : grantResults) {
				if (grant == PackageManager.PERMISSION_DENIED) {
					finish();
					return;
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestPermissions();
		fManager = getSupportFragmentManager();
		context = this;
	}

	/**
	 * Fragment显示管理
	 *
	 * @param tag  标识
	 * @param id   容器Id
	 * @param args 通信内容
	 * @return
	 */
	protected Fragment showFrament(String tag, int id, Bundle args) {
		Fragment fragmentCurrent = fManager.findFragmentByTag(mCurrentFragmentTag);
		Fragment fragmentReplace = fManager.findFragmentByTag(tag);
		if (fragmentReplace == null)
			fragmentReplace = getNewFragment(tag, args);
		FragmentTransaction transaction = fManager.beginTransaction();
		if (fragmentCurrent != null)
			transaction.hide(fragmentCurrent);
		if (!(fragmentReplace.isAdded()))
			transaction.add(id, fragmentReplace, tag);
		transaction.show(fragmentReplace);
		transaction.commit();
		mCurrentFragmentTag = tag;
		return fragmentReplace;
	}

	/**
	 * 获取Fragment实例
	 *
	 * @param tag  标识
	 * @param args 通信内容
	 * @return
	 */
	protected Fragment getNewFragment(String tag, Bundle args) {
		Fragment fragment = null;
		return fragment;
	}

	/**
	 * 判断软键盘是否隐藏
	 *
	 * @return
	 */
	protected boolean isKeyBoardAutoHidden() {
		return true;
	}

	/**
	 * 事件分发
	 *
	 * @param ev
	 * @return
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isKeyBoardAutoHidden()) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				View v = getCurrentFocus();
				if (isShouldHideKeyboard(v, ev)) {
					hideKeyboard(v.getWindowToken());
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideKeyboard(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = {0, 0};
			v.getLocationInWindow(l);
			int left = l[0],
					top = l[1],
					bottom = top + v.getHeight(),
					right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 获取InputMethodManager，隐藏软键盘
	 *
	 * @param token
	 */
	private void hideKeyboard(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 获得导航栏高度
	 *
	 * @param activity
	 * @return
	 */
	public int getNavigationBarHeight(Activity activity) {
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		//获取NavigationBar的高度
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	/*--------------------对话框--------------------*/

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	public void showLoadingDialog() {
	}

	public void dismissLoadingDialog() {
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
