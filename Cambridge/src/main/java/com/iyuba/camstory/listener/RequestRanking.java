package com.iyuba.camstory.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.DailyTestInfo;
import com.iyuba.voa.activity.setting.Constant;


public class RequestRanking extends BaseJsonObjectRequest {
	private static final String TAG = RequestRanking.class.getSimpleName();

	private String result;
	public int totalTime;
	public int timeRanking;
	public int totalTest;
	public int testRanking;
	public double rightRate;
	public int totalUser;
	public int rateRanking;
	public static String url;
	public List<DailyTestInfo> dailyTestInfos = new ArrayList<DailyTestInfo>();

	public RequestRanking(int uid, String begindate, String enddate,
			final RequestCallBack rc) {
		super(url = "http://daxue."+Constant.IYBHttpHead+"/ecollege/getPaiming.jsp?"
				+ "format=json&uid=" + uid + "&begindate=" + begindate + "&enddate="
				+ enddate + "&appName=" + Constant.getAppfile());
		Log.e(TAG, url);
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				try {
					result = jsonObjectRootRoot.getString("result");
					if (result.equals("1")) {
						totalTime = jsonObjectRootRoot.getInt("totalTime");
						timeRanking = jsonObjectRootRoot.getInt("positionByTime");
						if (jsonObjectRootRoot.has("totalTest")) {
							totalTest = jsonObjectRootRoot.getInt("totalTest");
							testRanking = jsonObjectRootRoot.getInt("positionByTest");
						}
						if (jsonObjectRootRoot.has("totalRate")) {
							rightRate = jsonObjectRootRoot.getDouble("totalRate");
							if (rightRate == -1) {
								rightRate = 0;
							}
							rateRanking = jsonObjectRootRoot.getInt("positionByRate");
						}
						if (jsonObjectRootRoot.has("totalUser")) {
							totalUser = jsonObjectRootRoot.getInt("totalUser");
						}
						if (jsonObjectRootRoot.has("everyDayInfo")) {
							JSONArray everyDayJsonArray = jsonObjectRootRoot
									.getJSONArray("everyDayInfo");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							for (int i = 0; i < everyDayJsonArray.length(); i++) {
								DailyTestInfo dailyTestInfo = new DailyTestInfo();
								dailyTestInfo.everyDay = sdf
										.parse(((JSONObject) everyDayJsonArray.get(i))
												.getString("everyday"));
								dailyTestInfo.testNumber = ((JSONObject) everyDayJsonArray
										.get(i)).getInt("testNumber");
								dailyTestInfo.rightAnswer = ((JSONObject) everyDayJsonArray
										.get(i)).getInt("rightanswer");
								dailyTestInfos.add(dailyTestInfo);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				rc.requestResult(RequestRanking.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
        return "1".equals(result);
    }

}
