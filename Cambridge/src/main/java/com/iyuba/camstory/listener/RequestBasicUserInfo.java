package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.UserInfo;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestBasicUserInfo extends BaseJsonObjectRequest {
	private static final String TAG = RequestBasicUserInfo.class.getSimpleName();

	private String message;// 返回信息
	private String result;// 返回代码
	public UserInfo userInfo = new UserInfo();

	public RequestBasicUserInfo(int uid, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=20001" + "&id=" + uid
				+ "&sign=" + MD5.getMD5ofStr("20001" + uid + "iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonBody) {
				try {
					result = jsonBody.getString("result");
					message = jsonBody.getString("message");
					if (isRequestSuccessful()) {
						userInfo.relation = jsonBody.getInt("relation");
						userInfo.username = jsonBody.getString("username");
						userInfo.doings = jsonBody.getInt("doings");
						userInfo.views = jsonBody.getString("views");
						userInfo.gender = jsonBody.getString("gender");
						if (jsonBody.has("text")) {
							userInfo.text = jsonBody.getString("text");
						}
						userInfo.iyubi = jsonBody.getInt("amount");
						userInfo.expireTime = jsonBody.getString("expireTime");
						userInfo.vipStatus = jsonBody.getString("vipStatus");
						userInfo.distance = jsonBody.getString("distance");
						userInfo.follower = jsonBody.getInt("follower");
						userInfo.following = jsonBody.getInt("following");
						userInfo.credits = jsonBody.getInt("credits");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				rc.requestResult(RequestBasicUserInfo.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return "201".equals(result);
	}
}
