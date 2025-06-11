package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.iyuba.camstory.bean.BrandUtil;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.FeedBackJsonRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.VersionManager;
import com.iyuba.camstory.utils.ScreenUtils;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.voa.activity.setting.Constant;
import com.umeng.analytics.MobclickAgent;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.iyuba.camstory.bean.Util.startQQ;
import static com.iyuba.camstory.bean.Util.startQQGroup;

/**
 * @author chentong
 * 
 */
public class Feedback extends Activity {
	private static final String TAG = Feedback.class.getSimpleName();

	private View backView;
	private CustomDialog waitingDialog;
	private Button backBtn;
	private View submit;
	private EditText content, email;
	private Feedback mContext;
	private boolean underway = false;
	private ImageView qq_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);
		backView = findViewById(R.id.backlayout);
		backView.setBackgroundColor(Constant.getBackgroundColor());
		waitingDialog = new WaittingDialog().wettingDialog(Feedback.this);
		content = findViewById(R.id.editText_info);
		email = findViewById(R.id.editText_Contact);

		backBtn = findViewById(R.id.button_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		submit = findViewById(R.id.ImageView_submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!underway) {
					underway = true;
					waitingDialog.show();
					int uid;
					if(AccountManager.getInstance().checkUserLogin()){
						uid = AccountManager.getInstance().userId;
					}else{
						uid = 0;
					}

					// int uid = AccountManager.getInstance().userId;
					if (verification()) {
						FeedBackJsonRequest request = new FeedBackJsonRequest(content
								.getText().toString()
								+ "  appversion:["
								+ VersionManager.getInstance().VERSION_NAME
								+ "]versionCode:["
								+ VersionManager.getInstance().VERSION_CODE
								+ "]phone:["
								+ android.os.Build.BRAND
								+ android.os.Build.MODEL
								+ android.os.Build.DEVICE
								+ "]sdk:["
								+ android.os.Build.VERSION.SDK_INT
								+ "]sysversion:["
								+ android.os.Build.VERSION.RELEASE + "]", email.getText()
								.toString(), uid, new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// FeedBackJsonRequest rs = (FeedBackJsonRequest)result;
								waitingDialog.dismiss();
								handler.sendEmptyMessage(0);
								finish();
							}
						});
						CrashApplication.getInstance().getQueue().add(request);

					}else {
						underway = false;
					}
				} else {
					handler.sendEmptyMessage(2);
				}
			//}
			}
		});
		qq_image = (ImageView) findViewById(R.id.qq_image);
		qq_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showQQDialog(mContext, v);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				underway = false;
				CustomToast.showToast(Feedback.this, R.string.feedback_submit_success,
						1000);
				break;
			case 1:
				underway = false;
				CustomToast.showToast(Feedback.this, R.string.feedback_network_error,
						1000);
				break;
			case 2:
				CustomToast.showToast(Feedback.this, R.string.feedback_submitting, 1000);
				break;
			}
		}
	};

	/**
	 * 验证
	 */
	public boolean verification() {
		String contentString = content.getText().toString();
		String emailString = email.getText().toString();

		if (contentString.length() == 0) {
			waitingDialog.dismiss();
			content.setError("反馈信息不能为空！");
			Toast.makeText(getApplicationContext(), "反馈信息不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (emailString.length() == 0){
			waitingDialog.dismiss();
			email.setError("邮箱不能为空");
			Toast.makeText(getApplicationContext(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		} else if (emailString.length() != 0) {
			if (!emailCheck(emailString)) {
				waitingDialog.dismiss();
				email.setError(getResources().getString(
						R.string.feedback_effective_email));
				Toast.makeText(getApplicationContext(), "请填写有效的email！", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			if (!AccountManager.getInstance().checkUserLogin()) {
				waitingDialog.dismiss();
				email.setError("请填写email！");
				Toast.makeText(getApplicationContext(), "请填写email！", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return true;
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

	public void finish() {
		super.finish();
	}

	public void showQQDialog(final Context mContext, View v) {


		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		TextView tv1 = new TextView(mContext);
		TextView tv2 = new TextView(mContext);
		TextView tv3 = new TextView(mContext);

		tv1.setTextSize(16);
		tv2.setTextSize(16);
		tv3.setTextSize(16);

		tv1.setPadding(10, ScreenUtils.dp2px(mContext, 10), 10, 0);
		tv2.setPadding(10, ScreenUtils.dp2px(mContext, 20), 10, ScreenUtils.dp2px(mContext, 20));
		tv3.setPadding(10, 0, 10, ScreenUtils.dp2px(mContext, 10));

		tv1.setTextColor(Color.BLACK);
		tv2.setTextColor(Color.BLACK);
		tv3.setTextColor(Color.BLACK);

		BrandUtil.requestQQGroupNumber(mContext);//网络请求
		tv1.setText(String.format("%s用户群: %s", BrandUtil.getBrandChinese(), BrandUtil.getQQGroupNumber(mContext)));
		tv2.setText("客服QQ: 3099007489");
		tv3.setText("技术QQ: 1161178411");

		linearLayout.addView(tv1);
		linearLayout.addView(tv2);
		linearLayout.addView(tv3);

		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startQQGroup(mContext, BrandUtil.getQQGroupKey(mContext));
			}
		});

		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startQQ(mContext, "3099007489");
			}
		});

		tv3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startQQ(mContext, "1161178411");
			}
		});


		BubbleLayout bl = new BubbleLayout(this);
		new BubbleDialog(mContext)
				.addContentView(linearLayout)
				.setClickedView(v)
				.setPosition(BubbleDialog.Position.BOTTOM)
				.calBar(true)
				.setBubbleLayout(bl)
				.show();


		bl.setLook(BubbleLayout.Look.TOP);
	}
}
