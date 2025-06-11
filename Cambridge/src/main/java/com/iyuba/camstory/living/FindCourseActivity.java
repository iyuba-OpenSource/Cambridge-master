package com.iyuba.camstory.living;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.bean.living.SlideShowListBean;
import com.iyuba.camstory.listener.IOnClickListener;
import com.iyuba.camstory.listener.IOnDoubleClick;
import com.iyuba.camstory.lycam.adapter.FindCourseAdapter;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.camstory.lycam.network.IyuLiveRequestFactory;
import com.iyuba.camstory.lycam.network.MicroClassRequestFactory;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.camstory.lycam.util.NetWorkState;
import com.iyuba.camstory.lycam.util.T;
import com.iyuba.camstory.lycam.widget.DividerItemDecoration;
import com.iyuba.camstory.lycam.widget.recycleview.CustomSwipeToRefresh;
import com.iyuba.camstory.lycam.widget.swiperefreshlayout.MySwipeRefreshLayout;

import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：renzhy on 16/7/7 14:36
 * 邮箱：renzhongyigoo@gmail.com
 */
public class FindCourseActivity extends AppCompatActivity implements MySwipeRefreshLayout.OnRefreshListener, IOnClickListener {

	private static final int PRELOAD_SIZE = 6;

	private Context context;
	private Toolbar toolbar;
	private TextView mTitleView;
	private TextView mTextViewBack;
	private RecyclerView mRecyclerView;
	private CustomSwipeToRefresh mSwipeRefreshLayout;
	private ImageView ivScrollTop;

