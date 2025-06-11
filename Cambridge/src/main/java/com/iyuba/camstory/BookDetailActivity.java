package com.iyuba.camstory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.camstory.adpater.DetailViewPager;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.BookListResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;
import com.iyuba.camstory.bean.Collect;
import com.iyuba.camstory.fragment.AboutAuthorFragment;
import com.iyuba.camstory.fragment.AboutStoryFragment;
import com.iyuba.camstory.fragment.ChapterListFragment;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.network.RequestFactory;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.room.CollectDao;
import com.iyuba.camstory.utils.BaseObserver;
import com.iyuba.camstory.utils.NetWorkStateUtil;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.ProgressWheel;
import com.iyuba.configation.Constant;
import com.iyuba.multithread.DownloadProgressListener;
import com.iyuba.multithread.FileDownloader;
import com.iyuba.multithread.FileService;
import com.iyuba.multithread.MultiThreadDownloadManager;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Created by mingyu on 2016/10/9.
 * <p>
 * 该页面开启了一个界面用于展示每一个cardview具体的内容,
 * 在initView里面设置了上半部分的布局和下半部分tab和viewpager的数据并在viewpager里面添加了三个Fragment
 * 并且为收藏图片设置了点击事
 */

@RuntimePermissions
public class BookDetailActivity extends AppCompatActivity {
    private TabLayout tabs;
    private ViewPager viewpager_chapter;
    private AboutAuthorFragment aboutAuthorFragment;
    private AboutStoryFragment aboutStoryFragment;
    private ChapterListFragment chapterListFragment;
    private DetailViewPager pagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private String[] tabNames = {"章节列表", "故事概要", "关于作者"};
    private BookListResponse.BookInfo newBookInfo;
    private ImageView frontcover_little_imageview;
    private ImageView collectButton;
    private ImageView downloadImageButton;
    private int isConnect;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;
    private FileService fileService;
    private ProgressWheel progressWheel;
    private TextView booktitle_en;
    private TextView booktitle_cn;
    private TextView booklevel_textview;
    private TextView bookl_author;
    private TextView book_interpreter;
    private TextView book_wordcounts;
    private Context mContext;
    private int downloadStatus;//0未下载，1下载中，2下载完成

    private int currDown = 0;

    private boolean isCollect = false;

    private List<ChapterVersionResponse.ChapterVersion> chapterVersions;

    private List<BookDetailResponse.ChapterInfo> chapterInfoList;

    private CollectDao collectDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);
        int uiMode = getResources().getConfiguration().uiMode;

        collectDao = AppDatabase.getInstance(this).getCollectDao();
        mContext = this;

        initData();
        bindView();
        initView();
        initListener();

        //根据深色模式切换，这里屏蔽，不实用深色模式
