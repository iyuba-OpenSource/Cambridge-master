/**
 * 
 */
package com.iyuba.camstory.sqlite.mode;

import android.graphics.Bitmap;

/**
 * @author yao
 * 通知类，包括系统通知、日志留言、评论等
 */
public class NotificationInfo {
	public String id;//"id":1497,   通知id
    public String author;//"author":"", 通知发布者 空表示系统发送
    public String from_idtype;//"from_idtype":"", （若为日志评论，则为blog）
    public String isnew;//"new":1,  是否已读
    public String from_id;//"from_id":0, （若为日志评论则返回日志id）
    public String authorid;//"authorid":0, 通知的发布者 0表示系统发送
    public String dateline;//"dateline":1329369022,
    public String type;//"type":"system",
    public String from_num;//"from_num":1,  发送的次数
    public String note;//"note":"...."}, 发送的内容
    public Bitmap userBitmap;
    
/*    from_idtype 来源类型
    from_id     来源id
    这两个值应该是 blogid 以及日志id
    id在 9120之前的都是老数据*/
}
