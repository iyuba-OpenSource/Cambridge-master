/**
 * 
 */
package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * 搜索好友
 */
public class RequestEditUserInfo extends BaseJsonObjectRequest{
	private static final String protocolCode="20003";

	public String message;// 返回信息
	public String result;// 返回代码
	
	public RequestEditUserInfo(int uid, String key, String value, final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?"
				+ "platform=" + "android"
				+ "&format=" + "json" 
				+ "&protocol=" + protocolCode
				+ "&id=" + uid
				+ "&sign="+ MD5.getMD5ofStr(protocolCode + uid + "iyubaV2")
				+ "&key=" + key
				+ "&value=" + TextAttr.encode(TextAttr.encode(value)));
		setResListener(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonBody) {
				try {
					result = jsonBody.getString("result");
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				try {
					message = jsonBody.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				rc.requestResult(RequestEditUserInfo.this);
			}
		});
	}
	
	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
        return "221".equals(result);
    }

}

