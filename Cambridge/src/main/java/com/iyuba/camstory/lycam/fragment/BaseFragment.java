package com.iyuba.camstory.lycam.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.iyuba.camstory.widget.WaittingDialog;
import com.umeng.analytics.MobclickAgent;

import tv.lycam.gift.callback.BackHandledInterface;
import tv.lycam.mqtt.util.MqttBiz;

/**
 * Created by su on 16/5/13.
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    //获取手机状态
    private static final int READ_PHONE_STATE = 1;
    //标识
    protected String TAG;
    protected Context context;
    //加载对话框
    protected WaittingDialog mLoadingDialog;

    protected static MqttBiz MqttBIZ;
    //相机
    protected static final int REQUEST_CAMERA = 1;
    //相册
    protected static final int REQUEST_GALLERY = 2;
    //裁剪
    protected static final int REQUEST_GALLERY_CUT = 3;
    //Y轴高度
    private float mAdjustViewY;

    protected BackHandledInterface<BaseFragment> mBackHandledInterface;
    //拍照uri
    protected Uri cameraUri;
    //缓存uri
    protected Uri cacheUri;
    //是否裁剪
    private boolean mIsCrop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getActivity() instanceof BackHandledInterface)) {
            this.mBackHandledInterface = (BackHandledInterface<BaseFragment>) getActivity();
        }

    }

    //返回
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        if (mBackHandledInterface != null) {
            mBackHandledInterface.setSelectedFragment(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        TAG = this.getClass().getSimpleName();
        View view = initView(inflater, container, savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
        return view;
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

    /**
     * 需要处理点击事件时，重写该方法
     *
     * @param v
     */
    public void onClick(View v) {
    }


    /*----------------------------SoftKeyboard----------------------------*/

    private OnSoftKeyboardStateChangedListener mSoftKeyboardStateChangedListener;
    private OnKeyboardChangedExtraListener mKeyboardChangedExtraListener;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private int minkeyboardHeight;

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
                if (!isKeyBoardShow && mAdjustViewY == 0) {
                    minkeyboardHeight = keyboardHeight;
                    mAdjustViewY = mRootView.getY();
                }
                int height = keyboardHeight - minkeyboardHeight;

                if (isKeyBoardShow) {
                    mRootView.setY(mAdjustViewY - height);
                } else {
                    mRootView.setY(mAdjustViewY);
                }
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
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > screenHeight / 3;
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
        getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    /*
     * 从相册获取
     */
    protected void launchGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为REQUEST_GALLERY
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    protected void launchGallery(boolean isCrop) {
        mIsCrop = isCrop;
        launchGallery();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getActivity().getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
    }
}
