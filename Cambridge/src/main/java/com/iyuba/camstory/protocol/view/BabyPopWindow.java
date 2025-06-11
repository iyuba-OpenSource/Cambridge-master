package com.iyuba.camstory.protocol.view;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.BookDetail;
import com.iyuba.camstory.protocol.dao.DBHelper;
import com.iyuba.camstory.utils.GitHubImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@SuppressLint("CommitPrefEdits")
public class BabyPopWindow implements OnDismissListener, OnClickListener {
    private TextView nowPrice, content, confirm;
    private ImageView img;
    private LinearLayout blackBg;

    private PopupWindow popupWindow;
    private OnItemClickListener listener;
    private Context context;
    private BookDetail bookDetail = new BookDetail();

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private String uid;


    public BabyPopWindow(final Context context, final String uid) {
        this.context = context;
        this.uid = uid;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_popwindow, null);
        nowPrice = view.findViewById(R.id.book_now_price);
        content = view.findViewById(R.id.content_info);
        confirm = view.findViewById(R.id.confirm);
        img = view.findViewById(R.id.book_img);

        confirm.setOnClickListener(this);

        this.mDBHelper = DBHelper.getInstance(context);
        this.mDB = mDBHelper.getWritableDatabase();

        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(R.style.noAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                blackBg.setVisibility(View.GONE);
            }
        });

        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                String date = sdf.format(new Date());

                Cursor cursor = mDB.query("shop_cart", new String[]{"id"}, "uid=? and id=?", new String[]{AccountManager.getInstance().userId+"", bookDetail.id}, null, null, null);
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    ContentValues cv = new ContentValues();
                    cv.put("appImg", bookDetail.appImg);
                    cv.put("appInfo", bookDetail.appInfo);
                    cv.put("appName", bookDetail.appName);
                    cv.put("appPrice", bookDetail.appPrice);
                    cv.put("authorImg", bookDetail.authorImg);
                    cv.put("authorInfo", bookDetail.authorInfo);
                    cv.put("bookAuthor", bookDetail.bookAuthor);
                    cv.put("bookPrice", bookDetail.bookPrice);
                    cv.put("classImg", bookDetail.classImg);
                    cv.put("classInfo", bookDetail.classInfo);
                    cv.put("classPrice", bookDetail.classPrice);
                    cv.put("contentImg", bookDetail.contentImg);
                    cv.put("contentInfo", bookDetail.contentInfo);
                    cv.put("createTime", bookDetail.createTime);
                    cv.put("desc", bookDetail.desc);
                    cv.put("editImg", bookDetail.editImg);
                    cv.put("editInfo", bookDetail.editInfo);
                    cv.put("flg", bookDetail.flg);
                    cv.put("groups", bookDetail.groups);
                    cv.put("id", bookDetail.id);
                    cv.put("imgSrc", bookDetail.imgSrc);
                    cv.put("name", bookDetail.name);
                    cv.put("pkg", bookDetail.pkg);
                    cv.put("publishHouse", bookDetail.publishHouse);
                    cv.put("totalPrice", bookDetail.totalPrice);
                    cv.put("types", bookDetail.types);
                    cv.put("updateTime", bookDetail.updateTime);
                    cv.put("bookId", bookDetail.bookId);
                    cv.put("uid", uid);
                    cv.put("number", 1);

                    long result = mDB.replace("shop_cart", null, cv);
                    if (result > 0) {
                        Toast.makeText(context, "已添加至购物车", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                } else {
                    mDB.execSQL("update shop_cart set number = number + 1 where id = " + bookDetail.id + " and uid =" + AccountManager.getInstance().userId);
                    Toast.makeText(context, "已添加至购物车", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }


            }
        });
    }


    public interface OnItemClickListener {
        void onClickOKPop();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onDismiss() {

    }

    public void setValue(BookDetail bookDetail, LinearLayout blackBg) {
        this.bookDetail = bookDetail;
        GitHubImageLoader.Instace(context).setRawPic(bookDetail.editImg, img,
                R.drawable.failed_image);
        this.nowPrice.setText("¥" + bookDetail.totalPrice);
        this.content.setText(bookDetail.editInfo);
        this.blackBg = blackBg;
    }

    public void showAsDropDown(View parent) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    public void dissmiss() {
        popupWindow.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:


                break;
            default:
                break;
        }
    }

    private void setSaveData() {


    }

}
