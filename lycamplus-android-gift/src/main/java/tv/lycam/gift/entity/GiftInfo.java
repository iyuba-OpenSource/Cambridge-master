package tv.lycam.gift.entity;

import java.util.List;

/**
 * Created by su on 16/5/23.
 */
public class GiftInfo {

	private boolean success;

	private List<GiftItem> items;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<GiftItem> getItems() {
		return items;
	}

	public void setItems(List<GiftItem> items) {
		this.items = items;
	}

}
