package com.iyuba.camstory.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.iyuba.camstory.utils.Emotion;

public class EmotionAdapter extends BaseAdapter {

	private Emotion emotion = new Emotion();
	private Context mContext;

	/**
	 * @param emotion
	 * @param mContext
	 */
	public EmotionAdapter(Emotion emotion, Context mContext) {
		super();
		this.emotion = emotion;
		this.mContext = mContext;
	}

	/**
	 * @param mContext
	 */
	public EmotionAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		System.out.println(emotion.emotion.length);
		return emotion.emotion.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			// 给ImageView设置资源
			imageView = new ImageView(mContext);
			// 设置布局 图片120×120显示
			imageView.setLayoutParams(new GridView.LayoutParams(35, 35));
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(emotion.emotion[position]);
		return imageView;
	}
}
