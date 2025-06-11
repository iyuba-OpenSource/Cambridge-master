package tv.lycam.gift.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import master.flame.danmaku.ui.widget.DanmakuView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tv.lycam.gift.Info;
import tv.lycam.gift.R;
import tv.lycam.gift.adapter.GiftItemAdapter;
import tv.lycam.gift.adapter.GiftViewPagerAdapter;
import tv.lycam.gift.adapter.PublicMessageAdapter;
import tv.lycam.gift.callback.GiftlistCallback;
import tv.lycam.gift.consts.GEventCode;
import tv.lycam.gift.core.Event;
import tv.lycam.gift.entity.Animator;
import tv.lycam.gift.entity.GiftInfo;
import tv.lycam.gift.entity.GiftItem;
import tv.lycam.gift.entity.GiftReceived;
import tv.lycam.gift.entity.MqttInfo;
import tv.lycam.gift.entity.Result;
import tv.lycam.gift.runner.EngineRunner;
import tv.lycam.gift.util.DisplayUtil;
import tv.lycam.gift.util.FlashUtil;
import tv.lycam.gift.util.KeyBoardUtil;
import tv.lycam.gift.widget.GiftShowView;
import tv.lycam.gift.widget.danmaku.Danmu;
import tv.lycam.gift.widget.danmaku.DanmuControl;
import tv.lycam.gift.widget.flash.FlashDataParser;
import tv.lycam.gift.widget.flash.FlashView;
import tv.lycam.gift.widget.pop.HeartLayout;
import tv.lycam.gift.widget.pop.ScreenUtil;
import tv.lycam.mqtt.callback.MqttListener;
import tv.lycam.mqtt.constants.Action;
import tv.lycam.mqtt.constants.MqttConstants;
import tv.lycam.mqtt.util.MqttBiz;
import tv.lycam.mqtt.util.MqttConfig;

/**
 * Created by su on 16/5/16.
 */
public class GiftFragment extends GFragment implements GFragment.OnKeyboardChangedExtraListener, MqttListener, AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener, AdapterView.OnItemLongClickListener {

    protected static final String BEAN = "bean";
    protected PublicMessageAdapter mMsgListAdapter;
    protected MqttInfo mBean;
    protected GiftItem mCurrentGift;
    protected Queue<GiftReceived> mGiftList = new LinkedBlockingQueue<>();
    protected Queue<GiftReceived> mGiftChannel1 = new LinkedBlockingQueue<>();
    protected Queue<GiftReceived> mGiftChannel2 = new LinkedBlockingQueue<>();
    protected Queue<Animator> mFlashList = new LinkedBlockingQueue<>();
    protected GiftlistCallback mGiftlistCallback;
    // 礼物清单
    protected List<GiftItem> mGiftBeans = new ArrayList<>();
    //
    protected List<ImageView> mGiftPages = new ArrayList<>();

    protected int mGiftPage;
    protected ImageView mMsg;
    protected EditText mText;
    protected ImageView mGift;
    protected LinearLayout mChatzone;
    protected RelativeLayout mTool;
    protected ListView mMsglist;
    protected LinearLayout mSendzone;
    protected LinearLayout mGifts;
    protected ViewPager mGiftlists;
    protected LinearLayout mGiftPageLayout;
    protected GiftShowView mAnimGift1;
    protected GiftShowView mAnimGift2;
    protected CheckBox mDanmaku;
    protected DanmakuView mDanmakuView;
    protected HeartLayout mHeart;
    protected ImageView mSend;
    protected ImageView mGive;
    protected FlashView mFlashView;
    protected DanmuControl mDanmuControl;
    protected TextView mGiftMoney;
    protected TextView mGiftName;

    /**
     * 判断是否为观众界面, 默认为观众
     */
    protected boolean isAudience = true;
    protected String mChatChannel;

    private boolean isReconnected = false;

