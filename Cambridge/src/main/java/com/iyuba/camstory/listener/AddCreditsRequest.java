package com.iyuba.camstory.listener;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.Base64Coder;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TimeUtil;
import com.iyuba.voa.activity.setting.Constant;


public class AddCreditsRequest extends BaseJsonObjectRequest {
	private static final String TAG = AddCreditsRequest.class.getSimpleName();
	public String result;
	public int addCredit;
	public int totalCredit;
	public String message = "";

	public AddCreditsRequest(final int uid, int voaid, int srid, final RequestCallBack rc) {
		super(Constant.getAddintegralurl() + "srid=" + srid
				+ "&uid=" + uid + "&appid=" + Constant.getAppid()
				+ "&idindex="	+ voaid	+ "&mobile=1"	+ "&flag="
				+ Base64Coder.encode(MD5.getMD5ofStr(uid + TimeUtil.getNowInString())));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonBody) {
				Log.e(TAG, jsonBody.toString());
				try {
					result = jsonBody.getString("result");
					if(isRequestSuccessful()){
						addCredit = Integer.parseInt(jsonBody.getString("addcredit"));
						totalCredit = Integer.parseInt(jsonBody.getString("totalcredit"));
					} else {
						message = jsonBody.getString("message");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				rc.requestResult(AddCreditsRequest.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return ("200".equals(result));
	}
	
	public boolean isShareFirstlySuccess(){
		return isRequestSuccessful();
	}
	
	public boolean isShareRepeatlySuccess(){
		return ("201".equals(result));
	}

}
