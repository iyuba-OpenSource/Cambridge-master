package com.iyuba.camstory.sqlite.mode;
import java.io.Serializable;
import android.graphics.Bitmap;
public class Sentence implements Serializable{
	
	public int voaid;				//哪个文章里的      			根据voaid和starttime确定相等		
	public String imgpath;			//	图片			
	public String sentence;			//英文句子
	public String sentence_cn;		//中文句子
	public double starttime;		//开始时间
	public double endtime;			//结束时间
	public transient Bitmap picBitmap;
	
	@Override
	public boolean equals(Object o) {
		// TODO 自动生成的方法存根

        return o instanceof Sentence &&
                ((Sentence) o).voaid == this.voaid &&
                ((Sentence) o).starttime == this.starttime;
	}
	@Override
	public int hashCode() {
		// TODO 自动生成的方法存根
		 int result = 17;
		 result = 31*result +voaid; //c1,c2是什么看下文解释  
		  result = (int) (31*result +Double.doubleToLongBits(starttime));  
		  return result;  
	}
	
	
	
	
}
