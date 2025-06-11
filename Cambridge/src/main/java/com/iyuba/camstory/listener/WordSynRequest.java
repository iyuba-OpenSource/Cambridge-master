package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.Word;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class WordSynRequest extends BaseXmlRequest {
	
	public ArrayList<Word> wordList = new ArrayList<Word>();
	private Word temWord;
	public int total;
	private int user;
	private int requestPage;
	private int lastPage;
	public WordSynRequest(final int userid,int page,final RequestCallBack rc) {
		super("http://word."+Constant.IYBHttpHead+"/words/wordListService.jsp?u="
				+ userid + "&pageNumber="+page+"&pageCounts=30");
		requestPage = page;
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
								temWord = new Word();
								temWord.userid = userid;
							}
							else if ("Word".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temWord.key = response.getText();
								}
							}
							else if ("Audio".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temWord.audioUrl = response.getText();
								}
							}
							else if ("Pron".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temWord.pron = response.getText();
								}
							}
							else if ("Def".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									temWord.def = response.getText();
								}
							}
							else if ("lastPage".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									lastPage = Integer.parseInt(response.getText());
								}
							}
							break;
							
						case XmlPullParser.END_TAG:
							if ("row".equals(response.getName())) {
								wordList.add(temWord);
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
				rc.requestResult(WordSynRequest.this);
			}
		});
	}
	
	public boolean hasWord(){
        return requestPage <= lastPage
                && wordList.size() > 0;
    }

}
