package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 查询爱语币
 * 
 * @author chentong
 * 
 */
public class CheckAmountRequest extends BaseXmlRequest {
	
	private String result;
	public String msg;
	public String amount = "0";
	public CheckAmountRequest(int userId,final RequestCallBack rc) {
		super("http://app."+Constant.IYBHttpHead+"/pay/checkApi.jsp?userId=" + userId);
		setResListener(new Listener<XmlPullParser>() {

			@Override
			public void onResponse(XmlPullParser response) {
				int eventType;
				try {
					eventType = response.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							if ("result".equals(response.getName())) {
								response.next();
								result = response.getText();
							}
							else if ("amount".equals(response.getName())) {
								response.next();
								amount = response.getText();
							}
							break;
						}
						if ("0".equals(result)) {
							break;
						}
						eventType = response.next();
					}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			rc.requestResult(CheckAmountRequest.this);
			}
		});
	}
	
	public boolean isCheckSuccess(){
		return "0".equals(result);
	}

}
