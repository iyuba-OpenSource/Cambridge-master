package com.iyuba.camstory.listener;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestSubmitMessageCode extends BaseJsonObjectRequest {
	public String result;
	public int res_code;
	public String userphone;
	public String identifier;

	public RequestSubmitMessageCode(String phoneNumber, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/sendMessage3.jsp?format=json"
				+ "&userphone=" + phoneNumber);
		setResListener(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObjectRoot) {
				try {
					result = jsonObjectRoot.getString("result");
					Log.v("result","result="+result);
					if (result.equals("1")) {
						res_code = jsonObjectRoot.getInt("res_code");
						userphone = jsonObjectRoot.getString("userphone");
						identifier = jsonObjectRoot.getString("identifier");

						Log.e("res_code", res_code + "");
						Log.e("userphone", userphone + "");
						Log.e("identifier", identifier + "");
					} else {
						res_code = -1;
						userphone = "";
						identifier = "";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				rc.requestResult(RequestSubmitMessageCode.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return "1".equals(result);
	}
	
	public boolean isPhonenumAlreadyRegistered(){
		return "-1".equals(result);
	}

}
