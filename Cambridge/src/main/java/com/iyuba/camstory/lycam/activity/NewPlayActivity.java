package com.iyuba.camstory.lycam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.IyuSendMsgRspBean;
import com.iyuba.camstory.bean.living.IyuStreamInfo;
import com.iyuba.camstory.lil.GlideUtil;
import com.iyuba.camstory.lycam.fragment.MessageListFragment;
import com.iyuba.camstory.lycam.fragment.SubscribeFragment;
import com.iyuba.camstory.lycam.network.IyuLiveRequestFactory;
import com.iyuba.camstory.lycam.util.LycamUtils;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.camstory.lycam.util.ScreenUtils;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.configation.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import master.flame.danmaku.ui.widget.DanmakuView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.lycam.gift.consts.GEventCode;
import tv.lycam.gift.core.AndroidEventManager;
import tv.lycam.gift.core.EventManager;
import tv.lycam.gift.widget.danmaku.Danmu;
import tv.lycam.gift.widget.danmaku.DanmuControl;
import tv.lycam.ijkplayer.bean.VideoijkBean;
import tv.lycam.ijkplayer.listener.OnShowThumbnailListener;
import tv.lycam.ijkplayer.widget.PlayStateParams;
import tv.lycam.ijkplayer.widget.PlayerView;
import tv.lycam.mqtt.callback.MqttListener;
import tv.lycam.mqtt.constants.Action;
import tv.lycam.mqtt.constants.MqttConstants;
import tv.lycam.mqtt.util.MqttBiz;
import tv.lycam.mqtt.util.MqttConfig;

//import butterknife.BindView;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewPlayActivity extends PlayBaseActivity {

	public static final String BEAN = "bean";
	private static final int SET_VIEW_HIDE = 1;
	private static final int TIME_OUT = 5000;
	private static final int MESSAGE_SHOW_PROGRESS = 2;
	private List<VideoijkBean> list;
	View main;
	RelativeLayout layoutPlay;
	LinearLayout layoutChat;
	EditText etMessage;
	ImageView ivExitFull;
	//	@BindView(R.id.view_play)
//	LycamPlayView playView;
	ImageView ivSend;
	DanmakuView danmakuView;
	FrameLayout toolbar;
	TextView tvTitle;
	LinearLayout layoutInfo;
	ImageView ivAvatar;
	TextView tvName;
	TextView tvFollow;
/*	@BindView(R.id.layout_play)
	RelativeLayout layoutPlay;
	@BindView(R.id.layout_chat)
	LinearLayout layoutChat;
	@BindView(R.id.et_message)
	EditText etMessage;
	@BindView(R.id.iv_exitfull)
	ImageView ivExitFull;
//	@BindView(R.id.view_play)
//	LycamPlayView playView;
	@BindView(R.id.iv_send)
	ImageView ivSend;
	@BindView(R.id.danmakuView)
	DanmakuView danmakuView;
	@BindView(R.id.toolbar)
	FrameLayout toolbar;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.layout_info)
	LinearLayout layoutInfo;
	@BindView(R.id.iv_avatar)
	ImageView ivAvatar;
	@BindView(R.id.tv_name)
	TextView tvName;
	@BindView(R.id.tv_follow)
	TextView tvFollow;*/

	protected static MqttBiz MqttBIZ;

	private IyuStreamInfo mBean;
	private int titleId = 0;

	public DanmuControl mDanmuControl;
	//是否竖屏
	private boolean portrait = true;
	//消息列表是否显示
	private boolean isMessageListFragmentShow = false;
	//消息列表fragment
	private MessageListFragment messageListFragment;

	private boolean isFirstConnect = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		main = getLayoutInflater().from(this).inflate(R.layout.activity_newplay,null);
		setContentView(main);
		//setContentView(R.layout.activity_newplay);
		findView();
		//ButterKnife.bind(this);

		/**虚拟按键的隐藏方法*/
		main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				//比较Activity根布局与当前布局的大小
				int heightDiff = main.getRootView().getHeight() - main.getHeight();
				if (heightDiff > 100) {
					//大小超过100时，一般为显示虚拟键盘事件
					main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
				} else {
					//大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
					main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

				}
			}
		});

		list = new ArrayList<VideoijkBean>();

		mBean = getIntent().getParcelableExtra(BEAN);
		titleId = getIntent().getIntExtra("titleId",0);
		addSoftKeyboardListener();

		initView();
