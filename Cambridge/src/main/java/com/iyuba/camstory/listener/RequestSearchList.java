/**
 * 
 */
package com.iyuba.camstory.listener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.SearchItem;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author 搜索好友
 */
public class RequestSearchList extends BaseJsonObjectRequest {
	private static final String protocolCode = "52001";

	private String result;// 返回代码
	public String message;// 返回信息
	public JSONArray data;
	public ArrayList<SearchItem> list;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	public int total;

	public RequestSearchList(int uid, String search, String type,
			String pageNumber, ErrorListener errorListener,
			final RequestCallBack rc) {
		// TODO Auto-generated constructor stub
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode
				+ "&uid=" + uid + "&search=" + URLEncoder.encode(search)
				+ "&pageNumber=" + pageNumber + "&pageCounts=20" + "&type="
				+ type + "&sign="
				+ MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"), errorListener);
		setResListener(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				// TODO 自动生成的方法存根
				list = new ArrayList<SearchItem>();
				try {
					result = jsonObjectRootRoot.getString("result");
					if (result.equals("591")) {
						total = jsonObjectRootRoot.getInt("total");
						prevPage = jsonObjectRootRoot.getInt("prevPage");
						nextPage = jsonObjectRootRoot.getInt("nextPage");
						lastPage = jsonObjectRootRoot.getInt("lastPage");
						data = jsonObjectRootRoot.getJSONArray("data");
						if (data != null && data.length() != 0) {
							for (int i = 0; i < data.length(); i++) {
								SearchItem item = new SearchItem();
								JSONObject jsonObject = ((JSONObject) data
										.opt(i));
								item.uid = jsonObject.getString("uid");
								item.username = jsonObject
										.getString("username");
								item.similar = jsonObject.getString("similar");
								list.add(item);
							}
						}
					}
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				rc.requestResult(RequestSearchList.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
        return "591".equals(result);
    }

}
