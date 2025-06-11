package com.iyuba.camstory.listener;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.voa.activity.setting.Constant;


public class RequestPhoneNumRegister extends BaseJsonObjectRequest {
	private static final String protocolCode = "11002";
	public String resultCode;
	public String message;

	public RequestPhoneNumRegister(String userName, String password,
			String mobile, final RequestCallBack rc) {
		super(Constant.getPhoneRegisturl()
				+ "&format=json"
				+ "&username="
				+ URLEncoder.encode(userName)
				+ "&password="
				+ MD5.getMD5ofStr(password)
				+ "&sign="
				+ MD5.getMD5ofStr(protocolCode + userName + MD5.getMD5ofStr(password)
						+ "iyubaV2") + "&mobile=" + mobile);

		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRoot) {
				try {
					resultCode = jsonObjectRoot.getString("result");
					message = jsonObjectRoot.getString("message");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				rc.requestResult(RequestPhoneNumRegister.this);
			}
		});

	}

	@Override
	public boolean isRequestSuccessful() {
		return "111".equals(resultCode);
	}

}