    @Override
    public boolean onBackPressed() {
        if (mGifts.isShown()) {
            mGifts.setVisibility(View.GONE);
            return true;
        }
        if (KeyBoardUtil.isKeyboardActive(context)) {
            KeyBoardUtil.closeKeybord(context, getActivity().getCurrentFocus());
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        MqttBiz.registerReceiver(context, this);

        if (MqttBIZ != null && MqttBIZ.isConnected()) {
            Bundle data = new Bundle();
            data.putString(MqttConstants.topic, mChatChannel);
            MqttBIZ.subscribe(data);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MqttBiz.unregisterReceiver();
        if (MqttBIZ != null && MqttBIZ.isConnected()) {
            Bundle data = new Bundle();
            data.putString(MqttConstants.topic, mChatChannel);
            MqttBIZ.unsubscribe(data);
        }
        mGiftList.clear();
    }

    @Override
    public void onDestroyView() {
        removeSoftKeyboardListener();
        super.onDestroyView();
    }
    ///////////////////////////////////////////////////////////////////////////
    // Danmaku必须调用，跟随生命周期
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();
        mDanmuControl.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDanmuControl.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDanmuControl.destroy();
    }

    public static GiftFragment newInstance(MqttInfo bean) {
        GiftFragment fragment = new GiftFragment();
        Bundle args = new Bundle();
        args.putParcelable(BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }

    public int getLayout() {
        return R.layout.gfragment_gift;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(getLayout(), container, false);
        // 键盘高度改变是改变布局
        setOnKeyboardChangedExtraListener(this);
        mBean = getArguments().getParcelable(BEAN);
        if (mBean != null)
            isAudience = mBean.isAudience();
        mChatChannel = Info.AppName() == null ? mBean.getChatChannel() : Info.AppName() + ":" + mBean.getChatChannel();
        mMsg = $(view, R.id.igift_msg);
        mText = $(view, R.id.igift_text);
        mGift = $(view, R.id.igift_gift);
        mChatzone = $(view, R.id.igift_chatzone);
        mTool = $(view, R.id.igift_tool);
        mMsglist = $(view, R.id.igift_msglist);
        mSendzone = $(view, R.id.igift_sendzone);
        mSend = $(view, R.id.igift_send);
        mGifts = $(view, R.id.igift_gifts);
        mGiftlists = $(view, R.id.igift_giftlists);
        mGiftPageLayout = $(view, R.id.igift_points);
        mGive = $(view, R.id.igift_give);
        mAnimGift1 = $(view, R.id.anim_gift1);
        mAnimGift2 = $(view, R.id.anim_gift2);
        mDanmaku = $(view, R.id.igift_danmaku);
        mHeart = $(view, R.id.heart);
        mFlashView = $(view, R.id.flashview);
        mDanmakuView = $(view, R.id.danmakuView);
        mGiftMoney = $(view, R.id.igift_money);
        mGiftName = $(view, R.id.igift_name);
        $(view, R.id.mainLayout).setOnClickListener(this);
        initUserMoney(mGiftMoney);
        return view;
    }

    protected void initUserMoney(TextView money) {
    }

    @Override
    protected void setListener() {
        MqttBIZ = MqttBiz.getInstance(context, this);
        mMsg.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mGift.setOnClickListener(this);
        mGive.setOnClickListener(this);

        mDanmaku.setOnCheckedChangeListener(this);
        mText.setOnEditorActionListener(this);

        mMsglist.setOnTouchListener(this);
        mChatzone.setOnTouchListener(this);
//        mChatzone.setLongClickable(true);
        mMsglist.setOnItemLongClickListener(this);

        /**
         * todo
         * 增加事件监听, 以收到该事件结束的消息, onEventEnd
         */
        addEventListener(GEventCode.Http_GetGifts);
        addEventListener(GEventCode.Http_SendChat);
        addEventListener(GEventCode.Http_SendGift);
        addEventListener(GEventCode.Local_Barrage);
        addEventListener(GEventCode.Local_Gift);
        addEventListener(GEventCode.Local_Like);
        addEventListener(GEventCode.Local_UserJoin);
        addEventListener(GEventCode.Local_UserLeave);

        addSoftKeyboardListener(mChatzone);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initMessageAdapter();

        mDanmuControl = new DanmuControl(context);
        //设置弹幕视图
        mDanmuControl.setDanmakuView(mDanmakuView);

        try2InitData();
    }

    /**
     * 初始化消息适配器
     */
    protected void initMessageAdapter() {
        mMsgListAdapter = new PublicMessageAdapter(context, null);
        mMsglist.setAdapter(mMsgListAdapter);
    }

    /**
     * 初始化礼物列表
     */
    protected void initGiftList() {
        // 执行获取礼物清单事件
        runEvent(GEventCode.Http_GetGifts, Info.ApiToken());
    }

    /**
     * 尝试连接Mqtt服务器
     */
    protected void connectMqtt() {
        // 连接聊天室
        if (mBean == null) {
            return;
        }
        Bundle data = new Bundle();
        data.putString(MqttConstants.username, Info.Username());
        data.putString(MqttConstants.token, Info.ApiToken().replace("Bearer ", ""));
        data.putString(MqttConstants.server, mBean.getChatUrl());
        Bundle build = new MqttConfig.Option(data).build();
        // 连接消息服务器
        if (MqttBIZ != null)
            MqttBIZ.connect(build);
        onConnecting();
    }

    /**
     * 尝试初始化礼物列表及连接消息服务器, 这里需要使用API TOKEN
     */
    protected void try2InitData() {
        connectMqtt();
        if (EngineRunner.gifts != null) {
            handleGiftList(EngineRunner.gifts.getItems());
        } else {
            initGiftList();
        }
    }

    /**
     * 需要处理点击事件时，重写该方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.igift_msg) {
            onClickMsg();
        } else if (id == R.id.igift_send) {
            onClickSend();
        } else if (id == R.id.igift_gift) {
            onClickGift();
        } else if (id == R.id.igift_give) {
            onClickGiveGift();
        }
    }
    /*************************            处理点击事件          ****************************/
    /**
     * 点击消息图片
     */
    public void onClickMsg() {
        mGifts.setVisibility(View.GONE);
        if (mTool.isShown())
            mTool.setVisibility(View.GONE);
        KeyBoardUtil.openKeybord(context, mText);
    }

    /**
     * 发消息
     */
    public void onClickSend() {
        String msg = mText.getText().toString();
        if (mBean == null || TextUtils.isEmpty(msg)) {
            return;
        }
        if (mDanmaku.isChecked()) {
            runEvent(GEventCode.Http_SendBarrage, mChatChannel, msg);
        } else {
            runEvent(GEventCode.Http_SendChat, mChatChannel, msg);
        }
        mText.setText(null);
    }

    /**
     * 获取礼物清单
     */
    public void onClickGift() {
        if (mGiftBeans != null && mGiftBeans.size() > 0) {
            if (!mGifts.isShown()) {
                mGifts.setVisibility(View.VISIBLE);
            }
            mTool.clearFocus();
            if (mTool.isShown()) {
                mTool.clearFocus();
                mTool.setVisibility(View.GONE);
            }
        } else {
            if (mGiftlistCallback != null) {
                List<GiftItem> items = mGiftlistCallback.findGiftlist();
                if (items != null) {
                    handleGiftList(items);
                    onClickGift();
                } else {
                    Toast.makeText(context.getApplicationContext(), "加载中,请稍候...", Toast.LENGTH_SHORT).show();
                    initGiftList();
                }
            } else {
                Toast.makeText(context.getApplicationContext(), "加载中,请稍候...", Toast.LENGTH_SHORT).show();
                initGiftList();
            }
        }
    }

    /**
     * 送礼物
     */
    public void onClickGiveGift() {
        if (mCurrentGift == null) {
            return;
        }
        EngineRunner.API_ENGINE.sendGift(Info.ApiToken(), Info.Userid(), mCurrentGift.getGiftId(), mBean.getReceiverUuid(), mChatChannel).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int code = response.code();
                String balance = "";
                if (code == 200) {
                    Result body = response.body();
                    balance = String.valueOf(body.getBalance());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject obj = new JSONObject(errorBody);
                        balance = obj.optString("error_description");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mGiftMoney.setText(balance);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d(TAG, t == null ? "" : t.getMessage());
            }
        });
    }

