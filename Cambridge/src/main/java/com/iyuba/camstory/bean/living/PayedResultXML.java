package com.iyuba.camstory.bean.living;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 作者：renzhy on 16/8/15 20:21
 * 邮箱：renzhongyigoo@gmail.com
 */
@Root(name = "response")
public class PayedResultXML {
	@Element(required = false)
	private String result;

	@Element(required = false)
	private String MD5;

	@Element(required = false)
	private String msg;

	@Element(required = false)
	private String amount;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String MD5) {
		this.MD5 = MD5;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
