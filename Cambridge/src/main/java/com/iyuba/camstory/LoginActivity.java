package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.iyuba.camstory.bean.living.LoginSuccessEvent;
import com.iyuba.camstory.listener.LoginRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.setting.SettingConfig;
import com.iyuba.camstory.utils.SetUserLibUtils;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.common.WebConstant;
import com.iyuba.configation.Constant;
import com.iyuba.module.user.IyuUserManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import personal.iyuba.personalhomelibrary.PersonalType;
import personal.iyuba.personalhomelibrary.event.UserNameChangeEvent;


/**
 * 登录界面
 * 
 * @author chentong
 * 
 */
@RuntimePermissions
public class LoginActivity extends Activity {
	private static final String TAG = LoginActivity.class.getSimpleName();
	private Context mContext;
	
	private Button backBtn;
	private Button registBtn, loginBtn;
	private final int request_Code = 100;
	private String userName, userPwd;
	private EditText userNameET, userPwdET;
	private CheckBox autoLogin;
	private CustomDialog cd;
	private TextView findPassword;
	private TextView privacyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mContext = this;
		cd = new WaittingDialog().wettingDialog(mContext);
		userNameET = findViewById(R.id.editText_userId);
		userPwdET = findViewById(R.id.editText_userPwd);
		if (SettingConfig.Instance().isAutoLogin()) {// 保存账户密码
			String[] nameAndPwd = AccountManager.getInstance().getUserNameAndPwd(mContext);
			userName = nameAndPwd[0];
			userPwd = nameAndPwd[1];
			handler.sendEmptyMessage(0);
		}

		autoLogin = findViewById(R.id.checkBox_autoLogin);

		//关闭自动登陆操作
//		SettingConfig.Instance().setAutoLogin(true);
//		autoLogin.setChecked(true);
//		autoLogin.setOnCheckedChangeListener((buttonView, isChecked) -> SettingConfig.Instance().setAutoLogin(isChecked));

		backBtn = findViewById(R.id.button_back);
		backBtn.setOnClickListener(v -> {
			finish();
		});

