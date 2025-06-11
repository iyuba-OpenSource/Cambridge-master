package com.iyuba.camstory.living;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.material.tabs.TabLayout;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.LiveContentBean;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.bean.living.LivePayedItem;
import com.iyuba.camstory.bean.living.LivePayedRecordXML;
import com.iyuba.camstory.fragment.LiveDescFragment;
import com.iyuba.camstory.listener.ILiveAvailableListener;
import com.iyuba.camstory.listener.ILiveDescListener;
import com.iyuba.camstory.listener.ILiveListListener;
import com.iyuba.camstory.living.fragment.LiveListFragment;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.camstory.lycam.manager.RuntimeManager;
import com.iyuba.camstory.lycam.network.IyuLiveRequestFactory;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.camstory.lycam.util.NetWorkState;
import com.iyuba.camstory.lycam.util.T;
import com.iyuba.camstory.lycam.widget.dialog.Dialog;
import com.iyuba.camstory.lycam.widget.dialog.WaittingDialog;
import com.iyuba.camstory.lycam.widget.imageview.RoundImageView;
import com.iyuba.camstory.manager.AccountManager;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：renzhy on 16/7/12 11:17
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveContentActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, View.OnClickListener {

	public String[] TITLES = {"课程介绍", "课程表"};
	private Context context;
	private BasePagerAdapter adapter;
	private ArrayList<Fragment> fragments;
	private LiveDescFragment liveDescFragment;
	private LiveListFragment liveListFragment;
	private ILiveDescListener fragmentDescListener;
	private ILiveListListener fragmentListListener;
	private ILiveAvailableListener fragmentAvailableListener;

	private Toolbar toolbar;
	private TextView mTitleView;
	private TextView mTextViewBack;

	private ViewPagerChangeListener listener = new ViewPagerChangeListener();

	ViewPager viewPager;
	ObservableScrollView observableScrollView;
	TabLayout tabLayout;
	TabLayout tabLayoutSticky;
	View viewToSticky;
	TextView tv_deadline_day_num;
	TextView tv_deadline_hour_num;
	TextView tv_apply_num;
	ImageView iv_content_background;
	TextView tv_content_name;
	TextView tv_course_curr_price;
	TextView tv_course_old_price;
	RoundImageView iv_teacher_iamge;
	TextView tv_teacher_name;
	TextView tv_course_date_start;
	TextView tv_course_date_end;
	TextView tv_course_validity;
	TextView tv_apply_curr_price;
	TextView tv_apply_old_price;
	View ll_course_apply;
	Button btn_apply_now;

	//直播介绍的内容
	private LivePackListBean.LivePackDataBean livePackData;
	//直播列表
	private ArrayList<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailData;
	private long mDayNum = 0,mHourNum = 0,distanceMillis = 0;
	private Date currentDate,closeDate;
	private GregorianCalendar currentCal,closeCal;
	private SimpleDateFormat simpleDateFormat;
	//当ProductId=0时，该课程是打包购买
	public static boolean isAllowedWatch = false;
	private LivePayedItem livePayedItemDB;
	private String ProductId;
	private String liveContentTitle;
	private int livePackId;
	private String liveBgImageUrl;
	private String liveTeacherImageUrl;
	private String liveCourseName;
	private String liveApplyNum;
	private String liveCurrPrice;
	private String liveOldPrice;
	private String liveTeacherName;
	private String liveStartDate;
	private String liveEndDate;
	private String liveCloseDate;

	//等待进度框
	protected Dialog mLoadingDialog;
	private Intent intent;

	public void createLoadingDialog(String msg) {
		if (mLoadingDialog == null) {
			mLoadingDialog = new WaittingDialog.Builder(context).setMessage(msg).create();
		}
	}

	public void showLoadingDialog() {
		mLoadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_content);
		context = this;
		livePayedItemDB = new LivePayedItem();
		fragments = new ArrayList<>();
		liveDescFragment = new LiveDescFragment();
		liveListFragment = new LiveListFragment();
		fragments.add(liveDescFragment);
		fragments.add(liveListFragment);
		setFragmentDescListener(liveDescFragment);
		setFragmentListListener(liveListFragment);
		setFragmentAvailableListener(liveListFragment);

		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentCal = new GregorianCalendar();
		closeCal = new GregorianCalendar();

		initData();
		initWidget();
		showLoadingDialog();
		if(NetWorkState.getInstance().isConnectByCondition(NetWorkState.EXCEPT_2G)){
			new GetLiveContentTask().execute();
//			if(AccountManager.Instance(context).checkUserLogin()){
//				new GetLivePayedRecordTask().execute();
//			}
		}else {
			handler.sendEmptyMessage(3);
		}


	}

	@Override
	public void onResume() {
		super.onResume();
//		if(AccountManager.Instance(context).checkUserLogin() && livePackData != null
//				&& livePayedItemOp.findById(AccountManager.Instance(context).getUserId(),livePackData.getId()) != null){
//			livePayedItemDB =  livePayedItemOp.findById(
//					AccountManager.getInstance().getUserId(),livePackData.getId());
//			if(livePayedItemDB != null && livePayedItemDB.getPackId().equals(livePackData.getId()+"")){
//				//设置“立即报名”消失
//				isAllowedWatch = true;
//				if(fragmentAvailableListener != null){
//					fragmentAvailableListener.onAvailableStatusUpdate(isAllowedWatch);
//				}
//				handler.sendEmptyMessage(1);
//			}
//		}
	}

	public void setFragmentDescListener(ILiveDescListener listener) {
		fragmentDescListener = listener;
	}

	public void setFragmentListListener(ILiveListListener listener) {
		fragmentListListener = listener;
	}

	public void setFragmentAvailableListener(ILiveAvailableListener listener){
		fragmentAvailableListener = listener;
	}

	public static Intent getIntent2Me(Context context) {
		Intent intent = new Intent(context, LiveContentActivity.class);
		return intent;
	}

	public static Intent getIntent2Me(Context context, LivePackListBean.LivePackDataBean livePack) {
		Intent intent = new Intent(context, LiveContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("LivePack", livePack);
		intent.putExtras(bundle);
		return intent;
	}

	private void initData() {
		livePackData = (LivePackListBean.LivePackDataBean) getIntent().getSerializableExtra("LivePack");
		if(livePackData != null){
			livePackId = livePackData.getId();
			liveContentTitle = livePackData.getName();
			liveCourseName = livePackData.getName();
			liveApplyNum = livePackData.getStudentNum();
			liveCurrPrice = livePackData.getPrice();
			liveOldPrice = livePackData.getRealprice();
			liveStartDate = livePackData.getStartDate();
			liveEndDate = livePackData.getEndDate();
			liveCloseDate = livePackData.getDateClose();
			liveBgImageUrl = ConstantManager.LIVE_CONTENT_BGPIC_BASE + livePackId + ".png";

			currentDate = new Date(System.currentTimeMillis());
			try {
				if(liveCloseDate != null){
					closeDate = simpleDateFormat.parse(liveCloseDate);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(currentDate != null && closeDate != null){
				currentCal.setTime(currentDate);
				closeCal.setTime(closeDate);
				if(closeCal.getTimeInMillis() > currentCal.getTimeInMillis()){
					distanceMillis = closeCal.getTimeInMillis()-currentCal.getTimeInMillis();
					mDayNum = distanceMillis/(1000*3600*24);
					mHourNum = distanceMillis/(1000*3600) % 24;
				}
			}
		}
	}

	protected void initWidget() {

		toolbar = findViewById(R.id.toolbar_course_content);
		mTitleView = toolbar.findViewById(R.id.center_title);
		mTextViewBack = toolbar.findViewById(R.id.nav_left_text);

		mTitleView.setText(liveContentTitle);
		mTextViewBack.setVisibility(View.VISIBLE);
		mTextViewBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		viewPager = findViewById(R.id.viewpager_course_content);
		observableScrollView = findViewById(R.id.sv_course_content);
		tabLayout = findViewById(R.id.tablayout_course_content);
		tabLayoutSticky = findViewById(R.id.tablayout_sticky);
		viewToSticky = findViewById(R.id.ll_course_desc_and_content_list);
		tv_deadline_day_num = findViewById(R.id.tv_apply_deadline_day_num);
		tv_deadline_hour_num = findViewById(R.id.tv_apply_deadline_hour_num);
		tv_apply_num = findViewById(R.id.tv_apply_num);
		iv_content_background = findViewById(R.id.iv_course_desc_image);
		tv_content_name = findViewById(R.id.tv_course_name);
		tv_course_curr_price = findViewById(R.id.tv_course_curr_price);
		tv_course_old_price = findViewById(R.id.tv_course_old_price);
		iv_teacher_iamge = findViewById(R.id.iv_teacher_image);
		tv_teacher_name = findViewById(R.id.tv_teacher_name);
		tv_course_date_start = findViewById(R.id.tv_course_date_start);
		tv_course_date_end = findViewById(R.id.tv_course_date_end);
		tv_course_validity = findViewById(R.id.tv_course_validity);
		tv_apply_curr_price = findViewById(R.id.tv_apply_curr_price);
		tv_apply_old_price = findViewById(R.id.tv_apply_old_price);
		ll_course_apply = findViewById(R.id.ll_course_apply);
		btn_apply_now = findViewById(R.id.btn_apply_now);

		btn_apply_now.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent();
  			if(AccountManager.getInstance().checkUserLogin()){
				intent.putExtra("LivePack",livePackData);
				intent.setClass(LiveContentActivity.this,BuyCourseActivity.class);
				startActivity(intent);
			}else{
				intent.setClass(LiveContentActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			}
		});

		createLoadingDialog(context.getString(R.string.loading_and_waitting));
		adapter = new BasePagerAdapter(getSupportFragmentManager(), TITLES, fragments);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(listener);
		tabLayout.setupWithViewPager(viewPager);
		tabLayoutSticky.setupWithViewPager(viewPager);

		observableScrollView.setScrollViewCallbacks(this);
		observableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				onScrollChanged(observableScrollView.getScrollY(), true, true);
			}
		});

		liveDetailData = new ArrayList<>();

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = null,endDate = null,closeDate = null;

		try {
			if(liveStartDate != null && liveEndDate != null & liveCloseDate != null){
				startDate = sdf.parse(liveStartDate);
				endDate = sdf.parse(liveEndDate);
				closeDate = sdf.parse(liveCloseDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tv_content_name.setText(liveCourseName);
		tv_apply_num.setText(liveApplyNum);
		tv_deadline_day_num.setText(mDayNum+"");
		tv_deadline_hour_num.setText(mHourNum+"");
		tv_course_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_course_curr_price.setText("￥"+liveCurrPrice);
		tv_course_old_price.setText("￥"+liveOldPrice);
		if(startDate!=null & endDate != null & closeDate != null){
			tv_course_date_start.setText(sdf.format(startDate));
			tv_course_date_end.setText(sdf.format(endDate));
			tv_course_validity.setText(sdf.format(closeDate));
		}
		tv_apply_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_apply_curr_price.setText("￥"+liveCurrPrice);
		tv_apply_old_price.setText("￥"+liveOldPrice);
		ImageLoader.getInstance().displayImage(liveBgImageUrl, iv_content_background);
	}

	public void initAsnycData() {
		tv_teacher_name.setText(liveTeacherName);
		ImageLoader.getInstance().displayImage(liveTeacherImageUrl, iv_teacher_iamge);
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
		Log.w("onScroll", "Y:" + scrollY + "|" + viewToSticky.getTop() + "|" + Math.max(viewToSticky.getTop(), scrollY));
		tabLayoutSticky.setTranslationY(Math.max(viewToSticky.getTop(), scrollY));
	}

	@Override
	public void onDownMotionEvent() {

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {

	}

	@Override
	public void onClick(View view) {
//		switch (view.getId()) {
//			case R.id.nav_left_text:
//				onBackPressed();
//				break;
//			case R.id.btn_apply_now:
//				if(AccountManager.getInstance().checkUserLogin()){
//					startActivity(BuyCourseActivity.getIntent2Me(context,livePackData));
//				}else {
//					startActivity(LoginActivity.getIntent2Me(context));
//				}
//				break;
//		}
		if(view.getId() == R.id.btn_apply_now){
			T.s(context,"购买课程!");
		}
	}

	private class GetLivePayedRecordTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getLivePayedRecord();
			return null;
		}
	}

	private class GetLiveContentTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getLiveContentData();
			return null;
		}
	}

	public void getLivePayedRecord(){
		Call<LivePayedRecordXML> call = IyuLiveRequestFactory
				.getPayedRecordApi().getPayedRecord(
						ConfigManager.Instance().loadString("userId"),
						ConstantManager.getInstance().getAppId(),
						livePackId+"",
						MD5.getMD5ofStr(ConstantManager.getInstance().getAppId()
								+ConfigManager.Instance().loadString("userId")
								+livePackId
								+"iyuba")
				);
		call.enqueue(new Callback<LivePayedRecordXML>() {
			@Override
			public void onResponse(Call<LivePayedRecordXML> call, Response<LivePayedRecordXML> response) {
				if(response != null && response.body() != null){
					if(response.body().getListPayedRecord().size() > 0){
						ProductId = response.body().getListPayedRecord().get(0).getProductId();
						if(livePackId == Integer.parseInt(response.body().getListPayedRecord().get(0).getPackId())){
							LivePayedItem livePayedItem = new LivePayedItem();
							livePayedItem.setUid("0");
//							livePayedItem.setUid(AccountManager.Instance(context).USERID);
							livePayedItem.setPackId(response.body().getListPayedRecord().get(0).getPackId());
							livePayedItem.setProductId(response.body().getListPayedRecord().get(0).getProductId());
							livePayedItem.setAmount(response.body().getListPayedRecord().get(0).getAmount());
							livePayedItem.setFlg(response.body().getListPayedRecord().get(0).getFlg());
							//设置“立即报名”消失
							isAllowedWatch = true;
							if(fragmentAvailableListener != null){
								fragmentAvailableListener.onAvailableStatusUpdate(isAllowedWatch);
							}
							handler.sendEmptyMessage(1);
						}
					}
				}
			}

			@Override
			public void onFailure(Call<LivePayedRecordXML> call, Throwable t) {
				dismissLoadingDialog();
				handler.sendEmptyMessage(2);
			}

		});
	}

	public void getLiveContentData() {

		Call<LiveContentBean> call = IyuLiveRequestFactory
				.getLiveContentApi().getLiveContent(
						"10104", 1, livePackId, MD5.getMD5ofStr("10104class" + livePackId));
		call.enqueue(new Callback<LiveContentBean>() {
			@Override
			public void onResponse(Call<LiveContentBean> call, Response<LiveContentBean> response) {
				if (response != null && response.body() != null
						&& response.body().getResult() == 1) {
					liveTeacherName = response.body().getTeacher().getTname();
					liveTeacherImageUrl = ConstantManager.LIVE_CONTENT_TEACHER_IMG_BASE
							+ response.body().getTeacher().getTimg();

					for(LiveContentBean.LiveTitleBean liveTitle:response.body().getData()){
						for(LiveContentBean.LiveTitleBean.LiveDetailBean liveDetail:liveTitle.getBtlist()){
							liveDetailData.add(liveDetail);
						}
					}
					handler.sendEmptyMessage(0);

					if (fragmentDescListener != null) {
						fragmentDescListener.onFragmentDescUpdate(
								response.body().getDetail(),
								response.body().getTeacher().getTdes(),
								response.body().getCondition());
						dismissLoadingDialog();
					}
					if(fragmentListListener != null){
						fragmentListListener.onFragmentListUpdate(liveDetailData);
					}
				}
			}

			@Override
			public void onFailure(Call<LiveContentBean> call, Throwable t) {
				dismissLoadingDialog();
				handler.sendEmptyMessage(2);
			}
		});
	}

	/**
	 * 重新设置viewPager高度
	 *
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {
		View child = viewPager.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager
					.getLayoutParams();
			params.height = h + 100;
			viewPager.setLayoutParams(params);
		}
	}

	public class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int position, float positionOffset,
								   int positionOffsetPixels) {
		}
		@Override
		public void onPageSelected(int position) {
			// 页面切换后重置ViewPager高度
			switch (position) {
				case 0:
					resetViewPagerHeight(0);
					break;
				case 1:
					ViewGroup.LayoutParams mParams = viewPager.getLayoutParams();
					mParams.height = (RuntimeManager.dip2px(77)) * liveDetailData.size() + RuntimeManager.dip2px(8);
					mParams.width = RuntimeManager.getWindowWidth();
					viewPager.setLayoutParams(mParams);
					break;
			}
		}
	}

	class BasePagerAdapter extends FragmentPagerAdapter {

		String[] titles;
		ArrayList<Fragment> fragments;

		public BasePagerAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> fragments) {
			super(fm);
			this.titles = titles;
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					initAsnycData();
					resetViewPagerHeight(0);
					break;
				case 1:
					ll_course_apply.setVisibility(View.GONE);
					break;
				case 2:
					T.s(context, R.string.alert_network_error);
					break;
				case 3:
					T.s(context, R.string.no_internet);
					break;
			}
		}
	};
}