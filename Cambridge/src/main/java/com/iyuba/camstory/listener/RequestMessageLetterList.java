/**
 * 
 */
package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 * 私信列表
 */
public class RequestMessageLetterList extends BaseJsonObjectRequest{
	private static final String protocolCode="60001";
	

	public String result;// 返回代码
	public String message;// 返回信息
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<MessageLetter> list;
	public int num;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	public RequestMessageLetterList(int uid,final RequestCallBack rc) {
		//super(protocolCode);
		// TODO Auto-generated constructor stub
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol="+ protocolCode
				+ "&uid=" + uid
				+ "&asc=" + 0
				+ "&sign="+ MD5.getMD5ofStr(protocolCode+uid+"iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRootRoot) {
				// TODO 自动生成的方法存根
				list = new ArrayList<MessageLetter>();
				try {
					result = jsonObjectRootRoot.getString("result");
					firstPage = jsonObjectRootRoot.getInt("firstPage");
					prevPage = jsonObjectRootRoot.getInt("prevPage");
					nextPage = jsonObjectRootRoot.getInt("nextPage");
					lastPage = jsonObjectRootRoot.getInt("lastPage");
				if (result.equals("601")) {
						data = jsonObjectRootRoot.getJSONArray("data");
					if (data != null && data.length() != 0) {
						for (int i = 0; i < data.length(); i++) {
								MessageLetter letter = new MessageLetter();
								JSONObject jsonObject = ((JSONObject) data.opt(i));
								letter.friendid = jsonObject.getString("friendid");
								letter.pmnum = jsonObject.getInt("pmnum");
								letter.lastmessage = TextAttr.decode(jsonObject.getString("lastmessage"));
								letter.name = jsonObject.getString("name");
								letter.dateline = jsonObject.getString("dateline");
								letter.plid = jsonObject.getString("plid");
								letter.isnew = jsonObject.getString("isnew");
								list.add(letter);
						}
					}
				}
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				rc.requestResult(RequestMessageLetterList.this);
			}
		});
	}
	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
        return "601".equals(result);
    }

}
