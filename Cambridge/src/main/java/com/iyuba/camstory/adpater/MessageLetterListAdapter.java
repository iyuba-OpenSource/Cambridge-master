/**
 * 
 */
package com.iyuba.camstory.adpater;
import java.util.ArrayList;

import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.utils.TimeUtil;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author  私信列表Adapter
 */
public class MessageLetterListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MessageLetter> mList = new ArrayList<MessageLetter>();
	private ViewHolder1 viewHolder1;
	private ViewHolder2 viewHolder2;
	private final int TYPE_1 = 0;
	private final int TYPE_2 = 1;
	int i = 0;
	private String userId;
	private DisplayImageOptions mDisplayImageOptions = CrashApplication.getInstance()
			.getDefaultDisplayImageOptionsBuilder()
			.showImageOnLoading(R.mipmap.defaultavatar).build();
	/**
	 * @param mContext
	 */
	public MessageLetterListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @param mContext
	 * @param mList
	 */
	public MessageLetterListAdapter(Context mContext,
			ArrayList<MessageLetter> mList) {
		this.mContext = mContext;
		this.mList = mList;
		//handler.sendEmptyMessage(1);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		if (position == 0) {
			return 0;
		} else {
			return mList.get(position + 1);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position + 1;
	}

	/*public void clearList() {
		mList.clear();
	}*/

	public void addList(ArrayList<MessageLetter> List) {
		mList.clear();
		mList.addAll(List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		// 无convertView，需要new出各个控件
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (type) {
			case TYPE_1:
				convertView = layoutInflater.inflate(
							R.layout.button_writeletter, null);
				convertView.setPadding(0, 12, 0, 12);
				viewHolder1 = new ViewHolder1();
				viewHolder1.button_writeletter = convertView
						.findViewById(R.id.writeletterlayout);
				convertView.setTag(viewHolder1);
				break;
			case TYPE_2:
				convertView = layoutInflater.inflate(
						R.layout.messageletter_item, null);
				viewHolder2 = new ViewHolder2();
				viewHolder2.messageletter_portrait = convertView
						.findViewById(R.id.messageletter_portrait);
				viewHolder2.messageletter_username = convertView
						.findViewById(R.id.messageletter_username);
				viewHolder2.messageletter_pmnum = convertView
						.findViewById(R.id.messageletter_pmnum);
				viewHolder2.messageletter_lastmessage = convertView
						.findViewById(R.id.messageletter_lastmessage);
				viewHolder2.messageletter_dateline = convertView
						.findViewById(R.id.messageletter_dateline);
				viewHolder2.isNew= convertView.findViewById(R.id.isNew);
				convertView.setTag(viewHolder2);
				break;
			}
		} else {
			switch (type) {
			case TYPE_1:
				viewHolder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				viewHolder2 = (ViewHolder2) convertView.getTag();			
				break;
			default:
				break;
			}

		}
		// 设置资源
		switch (type) {
		case TYPE_1:
			break;
		case TYPE_2:
			viewHolder2.messageletter_lastmessage.setText(Html.fromHtml(
					replaceExpressPath(mList.get(position - 1).lastmessage),
					imageGetter, null));
			viewHolder2.messageletter_username
					.setText(mList.get(position - 1).name);
			CharSequence date="";
			if (TimeUtil.isSameYear(
					Long.parseLong(mList.get(position - 1).dateline) * 1000,
					System.currentTimeMillis())) {
				date = DateFormat.format(
								"MM-dd kk:mm",
								Long.parseLong(mList.get(position - 1).dateline) * 1000);
			}
			else {
				date = DateFormat.format(
						"yyyy-MM-dd kk:mm",
						Long.parseLong(mList.get(position - 1).dateline) * 1000);
			}
			viewHolder2.messageletter_dateline.setText(date);
//					"MM-dd kk:mm",
//					Long.parseLong(mList.get(position - 1).dateline) * 1000));
			viewHolder2.messageletter_pmnum.setText("["
					+ mList.get(position - 1).pmnum + "]");
			//显示图片
			ImageLoader.getInstance().displayImage(Constant.getUserimage()
										+ mList.get(position - 1).friendid+"&size=middle"
										, viewHolder2.messageletter_portrait
										, mDisplayImageOptions);
/*			if(mList.get(position - 1).userBitmap==null){
				viewHolder2.messageletter_portrait.setBackgroundResource(R.drawable.defaultavatar);
			}else{
				viewHolder2.messageletter_portrait.setImageBitmap(mList
						.get(position - 1).userBitmap);
			}	*/
			if(mList.get(position - 1).isnew.equals("1")){//未读
				viewHolder2.isNew.setVisibility(View.VISIBLE);
			}else if(mList.get(position - 1).isnew.equals("0")){//已读
				viewHolder2.isNew.setVisibility(View.GONE);
			}
			break;

		}
		return convertView;
	}

	class ViewHolder1 {
		LinearLayout button_writeletter;
	}

	public class ViewHolder2 {
		ImageView messageletter_portrait;
		TextView messageletter_username;
		TextView messageletter_pmnum;
		TextView messageletter_dateline;
		TextView messageletter_lastmessage;
		public	ImageView isNew;
	}
	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = position;
		if (p == 0) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			source = source.substring(source.lastIndexOf("/") + 1);
			source = "/mnt/sdcard/com.iyuba.iyubaclient/comcom/" + source;
			drawable = Drawable.createFromPath(source);
			drawable.setBounds(0, 0, 30, 30);
			return drawable;
		}
	};

	private String replaceExpressPath(String str) {
		// TODO Auto-generated method stub
		str = str.replaceAll("(.*?)src=\"(.*?)", "$1src=\"http://"+Constant.IYBHttpHead+"/$2");
		return str;
	}

/*	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCard.hasSDCard()) {
					for (i = 0; i < mList.size(); i++) {
						mList.get(i).userBitmap = imageLoder
								.getBitmap(Constant.getUserimage()
										+ mList.get(i).friendid+"&size=middle");
					}
					handler.sendEmptyMessage(0);
				}
			}
		};
		t.start();
	}*/
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
/*			case 1:
				DownLoadUserImg();
				break;*/
			}
		}
	};
}
