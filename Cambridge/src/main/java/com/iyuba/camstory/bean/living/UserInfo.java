package com.iyuba.camstory.bean.living;

/**
 * 作者：renzhy on 16/8/30 16:58
 * 邮箱：renzhongyigoo@gmail.com
 */
public class UserInfo {
	private int userid;
	private UserInfoBean userInfoBean;

	public UserInfo(int userid, UserInfoBean userInfoBean) {
		this.userid = userid;
		this.userInfoBean = userInfoBean;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}

	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}
}
