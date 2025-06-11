package com.iyuba.camstory.listener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class ChampionRequest extends BaseJsonObjectRequest {
	private int result = 0;
	public int day1Uid, week1Uid, month1Uid;
	public String day1Uname, week1Uname, month1Uname;
	public int day1Time, week1Time, month1Time;

	public ChampionRequest(ErrorListener errorListener, final RequestCallBack rc) {
		super("http://daxue."+Constant.IYBHttpHead+"/ecollege/getNo1ByStudy.jsp", errorListener);
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.has("result")
							&& (result = response.getInt("result")) == 1) {
						day1Uid = response.getInt("dayNo1Uid");
						week1Uid = response.getInt("weekNo1Uid");
						month1Uid = response.getInt("monthNo1Uid");

						day1Uname = response.getString("dayNo1Username");
						week1Uname = response.getString("weekNo1UserName");
						month1Uname = response.getString("monthNo1UserName");

						day1Time = response.getInt("dayNo1Usetime");
						week1Time = response.getInt("weekNo1Usetime");
						month1Time = response.getInt("monthNo1Usetime");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (rc != null) {
					rc.requestResult(ChampionRequest.this);
				}
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
        return result == 1;
    }

}
