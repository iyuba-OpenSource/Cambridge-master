package com.iyuba.camstory.adpater;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.sqlite.mode.Word;

/**
 * 修复没有单词表时的异常
 * 
 * @author ct
 * @time 12.9.27
 * 
 */

public class WordListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Word> mList = new ArrayList<Word>();
	public boolean modeDelete = false;
	public ViewHolder viewHolder;
	//private final static String notRemember = "没记住,可点击查看";

	public WordListAdapter(Context context, ArrayList<Word> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Word word = mList.get(position);
		final int pos = position;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.listitem_word, null);
			viewHolder = new ViewHolder();
			viewHolder.key = convertView.findViewById(R.id.word_key);
			viewHolder.key.setTextColor(Color.BLACK);
			viewHolder.pron = convertView
					.findViewById(R.id.word_pron);
			viewHolder.def = convertView.findViewById(R.id.word_def);
			viewHolder.deleteBox = convertView
					.findViewById(R.id.checkBox_isDelete);
			viewHolder.speaker = convertView
					.findViewById(R.id.word_speaker);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (modeDelete) {
			viewHolder.deleteBox.setVisibility(View.VISIBLE);
		} else {
			viewHolder.deleteBox.setVisibility(View.GONE);
		}
		if (mList.get(pos).isDelete) {
			viewHolder.deleteBox.setImageResource(R.drawable.check_box_checked);
		} else {
			viewHolder.deleteBox.setImageResource(R.drawable.check_box);
		}

		viewHolder.key.setText(word.key);
		/*Typeface mFace = Typeface.createFromAsset(mContext.getAssets(),
				"font/SEGOEUI.TTF");*/
		//viewHolder.pron.setTypeface(mFace);
		if (word.pron != null && word.pron.length() != 0
				&& !word.pron.equals("null")) {
			viewHolder.pron.setText(Html.fromHtml("[" + word.pron + "]"));
		} else {
			viewHolder.pron.setText("");
		}
		viewHolder.def.setText(mContext.getResources().getString(R.string.wladapter_info1));
		if (word.audioUrl != null && word.audioUrl.length() != 0) {
			viewHolder.speaker.setVisibility(View.VISIBLE);
		} else {
			viewHolder.speaker.setVisibility(View.GONE);
		}
		/*viewHolder.speaker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Player player = new Player(mContext, null, null);
				String url = word.audioUrl;
				player.playUrl(url);
			}
		});*/
		return convertView;
	}

	public class ViewHolder {
		TextView key;
		TextView pron;
		public TextView def;
		ImageView deleteBox;
		ImageView speaker;
	}

}
