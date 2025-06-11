package tv.lycam.gift.callback;

import java.util.List;

import tv.lycam.gift.entity.GiftItem;

/**
 * Created by liujunhong on 2016/7/4.
 */
public interface GiftlistCallback {
	/**
	 * 初始化礼物列表
	 *
	 * @param items 礼物清单信息
	 */
	void initGiftlist(List<GiftItem> items);

	/**
	 * 找到礼物列表
	 *
	 * @return 礼物清单信息
	 */
	List<GiftItem> findGiftlist();
}
