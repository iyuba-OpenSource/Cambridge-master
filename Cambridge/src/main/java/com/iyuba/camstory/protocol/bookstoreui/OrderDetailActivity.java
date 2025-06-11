package com.iyuba.camstory.protocol.bookstoreui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.BookDetail;
import com.iyuba.camstory.protocol.OrderDetailRequest;
import com.iyuba.camstory.protocol.OrderDetailResponse;
import com.iyuba.camstory.protocol.ShopCartAdapter;
import com.iyuba.camstory.protocol.dao.DBHelper;
import com.iyuba.camstory.protocol.dao.OrderResult;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;
import java.util.ArrayList;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrderDetailActivity extends SwipeBackActivity {
    private Context mContext;
    private ArrayList<OrderResult> orderResults = new ArrayList<>();
    private ShopCartAdapter adapter;
    private String[] bookIds;
    private ArrayList<BookDetail> bookDetails = new ArrayList<>();
    private HashMap<String, BookDetail> bookDetailHashMap = new HashMap<>();
    private ArrayList<String> diffBookIds = new ArrayList<>();
    private int index = 0;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private LayoutInflater inflater;
    private View listFooter;
    private boolean scorllable = true;
    private int page = 1;
    private int count = 10;

    //    @Bind(R.id.linear_layout)
    //    private LinearLayout ll;
    //    @Bind(R.id.titlebar_back_button)
    private ImageButton backBtn;
    //    @Bind(R.id.titlebar_title)
    private TextView title;
    //    @Bind(R.id.list)
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_reslut);
        Log.e("OrderDetailActivity","onCreate");
        mContext = this;
//        ButterKnife.bind(this);
        inflater = getLayoutInflater();
        this.mDBHelper = DBHelper.getInstance(mContext);
        this.mDB = mDBHelper.getWritableDatabase();

        Cursor cursor = mDB.query("shop_cart", new String[]{"id", "totalPrice", "editInfo", "editImg"}, "uid=?", new String[]{"0"}, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BookDetail bookDetail = new BookDetail();
            bookDetail.id = cursor.getString(0);
            bookDetail.totalPrice = cursor.getString(1);
            bookDetail.editInfo = cursor.getString(2);
            bookDetail.editImg = cursor.getString(3);
            bookDetailHashMap.put(bookDetail.id, bookDetail);
        }

//        ll = (LinearLayout) findViewById(R.id.linear_layout);
        backBtn = (ImageButton) findViewById(R.id.iv_title_back);
        title = (TextView) findViewById(R.id.titlebar_title);
        list = (ListView) findViewById(R.id.list);

        listFooter = inflater.inflate(R.layout.comment_footer, null);
        list.addFooterView(listFooter);

//        ll.setVisibility(View.GONE);
        title.setText("订单详情");

//        adapter = new ShopCartAdapter(mContext, bookDetails, 1);
//        list.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            // 当comment不为空且comment.size()不为0且没有完全加载
                            if (scorllable) {
                                page++;
                                listFooter.setVisibility(View.GONE);
                                handler.sendEmptyMessage(0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

        adapter = new ShopCartAdapter(mContext, bookDetails, 2);
        list.setAdapter(adapter);

        handler.sendEmptyMessage(0);
    }

    private void getBookDetails() {
//        for (int i = 0; i < orderResults.size() && orderResults.get(i).product.equals("图书商城"); i++) {
//            OrderResult orderResult = orderResults.get(i);
//            BookDetail bookDetail = new BookDetail();
//            bookDetail = bookDetailHashMap.get(orderResult.bookIds[0]);
//            bookDetail.createTime = orderResult.getCreateTime();
//            if (orderResult.flg.equals("0"))
//                bookDetail.totalPrice = "待支付";
//            else { if (orderResult.sendflg.equals("0"))
//                    bookDetail.totalPrice = "未支付";
//                else
//                    bookDetail.totalPrice = "已支付";
//            }
//            bookDetails.add(bookDetail);
//        }
// handler.sendEmptyMessage(1);
        bookDetails.clear();
        for (int i = 0; i < orderResults.size(); i++) {
            OrderResult orderResult = orderResults.get(i);
            BookDetail bookDetail = new BookDetail();
            if (orderResult.product.equals("图书商城")) {
                bookDetail = bookDetailHashMap.get(orderResult.bookIds[0]);
                if (bookDetail == null)
                    bookDetail = new BookDetail();
                bookDetail.createTime = orderResult.CreateTime;
                bookDetail.bookAuthor = "图书商城";
                if (orderResult.flg.equals("0"))
                    bookDetail.totalPrice = "待支付";
                else {
                    if (orderResult.sendflg.equals("0"))
                        bookDetail.totalPrice = "未发货";
                    else
                        bookDetail.totalPrice = "已发货";
                }
            } else {
                bookDetail.editInfo = orderResult.product;
                bookDetail.createTime = orderResult.CreateTime;
                if (orderResult.product.equals("爱语币"))
                    bookDetail.bookAuthor = "爱语币";
                if (orderResult.flg.equals("0"))
                    bookDetail.totalPrice = "待支付";
                else {
                    if (orderResult.sendflg.equals("0"))
                        bookDetail.totalPrice = "未发货";
                    else
                        bookDetail.totalPrice = "已发货";

                }
            }
            bookDetails.add(bookDetail);

        }
        handler.sendEmptyMessage(1);
    }

//    private Thread thread = new Thread(){
//        @Override
//        public void run() {
//            super.run();
//            ExeProtocol.exe(
//                    new BookDetailRequest(diffBookIds.get(index)),
//                    new ProtocolResponse() {
//
//                        @Override
//                        public void finish(BaseHttpResponse bhr) {
//                            BookDetailResponse response = (BookDetailResponse) bhr;
//                            if (response.result.equals("1")) {
//                                bookDetailHashMap.put(diffBookIds.get(index),response.bookDetail);
//                                index++;
//                                if(index<diffBookIds.size())
//                                { thread = new Thread();
//                                thread.start();}else{getBookDetails();}
//                            }
//                        }
//
//                        @Override
//                        public void error() {
//
//                        }
//                    });
//        }
//    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ExeProtocol.exe(
                            new OrderDetailRequest(AccountManager.getInstance().userId+"", String.valueOf(page), String.valueOf(count)),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    OrderDetailResponse response = (OrderDetailResponse) bhr;
                                    orderResults.clear();
                                    orderResults.addAll(response.orderResults);

                                    if (response.orderResults.size() < count) {
                                        scorllable = false;
                                    } else {
                                        handler.sendEmptyMessage(2);
                                    }

                                    for (int i = 0; i < orderResults.size(); i++) {
                                        orderResults.get(i).bookIds = orderResults.get(i).descs.split(",");
                                    }
                                    getBookDetails();
                                }

                                @Override
                                public void error() {

                                }
                            });
                    break;
                case 1:
//                    adapter = new ShopCartAdapter(mContext, bookDetails, 2);
//                    list.setAdapter(adapter);
                    adapter.addList(bookDetails);
                    break;
                case 2:
                    listFooter.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
