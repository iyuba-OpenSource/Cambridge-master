package com.iyuba.camstory.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Log;
import com.android.volley.Response.Listener;
import com.iyuba.voa.activity.setting.Constant;


/**
 * 反馈请求
 * 
 * @author chentong
 * 
 */
public class FeedBackJsonRequest extends BaseJsonObjectRequest {
	private static final String TAG = FeedBackJsonRequest.class.getSimpleName();

	public String status;
	public String msg;

	public FeedBackJsonRequest(String content, String email, int uid,
			final RequestCallBack rc) {
		super(Constant.getFeedbackurl() + uid + "&content="
				+ makeString(content) + "&email=" + email + "&format=json");
		String str = Constant.getFeedbackurl() + uid + "&content="
				+ makeString(content) + "&email=" + email + "&format=json";
		Log.e(TAG, str);
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject arg0) {
				if (rc != null) {
					rc.requestResult(FeedBackJsonRequest.this);
				}
			}
		});
		if (rc != null) {
			rc.requestResult(FeedBackJsonRequest.this);
		}
	}

	private static String makeString(String content) {
		try {
			return URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public boolean isRequestSuccessful() {
		return true;
	}

}
