package com.iyuba.camstory.lycam.util;

/**
 * 作者：renzhy on 16/7/20 14:26
 * 邮箱：renzhongyigoo@gmail.com
 */
public class MatchCourseType {

	private static volatile MatchCourseType instance = null;

	private MatchCourseType() {
	}

	public static MatchCourseType getInstance(){
		if(instance == null){
			synchronized (MatchCourseType.class){
				if(instance == null){
					instance =  new MatchCourseType();
				}
			}
		}
		return instance;
	}

	public String getCourseTypeName(int courseTypeId){
		String courseTypeName = null;
		switch (courseTypeId){
			case -2:
				courseTypeName = "全部";
				break;
			case -1:
				courseTypeName = "最新";
				break;
			case 1:
				courseTypeName = "N1";
				break;
			case 2:
				courseTypeName = "CET4";
				break;
			case 3:
				courseTypeName = "VOA";
				break;
			case 4:
				courseTypeName = "CET6";
				break;
			case 5:
				courseTypeName = "N2";
				break;
			case 6:
				courseTypeName = "N3";
				break;
			case 7:
				courseTypeName = "托福";
				break;
			case 8:
				courseTypeName = "考研英语1";
				break;
			case 21:
				courseTypeName = "新概念";
				break;
			case 22:
				courseTypeName = "走遍美国";
				break;
			case 28:
				courseTypeName = "学位英语";
				break;
			case 52:
				courseTypeName = "考研英语2";
				break;
			case 61:
				courseTypeName = "雅思";
				break;
			case 91:
				courseTypeName = "中职英语";
				break;
			default:
				courseTypeName = "";
				break;
		}
		return courseTypeName;
	}

	public String getCourseTypeId(int postion) {
		String courseTypeId = null;
		switch (postion) {
			case 0:
				//四级的reqPackId
				courseTypeId = "2";
				break;
			case 1:
				//六级的reqPackId
				courseTypeId = "4";
				break;
			case 2:
				//托福的reqPackId
				courseTypeId = "7";
				break;
			case 3:
				//雅思的reqPackId
				courseTypeId = "61";
				break;
			case 4:
				//日语的reqPackId
				courseTypeId = "1";
				break;
			case 5:
				//全部课程的reqPackId
				courseTypeId = "-2";
				break;
			default:
				courseTypeId = "-2";
				break;
		}
		return courseTypeId;
	}

	public String getCourseTypeTitle(int postion) {
		String courseTypeTitle = null;
		switch (postion) {
			case 0:
				courseTypeTitle = "CET4课程";
				break;
			case 1:
				courseTypeTitle = "CET6课程";
				break;
			case 2:
				courseTypeTitle = "托福课程";
				break;
			case 3:
				courseTypeTitle = "雅思课程";
				break;
			case 4:
				courseTypeTitle = "日语N1课程";
				break;
			case 5:
				courseTypeTitle = "全部课程";
				break;
			default:
				courseTypeTitle = "爱语学堂";
				break;
		}
		return courseTypeTitle;
	}

}
