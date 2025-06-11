package tv.lycam.gift.widget;

import android.content.Context;
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
import tv.lycam.gift.runner.EngineRunner;
import tv.lycam.gift.widget.danmaku.CropCircleTransformation;


/**
 * Created by su on 16/5/26.
 */
public class GiftShowView_BACKUP extends RelativeLayout {
    private TextView content;
    private TextView name;
    private ImageView avartar;
    private ImageView gift;
    private TextView num;
    private Animator mAnimator;
    private GiftShowingListener mShowingListener;
//    protected Queue<GiftReceived> mGiftEndList = new LinkedBlockingQueue<>();

    private int restTimes = 0;

    private boolean isShowing;
    private AnimationBuilder mAnimStart;
    private AnimationBuilder mAnimGift;
    private AnimationBuilder mAnimUpdate;
    private AnimationBuilder mAnimOver;
    private String times = "X%d";
    private String tagFormat = "%s;%s";
    private String tag = "";
    private String mUserId;
    private OnAvatarClickListener mOnAvatarClickListener;

    public GiftShowView_BACKUP(Context context) {
        this(context, null);
    }

    public GiftShowView_BACKUP(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftShowView_BACKUP(Context context, AttributeSet attrs, int defStyleAttr) {
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

        Glide.with(getContext())
                .load(avartarUrl)
                .asBitmap()
                .fallback(R.drawable.icon_default_user)
                .error(R.drawable.icon_default_user)
                .placeholder(R.drawable.icon_default_user)
                .transform(new CropCircleTransformation(getContext()))
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

    public void setGift(String name, String giftUrl, String giftId) {
        this.name.setText(name);
        tag = String.format(tagFormat, giftId, name);
        Glide.with(getContext())
                .load(giftUrl)
                .crossFade()
                .into(this.gift);
    }

    public void setGift(String name, String giftUrl, String giftId, Animator animator) {
        this.setGift(name, giftUrl, giftId);
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

    public boolean equalAnim(String username, String giftId) {
        return String.valueOf(this.tag).equals(giftId + ";" + username);
    }

    public void addAnimTime() {
        restTimes++;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        void onStop();
    }

    public void setGiftShowingListener(GiftShowingListener mListener) {
        mShowingListener = mListener;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    private OnClickListener mAvatarClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnAvatarClickListener != null) {
                mOnAvatarClickListener.onAvatarClick(mUserId, GiftShowView_BACKUP.this);
            }
        }
    };

    public GiftShowView_BACKUP setOnAvatarClickListener(OnAvatarClickListener listener) {
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

