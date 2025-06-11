package com.iyuba.camstory.lycam.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.living.CoursePackListBean;
import com.iyuba.camstory.bean.living.LivePackListBean;
import com.iyuba.camstory.bean.living.SlideShowListBean;
import com.iyuba.camstory.lycam.util.MatchCourseType;
import com.iyuba.configation.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.iyuba.iyulivelib.activity.LiveListActivity;


/**
 * 作者：renzhy on 16/7/7 16:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public class FindCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_CATEGORY = 1;
	private static final int TYPE_ITEM = 2;


	private Context mContext;
	private List<Integer> categoryList;
	private List<LivePackListBean.LivePackDataBean> liveList;
	private List<SlideShowListBean.SlideShowDataBean> adPicList;
	private OnItemClickListener onCBItemClickListener;
	private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

	public FindCourseAdapter(Context mContext) {
		this.mContext = mContext;
		categoryList = new ArrayList<>();
		liveList = new ArrayList<>();
		adPicList = new ArrayList<>();
	}

	public void setOnCBItemClickListener(OnItemClickListener onCBItemClickListener) {
		this.onCBItemClickListener = onCBItemClickListener;
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
		onRecyclerViewItemClickListener = onItemClickListener;
	}

	public void setCourseData(ArrayList<CoursePackListBean.CoursePackDataBean> courses) {
		notifyDataSetChanged();
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


	public void clearAdData() {
		adPicList.clear();
	}

	public void setAdData(ArrayList<SlideShowListBean.SlideShowDataBean> ads) {
		adPicList = ads;
		notifyItemChanged(0);
		notifyDataSetChanged();
	}

	public void clearCategoryData() {
		categoryList.clear();
	}

	public void setCategoryData(ArrayList<Integer> categories) {
		categoryList = categories;
		notifyItemChanged(1);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false));
		} else if (viewType == TYPE_CATEGORY) {
			return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false));
		} else if (viewType == TYPE_HEADER) {
			return new AdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			final LivePackListBean.LivePackDataBean livePack = getItem(position);
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null, endDate = null;

			try {
				startDate = sdf.parse(livePack.getStartDate());
				endDate = sdf.parse(livePack.getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (onRecyclerViewItemClickListener != null) {
				itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						onRecyclerViewItemClickListener.onItemClick(holder.itemView, pos - 2);
					}
				});
				itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						onRecyclerViewItemClickListener.onLongClick(holder.itemView, pos - 2);
						return true;
					}
				});
			}

			itemViewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			itemViewHolder.title.setText(livePack.getName());

			try {
				itemViewHolder.startDate.setText(sdf.format(startDate));
				itemViewHolder.endDate.setText(sdf.format(endDate));
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
		} else if (holder instanceof AdViewHolder) {
			AdViewHolder headerViewHolder = (AdViewHolder) holder;
			headerViewHolder.convenientBanner.setPages(headerViewHolder.adHolder, adPicList);
		} else if (holder instanceof CategoryViewHolder) {
			CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
			CourseCategoryAdapter courseCategoryAdapter = new CourseCategoryAdapter(mContext);
			courseCategoryAdapter.setImageData(categoryList);
			courseCategoryAdapter.setOnCategoryItemClickListener(new CourseCategoryAdapter.OnCategoryItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					/*mContext.startActivity(LiveListActivity.getIntent2Me(mContext,
							MatchCourseType.getInstance().getCourseTypeId(position),
							MatchCourseType.getInstance().getCourseTypeTitle(position)));*/
				}

				@Override
				public void onLongClick(View view, int position) {
				}
			});
			categoryViewHolder.recyclerView.setAdapter(courseCategoryAdapter);
			categoryViewHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
		}
	}

	@Override
	public int getItemCount() {
		return liveList.size() + 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else if (isPositionSecond(position)) {
			return TYPE_CATEGORY;
		}
		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	private boolean isPositionSecond(int position) {
		return position == 1;
	}

	private LivePackListBean.LivePackDataBean getItem(int position) {
		return liveList.get(position - 2);
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

	class CategoryViewHolder extends RecyclerView.ViewHolder {
		RecyclerView recyclerView;

		public CategoryViewHolder(View itemView) {
			super(itemView);
			recyclerView = itemView.findViewById(R.id.recyclerview_course_category);
		}
	}

	class LocalImageHolderView implements Holder<Integer> {
		private ImageView imageView;

		@Override
		public View createView(Context context) {
			imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			return imageView;
		}

		@Override
		public void UpdateUI(Context context, int position, Integer data) {
			imageView.setImageResource(data);
		}
	}

	class AdViewHolder extends RecyclerView.ViewHolder {
		ConvenientBanner convenientBanner;
		CBViewHolderCreator<NetworkImageHolderView> adHolder;

		public AdViewHolder(View view) {
			super(view);
			//添加轮播的动画效果(默认样式),新版不再支持ConvenientBanner.Transformer.XXX(样式)
//			ABaseTransformer transforemer = null;
//			try {
//				Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + DefaultTransformer.class.getSimpleName());
//				transforemer = (ABaseTransformer) cls.newInstance();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}

			convenientBanner = view.findViewById(R.id.header_convenientBanner);
			convenientBanner.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(int position) {
//					LivePackDataBean livePackDataBean
//							= new LivePackOp().findById(Integer.parseInt(adPicList.get(position).getId()));
//					if (livePackDataBean != null) {
//						mContext.startActivity(LiveContentActivity.getIntent2Me(
//								mContext, livePackDataBean));
//					}
				}
			});
			convenientBanner
					.startTurning(5000)
					.setPageIndicator(new int[]{R.drawable.dot_blur, R.drawable.dot_focus})
					.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
//					.setPageTransformer(transforemer);

			adHolder = new CBViewHolderCreator<NetworkImageHolderView>() {
				@Override
				public NetworkImageHolderView createHolder() {
					return new NetworkImageHolderView();
				}
			};
		}
	}

	class NetworkImageHolderView implements Holder<SlideShowListBean.SlideShowDataBean> {
		private ImageView imageView;
		private TextView textView;

		private View layout;

		@Override
		public View createView(Context context) {

			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.rollview_layout, null);
			imageView = view.findViewById(R.id.roll_view_image);
			textView = view.findViewById(R.id.roll_view_title);
			return view;
		}

		@Override
		public void UpdateUI(final Context context, final int position, final SlideShowListBean.SlideShowDataBean data) {
			ImageLoader.getInstance().displayImage("http://app."+Constant.IYBHttpHead+"/dev/" + data.getPic(), imageView);
			textView.setText(data.getName());
		}
	}
}
