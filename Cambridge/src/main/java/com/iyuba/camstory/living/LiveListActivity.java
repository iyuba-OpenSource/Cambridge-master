package com.iyuba.camstory.living;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.listener.IOnClickListener;
import com.iyuba.camstory.listener.IOnDoubleClick;
import com.iyuba.camstory.lycam.adapter.CourseListAdapter;
import com.iyuba.camstory.lycam.manager.ConstantManager;
import com.iyuba.camstory.lycam.network.IyuLiveRequestFactory;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.camstory.lycam.util.T;
import com.iyuba.camstory.lycam.widget.DividerItemDecoration;
import com.iyuba.camstory.lycam.widget.recycleview.CustomSwipeToRefresh;
import com.iyuba.camstory.lycam.widget.swiperefreshlayout.MySwipeRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：renzhy on 16/7/20 14:22
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveListActivity extends AppCompatActivity implements MySwipeRefreshLayout.OnRefreshListener, View.OnClickListener, IOnClickListener {

	private static final int PRELOAD_SIZE = 6;

	private Context context;
	private Toolbar toolbar;
	private TextView mTitleView;
	private TextView mTextViewBack;
	private RecyclerView mRecyclerView;
	private CustomSwipeToRefresh mSwipeRefreshLayout;
	private ImageView ivScrollTop;

	private String mCourseTypeTitle;
	private String reqPackId = "-1";
	private String reqPackDesc = "class.new";
	private int iLastPage = 0;
	private int pageNum = 1;
	private boolean isLast = false;
	private boolean mIsFirstTimeTouchBottom = true;
	private ArrayList<LivePackListBean.LivePackDataBean> livePackDataBeanArrayList = new ArrayList<>();
	private CourseListAdapter courseListAdapter;

	View mViewEmptyPage;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);
		context = this;
		initData();
		initWidget();
		new GetHeaderDataTask().execute();
	}

	public void initData() {
		reqPackId = getIntent().getStringExtra("reqPackId");
		mCourseTypeTitle = getIntent().getStringExtra("title");
	}

	public void initWidget() {
		mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_find_course);
		mRecyclerView = findViewById(R.id.recyclerview_find_course);
		ivScrollTop = findViewById(R.id.iv_scroll_top);
		mViewEmptyPage = findViewById(R.id.rl_empty_page);

		toolbar = findViewById(R.id.toolbar_course_list);
		mTitleView = toolbar.findViewById(R.id.center_title);
		mTextViewBack = toolbar.findViewById(R.id.nav_left_text);

		mTitleView.setText(mCourseTypeTitle);
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
		courseListAdapter = new CourseListAdapter(context);
		courseListAdapter.clearList();
		mRecyclerView.setAdapter(courseListAdapter);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				context, R.drawable.recycler_rectangle, DividerItemDecoration.VERTICAL_LIST));
		ivScrollTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mRecyclerView.smoothScrollToPosition(0);
			}
		});
		mViewEmptyPage.setOnClickListener(this);
		courseListAdapter.setOnItemClickListener(new CourseListAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
//				startActivity(LiveContentActivity.getIntent2Me(
//						context,
//						livePackDataBeanArrayList.get(position)));
			}

			@Override
			public void onLongClick(View view, int position) {

			}
		});
	}

	public static Intent getIntent2Me(Context context) {
		Intent intent = new Intent(context, LiveListActivity.class);
		return intent;
	}

	public static Intent getIntent2Me(Context context, String reqpackid, String typetitle) {
		Intent intent = new Intent(context, LiveListActivity.class);
		intent.putExtra("reqPackId", reqpackid);
		intent.putExtra("title", typetitle);
		return intent;
	}

	@Override
	public void onRefresh(int index) {
		pageNum = 1;
		isLast = false;
		getRefreshPackData(true);
	}

	@Override
	public void onLoad(int index) {
		getRefreshPackData(false);
	}

	private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			pageNum = 1;
			isLast = false;
			getRefreshPackData(true);
			return null;
		}
	}

	private class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getRefreshPackData(false);
			return null;
		}
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
						reqPackId, "1", pageNum, 20, MD5.getMD5ofStr("10102class" + reqPackId));
		call.enqueue(new Callback<LivePackListBean>() {
			@Override
			public void onResponse(Call<LivePackListBean> call, Response<LivePackListBean> response) {
				if (response != null && response.body() != null
						&& response.body().getResult() == 1) {
					iLastPage = response.body().getLastPage();
					if (iLastPage != pageNum) {
						isLast = false;
					} else if (iLastPage == 0 || pageNum == iLastPage) {
						isLast = true;
					}
					pageNum += 1;
					if (response.body().getData().size() > 0) {
						if (isClean) {
							livePackDataBeanArrayList.clear();
							livePackDataBeanArrayList.addAll(response.body().getData());
							courseListAdapter.clearList();
							courseListAdapter.addList(response.body().getData());
							handler.sendEmptyMessage(1);
						} else {
							courseListAdapter.clearList();
							livePackDataBeanArrayList.addAll(response.body().getData());
							courseListAdapter.addList(livePackDataBeanArrayList);
						}
						handler.sendEmptyMessage(0);
						handler.sendEmptyMessage(1);

						mSwipeRefreshLayout.setVisibility(View.VISIBLE);
						mViewEmptyPage.setVisibility(View.GONE);
						ivScrollTop.setVisibility(View.VISIBLE);
					} else if (response.body().getData().size() == 0) {
						mSwipeRefreshLayout.setVisibility(View.GONE);
						mViewEmptyPage.setVisibility(View.VISIBLE);
						ivScrollTop.setVisibility(View.GONE);
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(0);
						handler.sendEmptyMessage(3);
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

	private int getScrolledDistance(RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
		View firstVisibleItem = recyclerView.getChildAt(0);
		int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
		int itemHeight = firstVisibleItem.getHeight();
		int firstItemBottom = linearLayoutManager.getDecoratedBottom(firstVisibleItem);
		return (firstItemPosition + 1) * itemHeight - firstItemBottom;
	}

	@Override
	public void onClick(View view, Object message) {
		mRecyclerView.smoothScrollToPosition(0);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rl_empty_page) {
			mSwipeRefreshLayout.setVisibility(View.VISIBLE);
			mViewEmptyPage.setVisibility(View.GONE);
			mSwipeRefreshLayout.setRefreshing(true);
			handler.sendEmptyMessageDelayed(4, 2000);
		}
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
					if (courseListAdapter == null) {
						courseListAdapter = new CourseListAdapter(context);
					} else {
						courseListAdapter.notifyDataSetChanged();
					}
					break;
				case 2:
					T.s(context, R.string.alert_already_last_page);
					break;
				case 3:
					T.s(context, R.string.alert_network_error);
					break;
				case 4:
					new GetHeaderDataTask().execute();
					break;
			}
		}
	};
}
