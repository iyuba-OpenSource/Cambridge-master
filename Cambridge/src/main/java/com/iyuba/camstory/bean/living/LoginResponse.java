package com.iyuba.camstory.bean.living;

/**
 * 作者：renzhy on 16/8/2 21:24
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LoginResponse {

	/**
	 * uid : 928
	 * expireTime : 1483711704
	 * result : 101
	 * Amount : 33
	 * vipStatus : 1
	 * message : success
	 * username : iyuba
	 * email : iyuba@sina.com
	 * jiFen : 0
	 * imgSrc : http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=big&uid=928
	 * money : 2
	 * mobile : 000
	 * isteacher : 0
	 */

	private int uid;
	private long expireTime;
	private String result;
	private String Amount;
	private String vipStatus;
	private String message;
	private String username;
	private String email;
	private int jiFen;
	private String imgSrc;
	private String money;
	private String mobile;
	private String isteacher;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String Amount) {
		this.Amount = Amount;
	}

	public String getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getJiFen() {
		return jiFen;
	}

	public void setJiFen(int jiFen) {
		this.jiFen = jiFen;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsteacher() {
		return isteacher;
	}

	public void setIsteacher(String isteacher) {
		this.isteacher = isteacher;
	}
}
