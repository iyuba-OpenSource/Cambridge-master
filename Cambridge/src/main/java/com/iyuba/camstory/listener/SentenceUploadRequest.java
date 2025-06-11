package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SentenceUploadRequest extends BaseXmlRequest {

	public String result;
	public String msg;
	public SentenceUploadRequest(int userid,String type,int voaid,float sentenceid,
			final RequestCallBack rc) {
		// TODO 自动生成的构造函数存根
		super("http://apps."+Constant.IYBHttpHead+"/voa/updateCollect.jsp?" +
							"userId=" +
							userid +
							"&type=" +
							type +
							"&voaId=" +
							voaid +
							"&sentenceId=" +
							sentenceid +
							"&groupName=Iyuba&sentenceFlg=1");
		
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
								}
							}
							else if ("msg".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									msg = response.getText();
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
				if (rc == null) {
					rc.requestResult(SentenceUploadRequest.this);
				}
			}
		});
	}

}
