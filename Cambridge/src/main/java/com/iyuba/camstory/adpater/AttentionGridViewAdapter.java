package com.iyuba.camstory.adpater;

import java.util.ArrayList;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.sqlite.mode.Attention;
import com.iyuba.camstory.widget.CircularImageView;
import com.iyuba.voa.activity.setting.Constant;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttentionGridViewAdapter extends BaseAdapter {

	private ArrayList<AttentionBean.DataBean> fans = new ArrayList<AttentionBean.DataBean>();
	private LayoutInflater mInflater;

	public AttentionGridViewAdapter(Context context, ArrayList<AttentionBean.DataBean> list) {
		mInflater = LayoutInflater.from(context);
		fans = list;
	}

	@Override
	public int getCount() {
		return fans.size();
	}

	@Override
	public Object getItem(int position) {
		return fans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AttentionBean.DataBean fan = fans.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_fans, null);
		}
		CircularImageView uCircularImageView = ViewHolder.get(convertView,
				R.id.user_image);
		TextView unameTextView = ViewHolder
				.get(convertView, R.id.username_textview);
		ImageLoader.getInstance().displayImage(
				Constant.getUserimage() + fan.getFollowuid() + "&size=big",
				uCircularImageView);
		unameTextView.setText(fan.getFusername());

		return convertView;
	}
}