		loginBtn = findViewById(R.id.button_login);
		loginBtn.setOnClickListener(v -> {
			if (!autoLogin.isChecked()){
				ToastUtil.showToast(this,"请先阅读并同意隐私政策和用户协议");
				return;
			}

			openGps();
		});
		registBtn = findViewById(R.id.button_regist);
		registBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, RegistByPhoneActivity.class);
            startActivity(intent);
            finish();
        });

		findPassword = findViewById(R.id.find_password);
		findPassword.setText(Html
						.fromHtml("<a href=\"http://m."+Constant.IYBHttpHead+"/m_login/inputPhonefp.jsp\">"
								+ getResources().getString(
										R.string.login_find_password) + "</a>"));
		findPassword.setMovementMethod(LinkMovementMethod.getInstance());

		//设置隐私政策和用户协议显示
		privacyView = findViewById(R.id.textView_privacy);
		privacyView.setText(setPrivacySpan());
		privacyView.setMovementMethod(new LinkMovementMethod());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case request_Code:
			if (RESULT_OK == resultCode) { // 注册成功填充注册用户名及密码
				userName = data.getExtras().getString("userName");
				userPwd = data.getExtras().getString("userPwd");
				handler.sendEmptyMessage(0);
				finish();
			}
			break;
		}
	}

	private SpannableStringBuilder setPrivacySpan(){
//		String childPrivacyStr = "《儿童隐私政策》";
		String privacyStr = "《隐私政策》";
		String termStr = "《用户协议》";
//		String showMsg = "我已阅读并同意"+childPrivacyStr+"、"+privacyStr+"、"+termStr;
		String showMsg = "我已阅读并同意"+privacyStr+"和"+termStr;

		SpannableStringBuilder spanStr = new SpannableStringBuilder();
		spanStr.append(showMsg);
		//儿童隐私政策
//		int childPrivacyIndex = showMsg.indexOf(childPrivacyStr);
//		spanStr.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(@NonNull View widget) {
//				Intent intent = new Intent(LoginActivity.this, WebActivity.class);
//				String url = App.Url.CHILD_PROTOCOL_URL + App.APP_NAME_CH;
//				intent.putExtra("url", url);
//				intent.putExtra("title", childPrivacyStr);
//				startActivity(intent);
//			}
//
//			@Override
//			public void updateDrawState(@NonNull TextPaint ds) {
//				ds.setColor(getResources().getColor(R.color.colorPrimary));
//			}
//		},childPrivacyIndex,childPrivacyIndex+childPrivacyStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//隐私政策
		int privacyIndex = showMsg.indexOf(privacyStr);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(@NonNull View widget) {
				Intent intent = new Intent(LoginActivity.this, WebActivity.class);
				String url = WebConstant.HTTP_SPEECH_ALL+"/api/protocolpri.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1";
				intent.putExtra("url", url);
				intent.putExtra("title", privacyStr.replace("《","").replace("》",""));
				startActivity(intent);
			}

			@Override
			public void updateDrawState(@NonNull TextPaint ds) {
				ds.setColor(getResources().getColor(R.color.colorPrimary));
			}
		},privacyIndex,privacyIndex+privacyStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//用户协议
		int termIndex = showMsg.indexOf(termStr);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(@NonNull View widget) {
				Intent intent = new Intent(LoginActivity.this, WebActivity.class);
				String url = WebConstant.HTTP_SPEECH_ALL+"/api/protocoluse.jsp?apptype=" + getResources().getString(R.string.app_name) + "&company=1";
				intent.putExtra("url", url);
				intent.putExtra("title", termStr.replace("《","").replace("》",""));
				startActivity(intent);
			}

			@Override
			public void updateDrawState(@NonNull TextPaint ds) {
				ds.setColor(getResources().getColor(R.color.colorPrimary));
			}
		},termIndex,termIndex+termStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spanStr;
	}

	/**
	 * 验证
	 */
	public boolean verification() {
		userName = userNameET.getText().toString();
		userPwd = userPwdET.getText().toString();
		if (userName.length() < 3) {
			/*YoYo.with(Techniques.Shake).duration(200)
					.interpolate(new AccelerateInterpolator())
					.playOn(findViewById(R.id.linearLayout1));*/
			userNameET.setError(getResources().getString(
					R.string.login_check_effective_user_id));
			return false;
		}
		if (userPwd.length() == 0) {
			/*YoYo.with(Techniques.Shake).duration(200)
					.interpolate(new AccelerateInterpolator())
					.playOn(findViewById(R.id.linearLayout2));*/
			userPwdET.setError(getResources().getString(
					R.string.login_check_user_pwd_null));
			return false;
		}
		if (!checkUserPwd(userPwd)) {
			/*YoYo.with(Techniques.Shake).duration(200)
					.interpolate(new AccelerateInterpolator())
					.playOn(findViewById(R.id.linearLayout2));*/
			userPwdET.setError(getResources().getString(
					R.string.login_check_user_pwd_constraint));
			return false;
		}
		return true;
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

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				userNameET.setText(userName);
				userPwdET.setText(userPwd);
				break;
			case 1:
				cd.show();
				break;
			case 2:
				cd.dismiss();
				break;
			case 3:
				CustomToast.showToast(mContext,
						getResources().getString(R.string.welcome) + userName,
						1000);
				break;
			}
		}
	};

//	@Override
//	public void finish() {
//		super.finish();
//		if (AccountManager.getInstance().userName == null) {
//			SettingConfig.Instance().setAutoLogin(false);
//		}
//	}

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
	@NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
	void openGps(){
		// 登录操作
		if (verification()) {
			handler.sendEmptyMessage(1);
			AccountManager.getInstance().login(mContext, userName, userPwd,
					new RequestCallBack() {
						@Override
						public void requestResult(Request result) {
							LoginRequest rr = (LoginRequest) result;
							if (rr.isLoginSuccess()) {
								EventBus.getDefault().post(new LoginSuccessEvent());
//								if (SettingConfig.Instance().isAutoLogin()) {// 保存账户密码
//									handler.sendEmptyMessage(3);
//									AccountManager.getInstance().saveUserNameAndPwd(userName, userPwd);
//								} else {
//									AccountManager.getInstance().saveUserNameAndPwd(userName, "");
//								}
								AccountManager.getInstance().saveUserNameAndPwd(userName, userPwd);
								handler.sendEmptyMessage(2);
								SetUserLibUtils.setUserInfoLib(mContext);

								//设置自动登录
								SettingConfig.Instance().setAutoLogin(true);

								finish();
							} else {
								String errMsg="登录失败";
								if (rr.result!=null){
									switch (rr.result) {
										case "102":
											errMsg = "该用户名不存在";
											break;
										case "103":
											errMsg = "用户名密码不匹配";
											break;
										default:
											errMsg = "登录失败";
											break;
									}
								}
								CustomToast.showToast(mContext, errMsg, 1500);
								handler.sendEmptyMessage(2);
							}
						}
					});
		}

	}

	@OnPermissionDenied(android.Manifest.permission.ACCESS_FINE_LOCATION)
	void showDeniedForGps() {
		Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
	}
}
