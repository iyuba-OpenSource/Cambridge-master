package com.iyuba.camstory.lycam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.LiveContentBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 作者：renzhy on 16/7/25 21:27
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CourseContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private Context mContext;
	private List<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailList;
	private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

	public CourseContentAdapter(Context mContext) {
		this.mContext = mContext;
		liveDetailList = new ArrayList<>();
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
		onRecyclerViewItemClickListener = onItemClickListener;
	}

	public void setLiveDetailData(ArrayList<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailList) {
		this.liveDetailList = liveDetailList;
		notifyDataSetChanged();
	}

	public void clearList() {
		liveDetailList.clear();
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ContentItemView(LayoutInflater.from(mContext).inflate(R.layout.item_live_detail,parent,false));
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		final ContentItemView contentItemView = (ContentItemView) holder;
		final LiveContentBean.LiveTitleBean.LiveDetailBean liveDetail = liveDetailList.get(position);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = null;
		Date currentDate = null;

		currentDate = new Date(System.currentTimeMillis());

		if (onRecyclerViewItemClickListener != null) {
			contentItemView.btnLive.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = holder.getLayoutPosition();
					onRecyclerViewItemClickListener.onItemClick(holder.itemView, pos);
				}
			});
			contentItemView.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = holder.getLayoutPosition();
					onRecyclerViewItemClickListener.onItemClick(holder.itemView, pos);
				}
			});
			contentItemView.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int pos = holder.getLayoutPosition();
					onRecyclerViewItemClickListener.onLongClick(holder.itemView, pos);
					return true;
				}
			});
		}

		try {
			if(liveDetail.getStartDate() != null){
				startDate = sdf.parse(liveDetail.getStartDate());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"font/ZHS.ttf");
		contentItemView.detailOrder.setTypeface(typeface);

		if(position < 9){
			contentItemView.detailOrder.setText("0"+(position+1));
		}else {
			contentItemView.detailOrder.setText((position+1)+"");
		}
		contentItemView.detailName.setText(liveDetail.getTitleName());
		if(startDate != null){
			//显示课程开始时间
			contentItemView.detailStartDate.setText(sdf.format(startDate));
			if(currentDate != null){
				//课程已经开始
				if(currentDate.after(startDate)){
					contentItemView.detailWaitting.setVisibility(View.GONE);
					contentItemView.btnLive.setVisibility(View.VISIBLE);
				}else {
					contentItemView.detailWaitting.setVisibility(View.VISIBLE);
					contentItemView.btnLive.setVisibility(View.GONE);
				}
				contentItemView.btnRecord.setVisibility(View.GONE);
			}else {
				contentItemView.detailWaitting.setVisibility(View.GONE);
				contentItemView.btnLive.setVisibility(View.GONE);
				contentItemView.btnRecord.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public int getItemCount() {
		return liveDetailList.size();
	}

	// 用来设置每个item的接听
	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);

		void onLongClick(View view, int position);
	}

	public class ContentItemView extends RecyclerView.ViewHolder{

		public TextView detailOrder;
		public TextView detailName;
		public TextView detailStartDate;
		public TextView detailWaitting;

		public Button btnLive;
		public Button btnRecord;

		public ContentItemView(View itemView) {
			super(itemView);
			detailOrder = itemView.findViewById(R.id.tv_item_live_order);
			detailName = itemView.findViewById(R.id.tv_live_detail_name);
			detailStartDate = itemView.findViewById(R.id.tv_live_detail_starttime);
			detailWaitting = itemView.findViewById(R.id.tv_live_waitting);
			btnLive = itemView.findViewById(R.id.btn_live);
			btnRecord = itemView.findViewById(R.id.btn_record);
		}
	}
}
