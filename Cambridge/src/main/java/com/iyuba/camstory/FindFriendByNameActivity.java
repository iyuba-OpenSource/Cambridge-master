/**
 * 
 */
package com.iyuba.camstory;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.iyuba.camstory.adpater.SearchResultAdapter;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestSearchList;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.sqlite.mode.SearchItem;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.umeng.analytics.MobclickAgent;

/**
 * @author yao
 * 
 */
public class FindFriendByNameActivity extends AppCompatActivity implements
		OnScrollListener {
	private WindowManager mWindowManager;
	private NightModeManager nightModeManager;
	
	private Button cancel/* ,inAll,inFans,inFollows,inMutual */;
	private EditText editText;
	private String currPages = "1";
	private int curPage = 1;
	private SearchResultAdapter adapter;
	private ArrayList<SearchItem> list = new ArrayList<SearchItem>();
	private String search;
	private CustomDialog waitingDialog;
	private String id;
	private String nameString;
	private ListView listView;
	private TextView shake_text;
	private Context mContext;
	private int searchRange;// 1 粉丝 2 我关注的 3 互相关注 0 全站用户
	private String lastSearchName;
	private Spinner searchWhereSpinner;
	// 震动
	//private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		mContext = this;
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		nightModeManager = new NightModeManager(mWindowManager, mContext);
		//initActionbar();
		waitingDialog = waitingDialog();
		initWidget();
		listView.setOnScrollListener(this);
		setListener();
	}

	private void initWidget() {
		cancel = findViewById(R.id.findCancel);
		editText = findViewById(R.id.edittext);
		listView = findViewById(R.id.findresult);
		shake_text = findViewById(R.id.shake_it);
		searchWhereSpinner = findViewById(R.id.searchwhere);
		searchWhereSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						searchRange = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO 自动生成的方法存根
					}
				});
	}

	/*private void initActionbar() {
		SpannableString ss = new SpannableString("搜索好友");
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}*/

	private void setListener() {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				String content = editText.getText().toString();
				System.out.println("content====" + content);
				if (EditorInfo.IME_ACTION_SEND == actionId) {
					System.out.println("dahsidshdaahskadasjdsjdjfksjfk");
				}
				if (EditorInfo.IME_ACTION_DONE == actionId) {
					System.out.println("IME_ACTION_DONE");
				}
				if (EditorInfo.IME_ACTION_GO == actionId) {
					System.out.println("IME_ACTION_GO");
				}

				return true;
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search = editText.getText().toString();
				if (search != null) {
					curPage = 1;
					currPages = curPage + "";
					handler.sendEmptyMessage(2);
					handler.sendEmptyMessage(1);
				}
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				id = list.get(arg2).uid;
				nameString = list.get(arg2).username;
				handler.sendEmptyMessage(7);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
		nightModeManager.checkMode();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				curPage = 1;
				currPages = String.valueOf(curPage);
				// adapter.clearList();
				list.clear();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				break;
			case 1:
				// 搜索
				RequestSearchList request = new RequestSearchList(
						AccountManager.getInstance().userId, search, searchRange
								+ "", currPages, 
						new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO 自动生成的方法存根
								handler.sendEmptyMessage(3);
								CustomToast.showToast(
										CrashApplication.getInstance(),
										"查找失败，请稍后重试", 1000);
							}
						}, 
						new RequestCallBack() {
							@Override
							public void requestResult(Request result) {
								// TODO 自动生成的方法存根
								list.clear();
								lastSearchName = search;
								RequestSearchList res = (RequestSearchList) result;
								if (res.isRequestSuccessful()) {
									list.addAll(res.list);
									if (adapter == null) {
										adapter = new SearchResultAdapter(
												mContext, list);
										listView.setAdapter(adapter);
									} else {
										adapter.notifyDataSetChanged();
									}
									sendEmptyMessage(5);
								} else {
									CustomToast.showToast(mContext,
											R.string.social_no_similar_user,
											1000);
								}
								curPage += 1;
								currPages = curPage + "";
								handler.sendEmptyMessage(3);
							}
						});
				CrashApplication.getInstance().getQueue().add(request);
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			case 4:
				handler.sendEmptyMessage(3);
				break;
			case 5:
				listView.setVisibility(View.VISIBLE);
				shake_text.setVisibility(View.GONE);
				break;
			case 6:
				listView.setVisibility(View.GONE);
				shake_text.setVisibility(View.VISIBLE);
				break;
			case 7:
				handler.sendEmptyMessage(3);
				Intent intent = new Intent();
				intent.putExtra("friendid", id);
				intent.putExtra("currentname", nameString);
				intent.setClass(mContext, MessageLetterContentActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

	};

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				handler.sendEmptyMessage(1);
			}
			break;
		default:
			break;
		}
	}
	
	public void finish() {
		super.finish();
		nightModeManager.remove();
	}
}
