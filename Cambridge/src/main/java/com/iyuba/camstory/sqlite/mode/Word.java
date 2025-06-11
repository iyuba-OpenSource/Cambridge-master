package com.iyuba.camstory.sqlite.mode;

public class Word {
	public int userid;
	public String key=""; // 关键字
	public String lang="";
	public String audioUrl=""; // 多媒体网络路径
	public String pron=""; // 音标
	public String def=""; // 解释
	public String examples=""; // 例句，多条以“,”分割
	public String createDate=""; // 创建时间
	public boolean isDelete=false;	
	public String delete;
}
