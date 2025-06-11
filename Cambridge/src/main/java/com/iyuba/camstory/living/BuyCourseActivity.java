package com.iyuba.camstory.living;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.WebActivity;
import com.iyuba.camstory.bean.living.CheckIyubiRspBean;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.bean.living.LivePayedItem;
import com.iyuba.camstory.bean.living.PayedResultXML;
import com.iyuba.camstory.lycam.activity.BaseActivity;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.camstory.lycam.network.IyuLiveRequestFactory;
import com.iyuba.camstory.lycam.util.T;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import butterknife.BindView;
/**
 * 作者：renzhy on 16/8/15 10:22
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BuyCourseActivity extends BaseActivity implements View.OnClickListener{

	private final static int PAYPAL_TYPE = 1;
	private final static int IYUBI_TYPE = 2;
	private int mPayType = IYUBI_TYPE;
	private double mIyubiAmount = 0;
	private double mCoursePrice = 0;
	private int packageId;
	private String resultBalance;
	private LivePackListBean.LivePackDataBean livePackData;
	private View root;
	private RadioGroup mRgPayType;
	private RadioButton mRbPaypal;
	private RadioButton mRbIyubi;
	private TextView mTvPayPrice;
	private TextView mTvBuyCourseTitle;
	private TextView mTvPrice;
	private Button mBtnPay;

	/*@BindView(R.id.root)
	View root;
	@BindView(R.id.rg_pay_type)
	RadioGroup mRgPayType;
	@BindView(R.id.rb_buy_course_paypal)
	RadioButton mRbPaypal;
	@BindView(R.id.rb_buy_course_iyubi)
	RadioButton mRbIyubi;
	@BindView(R.id.tv_buy_course_payprice)
	TextView mTvPayPrice;
	@BindView(R.id.tv_buy_course_title)
	TextView mTvBuyCourseTitle;
	@BindView(R.id.tv_buy_course_price)
	TextView mTvPrice;
	@BindView(R.id.btn_pay_now)
	Button mBtnPay;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_course);
		findView();
		//ButterKnife.bind(this);
		livePackData = (LivePackListBean.LivePackDataBean) getIntent().getSerializableExtra("LivePack");
		packageId = livePackData.getId();
		initWidget();
		setListener();
	}

	private void findView() {
		root = findViewById(R.id.root);
		mRgPayType = findViewById(R.id.rg_pay_type);
		mRbPaypal = findViewById(R.id.rb_buy_course_paypal);
		mRbIyubi = findViewById(R.id.rb_buy_course_iyubi);
		mTvPayPrice = findViewById(R.id.tv_buy_course_payprice);
		mTvBuyCourseTitle = findViewById(R.id.tv_buy_course_title);
		mTvPrice = findViewById(R.id.tv_buy_course_price);
		mBtnPay = findViewById(R.id.btn_pay_now);

	}

	/*@Override
	protected int getLayoutId() {
		return R.layout.activity_buy_course;
	}*/

	public void onResume() {
		super.onResume();
		if(String.valueOf(AccountManager.getInstance().userId) != null && !String.valueOf(AccountManager.getInstance().userId).equals("0")){
			getIyubiAmount(String.valueOf(AccountManager.getInstance().userId), ConstantManager.FORMAT_JSON);
		}
	}

	public static Intent getIntent2Me(Context context, LivePackListBean.LivePackDataBean livePackData) {
		Intent intent = new Intent(context, BuyCourseActivity.class);
		intent.putExtra("LivePack",livePackData);
		return intent;
	}

	/*@Override
	protected void initToolbarView() {
		super.initToolbarView();
	}*/


	protected void initWidget() {
		//super.initWidget();
		//createLoadingDialog(context.getString(R.string.loading_and_waitting));
		if(livePackData != null){
			//getToolbarTitle().setText(livePackData.getName());
			mTvBuyCourseTitle.setText(livePackData.getName());
			mTvPayPrice.setText("￥"+livePackData.getPrice());
			mTvPrice.setText("￥"+livePackData.getPrice());
			mCoursePrice = Double.parseDouble(livePackData.getPrice());
		}
	}


	protected void setListener() {
		//super.setListener();
		mRbPaypal.setOnClickListener(this);
		mRbIyubi.setOnClickListener(this);
		mBtnPay.setOnClickListener(this);
	}

	public void PayCourseByType(int type){
		switch (type){
			case IYUBI_TYPE:
				if(mIyubiAmount < mCoursePrice){
					final MaterialDialog materialDialog = new MaterialDialog(context);
					materialDialog
							.setPositiveButton(R.string.buy_course_recharge, new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									materialDialog.dismiss();
									Intent intent = new Intent(context, WebActivity.class);
									intent.putExtra("url", "http://app."+Constant.IYBHttpHead+"/wap/index.jsp?uid="
											+String.valueOf(AccountManager.getInstance().userId));
									intent.putExtra("title", context.getString(R.string.buy_iyubi));
									startActivity(intent);
								}
							})
							.setNegativeButton(R.string.cancel, new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									materialDialog.dismiss();
								}
							});
					materialDialog
							.setTitle(R.string.buy_balance_not_enough)
							.setMessage(context.getString(R.string.buy_balance_not_enough_tip,
									(int)mCoursePrice,(int)mIyubiAmount));
