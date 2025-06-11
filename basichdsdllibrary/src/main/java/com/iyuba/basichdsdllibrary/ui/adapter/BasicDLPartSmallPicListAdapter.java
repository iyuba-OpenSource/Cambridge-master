package com.iyuba.basichdsdllibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.basichdsdllibrary.R;
import com.iyuba.basichdsdllibrary.db.BasicHDsDLPart;
import com.iyuba.basichdsdllibrary.util.BasicHeadlineJudgeAudioCatg;
import com.iyuba.basichdsdllibrary.util.BasicHeadlineJudgeVideoCatg;

import java.util.List;

/**
 * 作者：renzhy on 17/2/16 17:29
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicDLPartSmallPicListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BasicDLPartSmallPicListAdapter";
    private boolean ifshowchoose = false;
    private List<BasicHDsDLPart> basicHDsDLPartList;
    private List<BasicHDsDLPart> mdeleteBdsList;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnItemCheckedListener onItemCheckedListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic_part_small_pic_headlines_in, parent, false);
        return new BasicHeadlinesViewHolder(view);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        onRecyclerViewItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String imageUrl;
        final BasicHeadlinesViewHolder viewHolder = (BasicHeadlinesViewHolder) holder;
        final BasicHDsDLPart basicHDsDLPart = basicHDsDLPartList.get(position);

        viewHolder.tv_title_cn.setText(basicHDsDLPart.getTitle());
        viewHolder.tv_title_en.setText(basicHDsDLPart.getTitle_cn());
        imageUrl = basicHDsDLPart.getPic();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.basic_headlines_loading)
                .crossFade()
                .into(viewHolder.iv_headlines);
        if (ifshowchoose) {
            viewHolder.cb_ischoose.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cb_ischoose.setVisibility(View.GONE);
            viewHolder.cb_ischoose.setChecked(false);
        }
        viewHolder.tv_headlines_category_name.setText(basicHDsDLPart.getCategoryName());
        if (basicHDsDLPart.getCreateTime() != null && basicHDsDLPart.getCreateTime().length() >= 10) {
            viewHolder.tv_headlines_date.setText(basicHDsDLPart.getCreateTime().substring(0, 10));
        }
        if (basicHDsDLPart.getType() != null) {
            viewHolder.iv_headlines_cate_sign.setImageResource(getMediaTypeSign(basicHDsDLPart.getType()));
        }

        viewHolder.cb_ischoose.setOnCheckedChangeListener(null);
        viewHolder.cb_ischoose.setChecked(mdeleteBdsList.contains(basicHDsDLPart));
        viewHolder.cb_ischoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.onItemChecked(isChecked, position);
                }
            }
        });

        if (onRecyclerViewItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ifshowchoose) {
                        int pos = holder.getLayoutPosition();
                        onRecyclerViewItemClickListener.onItemClick(holder.itemView, pos);
                    } else {
                        viewHolder.ischeck = !viewHolder.ischeck;
                        viewHolder.cb_ischoose.setChecked(viewHolder.ischeck);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ifshowchoose) {
                        int pos = holder.getLayoutPosition();
                        onRecyclerViewItemClickListener.onLongClick(holder.itemView, pos);
                    } else {
                        viewHolder.ischeck = !viewHolder.ischeck;
                        viewHolder.cb_ischoose.setChecked(viewHolder.ischeck);
                    }
                    return true;
                }
            });
        }

    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public void setIfshowchoose(boolean ifshowchoose) {
        this.ifshowchoose = ifshowchoose;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return basicHDsDLPartList == null ? 0 : basicHDsDLPartList.size();
    }

    public void setItems(List<BasicHDsDLPart> basicHDsDLParts) {
        this.basicHDsDLPartList = basicHDsDLParts;
        notifyDataSetChanged();
    }

    public void setDeleteBdsList(List<BasicHDsDLPart> mdeleteBdsList) {
        this.mdeleteBdsList = mdeleteBdsList;
    }

    public void clearItems() {
        if (basicHDsDLPartList != null && basicHDsDLPartList.size() > 0) {
            basicHDsDLPartList.clear();
            notifyDataSetChanged();
        }
    }

    public int getMediaTypeSign(String typeName) {
        if (BasicHeadlineJudgeVideoCatg.isVideoCatg(typeName)) {
            return R.drawable.basic_headlines_cate_video;
        }
        if (BasicHeadlineJudgeAudioCatg.isAudioCatg(typeName)) {
            return R.drawable.basic_headlines_cate_audio;
        }
        return R.drawable.basic_headlines_cate_text;

    }

    public void addItems(List<BasicHDsDLPart> basicHDsDLParts) {
        this.basicHDsDLPartList.addAll(basicHDsDLParts);
        notifyDataSetChanged();
    }

    // 用来设置每个item的接听
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface OnItemCheckedListener {
        void onItemChecked(boolean ischecked, int position);
    }

    static class BasicHeadlinesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_cn;
        ImageView iv_headlines;
        TextView tv_headlines_category_name;
        TextView tv_headlines_date;
        ImageView iv_headlines_cate_sign;
        TextView tv_title_en;
        CheckBox cb_ischoose;
        View headlines_item;
        boolean ischeck = false;

        public BasicHeadlinesViewHolder(View itemView) {
            super(itemView);
            tv_title_cn = itemView.findViewById(R.id.tv_basic_headlines_title_cn);
            iv_headlines = itemView.findViewById(R.id.iv_basic_headlines);
            tv_headlines_category_name = itemView.findViewById(R.id.tv_basic_headlines_category_name);
            tv_headlines_date = itemView.findViewById(R.id.tv_basic_headlines_date);
            iv_headlines_cate_sign = itemView.findViewById(R.id.iv_basic_headlines_cate);
            tv_title_en = itemView.findViewById(R.id.tv_basic_headlines_title_en);
            cb_ischoose = itemView.findViewById(R.id.is_choose);
            headlines_item = itemView.findViewById(R.id.headlines_dllitem_layout);
        }
    }
}
