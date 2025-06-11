package com.iyuba.camstory.adpater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.camstory.R;

public class VipRightsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private String[] rightTexts = { "无广告", "尊贵标识", "调节语速", "高速下载", "查看解析",
			"智慧化评测", "无限下载", "全部应用", "换话费" };
	private Drawable[] icons;

	public VipRightsAdapter(Context context) {
		this.mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		icons = new Drawable[9];
		icons[0] = mContext.getResources().getDrawable(R.mipmap.vipright0);
		icons[1] = mContext.getResources().getDrawable(R.mipmap.vipright1);
		icons[2] = mContext.getResources().getDrawable(R.mipmap.vipright2);
		icons[3] = mContext.getResources().getDrawable(R.mipmap.vipright3);
		icons[4] = mContext.getResources().getDrawable(R.mipmap.vipright4);
		icons[5] = mContext.getResources().getDrawable(R.mipmap.vipright5);
		icons[6] = mContext.getResources().getDrawable(R.mipmap.vipright6);
		icons[7] = mContext.getResources().getDrawable(R.mipmap.vipright7);
		icons[8] = mContext.getResources().getDrawable(R.mipmap.vipright8);
	}

	@Override
	public int getCount() {
		return rightTexts.length;
	}

	@Override
	public Object getItem(int position) {
		return rightTexts[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.vipright_item, null);
		}
		ImageView rightIcon = ViewHolder.get(convertView, R.id.vipright_icon);
		TextView rightText = ViewHolder.get(convertView, R.id.vipright_text);
		ImageView stub = ViewHolder.get(convertView, R.id.vipright_stub);
		rightIcon.setImageDrawable(icons[position]);
		rightText.setText(rightTexts[position]);
		stub.setVisibility(View.INVISIBLE);

		return convertView;
	}

}
