/**
 * 
 */
package com.iyuba.camstory.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestAddAttention;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.sqlite.mode.SearchItem;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.RoundAngleImageView;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * @author
 *
 */
public class SearchResultAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<SearchItem> mList=new ArrayList<SearchItem>();
	private ViewHolder viewHolder;
	int i = 0;
	private String userId;
	public SearchResultAdapter(Context mContext,ArrayList<SearchItem> list) {
		this.mContext = mContext;
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
		return position ;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// TODO Auto-generated method stub
		final int  pos=position;
		if(convertView==null){
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.searchlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.username = convertView
					.findViewById(R.id.searchlist_username);
			viewHolder.userImageView = convertView
					.findViewById(R.id.searchlist_portrait);
			viewHolder.followButton = convertView.findViewById(R.id.follow_button);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.username.setText(mList.get(position).username);
		ImageLoader.getInstance().displayImage(Constant.getUserimage()
				+ mList.get(i).uid+"&size=middle", viewHolder.userImageView);
		viewHolder.followButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Log.d("clickname", mList.get(pos).username);
				RequestAddAttention request = new RequestAddAttention(AccountManager.getInstance().userId, mList.get(pos).uid
						, new RequestCallBack() {
					@Override
					public void requestResult(Request result) {
						// TODO 自动生成的方法存根
						RequestAddAttention rs = (RequestAddAttention)result;
						if (rs.isRequestSuccessful()) {
							handler.sendEmptyMessage(2);
						}
						else if (rs.isAttentioned()) {
							handler.sendEmptyMessage(3);
						}
						else {
							handler.sendEmptyMessage(4);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(request);
			}
		});
		
		return convertView;
	}

	class ViewHolder{
		TextView username;
		RoundAngleImageView userImageView;
		Button followButton;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
			case 1:
				break;
				case 2:
					CustomToast.showToast(mContext,R.string.social_success_attention, 1000);
					break;
				case 3:
					CustomToast.showToast(mContext, R.string.social_attentioned, 1000);
					break;
				case 4:
					CustomToast.showToast(mContext, R.string.social_failed_attention, 1000);
					break;
		}}
	};
	
}
