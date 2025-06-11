package com.iyuba.camstory.bean.living;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 作者：renzhy on 16/8/11 15:23
 * 邮箱：renzhongyigoo@gmail.com
 */
@Root(name="record")
public class PayedRecordItemXML {
	@Element(required = false)
	private String PackId;

	@Element(required = false)
	private String ProductId;

	@Element(required = false)
	private String Amount;

	@Element(required = false)
	private String flg;

	public String getPackId() {
		return PackId;
	}

	public void setPackId(String packId) {
		PackId = packId;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}
}
