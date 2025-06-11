package com.iyuba.camstory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.OnResultListener;
import com.iyuba.camstory.listener.PayVipRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.manager.PayManager;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.WaittingDialog;
import com.iyuba.voa.activity.setting.Constant;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.umeng.analytics.MobclickAgent;
/**
 * 购买VIP
 * 
 */
public class BuyVipActivity extends Activity {
	private static final String TAG = BuyVipActivity.class.getSimpleName();
	private static final int ONE_MONTH_ALL = 200;
	private static final int THREE_MONTH_ALL = 600;
	private static final int SIX_MONTH_ALL = 1000;
	private static final int TWELVE_MONTH_ALL = 2000;
	private static final int LIFELONG_APP = 1000;
	private Context mContext;
	private WindowManager mWindowManager;
	private NightModeManager nightModeManager;
	private View relativeLayout_noLogin, relativetLayout_login, backView;
	private TextView username, account_balance;
	private Button loginBtn, btn_oneM, btn_threeM, btn_sixM, btn_twelveM;
	private TextView applifevip;

	private String pay_account;
	private String pay_month;
	private int iyubi_balance;
	private CustomDialog waittingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_vip2);
		mContext = this;
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		nightModeManager = new NightModeManager(mWindowManager, mContext);
		//initActionbar();
		initview();

		waittingDialog = new WaittingDialog().wettingDialog(mContext);
	}

	/*private void initActionbar() {
		SpannableString ss = new SpannableString("会员商店");
		ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		actionBar = this.getSupportActionBar();
		actionBar.setTitle(ss);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.color.camstorygreen));
	}*/

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.buyvip_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		} else if (itemId == R.id.buyvip_iyubi) {
			Intent intent = new Intent();
			
			  intent.setClass(mContext, WebActivity.class); 
			  intent.putExtra( "url",  "http://app."+Constant.IYBHttpHead+"/wap/index.jsp?uid=" +
			  AccountManager.getInstance().userId + "&appid=" + Constant.getAppid());
			 
			//intent.setClass(mContext, BuyIyubiActivity.class);
			startActivity(intent);
		}
		return true;
	}*/

	private void initview() {
		backView = findViewById(R.id.buyvip_backlayout);
		relativeLayout_noLogin = findViewById(R.id.buyvip_noLoginlayout);
		relativetLayout_login = findViewById(R.id.buyvip_loginlayout);
		loginBtn = findViewById(R.id.buyvip_login);
		username = findViewById(R.id.buy_username);
		account_balance = findViewById(R.id.buy_accountBalance);
		btn_oneM = findViewById(R.id.btn_onemonth);
		btn_threeM = findViewById(R.id.btn_threemonth);
		btn_sixM = findViewById(R.id.btn_sixmonth);
		btn_twelveM = findViewById(R.id.btn_twelvemonth);
		applifevip = findViewById(R.id.app_lifelong_tv);
		setOnClickListener();
	}

	private void setOnClickListener() {
		loginBtn.setOnClickListener(ocl);
		btn_oneM.setOnClickListener(ocl);
		btn_threeM.setOnClickListener(ocl);
		btn_sixM.setOnClickListener(ocl);
		btn_twelveM.setOnClickListener(ocl);
		applifevip.setOnClickListener(ocl);
	}

	/**
	 * @param viptype
	 *          购买全站还是单独APP的VIP。
	 * 
	 *          0表示全站，1表示单独APP
	 */
	private void buyVip(int viptype) {
		waittingDialog.show();
		if (0 == viptype) {
			final PayVipRequest request = new PayVipRequest(
					AccountManager.getInstance().userId, Integer.valueOf(pay_account),
					Integer.valueOf(pay_month), new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							runOnUiThread(new Runnable() {
								public void run() {
									waittingDialog.dismiss();
									CustomToast.showToast(mContext, "请求错误..请重试", 1000);
								}
							});
						}
					}, new RequestCallBack() {

						@Override
						public void requestResult(Request result) {
							PayVipRequest request = (PayVipRequest) result;
							if (request.isBuyVipSuccess()) {
								Message hmsg = handler.obtainMessage(2, request.getAmount());// 对话框提示支付成功
								handler.sendMessage(hmsg);
							} else {
								Message hmsg = handler.obtainMessage(2, request.getAmount());// 对话框提示支付成功
								handler.sendMessage(hmsg);
							}
						}
					});
			CrashApplication.getInstance().getQueue().add(request);

		} else if (1 == viptype) {
			PayManager.getInstance(mContext).payAmount(mContext,
					AccountManager.getInstance().userId, pay_account, pay_month,
					new OnResultListener() {
						@Override
						public void OnSuccessListener(String msg) {
							Message hmsg = handler.obtainMessage(2, msg);// 对话框提示支付成功
							handler.sendMessage(hmsg);
						}

						@Override
						public void OnFailureListener(String msg) {
							handler.sendEmptyMessage(1);
						}
					});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (!AccountManager.getInstance().checkUserLogin()) {
			relativeLayout_noLogin.setVisibility(View.VISIBLE);
			relativetLayout_login.setVisibility(View.GONE);
		} else {
			fillUserInfo();
			relativeLayout_noLogin.setVisibility(View.GONE);
			relativetLayout_login.setVisibility(View.VISIBLE);
		}
		nightModeManager.checkMode();
	}

	private void fillUserInfo() {
		username.setText(AccountManager.getInstance().userName);
		iyubi_balance = Integer.parseInt(ConfigManagerVOA.Instance(mContext)
				.loadString("currUserAmount"));
		account_balance.setText(iyubi_balance + "");
	}

	@Override
	public void finish() {
		super.finish();
		nightModeManager.remove();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn_oneM) {
				if (iyubi_balance >= ONE_MONTH_ALL) {
					pay_account = ONE_MONTH_ALL + "";
					pay_month = "1";
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			} else if (v == btn_threeM) {
				if (iyubi_balance >= THREE_MONTH_ALL) {
					pay_account = THREE_MONTH_ALL + "";
					pay_month = "3";
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			} else if (v == btn_sixM) {
				if (iyubi_balance >= SIX_MONTH_ALL) {
					pay_account = SIX_MONTH_ALL + "";
					pay_month = "6";
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			} else if (v == btn_twelveM) {
				if (iyubi_balance >= TWELVE_MONTH_ALL) {
					pay_account = TWELVE_MONTH_ALL + "";
					pay_month = "12";
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			} else if (v == applifevip) {
				if (ConfigManagerVOA.Instance(mContext).loadString("validity")
						.contains("2099")) {
					CustomToast.showToast(mContext, R.string.buy_already, 1000);
				} else if (iyubi_balance >= LIFELONG_APP) {
					pay_account = LIFELONG_APP + "";
					pay_month = "0";
					handler.sendEmptyMessage(3);
				} else {
					handler.sendEmptyMessage(1);
				}
			} else if (v == loginBtn) {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
			}
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Dialog dialog = new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.alert_title)
						.setMessage(R.string.alert_buy_content)
						.setPositiveButton(R.string.alert_btn_buy,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										buyVip(0);
									}
								}).setNeutralButton(R.string.alert_btn_cancel, null).create();
				dialog.show();
				break;
			case 3:
				Dialog dialog2 = new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.alert_title)
						.setMessage(R.string.alert_buy_content)
						.setPositiveButton(R.string.alert_btn_buy,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										buyVip(1);
									}
								}).setNeutralButton(R.string.alert_btn_cancel, null).create();
				dialog2.show();
				break;
			case 1:
				waittingDialog.dismiss();
				Dialog dialog1 = new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.alert)
						.setMessage(R.string.alert_recharge_content)
						.setPositiveButton(R.string.alert_btn_recharge,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Intent intent = new Intent();
										intent.setClass(mContext, WebActivity.class);
										intent.putExtra("url",
												"http://app."+Constant.IYBHttpHead+"/wap/index.jsp?uid="
														+ AccountManager.getInstance().userId + "&appid="
														+ Constant.getAppid());
										startActivity(intent);
									}
								})
						.setNeutralButton(
								getResources().getString(R.string.alert_btn_cancel), null)
						.create();
				dialog1.show();
				break;
			case 2:
				waittingDialog.dismiss();
				String currentBalance = msg.obj.toString();
				CustomToast
						.showToast(mContext, getResources().getString(R.string.buy_success)
								+ msg.obj.toString(), 1000);
				ConfigManagerVOA.Instance(mContext).putString("currUserAmount",
						currentBalance);
				account_balance.setText(currentBalance);
				break;
			default:
				break;
			}
		}
	};

}
