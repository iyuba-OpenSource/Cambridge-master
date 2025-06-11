package com.iyuba.camstory.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.BookListAdapter;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.room.BookDao;
import com.iyuba.camstory.sqlite.op.BookInfoOp;
import com.iyuba.camstory.utils.BaseObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mingyu on 2016/10/8.
 *
 */
//该类是用来创建一个Fragment
//该方法中的initdata方法用来初始化数据,其中包含了一个bookinfo的类,该类继承了一个用于数据库操作的类,用于取出数据
//findview方法主要是为GIRDEview设置adapter和设置点击监听的在监听里面设置开启一个新的界面用于展示每个卡片内的数据
//并且为adapter设置数据

public class ContentFragment extends Fragment {//implements PagerChangeListener
    private BookInfoOp bOp;
    private  int curPosition;
    //是用来存放从数据库读取的是数据的bean类de
    private RecyclerView rvBookGrid;
    private BookListAdapter bookListAdapter;
    private ImageView emptyBg;

    private SwipeRefreshLayout refresh;

    private String type;

    private boolean isStart = false;

    public static Fragment newInstance(int position,String type) {
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.curPosition = position;
        contentFragment.type = type;
        return  contentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        // book_gridview = view.findViewById(R.id.book_gridview);
        emptyBg = view.findViewById(R.id.empty_bg);
        refresh = view.findViewById(R.id.refresh_layout);

        rvBookGrid = view.findViewById(R.id.rv_book_grid);
        rvBookGrid.setLayoutManager(new GridLayoutManager(getContext(),3));

        onRefresh(false);

        refresh.setOnRefreshListener(() -> {
            onRefresh(true);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
    }

    private void onRefresh(boolean isRefresh){
        BookDao bookInfoDao = AppDatabase.getInstance(requireActivity()).getBookInfoDao();
        bookInfoDao.findByPrimaryKey(curPosition,type).flatMap((Function<List<BookListResponse.BookInfo>, ObservableSource<List<BookListResponse.BookInfo>>>) bookInfos -> {
                    if(bookInfos.isEmpty())
                        return RequestFactory.getBookApi().getBookList(type,curPosition).flatMap(bookListResponse -> {
                            for (BookListResponse.BookInfo datum : bookListResponse.getData()) {
                                bookInfoDao.insert(datum);
                            }
                            return Observable.fromArray(bookListResponse.getData());
                        });
                    else{
                        return Observable.fromArray(bookInfos);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BookListResponse.BookInfo>>() {
                    @Override
                    public void onSuccess(List<BookListResponse.BookInfo> bookInfos) {
                        if(bookInfos.isEmpty()){
                            emptyBg.setVisibility(View.VISIBLE);
                            rvBookGrid.setVisibility(View.GONE);
                        }else{
                            bookListAdapter = new BookListAdapter(getActivity(),bookInfos);
                            rvBookGrid.setAdapter(bookListAdapter);
                            bookListAdapter.notifyDataSetChanged();
                        }
                        if(isRefresh){
                            refresh.setRefreshing(false);
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e("TAG", "onFailure: "+e.getMessage() );
                    }
                });
    }
}
