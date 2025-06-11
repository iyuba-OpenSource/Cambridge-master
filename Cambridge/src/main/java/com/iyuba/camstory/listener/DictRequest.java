package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.Word;
import com.iyuba.configation.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**z
 * 获取网页单词本
 * @author Administrator
 *
 */
public class DictRequest extends BaseXmlRequest {
	public Word word;
	public String result;
	public DictRequest(String wordString,final RequestCallBack rc) {
		super("http://word."+Constant.IYBHttpHead+"/words/apiWord.jsp?q="+wordString);
		// TODO 自动生成的构造函数存根
		setResListener(new Listener<XmlPullParser>() {
			@Override
			public void onResponse(XmlPullParser response) {
				// TODO 自动生成的方法存根
				try {
					int eventType = response.getEventType();
					StringBuffer sentence = new StringBuffer();
					 while (eventType != XmlPullParser.END_DOCUMENT){
						 switch (eventType) {
	                     case XmlPullParser.START_TAG:  
	                    	 String nodeName = response.getName(); 
	                    	 if ("result".equals(nodeName)) {
	                    		 response.next();
	                    		 result = response.getText();
	                         }
	                    	 else if ("key".equals(nodeName)) {
	                    		 response.next();
	                    		 word = new Word();
	                    		 word.key = response.getText();
	                    	 }
	                    	 else if ("audio".equals(nodeName)) {
	                    		 response.next();
	                    		 word.audioUrl = response.getText();
							 }
	                    	 else if ("lang".equals(nodeName)) {
	                    		 response.next();
	                    		 word.audioUrl = response.getText();
							}
	                    	 else if ("pron".equals(nodeName)) {
	                    		 response.next();
	                    		 word.pron = response.getText();
							}
	                    	 else if ("def".equals(nodeName)) {
	                    		 response.next();
	                    		 word.def = response.getText();
							}
	                    	 else if ("number".equals(nodeName)) {
	                    		 response.next();
	                    		 sentence.append(response.getText());
	             				sentence.append("：");
							}
	                    	 else if ("orig".equals(nodeName)) {
	                    		 response.next();
	                    		 sentence.append(response.getText());
		             				sentence.append("<br/>");
	                    	 }
	                    	 else if ("trans".equals(nodeName)) {
	                    		 response.next();
	                    		 sentence.append(response.getText());
		             				sentence.append("<br/>");
	                    	 }
	                         break;  
	                        case XmlPullParser.END_TAG:  
	                        	if ("sent".equals(response.getName())) {
									word.examples = sentence.toString();
								}
	                        	break;
						 }
						 eventType  = response.next();
					}
					 rc.requestResult(DictRequest.this);
				} catch (XmlPullParserException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
	}
	
	public boolean isWordRequested(){
        return "1".equals(result);
	}

	
//	public DictRequest(String word){
//		this.word=word;
//		setAbsoluteURI("http://word."+Constant.IYBHttpHead+"/words/apiWord.jsp?q="+word);
//	}
}
