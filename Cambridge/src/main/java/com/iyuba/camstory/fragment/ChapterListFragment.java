package com.iyuba.camstory.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.iyuba.camstory.ChapterDetailActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.adpater.ChapterListAdapter;
import com.iyuba.camstory.bean.BookDetailResponse;
import com.iyuba.camstory.bean.ChapterVersionResponse;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.StoryDataManager;
import com.iyuba.camstory.room.AppDatabase;
import com.iyuba.camstory.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class ChapterListFragment extends Fragment {

    public static final String CHAPTER_INFO = "CHAPTER_INFO";
    public static final String CHAPTER_VERSION = "CHAPTER_VERSION";

    private View info;
    private ListView listView;
    private ChapterListAdapter mAdapter;
    private Context mContext;
    private List<BookDetailResponse.ChapterInfo> chapters;
    private List<ChapterVersionResponse.ChapterVersion> chapterVersions;

    public ChapterListFragment(List<BookDetailResponse.ChapterInfo> chapters, List<ChapterVersionResponse.ChapterVersion> chapterVersions) {
        this.chapters = chapters;
        this.chapterVersions = chapterVersions;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView,
                                View paramAnonymousView, int paramAnonymousInt,
                                long paramAnonymousLong) {
            StoryDataManager.Instance().setCurrChapter(paramAnonymousInt);
            Intent intent = new Intent();
            intent.setClass(mContext, ChapterDetailActivity.class);
            intent.putExtra(CHAPTER_INFO, chapters.get(paramAnonymousInt));
            if (chapterVersions != null) {
                ChapterVersionResponse.ChapterVersion chapterVersion = chapterVersions.get(Integer.parseInt(chapters.get(paramAnonymousInt).getChapterOrder()));
                intent.putExtra(CHAPTER_VERSION, chapterVersion);
            }
            startActivity(intent);
            mAdapter.setCurPos(paramAnonymousInt);
        }
    };

    private void initView(View paramView) {
        AppDatabase database = AppDatabase.getInstance(requireContext());

        List<BookDetailResponse.ChapterInfo> chapterInfos = new ArrayList<>();

        Observable.fromIterable(chapters)
                .flatMap((Function<BookDetailResponse.ChapterInfo, ObservableSource<BookDetailResponse.ChapterInfo>>) chapter -> Observable.zip(
                        Observable.zip(
                                database.getSentenceDao()
                                        .count(chapter.getVoaid(), chapter.getTypes()),
                                database.getEvaluateDao()
                                        .countEva(AccountManager.getInstance().userId + "", chapter.getVoaid(), chapter.getTypes()),
                                (sentenceCount, evaCount) -> {
                                    if(sentenceCount == 0){
                                        chapter.setShowProgress(false);
                                    }else{
                                        chapter.setShowProgress(true);
                                    }
                                    String percent = "";
                                    if (sentenceCount == 0 || evaCount == 0) {
                                        percent = "0%";
                                    } else {
                                        percent = Utils.percent(evaCount, sentenceCount);
                                    }
                                    chapter.setEvaProgress(percent);
                                    return chapter;
                                }
                        ),
                        Observable.zip(
                                database.getSoundRecordDao()
                                        .selectMaxProgressByUidAndVoaId(
                                                AccountManager.getInstance().userId+"",
                                                chapter.getVoaid(),
                                                chapter.getTypes()
                                        ),
                                database.getSentenceDao()
                                        .selectDuration(
                                                chapter.getVoaid(),
                                                chapter.getTypes()
                                        ),
                                (maxPro, allDur) -> {
                                    int durationInt = 0;
                                    if(allDur != 0.0){
                                        durationInt = (int) (allDur*1024);
                                    }
                                    String percent = "";
                                    if(maxPro == 0 || durationInt == 0){
                                        percent = "0%";
                                    }else{
                                        percent = Utils.percent(maxPro,durationInt);
                                    }
                                    chapter.setSoundProgress(percent);
                                    return chapter;
                                }
                        ),
                        (chapterInfo, chapterInfo2) -> {
                            //第一个参数是评测的进度，第二个是听的进度,合并到一块返回
                            chapterInfo.setSoundProgress(chapterInfo2.getSoundProgress());
                            return chapterInfo;
                        }
                )).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetailResponse.ChapterInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "syz onSubscribe: ");
                    }
                    @Override
                    public void onNext(BookDetailResponse.ChapterInfo c) {
                        chapterInfos.add(c);
                        if(chapterInfos.size() == chapters.size()){
                            Collections.sort(chapterInfos, (c1, c2) -> {
                                int result =  Integer.valueOf(c1.getChapterOrder()) - Integer.valueOf(c2.getChapterOrder());
                                return result;
                            });
                            mAdapter = new ChapterListAdapter(mContext, chapters);
                            listView = paramView.findViewById(R.id.chapter_list);
                            listView.setAdapter(mAdapter);
                            listView.setOnItemClickListener(mOnItemClickListener);
                            listView.setSelection(0);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "syz onError: "+e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void notifyChapterChanged() {
        if (this.mAdapter != null)
            this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity paramActivity) {
        this.mContext = paramActivity;
        this.mAdapter = new ChapterListAdapter(this.mContext, chapters);
        super.onAttach(paramActivity);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup paramViewGroup, Bundle paramBundle) {
        this.info = paramLayoutInflater.inflate(R.layout.fragment_chapterlist, paramViewGroup, false);
        initView(this.info);
        return this.info;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ChapterList_Fragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ChapterList_Fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