    /*************************            处理键盘高度改变事件          ****************************/
    /**
     * 键盘高度改变是显示部分界面,
     *
     * @param isKeyBoardShow
     * @param keyboardHeight
     */
    @Override
    public void OnKeyboardChanged(boolean isKeyBoardShow, int keyboardHeight) {
        if (isKeyBoardShow) {
            setSendzoneMargin(keyboardHeight);
            mSendzone.setVisibility(View.VISIBLE);
            mText.requestFocus();
            if (keyboardHeight > ScreenUtil.getScreenHeightInPx(context) >> 3 || mGifts.isShown()) {
                mTool.setVisibility(View.GONE);
            }
        } else {
            if (mGifts.isShown()) {
                mSendzone.setVisibility(View.GONE);
                mMsglist.setVisibility(View.GONE);
            } else {
                setSendzoneMargin(0);
                mSendzone.setVisibility(View.INVISIBLE);
                mMsglist.setVisibility(View.VISIBLE);
            }
            if (keyboardHeight < ScreenUtil.getScreenHeightInPx(context) >> 3 && !mGifts.isShown()) {
                mTool.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void setSendzoneMargin(int margin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = margin;
        mSendzone.setLayoutParams(lp);
    }

    /*************************            MQTT消息回调事件          ****************************/
    /**
     * 消息发布成功
     *
     * @param action
     */
    @Override
    public void onActionSuccess(Action action) {
        System.out.println("success-action:" + action);
        if (action.equals(Action.CONNECT)) {
            Bundle data = new Bundle();
            //data.putString(MqttConstants.topic, AppName + ":" + mChatChannel);
            data.putString(MqttConstants.topic, mChatChannel);
            if (MqttBIZ != null)
                MqttBIZ.subscribe(data);
        } else if (action.equals(Action.SUBSCRIBE)) {
            onSubscribed(isReconnected);
        }
    }

    protected void onConnecting() {

    }

    protected void onSubscribed(boolean isReconnected) {

    }

    /**
     * 消息发布失败
     *
     * @param action
     */
    @Override
    public void onActionFailed(Action action, Throwable throwable) {
        System.out.println("failed-action:" + action + ";" + throwable == null ? "" : throwable.getMessage());
    }

    /**
     * 收到消息
     *
     * @param topic   消息通道
     * @param message 消息
     */
    @Override
    public synchronized void onMessageArrived(String topic, String message) {
        System.out.println(message);
        mMsgListAdapter.addData(message, new PublicMessageAdapter.OnMessageDataChanged() {
            @Override
            public void onChanged() {
                mMsglist.smoothScrollToPosition(mMsgListAdapter.getCount() - 1);
            }
        });


    }

    /**
     * 连接断开
     *
     * @param cause 断开原因
     */
    @Override
    public void onConnectLost(Throwable cause) {
        if (MqttBIZ != null) {
            MqttBIZ.reconnect();
            isReconnected = true;
        }
    }

    /**
     * 各种touch事件
     *
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (KeyBoardUtil.isKeyboardActive(context)) {
//                KeyBoardUtil.closeKeybord(context, mText);
//            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isAudience) {
                runEvent(GEventCode.Http_SendLike, mChatChannel);
            }
            if (mGifts.isShown()) {
                mGifts.setVisibility(View.GONE);
                mTool.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onTouch(view, event);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //if (e1.getAction() == MotionEvent.ACTION_DOWN && e2.getAction() == MotionEvent.ACTION_MOVE)
        if (distanceY < 200 && distanceY > 100) {// 往上滑
            if (!KeyBoardUtil.isKeyboardActive(context)) {
                KeyBoardUtil.openKeybord(context, mText);
            }
            if (mTool.isShown())
                mTool.setVisibility(View.GONE);
        } else if (distanceY > -160 && distanceY < -80) {// 往下滑
            if (KeyBoardUtil.isKeyboardActive(context)) {
                KeyBoardUtil.closeKeybord(context, mText);
            }
            if (!mTool.isShown())
                mTool.setVisibility(View.VISIBLE);
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    /**
     * 处理礼物消息
     */
    protected void handleGiftEvent() {
        handleGiftChannel(mGiftChannel1, mAnimGift1);
        handleGiftChannel(mGiftChannel2, mAnimGift2);
    }

    private void handleGiftChannel(final Queue<GiftReceived> channel, final GiftShowView view) {
        if (channel.size() > 0 && view != null) {
            GiftReceived gift = channel.element();
            String giftId = gift.getGiftId();
            String userId = gift.getUserId();
            String object = giftId + ";" + userId;
            if (view.isShowing()) {
                addGiftAnim(channel, view, giftId, userId);
                return;
            } else {
                view.setSymbol(object);
            }
            view.setAvartar(R.drawable.icon_default_user);
            GiftReceived e = channel.remove();
            if (!TextUtils.isEmpty(e.getAvatarUrl())) {
                view.setAvartar(e.getAvatarUrl());
            }
            String gId = e.getGiftId();
            for (GiftItem g : mGiftBeans) {
                String id = g.getGiftId();
                if (gId.equals(id)) {
                    String name = g.getDisplayName();
                    view.setContent("送了一个" + name);
                    Animator animator = g.getAnimator();
                    if (animator != null && animator.isAvailable()) {
                        mFlashList.add(animator);
                        handleFlashAnim();
                        view.setCurGift(e, g, animator);
                    } else {
                        view.setCurGift(e, g);
                    }
                    break;
                }
            }
            view.setGiftShowingListener(new GiftShowView.GiftShowingListener() {
                @Override
                public void onStart() {
                    handleGiftChannel(channel, view);
                    handleGiftChannels();
                }

                @Override
                public void onStop() {
                    handleGiftChannel(channel, view);
                    handleGiftChannels();
                }
            });
            view.start();
        }

    }

    /**
     * 如果可以刷新(增加)动画显示次数, 返回true
     *
     * @param channel
     * @param view
     * @param giftId
     * @return
     */
    protected boolean addGiftAnim(Queue<GiftReceived> channel, GiftShowView view, String giftId, String userId) {
        String object = giftId + ";" + userId;
        if (view.isShowing()) {
            if (object.equals(view.getSymbol())) {
                channel.remove();
                view.addAnimTime();
                if (view.isAnimatorAvailable()) {
                    mFlashList.add(view.getAnimator());
                }
                handleGiftChannels();
                return true;
            }
        }
        return false;
    }

    /**
     * 处理Flash动画
     */
    protected void handleFlashAnim() {
        if (mFlashList == null || mFlashList.size() == 0) {
            return;
        }
        if (mFlashView == null || !mFlashView.isStoped()) {
            return;
        }
        Animator animator = mFlashList.remove();
        String flashName = animator.getFileName();
        String animName = animator.getAnimName();
        mFlashView.reload(flashName, FlashUtil.FLASH_DIR);
        mFlashView.play(animName, FlashDataParser.FlashLoopTimeOnce);
        mFlashView.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
            @Override
            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                if (e.equals(FlashDataParser.FlashViewEvent.STOP)) {
                    handleFlashAnim();
                    mFlashView.setLayerType(View.LAYER_TYPE_NONE, null);
                } else if (e.equals(FlashDataParser.FlashViewEvent.START)) {
                    mFlashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentGift = (GiftItem) parent.getAdapter().getItem(position);
        int count = parent.getCount();
        AppCompatCheckBox mCurrentbox = parent.getChildAt(position).findViewById(R.id.gift_select);
        if (mCurrentbox.getVisibility() == View.VISIBLE) {
            mGiftName.setText("请选择礼物:");
            mCurrentbox.setVisibility(View.INVISIBLE);
            mCurrentGift = null;
        } else {
            mCurrentbox.setVisibility(View.VISIBLE);
            mGiftName.setText("已选择:" + mCurrentGift.getDisplayName());
        }
        for (int i = 0; i < count; i++) {
            AppCompatCheckBox box = parent.getChildAt(i).findViewById(R.id.gift_select);
            if (position != i) {
                if (box.getVisibility() == View.VISIBLE) {
                    box.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        int count = mGiftlists.getChildCount();
        for (int i = 0; i < count; i++) {
            if (i != position) {
                GridView view = (GridView) mGiftlists.getChildAt(i);
                int childCount = view.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childAt = view.getChildAt(j);
                    childAt.findViewById(R.id.gift_select).setVisibility(View.GONE);
                }
            }
        }
        InitPoint(position);
        mCurrentGift = null;
        mGiftName.setText("请选择礼物:");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化游标
     */
    private void InitPoint(int position) {
        mGiftPages.clear();
        mGiftPageLayout.removeAllViews();
        ImageView imageView;
        for (int i = 0; i < mGiftPage; i++) {
            imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = DisplayUtil.dp2px(context, 4);
            layoutParams.rightMargin = DisplayUtil.dp2px(context, 4);
            layoutParams.topMargin = DisplayUtil.dp2px(context, 10);
            layoutParams.bottomMargin = DisplayUtil.dp2px(context, 10);

            if (i == position) {
                imageView.setBackgroundResource(R.drawable.bg_full_circle_pink);
            } else {
                imageView.setBackgroundResource(R.drawable.bg_full_circle_white);
            }
            mGiftPageLayout.addView(imageView, layoutParams);
        }
    }

    /**
     * CheckBox状态改变
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 处理弹幕开关事件
        if (isChecked) {
            mText.setHint("弹幕1条/金币");
        } else {
            mText.setHint(null);
        }
    }

    /**
     * 编辑框弹出软键盘后, 软件盘的下一步动作ActionButton
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                onClickSend();
                break;
        }
        return true;
    }

    public void handleGiftList(List<GiftItem> giftItems) {
        if (mGiftlistCallback != null) {
            mGiftlistCallback.initGiftlist(giftItems);
        }
        mGiftBeans.clear();
        mGiftBeans.addAll(giftItems);
        List<View> giftViews = new ArrayList<View>();
        List<GiftItemAdapter> mGiftAdapter = new ArrayList<GiftItemAdapter>();
        mGiftPage = mGiftBeans.size() / 8 + 1;
        int curPage = 0;
        for (int i = 0; i < mGiftPage; i++) {
            List<GiftItem> beansPage = new ArrayList<GiftItem>();
            for (int j = 0; j < 8; j++) {
                if (mGiftBeans.size() == j + curPage * 8)
                    break;
                beansPage.add(mGiftBeans.get(j + curPage * 8));
            }
            curPage++;
            GridView view = new GridView(context);
            view.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            view.setOnItemClickListener(this);
            GiftItemAdapter adapter = new GiftItemAdapter(context, beansPage);
            view.setAdapter(adapter);
            mGiftAdapter.add(adapter);
            view.setNumColumns(4);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            giftViews.add(view);
        }
        mGiftlists.addOnPageChangeListener(this);
        mGiftlists.setAdapter(new GiftViewPagerAdapter(giftViews));
        InitPoint(0);
        for (GiftItem item : mGiftBeans) {
            Animator animator = item.getAnimator();
            if (animator != null) {
                String url = animator.getUrl();
                String fileName = animator.getFileName();
                runEvent(GEventCode.Http_DownloadZip, context, url, fileName);
            }
        }
    }

    /**
     * 网络请求结束
     * addEventListener(GEventCode.Http_GetGifts);
     * addEventListener(GEventCode.Http_SendChat);
     * addEventListener(GEventCode.Http_SendGift);
     * addEventListener(GEventCode.Local_Barrage);
     * addEventListener(GEventCode.Local_Gift);
     * addEventListener(GEventCode.Local_Like);
     * addEventListener(GEventCode.Local_UserJoin);
     * addEventListener(GEventCode.Local_UserLeave);
     *
     * @param event
     */
    @Override
    public void onEventEnd(Event event) {
        int code = event.getEventCode();
        if (GEventCode.Local_Barrage == code) {
            if ("live".equals(mBean == null ? "" : mBean.getStatus())) {
                String avatarUrl = event.getParamAtIndex(0);
                String username = event.getParamAtIndex(1);
                String content = event.getParamAtIndex(2);
//            DanmakuReceived model = new DanmakuReceived(avatarUrl, username, content);
                Danmu danmu = new Danmu(0, new Random().nextInt(), "Comment", avatarUrl, username + ":" + content);
                // 添加弹幕
                mDanmuControl.addDanmu(danmu);
            }
        } else if (GEventCode.Local_Gift == code) {
            String userId = event.getParamAtIndex(0);
            String avatarUrl = event.getParamAtIndex(1);
            String username = event.getParamAtIndex(2);
            String content = event.getParamAtIndex(3);
            GiftReceived model = new GiftReceived(userId, avatarUrl, username, content);
            mGiftList.add(model);
            handleGiftChannels();
        } else if (GEventCode.Local_Like == code) {
            if (mHeart != null) {
                mHeart.addHeart(mHeart.randomColor());
            }
        } else if (GEventCode.Http_GetGifts == code) {
            if (event.isSuccess()) {
                GiftInfo model = event.getReturnParamAtIndex(0);
                handleGiftList(model.getItems());
            }
        }
    }

    private void handleGiftChannels() {
        if (mGiftList.isEmpty()) return;
        GiftReceived element = mGiftList.element();
        if (mGiftChannel1.isEmpty()) {
            mGiftChannel1.add(mGiftList.remove());
            handleGiftEvent();
            return;
        } else {

            GiftReceived targetElement1 = mGiftChannel1.element();
            if (targetElement1.getUserId().equals(element.getUserId())) {
                mGiftChannel1.add(mGiftList.remove());
                handleGiftEvent();
                return;
            }

            if (mGiftChannel2.isEmpty()) {
                mGiftChannel2.add(mGiftList.remove());
                handleGiftEvent();
                return;
            }

            GiftReceived targetElement2 = mGiftChannel1.element();

            if (targetElement2.getUserId().equals(element.getUserId())) {
                mGiftChannel2.add(mGiftList.remove());
                handleGiftEvent();
                return;
            }
        }
    }

    /**
     * 聊天信息区域,Item的长按事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (KeyBoardUtil.isKeyboardActive(context)) {
            KeyBoardUtil.closeKeybord(context, mText);
        }
        String displayName = mMsgListAdapter.getDisplayName(i);
        String text = mText.getText().toString();
        String point = " @" + displayName + " ";
        if (!TextUtils.isEmpty(text) && text.contains(point)) {
            return true;
        }
        mText.append(point);
        return true;
    }

    public void setGiftlistCallback(GiftlistCallback giftlistCallback) {
        mGiftlistCallback = giftlistCallback;
    }

    public GiftlistCallback getGiftlistCallback() {
        return mGiftlistCallback;
    }
}
