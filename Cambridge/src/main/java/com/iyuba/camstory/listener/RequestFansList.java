package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.sqlite.mode.Fans;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao 请求粉丝列表 51002
 */
public class RequestFansList extends BaseJsonObjectRequest {
	private static final String protocolCode = "51002";
	private String md5Status = "1"; // 0=未加密,1=加密
	private static final int pageCounts = 30;

	private String result = "";// 返回代码
	public String message;// 返回信息
	public Fans fan = new Fans();
	public JSONArray data;
	public ArrayList<FansBean.DataBean> fansList;
	public int num;

	public RequestFansList(int uid, int pageNumber, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode + "&uid=" + uid
				+ "&pageCounts=" + pageCounts + "&pageNumber=" + pageNumber + "&sign="
				+ MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
		setResListener(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonBody) {
				fansList = new ArrayList<FansBean.DataBean>();
				try {
					result = jsonBody.getString("result");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					message = jsonBody.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (result.equals("560")) {
					try {
						num = jsonBody.getInt("num");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					try {
						data = jsonBody.getJSONArray("data");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (data != null && data.length() != 0) {
						for (int i = 0; i < data.length(); i++) {
							try {
								FansBean.DataBean fans = new FansBean.DataBean();
								JSONObject jsonObject = ((JSONObject) data.opt(i));
//								fans.mutual = jsonObject.getString("mutual");
								fans.setUid(jsonObject.getString("uid"));
								fans.setDateline(jsonObject.getString("dateline"));
								fans.setUsername(jsonObject.getString("username"));
								fans.setDoing(jsonObject.getString("doing"));
								fansList.add(fans);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
				rc.requestResult(RequestFansList.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		return ("560".equals(result));
	}

}
