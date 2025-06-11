package com.iyuba.camstory.lycam.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.lycam.util.MatchCourseType;
import com.iyuba.configation.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：renzhy on 16/7/7 16:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context mContext;
	private List<LivePackListBean.LivePackDataBean> liveList;
	private OnItemClickListener onCBItemClickListener;
	private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

	public CourseListAdapter(Context mContext) {
		this.mContext = mContext;
		liveList = new ArrayList<>();
	}

	public void setOnCBItemClickListener(OnItemClickListener onCBItemClickListener) {
		this.onCBItemClickListener = onCBItemClickListener;
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
		onRecyclerViewItemClickListener = onItemClickListener;
	}

	public void setLiveData(ArrayList<LivePackListBean.LivePackDataBean> lives) {
		liveList = lives;
		notifyDataSetChanged();
	}

	public void addList(List<LivePackListBean.LivePackDataBean> live) {
		liveList.addAll(live);
		notifyDataSetChanged();
	}

	public void clearList() {
		liveList.clear();
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false));

	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			final LivePackListBean.LivePackDataBean livePack = getItem(position);
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null, endDate = null;

			try {
				if (livePack.getStartDate() != null && livePack.getEndDate() != null) {
					startDate = sdf.parse(livePack.getStartDate());
					endDate = sdf.parse(livePack.getEndDate());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (onRecyclerViewItemClickListener != null) {
				itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						onRecyclerViewItemClickListener.onItemClick(holder.itemView, pos);
					}
				});
				itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						onRecyclerViewItemClickListener.onLongClick(holder.itemView, pos);
						return true;
					}
				});
			}

			itemViewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			itemViewHolder.title.setText(livePack.getName());

			try {
				if (startDate != null && endDate != null) {
					itemViewHolder.startDate.setText(sdf.format(startDate));
					itemViewHolder.endDate.setText(sdf.format(endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			itemViewHolder.oldPrice.setText("￥" + livePack.getRealprice());
			itemViewHolder.newPrice.setText("￥" + livePack.getPrice());

			DecimalFormat df = new DecimalFormat("#.0");
			itemViewHolder.courseType.setText(MatchCourseType.getInstance()
					.getCourseTypeName(Integer.parseInt(livePack.getOwnerid())));
			itemViewHolder.applyNum.setText(livePack.getStudentNum() + "人");
			ImageLoader.getInstance().displayImage("http://static3."+Constant.IYBHttpHead+"/resource/packIcon/" + livePack.getPic() + ".jpg", itemViewHolder.pic);
		}
	}

	@Override
	public int getItemCount() {
		return liveList.size();
	}

	private LivePackListBean.LivePackDataBean getItem(int position) {
		return liveList.get(position);
	}

	// 用来设置每个item的接听
	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);

		void onLongClick(View view, int position);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		public TextView title;
		public TextView startDate;
		public TextView endDate;
		public TextView applyNum;
		public TextView courseType;
		public ImageView pic;

		public TextView oldPrice;
		public TextView newPrice;

		public ItemViewHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.livePack_title);
			startDate = itemView.findViewById(R.id.livePackDesc_start_date);
			endDate = itemView.findViewById(R.id.livePackDesc_end_date);
			applyNum = itemView.findViewById(R.id.tv_livePack_applyNum);
			courseType = itemView.findViewById(R.id.tv_livepack_type);
			pic = itemView.findViewById(R.id.livePack_pic);

			oldPrice = itemView.findViewById(R.id.tv_livePack_oldprice);
			newPrice = itemView.findViewById(R.id.tv_livePack_newprice);
		}
	}
}
