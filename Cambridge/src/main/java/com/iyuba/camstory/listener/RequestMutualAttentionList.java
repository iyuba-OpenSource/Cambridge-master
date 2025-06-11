package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.MutualAttention;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 获取相互关注者列表
 */
public class RequestMutualAttentionList extends BaseJsonObjectRequest {
	private static final String protocolCode = "51003";
	private String md5Status = "1"; // 0=未加密,1=加密

	public String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<MutualAttention> list;
	public int num;

	public RequestMutualAttentionList(int uid, String page, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode + "&uid=" + uid
				+ "&pageNumber=" + page + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				list = new ArrayList<MutualAttention>();
				try {
					result = jsonObjectRootRoot.getString("result");
					message = jsonObjectRootRoot.getString("message");
					if (result.equals("570")) {
						num = jsonObjectRootRoot.getInt("num");
						data = jsonObjectRootRoot.getJSONArray("data");
						if (data != null && data.length() != 0) {
							for (int i = 0; i < data.length(); i++) {
								MutualAttention item = new MutualAttention();
								JSONObject jsonObject = ((JSONObject) data.opt(i));
								item.bkname = jsonObject.getString("bkname");
								item.dateline = jsonObject.getString("dateline");
								item.followuid = jsonObject.getString("followuid");
								item.fusername = jsonObject.getString("fusername");
								item.status = jsonObject.getString("status");
								list.add(item);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				rc.requestResult(RequestMutualAttentionList.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return ("570".equals(result));
	}

}
