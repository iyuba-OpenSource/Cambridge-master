package com.iyuba.camstory.adpater;

import java.util.ArrayList;


import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.FansBean;
import com.iyuba.camstory.sqlite.mode.Fans;
import com.iyuba.camstory.widget.CircularImageView;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FansGridViewAdapter extends BaseAdapter {

	private ArrayList<FansBean.DataBean> fans = new ArrayList<FansBean.DataBean>();
	private LayoutInflater mInflater;
	private ViewHolder holder;

	public FansGridViewAdapter(Context context, ArrayList<FansBean.DataBean> list) {
		// TODO 自动生成的构造函数存根
		if (context != null) {
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (list != null) {
			fans = list;
		}
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return fans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return fans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		FansBean.DataBean fan = fans.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_fans, null);
			holder = new ViewHolder();
			holder.uCircularImageView = convertView
					.findViewById(R.id.user_image);
			holder.unameTextView = convertView
					.findViewById(R.id.username_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(
				Constant.getUserimage() + fan.getUid() + "&size=big",
				holder.uCircularImageView);
		holder.unameTextView.setText(fan.getUsername());
		return convertView;
	}

	class ViewHolder {
		public CircularImageView uCircularImageView;
		public TextView unameTextView;
	}

}
