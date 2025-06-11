package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WordUpdateRequest extends BaseXmlRequest {
	
	public static final String MODE_INSERT="insert";
	public static final String MODE_DELETE="delete";
	static String groupname="Iyuba";
	
	public String result;
	public String word;
	public WordUpdateRequest(int userId,String update_mode,String word,
			final RequestCallBack rc){
		super("http://word."+Constant.IYBHttpHead+"/words/updateWord.jsp?userId="+userId+
				"&mod="+update_mode+"&groupName="+groupname+
				"&word="+word);
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
							else if ("word".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									WordUpdateRequest.this.word = response.getText();
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
				rc.requestResult(WordUpdateRequest.this);
			}
		});
	}
	
	public boolean isUpdateWordSuccess(){
        return "1".equals(result);
	}

}
