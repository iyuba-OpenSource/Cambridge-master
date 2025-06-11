package com.iyuba.camstory.bean.living;

/**
 * 作者：renzhy on 16/7/16 12:09
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LivePayedItem {
	public String uid;
	public String PackId;
	public String ProductId;
	public String Amount;
	public String flg;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

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

	@Override
	public String toString() {
		return "LivePayedItem{" +
				"uid='" + uid + '\'' +
				", PackId='" + PackId + '\'' +
				", ProductId='" + ProductId + '\'' +
				", Amount='" + Amount + '\'' +
				", flg='" + flg + '\'' +
				'}';
	}
}
