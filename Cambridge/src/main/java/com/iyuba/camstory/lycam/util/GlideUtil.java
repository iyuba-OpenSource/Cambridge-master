//package com.iyuba.camstory.lycam.util;
//
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.RequestManager;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.iyuba.camstory.R;
//import com.iyuba.camstory.frame.CrashApplication;
//import com.iyuba.camstory.lycam.manager.RuntimeManager;
//
//import tv.lycam.gift.widget.danmaku.CropCircleTransformation;
//
//
///**
// * Created by su on 16/7/2.
// */
//public class GlideUtil {
//     static {
//        manager = Glide.with(RuntimeManager.getApplication());
//    }
//
//    private static RequestManager manager;
//
//    public static void loadNormalAvatar(ImageView view, String url) {
//        loadNormalAvatar(view, url, 0);
//    }
//
//    public static void loadNormalAvatar(ImageView view, String url, int obj) {
//        if (obj == 0) {
//            obj = R.drawable.icon_default_user;
//        }
//        manager.load(url)
//                .fallback(obj)
//                .error(obj)
//                .placeholder(obj)
////                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new CropCircleTransformation(CrashApplication.getInstance()))
//                .into(view);
//
//    }
//
//    public static void loadImage(ImageView view, String url) {
//        loadImage(view, url, 0);
//    }
//
//    public static void loadImage(ImageView view, String url, int obj){
//        manager.load(url)
//                .fallback(obj)
//                .error(obj)
//                .placeholder(obj)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(view);
//    }
//
//    public static void onStart() {
//        if (manager != null)
//            manager.onStart();
//    }
//
//    public static void onStop() {
//        if (manager != null)
//            manager.onStop();
//    }
//
//    public static void onDestroy() {
//        if (manager != null)
//            manager.onDestroy();
//    }
//}
