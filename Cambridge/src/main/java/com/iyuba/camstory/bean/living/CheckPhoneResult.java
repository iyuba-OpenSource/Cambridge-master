package com.iyuba.camstory.bean.living;

/**
 * 作者：renzhy on 16/8/6 11:38
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CheckPhoneResult {

	/**
	 * result : 1
	 * res_code : 0
	 * userphone : 13041095153
	 * identifier :
	 */

	private String result;
	private String message;
	private String res_code;
	private String userphone;
	private String identifier;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRes_code() {
		return res_code;
	}

	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
