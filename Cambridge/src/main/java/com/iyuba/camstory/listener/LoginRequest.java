package com.iyuba.camstory.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.voa.activity.setting.Constant;

/**
 * 用户登录
 * 
 * @author chentong
 * 
 */

public class LoginRequest extends BaseXmlRequest {
	private static final String TAG = LoginRequest.class.getSimpleName();
	
	public String result, username, imgsrc, vip, validity, amount,money;
	public int uid;

	public LoginRequest(String userName, String password, String latitude,
			String longitude, ErrorListener listener, final RequestCallBack rc) throws UnsupportedEncodingException {
		super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=11001&username="
				+ URLEncoder.encode(userName,"UTF-8")
				+ "&password="
				+ MD5.getMD5ofStr(password)
				+ "&x="
				+ longitude
				+ "&y="
				+ latitude
				+ "&appid="
				+ Constant.getAppid()
				+ "&sign="
				+ MD5.getMD5ofStr("11001" + userName + MD5.getMD5ofStr(password)
						+ "iyubaV2") + "&format=xml", listener);
		setResListener(new Listener<XmlPullParser>() {
			@Override
			public void onResponse(XmlPullParser response) {
				try {
					int eventType = response.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							String nodeName = response.getName();
							if ("result".equals(nodeName)) {
								response.next();
								result = response.getText();
							} else if ("uid".equals(nodeName)) {
								response.next();
								String uidsString = response.getText();
								if (uidsString == null || uidsString.equals("")) {
									uid = 0;
								} else {
									uid = Integer.parseInt(uidsString);
								}
							} else if ("username".equals(nodeName)) {
								response.next();
								username = response.getText();
							} else if ("imgSrc".equals(nodeName)) {
								response.next();
								imgsrc = response.getText();
							} else if ("vipStatus".equals(nodeName)) {
								response.next();
								vip = response.getText();
							} else if ("expireTime".equals(nodeName)) {
								response.next();
								validity = response.getText();
							} else if ("Amount".equals(nodeName)) {
								response.next();
								amount = response.getText();
							}else if("money".equals(nodeName)){
								response.next();
								money = response.getText();
							}
							break;
						}
						eventType = response.next();
					}
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				rc.requestResult(LoginRequest.this);
			}
		});
		setRetryPolicy(new RetryPolicy() {

			@Override
			public void retry(VolleyError error) throws VolleyError {
				Log.e(TAG, "run retry once");
			}

			@Override
			public int getCurrentTimeout() {
				return 5000;
			}

			@Override
			public int getCurrentRetryCount() {
				return 3;
			}
		});
	}

	public boolean isLoginSuccess() {
		return "101".equals(result);
	}

	public static class AppUpdateRequest extends BaseXmlRequest {
        private static final String TAG = AppUpdateRequest.class.getSimpleName();
        private String result = "";
        public String msg = "";
        public String version = "";
        public String appUrl = "";

        public AppUpdateRequest(int version, final RequestCallBack rc) {
            super(Constant.getAppupdateurl() + version);
            Log.e(TAG, Constant.getAppupdateurl() + version);
            setResListener(new Listener<XmlPullParser>() {
                @Override
                public void onResponse(XmlPullParser response) {
                    int eventType;
                    try {
                        eventType = response.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if ("status".equals(response.getName())) {
                                    if (response.next() == XmlPullParser.TEXT) {
                                        result = response.getText();
                                    }
                                } else if ("versiuon".equals(response.getName())) {
                                    if (response.next() == XmlPullParser.TEXT) {
                                        AppUpdateRequest.this.version = response.getText();
                                    }
                                } else if ("url".equals(response.getName())) {
                                    if (response.next() == XmlPullParser.TEXT) {
                                        appUrl = response.getText();
                                    }
                                }
                                break;
                            default:
                                break;
                            }
                            eventType = response.next();
                            if ("YES".equals(result)) {
                                break;
                            }
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rc.requestResult(AppUpdateRequest.this);
                }
            });
        }

        public boolean isLastestVersion() {
            return "YES".equals(result);
        }

        public boolean isOldVersion(){
            return ("NO".equals(result));
        }

    }

	@Override
	public String toString() {
		return "LoginRequest{" +
				"result='" + result + '\'' +
				", username='" + username + '\'' +
				", imgsrc='" + imgsrc + '\'' +
				", vip='" + vip + '\'' +
				", validity='" + validity + '\'' +
				", amount='" + amount + '\'' +
				", uid=" + uid +
				'}';
	}
}
