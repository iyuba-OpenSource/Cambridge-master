package com.iyuba.camstory;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.iyuba.camstory.adpater.ChattingAdapter;
import com.iyuba.camstory.adpater.EmotionAdapter;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.listener.RequestAddAttention;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestCancelAttention;
import com.iyuba.camstory.listener.RequestMessageLetterContentList;
import com.iyuba.camstory.listener.RequestSendMessageLetter;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.SocialDataManager;
import com.iyuba.camstory.manager.UserInfoManager;
import com.iyuba.camstory.sqlite.mode.Attention;
import com.iyuba.camstory.sqlite.mode.MessageLetterContent;
import com.iyuba.camstory.sqlite.mode.UserInfo;
import com.iyuba.camstory.utils.Emotion;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.umeng.analytics.MobclickAgent;
public class MessageLetterContentActivity extends AppCompatActivity {

	private Context mContext;

	private ChattingAdapter adapter;
	private ArrayList<MessageLetterContent> list = new ArrayList<MessageLetterContent>();
	private ListView chatHistoryLv;
	private Button sendBtn, back;
	private EditText textEditor;
	private Button showBtn;
	private RelativeLayout rlShow;
	private CustomDialog waitingDialog;
	private String currPages = "1";
	private int curPage = 1;
	private TextView friendName;
	private GridView emotion_GridView;
	private EmotionAdapter emoAdapter;
	private String sendStr;
	private String friendid, search, mutualAttention, currentname = null;
	private Button attention_button;
	private boolean isfriend = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatting);
		mContext = this;
		
		chatHistoryLv = findViewById(R.id.chatting_history_lv);
		Intent intent = getIntent();
		friendid = intent.getStringExtra("friendid");
		Log.d("friendid==null", (friendid) + "");
		search = intent.getStringExtra("search");
		mutualAttention = intent.getStringExtra("mutualAttention");
		currentname = intent.getStringExtra("currentname");
		initWidget();
		waitingDialog = waitingDialog();
		initMessages();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initWidget() {
		attention_button = findViewById(R.id.messagelettercontent_attention);
		Attention at;
		UserInfoManager.getUserInfo(Integer.valueOf(friendid), new CommonCallBack() {
			@Override
			public void onPositive(Object object) {
				if (object != null) {
					UserInfo info = (UserInfo) object;
					if (info.relation / 100 != 0) {
						isfriend = true;
						attention_button.post(new Runnable() {
							public void run() {
								attention_button.setText(R.string.social_cancle_attention);
							}
						});
					}
				}
			}

			@Override
			public void onNegative(Object object) {
				isfriend = false;
			}
		});
		attention_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isfriend) {
					RequestCancelAttention request1 = new RequestCancelAttention(
							AccountManager.getInstance().userId, friendid, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestCancelAttention rs = (RequestCancelAttention) result;
									if (rs.isRequestSuccessful()) {
										isfriend = !isfriend;
										CustomToast.showToast(mContext, R.string.social_success_cancle_attention, 1000);
										Attention attention;
										handler.removeMessages(0);
										handler.sendEmptyMessage(0);
									} else {
										CustomToast.showToast(mContext, R.string.social_failed_cancle_attention, 1000);
									}
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request1);
					attention_button.setText(R.string.social_attention);
				} else {
					RequestAddAttention request = new RequestAddAttention(
							AccountManager.getInstance().userId, friendid, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestAddAttention rs = (RequestAddAttention) result;
									if (rs.isRequestSuccessful()) {
										isfriend = !isfriend;
										CustomToast.showToast(mContext, R.string.social_success_attention, 1000);
										handler.removeMessages(0);
										handler.sendEmptyMessage(0);
									} else {
										CustomToast.showToast(mContext, R.string.social_failed_attention, 1000);
									}
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
					attention_button.setText(R.string.social_cancle_attention);

				}
			}
		});
		sendBtn = findViewById(R.id.send_button);
		showBtn = findViewById(R.id.show);
		back = findViewById(R.id.messageletterContent_back_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		rlShow = findViewById(R.id.rl_show);
		textEditor = findViewById(R.id.text_editor);
		emotion_GridView = rlShow.findViewById(R.id.grid_emotion);
		// emotion_GridView=createGridView();
		emoAdapter = new EmotionAdapter(mContext);
		friendName = findViewById(R.id.messagelettercontent_friendname);
		if (friendid != null) {
			if (search != null && search.equals("search")) {
				Log.e("search", "searchsearch");
				friendName.setText(SocialDataManager.getInstance().searchItem.username);
			} else if (mutualAttention != null && mutualAttention.equals("mutualAttention")) {
				friendName.setText(currentname);
				Log.e("mutualAttention", "mutualAttention");
			} else {
				friendName.setText(currentname);
				Log.e("currentname", "currentname");
			}
		} else {
			friendName.setText(SocialDataManager.getInstance().letter.name);
			Log.e("letter", "letter");
		}
		sendBtn.setOnClickListener(l);
		showBtn.setOnClickListener(l);
		textEditor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rlShow.getVisibility() == View.VISIBLE) {
					rlShow.setVisibility(View.GONE);
				}
			}
		});
	}

	// 设置adapter
	private void setAdapterForThis() {
		adapter = new ChattingAdapter(this, AccountManager.getInstance().userId);
		chatHistoryLv.setAdapter(adapter);
	}

	// 为listView添加数据
	private void initMessages() {
		setAdapterForThis();
		handler.sendEmptyMessage(0);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage = 1;
				currPages = String.valueOf(curPage);
				adapter.clearList();
				list.clear();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				// 联网获取日志列表，滑到底部点击更多进行加载
				if (friendid != null) {
					RequestMessageLetterContentList request = new RequestMessageLetterContentList(
							AccountManager.getInstance().userId, friendid, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestMessageLetterContentList res = (RequestMessageLetterContentList) result;
									if (res.isRequestSuccessful()) {
										list.addAll(res.list);
										adapter.addList(res.list);
										Log.d("message_list", res.list.size() + ";" + res.list.get(0).message);
									} else {
										Log.d("message_list", "wrong");
									}
									Log.d("friendid!=null", "friendid!=null");
									curPage += 1;
									currPages = String.valueOf(curPage);
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				} else {
					RequestMessageLetterContentList request = new RequestMessageLetterContentList(
							AccountManager.getInstance().userId, SocialDataManager.getInstance().letter.friendid,
							new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestMessageLetterContentList res = (RequestMessageLetterContentList) result;
									if (res.isRequestSuccessful()) {
										list.addAll(res.list);
										adapter.addList(res.list);
									} else {
										Log.d("message_list", "wrong");
									}
									Log.d("friendid==null", "friendid==null");
									curPage += 1;
									currPages = String.valueOf(curPage);
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				}
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler.sendEmptyMessage(3);
				adapter.notifyDataSetChanged();
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.setClass(mContext, MessageLetterContentActivity.class);
				startActivity(intent);
				break;
			case 10:
				// 发送私信
				if (search != null && search.equals("search")) {
					RequestSendMessageLetter request = new RequestSendMessageLetter(
							AccountManager.getInstance().userId,
							SocialDataManager.getInstance().searchItem.username, sendStr, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestSendMessageLetter rs = (RequestSendMessageLetter) result;
									if (rs.isRequestSuccessful()) {
									} else {
									}
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				} else if (mutualAttention != null && mutualAttention.equals("mutualAttention")) {
					RequestSendMessageLetter request = new RequestSendMessageLetter(
							AccountManager.getInstance().userId,
							SocialDataManager.getInstance().mutualAttention.fusername, sendStr,
							new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestSendMessageLetter rs = (RequestSendMessageLetter) result;
									if (rs.isRequestSuccessful()) {
									} else {
									}
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				} else if (currentname != null) {
					RequestSendMessageLetter request = new RequestSendMessageLetter(
							AccountManager.getInstance().userId, currentname, sendStr, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestSendMessageLetter rs = (RequestSendMessageLetter) result;
									if (rs.isRequestSuccessful()) {
									} else {
									}
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				} else {
					RequestSendMessageLetter request = new RequestSendMessageLetter(
							AccountManager.getInstance().userId, SocialDataManager.getInstance().letter.name,
							sendStr, new RequestCallBack() {
								@Override
								public void requestResult(Request result) {
									RequestSendMessageLetter rs = (RequestSendMessageLetter) result;
									if (rs.isRequestSuccessful()) {
									} else {
									}
									handler.sendEmptyMessage(4);
								}
							});
					CrashApplication.getInstance().getQueue().add(request);
				}

				break;
			default:
				break;
			}
		}

	};

	/**
	 * 按键时监听
	 */
	private int[] imageIds = new int[30];
	private OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == sendBtn.getId()) {
				String str = textEditor.getText().toString();
				// String sendStr;
				if (str != null
						&& (sendStr = str.trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")
								.replaceAll("\f", "")) != "") {
					sendMessage(sendStr);
				}
				textEditor.setText("");
			}
			if (v.getId() == showBtn.getId()) {
				if (rlShow.getVisibility() == View.GONE) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							MessageLetterContentActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					rlShow.setVisibility(View.VISIBLE);
					initEmotion();
					emotion_GridView.setVisibility(View.VISIBLE);
					emotion_GridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeResource(getResources(),
									imageIds[arg2 % imageIds.length]);
							ImageSpan imageSpan = new ImageSpan(MessageLetterContentActivity.this, bitmap);
							String str = null;
							str = "image" + arg2;
							SpannableString spannableString = new SpannableString(str);
							String str1 = null;
							str1 = Emotion.express[arg2];
							System.out.println("spannableString====" + spannableString);
							SpannableString spannableString1 = new SpannableString(str1);
							if (str.length() == 6) {
								spannableString.setSpan(imageSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							} else if (str.length() == 7) {
								spannableString.setSpan(imageSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							} else {
								spannableString.setSpan(imageSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
							System.out.println("1111   spannableString====" + spannableString);

							textEditor.append(spannableString1);
							String string = textEditor.getText().toString();
							System.out.println("1111   ====" + string);
						}
					});
				} else {
					rlShow.setVisibility(View.GONE);
				}
			}
		}

		// 模拟发送消息
		private void sendMessage(String sendStr) {
			handler.sendEmptyMessage(10);
			MessageLetterContent letterContent = new MessageLetterContent();
			letterContent.setMessage(sendStr);
			letterContent.setDirection(1);
			letterContent.setAuthorid(AccountManager.getInstance().userId);
			letterContent.setDateline(String.valueOf(System.currentTimeMillis() / 1000));
			list.add(letterContent);
			System.out.println("listdaxiao  " + list.size());
			System.out.println("listdaxiao  " + list.get(list.size() - 1).message);
			adapter.clearList();
			adapter.addList(list);
		}

	};

	private void initEmotion() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
		for (int i = 0; i < 30; i++) {
			try {
				Field field = R.drawable.class.getDeclaredField("image" + (i + 1));
				int resourceId = Integer.parseInt(field.get(null).toString());
				System.out.println("resourceId==" + resourceId);
				System.out.println("field==" + field);
				imageIds[i] = resourceId;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.team_layout_single_expression_cell, new String[] { "image" },
				new int[] { R.id.image });
		emotion_GridView.setAdapter(simpleAdapter);
		emotion_GridView.setNumColumns(7);
	}

	/**
	 * 生成一个表情对话框中的gridview
	 * 
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(this);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
		for (int i = 0; i < 30; i++) {
			try {
				Field field = R.drawable.class.getDeclaredField("image" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				System.out.println("resourceId==" + resourceId);
				System.out.println("field==" + field);
				imageIds[i] = resourceId;
				/*
				 * if(i<10){ Field field = R.drawable.class.getDeclaredField("image" +
				 * i); int resourceId = Integer.parseInt(field.get(null).toString());
				 * System.out.println("resourceId=="+resourceId); imageIds[i] =
				 * resourceId; }else{ Field field =
				 * R.drawable.class.getDeclaredField("image" + i); int resourceId =
				 * Integer.parseInt(field.get(null).toString()); imageIds[i] =
				 * resourceId; }
				 */
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.team_layout_single_expression_cell, new String[] { "image" },
				new int[] { R.id.image });
		view.setAdapter(simpleAdapter);
		view.setNumColumns(5);
		// view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		System.out.println("listItem===" + listItems.get(5));
		System.out.println("listItem===" + listItems.size());
		System.out.println("view===" + view);
		return view;
	}

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}
}
