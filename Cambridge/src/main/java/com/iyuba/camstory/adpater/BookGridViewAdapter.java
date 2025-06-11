package com.iyuba.camstory.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.sqlite.op.BookInfo;

import java.util.ArrayList;
import java.util.List;

public class BookGridViewAdapter extends BaseAdapter {
    private ViewHolder holder;
    private LayoutInflater inflater;
    private Context mContext;
    private List<BookListResponse.BookInfo> mList;
    public List<BookListResponse.BookInfo> mDeleteList;

    public boolean isDelete;

    public BookGridViewAdapter(Context paramContext, List<BookListResponse.BookInfo> paramArrayList) {
        mContext = paramContext;
        mList = paramArrayList;
        mDeleteList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();

    }

    @Override
    public Object getItem(int paramInt) {
        return mList.get(paramInt);
    }

    @Override
    public long getItemId(int paramInt) {
        return paramInt;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final BookListResponse.BookInfo localBookInfo = this.mList.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.griditem_bookinfo, null);

            this.holder = new ViewHolder();

            this.holder.title_cn_TextView = convertView
                    .findViewById(R.id.booktitle_cn);
            this.holder.title_en_TextView = convertView
                    .findViewById(R.id.booktitle_en);
            this.holder.author_TextView = convertView
                    .findViewById(R.id.book_author);
            this.holder.check_box = convertView
                    .findViewById(R.id.check_box);
            this.holder.mBook = convertView
                    .findViewById(R.id.ll_book);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (localBookInfo != null) {
            holder.title_cn_TextView.setText(localBookInfo.getBookname_cn());
            holder.title_en_TextView.setText(localBookInfo.getBookname_en());
            holder.author_TextView.setText(localBookInfo.getAuthor());
            if (isDelete) {
                holder.check_box.setVisibility(View.VISIBLE);
            } else {
                holder.check_box.setVisibility(View.INVISIBLE);
            }

            if(holder.check_box != null && localBookInfo.getChecked() != null){
                holder.check_box.setChecked(localBookInfo.getChecked());
            }

            if (isDelete){
                holder.mBook.setOnClickListener(v -> {
                    if (mDeleteList.contains(localBookInfo)) {
                        mDeleteList.remove(localBookInfo);
                        localBookInfo.setChecked(false);
                    } else {
                        mDeleteList.add(localBookInfo);
                        localBookInfo.setChecked(true);
                    }
                    notifyDataSetChanged();
                });
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView author_TextView;
        TextView title_cn_TextView;
        TextView title_en_TextView;
        public CheckBox check_box;
        LinearLayout mBook;
    }

    public void deleteBook(){
        mList.removeAll(mDeleteList);
    }
}