	private String reqPackId = "-2";
	private String reqPackDesc = "class.live";
	private int iLastPage = 0;
	private int pageNum = 1;
	private static boolean isLast = false;
	private ArrayList<Integer> localImages = new ArrayList<>();
	private ArrayList<LivePackListBean.LivePackDataBean> livePackDataBeanArrayList = new ArrayList<>();
	private ArrayList<SlideShowListBean.SlideShowDataBean> slideShowList = new ArrayList<>();
	private FindCourseAdapter findCourseAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_course);

		context = this;
		loadImageData();
		initWidget();

		if (NetWorkState.getInstance().isConnectByCondition(NetWorkState.EXCEPT_2G)) {
			getSlidePicData();
			pageNum = 1;
			isLast = false;
			try {
				getRefreshPackData(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			handler.sendEmptyMessage(4);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void initWidget() {
		toolbar = findViewById(R.id.toolbar_find_course);
		mTitleView = toolbar.findViewById(R.id.center_title);
		mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_find_course);
		mRecyclerView = findViewById(R.id.recyclerview_find_course);
		ivScrollTop = findViewById(R.id.iv_scroll_top);
		mTextViewBack = toolbar.findViewById(R.id.nav_left_text);

		mTitleView.setText("爱语课堂");
		mTextViewBack.setVisibility(View.VISIBLE);
		mTextViewBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		toolbar.setOnTouchListener(new IOnDoubleClick(this, context.getString(R.string.list_doubleclick_to_head)));
		mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		// 确定每个item的排列方式
		LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
			@Override
			public RecyclerView.LayoutParams generateDefaultLayoutParams() {
				return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		};
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		findCourseAdapter = new FindCourseAdapter(context);
		findCourseAdapter.clearList();
		findCourseAdapter.setCategoryData(localImages);
		mRecyclerView.setAdapter(findCourseAdapter);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				context, R.drawable.recycler_rectangle, DividerItemDecoration.VERTICAL_LIST));
		ivScrollTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mRecyclerView.smoothScrollToPosition(0);
			}
		});
		findCourseAdapter.setOnItemClickListener(new FindCourseAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				startActivity(LiveContentActivity.getIntent2Me(
						context,
						livePackDataBeanArrayList.get(position)));
			}

			@Override
			public void onLongClick(View view, int position) {
			}
		});
	}

	@Override
	public void onRefresh(int index) {
		if (NetWorkState.getInstance().isConnectByCondition(NetWorkState.ALL_NET)) {
			getSlidePicData();
			pageNum = 1;
			isLast = false;
			getRefreshPackData(true);
		} else {
			handler.sendEmptyMessage(4);
		}

	}

	@Override
	public void onLoad(int index) {
		if (NetWorkState.getInstance().isConnectByCondition(NetWorkState.ALL_NET)) {
			getRefreshPackData(false);
		} else {
			handler.sendEmptyMessage(4);
		}
	}

	/**
	 * 初始化相关Data
	 */
	private void getSlidePicData() {
		// 一步任务获取图片
		Call<SlideShowListBean> call = MicroClassRequestFactory.getSlidePicApi().getSlidePicList(reqPackDesc);
		call.enqueue(new Callback<SlideShowListBean>() {
			@Override
			public void onResponse(Call<SlideShowListBean> call, Response<SlideShowListBean> response) {
				if (response != null && response.body() != null
						&& response.body().getData() != null && response.body().getData().size() != 0) {

					if (response.body().getResult().equals("1")) {
						slideShowList.clear();
						slideShowList.addAll(response.body().getData());
						findCourseAdapter.setAdData(slideShowList);
						handler.sendEmptyMessage(1);
					}
				} else {
					SlideShowListBean.SlideShowDataBean slideShowDataBean = new SlideShowListBean.SlideShowDataBean();
					slideShowDataBean.setPic(String.valueOf(R.drawable.find_course_banner_default));
					slideShowList.clear();
					slideShowList.add(slideShowDataBean);
					findCourseAdapter.setAdData(slideShowList);
				}
			}

			@Override
			public void onFailure(Call<SlideShowListBean> call, Throwable t) {
				SlideShowListBean.SlideShowDataBean slideShowDataBean = new SlideShowListBean.SlideShowDataBean();
				slideShowDataBean.setPic(String.valueOf(R.drawable.find_course_banner_default));
				slideShowList.clear();
				slideShowList.add(slideShowDataBean);
				findCourseAdapter.setAdData(slideShowList);
			}

		});
	}

	public void getRefreshPackData(final boolean isClean) {
		if (isLast) {
			handler.sendEmptyMessage(0);
			handler.sendEmptyMessage(2);
			return;
		}
		Call<LivePackListBean> call = IyuLiveRequestFactory
				.getLivePackApi().getLivePackList(
						ConstantManager.LIVE_PACK_LIST_PROTOCOL,
						ConstantManager.REQUEST_ZHIBO_SIGN,
						reqPackId,
						ConstantManager.REQUEST_LIVE_PACK_LIST_TYPE,
						pageNum,
						ConstantManager.DEFAULT_PAGE_COUNTS,
						MD5.getMD5ofStr("10102class" + reqPackId));
		call.enqueue(new Callback<LivePackListBean>() {
			@Override
			public void onResponse(Call<LivePackListBean> call, Response<LivePackListBean> response) {
				if (response != null && response.body() != null
						&& response.body().getResult() == 1) {
					iLastPage = response.body().getLastPage();
					if (iLastPage == 0 || pageNum >= iLastPage) {
						isLast = true;
					} else if (iLastPage != pageNum) {
						isLast = false;
					}
					pageNum += 1;
					if (response.body().getData().size() > 0) {
						if (isClean) {
							livePackDataBeanArrayList.clear();
							livePackDataBeanArrayList.addAll(response.body().getData());
							findCourseAdapter.clearList();
							findCourseAdapter.addList(livePackDataBeanArrayList);
							handler.sendEmptyMessage(1);
						} else {
							findCourseAdapter.clearList();
							livePackDataBeanArrayList.addAll(response.body().getData());
							findCourseAdapter.addList(livePackDataBeanArrayList);
						}
						handler.sendEmptyMessage(0);
						handler.sendEmptyMessage(1);
					}
				} else {
					handler.sendEmptyMessage(0);
					handler.sendEmptyMessage(3);
				}
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(1);
			}

			@Override
			public void onFailure(Call<LivePackListBean> call, Throwable t) {
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(3);
			}
		});
	}

	private void loadImageData() {
		localImages.clear();

//		localImages.add(getResources().getIdentifier("findcourse_category_cet4","drawable",getPackageName()));
//		localImages.add(getResources().getIdentifier("findcourse_category_cet6","drawable",getPackageName()));
//		localImages.add(getResources().getIdentifier("findcourse_category_toefl","drawable",getPackageName()));
//		localImages.add(getResources().getIdentifier("findcourse_category_ielts","drawable",getPackageName()));
//		localImages.add(getResources().getIdentifier("findcourse_category_jlpt","drawable",getPackageName()));
//		localImages.add(getResources().getIdentifier("findcourse_category_other","drawable",getPackageName()));

//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_cet4"));
//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_cet6"));
//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_toefl"));
//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_ielts"));
//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_jlpt"));
//		localImages.add(MResource.getIdByName(getApplication(),"drawable","findcourse_category_other"));

		localImages.add(getResId("findcourse_category_cet4", R.drawable.class));
		localImages.add(getResId("findcourse_category_cet6", R.drawable.class));
		localImages.add(getResId("findcourse_category_toefl", R.drawable.class));
		localImages.add(getResId("findcourse_category_ielts", R.drawable.class));
		localImages.add(getResId("findcourse_category_jlpt", R.drawable.class));
		localImages.add(getResId("findcourse_category_other", R.drawable.class));
	}

	/**
	 * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
	 *
	 * @param variableName
	 * @param c
	 * @return
	 */
	public static int getResId(String variableName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(variableName);
			return idField.getInt(idField);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void onClick(View view, Object message) {
		mRecyclerView.smoothScrollToPosition(0);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					mSwipeRefreshLayout.setRefreshing(false);
					break;
				case 1:
					if (findCourseAdapter == null) {
						findCourseAdapter = new FindCourseAdapter(context);
					} else {
						findCourseAdapter.notifyDataSetChanged();
					}
					break;
				case 2:
					T.s(context, R.string.alert_already_last_page);
					break;
				case 3:
					T.s(context, R.string.alert_network_error);
					break;
				case 4:
					T.s(context, R.string.no_internet);
					break;
			}
		}
	};
}
