package com.iyuba.camstory.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.iyuba.camstory.bean.Subtitle;

public class SubtitleSum {
	public String articleTitle; // 文章标题
	public String articleDesc;
	public int voaid;
	public String photoUrl;
	public String mp3Url;
	public Bitmap photoImg;
	public boolean favorites; // 是否收藏
	public List<Subtitle> subtitles=new ArrayList<Subtitle>(); // 字幕
	
	public int getParagraph(double second){
		int step =0;
		if(subtitles!=null&&subtitles.size()!=0){
			for(int i=0;i<subtitles.size();i++){
				if(second>=subtitles.get(i).pointInTime){
					step = i+1;
				}else{
					break;
				}
			}
		}
		return step;
	}
}
