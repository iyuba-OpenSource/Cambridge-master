/**
 * 
 */
package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * 取消关注
 * 50002
 */
public class RequestCancelAttention extends BaseJsonObjectRequest{
	private static final String protocolCode="50002";
	private String result;// 返回代码
	public String message;// 返回信息
	public RequestCancelAttention(int uid,String followid,final RequestCallBack rc) {
		// TODO Auto-generated constructor stub
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol="+ protocolCode
				+ "&uid=" + uid
				+ "&followid=" + followid 
				+ "&sign="+ MD5.getMD5ofStr(protocolCode+uid+followid+"iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				// TODO 自动生成的方法存根
				try {
					result = jsonObjectRootRoot.getString("result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rc.requestResult(RequestCancelAttention.this);
			}
		});
	}
	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
        return "510".equals(result);
    }

}

