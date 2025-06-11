package com.iyuba.camstory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.LoginRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestPhoneNumRegister;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.voa.activity.setting.SettingConfig;
import com.umeng.analytics.MobclickAgent;

public class RegistSubmitActivity extends Activity {

	private View backView;
	private Context mContext;
	private EditText userNameEditText, passWordEditText;
	private Button submitButton, backButton;
	private String phonenumb, userName, passWord;
	private CustomDialog wettingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mContext = this;
		setContentView(R.layout.regist_layout_phone_regist);
		backView = findViewById(R.id.backlayout);
		backView.setBackgroundColor(0xfff2f2f2);
		backButton = findViewById(R.id.button_back);
		userNameEditText = findViewById(R.id.regist_phone_username);
		passWordEditText = findViewById(R.id.regist_phone_paswd);
		submitButton = findViewById(R.id.regist_phone_submit);
		phonenumb = getIntent().getExtras().getString("phoneNumb");
		Log.e("电话号码", phonenumb);
		wettingDialog = new WaittingDialog().wettingDialog(mContext);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (verification()) {// 验证通过
					// 开始注册

					wettingDialog.show();
					handler.sendEmptyMessage(0);// 在handler中注册
					CustomToast.showToast(mContext, R.string.regist_operating, 1000);
				} else {

				}
			}

		});
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}

	/**
	 * 验证
	 */
	public boolean verification() {
		userName = userNameEditText.getText().toString();
		passWord = passWordEditText.getText().toString();

		if (userName.length() == 0) {
			CustomToast.showToast(mContext, R.string.regist_check_username_1, 1000);
			return false;
		}
		if (!checkUsernameLength(userName)) {
			CustomToast.showToast(mContext, R.string.regist_check_username_1, 1000);
			return false;
		}

		if (passWord.length() == 0) {
			CustomToast.showToast(mContext, R.string.regist_check_userpwd_1, 1000);
			return false;
		}
		if (!checkPasswordLength(passWord)) {
			CustomToast.showToast(mContext, R.string.regist_check_userpwd_1, 1000);
			return false;
		}
		return true;
	}

	/**
	 * 匹配用户名
	 * 
	 * @param username
	 * @return
	 */
	public boolean checkUsernameLength(String username) {
		return !(username.length() < 3 || username.length() > 15);
	}

	/**
	 * 匹配密码
	 * 
	 * @param userPwd
	 * @return
	 */
	public boolean checkPasswordLength(String userPwd) {
		return !(userPwd.length() < 6 || userPwd.length() > 20);
	}

	private void regist() {
		RequestPhoneNumRegister request = new RequestPhoneNumRegister(userName,
				passWord, phonenumb, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestPhoneNumRegister rr = (RequestPhoneNumRegister) result;
						if (rr.isRequestSuccessful()) {
							// 成功
							AccountManager.getInstance().login(mContext, userName, passWord,
									new RequestCallBack() {
										@Override
										public void requestResult(Request request) {
											LoginRequest loginRequest = (LoginRequest) request;
											if (loginRequest.isLoginSuccess()) {
												if (SettingConfig.Instance(mContext).isAutoLogin()) {// 保存账户密码
													AccountManager.getInstance().saveUserNameAndPwd(
															userName, passWord);
													SetUserLibUtils.setUserInfoLib(mContext);//
												} else {
													AccountManager.getInstance().saveUserNameAndPwd("",
															"");
												}
												Intent intent = new Intent(mContext,
														UpLoadImageActivity.class);
												startActivity(intent);
												finish();
											} else {

											}
										}
									});
							handler.sendEmptyMessage(4);
							finish();
						} else if (rr.resultCode.equals("112")) {
							// 提示用户已存在
							// 成功
//							AccountManager.getInstance().login(mContext, userName, passWord,
//									new RequestCallBack() {
//										@Override
//										public void requestResult(Request request) {
//											// TODO Auto-generated method stub
//											LoginRequest loginRequest = (LoginRequest) request;
//											if (loginRequest.isLoginSuccess()) {
//												if (SettingConfig.Instance(mContext).isAutoLogin()) {// 保存账户密码
//													AccountManager.getInstance().saveUserNameAndPwd(
//															userName, passWord);
//												} else {
//													AccountManager.getInstance().saveUserNameAndPwd("",
//															"");
//												}
//												Intent intent = new Intent(mContext,
//														UpLoadImageActivity.class);
//												startActivity(intent);
//												finish();
//											} else {
//												handler.sendEmptyMessage(3);
//												handler.sendEmptyMessage(4);
//											}
//										}
//									});
//							finish();
							handler.sendEmptyMessage(3);
							handler.sendEmptyMessage(4);
						} else if (rr.resultCode.equals("115")) {
							// 提示手机号已存在
							CustomToast.showToast(mContext, "手机号已注册，请使用手机号登录", 1000);
							handler.sendEmptyMessage(4);
						} else {
							// 注册失败
							handler.sendEmptyMessage(1);// 弹出错误提示
							handler.sendEmptyMessage(4);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				regist();
				break;
			case 1:
				CustomToast.showToast(mContext, R.string.regist_fail, 1000);
				break;
			case 2:
				CustomToast.showToast(mContext, R.string.regist_success, 1000);
				break;
			case 3:
				CustomToast.showToast(mContext, R.string.regist_userid_exist, 1000);
				break;
			case 4:
				wettingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

}