//							.setMessage("当前购买需要支付" + mCoursePrice + "爱语币,您的余额为"+mIyubiAmount+",不足以支付，是否充值？");
					materialDialog.show();
				}else {
					final MaterialDialog materialDialog = new MaterialDialog(context);
					materialDialog
							.setPositiveButton(R.string.buy_course_pay, new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									materialDialog.dismiss();
									payForCourse(String.valueOf(AccountManager.getInstance().userId),(int)mCoursePrice,packageId);
								}
							})
							.setNegativeButton(R.string.cancel, new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									materialDialog.dismiss();
								}
							});
					materialDialog
							.setTitle(R.string.buy_course)
							.setMessage(context.getString(R.string.buy_course_enough_tip,(int)mCoursePrice));
//							.setMessage("您需要支付" + mCoursePrice + "爱语币购买该课程，是否确认购买?");
					materialDialog.show();
				}
				break;
			case PAYPAL_TYPE:
				T.s(context,R.string.buy_course_paypal);
				break;
			default:
				T.s(context,R.string.buy_course_pay_type_select);
				break;
		}
	}

	public void payForCourse(String userId,final int amount,final int packageId){
		showLoadingDialog();
		Call<PayedResultXML> call = IyuLiveRequestFactory.getPayForCourseApi().getPayResult(
				userId,
				amount,
				ConstantManager.getInstance().getAppId(),
				0,
				packageId,
				MD5.getMD5ofStr(amount+ConstantManager.getInstance().getAppId()+userId+packageId+"0"+"iyuba"),
				ConstantManager.REQUEST_ZHIBO_SIGN

		);
		call.enqueue(new Callback<PayedResultXML>() {
			@Override
			public void onResponse(Call<PayedResultXML> call, Response<PayedResultXML> response) {
				if(response != null && response.body() != null){
					PayedResultXML payedResultXML = response.body();
					if(payedResultXML.getResult().equals("1")){
						resultBalance = payedResultXML.getAmount();
						if(payedResultXML.getMsg().equals(" already buy")){
							T.s(context,R.string.buy_course_already_owned);
						}else {
							LivePayedItem livePayedItem = new LivePayedItem();
							livePayedItem.setUid(String.valueOf(AccountManager.getInstance().userId));
							livePayedItem.setPackId(packageId+"");
							livePayedItem.setProductId("0");
							livePayedItem.setAmount(amount+"");
							livePayedItem.setFlg("1");
							//new LivePayedItemOp().saveData(livePayedItem);
							handler.sendEmptyMessage(0);
						}
					}else {
						T.s(context,R.string.buy_course_error);
					}
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailure(Call<PayedResultXML> call, Throwable t) {
				T.s(context,R.string.buy_course_error);
				dismissLoadingDialog();
			}
		});
	}

	public void getIyubiAmount(String userId, String format) {
		Call<CheckIyubiRspBean> call = IyuLiveRequestFactory.getCheckIyubiApi().getIyubiAmount(
				userId, format);
		call.enqueue(new Callback<CheckIyubiRspBean>() {
			@Override
			public void onResponse(Call<CheckIyubiRspBean> call, Response<CheckIyubiRspBean> response) {
				if(response != null && response.body() != null){
					final CheckIyubiRspBean checkIyubiRspBean = response.body();
					if(checkIyubiRspBean.getResult().equals("1")){
						mIyubiAmount = Integer.parseInt(checkIyubiRspBean.getAmount());
					}else {
						T.s(context,R.string.request_iyubi_error);
					}
				}
			}

			@Override
			public void onFailure(Call<CheckIyubiRspBean> call, Throwable t) {
				T.s(context,R.string.request_iyubi_fail);
			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.rb_buy_course_iyubi:
				mPayType = IYUBI_TYPE;
				break;
			case R.id.rb_buy_course_paypal:
				mPayType = PAYPAL_TYPE;
				break;
			case R.id.btn_pay_now:
				if(AccountManager.getInstance().checkUserLogin()){
					PayCourseByType(mPayType);
				}else {
					//startActivity(LoginActivity.getIntent2Me(context));
				}

				break;
		}
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					T.s(context,context.getString(R.string.buy_course_balance_now,resultBalance));
					finish();
					break;
				case 1:
					break;
				default:
					break;
			}
		}
	};
}
