package com.iyuba.camstory.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iyuba.camstory.R;

/**
 * @author 表情类
 */
public class Emotion {

	/**
	 * 
	 */
	public Emotion() {
		super();
	}

	private int position;
	public int[] emotion = { R.mipmap.image1, R.mipmap.image2, R.mipmap.image3,
			R.mipmap.image4, R.mipmap.image5, R.mipmap.image6, R.mipmap.image7,
			R.mipmap.image8, R.mipmap.image9, R.mipmap.image10, R.mipmap.image11,
			R.mipmap.image12, R.mipmap.image13, R.mipmap.image14, R.mipmap.image15,
			R.mipmap.image16, R.mipmap.image17, R.mipmap.image18, R.mipmap.image19,
			R.mipmap.image20, R.mipmap.image21, R.mipmap.image22, R.mipmap.image23,
			R.mipmap.image24, R.mipmap.image25, R.mipmap.image26, R.mipmap.image27,
			R.mipmap.image28, R.mipmap.image29, R.mipmap.image30 };

	/**
	 * @param position
	 */
	public Emotion(int position) {
		super();
		this.position = position;
	}

	public static final String[] express = { "(发呆)", "(微笑)", "(大笑)", "(坏笑)", "(偷笑)", "(生气)", "(不)",
			"(难过)", "(哭)", "(偷瞄)", "(晕)", "(汗)", "(困)", "(害羞)", "(惊讶)", "(开心)", "(色)", "(得意)", "(骷髅)",
			"(囧)", "(睡觉)", "(撇嘴)", "(亲)", "(疑问)", "(闭嘴)", "(失望)", "(茫然)", "(努力)", "(鄙视)", "(猪头)", };
	public final static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("(发呆)", "image1");
		map.put("(微笑)", "image2");
		map.put("(大笑)", "image3");
		map.put("(坏笑)", "image4");
		map.put("(偷笑)", "image5");
		map.put("(生气)", "image6");
		map.put("(不)", "image7");
		map.put("(难过)", "image8");
		map.put("(哭)", "image9");
		map.put("(偷瞄)", "image10");
		map.put("(晕)", "image11");
		map.put("(汗)", "image12");
		map.put("(困)", "image13");
		map.put("(害羞)", "image14");
		map.put("(惊讶)", "image15");
		map.put("(开心)", "image16");
		map.put("(色)", "image17");
		map.put("(得意)", "image18");
		map.put("(骷髅)", "image19");
		map.put("(囧)", "image20");
		map.put("(睡觉)", "image21");
		map.put("(撇嘴)", "image22");
		map.put("(亲)", "image23");
		map.put("(疑问)", "image24");
		map.put("(闭嘴)", "image25");
		map.put("(失望)", "image26");
		map.put("(茫然)", "image27");
		map.put("(努力)", "image28");
		map.put("(鄙视)", "image29");
		map.put("(猪头)", "image30");
	}

	private static int[] imageIds = new int[30];

	public static List<Map<String, Object>> initEmotion() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
		for (int i = 0; i < 30; i++) {
			try {
				Field field = R.drawable.class.getDeclaredField("image" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				System.out.println("resourceId==" + resourceId);
				System.out.println("field==" + field);
				imageIds[i] = resourceId;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		return listItems;
	}

	public static Boolean isExpress(String string) {
		String check = "\\([\\u4e00-\\u9fa5]{1,2}\\)";
		boolean result = Pattern.matches(check, string);
		return result;
	}

	public static String replace(String string) {
		String check = "\\([\\u4e00-\\u9fa5]{1,2}\\)";
		boolean result = Pattern.matches(check, string);
		Pattern pattern = Pattern.compile(check);
		Matcher matcher = pattern.matcher(string);
		for (int i = 0; matcher.find(); i++) {
			String valueString = Emotion.map.get(matcher.group(0));
			string = string.replace(matcher.group(0), valueString);
		}
		return string;
	}
}
