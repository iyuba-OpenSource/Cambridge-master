package com.iyuba.camstory.listener;

import java.io.IOException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.voa.activity.setting.Constant;

/**
 * 用户注册
 * 
 * @author chentong
 * 
 */
public class RegistRequest extends BaseXmlRequest {
	public String result = "";
	
	public RegistRequest(String userName, String password, String realName,
			String email,final RequestCallBack rc) {
		super(Constant.getEmailRegisturl()+"&email="
				+ email
				+ "&username="
				+ URLEncoder.encode(userName)
				+ "&password="
				+ MD5.getMD5ofStr(password)
				+ "&platform=android&format=xml&sign="
				+ MD5.getMD5ofStr("11002" + userName + MD5.getMD5ofStr(password) + email
						+ "iyubaV2"));
//		Log.e("url", Constant.getEmailRegisturl()+"&email="
//				+ email
//				+ "&username="
//				+ URLEncoder.encode(userName)
//				+ "&password="
//				+ MD5.getMD5ofStr(password)
//				+ "&platform=android&format=xml&sign="
//				+ MD5.getMD5ofStr("10002" + userName + MD5.getMD5ofStr(password) + email
//						+ "iyubaV2"));
		setResListener(new Listener<XmlPullParser>() {
			@Override
			public void onResponse(XmlPullParser response) {
				// TODO 自动生成的方法存根
				int eventType;
				try {
					eventType = response.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							if ("result".equals(response.getName())) {
								
								if (response.next() == XmlPullParser.TEXT) {
									result = response.getText();
									Log.e("result", result+"");
								}
								
							}
							break;
						}
						eventType = response.next();
					}
				} catch (XmlPullParserException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				rc.requestResult(RegistRequest.this);
			}
		});
	}
	
	public boolean isRegistSuccess(){
        return "111".equals(result);
    }
	
	public boolean isUserIdExist(){
        return "112".equals(result);
    }

}
