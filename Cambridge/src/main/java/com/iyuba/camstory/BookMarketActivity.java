package com.iyuba.camstory;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iyuba.camstory.lycam.util.NetWorkState;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.BookListAdapter;
import com.iyuba.camstory.protocol.BookMarketRequest;
import com.iyuba.camstory.protocol.BookMarketResponse;
import com.iyuba.camstory.protocol.MarketBook;
import com.iyuba.camstory.protocol.TestArrayAdapter;
import com.iyuba.camstory.protocol.bookstoreui.BookDetailActivity2;
import com.iyuba.camstory.protocol.dao.DBHelper;
import com.iyuba.configation.ConfigManagerHead;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BookMarketActivity extends SwipeBackActivity {


    private Context mContext;
    private BookListAdapter bookListAdapter = null;
    private int pageSize;
    private int pageCount;
    private List<MarketBook> books = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private TestArrayAdapter adapter;
    private int type = 0;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    @BindView(R.id.titlebar_title)
    TextView title;
    @BindView(R.id.book_type)
    Spinner bookType;
    @BindView(R.id.iv_title_back)
    ImageButton backButton;
    @BindView(R.id.book_list)
    PullToRefreshListView bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_market);

        mContext = this;
        ButterKnife.bind(this);
        Log.e("TagBookMarket","onCreate--bind()");
        this.mDBHelper = DBHelper.getInstance(mContext);
        Log.e("TagBookMarket","Dbhelper");
        this.mDB = mDBHelper.getWritableDatabase();
        Log.e("TagBookMarket","db");
        pageSize = 10;
        pageCount = 1;

        title.setText("图书网城");
        types.add("全部");
        types.add("英语四级");
        types.add("英语六级");
        types.add("Voa系列");
        types.add("考研英语(一)");
        types.add("考研英语(二)");
        types.add("日语N1");
        types.add("日语N2");
        types.add("日语N3");
        types.add("托福");
        types.add("雅思");
        types.add("中职英语");
        types.add("新概念");
        types.add("走遍美国");
        types.add("剑桥小说");
        types.add("学位英语");

        // 适配器
        adapter = new TestArrayAdapter(mContext,
                types);

        // 设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 加载适配器
        bookType.setAdapter(adapter);


        bookType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                switch (position) {
                    case 0:
                        type = 0;
                        break;
                    case 1:
                        type = 2;
                        break;
                    case 2:
                        type = 4;
                        break;
                    case 3:
                        type = 3;
                        break;
                    case 4:
                        type = 8;
                        break;
                    case 5:
                        type = 52;
                        break;
                    case 6:
                        type = 1;
                        break;
                    case 7:
                        type = 5;
                        break;
                    case 8:
                        type = 6;
                        break;
                    case 9:
                        type = 7;
                        break;
                    case 10:
                        type = 61;
                        break;
                    case 11:
                        type = 91;
                        break;
                    case 12:
                        type = 21;
                        break;
                    case 13:
                        type = 22;
                        break;
                    case 14:
                        type = 23;
                        break;
                    case 15:
                        type = 28;
                        break;
                }
                ConfigManagerHead.Instance().putInt("bookType", type);
                handler.sendEmptyMessage(0);

            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        handler.sendEmptyMessage(0);

    }

    private void setView() {

        bookList.getLoadingLayoutProxy(true, false).setPullLabel(
                mContext.getString(R.string.pulldown));
        bookList.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                mContext.getString(R.string.pulldown_refreshing));
        bookList.getLoadingLayoutProxy(true, false).setReleaseLabel(
                mContext.getString(R.string.pulldown_release));
        bookList.getLoadingLayoutProxy(false, true).setPullLabel(
                mContext.getString(R.string.pullup));
        bookList.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                mContext.getString(R.string.pullup_loading));
        bookList.getLoadingLayoutProxy(false, true).setReleaseLabel(
                mContext.getString(R.string.pullup_release));
        bookList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                if (NetWorkState.isConnectingToInternet()) {
                    handler.sendEmptyMessage(0);
                } else {

                }
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                if (NetWorkState.isConnectingToInternet()) {
                    handler.sendEmptyMessage(2);
                } else {
                }
            }
        });
        bookList.getRefreshableView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (AccountManager.getInstance().checkUserLogin()
                                ||AccountManager.getInstance().islinshi) {
                            Intent intent = new Intent();
                            MarketBook marketBook = (MarketBook) bookListAdapter.getItem(position - 1);
                            intent.putExtra("bookId", marketBook.getBookId());
                            intent.putExtra("bookorg", marketBook.getOriginalPrice());
                            intent.setClass(mContext, BookDetailActivity2.class);
                            startActivity(intent);
                        } else {



//                           LoginDialog.showDialog(mContext, BookMarketActivity.class.getSimpleName());



                        }
                    }
                });


        if (books.size()>0&& TextUtils.isEmpty(books.get(0).getBookId())) {
            for (int i = 0; i < books.size(); i++) {
                ContentValues contentValues = new ContentValues();
                MarketBook marketBook = books.get(i);
                if(marketBook!=null&&marketBook.getBookId()!=null)
                try {
                    contentValues.put("id", marketBook.getBookId());
                    contentValues.put("totalPrice", marketBook.getTotalPrice());
                    contentValues.put("editInfo", marketBook.getEditInfo());
                    contentValues.put("editImg", marketBook.getEditImg());
                    contentValues.put("uid", "0");
                    mDB.replace("shop_cart", null, contentValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    pageCount = 1;
                    ExeProtocol.exe(
                            new BookMarketRequest(String.valueOf(pageSize), String.valueOf(pageCount), String.valueOf(type)),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    BookMarketResponse response = (BookMarketResponse) bhr;
                                    if (response.result.equals("1")) {
                                        books.clear();
                                        books.addAll(response.books);
                                        handler.sendEmptyMessage(1);
                                    }
                                }

                                @Override
                                public void error() {

                                }
                            });
                    break;
                case 1:
                    if (bookListAdapter == null) {
                        bookListAdapter = new BookListAdapter(mContext, books);
                        bookList.getRefreshableView().setAdapter(bookListAdapter);
                    } else
                        bookListAdapter.replaceBooks(books);
                    bookList.onRefreshComplete();
                    setView();
                    break;
                case 2:
                    pageCount++;
                    ExeProtocol.exe(
                            new BookMarketRequest(String.valueOf(pageSize), String.valueOf(pageCount), String.valueOf(type)),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    BookMarketResponse response = (BookMarketResponse) bhr;
                                    if (response.result.equals("1")) {
                                        pageCount++;
                                        books.clear();
                                        books.addAll(response.books);
                                        handler.sendEmptyMessage(3);
                                    }
                                }

                                @Override
                                public void error() {

                                }
                            });
                    break;
                case 3:
                    bookListAdapter.addBooks(books);
                    bookList.onRefreshComplete();
                    setView();
                    break;

            }
        }
    };
}
