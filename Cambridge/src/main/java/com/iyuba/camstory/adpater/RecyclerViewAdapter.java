package com.iyuba.camstory.adpater;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.lil.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshuai on 2018/2/2.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private double currentData = 0d;
    private int playPosition;
    private OnItemClickListener mOnItemClickListener;
    public int temp = 0;
    private int mPosition = 0;
    public boolean isTranslate =true;

    private List<BookContentResponse.Texts> texts = new ArrayList<>();

    public int getTemp() {
        return temp;
    }

    public RecyclerViewAdapter() {
        this.mData = new ArrayList<>();

    }

    public void setSentence(List<String> ens) {
        mData.clear();
        mData.addAll(ens);
        notifyDataSetChanged();
    }

    public void setTexts(List<BookContentResponse.Texts> texts) {

        this.texts=texts;
        notifyDataSetChanged();
    }


    public void updateData(ArrayList<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setTranslate(boolean isTranslate){
        this.isTranslate =isTranslate;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_itemlayout, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position ==playPosition-1) {
            holder.tvEn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.story_text));
            holder.tvCn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.story_text));

            if (mPosition <= position){
                mPosition = position;
            }
        } else {
            holder.tvEn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorText));
            holder.tvCn.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorText));
        }

        if(!TextUtils.isEmpty(texts.get(position).getImage())){
            GlideUtil.loadImg(CrashApplication.getInstance(),"http://static2.iyuba.cn"+texts.get(position).getImage(),0,holder.img);
            holder.img.setVisibility(View.VISIBLE);
        }else{
            holder.img.setVisibility(View.GONE);
        }
        holder.tvEn.setText(texts.get(position).getTextEN());
        holder.tvCn.setText(texts.get(position).getTextCH());
        if (isTranslate) {
            holder.tvCn.setVisibility(View.VISIBLE);
        }else {
            holder.tvCn.setVisibility(View.INVISIBLE);
        }
        temp = position;
    }

    public int getPosition(){
        return mPosition;
    }

    @Override
    public int getItemCount() {
        if(texts != null){
            return texts.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCn;
        TextView tvEn;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEn = itemView.findViewById(R.id.sentence_en);
            tvCn = itemView.findViewById(R.id.sentence_cn);
            img = itemView.findViewById(R.id.img);
        }
    }

    public void changeColors(double current,int position) {
        currentData = current; //现在的数据
        playPosition = position;
        notifyDataSetChanged();
    }
    public void addOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);

        void onScrollChange(int position);
    }
}
