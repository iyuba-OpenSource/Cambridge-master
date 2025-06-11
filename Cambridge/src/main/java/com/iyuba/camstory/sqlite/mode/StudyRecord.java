package com.iyuba.camstory.sqlite.mode;

public class StudyRecord {
	public int uid;        //爱语吧Id
	public String appName;
	public String BeginTime;
	public String EndTime;
	public String Lesson;  //产品名：voa慢速英语,VOA常速英语,BBC六分钟英语,听歌学英语
	public int LessonId;//文章ID
	public int EndFlg;  //完成标志：0：只开始听；1：听力完成；2：做题完成.
	//public String Device;   //做题设备：android手机，iphone手机，firefox,ie等
}
