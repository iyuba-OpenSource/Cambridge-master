package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.iyuba.camstory.bean.AdBean;
import com.iyuba.camstory.bean.BookContentResponse;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;
import com.iyuba.camstory.fragment.ChapterListFragment;
import com.iyuba.camstory.fragment.ReadFragment;
import com.iyuba.camstory.fragment.ReadRankFragment;
import com.iyuba.camstory.fragment.StoryTextFragment;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.room.ChapterVersionDao;
import com.iyuba.camstory.utils.BaseObserver;
import com.iyuba.configation.Constant;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.apache.commons.collections4.ListUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 章节细节
 */
public class ChapterDetailActivity extends AppCompatActivity{

    private TabLayout mIndicator;
    private ViewPager mViewPager;
    private String[] mTitles = new String[]{"原文", "评测", "排行"};
    private List<Fragment> mFragmentsList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private View downloadView;
    private ImageView photoImage;
    private YouDaoNative youDaoNative;

    private final String prefix = "http://static3." + Constant.IYBHttpHead + "/dev/";
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

    private ImageView adClose;
    private RelativeLayout rlAd;

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);
        Toolbar toolBar = findViewById(R.id.toolbar);
        downloadView = findViewById(R.id.downloadBtn);
        photoImage = findViewById(R.id.photoImage);
        rlAd = findViewById(R.id.rl_ad);
        adClose = findViewById(R.id.adtips);
        adClose.setOnClickListener(v -> {
            downloadView.setVisibility(View.GONE);
            rlAd.setVisibility(View.GONE);
        });
        if (AccountManager.getInstance().isVip(this)) {
            rlAd.setVisibility(View.GONE);
        } else {
            //广告一直不出来，隐藏广告
            rlAd.setVisibility(View.GONE);
        }

        setSupportActionBar(toolBar);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(StoryDataManager.Instance().getCurChapterInfo().getCname_cn());
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
        initView();
        initData();
    }

    /*public void getLoadAd() {//230// d59b7c0a808d01b7041c2d127da95
        youDaoNative = new YouDaoNative(this, "230d59b7c0a808d01b7041c2d127da95", this);
        Location exampleLocation = new Location("example");
        exampleLocation.setLatitude(23.1);
        exampleLocation.setLongitude(42.1);
        exampleLocation.setAccuracy(100);
        RequestParameters requestParameters = RequestParameters.builder()
                .location(exampleLocation).build();
        // 请求广告
        youDaoNative.makeRequest(requestParameters);
    }*/

    private void initView() {
        SpannableString ss = new SpannableString("返回");
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mIndicator = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.id_stickynavlayout_viewpager);
    }

    //是否已经初始化
    private boolean isInitFragment = false;
    private void initData() {
        //如果是从通知栏控制条跳转过来直接在storymanager中拿数据
        String service = getIntent().getStringExtra("SERVICE");
        BookDetailResponse.ChapterInfo chapterInfo;
        if(service != null){
            chapterInfo = StoryDataManager.Instance().getCurChapterInfo();
        }else{
            chapterInfo = (BookDetailResponse.ChapterInfo) getIntent().getSerializableExtra(ChapterListFragment.CHAPTER_INFO);
        }
        Log.e("TAG", "initData: "+chapterInfo.toString());
        ChapterVersionResponse.ChapterVersion chapterVersion = (ChapterVersionResponse.ChapterVersion) getIntent().getSerializableExtra(ChapterListFragment.CHAPTER_VERSION);
        int newVersion = 1;
        if (chapterVersion != null) {
            newVersion = chapterVersion.getVersion();
        }

        //根据版本号判断数据是否更新，从而确定是从本地获取数据还是从接口获取并更新本地数据
        ChapterVersionDao chapterVersionDao = AppDatabase.getInstance(this).getChapterVersionDao();
        int finalNewVersion = newVersion;
        chapterVersionDao.findByVoaId(Integer.valueOf(chapterInfo.getVoaid()))
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<ChapterVersionResponse.ChapterVersion>, ObservableSource<List<BookContentResponse.Texts>>>) chapterVersions -> {
                    boolean update = true;
                    if (ListUtils.emptyIfNull(chapterVersions).size() > 0) {
                        int oldVersion = chapterVersions.get(0).getVersion();
                        if (oldVersion >= finalNewVersion) {
                            update = false;
                        }
                    }
                    if(update){
                        return RequestFactory.getBookApi()
                                .getBookContent(
                                        chapterInfo.getTypes(),
                                        chapterInfo.getLevel(),
                                        chapterInfo.getOrderNumber(),
                                        chapterInfo.getChapterOrder()
                                ).flatMap(bookContentResponse -> {
                                    for (BookContentResponse.Texts text : bookContentResponse.getTexts()) {
                                        AppDatabase.getInstance(ChapterDetailActivity.this).getSentenceDao().insert(text);
                                    }
                                    if (chapterVersion != null) {
                                        chapterVersionDao.insert(chapterVersion);
                                    }
                                    return Observable.fromArray(bookContentResponse.getTexts());
                                });
                    }else{
                        return AppDatabase.getInstance(ChapterDetailActivity.this)
                                .getSentenceDao()
                                .list(chapterInfo.getVoaid(),chapterInfo.getTypes());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BookContentResponse.Texts>>() {

                    @Override
                    public void onSuccess(List<BookContentResponse.Texts> textsList) {
                        if (!isInitFragment){
                            initFragment(textsList,chapterInfo);
                            isInitFragment = true;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
    }

    /*@Override
    public void onNativeLoad(final NativeResponse nativeResponse) {
        // 广告load完毕之后，数据都封装在nativeResponse，包括是否是下载包，还有一系列点击展示方法
        List<String> imageUrls = new ArrayList<String>();
        if (nativeResponse.getIconImageUrl() != null) {
            imageUrls.add(nativeResponse.getIconImageUrl());
        } else {
            imageUrls.add(nativeResponse.getMainImageUrl());
        }
        // 广告点击监听事件
        downloadView.setOnClickListener(v -> {
            // 广告点击，该方法会自动记录点击次数，对于落地页类，下载类和下载详情类广告都统一调用handleClick方法
            // 对于下载类广告，采用默认弹窗
            nativeResponse.handleClick(downloadView);
        });
        ImageService.get(this, imageUrls,
                new ImageService.ImageServiceListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(final Map<String, Bitmap> bitmaps) {
                        if (nativeResponse.getIconImageUrl() != null || nativeResponse.getMainImageUrl() != null) {
                            Bitmap bitMap = bitmaps.get(nativeResponse
                                    .getIconImageUrl());
                            if (bitMap == null) {
                                bitMap = bitmaps.get(nativeResponse
                                        .getMainImageUrl());
                            }
                            if (bitMap != null) {
                                photoImage
                                        .setBackground(new BitmapDrawable(
                                                bitMap));
                                photoImage.setVisibility(View.VISIBLE);
                                // 记录一下展示，该方法必须调用，否则广告展示将记为0
                                nativeResponse.recordImpression(photoImage);
                            }
                        }
                    }

                    @Override
                    public void onFail() {
                    }
                });
        downloadView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {
        final String errorMessage = (nativeErrorCode != null) ? nativeErrorCode.toString() : "";
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initFragment(List<BookContentResponse.Texts> textsList, BookDetailResponse.ChapterInfo chapterInfo){
        Log.e("TAG", "initFragment: "+chapterInfo.toString());
        StoryTextFragment storyTextFragment = new StoryTextFragment(textsList, chapterInfo);
        ReadFragment readFragment = new ReadFragment(textsList, chapterInfo);
        ReadRankFragment readRankFragment = new ReadRankFragment();

        Log.d("广告显示", "显示这里的数据，查看是否走了两遍---2222");

        mFragmentsList.add(storyTextFragment);
        mFragmentsList.add(readFragment);
        mFragmentsList.add(readRankFragment);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentsList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mFragmentsList.size());//预加载
        mIndicator.setupWithViewPager(mViewPager);
    }
}

