package com.iyuba.camstory.manager;

import android.util.Log;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.CommonCallBack;
import com.iyuba.camstory.listener.RequestBasicUserInfo;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.sqlite.mode.UserInfo;


public class UserInfoManager {
	public static UserInfo myInfo;

	public static void getMyInfo(final CommonCallBack listener) {
		RequestBasicUserInfo request = new RequestBasicUserInfo(
				AccountManager.getInstance().userId, new RequestCallBack() {

					@Override
					public void requestResult(Request result) {
						RequestBasicUserInfo response = (RequestBasicUserInfo) result;
						if (response.isRequestSuccessful()) {
							myInfo = response.userInfo;
							listener.onPositive(myInfo);
							//listener.onPositive(null);
							Log.e("myuser",myInfo.toString());
						} else {
							listener.onNegative(null);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

	public static void getUserInfo(int userid, final CommonCallBack listener) {
		RequestBasicUserInfo request = new RequestBasicUserInfo(userid,
				new RequestCallBack() {

					@Override
					public void requestResult(Request result) {
						RequestBasicUserInfo response = (RequestBasicUserInfo) result;
						if (response.isRequestSuccessful()) {
							listener.onPositive(response.userInfo);
							Log.e("userid_info",myInfo.toString());
						} else {
							listener.onNegative(null);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
	}

}
