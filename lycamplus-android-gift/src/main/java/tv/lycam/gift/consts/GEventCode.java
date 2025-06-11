package tv.lycam.gift.consts;


import tv.lycam.gift.core.EventCode;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class GEventCode extends EventCode {

    /**
     * 获取礼物清单
     */
    public static final int Http_GetGifts = ++CODE_INC;

    /**
     * 发送消息
     */
    public static final int Http_SendChat = ++CODE_INC;

    /**
     * 发送点亮消息
     */
    public static final int Http_SendLike = ++CODE_INC;

    /**
     * 发送礼物
     */
    public static final int Http_SendGift = ++CODE_INC;

    /**
     * 处理弹幕消息
     */
    public static final int Local_Barrage = ++CODE_INC;

    /**
     * 处理普通聊天消息
     */
    public static final int Local_Normal = ++CODE_INC;

    /**
     * 处理礼物消息
     */
    public static final int Local_Gift = ++CODE_INC;

    /**
     * 处理点心消息
     */
    public static final int Local_Like = ++CODE_INC;

    /**
     * 处理用户进入消息
     */
    public static final int Local_UserJoin = ++CODE_INC;

    /**
     * 处理用户离开消息
     */
    public static final int Local_UserLeave = ++CODE_INC;

    /**
     * 发送弹幕消息
     */
    public static final int Http_SendBarrage = ++CODE_INC;

    /**
     * 下载zip包
     */
    public static final int Http_DownloadZip = ++CODE_INC;

    /**
     * 主播状态改变
     */
    public static final int Local_UserStatus = ++CODE_INC;

    /**
     * 直播结束
     */
    public static final int Local_UnPublish = ++CODE_INC;
}
