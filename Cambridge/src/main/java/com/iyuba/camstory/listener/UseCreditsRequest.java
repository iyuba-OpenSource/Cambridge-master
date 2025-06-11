package com.iyuba.camstory.listener;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.Base64Coder;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.camstory.utils.TimeUtil;
import com.iyuba.voa.activity.setting.Constant;


public class UseCreditsRequest extends BaseJsonObjectRequest {
	private static final String TAG = UseCreditsRequest.class.getSimpleName();
	
	public String result;
	public int addcredit;
	public int totalcredit;
	public String message;
	
	private static String updateIntegralURL = "http://api."+Constant.IYBHttpHead+"/credits/updateScore.jsp?srid=40";

	public UseCreditsRequest(int uid, int voaid, final RequestCallBack rc) {
		super(updateIntegralURL + "uid=" + uid + "&appid=" + Constant.getAppid()
				+ "&idindex="	+ voaid + "&mobile=1&flag=" + 
				Base64Coder.encode(MD5.getMD5ofStr(uid + TimeUtil.getNowInString())));
		Log.e(TAG, updateIntegralURL + "uid=" + uid + "&appid=" + Constant.getAppid()
				+ "&idindex="	+ voaid + "&mobile=1&flag=" + 
				Base64Coder.encode(MD5.getMD5ofStr(uid + TimeUtil.getNowInString())));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonRoot) {
				try {
					result = jsonRoot.getString("result");
					if(isRequestSuccessful()){
						addcredit = Integer.parseInt(jsonRoot.getString("addcredit"));
						totalcredit = Integer.parseInt(jsonRoot.getString("totalcredit"));
					} else {
						message = TextAttr.decode(jsonRoot.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				rc.requestResult(UseCreditsRequest.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return result.equals("200");
	}
}