//		initChatInfo();
		initListener();
		connectMqtt();
	}

	private void findView() {
		layoutPlay = findViewById(R.id.layout_play);
		layoutChat = findViewById(R.id.layout_chat);
		etMessage = findViewById(R.id.et_message);
		ivExitFull = findViewById(R.id.iv_exitfull);
		ivSend = findViewById(R.id.iv_send);
		danmakuView = findViewById(R.id.danmakuView);
		toolbar = findViewById(R.id.toolbar);
		tvTitle = findViewById(R.id.tv_title);
		layoutInfo = findViewById(R.id.layout_info);
		ivAvatar = findViewById(R.id.iv_avatar);
		tvName = findViewById(R.id.tv_name);
		tvFollow = findViewById(R.id.tv_follow);
	}

	@Override
	protected void onStart() {
		super.onStart();
		handler.sendEmptyMessageDelayed(SET_VIEW_HIDE, TIME_OUT);

//		initListener();
//		connectMqtt();

		MqttBiz.registerReceiver(context, mqttListener);

//		if (MqttBIZ != null && MqttBIZ.isConnected()) {
//			Bundle data = new Bundle();
//			data.putString(MqttConstants.topic, mBean.getTopic());
//			MqttBIZ.subscribe(data);
//		}
	}

	@Override
	public void onStop() {
		super.onStop();
		MqttBiz.unregisterReceiver();
		if (MqttBIZ != null && MqttBIZ.isConnected()) {
			Bundle data = new Bundle();
			data.putString(MqttConstants.topic, mBean.getTopic());
			MqttBIZ.unsubscribe(data);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mDanmuControl.resume();
//		playView.pause();
	}

	@Override
	public void onPause() {
		super.onPause();
		mDanmuControl.pause();
//		playView.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDanmuControl.destroy();
		if (MqttBIZ != null) {
			MqttBIZ.disconnect();
			MqttBIZ = null;
		}
		AndroidEventManager.getInstance()
				.removeEventListener(GEventCode.Local_Barrage, mqttEventListener);
	}
	private void initView() {
		//计算普通情况下视频的宽和高
		int width = ScreenUtils.getInstance().getWidth();
		int height = width * width / ScreenUtils.getInstance().getHeight();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);

//		//设置playView属性
//		playView.setLayoutParams(lp, width, height);
//		playView.start(mBean.getStreamUrl());
//		playView.setPortrait(true);
		//layoutPlay.setPadding(0,ScreenUtils.getInstance().getStatusBarHeight(),0,0);
		if (TextUtils.isEmpty(mBean.getTitle())) {
			tvTitle.setText(mBean.getUser().getDisplayName());
		} else {
			tvTitle.setText(mBean.getTitle());
		}
		GlideUtil.loadImg(this,mBean.getUser().getAvatarUrl(),0,ivAvatar);
//		GlideUtil.loadNormalAvatar(ivAvatar, mBean.getUser().getAvatarUrl());
		tvName.setText(mBean.getUser().getDisplayName());

//		setHeartState(mBean.isFollow());

		mDanmuControl = new DanmuControl(context);
//		设置弹幕视图
		mDanmuControl.setDanmakuView(danmakuView);

		if (LycamUtils.isReady(mBean.getStatus())) {
//			playView.stop();
//			playView.showReadyView(mBean.getThumbnailUrl());
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.layout_content, SubscribeFragment.newInstance(mBean))
					.commit();
			pagerChanged(0);
		} else {
			VideoijkBean videoijkBean = new VideoijkBean();
			videoijkBean.setStream(mBean.getTitle());
			videoijkBean.setUrl(mBean.getStreamUrl());
			list.add(videoijkBean);
			player = new PlayerView(this) {
				@Override
				public PlayerView toggleProcessDurationOrientation() {
					hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
				}
				@Override
				public PlayerView setPlaySource(List<VideoijkBean> list) {
					return super.setPlaySource(list);
				}
			}
					.setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
					.setScaleType(PlayStateParams.fillparent)
					.forbidTouch(false)
					.hideSteam(true)
					.hideBack(true)
					.hideCenterPlayer(true)
					.showThumbnail(new OnShowThumbnailListener() {
						@Override
						public void onShowThumbnail(ImageView ivThumbnail) {
							Glide.with(mContext)
									.load(mBean.getThumbnailUrl())
									.dontTransform()
									.placeholder(R.color.colorPrimary)
									.error(R.color.colorAccent)
									.into(ivThumbnail);
						}
					})
					.setPlaySource(list)
					.startPlay();

			messageListFragment = MessageListFragment.newInstance();
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.layout_content, messageListFragment)
					.commit();
			pagerChanged(1);
		}
	}

