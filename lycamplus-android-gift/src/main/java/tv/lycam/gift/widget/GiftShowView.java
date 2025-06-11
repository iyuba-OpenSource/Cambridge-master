package tv.lycam.gift.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import tv.lycam.gift.R;
import tv.lycam.gift.entity.Animator;
import tv.lycam.gift.entity.GiftItem;
import tv.lycam.gift.entity.GiftReceived;
import tv.lycam.gift.runner.EngineRunner;
import tv.lycam.gift.widget.danmaku.CropCircleTransformation;


/**
 * Created by su on 16/5/26.
 */
public class GiftShowView extends RelativeLayout {
    private TextView content;
    private TextView name;
    private ImageView avartar;
    private ImageView gift;
    private TextView num;
    private Animator mAnimator;
    private GiftReceived mCurUser;
    private GiftItem mCurGift;
    private GiftShowingListener mShowingListener;
//    protected Queue<GiftReceived> mGiftEndList = new LinkedBlockingQueue<>();

    private int restTimes = 0;

    private boolean isShowing;
    private AnimationBuilder mAnimStart;
    private AnimationBuilder mAnimGift;
    private AnimationBuilder mAnimUpdate;
    private AnimationBuilder mAnimOver;
    private String times = "X%d";
    //    private String tagFormat = "%s;%s";
    private String symbol = "";
    private OnAvatarClickListener mOnAvatarClickListener;

    public GiftShowView(Context context) {
        this(context, null);
    }

    public GiftShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.custom_view_giftshow, this);
        content = findViewById(R.id.user_content);
        name = findViewById(R.id.user_name);
        avartar = findViewById(R.id.user_avatar);
        gift = findViewById(R.id.user_gift);
        num = findViewById(R.id.user_num);
        num.setText(String.format(times, 1));
        avartar.setOnClickListener(mAvatarClickListener);
    }

    private void initAnim() {
        final int width = getWidth();
        int height = getHeight();
        mAnimStart = ViewAnimator.animate(this)
                .translationX(-width, 0)
                .duration(1500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        isShowing = true;
                        if (mShowingListener != null) {
                            mShowingListener.onStart();
                        }
                        setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    }
                })
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        mAnimGift.start();
                    }
                });
        mAnimGift = ViewAnimator.animate(gift)
                .translationX(-width, 0)
                .alpha(0, 0.2f, 0.6f, 1)
                .duration(500)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        mAnimUpdate.start();
                    }
                });

        mAnimOver = ViewAnimator.animate(this)
                .translationY(0, 0)
                .duration(1000)
                .translationY(0, -height)
                .alpha(1, 0)
                .duration(1000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        isShowing = false;
                        setTranslationX(-width);
                        setCount(1);
                        setTranslationX(-width);
                        setAlpha(1f);
                        setTranslationY(0);
                        gift.setAlpha(0f);
                        setVisibility(View.INVISIBLE);
                        if (mShowingListener != null) {
                            mShowingListener.onStop();
                        }
                        setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                });

        animUpdate();
    }


    private void animUpdate() {
        mAnimUpdate = ViewAnimator.animate(num)
                .alpha(0, 1, 1, 1)
                .scaleX(0.3f, 1.25f, 0.8f, 1)
                .scaleY(0.3f, 1.25f, 0.8f, 1)
                .duration(700)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if (restTimes == 0) {
                            mAnimOver.start();
                        } else {
                            restTimes--;
                            setCount(getCount() + 1);
                            animUpdate();
                            mAnimUpdate.start();
                        }
                    }
                });
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void setAvartar(String avartarUrl) {

        Glide.with(EngineRunner.context)
                .load(avartarUrl)
                .asBitmap()
                .fallback(R.drawable.icon_default_user)
                .error(R.drawable.icon_default_user)
                .placeholder(R.drawable.icon_default_user)
                .transform(new CropCircleTransformation(EngineRunner.context))
                .into(this.avartar);
    }

    public boolean isAnimatorAvailable() {
        return mAnimator != null;
    }

    public Animator getAnimator() {
        return mAnimator;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public ImageView getGift() {
        return gift;
    }

    public void setCurGift(GiftReceived curUser, GiftItem curGift) {
        if (curGift == null || curUser == null) return;
        this.mCurUser = curUser;
        this.mCurGift = curGift;
        this.name.setText(curUser.getUserName());
//        symbol = String.format(tagFormat, curGift.getGiftId(), curUser.getUserId());
        Glide.with(getContext())
                .load(curGift.getImageUrl())
                .crossFade()
                .into(this.gift);
    }

    public void setCurGift(GiftReceived curUser, GiftItem curGift, Animator animator) {
        this.setCurGift(curUser, curGift);
        this.mAnimator = animator;
    }


    public void setCount(int num) {
        this.num.setText(String.format(times, num));
    }

    public int getCount() {
        return Integer.parseInt(this.num.getText().toString().replace("X", ""));
    }

    public void start() {
        initAnim();
        setVisibility(View.VISIBLE);
        isShowing = true;
        mAnimStart.start();
    }

    public String getUserId() {
        return mCurUser == null ? "" : TextUtils.isEmpty(mCurUser.getUserId()) ? "" : mCurUser.getUserId();
    }

    public void addAnimTime() {
        restTimes++;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setAvartar(int resId) {
        Glide.with(EngineRunner.context)
                .load(resId)
                .crossFade()
                .into(this.gift);
    }

    /**
     * 动画结束的回调
     */
    public interface GiftShowingListener {
        void onStart();

        void onStop();
    }

    public void setGiftShowingListener(GiftShowingListener mListener) {
        mShowingListener = mListener;
    }

    private OnClickListener mAvatarClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnAvatarClickListener != null && mCurUser != null) {
                mOnAvatarClickListener.onAvatarClick(mCurUser.getUserId(), GiftShowView.this);
            }
        }
    };

    public GiftShowView setOnAvatarClickListener(OnAvatarClickListener listener) {
        mOnAvatarClickListener = listener;
        return this;
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(String userId, View view);
    }

    public String getName() {
        return name.getText().toString();
    }
}

