package com.iyuba.camstory.adpater;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.widget.RoundProgressBar;
import com.iyuba.camstory.widget.RoundProgressBar2;

import java.util.List;

public class ChapterListAdapter extends BaseAdapter {

    public static final String TAG = ChapterListAdapter.class.getSimpleName();
    private List<BookDetailResponse.ChapterInfo> chapters;
    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder viewHolder;

    private int pos = 0;

    public ChapterListAdapter(Context paramContext, List<BookDetailResponse.ChapterInfo> paramArrayList) {
        mContext = paramContext;
        chapters = paramArrayList;
        if (this.mContext != null)
            this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chapters.size();

    }

    @Override
    public Object getItem(int position) {
        return this.chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_chapterinfo, null);
            viewHolder = new ViewHolder();
            viewHolder.indicator = convertView.findViewById(R.id.indicator);
            viewHolder.textView = convertView.findViewById(R.id.chapter_text);
            viewHolder.linearLayoutCompat = convertView.findViewById(R.id.progress_layout);
            viewHolder.tvSoundProgress = convertView.findViewById(R.id.tv_sound_progress);
            viewHolder.tvEvlProgress = convertView.findViewById(R.id.tv_evaluate_progress);
            viewHolder.imgSoundProgress = convertView.findViewById(R.id.img_sound_progress);
            viewHolder.imgEvlProgress = convertView.findViewById(R.id.img_evaluate_progress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BookDetailResponse.ChapterInfo chapter = chapters.get(position);
        this.viewHolder.textView.setText(chapter.getCname_en()+"/"+chapter.getCname_cn());
        viewHolder.tvSoundProgress.setText(chapter.getSoundProgress());
        viewHolder.tvEvlProgress.setText(chapter.getEvaProgress());

        viewHolder.imgEvlProgress.setMax(100);
        viewHolder.imgSoundProgress.setMax(100);
        viewHolder.imgEvlProgress.setProgress(Math.round(Float.valueOf(chapter.getEvaProgress().replace("%",""))));
        viewHolder.imgSoundProgress.setProgress(Math.round(Float.valueOf(chapter.getSoundProgress().replace("%",""))));

        if(!chapter.getEvaProgress().equals("0%")){
            viewHolder.imgEvlProgress.setBackgroundResource(R.drawable.ic_evaluate_progress_green);
        }else{
            viewHolder.imgEvlProgress.setBackgroundResource(R.drawable.ic_evaluate_progress);
        }

        if(!chapter.getSoundProgress().equals("0%")){
            viewHolder.imgSoundProgress.setBackgroundResource(R.drawable.ic_sound_progress_green);
        }else{
            viewHolder.imgSoundProgress.setBackgroundResource(R.drawable.ic_sound_progress);
        }


        if (pos == position) {
            viewHolder.indicator.setVisibility(View.VISIBLE);
            viewHolder.textView.setTextColor(Color.parseColor("#ff7eaf2f"));
        } else {
            viewHolder.indicator.setVisibility(View.INVISIBLE);
            viewHolder.textView.setTextColor(Color.BLACK);
        }

        // TODO: 2025/1/13 这里处理下，不再显示进度
        if(!chapter.isShowProgress()){
            viewHolder.linearLayoutCompat.setVisibility(View.GONE);
        }else{
            viewHolder.linearLayoutCompat.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static class ViewHolder {
        ImageView indicator;
        TextView textView;

        LinearLayoutCompat linearLayoutCompat;

        RoundProgressBar2 imgSoundProgress;
        RoundProgressBar2 imgEvlProgress;

        TextView tvSoundProgress;
        TextView tvEvlProgress;
    }

    public void setCurPos(int pos){
        this.pos = pos;
        notifyDataSetChanged();
    }
}