//        if ((uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
//            findViewById(R.id.top_layout).setBackground(getDrawable(R.mipmap.blacktemp));
//        } else {
//            findViewById(R.id.top_layout).setBackground(getDrawable(R.mipmap.whitetemp));
//        }
        findViewById(R.id.top_layout).setBackground(getDrawable(R.mipmap.whitetemp));

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(newBookInfo.getBookname_cn());
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 网络初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        newBookInfo = (BookListResponse.BookInfo) intent.getSerializableExtra("book");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MultiThreadDownloadManager.stopDownloads();
        if (!isDownloadFinish){
            deleteAllDownload();
        }
    }

    private void bindView() {
        booktitle_en = findViewById(R.id.booktitle_en);
        booktitle_cn = findViewById(R.id.booktitle_cn);
        booklevel_textview = findViewById(R.id.booklevel_textview);
        bookl_author = findViewById(R.id.bookl_author);
        book_interpreter = findViewById(R.id.book_interpreter);
        book_wordcounts = findViewById(R.id.book_wordcounts);
        downloadImageButton = findViewById(R.id.downloadbutton);
        tabs = findViewById(R.id.tabs_chapter);
        viewpager_chapter = findViewById(R.id.viewpager_chapter);
        frontcover_little_imageview = findViewById(R.id.frontcover_little_imageview);
        collectButton = findViewById(R.id.collectbutton);
        progressWheel = findViewById(R.id.videodownload_wheel);
    }

    private void initListener() {
        //为收藏按钮设置点击事件
        collectButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (AccountManager.getInstance().islinshi) {
                intent.setClass(BookDetailActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if (AccountManager.getInstance().loginStatus == 0) {
                intent.setClass(BookDetailActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                /**
                 * 收藏操作，更新数据库
                 */
                if (isCollect) {
                    try {
                        collectDao.delete(new Collect(
                                String.valueOf(AccountManager.getInstance().userId),
                                newBookInfo.getLevel(),
                                newBookInfo.getOrderNumber(),
                                newBookInfo.getTypes())
                        );
                        isCollect = false;
                        collectButton.setImageResource(R.mipmap.news_uncollect);
                    } catch (Exception e) {
                        ToastUtil.showToast(this, "操作失败");
                    }
                } else {
                    try {
                        collectDao.insert(new Collect(
                                String.valueOf(AccountManager.getInstance().userId),
                                newBookInfo.getLevel(),
                                newBookInfo.getOrderNumber(),
                                newBookInfo.getTypes())
                        );
                        collectButton.setImageResource(R.mipmap.news_collected1);
                        isCollect = true;
                    } catch (Exception e) {
                        ToastUtil.showToast(this, "收藏失败");
                    }
                }

            }
        });

        //下载文件的逻辑
        downloadImageButton.setOnClickListener(downListener);

        //当弹出下载按钮时候机会弹出对话框,如果点击确定就开始下载
        alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            //下载听力   //download();
                            BookDetailActivityPermissionsDispatcher.openPermissionWithPermissionCheck(BookDetailActivity.this);
                        })
                //如果点击取消之后就关闭对话框
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> dialog.cancel()).create();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.mydialog, null);
        alertDialog.setView(layout);
        alertDialog.setTitle(R.string.alert);

        progressWheel.setOnClickListener(downListener);
    }

    /**
     * 接口获取书籍详情，并初始化视图
     */
    private void initView() {

        if (AccountManager.getInstance().loginStatus != 0) {
            collectDao.findByBookInfo(
                            AccountManager.getInstance().userId + "",
                            newBookInfo.getLevel(),
                            newBookInfo.getOrderNumber(),
                            newBookInfo.getTypes())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Collect>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Collect> collects) {
                            if (!CollectionUtils.isEmpty(collects)) {
                                collectButton.setImageResource(R.mipmap.news_collected1);
                                isCollect = true;
                            } else {
                                collectButton.setImageResource(R.mipmap.news_uncollect);
                                isCollect = false;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

        }

        // TODO: 2025/1/13 单独的获取的书籍数据
        RequestFactory.getBookApi().getBookInfo(newBookInfo.getTypes(), newBookInfo.getLevel(), newBookInfo.getOrderNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookDetailResponse response) {
                        chapterInfoList = response.getChapterInfo();

                        //将上边的操作换成下边的这样
                        if (!isDownloadFinish&&isAllFileDownload()){
                            isDownloadFinish = true;
                            progressWheel.setVisibility(View.GONE);
                            downloadImageButton.setImageResource(R.mipmap.news_downloaded1);//下载完成显示
                            downloadImageButton.setVisibility(View.VISIBLE);
                        }

                        String pic = newBookInfo.getPic();
                        com.iyuba.camstory.lil.GlideUtil.loadImg(BookDetailActivity.this,Constant.BASE_URL_STATIC + pic,0,frontcover_little_imageview);
                        booktitle_en.setText(newBookInfo.getBookname_en());
                        booktitle_cn.setText(newBookInfo.getBookname_cn());
                        int level = Integer.valueOf(newBookInfo.getLevel());
                        String[] stringArray = getResources().getStringArray(R.array.story_leve);
                        booklevel_textview.setText(stringArray[level]);
                        bookl_author.setText("作者：" +newBookInfo.getAuthor());
                        book_interpreter.setText("译者：" + newBookInfo.getInterpreter());
                        book_wordcounts.setText("字数：" + newBookInfo.getWordcounts());

                        aboutAuthorFragment = new AboutAuthorFragment(newBookInfo);
                        aboutStoryFragment = new AboutStoryFragment(newBookInfo);
                        chapterListFragment = new ChapterListFragment(response.getChapterInfo(), chapterVersions);
                        fragments.add(chapterListFragment);
                        fragments.add(aboutStoryFragment);
                        fragments.add(aboutAuthorFragment);
                        /*fragments.set(0,chapterListFragment);
                        fragments.set(1,aboutStoryFragment);
                        fragments.set(2,chapterListFragment);*/

                        pagerAdapter = new DetailViewPager(getSupportFragmentManager(), fragments);
                        viewpager_chapter.setAdapter(pagerAdapter);
                        tabs.setupWithViewPager(viewpager_chapter);
                        for (int i = 0; i < tabNames.length; i++) {
                            tabs.getTabAt(i).setText(tabNames[i]);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        AppDatabase database = AppDatabase.getInstance(this);
        //初始化收藏状态
        // TODO: 2025/1/13 更换成单独获取书籍信息的接口
        /*RequestFactory.getBookApi()
                .getUpdateVersion(newBookInfo.getTypes(), newBookInfo.getLevel(), newBookInfo.getOrderNumber())
                .onErrorReturn(throwable -> new ChapterVersionResponse()).flatMap((Function<ChapterVersionResponse, ObservableSource<List<BookDetailResponse.ChapterInfo>>>) chapterVersionResponse -> {
                    BookDetailActivity.this.chapterVersions = chapterVersionResponse.getData();
                    return Observable.fromArray(database.getChapterDao().list(
                            Integer.valueOf(newBookInfo.getLevel()),
                            Integer.valueOf(newBookInfo.getOrderNumber()),
                            newBookInfo.getTypes()
                    )).flatMap(chapterInfos -> {
                        if(chapterInfos.isEmpty()){
                            return RequestFactory.getBookApi().getBookInfo(
                                    newBookInfo.getTypes(),
                                    newBookInfo.getLevel(),
                                    newBookInfo.getOrderNumber()
                            ).flatMap(bookDetailResponse -> {
                                List<BookDetailResponse.ChapterInfo> chapterInfos1 = bookDetailResponse.getChapterInfo();
                                for (BookDetailResponse.ChapterInfo chapterInfo : chapterInfos1) {
                                    database.getChapterDao().insert(chapterInfo);
                                }
                                return Observable.fromArray(chapterInfos1);
                            });
                        }else{
                            return Observable.fromArray(chapterInfos);
                        }
                    });
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<BookDetailResponse.ChapterInfo>>() {
                    @Override
                    public void onSuccess(List<BookDetailResponse.ChapterInfo> chapterInfos) {
                        chapterInfoList = chapterInfos;
                        //再次进入显示下载情况，实际上没有卵用
                        *//*if (isAudioFileExist(chapterInfoList.size() - 1)) {
                            progressWheel.setVisibility(View.GONE);
                            downloadImageButton.setImageResource(R.mipmap.news_downloaded1);//下载完成显示
                            downloadImageButton.setVisibility(View.VISIBLE);
                        }*//*

                        //将上边的操作换成下边的这样
                        if (!isDownloadFinish&&isAllFileDownload()){
                            isDownloadFinish = true;
                            progressWheel.setVisibility(View.GONE);
                            downloadImageButton.setImageResource(R.mipmap.news_downloaded1);//下载完成显示
                            downloadImageButton.setVisibility(View.VISIBLE);
                        }

                        String pic = newBookInfo.getPic();
                        com.iyuba.camstory.lil.GlideUtil.loadImg(BookDetailActivity.this,Constant.BASE_URL_STATIC + pic,0,frontcover_little_imageview);
//                        GlideUtil.loadImage(frontcover_little_imageview, Constant.BASE_URL_STATIC + pic);
                        booktitle_en.setText(newBookInfo.getBookname_en());
                        booktitle_cn.setText(newBookInfo.getBookname_cn());
                        int level = Integer.valueOf(newBookInfo.getLevel());
                        String[] stringArray = getResources().getStringArray(R.array.story_leve);
                        booklevel_textview.setText(stringArray[level]);
                        bookl_author.setText("作者：" +newBookInfo.getAuthor());
                        book_interpreter.setText("译者：" + newBookInfo.getInterpreter());
                        book_wordcounts.setText("字数：" + newBookInfo.getWordcounts());

                        aboutAuthorFragment = new AboutAuthorFragment(newBookInfo);
                        aboutStoryFragment = new AboutStoryFragment(newBookInfo);
                        chapterListFragment = new ChapterListFragment(chapterInfos, chapterVersions);
                        fragments.add(chapterListFragment);
                        fragments.add(aboutStoryFragment);
                        fragments.add(aboutAuthorFragment);
                        *//*fragments.set(0,chapterListFragment);
                        fragments.set(1,aboutStoryFragment);
                        fragments.set(2,chapterListFragment);*//*

                        pagerAdapter = new DetailViewPager(getSupportFragmentManager(), fragments);
                        viewpager_chapter.setAdapter(pagerAdapter);
                        tabs.setupWithViewPager(viewpager_chapter);
                        for (int i = 0; i < tabNames.length; i++) {
                            tabs.getTabAt(i).setText(tabNames[i]);
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {

                    }
                });*/
    }

    private void dpChanged2() {
        downloadImageButton.setImageResource(R.mipmap.news_download);
        downloadImageButton.setVisibility(View.VISIBLE);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 7:
                    dpChanged2();//下载失败
                    downloadStatus = 0;
                    break;
                case 8:
                    /**
                     * 由于是每个章节的音频都分别入队下载
                     * 所以有几个章节便会下载完成几次，因此这里每下载完一个章节就更新一下进度
                     */
                    currDown++;
                    double i = currDown / (double) chapterInfoList.size();
                    progressWheel.setProgress((int) (i * 360));
                    progressWheel.setText((int) (i * 100) + "%");
                    if (i == 1) {
                        downloadImageButton.setVisibility(View.VISIBLE);
                        downloadImageButton.setImageResource(R.mipmap.news_downloaded1);
                        progressWheel.setVisibility(View.GONE);
                        downloadStatus = 2;
                    }

                    if (isAllFileDownload()){
                        isDownloadFinish = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };
    View.OnClickListener downListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (AccountManager.getInstance().loginStatus == 0) {
                startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                return;
            }

            if (isDownloadFinish){
                ToastUtil.showToast(BookDetailActivity.this,"当前课程已经下载完成");
                return;
            }

            if (!alertDialog.isShowing()) {
                switch (NetWorkStateUtil.getAPNType()) {
                    case 0:
                        ToastUtil.showToast(mContext, mContext.getString(R.string.category_check_network));
                        break;
                    case 2:
                        download();//wifi
                        downloadStatus = 1;
                        break;
                    default:
                        alertDialog.show();//流量
                }
            }
        }
    };

    private boolean checkNetWork() {
        boolean result = false;
        isConnect = NetWorkStateUtil.getAPNType();
        if (isConnect == 0) {
            CustomToast.showToast(this, R.string.category_check_network,
                    1000);
        } else if (isConnect == 1) {
            //如果等于1判断一下勾选框是否勾选,如果没勾选则弹出对话框提示,如果是第一次的话默认是false
            if (!ConfigManagerVOA.Instance(this).loadBoolean("remember_download_ok"))
                alertDialog.show();
            else if (isAudioFileExist(StoryDataManager.Instance().getCurrChapter())
                    && isAudioFileComplete(StoryDataManager.Instance().getCurrChapter())) {
                CustomToast.showToast(this, "已下载，您可在个人中心查看下载", 1000);
            } else {
                result = true;
            }
        } else if (isConnect == 2) {
            if (isAudioFileExist(StoryDataManager.Instance().getCurrChapter())
                    && isAudioFileComplete(StoryDataManager.Instance().getCurrChapter())) {
                CustomToast.showToast(this, "已下载，您可在个人中心查看下载", 1000);
            } else {
                //这个标志用于判断用户的状态
                result = true;
            }
        }

        return result;
    }

    private boolean isAudioFileExist(int currChapter) {
        File downFile = new File(getAudioSubPath(currChapter));
        return downFile.exists() && downFile.length() != 0;
    }

    private boolean isAudioFileComplete(int currChapter) {
        // TODO: 2023/1/13 使用需要改动
        return !fileService.isInDownloadDB("getAudioUrl(currChapter)");
    }

    //download是下载的方法
    private void download() {
        newBookInfo.setIsDown(1);
        AppDatabase.getInstance(this).getBookInfoDao().update(newBookInfo);
        if (downloadStatus == 1) {
            ToastUtil.showToast(mContext, "正在下载！");
            return;
        } else if (downloadStatus == 2) {
            ToastUtil.showToast(mContext, "已经下载完成！");
            return;
        }
        currDown = 0;
        ToastUtil.showToast(mContext, "开始下载！");
        downloadImageButton.setVisibility(View.GONE);
        progressWheel.setVisibility(View.VISIBLE);
        String fileSaveDir;
        //下载的时候创建一个文件夹然后进一步判断文件,然后进一步判断文件是否存在如果存在则直接用如果不存在则创建一个文件
        File file = new File(ConfigManagerVOA.Instance(this).loadString("media_saving_path"));
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < chapterInfoList.size(); i++) {
            fileSaveDir = getAudioSubPath(i);//合并音频路径！
            final String finalFileSaveDir1 = fileSaveDir;
            MultiThreadDownloadManager.enQueue(
                    mContext,
                    Integer.valueOf(chapterInfoList.get(i).getVoaid()),
                    "http://static2.iyuba.cn" + chapterInfoList.get(i).getSound(),
                    new File(fileSaveDir),
                    2,
                    new DownloadProgressListener() {
                        @Override
                        public void onProgressUpdate(int id, String url, int fileDownloadSize) {

                        }

                        @Override
                        public void onDownloadStart(FileDownloader fileDownloader, int id, int fileTotalSize) {
                            handler.sendEmptyMessage(6);
                        }

                        @Override
                        public void onDownloadError(int id, Exception e) {
                            File file = new File(finalFileSaveDir1);
                            if (file.exists()) {
                                file.delete();
                            }
                            (BookDetailActivity.this).runOnUiThread(() -> {
                                CustomToast.showToast(BookDetailActivity.this, "文章下载失败，请重试",
                                        2000);
                            });
                            handler.sendEmptyMessage(7);
                        }

                        @Override
                        public void onDownloadComplete(final int id, String string) {
                            // TODO: 2023/1/13 已经下载好的章节需要记录一下地址
                            handler.sendEmptyMessage(8);//下载完成显示
                        }

                        @Override
                        public void onDownloadStoped(int id) {

                        }
                    });
        }
    }

    private String getAudioSubPath(int paramInt) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(ConfigManagerVOA.Instance(this).loadString(
                "media_saving_path"));
        localStringBuffer.append(File.separator);
        localStringBuffer.append("temp");
        localStringBuffer.append("_");
        localStringBuffer
                .append(chapterInfoList.get(paramInt).getVoaid());
        localStringBuffer.append("_");
        localStringBuffer.append(chapterInfoList.get(paramInt).getTypes());
        localStringBuffer.append(".mp3");
        return localStringBuffer.toString();
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void openPermission() {
        download();
        downloadStatus = 1;
    }

    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForPermission() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /***************************下载操作***********************/
    //是否完成下载操作
    private boolean isDownloadFinish = false;

    //删除全部的文件
    private void deleteAllDownload(){
        if (chapterInfoList!=null&&chapterInfoList.size()>0){
            for (int i = 0; i < chapterInfoList.size(); i++) {
                String audioPath = getAudioSubPath(i);

                File tempFile = new File(audioPath);
                if (tempFile.exists()&&tempFile.length()>0){
                    tempFile.delete();
                }
            }
        }
    }

    //是否全部文件已经下载完成
    private boolean isAllFileDownload(){
        if (chapterInfoList!=null&&chapterInfoList.size()>0){
            int downCount = 0;

            for (int i = 0; i < chapterInfoList.size(); i++) {
                String audioPath = getAudioSubPath(i);

                File tempFile = new File(audioPath);
                if (tempFile.exists()&&tempFile.length()>0){
                    downCount++;
                }
            }

            if (downCount==chapterInfoList.size()){
                return true;
            }
        }
        return false;
    }
}
