package com.iyuba.basichdsfavorlibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.basichdsfavorlibrary.R;
import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;
import com.iyuba.basichdsfavorlibrary.util.BasicHeadlineJudgeAudioCatg;
import com.iyuba.basichdsfavorlibrary.util.BasicHeadlineJudgeVideoCatg;

import java.util.List;

/**
 * 作者：renzhy on 17/2/16 17:29
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicFavorPartSmallPicListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BasicDLPartSmallPicListAdapter";

    List<BasicHDsFavorPart> basicHDsFavorPartList;
    private List<BasicHDsFavorPart> mdeletecollects;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private boolean ifshowcheckbox = false;
    private OnItemCheckedListener onItemCheckedListener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic_part_small_pic_headlines_in,parent,false);
        return new BasicHeadlinesViewHolder(view);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        onRecyclerViewItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String imageUrl;
        final BasicHeadlinesViewHolder viewHolder = (BasicHeadlinesViewHolder) holder;
        final BasicHDsFavorPart basicHDsFavorPart = basicHDsFavorPartList.get(position);

        viewHolder.tv_title_cn.setText(basicHDsFavorPart.getTitle());

        imageUrl = basicHDsFavorPart.getPic();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.basic_headlines_loading)
                .crossFade()
                .into(viewHolder.iv_headlines);
        viewHolder.tv_title_en.setText(basicHDsFavorPart.getTitle_cn());
        viewHolder.tv_headlines_category_name.setText(basicHDsFavorPart.getCategoryName());
        if(ifshowcheckbox){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(mdeletecollects.contains(basicHDsFavorPart));
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ifshowcheckbox){
                    if(onItemCheckedListener!=null)
                    {
                        onItemCheckedListener.OnItemChecked(isChecked,position);
                    }
                }
            }
        });
        if(basicHDsFavorPart.getCreateTime() != null && basicHDsFavorPart.getCreateTime().length() >= 10){
            viewHolder.tv_headlines_date.setText(basicHDsFavorPart.getCreateTime().substring(0, 10));
        }
        if (basicHDsFavorPart.getType() != null) {
            viewHolder.iv_headlines_cate_sign.setImageResource(getMediaTypeSign(basicHDsFavorPart.getType()));
        }

        if(onRecyclerViewItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ifshowcheckbox){
                        int pos = holder.getLayoutPosition();
                        onRecyclerViewItemClickListener.onItemClick(holder.itemView,pos);
                    }else {
                        viewHolder.ischeck = !viewHolder.ischeck;
                        viewHolder.checkBox.setChecked(viewHolder.ischeck);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(!ifshowcheckbox){
                        int pos = holder.getLayoutPosition();
                        onRecyclerViewItemClickListener.onLongClick(holder.itemView,pos);
                    }
                    else {
                        viewHolder.ischeck = !viewHolder.ischeck;
                        //viewHolder.cb_favor.setChecked(viewHolder.ischeck);
                    }
                    return true;
                }
            });
        }
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public void setIfshowcheckbox(boolean ifshowcheckbox) {
        this.ifshowcheckbox = ifshowcheckbox;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return basicHDsFavorPartList == null ? 0 : basicHDsFavorPartList.size();
    }

    public void setItems(List<BasicHDsFavorPart> basicHDsFavorParts){
        this.basicHDsFavorPartList = basicHDsFavorParts;
        notifyDataSetChanged();
    }

    public void setMdeletecollects(List<BasicHDsFavorPart> mdeletecollects) {
        this.mdeletecollects = mdeletecollects;
    }

    public void clearItems(){
        if(basicHDsFavorPartList != null && basicHDsFavorPartList.size() > 0){
            basicHDsFavorPartList.clear();
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

    public void addItems(List<BasicHDsFavorPart> basicHDsFavorParts){
        this.basicHDsFavorPartList.addAll(basicHDsFavorParts);
         notifyDataSetChanged();
    }

    // 用来设置每个item的接听
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface OnItemCheckedListener{
        void OnItemChecked(boolean ischeck,int position);
    }

    static class BasicHeadlinesViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title_cn;
        ImageView iv_headlines;
        TextView tv_headlines_category_name;
        TextView tv_headlines_date;
        ImageView iv_headlines_cate_sign;
        TextView tv_title_en;
        CheckBox checkBox;
        boolean ischeck = false;

        public BasicHeadlinesViewHolder(View itemView) {
            super(itemView);
            tv_title_en = itemView.findViewById(R.id.tv_basic_headlines_title_en);
            tv_title_cn = itemView.findViewById(R.id.tv_basic_headlines_title_cn);
            iv_headlines = itemView.findViewById(R.id.iv_basic_headlines);
            checkBox = itemView.findViewById(R.id.is_choose);
            tv_headlines_category_name = itemView.findViewById(R.id.tv_basic_headlines_category_name);
            tv_headlines_date = itemView.findViewById(R.id.tv_basic_headlines_date);
            iv_headlines_cate_sign = itemView.findViewById(R.id.iv_basic_headlines_cate);
        }
    }
}
