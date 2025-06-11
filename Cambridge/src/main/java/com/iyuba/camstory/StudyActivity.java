//
//package com.iyuba.cambridge;
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Field;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewConfiguration;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.CompoundButton;
//import android.widget.HorizontalScrollView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qq.QQ;
//import cn.sharesdk.tencent.qzone.QZone;
//import cn.sharesdk.wechat.favorite.WechatFavorite;
//import cn.sharesdk.wechat.friends.Wechat;
//import cn.sharesdk.wechat.moments.WechatMoments;
//
//
//import com.android.volley.Request;
////import com.google.ads.AdRequest;
////import com.google.ads.AdSize;
////import com.google.ads.AdView;
//import com.iyuba.cambridge.frame.CrashApplication;
//import com.iyuba.cambridge.listener.AddCreditsRequest;
//import com.iyuba.cambridge.listener.RequestCallBack;
//import com.iyuba.cambridge.manager.AccountManager;
//import com.iyuba.cambridge.manager.NightModeManager;
//import com.iyuba.cambridge.manager.VoaDataManager;
//import com.iyuba.cambridge.sqlite.mode.Voa;
//import com.iyuba.cambridge.sqlite.op.VoaOp;
//import com.iyuba.cambridge.widget.CallPlatform;
//import com.iyuba.cambridge.widget.CustomToast;
//import com.iyuba.cambridge.widget.ShareDialog;
//import com.iyuba.voa.activity.setting.Constant;
//import com.iyuba.voa.activity.setting.SettingConfig;
//import com.iyuba.voa.frame.components.ConfigManager;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.utils.DiskCacheUtils;
//import com.umeng.analytics.MobclickAgent;
//
//public class StudyActivity extends Activity implements
//		OnClickListener, OnRefreshDataListener {
//	private static final String TAG = StudyActivity.class.getSimpleName();
//	private Context mContext;
//
//	private WindowManager mWindowManager;
//	private NightModeManager nightModeManager;
//
//	//private ActionBar actionBar;
//	// page content
//	private View backView;
//	private ViewPager viewpager;
//	private FragmentStatePagerAdapter viewPagerAdapter;
////	private AdView adView;
//	// scrolling navigation bar
//	private HorizontalScrollView mHorizontalScrollView;
//	private LinearLayout ll;
//	private TextView infoButton, testButton, videoButton, readButton,
//			commentButton, voaWordsButton;
//
//	private boolean isConnected;
//	private int currentPage = 2;
//	private VoaOp vop;
//
//	private HeadsetPlugReceiver headsetPlugReceiver;
//	private StudyBaseFragment infoFragment, testFragment, videoFragment,
//			readFragment, wordFragment, commentFragment;
//	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//
//	private ShareDialog shareDialog;
//	private Voa sharedVoa;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.study);
//		mContext = this;
//		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		nightModeManager = new NightModeManager(mWindowManager, mContext);
//		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//		Log.e(TAG, "when onCreate vdm.detailsize: " +
//				VoaDataManager.getInstance().voaDetailsTemp.size());
//		String from = getIntent().getExtras().getString("from");
//		if (from != null && from.equals("collection")) {
//			AlertDialog alert = new AlertDialog.Builder(this).create();
//			alert.setTitle("");
//			alert.setMessage(getResources().getString(R.string.sutdy_justlocal));
//			alert.setIcon(android.R.drawable.ic_dialog_alert);
//			alert.setButton(AlertDialog.BUTTON_POSITIVE,
//					getResources().getString(R.string.alert_btn_ok),
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							ConfigManager.Instance(mContext).putBoolean("play_local_only",
//									true);
//						}
//					});
//			alert.setButton(AlertDialog.BUTTON_NEGATIVE,
//					getResources().getString(R.string.alert_btn_cancel),
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							ConfigManager.Instance(mContext).putBoolean("play_local_only",
//									false);
//						}
//					});
//			alert.show();
//		} else {
//			ConfigManager.Instance(mContext).putBoolean("play_local_only", false);
//		}
//
//		isConnected = NetWorkStateUtil.isConnectingToInternet();
//		if (SettingConfig.Instance(mContext).isScreenLit()) {
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		}
//		if (!VoaDataManager.getInstance().voaTemp.favourite
//				&& VoaDataManager.getInstance().collectPlay) {
//			VoaDataManager.getInstance().collectPlay = false;
//			CustomToast.showToast(mContext, "收藏列表播放已取消", 2500);
//		}
//		// initiate the viewpager
//		infoFragment = new StudyInfoFragment();
//		if (Constant.getAppid().equals("201") || Constant.getAppid().equals("212")
//				|| Constant.getAppid().equals("217")) {
//			testFragment = new StudyVoaTestFragment();
//		} else if (Constant.getAppid().equals("215")
//				|| Constant.getAppid().equals("221")) {
//			testFragment = new StudyBBCTestFragment();
//		}
//		videoFragment = new StudyVideoFragment();
//		readFragment = new StudyReadFragment3();
//		if (Constant.getAppid().equals("201") || Constant.getAppid().equals("212")
//				|| Constant.getAppid().equals("217")) {
//			wordFragment = new StudyWordFragment();
//		} else if (!Constant.getAppid().equals("213")) {
//			wordFragment = new StudyBBCWordFragment();
//		}
//		commentFragment = new StudyCommentFragment();
//		fragments.add(infoFragment);
//		if (!Constant.getAppid().equals("213")
//				&& !Constant.getAppid().equals("229")) {
//			fragments.add(wordFragment);
//		}
//		fragments.add(videoFragment);
//		fragments.add(readFragment);
//		if (!Constant.getAppid().equals("213")
//				&& !Constant.getAppid().equals("229")) {
//			fragments.add(testFragment);
//		}
//		fragments.add(commentFragment);
//		viewpager = (ViewPager) findViewById(R.id.mainBody);
//		// 头部
//		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsc);
//		mHorizontalScrollView
//				.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_ALWAYS);
//		mHorizontalScrollView.setSmoothScrollingEnabled(true);
//		ll = (LinearLayout) findViewById(R.id.l1_select);
//		// 初始化头部
//		initActionBar();
//
//		infoButton = (TextView) findViewById(R.id.study_button_info);
//		testButton = (TextView) findViewById(R.id.study_button_exercise);
//		readButton = (TextView) findViewById(R.id.study_button_read);
//		voaWordsButton = (TextView) findViewById(R.id.study_button_voawords);
//		commentButton = (TextView) findViewById(R.id.study_button_comment);
//		videoButton = (TextView) findViewById(R.id.study_button_video);
//		backView = findViewById(R.id.backlayout);
//		backView.setBackgroundColor(Constant.getBackgroundColor());
//		if (Constant.getAppid().equals("213") || Constant.getAppid().equals("229")) {
//			currentPage = 1;
//			testButton.setVisibility(View.GONE);
//			voaWordsButton.setVisibility(View.GONE);
//		}
//
//		NotificationService.isStudyExit = false;
//		// 初始化视频信息
//		vop = new VoaOp();
//		infoButton.setOnClickListener(this);
//		testButton.setOnClickListener(this);
//		videoButton.setOnClickListener(this);
//		readButton.setOnClickListener(this);
//		voaWordsButton.setOnClickListener(this);
//		commentButton.setOnClickListener(this);
//		viewPagerAdapter = new MainPagerAdapter(this.getSupportFragmentManager(),
//				fragments);
//		registerHeadsetPlugReceiver();
//		registerReceiver(mBatInfoReceiver, new IntentFilter());
//		getSupportActionBar().setTitle(VoaDataManager.getInstance().voaTemp.title);
//		viewpager.setAdapter(viewPagerAdapter);
//		viewpager.addOnPageChangeListener(new OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int position) {
//				currentPage = viewpager.getCurrentItem();
//				setBackGround(currentPage);
//				mHorizontalScrollView.smoothScrollTo(
//						(ll.getChildAt(position).getLeft()) - 100, 0);
//				if ((!Constant.getAppid().equals("213") && currentPage == 3)
//						|| (!Constant.getAppid().equals("229") && currentPage == 3)) {
//					VideoView vv = Background.vv;
//					if (vv != null) {
//						// vv.pause();
//						vv.stopPlayback2();
//					}
//				} else if ((Constant.getAppid().equals("213") && currentPage == 2)
//						|| (Constant.getAppid().equals("229") && currentPage == 2)) {
//					VideoView vv = Background.vv;
//					if (vv != null) {
//						// vv.pause();
//						vv.stopPlayback2();
//					}
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});
//
//		//initAD();
//
//		refreshReadCount();
//
//		VoaStudyManager.newRecord();
//	}
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//		if (!Constant.getAppid().equals("213")
//				&& !Constant.getAppid().equals("229")) {
//			if (currentPage != 4) {
//				viewpager.setCurrentItem(2);
//			} else {
//				viewpager.setCurrentItem(currentPage);
//			}
//		}
//		if (Constant.getAppid().equals("213") || Constant.getAppid().equals("229")) {
//			if (currentPage != 4) {
//				viewpager.setCurrentItem(1);
//			} else {
//				viewpager.setCurrentItem(currentPage);
//			}
//		}
//	}
//
//	@Override
//	public void onContentChanged() {
//		super.onContentChanged();
//		Log.e(TAG, "onContentChanged");
//	}
//
//	private void initActionBar() {
//		setOverflowShowingAlways();
//		actionBar = this.getSupportActionBar();
//		actionBar.setTitle(VoaDataManager.getInstance().voaTemp == null ? ""
//				: VoaDataManager.getInstance().voaTemp.title);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayShowHomeEnabled(true);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setDisplayUseLogoEnabled(true);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.titlebar_lightgray_bg));
//	}
//
//	private void setOverflowShowingAlways() {
//		ViewConfiguration config = ViewConfiguration.get(mContext);
//		try {
//			Field menuKeyField = ViewConfiguration.class
//					.getDeclaredField("sHasPermanentMenuKey");
//			menuKeyField.setAccessible(true);
//			menuKeyField.setBoolean(config, false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.study_video_menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int itemId = item.getItemId();
//		if (itemId == R.id.studymenu_share) {
//			if (!isConnected)
//				CustomToast.showToast(mContext, R.string.category_check_network, 1000);
//			else {
//				// if (AccountManager.getInstance().checkUserLogin()) {
//				// prepareMessage();
//				showShareDialog();
//				// } else {
//				// Intent intent = new Intent();
//				// intent.setClass(mContext, LoginActivity.class);
//				// startActivity(intent);
//				// }
//
//			}
//		} else if (itemId == R.id.studymenu_setting) {
//			startActivity(new Intent(mContext, PreferancePLaySettingActivity.class));
//		} else if (itemId == R.id.studymenu_refresh) {
//			VoaDataManager.getInstance().getNetDetail(
//					VoaDataManager.getInstance().voaTemp, new DetailCallback() {
//						@Override
//						public void onDetail(ArrayList<VoaDetail> details) {
//							VoaDataManager.getInstance().voaDetailsTemp = details;
//							VoaDataManager.getInstance().setSubtitleSum(
//									VoaDataManager.getInstance().voaTemp, details);
//							Log.e("onOptionsItemSelected", "onOptionsItemSelected");
//							onDataRefresh();
//						}
//					});
//		} else if (itemId == R.id.studymenu_like) {
//			vop.updateDataByCollection(VoaDataManager.getInstance().voaTemp.voaid);
//			new AlertDialog.Builder(mContext)
//					.setTitle("收藏成功")
//					.setMessage("喜欢这篇文章的话不要独享哦，快分享给小伙伴儿们一块学英语吧~")
//					.setPositiveButton(R.string.alert_btn_ok,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int whichButton) {
//									// prepareMessage();
//									showShareDialog();
//								}
//							}).setNegativeButton(R.string.alert_btn_cancel, null).show();
//		}
//
//		else if (itemId == R.id.studymenu_bell) {
//			new AlertDialog.Builder(mContext)
//					.setTitle(mContext.getResources().getString(R.string.alert))
//					.setMessage(R.string.setting_wakeup_set)
//					.setPositiveButton(
//							mContext.getResources().getString(R.string.alert_btn_ok),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int whichButton) {
//									// 如果所选文章已经下载到本地了（有这篇文章的音频文件）
//									if (new File(Constant.getVideoAddr()
//											+ VoaDataManager.getInstance().voaTemp.voaid
//											+ Constant.getAppend()).exists()) {
//										String bellvoaid = Constant.getVideoAddr()
//												+ VoaDataManager.getInstance().voaTemp.voaid;
//										ConfigManager.Instance(mContext).putString("belladdress",
//												bellvoaid + Constant.getAppend());
//										ConfigManager.Instance(mContext).putString(
//												"bellurl",
//												Constant.getAudiourl()
//														+ VoaDataManager.getInstance().voaTemp.sound);
//										ConfigManager.Instance(mContext).putString("bellvoaid",
//												VoaDataManager.getInstance().voaTemp.voaid + "");
//										Toast.makeText(mContext, R.string.setting_wakeup_bellok,
//												2000).show();
//									} else {
//										// 弹出未下载的提示
//										CustomToast.showToast(mContext,
//												R.string.setting_wakeup_notdownload, 2000);
//									}
//
//								}
//							}).show();
//		} else if (itemId == android.R.id.home) {
//			tofinish();
//		}
//		return true;
//	}
//
//	private void showShareDialog() {
//		shareDialog = new ShareDialog(mContext);
//		shareDialog.setCancelButtonOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				shareDialog.dismiss();
//			}
//		});
//		shareDialog.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				HashMap<String, Object> item = (HashMap<String, Object>) parent
//						.getItemAtPosition(position);
//				shareDialog.dismiss();
//				// 调用分享平台
//				sharedVoa = VoaDataManager.getInstance().voaTemp;
//				new CallPlatform(item, sharedVoa, pal);
//			}
//		});
//	}
//
//	private PlatformActionListener pal = new PlatformActionListener() {
//
//		@Override
//		public void onError(Platform platform, int arg1, Throwable arg2) {
//			CustomToast.showToast(mContext, "分享失败", 1000);
//		}
//
//		@Override
//		public void onComplete(Platform platform, int arg1,
//				HashMap<String, Object> arg2) {
//			int srid = 19;
//			String name = platform.getName();
//			if (name.equals(QQ.NAME) || name.equals(Wechat.NAME)
//					|| name.equals(WechatFavorite.NAME)) {
//				srid = 41;
//			} else if (name.equals(QZone.NAME) || name.equals(WechatMoments.NAME)
//					|| name.equals(SinaWeibo.NAME)) {
//				srid = 19;
//			}
//			if (AccountManager.getInstance().checkUserLogin()) {
//				RequestCallBack rc = new RequestCallBack() {
//
//					@Override
//					public void requestResult(Request result) {
//						AddCreditsRequest rq = (AddCreditsRequest) result;
//						if (rq.isShareFirstlySuccess()) {
//							String msg = "分享成功，增加了" + rq.addCredit + "积分，共有" + rq.totalCredit
//									+ "积分";
//							CustomToast.showToast(mContext, msg, 1500);
//						} else if (rq.isShareRepeatlySuccess()) {
//							CustomToast.showToast(mContext, "分享成功", 1500);
//						}
//					}
//				};
//				int uid = AccountManager.getInstance().userId;
//				AddCreditsRequest rq = new AddCreditsRequest(uid, sharedVoa.voaid,
//						srid, rc);
//				CrashApplication.getInstance().getQueue().add(rq);
//			}
//		}
//
//		@Override
//		public void onCancel(Platform platform, int arg1) {
//			CustomToast.showToast(mContext, "分享已取消", 1000);
//		}
//	};
//
//
//
//	// initiate the advertisement view
//	private void initAD() {
//		if (!AccountManager.getInstance().isVip(mContext)) {
//			adView = new AdView(this, AdSize.SMART_BANNER, "a14f6944a161b86");
//			LinearLayout layout = (LinearLayout) findViewById(R.id.backlayout);
//			AdRequest adRequest = new AdRequest();
//			HashSet<String> set = new HashSet<String>();
//			set.add("english");
//			set.add("game");
//			set.add("business");
//			set.add("music");
//			adRequest.addKeywords(set);
//			layout.addView(adView);
//			adView.loadAd(adRequest);
//		}
//	}
//
//
//
//	private void registerHeadsetPlugReceiver() {
//		headsetPlugReceiver = new HeadsetPlugReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
//		registerReceiver(headsetPlugReceiver, intentFilter);
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		NotificationService.isStudyExit = true;
//		// updateNotification();
//		unregisterReceiver(headsetPlugReceiver);
//		unregisterReceiver(mBatInfoReceiver);
//		if (!NotificationService.isNotificationCreated
//				&& NotificationService.isAppExit) {
//			android.os.Process.killProcess(android.os.Process.myPid());
//		}
//	}
//
//	@Override
//	public void finish() {
//		super.finish();
//		nightModeManager.remove();
//		if (Constant.getAppid().equals("201") || Constant.getAppid().equals("212")
//				|| Constant.getAppid().equals("217")) {
//		} else {
//			VoaStudyManager.saveLastStudyRecord();
//		}
//	}
//
//	private void sendMsgToService(String cmd) {
//		Intent intent = new Intent(getApplicationContext(),
//				NotificationService.class);
//		intent.setAction(NotificationService.ACTION_NOTIFICATION_CONTROL);
//		intent.putExtra(NotificationService.COMMAND_KEY, cmd);
//		startService(intent);
//	}
//
//	private void updateNotification() {
//		ConfigManager.Instance(mContext).putInt("lately",
//				VoaDataManager.getInstance().voaTemp.voaid);
//	}
//
//	private void sendMsgToService(String cmd, String title, int source) {
//		Intent intent = new Intent(this, NotificationService.class);
//		intent.setAction(NotificationService.ACTION_NOTIFICATION_CONTROL);
//		intent.putExtra(NotificationService.COMMAND_KEY, cmd);
//		intent.putExtra(NotificationService.TITLE, title);
//		intent.putExtra(NotificationService.SOURCE, source);
//		startService(intent);
//	}
//
//	private void setBackGround(int item) {
//		infoButton.setBackgroundResource(0);
//		infoButton.setTextColor(0xffd7d7d7);
//		testButton.setBackgroundResource(0);
//		testButton.setTextColor(0xffd7d7d7);
//		videoButton.setBackgroundResource(0);
//		videoButton.setTextColor(0xffd7d7d7);
//		readButton.setBackgroundResource(0);
//		readButton.setTextColor(0xffd7d7d7);
//		voaWordsButton.setBackgroundResource(0);
//		voaWordsButton.setTextColor(0xffd7d7d7);
//		commentButton.setBackgroundResource(0);
//		commentButton.setTextColor(0xffd7d7d7);
//		switch (item) {
//		case 0:
//			infoButton.setBackgroundResource(R.drawable.study_text_selected);
//			infoButton.setTextColor(Constant.getStudytextColor());
//			break;
//		case 1:
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				videoButton.setBackgroundResource(R.drawable.study_text_selected);
//				videoButton.setTextColor(Constant.getStudytextColor());
//			} else {
//				voaWordsButton.setBackgroundResource(R.drawable.study_text_selected);
//				voaWordsButton.setTextColor(Constant.getStudytextColor());
//			}
//			break;
//		case 2:
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				readButton.setBackgroundResource(R.drawable.study_text_selected);
//				readButton.setTextColor(Constant.getStudytextColor());
//			} else {
//				videoButton.setBackgroundResource(R.drawable.study_text_selected);
//				videoButton.setTextColor(Constant.getStudytextColor());
//			}
//			break;
//		case 3:
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				commentButton.setBackgroundResource(R.drawable.study_text_selected);
//				commentButton.setTextColor(Constant.getStudytextColor());
//			} else {
//				readButton.setBackgroundResource(R.drawable.study_text_selected);
//				readButton.setTextColor(Constant.getStudytextColor());
//			}
//			break;
//		case 4:
//			testButton.setBackgroundResource(R.drawable.study_text_selected);
//			testButton.setTextColor(Constant.getStudytextColor());
//			break;
//		case 5:
//			commentButton.setBackgroundResource(R.drawable.study_text_selected);
//			commentButton.setTextColor(Constant.getStudytextColor());
//			break;
//		}
//	}
//
//	private void refreshReadCount() {
//		ReadCountUpdateRequest request = new ReadCountUpdateRequest(
//				VoaDataManager.getInstance().voaTemp.voaid + "", 1 + "",
//				new RequestCallBack() {
//					@Override
//					public void requestResult(Request result) {
//						ReadCountUpdateRequest rs = (ReadCountUpdateRequest) result;
//						if (rs.isRequestSuccessful()) {
//							for (Iterator iterator = rs.readCounts.iterator(); iterator
//									.hasNext();) {
//								ReadCount item = (ReadCount) iterator.next();
//								VoaDataManager.getInstance().voaTemp.readcount = item.readCount;
//								vop.updateCountData(VoaDataManager.getInstance().voaTemp.voaid,
//										item.readCount);
//							}
//						} else {
//							Log.e("readcount", "update fail");
//						}
//					}
//				});
//		CrashApplication.getInstance().getQueue().add(request);
//	}
//
//	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			final String action = intent.getAction();
//			if (Intent.ACTION_SCREEN_ON.equals(action)) {
////				if (adView != null && MobclickAgent.getConfigParams(mContext, "ad").equals("1")) {
////					adView.loadAd(new AdRequest());
////				}
//			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
////				if (adView != null) {
////					adView.stopLoading();
////				}
//			}
//		}
//	};
//
//	private void shareMessage(String imagePath, String text, String url) {
//		NewOneKeyShare oks = new NewOneKeyShare(mContext);
//		// 分享时Notification的图标和文字
//		// oks.setNotification(R.drawable.icon,
//		// mContext.getString(R.string.app_name));
//		// address是接收人地址，仅在信息和邮件使用
//		oks.setAddress("");
//		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle(VoaDataManager.getInstance().voaTemp.title_cn);
//		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl(url);
//		// text是分享文本，所有平台都需要这个字段
//		oks.setText(text);
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(imagePath);
//		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
//		// 微信的两个平台、Linked-In支持此字段
//		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl(url);
//		// appPath是待分享应用程序的本地路劲，仅在微信中使用
//		// oks.setAppPath(MainActivity.TEST_IMAGE);
//		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("爱语吧的这款应用" + Constant.getAppname() + "真的很不错啊~推荐！");
//		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(Constant.getAppname());
//		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl(url);
//		// venueName是分享社区名称，仅在Foursquare使用
//		oks.setVenueName("Fun English From Iyuba");
//		// venueDescription是分享社区描述，仅在Foursquare使用
//		oks.setVenueDescription("Nice App!");
//		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
//		if (GetLocationUtil.getInstance(mContext).getLongitude() != 0
//				&& GetLocationUtil.getInstance(mContext).getLatitude() != 0) {
//			oks.setLatitude((float) GetLocationUtil.getInstance(mContext)
//					.getLatitude());
//			// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
//			oks.setLongitude((float) GetLocationUtil.getInstance(mContext)
//					.getLongitude());
//		}
//		// 是否直接分享（true则直接分享）
//		oks.setSilent(false);
//		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
//		// 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
//		// oks.setCallback(new OneKeyShareCallback());
//		// 通过OneKeyShareCallback来修改不同平台分享的内容
//		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
//		oks.setDialogMode();
//		oks.show(mContext);
//	}
//
//
//
//*
//	 * 快捷分享项目现在添加为不同的平台添加不同分享内容的方法。 本类用于演示如何区别Twitter的分享内容和其他平台分享内容。
//
//
//	public static class ShareContentCustomizeDemo implements
//			ShareContentCustomizeCallback {
//		@Override
//		public void onShare(Platform platform,
//				Platform.ShareParams paramsToShare) {
//			String music_url;
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("217")) {
//				music_url = Constant.getAudiourlVip()
//						+ VoaDataManager.getInstance().voaTemp.voaid + Constant.getAppend();
//			} else {
//				music_url = Constant.getAudiourlVip()
//						+ VoaDataManager.getInstance().voaTemp.sound;
//			}
//			if (Wechat.NAME.equals(platform.getName())
//					|| WechatMoments.NAME.equals(platform.getName())) {
//				paramsToShare.setShareType(Wechat.SHARE_MUSIC);
//				paramsToShare.setMusicUrl(music_url);
//			}
//		}
//
//	}
//
//	private void prepareMessage() {
//		String imagePath = "";
//		if (DiskCacheUtils.findInCache(VoaDataManager.getInstance().voaTemp.pic,
//				ImageLoader.getInstance().getDiscCache()) != null) {
//			imagePath = DiskCacheUtils.findInCache(
//					VoaDataManager.getInstance().voaTemp.pic,
//					ImageLoader.getInstance().getDiscCache()).toString();
//		}
//		if (VoaDataManager.getInstance().subtitleSum != null
//				&& VoaDataManager.getInstance().subtitleSum.articleTitle == null) {
//			VoaDataManager.getInstance().subtitleSum.articleTitle = "";
//		}
//		if (VoaDataManager.getInstance().voaTemp == null) {
//			return;
//		}
//		String url;
//		try {
//			String sound;
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("217")) {
//
//				sound = Constant.getAudiourlVip()
//						+ VoaDataManager.getInstance().voaTemp.voaid + Constant.getAppend();
//
//			} else {
//
//				sound = Constant.getAudiourlVip()
//						+ VoaDataManager.getInstance().voaTemp.sound;
//			}
//			url = getString(R.string.sharesdk_url)
//					+ VoaDataManager.getInstance().voaTemp.voaid
//					+ "&title="
//					+ URLEncoder.encode(URLEncoder.encode(
//							VoaDataManager.getInstance().voaTemp.title_cn, "utf-8"), "utf-8")
//					+ "&pic=" + VoaDataManager.getInstance().voaTemp.pic + "&sound="
//					+ VoaDataManager.getInstance().voaTemp.sound;
//
//			String text = getResources().getString(R.string.study_info)
//					+ VoaDataManager.getInstance().subtitleSum.articleTitle.toString()
//					+ "》" + " [ " + url + " ]";
//			String chinese = VoaDataManager.getInstance().voaTemp.desccn;
//			int length = TextLength.getLength(text);
//			if (chinese == null) {
//				chinese = "";
//			}
//			int i = 140 - TextLength.getLength(getResources().getString(
//					R.string.study_info)
//					+ VoaDataManager.getInstance().subtitleSum.articleTitle.toString()
//					+ "》");
//			if (TextLength.getLength(chinese) < i - 20) {
//				text = text + chinese;
//			} else {
//				chinese = chinese.substring(0, i - 20);
//				text = text + chinese;
//			}
//			shareMessage(imagePath, text, url);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void tofinish() {
//		StudyActivity.this.finish();
//		overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		nightModeManager.checkMode();
//		MobclickAgent.onResume(this);
//
//
//		if (adView != null && MobclickAgent.getConfigParams(this, "ad").equals("1")) {
//			adView.loadAd(new AdRequest());
//		}
//
//		Log.e(TAG, "when onResume vdm.detailsize: " +
//				VoaDataManager.getInstance().voaDetailsTemp.size());
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//
//
//		if (adView != null) {
//			adView.stopLoading();
//		}
//
//
//		updateNotification();
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 当前为横屏
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			} else {
//				if (NotificationService.isAppExit) {
//					CustomDialog dialog = new CustomDialog(mContext);
//					dialog.setTitle("请选择退出方式");
//					dialog.setConfirm("完全退出");
//					dialog.setCancel("后台播放");
//					dialog.setContent("请选择完全退出或后台继续播放音频");
//					dialog.setHasIgnoreBox(false);
//					dialog
//							.setIgnoreCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
//								@Override
//								public void onCheckedChanged(CompoundButton buttonView,
//										boolean isChecked) {
//									ConfigManager.Instance(mContext).putBoolean(
//											"ignore_exit_check", isChecked);
//								}
//							});
//					dialog.setClickListener(new ClickListener() {
//						@Override
//						public void onConfirmClick() {
//							sendBroadcast(new Intent("service_stop"));
//							ConfigManager.Instance(mContext).putInt("isdowning", 0);
//							NotificationService.isAppExit = true;
//							sendMsgToService(NotificationService.KEY_COMMAND_REMOVE);
//							tofinish();
//						}
//
//						@Override
//						public void onCancelClick() {
//							sendMsgToService(NotificationService.KEY_COMMAND_APPCLOSE);
//							tofinish();
//						}
//					});
//					dialog.show();
//				} else {
//					tofinish();
//				}
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	public void onClick(View v) {
//		int id = v.getId();
//		if (id == R.id.study_button_info) {
//			viewpager.setCurrentItem(0, true);
//		} else if (id == R.id.study_button_exercise) {
//			viewpager.setCurrentItem(4, true);
//		} else if (id == R.id.study_button_video) {
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				viewpager.setCurrentItem(1, true);
//			} else {
//				viewpager.setCurrentItem(2, true);
//			}
//		} else if (id == R.id.study_button_read) {
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				viewpager.setCurrentItem(2, true);
//			} else {
//				viewpager.setCurrentItem(3, true);
//			}
//		} else if (id == R.id.study_button_voawords) {
//			viewpager.setCurrentItem(1, true);
//		} else if (id == R.id.study_button_comment) {
//			if (Constant.getAppid().equals("213")
//					|| Constant.getAppid().equals("229")) {
//				viewpager.setCurrentItem(3, true);
//			} else {
//				viewpager.setCurrentItem(5, true);
//			}
//		}
//	}
//
//	@Override
//	public void onDataRefresh() {
//		getSupportActionBar().setTitle(VoaDataManager.getInstance().voaTemp.title);
//		refreshReadCount();
//		Log.e(TAG, "info Fragment null? : " + (infoFragment == null));
//		if (infoFragment != null) {
//			infoFragment.refresh();
//		}
//		Log.e(TAG, "test Fragment null? : " + (testFragment == null));
//		if (testFragment != null) {
//			testFragment.refresh();
//		}
//		Log.e(TAG, "video Fragment null? : " + (videoFragment == null));
//		if (videoFragment != null) {
//			videoFragment.refresh();
//		}
//		Log.e(TAG, "read Fragment null? : " + (readFragment == null));
//		if (readFragment != null) {
//			readFragment.refresh();
//		}
//		Log.e(TAG, "word Fragment null? : " + (wordFragment == null));
//		if (wordFragment != null) {
//			wordFragment.refresh();
//		}
//		Log.e(TAG, "comment Fragment null? : " + (commentFragment == null));
//		if (commentFragment != null) {
//			commentFragment.refresh();
//		}
//	}
//
//}
//
