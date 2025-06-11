

package com.iyuba.camstory.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.ChampionRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestRanking;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.Mychart;
import com.iyuba.camstory.view.ChampionImageView;
import com.iyuba.camstory.view.CustomTimeImageView;
import com.iyuba.camstory.view.TestNumberImageView;
import com.iyuba.configation.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.zenkun.datetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.iyuba.cambridge.utils.Mychart;

public class GradeFragment extends Fragment {
	private static final String TAG = GradeFragment.class.getSimpleName();
	private Context mContext;
	private View root;
	private View rankingView, totalView;
	private TextView startDate, endDate;
	private LinearLayout chartLinearLayout;// 放图表的linerlaout
	private View noLoginView;
	private Button noLoginButton;
	private CustomTimeImageView customTimeImageView;
	private TestNumberImageView testNumberImageView;
	private ChampionImageView dayChampionImageView, weekChampionImageView,
			monthChampionImageView;
	private TextView dayName, weekName, monthName, dayTime, weekTime, monthTime;

	private int Year;
	private int Month;
	private int Day;
	private String currSrartString;
	private String currEndString;
	static final int STARTDATE_DIALOG_ID = 0;
	static final int ENDDATE_DIALOG_ID = 1;

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.grade_statistics, container, false);
		initView(root);
		return root;
	}

	protected void initView(View view) {
		rankingView = view.findViewById(R.id.content_ranking);
		totalView = view.findViewById(R.id.content_total);
		customTimeImageView = view
				.findViewById(R.id.customtime);
		testNumberImageView = view
				.findViewById(R.id.test_result);
		dayChampionImageView = view
				.findViewById(R.id.day_champion);
		weekChampionImageView = view
				.findViewById(R.id.week_champion);
		monthChampionImageView = view
				.findViewById(R.id.month_champion);
		dayName = view.findViewById(R.id.day_champion_name);
		weekName = view.findViewById(R.id.week_champion_name);
		monthName = view.findViewById(R.id.month_champion_name);
		dayTime = view.findViewById(R.id.day_champion_time);
		weekTime = view.findViewById(R.id.week_champion_time);
		monthTime = view.findViewById(R.id.month_champion_time);
		// 以上为排名信息情况的初始化
		startDate = view.findViewById(R.id.starting_date_TextView);
		endDate = view.findViewById(R.id.ending_date_TextView);
		chartLinearLayout = view.findViewById(R.id.chart_container);
		noLoginView = view.findViewById(R.id.relativeLayout_noLogin);
		noLoginButton = view.findViewById(R.id.button_to_login);
		noLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
		});
		startDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，
				// 并会调用 onCreateDialog(int)回调函数来请求一个Dialog
				CreateDialog(STARTDATE_DIALOG_ID);
			}
		});
		endDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，
				// 并会调用 onCreateDialog(int)回调函数来请求一个Dialog
				CreateDialog(ENDDATE_DIALOG_ID);
			}

		});
		// 获得当前的日期：
		final Calendar currentDate = Calendar.getInstance();
		Year = currentDate.get(Calendar.YEAR);
		Month = currentDate.get(Calendar.MONTH);
		Day = currentDate.get(Calendar.DAY_OF_MONTH);
		// 设置文本的内容：
		startDate.setText(new StringBuilder().append(Year).append("年")
				.append(Month + 1).append("月")// 得到的月份+1，因为从0开始
				.append(Day).append("日"));
		currSrartString = Year + "-" + (Month + 1) + "-" + Day;
		// 设置文本的内容：
		endDate.setText(new StringBuilder().append(Year).append("年")
				.append(Month + 1).append("月")// 得到的月份+1，因为从0开始
				.append(Day).append("日"));
		currEndString = Year + "-" + (Month + 1) + "-" + Day;
		getRankingData();
		getChampionData();
	}

	private void getRankingData() {
		RequestRanking request = new RequestRanking(
				AccountManager.getInstance().userId, currSrartString, currEndString,
				new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						RequestRanking tr = (RequestRanking) result;
						if (tr.isRequestSuccessful()) {
							handler.obtainMessage(0, tr).sendToTarget();
						} else {
							handler.obtainMessage(1, tr).sendToTarget();
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	private void getChampionData() {
		ChampionRequest request = new ChampionRequest(new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error instanceof TimeoutError) {
					Toast.makeText(mContext, "获取冠军信息失败", Toast.LENGTH_SHORT).show();
				}
			}
		}, new RequestCallBack() {

			@Override
			public void requestResult(Request result) {
				ChampionRequest response = (ChampionRequest) result;
				if (response.isRequestSuccessful()) {
					handler.obtainMessage(2, response).sendToTarget();
				}
			}
		});
		CrashApplication.getInstance().getQueue().add(request);
	}

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).showImageOnLoading(R.drawable.defaultavatar).build();

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			RequestRanking rs;
			ChampionRequest cRequest;
			switch (msg.what) {
			case 0:
				rs = (RequestRanking) (msg.obj);
				// 以下为显示排名信息
				customTimeImageView.setData(rs.totalTime / 3600,
						(rs.totalTime / 60) % 60, rs.totalTime % 60, rs.timeRanking,
						(rs.totalUser - rs.timeRanking) * 100 / rs.totalUser);
				testNumberImageView.setData(rs.totalTest, (int) (rs.rightRate * 100),
						rs.testRanking);
				// 以下为显示累计信息（做题信息）
				if (rs.dailyTestInfos.size() > 0) {
					int i = rs.dailyTestInfos.size();
					double[] rd = new double[i];
					double[] td = new double[i];
					List<String> time = new ArrayList<String>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					for (int j = 0; j < rs.dailyTestInfos.size(); j++) {
						rd[j] = rs.dailyTestInfos.get(j).rightAnswer;
						td[j] = rs.dailyTestInfos.get(j).testNumber;
						time.add(sdf.format(rs.dailyTestInfos.get(j).everyDay));
					}
					chartLinearLayout.removeAllViews();
					chartLinearLayout
							.addView(new Mychart(rd, td, time).execute(mContext));
				} else {
					chartLinearLayout.removeAllViews();
				}
				break;
			case 1:
				rs = (RequestRanking) (msg.obj);
				// CustomToast.showToast(mContext, "请检查网络设置", 1000);
				break;
			case 2:
				cRequest = (ChampionRequest) (msg.obj);
				ImageLoader.getInstance().displayImage(
						"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid="
								+ cRequest.day1Uid, dayChampionImageView, options);
				ImageLoader.getInstance().displayImage(
						"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid="
								+ cRequest.week1Uid, weekChampionImageView, options);
				ImageLoader.getInstance().displayImage(
						"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=middle&uid="
								+ cRequest.month1Uid, monthChampionImageView, options);
				dayName.setText(cRequest.day1Uname);
				weekName.setText(cRequest.week1Uname);
				monthName.setText(cRequest.month1Uname);
				dayTime.setText(formatTime(cRequest.day1Time));
				weekTime.setText(formatTime(cRequest.week1Time));
				monthTime.setText(formatTime(cRequest.month1Time));
				break;
			default:
				break;
			}
		}

	};

	private String formatTime(int seconds) {
		StringBuilder builder = new StringBuilder();
		builder.append(seconds / 3600);
		builder.append("时");
		builder.append((seconds / 60) % 60);
		builder.append("分");
		builder.append(seconds % 60);
		builder.append("秒");
		return builder.toString();
	}

    public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG); // 统计页面
		if (AccountManager.getInstance().checkUserLogin()) {
			noLoginView.setVisibility(View.GONE);
			rankingView.setVisibility(View.VISIBLE);
			totalView.setVisibility(View.GONE);
		} else {
			noLoginView.setVisibility(View.VISIBLE);
			totalView.setVisibility(View.GONE);
			rankingView.setVisibility(View.GONE);
		}
	}

	private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year,
				int month, int day) {
			Year = year;
			Month = month;
			Day = day;
			// 设置文本的内容：
			startDate.setText(new StringBuilder().append(Year).append("年")
					.append(Month + 1).append("月")// 得到的月份+1，因为从0开始
					.append(Day).append("日"));
			currSrartString = Year + "-" + (Month + 1) + "-" + Day;
			getRankingData();
		}
	};

	private DatePickerDialog.OnDateSetListener eDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year,
				int month, int day) {
			Year = year;
			Month = month;
			Day = day;
			// 设置文本的内容：
			endDate.setText(new StringBuilder().append(Year).append("年")
					.append(Month + 1).append("月")// 得到的月份+1，因为从0开始
					.append(Day).append("日"));
			currEndString = Year + "-" + (Month + 1) + "-" + Day;
			getRankingData();
		}
	};



	protected void CreateDialog(int id) {
		switch (id) {
		case STARTDATE_DIALOG_ID:
			DatePickerDialog date = DatePickerDialog.newInstance(sDateSetListener,
					Year, Month, Day, true);
			//date.show(getSherlockActivity().getSupportFragmentManager(), "GRADE");
			break;
		case ENDDATE_DIALOG_ID:
			DatePickerDialog date1 = DatePickerDialog.newInstance(eDateSetListener,
					Year, Month, Day, true);
			//date1.show(getSherlockActivity().getSupportFragmentManager(), "GRADE");
			break;
		}
	}

	public void showRanking() {
		if (AccountManager.getInstance().checkUserLogin()) {
			noLoginView.setVisibility(View.GONE);
			rankingView.setVisibility(View.VISIBLE);
			totalView.setVisibility(View.GONE);
		}
	}

	public void showCounting() {
		if (AccountManager.getInstance().checkUserLogin()) {
			noLoginView.setVisibility(View.GONE);
			totalView.setVisibility(View.VISIBLE);
			rankingView.setVisibility(View.GONE);
		}
	}

}


