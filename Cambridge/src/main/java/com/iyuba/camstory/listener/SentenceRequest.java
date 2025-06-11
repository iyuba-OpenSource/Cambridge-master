package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.Sentence;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


public class SentenceRequest extends BaseXmlRequest {
	
	public ArrayList<Sentence>  sList=new ArrayList<Sentence>();
	private Sentence temSentence;
	int total;
	public SentenceRequest(int userid,int pageNumber,final RequestCallBack rc) {
		// TODO 自动生成的构造函数存根
		super("http://apps."+Constant.IYBHttpHead+"/voa/getCollect.jsp?" +
							"userId=" +
							userid +
							"&groupName=Iyuba&type=voa&sentenceFlg=1&" +
							"pageNumber=" +
							pageNumber +
							"&pageCounts=10000");
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
							if ("counts".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									total = Integer.parseInt(response.getText());
								}
							}
							else if ("row".equals(response.getName())) {
								temSentence = new Sentence();
							}
							else if ("voaid".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temSentence.voaid =Integer.parseInt(response.getText());
								}
							}
							else if ("sentenceid".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temSentence.starttime =Double.parseDouble(response.getText());
								}
							}
							else if ("endTime".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temSentence.endtime =Double.parseDouble(response.getText());
								}
							}
							else if ("content".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temSentence.sentence =response.getText();
								}
							}
							else if ("contentcn".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temSentence.sentence_cn =response.getText();
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if ("row".equals(response.getName())) {
								sList.add(temSentence);
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
				rc.requestResult(SentenceRequest.this);
			}
		});
	}
	
	public boolean hasSentence(){
        return total != 0;
    }
	

}
