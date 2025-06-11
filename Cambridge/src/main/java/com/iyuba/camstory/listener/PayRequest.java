package com.iyuba.camstory.listener;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.voa.activity.setting.Constant;



/**
 * 
 * @author zqq
 * 
 *         功能：支付回复  result 0是失败，1 是成功  msg 为此次操作信息。  amount
 *         若购买成功，返回此用户当前（购买之后）的amount值
 */
public class PayRequest extends BaseXmlRequest {

	public String result;
	public String msg;
	public String amount;
	public String vipflag;
	public String VipEndTime;
	public PayRequest(int userId, String amount,String month,final RequestCallBack rc) {
		super("http://app."+Constant.IYBHttpHead+"/pay/apiPayByDate.jsp?userId="
				+ userId + "&amount=" + amount +"&month=" +month + "&appId=" + Constant.getAppid()
				+ "&productId=0&sign=" + MD5.getMD5ofStr(amount + Constant.getAppid() + userId + "0"+month+"iyuba"));
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
							else if ("amount".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									PayRequest.this.amount = response.getText();
								}
							}
							else if ("VipFlg".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									vipflag = response.getText();
								}
							}
							else if ("VipEndTime".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									VipEndTime = response.getText();
								}
							}
							break;
						}
						if ("0".equals(result)) {
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
//				Log.e("付款结果回复PayResponse", "result = " + result + ", msg = " + msg
//						+ ", amount = " + amount + "vipflag =" + vipflag
//						+ "		VipEndTime = " +VipEndTime);
				
				rc.requestResult(PayRequest.this);
			}
		});
	}
	public boolean isPaySuccess(){
        return "1".equals(result);
    }

}
