package com.iyuba.camstory.bean.living;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * 作者：renzhy on 16/8/11 16:10
 * 邮箱：renzhongyigoo@gmail.com
 */
@Root(name="data",strict=false)
public class PayedRecordListOfBody {
	@ElementList(name="record",required = false)
	private List<PayedRecordItemXML> listPayedRecord;

	public List<PayedRecordItemXML> getListPayedRecord() {
		return listPayedRecord;
	}

	public void setListPayedRecord(List<PayedRecordItemXML> listPayedRecord) {
		this.listPayedRecord = listPayedRecord;
	}
}
