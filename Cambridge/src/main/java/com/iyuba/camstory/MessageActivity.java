package com.iyuba.camstory;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.iyuba.camstory.adpater.AttentionListAdapter;
import com.iyuba.camstory.adpater.MessageLetterListAdapter;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestAttentionList;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestMessageLetterList;
import com.iyuba.camstory.listener.RequestSetMessageLetterRead;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.manager.NightModeManager;
import com.iyuba.camstory.manager.SocialDataManager;
import com.iyuba.camstory.sqlite.mode.MessageLetter;
import com.iyuba.camstory.sqlite.mode.NotificationInfo;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class MessageActivity extends AppCompatActivity implements
        OnScrollListener {
    private WindowManager mWindowManager;
    private NightModeManager nightModeManager;
    // private Button message_letter,/*message_notification,*/message_reply;
    // private Button
    // message_letter_press,/*message_notification_press,*/message_reply_press;
    private Context mContext;
    private View letterView;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPageAdapter;
    private List<View> mListViews;
    private LayoutInflater mInflater;
    private CustomDialog waitingDialog;
    private Boolean isLetterLastPage = true;
    private boolean isRerefreshBoolean;

    //private ActionBar actionBar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mContext = this;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        nightModeManager = new NightModeManager(mWindowManager, mContext);
        //initActionbar();
        initWidget();
        waitingDialog = waitingDialog();
        // mTimer.schedule(mTimerTask, 0, 20000);
    }

	/*private void initActionbar() {
        actionBar = this.getSupportActionBar();
		SpannableString ss = new SpannableString("消息列表");
		ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		actionBar.setTitle(ss);
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.color.camstorygreen));
	}*/
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}*/

    public void onResume() {
        super.onResume();
        nightModeManager.checkMode();
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initWidget() {
        viewPageAdapter = new ViewPagerAdapter();
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 == 0) {// 私信
                    letterView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        mListViews = new ArrayList<View>();
        mInflater = getLayoutInflater();
        letterView = mInflater.inflate(R.layout.message_letter, null);
        mListViews.add(letterView);
        viewPager.setAdapter(viewPageAdapter);
        initLetterView();
    }

    // 私信部分
    private PullToRefreshListView letterListView;
    private ArrayList<MessageLetter> letterList = new ArrayList<MessageLetter>();
    private MessageLetterListAdapter adapter;
    private int curPage = 1;

    // 通知部分
    private PullToRefreshListView notificationListView;
    private ArrayList<NotificationInfo> notificationList = new ArrayList<NotificationInfo>();

    // 回复部分
    private AttentionListAdapter adapterReply;
    private ArrayList<AttentionBean.DataBean> replyList = new ArrayList<AttentionBean.DataBean>();

    private void initLetterView() {
        letterListView = letterView
                .findViewById(R.id.letterlist);
        adapter = new MessageLetterListAdapter(mContext);
        // whichView=1;
        letterListView.setAdapter(adapter);
        handler.sendEmptyMessage(0);
        letterListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(0);
                isRerefreshBoolean = true;
            }
        });
        letterListView.setOnScrollListener(this);
    }

    Handler handler_reply = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    replyList.clear();
                    handler_reply.sendEmptyMessage(1);
                    handler_reply.sendEmptyMessage(2);
                    break;

                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    RequestAttentionList request = new RequestAttentionList(
                            AccountManager.getInstance().userId, curPage,
                            new RequestCallBack() {
                                @Override
                                public void requestResult(Request result) {
                                    RequestAttentionList rs = (RequestAttentionList) result;
                                    if (rs.isRequestSuccessful()) {
                                        replyList.clear();
                                        replyList.addAll(rs.fansList);
                                        adapterReply.addList(replyList);
                                    }
                                    curPage += 1;
                                    handler_reply.sendEmptyMessage(4);
                                }
                            });
                    CrashApplication.getInstance().getQueue().add(request);
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler_reply.sendEmptyMessage(3);
                    adapterReply.notifyDataSetChanged();
                    if (isRerefreshBoolean) {
                        // replyListView.onRefreshComplete();
                    }
                    setListener();
                    break;
                default:
                    break;
            }
        }

    };

    Handler handler_notification = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    // adapterNoti.clearList();
                    notificationList.clear();
                    handler_notification.sendEmptyMessage(1);
                    handler_notification.sendEmptyMessage(2);
                    break;
                case 1:
				/*
				 * //联网获取日志列表，滑到底部点击更多进行加载 ClientSession.Instance().asynGetResponse(new
				 * RequestNotificationInfo (AccountManager.Instance(mContext).userId
				 * ,currPages,"system,app,follow") , new IResponseReceiver(){
				 * 
				 * @Override public void onResponse(BaseHttpRequest request,
				 * BaseHttpResponse response) { // TODO Auto-generated method stub
				 * ResponseNotificationInfo res=(ResponseNotificationInfo)response;
				 * if(res.result.equals("631")){ notificationList.clear();
				 * notificationList.addAll(res.list); adapterNoti.addList(res.list);
				 * if(res.lastPage==res.nextPage){ isNotificationLastPage=true; }else{
				 * isNotificationLastPage=false; } }else{ } curPage+=1;
				 * currPages=String.valueOf(curPage);
				 * handler_notification.sendEmptyMessage(4); } }); break;
				 */
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler_notification.sendEmptyMessage(3);
                    // adapterNoti.notifyDataSetChanged();
                    if (isRerefreshBoolean) {
                        notificationListView.onRefreshComplete();
                    }
                    setListener();
                    break;

                case 7:
                    handler_notification.sendEmptyMessage(3);
                    Intent intent = new Intent();
                    intent.setClass(mContext, MessageLetterContentActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    // adapter.clearList();
                    letterList.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    RequestMessageLetterList request = new RequestMessageLetterList(
                            AccountManager.getInstance().userId, new RequestCallBack() {
                        @Override
                        public void requestResult(Request result) {
                            RequestMessageLetterList res = (RequestMessageLetterList) result;
                            if (res.isRequestSuccessful()) {
                                letterList.clear();
                                letterList.addAll(res.list);
                                isLetterLastPage = res.lastPage == res.nextPage;
                            }
                            curPage += 1;
                            handler.sendEmptyMessage(4);
                        }
                    });
                    CrashApplication.getInstance().getQueue().add(request);
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler.sendEmptyMessage(3);
                    adapter.addList(letterList);
                    adapter.notifyDataSetChanged();
                    if (isRerefreshBoolean) {
                        letterListView.onRefreshComplete();
                    }
                    setListener();
                    break;
                case 7:
                    handler.sendEmptyMessage(3);
                    Intent intent = new Intent();
                    intent.putExtra("friendid",
                            SocialDataManager.getInstance().letter.friendid);
                    intent.putExtra("currentname",
                            SocialDataManager.getInstance().letter.name);
                    // Log.d("friendid==null",
                    // (SocialDataManager.Instance().letter.friendid)+"");
                    intent.setClass(mContext, MessageLetterContentActivity.class);
                    startActivity(intent);
                    break;
                case 10:
                    // 设置是否已读
                    RequestSetMessageLetterRead request1 = new RequestSetMessageLetterRead(
                            AccountManager.getInstance().userId,
                            SocialDataManager.getInstance().letter.plid, new RequestCallBack() {
                        @Override
                        public void requestResult(Request result) {
                            // TODO 自动生成的方法存根
                            RequestSetMessageLetterRead res = (RequestSetMessageLetterRead) result;
                            if (res.isRequestSuccessful()) {
                            }
                        }
                    });
                    CrashApplication.getInstance().getQueue().add(request1);
                    break;
                default:
                    break;
            }
        }

    };
    private int whichView;

    private void setListener() {
        letterListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // Log.d("pressed", "pressed1");
                if (position == 1) {
                    if(AccountManager.getInstance().islinshi){
                        Intent intent = new Intent();
                        intent.setClass(mContext, LoginActivity.class);
                        ToastUtil.showToast(mContext,"临时用户无法使用私信功能，请登录|注册");
                        startActivity(intent);
                    }
                    else{
                    // Log.d("pressed", "pressed");
                    Intent intent = new Intent();
                    intent.setClass(mContext, FindFriendByNameActivity.class);
                    startActivity(intent);
                    }
                } else {
                    SocialDataManager.getInstance().letterlist = letterList;
                    SocialDataManager.getInstance().letter = letterList.get(position - 2);
                    MessageLetterListAdapter.ViewHolder2 viewholder = (MessageLetterListAdapter.ViewHolder2) arg1
                            .getTag();
                    viewholder.isNew.setVisibility(View.GONE);
                    handler.sendEmptyMessage(7);// 进入私信聊天界面
                    handler.sendEmptyMessage(10);
                }
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            ((ViewPager) collection).addView(mListViews.get(position), 0);

            return mListViews.get(position);
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView(mListViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

    public CustomDialog waitingDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.wetting, null);
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
        CustomDialog cDialog = customBuilder.setContentView(layout).create();
        return cDialog;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    // listView.addFooterView(footer);
                    if (whichView == 1) {
                        if (!isLetterLastPage) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                    isRerefreshBoolean = false;
                    System.out.println("滑动到底部");
                    break;

                }
        }
    }

    public void finish() {
        super.finish();
        nightModeManager.remove();
    }
}
