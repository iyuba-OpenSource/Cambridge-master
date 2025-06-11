/**
 * 
 */
package com.iyuba.camstory.listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;

/**
 * @author
 * 搜索好友
 */
public class RequestLocation extends BaseJsonObjectRequest{
	public String subLocality = "";
	public String locality = "";
	public String province = "";
	private String type = "";
	
	public RequestLocation(String latitude, String longitude, final RequestCallBack rc) {
		super("http://maps.google.cn/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=true&language=zh-CN");
		setResListener(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonBody) {
				try {
					JSONArray JsonArrayData = jsonBody.getJSONArray("results");
					if (JsonArrayData != null) {
						JSONObject jsonLocationData = JsonArrayData.getJSONObject(0);
						JSONArray JsonArrayDataInner = jsonLocationData
								.getJSONArray("address_components");
						if (JsonArrayDataInner != null) {
							int size = JsonArrayDataInner.length();
							JSONObject jsonPositionData;
							JSONArray JsonArrayDataType;
							for (int i = 0; i < size; i++) {
								jsonPositionData = JsonArrayDataInner.getJSONObject(i);
								JsonArrayDataType = jsonPositionData
										.getJSONArray("types");
								type = JsonArrayDataType.get(0).toString();
								if (type.equals("administrative_area_level_1")) {
									province = jsonPositionData.getString("short_name");
								}
								if (type.equals("locality")) {
									locality = jsonPositionData.getString("short_name");
								}
								if (type.equals("sublocality")) {
									subLocality = jsonPositionData
											.getString("short_name");
								}
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				rc.requestResult(RequestLocation.this);
			}
		});
	}
	
	@Override
	public boolean isRequestSuccessful() {
		return true;
	}

}