//	private void initChatInfo() {
//		Info.init(null, ConfigManager.Instance().loadString("userid"),
//				ConfigManager.Instance().loadString("username"),
//				"Bearer " + mBean.getChatToken(),
//				"Bearer "+ ConfigManager.Instance().loadString("apptoken"));
//	}

	private void initListener() {
		AndroidEventManager.getInstance()
				.addEventListener(GEventCode.Local_Barrage, mqttEventListener);
//		playView.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//				if (isControllerShow()) {
//					hideController();
//				} else {
//					showController();
//					hideController(TIME_OUT);
//				}
//				return false;
//			}
//		});

		ivExitFull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NewPlayActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
		ivSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSend();
			}
		});

		MqttBIZ = MqttBiz.getInstance(context, mqttListener);
	}

	private void pagerChanged(int position) {
		if (position == 0) {
			layoutChat.setVisibility(View.GONE);
			hideKeyBoard();
			isMessageListFragmentShow = false;
		} else {
			layoutChat.setVisibility(View.VISIBLE);
			isMessageListFragmentShow = true;
		}
	}

	/**
	 * 尝试连接Mqtt服务器
	 */
	protected void connectMqtt() {
		// 连接聊天室
		if (mBean == null) {
			return;
		}
		Bundle data = new Bundle();
		data.putString(MqttConstants.username, AccountManager.getInstance().userName);
		data.putString(MqttConstants.token,  mBean.getChatToken());
		data.putString(MqttConstants.server, mBean.getChatUrl());
		Bundle build = new MqttConfig.Option(data).withKeepAlive(600).build();
		// 连接消息服务器
		if (MqttBIZ != null)
			MqttBIZ.connect(build);
		//mMsgAdapter.addString("消息服务连接中...");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
//		if (playView != null) {
//			playView.onChanged(newConfig);
//		}
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			portrait = true;
			ivExitFull.setVisibility(View.GONE);
			danmakuView.setVisibility(View.GONE);
			toolbar.setVisibility(View.VISIBLE);
			etMessage.setVisibility(View.VISIBLE);
			ivSend.setVisibility(View.VISIBLE);
			if (isMessageListFragmentShow) {
				layoutChat.setVisibility(View.VISIBLE);
			} else {
				hideKeyBoard();
				layoutChat.setVisibility(View.GONE);
			}
		} else {
			hideKeyBoard();
			portrait = false;
			ivExitFull.setVisibility(View.VISIBLE);
			danmakuView.setVisibility(View.GONE);
			toolbar.setVisibility(View.GONE);
			etMessage.setVisibility(View.INVISIBLE);
			ivSend.setVisibility(View.INVISIBLE);
		}
	}

	public static Intent newIntent(Context context, IyuStreamInfo bean) {
		Intent intent = new Intent(context, NewPlayActivity.class);
		intent.putExtra(BEAN, bean);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	public static void intentTo(Context context, IyuStreamInfo bean) {
		context.startActivity(newIntent(context, bean));
	}

	@Override
	public void onBackPressed() {
		if (ScreenUtils.getInstance().getScreenOrientation(this) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//			playView.release();
			hideKeyBoard();
			super.onBackPressed();
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	/*@Override
	protected boolean isKeyBoardAutoHidden() {
		return false;
	}

	@OnClick(R.id.btn_back)
	public void back() {
		onBackPressed();
	}

	@OnClick(R.id.btn_share)
	public void share() {
		showShare();
	}

	@OnClick(R.id.iv_avatar)
	public void launchUserIndex() {
	}

	@OnClick(R.id.tv_follow)
	public void follow() {
		boolean isFollow = tvFollow.getText().toString().trim().equals("关注");
		showLoadingDialog();
	}*/

	/*private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(mBean.getTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://class."+Constant.IYBHttpHead+"/v.jsp?tid="+titleId+"&sign="+MD5.getMD5ofStr(titleId+"0passager"));
		// text是分享文本，所有平台都需要这个字段
		oks.setText(mBean.getDescription());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://class."+Constant.IYBHttpHead+"/v.jsp?tid="+titleId+"&sign="+MD5.getMD5ofStr(titleId+"0passager"));
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(ConstantManager.getInstance().getAppName());
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(ConstantManager.getInstance().getAppName());
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://class."+Constant.IYBHttpHead+"/v.jsp?tid="+titleId+"&sign="+MD5.getMD5ofStr(titleId+"0passager"));
		//图片的网络路径，新浪微博、人人、QQ空间和Linked-in
//		oks.setImageUrl("http://static3."+Constant.IYBHttpHead+"/resource/categoryIcon/129.png");
		oks.setImageUrl(mBean.getThumbnailUrl());
		// 启动分享GUI
		oks.show(this);
	}*/

	private MqttListener mqttListener = new MqttListener() {
		@Override
		public void onMessageArrived(String topic, String message) {
			if (messageListFragment != null) {
				messageListFragment.addData(message);
				Log.w("onMessageArrived:",message);
			}
		}

		@Override
		public void onConnectLost(Throwable cause) {
			if (MqttBIZ != null) {
				MqttBIZ.reconnect();
			}
		}

		@Override
		public void onActionSuccess(Action action) {
			if (action.equals(Action.CONNECT)) {
				Bundle data = new Bundle();
				data.putString(MqttConstants.topic, mBean.getTopic());
				if (MqttBIZ != null)
					MqttBIZ.subscribe(data);
			} else if (action.equals(Action.SUBSCRIBE)) {
				if (isFirstConnect) {
					if (messageListFragment != null) {
						messageListFragment.addString("消息服务连接成功");
					}
					isFirstConnect = false;

				}
			}
		}

		@Override
		public void onActionFailed(Action action, Throwable exception) {
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SET_VIEW_HIDE:
					if (isSoftKeyBoardShow() && !portrait) {
						hideController(TIME_OUT);
					} else {
						hideController();
					}
					break;
				case MESSAGE_SHOW_PROGRESS:
					break;
			}
		}
	};

	/**
	 * 几秒后隐藏控制器
	 *
	 * @param timeout
	 */
	public void hideController(int timeout) {
		handler.removeMessages(SET_VIEW_HIDE);
		handler.sendEmptyMessageDelayed(SET_VIEW_HIDE, timeout);
	}

	//隐藏控制器
	public void hideController() {
//		playView.hideController();
		if (portrait) {

		} else {
			hideKeyBoard();
			layoutChat.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyBoard() {
		/*if(isSoftKeyBoardShow()){
			toggleKeyBoard();
        }*/
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
	}

	//显示控制器
	public void showController() {
//		playView.showController();
		if (portrait) {

		} else {
			layoutChat.setVisibility(View.VISIBLE);
		}
	}

	//控制器是否显示
//	public boolean isControllerShow() {
//		if (portrait) {
//			return playView.isControllerShow();
//		} else {
//			return layoutChat.isShown();
//		}
//	}

	/**
	 * 判断软键盘是否显示
	 *
	 * @return
	 */
	public boolean isSoftKeyBoardShow() {
		final int screenHeight = getResources().getDisplayMetrics().heightPixels;
		Rect r = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		int heightDifference = screenHeight - r.bottom;
		boolean isKeyboardShowing = heightDifference > screenHeight / 3;
		return isKeyboardShowing;
	}

	/**
	 * 切换软键盘状态
	 */
	public void toggleKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
	}

	private AndroidEventManager.OnEventListener
			mqttEventListener = new EventManager.OnEventListener() {
		@Override
		public void onEventEnd(tv.lycam.gift.core.Event event) {
			if (event.getEventCode() == GEventCode.Local_Barrage) {
//				if ("live".equals(mBean == null ? "" : mBean.getStatus())) {
				String avatarUrl = event.getParamAtIndex(0);
				String username = event.getParamAtIndex(1);
				String content = event.getParamAtIndex(2);
				Danmu danmu = new Danmu(0, new Random().nextInt(), "CommentList", avatarUrl, username + ":" + content);
				// 添加弹幕
				mDanmuControl.addDanmu(danmu);
//				}
			}
		}
	};

	/**
	 * 发消息
	 */
	public void onSend() {
		String msg = etMessage.getText().toString().trim();
		if (mBean == null || TextUtils.isEmpty(msg)) {
			return;
		}
//		tv.lycam.gift.core.AndroidEventManager.getInstance()
//				.runEvent(GEventCode.Http_SendBarrage, mBean.getChatChannel(), msg);
		Call<IyuSendMsgRspBean> call = IyuLiveRequestFactory.getLiveSendMsgApi().sendChatMessage(
				etMessage.getText().toString(),
				mBean.getTid(),
				String.valueOf(AccountManager.getInstance().userId),
				"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid="+String.valueOf(AccountManager.getInstance().userId),
				AccountManager.getInstance().userName,
				MD5.getMD5ofStr("avatarUrl=http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid="+AccountManager.getInstance().userId
						+ "&content="+etMessage.getText().toString()
						+ "&nickname="+AccountManager.getInstance().userName
						+ "&tid="+mBean.getTid()
						+ "&uid="+AccountManager.getInstance().userId
						+ "&secretkey=OCX3LBO15L").toUpperCase()
		);
		call.enqueue(new Callback<IyuSendMsgRspBean>() {
			@Override
			public void onResponse(Call<IyuSendMsgRspBean> call, Response<IyuSendMsgRspBean> response) {
				if(response != null && response.body() != null){
//					T.s(getApplicationContext(),"Success too!");
				}
			}

			@Override
			public void onFailure(Call<IyuSendMsgRspBean> call, Throwable t) {

			}
		});
		etMessage.setText(null);
	}

	@Override
	public void OnKeyboardChanged(boolean isKeyBoardShow, int keyboardHeight) {
		if (portrait) {
			if (isKeyBoardShow) {
				layoutInfo.setVisibility(View.GONE);
			} else {
				if (isMessageListFragmentShow) {
					layoutInfo.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}

