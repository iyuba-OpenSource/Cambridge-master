package com.iyuba.camstory.protocol.bookstoreui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.BookDetail;
import com.iyuba.camstory.protocol.ShopCartAdapter;
import com.iyuba.camstory.protocol.dao.DBHelper;
import com.iyuba.camstory.protocol.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ShopCartActivity extends SwipeBackActivity implements ShopCartAdapter.onCheckedChanged, View.OnClickListener {
    private Context mContext;
    private TextView tv_cart_Allprice, tv_cart_buy_Ordel, title;
    private LinearLayout ll_cart;
    private MyListView listView_cart;
    private CheckBox cb_cart_all;
    private ShopCartAdapter adapter;
    private String str_del = "结 算";
    private boolean[] is_choice;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private String uid;
    private List<BookDetail> bookDetails = new ArrayList<>();
    private ArrayList<BookDetail> bookDetailsNow = new ArrayList<>();
    private float allPrice = 0.0f;
    private ImageButton backBtn, overflowBtn;
    private int a = 0;//adapter里面的textwatcher调用三次，计数
    //private boolean isexit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_cart);

        mContext = this;
        uid = AccountManager.getInstance().userId+"";

        this.mDBHelper = DBHelper.getInstance(mContext);
        this.mDB = mDBHelper.getWritableDatabase();
        Cursor cursor = mDB.query("shop_cart", new String[]{"editImg", "editInfo", "totalPrice",
                "number", "id"}, "uid=?",
                new String[]{AccountManager.getInstance().userId+""}, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BookDetail bookDetail = new BookDetail();
            bookDetail.editImg = cursor.getString(0);
            bookDetail.editInfo = cursor.getString(1);
            bookDetail.totalPrice = cursor.getString(2);
            bookDetail.num = cursor.getInt(3);
            bookDetail.id = cursor.getString(4);
            bookDetails.add(bookDetail);
        }

        is_choice = new boolean[bookDetails.size()];

//        tv_goShop = (TextView) findViewById(R.id.tv_goShop);
        tv_cart_Allprice = (TextView) findViewById(R.id.tv_cart_Allprice);
        tv_cart_buy_Ordel = (TextView) findViewById(R.id.tv_cart_buy_or_del);
        tv_cart_buy_Ordel.setText(str_del);
        backBtn = (ImageButton) findViewById(R.id.iv_title_back);
        title = (TextView) findViewById(R.id.titlebar_title);
        overflowBtn = (ImageButton) findViewById(R.id.titlebar_overflow_button);
        overflowBtn.setVisibility(View.GONE);
        title.setText("购物车");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cb_cart_all = (CheckBox) findViewById(R.id.cb_cart_all);

        cb_cart_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int isChoice_all = 0;
                if (arg1) {

                    for (int i = 0; i < bookDetails.size(); i++) {

                        ((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(true);
                    }
                } else {

                    for (int i = 0; i < bookDetails.size(); i++) {

                        if (((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {

                            isChoice_all += 1;
                        }
                    }

                    if (isChoice_all == bookDetails.size()) {

                        for (int i = 0; i < bookDetails.size(); i++) {

                            ((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(false);
                        }
                        allPrice = 0;
                    }
                }
            }
        });

        ll_cart = (LinearLayout) findViewById(R.id.ll_cart);
        listView_cart = (MyListView) findViewById(R.id.listView_cart);

        if (bookDetails != null && bookDetails.size() != 0) {
            adapter = new ShopCartAdapter(mContext, (ArrayList<BookDetail>) bookDetails, 0);
            adapter.setOnCheckedChanged(this);
            listView_cart.setAdapter(adapter);
            ll_cart.setVisibility(View.GONE);
        } else {
            ll_cart.setVisibility(View.VISIBLE);
        }

        listView_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Intent intent = new Intent(getActivity(), BabyActivity.class);
//                startActivity(intent);
            }
        });


        tv_cart_buy_Ordel.setOnClickListener(this);
//        tv_goShop.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!uid.equals(AccountManager.getInstance().userId+"")){
            finish();
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
        normalDialog.setMessage("临时用户无法购买！");
        normalDialog.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Intent intent = new Intent();
                        intent.setClass(mContext, LoginActivity.class);
                        startActivity(intent);


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_goShop:
//                btnCallListener.transferMsg();
//                break;
            case R.id.tv_cart_buy_or_del:
                if(AccountManager.getInstance().islinshi){

                    showNormalDialog();
                }else {
//                boolean[] is_choice_copy = is_choice;
//                if (tv_cart_buy_Ordel.getText().toString().equals("ɾ��")) {
//
//                    if (bookDetails.size() != 0) {
//                        for (int i = is_choice_copy.length - 1; i >= 0; i--) {
//                            if (is_choice_copy[i]) {
//                                ((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(false);
//                                bookDetails.remove(i);
//                                is_choice_copy = deleteByIndex(is_choice, i);
//                            }
//                        }
//                    }
//
//
//                    if (bookDetails.size() == 0) {
//                        ll_cart.setVisibility(View.VISIBLE);
//                    }
//
//                    adapter.notifyDataSetChanged();
//                    is_choice = new boolean[bookDetails.size()];
//                } else {
//
//                    Toast.makeText(mContext, "  ", Toast.LENGTH_SHORT).show();
//                }
                    bookDetailsNow.clear();
                    for (int i = 0; i < bookDetails.size(); i++) {
                        BookDetail bookDetail = new BookDetail();
                        if (((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {
                            bookDetail = bookDetails.get(i);
                            bookDetail.num = Integer.parseInt(((EditText) (listView_cart.getChildAt(i)).findViewById(R.id.tv_num)).getText().toString());
                            Log.e("BookName---Num",bookDetail.bookAuthor+"---"+bookDetail.num);
                            bookDetailsNow.add(bookDetail);
                        }
                    }

                    if (bookDetailsNow.size() == 0) {
                        Toast.makeText(mContext, "您没有选择任何商品", Toast.LENGTH_SHORT);
                    } else {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("books", bookDetailsNow);
                        intent.putExtra("books", bundle);
                        intent.setClass(mContext, OrderConfirmActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }

    }


    public static boolean[] deleteByIndex(boolean[] array, int index) {
        boolean[] newArray = new boolean[array.length - 1];
        for (int i = 0; i < newArray.length; i++) {
            if (i < index) {
                newArray[i] = array[i];
            } else {
                newArray[i] = array[i + 1];
            }
        }
        return newArray;
    }

    @Override
    public void getChoiceData(int position, boolean isChoice, int bookNum, boolean flag) {
//        if (a == 3)
//            a = 0;
//        if(allPrice<0){
//            allPrice = 0;
//        }
//        if (flag) {
//            if (isChoice) {
//                if (bookDetails.size() != 0) {
//                    EditText et = (EditText) (listView_cart.getChildAt(position).findViewById(R.id.tv_num));
//
//                    allPrice += Integer.valueOf(et.getText().toString()) *
//                            Float.valueOf(bookDetails.get(position).totalPrice);
//                    Log.e("ET-jia--shopCart",""+allPrice);
//                }
//            } else {
//                if (bookDetails.size() != 0) {
//                    EditText et = (EditText) (listView_cart.getChildAt(position).findViewById(R.id.tv_num));
//                    allPrice -= Integer.valueOf(et.getText().toString()) *
//                            Float.valueOf(bookDetails.get(position).totalPrice);
//                    Log.e("ET-jian--shopCart",""+allPrice);
//                }
//            }
//        } else {
//            if (isChoice && a == 0) {
//                if (bookDetails.size() != 0) {
//                    allPrice += bookNum * Float.valueOf(bookDetails.get(position).totalPrice);
//                    Log.e("ET---shopCart",""+allPrice);
//                }
//            }
//            a++;
//        }

        allPrice = 0;
        int num_choice = 0;
        for (int i = 0; i < bookDetails.size(); i++) {

            if (null != listView_cart.getChildAt(i) &&
                    ((CheckBox) (listView_cart.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {

                    num_choice += 1;
                    is_choice[i] = true;
                    EditText et = listView_cart.getChildAt(i).findViewById(R.id.tv_num);
                    allPrice += Integer.valueOf(et.getText().toString()) *
                            Float.valueOf(bookDetails.get(i).totalPrice);


            }
        }

        if (num_choice == bookDetails.size()) {

            cb_cart_all.setChecked(true);
        } else {

            cb_cart_all.setChecked(false);
        }

        tv_cart_Allprice.setText("合计：￥" + String.valueOf(allPrice) + "");

    }

}
