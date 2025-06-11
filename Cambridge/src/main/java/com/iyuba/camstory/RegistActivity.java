package com.iyuba.camstory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.LoginRequest;
import com.iyuba.camstory.listener.RegistRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.ProtocolText;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.voa.activity.setting.SettingConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistActivity extends Activity {
	private static final String TAG = RegistActivity.class.getSimpleName();
	
	private Context mContext;
	
	private Button backBtn;
	private EditText userName, userPwd, reUserPwd, email;
	private Button regBtn;
	private TextView toPhoneButton;
	
	private String userNameString;
	private String userPwdString;
	private String reUserPwdString;
	private String emailString;
	private CheckBox protocol;
	private boolean send = false;
	private CustomDialog wettingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.regist_layout);
		wettingDialog = new WaittingDialog().wettingDialog(mContext);
		backBtn = findViewById(R.id.button_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		userName = findViewById(R.id.editText_userId);
		userPwd = findViewById(R.id.editText_userPwd);
		reUserPwd = findViewById(R.id.editText_reUserPwd);
		email = findViewById(R.id.editText_email);
		regBtn = findViewById(R.id.button_regist);
		protocol = findViewById(R.id.protocol);
		ProtocolText.setText(protocol,mContext);
		regBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				if (verification()) { // 验证通过
					// 开始注册
					if (protocol.isChecked()){
						if (!send) {
							send = true;
							wettingDialog.show();
							regist();
						} else {
							CustomToast.showToast(mContext, R.string.regist_operating, 1000);
						}
					} else{
						CustomToast.showToast(mContext, "请阅读并同意使用条款和隐私政策", 1000);
					}

				}
			}
		});
		toPhoneButton = findViewById(R.id.button_regist_phone);
		toPhoneButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		toPhoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			/*	Intent intent = new Intent();
				intent.setClass(mContext, RegistByPhoneActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);*/
				finish();
			}
		});
	}

	/**
	 * 验证
	 */
	public boolean verification() {
		userNameString = userName.getText().toString();
		userPwdString = userPwd.getText().toString();
		reUserPwdString = reUserPwd.getText().toString();
		emailString = email.getText().toString();
		if (userNameString.length() == 0) {
			userName.setError(getResources().getString(
					R.string.regist_check_username_1));
			return false;
		}
		if (!checkUserId(userNameString)) {
			userName.setError(getResources().getString(
					R.string.regist_check_username_1));
			return false;
		}

		if (userPwdString.length() == 0) {
			userPwd.setError(getResources()
					.getString(R.string.regist_check_userpwd_1));
			return false;
		}
		if (!checkUserPwd(userPwdString)) {
			userPwd.setError(getResources()
					.getString(R.string.regist_check_userpwd_1));
			return false;
		}
		if (!reUserPwdString.equals(userPwdString)) {
			reUserPwd.setError(getResources().getString(
					R.string.regist_check_reuserpwd));
			return false;
		}
		if (emailString.length() == 0) {
			email.setError(getResources().getString(R.string.regist_check_email_1));
			return false;
		}
		if (!emailCheck(emailString)) {
			email.setError(getResources().getString(R.string.regist_check_email_2));
			return false;
		}
		return true;
	}

	/**
	 * 匹配用户名
	 * 
	 * @param userId
	 * @return
	 */
	public boolean checkUserId(String userId) {
        return !(userId.length() < 3 || userId.length() > 15);
    }

	/**
	 * 匹配密码
	 * 
	 * @param userPwd
	 * @return
	 */
	public boolean checkUserPwd(String userPwd) {
        return !(userPwd.length() < 6 || userPwd.length() > 20);
    }

	/**
	 * email格式匹配
	 * 
	 * @param email
	 * @return
	 */
	public boolean emailCheck(String email) {
		String check = "^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				break;
			case 1: // 弹出错误信息
				CustomToast.showToast(mContext, R.string.regist_email_used, 1000);
				break;
			case 2:
				CustomToast.showToast(mContext, R.string.category_check_network, 1000);
				break;
			case 3:
				CustomToast.showToast(mContext, R.string.regist_userid_exist, 1000);
				break;
			}
		}
	};

	private void regist() {
		RegistRequest request = new RegistRequest(userName.getText().toString(),
				userPwd.getText().toString(), "", email.getText().toString(),
				new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RegistRequest rs = (RegistRequest) result;
						send = false;
						wettingDialog.dismiss();
						if (rs.isRegistSuccess()) {
							AccountManager.getInstance().login(mContext,
									userName.getText().toString(), userPwd.getText().toString(),
									new RequestCallBack() {
										@Override
										public void requestResult(Request request) {
											LoginRequest rs = (LoginRequest) request;
											if (rs.isLoginSuccess()) {
												if (SettingConfig.Instance(mContext).isAutoLogin()) {// 保存账户密码
													AccountManager.getInstance().saveUserNameAndPwd(
															userName.getText().toString(),
															userPwd.getText().toString());

													SetUserLibUtils.setUserInfoLib(mContext);
												} else {
													AccountManager.getInstance().saveUserNameAndPwd("", "");
												}
												Intent intent = new Intent(mContext,
														UpLoadImageActivity.class);
												startActivity(intent);
												finish();
											} else {
											}
										}
									});
							finish();
						} else if (rs.isUserIdExist()) {

//							AccountManager.getInstance().login(mContext,
//									userName.getText().toString(), userPwd.getText().toString(),
//									new RequestCallBack() {
//
//										@Override
//										public void requestResult(Request result) {
//											LoginRequest request = (LoginRequest) result;
//											if (request.isLoginSuccess()) {
//												if (SettingConfig.Instance(mContext).isAutoLogin()) {// 保存账户密码
//													AccountManager.getInstance().saveUserNameAndPwd(
//															userName.getText().toString(),
//															userPwd.getText().toString());
//												} else {
//													AccountManager.getInstance().saveUserNameAndPwd("", "");
//												}
//												Intent intent = new Intent(mContext,
//														UpLoadImageActivity.class);
//												startActivity(intent);
//												finish();
//											} else {
//												handler.sendEmptyMessage(3);
//											}
//										}
//									});
//							finish();
							 handler.sendEmptyMessage(3);
						} else {
							// 注册失败
							handler.sendEmptyMessage(1);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

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
