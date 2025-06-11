package com.iyuba.camstory.sqlite.mode;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.listener.BaseJsonObjectRequest;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 * 
 */
public class RequestSameAppFriendsList extends BaseJsonObjectRequest {
	private static final String protocolCode = "90003";

	private String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<SameAppFriendInfo> list;
	public int friendCounts;

	public RequestSameAppFriendsList(int uid, int pageNumber, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode + "&uid=" + uid
				+ "&pagesize=" + "20" + "&pagenum=" + pageNumber);

		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				list = new ArrayList<SameAppFriendInfo>();
				try {
					result = jsonObjectRootRoot.getString("result");
					friendCounts = jsonObjectRootRoot.getInt("friendCounts");
					if (result.equals("261")) {
						data = jsonObjectRootRoot.getJSONArray("data");
						if (data != null && data.length() != 0) {
							for (int i = 0; i < data.length(); i++) {
								SameAppFriendInfo item = new SameAppFriendInfo();
								JSONObject jsonObject = ((JSONObject) data.opt(i));
								item.uid = jsonObject.getString("uid");
								item.username = jsonObject.getString("username");
								item.appid = jsonObject.getString("appid");
								item.appname = jsonObject.getString("appname");
								list.add(item);
							}
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				rc.requestResult(RequestSameAppFriendsList.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
        return "261".equals(result);
    }

}
