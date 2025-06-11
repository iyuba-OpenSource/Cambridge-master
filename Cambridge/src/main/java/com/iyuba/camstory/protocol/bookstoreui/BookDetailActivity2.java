package com.iyuba.camstory.protocol.bookstoreui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.bean.Util;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.BookDetail;
import com.iyuba.camstory.protocol.BookDetailRequest;
import com.iyuba.camstory.protocol.BookDetailResponse;
import com.iyuba.camstory.protocol.dao.DBHelper;
import com.iyuba.camstory.protocol.view.BabyPopWindow;
import com.iyuba.camstory.protocol.view.BadgeView;
import com.iyuba.camstory.utils.GitHubImageLoader;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BookDetailActivity2 extends SwipeBackActivity {
    private Context mContext;
    private BookDetail bookDetail;
    private BabyPopWindow popWindow;
    private BadgeView commentBadge;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private int goodsNum = 0;

    @BindView(R.id.book_scroll)
    ScrollView bookScroll;
    @BindView(R.id.iv_title_back)
    ImageButton backBtn;
    @BindView(R.id.book)
    TextView book;
    @BindView(R.id.micro_class)
    TextView microClass;
    @BindView(R.id.app)
    TextView app;
    @BindView(R.id.edit_img)
    ImageView editImg;
    @BindView(R.id.edit_info)
    TextView editInfo;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.origin_price)
    TextView originPrice;
    @BindView(R.id.book_name)
    TextView bookName;
    @BindView(R.id.author_name)
    TextView authorName;
    @BindView(R.id.press_name)
    TextView pressName;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.content_img)
    ImageView contentImg;
    @BindView(R.id.content_info)
    TextView contentInfo;
    @BindView(R.id.author_img)
    ImageView authorImg;
    @BindView(R.id.class_img)
    ImageView classImg;
    @BindView(R.id.app_img)
    ImageView appImg;
    @BindView(R.id.book_introduction)
    TextView bookIntro;
    @BindView(R.id.micro_class_introduction)
    TextView microClassIntro;
    @BindView(R.id.app_introduction)
    TextView appIntro;
    @BindView(R.id.customer_service)
    ImageView customerService;
    @BindView(R.id.shop_cart)
    ImageView shopCart;
    @BindView(R.id.add_goods)
    TextView addGoods;
    @BindView(R.id.buy_now)
    TextView buyNow;
    @BindView(R.id.back_ground)
    LinearLayout backBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail2);

        mContext = this;
        ButterKnife.bind(this);

        this.mDBHelper = DBHelper.getInstance(mContext);
        this.mDB = mDBHelper.getWritableDatabase();

        commentBadge = new BadgeView(mContext);
        commentBadge.setTextSize(8);
        commentBadge.setBackground(7, Color.parseColor("#d3321b"));
        popWindow = new BabyPopWindow(this, AccountManager.getInstance().userId+"");

        String id = getIntent().getStringExtra("bookId");
        String bookorg = getIntent().getStringExtra("bookorg");
        originPrice.setText(bookorg);
        originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ExeProtocol.exe(
                new BookDetailRequest(id),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        BookDetailResponse response = (BookDetailResponse) bhr;
                        if (response.result.equals("1")) {
                            bookDetail = response.bookDetail;
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void error() {

                    }
                });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookScroll.scrollTo(0, (int) bookIntro.getY());
                book.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                microClass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                app.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
        });
        microClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookScroll.scrollTo(0, (int) microClassIntro.getY());
                book.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                microClass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                app.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
        });
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookScroll.scrollTo(0, (int) appIntro.getY());
                book.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                microClass.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                app.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
            }
        });

        addGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccountManager.getInstance().islinshi){
                    showNormalDialog();

                }else {
                    popWindow.setValue(bookDetail, backBg);
                    backBg.setVisibility(View.VISIBLE);
                    popWindow.showAsDropDown(view);
                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccountManager.getInstance().islinshi){
                    showNormalDialog();

                }else {
                bookDetail.num = 1;
                ArrayList<BookDetail> bookDetails = new ArrayList<BookDetail>();
                bookDetails.add(bookDetail);
                Bundle bundle = new Bundle();
                bundle.putSerializable("books", bookDetails);
                Intent intent = new Intent();
                intent.putExtra("books", bundle);
                intent.setClass(mContext, OrderConfirmActivity.class);
                startActivity(intent);
                }
            }
        });

        shopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShopCartActivity.class));
            }
        });

        customerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.startQQ(mContext, "3099007489");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentBadge.setTargetView(shopCart);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mDB!=null){
            goodsNum = 0;
            Cursor cursor = mDB.query("shop_cart", new String[]{"editImg", "editInfo", "totalPrice", "number"},
                    "uid=?", new String[]{AccountManager.getInstance().userId+""}, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                goodsNum++;
            }


            commentBadge.setText(String.valueOf(goodsNum));

        }
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final android.app.AlertDialog.Builder normalDialog =
                new android.app.AlertDialog.Builder(mContext);
        normalDialog.setIcon(R.drawable.iyubi_icon);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("临时用户无法购买和添加购物车！");
        normalDialog.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Intent intent = new Intent();
                        intent.setClass(mContext, LoginActivity.class);
                        startActivity(intent);
                        BookDetailActivity2.this.finish();

                    }
                });
        normalDialog.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do

                    }
                });
        // 显示
        normalDialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    GitHubImageLoader.Instace(mContext).setRawPic(bookDetail.getEditImg(), editImg,
                            R.drawable.failed_image);
                    editInfo.setText(bookDetail.editInfo);
                    totalPrice.setText("¥" + bookDetail.totalPrice);
                    bookName.setText(bookDetail.name);
                    authorName.setText(bookDetail.bookAuthor);
                    pressName.setText(bookDetail.publishHouse);
                    desc.setText(bookDetail.desc);
                    GitHubImageLoader.Instace(mContext).setRawPic(bookDetail.contentImg, contentImg,
                            R.drawable.failed_image);
                    contentInfo.setText(bookDetail.contentInfo);
                    GitHubImageLoader.Instace(mContext).setRawPic(bookDetail.authorImg, authorImg,
                            R.drawable.failed_image);
                    GitHubImageLoader.Instace(mContext).setRawPic(bookDetail.classImg, classImg,
                            R.drawable.failed_image);
                    GitHubImageLoader.Instace(mContext).setRawPic(bookDetail.appImg, appImg,
                            R.drawable.failed_image);

//                    bookScroll.scrollTo(0, 200);
                    break;
            }
        }
    };
}
