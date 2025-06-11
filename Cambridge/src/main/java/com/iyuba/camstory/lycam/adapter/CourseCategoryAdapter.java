package com.iyuba.camstory.lycam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.camstory.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 16/7/7 19:07
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CourseCategoryAdapter extends RecyclerView.Adapter<CourseCategoryAdapter.CategoryImageHolder> {

	private Context mContext;
	private List<Integer> localImages;

	private OnCategoryItemClickListener onCategoryItemClickListener;
	DisplayImageOptions options;

	public CourseCategoryAdapter(Context mContext) {
		this.mContext = mContext;
		localImages = new ArrayList<>();
		setImageLoaderOptions();
	}

	public void setOnCategoryItemClickListener(OnCategoryItemClickListener onCategoryItemClickListener) {
		this.onCategoryItemClickListener = onCategoryItemClickListener;
	}

	public void setImageData(List<Integer> images) {
		localImages = images;
	}

	@Override
	public CategoryImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		return new CategoryImageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category_image, parent, false));
	}

	@Override
	public void onBindViewHolder(final CategoryImageHolder holder, int position) {
		holder.imageView.setImageResource(localImages.get(position));
		if (onCategoryItemClickListener != null) {
			holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = holder.getLayoutPosition();
					onCategoryItemClickListener.onItemClick(holder.itemView, pos);
				}
			});
			holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					int pos = holder.getLayoutPosition();
					onCategoryItemClickListener.onLongClick(holder.itemView, pos);
					return true;
				}
			});
		}
		ImageLoader.getInstance()
				.displayImage("drawable://" + localImages.get(position), holder.imageView, options);
	}

	@Override
	public int getItemCount() {
		return localImages.size();
	}

	// 用来设置每个item的接听
	public interface OnCategoryItemClickListener {
		void onItemClick(View view, int position);

		void onLongClick(View view, int position);
	}

	class CategoryImageHolder extends RecyclerView.ViewHolder {

		ImageView imageView;

		public CategoryImageHolder(View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.iv_course_category);
		}
	}

	private void setImageLoaderOptions() {

		options = new DisplayImageOptions.Builder()
				.displayer(new RoundedBitmapDisplayer(10)) //rounded corner bitmap
				.cacheInMemory(false)
				.cacheOnDisc(false)
				.build();
	}
}
