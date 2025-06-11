package com.iyuba.camstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iyuba.camstory.adpater.BookGridViewAdapter;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.utils.BaseObserver;
import com.iyuba.voa.frame.components.ConfigManagerVOA;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DownloadActivity extends AppCompatActivity {

    private Context mContext;
    private WindowManager windowManager;
    private NightModeManager nightModeManager;

    private GridView bookGridView21;
    private BookGridViewAdapter bookGridViewAdapter;
    private Animation gridinAnimation;
    private Animation gridoutAnimation;

    private TextView mTvDeleteSure;
    private ImageView mIvDelete;

    private List<BookListResponse.BookInfo> bookInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mContext = this;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(windowManager, mContext);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("小说下载");
        toolBar.setNavigationOnClickListener(v -> onBackPressed());

        initData();
        initView();
    }

    public void initData(){
        this.gridinAnimation = AnimationUtils.loadAnimation(this.mContext,
                R.anim.grid_in);
        this.gridoutAnimation = AnimationUtils.loadAnimation(this.mContext,
                R.anim.grid_out);
    }

    public void initView(){
        mTvDeleteSure=findViewById(R.id.tv_delete_sure);
        mIvDelete=findViewById(R.id.iv_delete);
        this.bookGridView21 = findViewById(R.id.book_gridview21);

        AppDatabase.getInstance(this)
                .getBookInfoDao()
                .listDown()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookListResponse.BookInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BookListResponse.BookInfo> bookInfos) {
                        DownloadActivity.this.bookInfoList = bookInfos;
                        bookGridViewAdapter = new BookGridViewAdapter(mContext, bookInfos);
                        bookGridView21.setAdapter(bookGridViewAdapter);
                        bookGridView21.setOnItemClickListener(itemClickListener2);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });




        mIvDelete.setOnClickListener(v -> {
            mIvDelete.setVisibility(View.GONE);
            mTvDeleteSure.setVisibility(View.VISIBLE);

            if (bookGridViewAdapter!=null){
                bookGridViewAdapter.isDelete=true;
                bookGridViewAdapter.notifyDataSetChanged();
            }
        });

        mTvDeleteSure.setOnClickListener(v -> {
            mIvDelete.setVisibility(View.VISIBLE);
            mTvDeleteSure.setVisibility(View.GONE);

            if (bookGridViewAdapter!=null){
                bookGridViewAdapter.isDelete=false;
                if (bookGridViewAdapter.mDeleteList.size()>0) {
                    for (BookListResponse.BookInfo bookInfo : bookGridViewAdapter.mDeleteList) {
                        bookInfo.setIsDown(0);
                        AppDatabase.getInstance(this).getBookInfoDao().update(bookInfo);
                        RequestFactory.getBookApi()
                                .getBookInfo(bookInfo.getTypes(), bookInfo.getLevel(), bookInfo.getOrderNumber())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(new BaseObserver<BookDetailResponse>() {
                                    @Override
                                    public void onSuccess(BookDetailResponse bookDetailResponse) {
                                        for (BookDetailResponse.ChapterInfo chapterInfo : bookDetailResponse.getChapterInfo()) {
                                            deleteFile2(getAudioSubPath(chapterInfo.getVoaid(),chapterInfo.getTypes()));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable e) {

                                    }
                                });
                    }
                    bookGridViewAdapter.deleteBook();
                }
                bookGridViewAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
        nightModeManager.checkMode();
    }

    @Override
    public void finish(){
        super.finish();
        MobclickAgent.onPause(this);
        nightModeManager.remove();
    }

    private AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView,
                                View paramAnonymousView, int paramAnonymousInt,
                                long paramAnonymousLong) {
            if (!bookGridViewAdapter.isDelete) {
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("book", bookInfoList.get(paramAnonymousInt));
                Intent localIntent = new Intent(mContext, BookDetailActivity.class);
                localIntent.putExtras(mBundle);
                startActivity(localIntent);
            }
        }

    };

    private void deleteFile2(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }


    private String getAudioSubPath(String voaId,String types) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(ConfigManagerVOA.Instance(this).loadString(
                "media_saving_path"));
        localStringBuffer.append(File.separator);
        localStringBuffer.append("temp");
        localStringBuffer.append("_");
        localStringBuffer
                .append(voaId);
        localStringBuffer.append("_");
        localStringBuffer.append(types);
        localStringBuffer.append(".mp3");
        return localStringBuffer.toString();
    }
}
