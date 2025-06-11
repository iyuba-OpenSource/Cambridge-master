package com.iyuba.camstory.adpater;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.sqlite.mode.Sentence;
import com.nostra13.universalimageloader.core.ImageLoader;
public class SentenceAdapter extends BaseAdapter {
	private List<Sentence> sList;
	private LayoutInflater mInflater;

	public SentenceAdapter(List<Sentence> sList) {
		super();
		this.sList = sList;
		mInflater = LayoutInflater.from(CrashApplication.getInstance());
	}

	@Override
	public int getCount() {
		return sList != null ? sList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return sList != null ? sList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return sList != null ? position : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Sentence sentence = sList.get(pos);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_sentencelist, null);
		}
		ImageView imageView = ViewHolder.get(convertView, R.id.sentence_image);
		TextView textView = ViewHolder.get(convertView, R.id.sentence_text);
		TextView textView_cn = ViewHolder.get(convertView, R.id.sentence_cn_text);
		textView.setText(sentence.sentence);
		textView_cn.setText(sentence.sentence_cn);
		ImageLoader.getInstance().displayImage(sentence.imgpath, imageView);

		return convertView;
	}

}
