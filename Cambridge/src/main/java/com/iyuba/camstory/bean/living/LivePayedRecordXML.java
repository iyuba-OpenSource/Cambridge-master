package com.iyuba.camstory.bean.living;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * 作者：renzhy on 16/8/11 15:02
 * 邮箱：renzhongyigoo@gmail.com
 */
@Root(name = "response",strict = false)
public class LivePayedRecordXML {
	@Element(required = false)
	private String result;

	@Element(required = false)
	private String msg;

	@ElementList(name = "data",required = false)
	private List<PayedRecordItemXML> listPayedRecord;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<PayedRecordItemXML> getListPayedRecord() {
		return listPayedRecord;
	}

	public void setListPayedRecord(List<PayedRecordItemXML> listPayedRecord) {
		this.listPayedRecord = listPayedRecord;
	}
}
