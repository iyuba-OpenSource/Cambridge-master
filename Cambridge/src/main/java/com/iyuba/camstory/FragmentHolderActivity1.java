package com.iyuba.camstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.iyuba.camstory.adpater.BookGridViewAdapter;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.Collect;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.sqlite.op.BookInfoOp;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHolderActivity1 extends AppCompatActivity {
    private static final String TAG = FragmentHolderActivity1.class.getSimpleName();
    private WindowManager mWindowManager;
    private NightModeManager nightModeManager;
    String fragmentName;
    FragmentManager fm = getSupportFragmentManager();
    private ActionBar actionBar;
    private Context mContext;

    private BookInfoOp bOp;
    private BookGridViewAdapter bookAdapter11;
    private GridView bookGridView11;
    private List<BookListResponse.BookInfo> bookInfoList = new ArrayList<>();
    private Animation gridinAnimation;
    private Animation gridoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentholder);
        mContext = this;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(mWindowManager, mContext);
        fragmentName = getIntent().getExtras().getString("fragmentname");

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("小说收藏");
        toolBar.setNavigationOnClickListener(v -> onBackPressed());

        initData();
        initView();
    }


    private void initData() {
        this.gridinAnimation = AnimationUtils.loadAnimation(this.mContext,
                R.anim.grid_in);
        this.gridoutAnimation = AnimationUtils.loadAnimation(this.mContext,
                R.anim.grid_out);
    }

    private void initView() {
        findView();
        AppDatabase.getInstance(this)
                .getCollectDao()
                .list(AccountManager.getInstance().userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BookListResponse.BookInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BookListResponse.BookInfo> bookInfos) {
                        bookInfoList = bookInfos;
                        bookAdapter11 = new BookGridViewAdapter(mContext, bookInfos);
                        bookGridView11.setAdapter(bookAdapter11);
                        bookGridView11.setOnItemClickListener(itemClickListener1);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

    }

    private void findView() {

        this.bookGridView11 = findViewById(R.id.book_gridview11);
    }


    private AdapterView.OnItemClickListener itemClickListener1 = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView,
                                View paramAnonymousView, int paramAnonymousInt,
                                long paramAnonymousLong) {
            int i = paramAnonymousAdapterView.getId();
            Bundle mBundle = null;
            mBundle = new Bundle();
            mBundle.putSerializable("book", bookInfoList.get(paramAnonymousInt));
            Intent localIntent = new Intent(mContext, BookDetailActivity.class);
            localIntent.putExtras(mBundle);
            startActivity(localIntent);

        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        nightModeManager.checkMode();

        mContext = this;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(mWindowManager, mContext);
        fragmentName = getIntent().getExtras().getString("fragmentname");

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("小说收藏");
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
        initData();
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        MobclickAgent.onPause(this);
        nightModeManager.remove();
    }
}
