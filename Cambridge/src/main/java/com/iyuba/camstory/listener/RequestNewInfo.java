package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestNewInfo extends BaseJsonObjectRequest{
	private static final String protocolCode="62001";

	//{"system":0,"letter":2,"notice":1,"follow":0}
	public int system=0;
	public int letter=0;
	public int notice=0;
	public int follow=0;
	public RequestNewInfo(int id,final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol="+ protocolCode
				+ "&uid=" + id
				+ "&sign="+ MD5.getMD5ofStr(protocolCode+id+"iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				// TODO 自动生成的方法存根
				try {
					system=jsonObjectRootRoot.getInt("system");
					letter=jsonObjectRootRoot.getInt("letter");
					notice=jsonObjectRootRoot.getInt("notice");
					follow=jsonObjectRootRoot.getInt("follow");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rc.requestResult(RequestNewInfo.this);
			}
		});
	}
	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
		return true;
	}

}

