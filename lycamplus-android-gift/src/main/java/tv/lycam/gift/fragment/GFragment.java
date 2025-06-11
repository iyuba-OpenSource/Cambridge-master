package tv.lycam.gift.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import tv.lycam.gift.callback.BackHandledInterface;
import tv.lycam.gift.core.AndroidEventManager;
import tv.lycam.gift.core.Event;
import tv.lycam.gift.core.EventManager;
import tv.lycam.mqtt.util.MqttBiz;

/**
 * Created by su on 16/5/13.
 */
public abstract class GFragment extends GestureFragment implements EventManager.OnEventListener, View.OnTouchListener {
    protected String TAG;
    protected Context context;
    protected static MqttBiz MqttBIZ;

    protected AndroidEventManager mEventManager = AndroidEventManager.getInstance();
    protected BackHandledInterface<GFragment> mBackHandledInterface;
    private float mAdjustViewY;

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BackHandledInterface) {
            this.mBackHandledInterface = (BackHandledInterface<GFragment>) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        TAG = this.getClass().getSimpleName();
        View view = initView(inflater, container, savedInstanceState);
        view.setOnTouchListener(this);
        view.setLongClickable(true);
        setListener();
        processLogic(savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        if (mBackHandledInterface != null)
            mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (MqttBIZ != null) {
            MqttBIZ.disconnect();
            MqttBIZ = null;
        }
    }

    /**
     * 初始化布局以及View控件
     */
    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected void setListener() {
    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected void processLogic(Bundle savedInstanceState) {
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T $(View rootView, int viewId) {

        View targetView = null;
        if (rootView != null) {
            targetView = rootView.findViewById(viewId);
        }
        return targetView == null ? null : (T) targetView;
    }

    /*----------------------------SoftKeyboard----------------------------*/

    private OnSoftKeyboardStateChangedListener mSoftKeyboardStateChangedListener;
    private OnKeyboardChangedExtraListener mKeyboardChangedExtraListener;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private int minkeyboardHeight;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }

    protected interface OnSoftKeyboardStateChangedListener {
        void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    protected interface OnKeyboardChangedExtraListener {
        void OnKeyboardChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    protected void setOnKeyboardChangedExtraListener(OnKeyboardChangedExtraListener keyboardChangedExtraListener) {
        this.mKeyboardChangedExtraListener = keyboardChangedExtraListener;
    }

    protected void addSoftKeyboardListener(final View mRootView) {
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;
        mSoftKeyboardStateChangedListener = new OnSoftKeyboardStateChangedListener() {
            @Override
            public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
//                if (!isKeyBoardShow && mAdjustViewY == 0) {
//                    minkeyboardHeight = keyboardHeight;
//                    mAdjustViewY = mRootView.getY();
//                }
//                int height = keyboardHeight - minkeyboardHeight;
////                    mRootView.setTranslationY(-h + height);
//
//                if (isKeyBoardShow) {
//                    mRootView.setY(mAdjustViewY - height);
//                } else {
//                    mRootView.setY(mAdjustViewY);
//                }
                if (mKeyboardChangedExtraListener != null) {
                    mKeyboardChangedExtraListener.OnKeyboardChanged(isKeyBoardShow, keyboardHeight);
                }
            }
        };
        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect r = new Rect();
                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - r.bottom;//( - r.top)
                boolean isKeyboardShowing = heightDifference > screenHeight / 5;
                mSoftKeyboardStateChangedListener.OnSoftKeyboardStateChanged(isKeyboardShowing, heightDifference);
            }
        };
        addLayoutChangeListener();
    }


    protected void removeSoftKeyboardListener() {
        if (mLayoutChangeListener != null) {
            removeLayoutChangeListener();
        }
        if (mSoftKeyboardStateChangedListener != null) {
            mSoftKeyboardStateChangedListener = null;
        }
    }

    private void removeLayoutChangeListener() {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            getActivity().getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
        mLayoutChangeListener = null;
    }

    private void addLayoutChangeListener() {
        //注册布局变化监听
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    protected View getRootView(){
        return getActivity().getWindow().getDecorView();
    }

    @Override
    public void onEventEnd(Event event) {

    }

    protected void addEventListener(int eventCode) {
        mEventManager.addEventListener(eventCode, this);
    }

    protected void runEvent(int eventCode, Object... params) {
        mEventManager.runEvent(eventCode, params);
    }

    public void runUiEvent(int eventCode, Object... params) {
        mEventManager.runUiEvent(eventCode, params);
    }
}
