package com.iyuba.camstory.adpater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.common.LogUtil;
import com.iyuba.camstory.BookDetailActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.BookListResponse;


import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author Hao Zhao
 * @date 2020/2/5
 */
public class BookListAdapter  extends RecyclerView.Adapter<BookListAdapter.BookHolder>{
    private List<BookListResponse.BookInfo> mList;
    private Context mContext;
    public boolean isDelete;
    public List<BookListResponse.BookInfo> mDeleteList;

    public BookListAdapter(Context paramContext, List<BookListResponse.BookInfo> paramArrayList) {
        mContext = paramContext;
        mList = paramArrayList;
        mDeleteList = new ArrayList<>();
    }

    public void deleteBook(){
        ArrayList<BookListResponse.BookInfo> list=new ArrayList<>();
        if (mDeleteList.size()>0&&mList.size()>0) {
            for (BookListResponse.BookInfo bookInfo : mList) {
                if (!mDeleteList.contains(bookInfo)) {
                    list.add(bookInfo);
                }
            }
        }
        mList=list;
    }
    
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.griditem_bookinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        holder.setData(mList.get(position));
        holder.setListener(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList!=null? mList.size():0;
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView author_TextView;
        TextView title_cn_TextView;
        TextView title_en_TextView;
        public CheckBox check_box;
        LinearLayout mBook;
        
        public BookHolder(View itemView) {
            super(itemView);
            title_cn_TextView = itemView.findViewById(R.id.booktitle_cn);
            title_en_TextView = itemView.findViewById(R.id.booktitle_en);
            author_TextView = itemView.findViewById(R.id.book_author);
            check_box = itemView.findViewById(R.id.check_box);
            mBook = itemView.findViewById(R.id.ll_book);
        }
        
        public void setData(BookListResponse.BookInfo info){
            LogUtil.e("BookListAdapter==setData");
            title_cn_TextView.setText(info.getBookname_cn());
            title_en_TextView.setText(info.getBookname_en());
            author_TextView.setText(info.getAuthor());
            if (isDelete) {
                check_box.setVisibility(View.VISIBLE);
            } else {
                check_box.setVisibility(View.INVISIBLE);
            }

//            check_box.setChecked(info.isChecked);
        }

        public void setListener(BookListResponse.BookInfo info){
            if (isDelete) {
                mBook.setOnClickListener(v -> {
                    if (mDeleteList.contains(info)) {
                        mDeleteList.remove(info);
//                            info.isChecked = false;
                    } else {
                        mDeleteList.add(info);
//                            info.isChecked = true;
                    }
                    notifyDataSetChanged();
                });
            }
            itemView.setOnClickListener(v -> {
                Bundle mBundle = null;
                //点击之后会把数据通过bundle传递到开启的界面,利用intent传递数据
                mBundle = new Bundle();
                mBundle.putSerializable("book",info);

                Intent localIntent = new Intent(itemView.getContext(), BookDetailActivity.class);
                localIntent.putExtras(mBundle);
                itemView.getContext().startActivity(localIntent);
            });

        }
    }
}
