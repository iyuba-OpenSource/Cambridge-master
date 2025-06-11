package com.iyuba.camstory.adpater;

import java.util.ArrayList;
import java.util.List;

import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.sqlite.mode.ChatMessage;
import com.iyuba.camstory.sqlite.mode.MessageLetterContent;
import com.iyuba.camstory.utils.Emotion;
import com.iyuba.camstory.utils.ExpressionUtil;
import com.iyuba.voa.activity.setting.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChattingAdapter extends BaseAdapter {
	private static final String TAG = ChattingAdapter.class.getSimpleName();

	private Context context;
	private List<MessageLetterContent> mList = new ArrayList<MessageLetterContent>();
	private int uid;
	int i = 0;
	private String userId;
	private String fansId;
	private DisplayImageOptions mDisplayImageOptions = CrashApplication.getInstance()
			.getDefaultDisplayImageOptionsBuilder().showImageOnLoading(R.drawable.defaultavatar).build();

	public ChattingAdapter(Context context, List<MessageLetterContent> messages, int uid) {
		this.context = context;
		this.mList = messages;
		this.uid = uid;
	}

	public ChattingAdapter(Context context, int uid) {
		this.context = context;
		this.uid = uid;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	public void addList(ArrayList<MessageLetterContent> list) {
		mList.addAll(list);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearList() {
		mList.clear();
	}

	public void addLetter(MessageLetterContent letter) {
		mList.add(letter);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		MessageLetterContent message = mList.get(position);
		System.out.println("position===" + position + "   message==" + message.authorid);
		if (message.authorid == uid) {
			message.setDirection(1);
		} else {
			message.setDirection(0);
		}
		if (convertView == null
				|| (holder = (ViewHolder) convertView.getTag()).flag != message.direction) {
			holder = new ViewHolder();
			if (message.direction == ChatMessage.MESSAGE_FROM) {
				holder.flag = ChatMessage.MESSAGE_FROM;
				convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_from, null);
			} else {
				holder.flag = ChatMessage.MESSAGE_TO;
				convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_to, null);
			}
			holder.text = convertView.findViewById(R.id.chatting_content_itv);
			holder.time = convertView.findViewById(R.id.chatting_time_tv);
			holder.userImageView = convertView.findViewById(R.id.chatting_content_iv);
			convertView.setTag(holder);
		}
		String zhengze = "image[0-9]{2}|image[0-9]";
		Emotion emotion = new Emotion();
		message.message = Emotion.replace(message.message);
		try {
			SpannableString spannableString = ExpressionUtil.getExpressionString(context,
					message.message, zhengze);
			holder.text.setText(spannableString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// holder.text.setText(Html.fromHtml(replaceExpressPath(message.message),imageGetter,null));
		holder.time.setText(DateFormat.format("MM-dd kk:mm", Long.parseLong(message.dateline) * 1000));
		ImageLoader.getInstance().displayImage(
				Constant.getUserimage() + message.authorid + "&size=middle", holder.userImageView,
				mDisplayImageOptions);
		holder.userImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(context, PersonalHomeActivity.class);
				 * intent.putExtra("fanuid", mList.get(position).authorid);
				 * context.startActivity(intent);
				 */
			}

		});
		return convertView;
	}

	static class ViewHolder {
		TextView text;
		ImageView userImageView;
		TextView time;
		int flag;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;

			}
		}
	};
}
