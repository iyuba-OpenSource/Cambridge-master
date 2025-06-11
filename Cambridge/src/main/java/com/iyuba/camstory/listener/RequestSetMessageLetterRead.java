/**
 * 
 */
package com.iyuba.camstory.listener;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.sqlite.mode.MessageLetterContent;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 *
 */
public class RequestSetMessageLetterRead extends BaseJsonObjectRequest{
	private static final String protocolCode="60003";
	

	private String result;// 返回代码
	public String message;// 返回信息
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<MessageLetterContent> list;
	public int num;
	public String plid;
	public RequestSetMessageLetterRead(int uid,String plid,final RequestCallBack rc) {
		// TODO Auto-generated constructor stub
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol="+ protocolCode
				+ "&uid=" + uid
				+ "&plid=" + plid 
				//+ "&pageNumber=" + 50
				+ "&sign="+ MD5.getMD5ofStr(protocolCode+uid+plid+"iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				// TODO 自动生成的方法存根
				list = new ArrayList<MessageLetterContent>();
				try {
					result = jsonObjectRootRoot.getString("result");
					message = jsonObjectRootRoot.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				rc.requestResult(RequestSetMessageLetterRead.this);
			}
		});
	}
	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
		if ("621".equals(result)) {
			Log.d("tag", "设置成已读");
			return true;
		}
		return false;
	}

}


