/**
 * 
 */
package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.Fans;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 * 请求通知
 *   condition可取的参数
 *   doing 心情评论
 *   blog  日志评论
 *   comment 评论回复
 *   uid    留言板
 *   pic    图片评论
 *   follow 关注通知
 *   system 系统通知
 *   invite 邀请通知
 *   app    应用通知
 */
public class RequestNotificationInfo extends BaseJsonObjectRequest{
	private static final String protocolCode="61002";
	
	
	public String result;// 返回代码
	public String message;// 返回信息
	public String uid;
	public MessageLetter letter = new MessageLetter();
	public JSONArray data;
	public ArrayList<Fans> list;
	public int firstPage;
	public int prevPage;
	public int nextPage;
	public int lastPage;
	
	public RequestNotificationInfo(int uid,String pageNumber,String condition,final RequestCallBack rc) {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol="+ protocolCode
				+ "&uid=" + uid
				+ "&pageCounts=10000"
				+ "&pageNumber="+pageNumber
				+ "&condition="+condition
				+ "&asc=0"
				+ "&sign="+ MD5.getMD5ofStr(protocolCode+uid+"iyubaV2"));
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonBody) {
				// TODO 自动生成的方法存根
				list = new ArrayList<Fans>();
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
				if (result.equals("632")) {

				} else {
					try {
						prevPage = jsonBody.getInt("prevPage");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						firstPage = jsonBody.getInt("firstPage");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						nextPage = jsonBody.getInt("nextPage");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						lastPage = jsonBody.getInt("lastPage");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						RequestNotificationInfo.this.uid = jsonBody.getString("uid");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (result.equals("631")) {
						try {
							data = jsonBody.getJSONArray("data");
						} catch (JSONException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						if (data != null && data.length() != 0) {
							for (int i = 0; i < data.length(); i++) {
								try {
									JSONObject jsonObject = ((JSONObject) data.opt(i));
									Fans item = new Fans();
									item.uid = jsonObject.getString("authorid");
									item.isnew = jsonObject.getString("new");
									item.doing = jsonObject.getString("note");
									item.username = jsonObject.getString("author");
									item.dateline=jsonObject.getString("dateline");
									list.add(item);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				rc.requestResult(RequestNotificationInfo.this);
			}
		});
}


	@Override
	public boolean isRequestSuccessful() {
		// TODO 自动生成的方法存根
        return "631".equals(result);
    }
}


