package com.iyuba.camstory.listener;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.voa.activity.setting.Constant;

public class PayVipRequest extends BaseXmlRequest {

	// result 0是失败，1 是成功
	// msg 为此次操作信息。
	// amount 若购买成功，返回此用户当前（购买之后）的爱语币amount值。

	private String result;
	private String message;
	private int amount;

	/**
	 *  userId 必须参数。表示购买者用户ID appId 必须参数：0 amount 必须参数。表示购买用户在这个app中所化费用。
	 * productId 必须参数。表示购买VIP的月数：1代表1个月，12代表一年 sign 必须参数。检查此次操作的合法性。
	 * 
	 * @param uid
	 * @param Amount
	 * @param mounth
	 * @param errorListener
	 * @param rc
	 */
	public PayVipRequest(int uid, int Amount, int mounth,
			ErrorListener errorListener, final RequestCallBack rc) {
		super(
				"http://app."+Constant.IYBHttpHead+"/pay/payVipApi.jsp?userId="
						+ uid
						+ "&amount="
						+ Amount
						+ "&appId="
						+ Constant.getAppid()
						+ "&productId="
						+ mounth
						+ "&sign="
						+ MD5.getMD5ofStr(Amount + Constant.getAppid() + uid + mounth
								+ "iyuba"), errorListener);
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
								if (response.next() == XmlPullParser.TEXT) {
									result = response.getText();
								}
							} else if ("msg".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									message = response.getText();
								}
							} else if ("amount".equals(response.getName())) {
								if (response.next() == XmlPullParser.TEXT) {
									amount = Integer.valueOf(response.getText());
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
				Log.e("购买VIP结果回复PayResponse", "result = " + result + ", msg = "
						+ message + ", amount = " + amount);
				rc.requestResult(PayVipRequest.this);
			}
		});
		// TODO 自动生成的构造函数存根
	}

	public boolean isBuyVipSuccess() {

        return "1".equals(result);

	}

	public String getAmount() {

		return amount + "";

	}

	public String getMessage() {

		return message;

	}

}
