package com.iyuba.camstory.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;
import com.iyuba.camstory.WebActivity;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.lycam.manager.ConfigManager;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.utils.ToastUtil;
import com.iyuba.common.WebConstant;
import com.iyuba.voa.activity.setting.Constant;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//import com.iyuba.camstory.utils.LogUtils;

//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by howard9891 on 2016/10/28.
 */

public class PayOrderActivity extends Activity {
    private TextView payorder_username;
    private TextView payorder_rmb_amount;
    private NoScrollListView methodList;
    private Toolbar mToolbar;
    private PayMethodAdapter methodAdapter;
    private Button payorder_submit_btn;
    private boolean confirmMutex = true;
    private static final String TAG = PayOrderActivity.class.getSimpleName();
    private Context mContext;
    private static final String Seller = "iyuba@sina.com";
    private String price;
    private String subject;
    private String body;
    private String amount;
    private int type;
    private String out_trade_no;
    private IWXAPI mWXAPI;
    private String mWeiXinKey = Constant.getWxappId();
    private int selectPosition = 0;
    private Button button;
    private String productId;
    private TextView mTvOrderInfo;
    //会员协议
    private TextView vipAgreement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent intent = getIntent();

        setContentView(R.layout.activity_buyvip);

        price = intent.getStringExtra("price");
        type = intent.getIntExtra("type", -1);
        Log.e("typetype", type + "");
        subject = intent.getStringExtra("subject");
        body = intent.getStringExtra("body");
        out_trade_no = intent.getStringExtra("out_trade_no");
        productId = getProductId(type);//0,8,10,1
        amount = getAmount(intent.getIntExtra("month", 0));
        findView();
//        mWeiXinKey = mContext.getString(R.string.weixin_key);
//        mWeiXinKey = Constant.mWeiXinKey;
        mWXAPI = WXAPIFactory.createWXAPI(this, mWeiXinKey, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        payorder_username.setText(AccountManager.getInstance().userName);
    }

    private String getAmount(int type) {
        String amount;
        if (type == 0) {
            amount = "0";
        } else {
            amount = type + "";
        }
        return amount;
    }

    private String getProductId(int type) {
        String productId = null;
        if (type == 8) {
            productId = "8";
        } else if (type == 0) {
            productId = "0";
        } else if (type == 10) {
            productId = "10";
        }else if (type==1){
            productId = "1";
        }
        return productId;
    }

    private void findView() {
        button = findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTvOrderInfo =findViewById(R.id.tv_order_info_text);
        mTvOrderInfo.setText(body);
        payorder_username = findViewById(R.id.payorder_username_tv);
        payorder_username.setText(AccountManager.getInstance().userName);
        payorder_rmb_amount = findViewById(R.id.payorder_rmb_amount_tv);
        payorder_rmb_amount.setText(price + "元");
        methodList = findViewById(R.id.payorder_methods_lv);
        payorder_submit_btn = findViewById(R.id.payorder_submit_btn);
        methodList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                methodAdapter.changeSelectPosition(position);
                methodAdapter.notifyDataSetChanged();
            }
        });
        methodAdapter = new PayMethodAdapter(this);
        methodList.setAdapter(methodAdapter);
        payorder_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountManager.getInstance().islinshi) {
                    showNormalDialog();
                } else {
                    if (confirmMutex) {
                        confirmMutex = false;
                        String newSubject;
                        String newbody;
                        try {
                            newSubject = URLEncoder.encode(subject, "UTF-8");
                            newbody = URLEncoder.encode(body, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            newSubject = "iyubi";
                            newbody = "iyubi";
                        }
                        switch (selectPosition) {
                            case PayMethodAdapter.PayMethod.ALIPAY:
                                payByAlipay(newbody, newSubject);
                                break;
                            case PayMethodAdapter.PayMethod.WEIXIN:
                            Log.e("PayOrderActivity", "weixin");
                            if (mWXAPI.isWXAppInstalled()) {
                                payByWeiXin();
                            } else {
//                                /*new AlertDialog.Builder(PayOrderActivity.this)
//                                        .setTitle("提示")
//                                        .setMessage("微信未安装无法使用微信支付!")
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                confirmMutex = true;
//                                                dialog.cancel();
//                                            }
//                                        })
//                                        .show();*/
                                ToastUtil.showToast(mContext, "您还未安装微信客户端");
                            }
                                break;
                      /*  case PayMethodAdapter.PayMethod.BANKCARD:
                            payByWeb();
                            break;*/
                            default:
                                payByAlipay(newbody, newSubject);
                                break;
                        }
                    }
                }
            }
        });
        //设置会员协议
        vipAgreement = findViewById(R.id.vip_agreement);
        vipAgreement.setVisibility(View.VISIBLE);
        vipAgreement.setText(setVipAgreement());
        vipAgreement.setMovementMethod(new LinkMovementMethod());
    }

    //设置会员服务协议的样式
    private SpannableStringBuilder setVipAgreement(){
        String vipStr = "《会员服务协议》";
        String showMsg = "点击支付即代表您已充分阅读并同意"+vipStr;

        SpannableStringBuilder spanStr = new SpannableStringBuilder();
        spanStr.append(showMsg);
        //会员服务协议
        int termIndex = showMsg.indexOf(vipStr);
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebActivity.start(PayOrderActivity.this, WebConstant.HTTP_SPEECH_ALL+"/api/vipServiceProtocol.jsp?company=北京爱语吧&type=app", "会员服务协议");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        },termIndex,termIndex+vipStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private void showNormalDialog() {
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

    private void payByAlipay(String body, String subject) {
        confirmMutex = true;
        RequestCallBack rc = new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                OrderGenerateRequest request = (OrderGenerateRequest) result;
                if (request.isRequestSuccessful()) {
                    // 完整的符合支付宝参数规范的订单信息
                    final String payInfo = request.orderInfo + "&sign=\"" + request.orderSign
                            + "\"&" + "sign_type=\"RSA\"";

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(PayOrderActivity.this);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(payInfo, true);

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = result;
                            alipayHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    validateOrderFail();
                }
            }
        };
        OrderGenerateRequest orderRequest = new OrderGenerateRequest(productId, Seller, out_trade_no,
                subject, price, body, "", Constant.getAppid(),
                String.valueOf(AccountManager.getInstance().userId), amount,
                mOrderErrorListener, rc);
        CrashApplication.getInstance().getQueue().add(orderRequest);
    }

    private void payByWeiXin() {
        // 通过刷新用户VIP状态判断用户是不是vip
        AccountManager.getInstance().VipRefresh(mContext, null);
        confirmMutex = true;
        RequestCallBack requestCallBack = new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                OrderGenerateWeiXinRequest first = (OrderGenerateWeiXinRequest) result;
                if (first.isRequestSuccessful()) {
                    Log.e(TAG, "OrderGenerateWeiXinRequest success!");
                    PayReq req = new PayReq();
                    req.appId = mWeiXinKey;
                    req.partnerId = first.partnerId;
                    req.prepayId = first.prepayId;
                    req.nonceStr = first.nonceStr;
                    req.timeStamp = first.timeStamp;
                    req.packageValue = "Sign=WXPay";
                    req.sign = buildWeixinSign(req, first.mchkey);
                    mWXAPI.sendReq(req);
                } else {
                    validateOrderFail();
                }
            }
        };
        String uid = String.valueOf(AccountManager.getInstance().userId);
        OrderGenerateWeiXinRequest request = new OrderGenerateWeiXinRequest(productId,mWeiXinKey, Constant.getAppid(), uid, price, amount,body ,mOrderErrorListener, requestCallBack);
        CrashApplication.getInstance().getQueue().add(request);
    }

    private String buildWeixinSign(PayReq payReq, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildWeixinStringA(payReq));
        sb.append("&key=").append(key);
        Log.i(TAG, sb.toString());
        return MD5.getMD5ofStr(sb.toString()).toUpperCase();
    }

    private String buildWeixinStringA(PayReq payReq) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(payReq.appId);
        sb.append("&noncestr=").append(payReq.nonceStr);
        sb.append("&package=").append(payReq.packageValue);
        sb.append("&partnerid=").append(payReq.partnerId);
        sb.append("&prepayid=").append(payReq.prepayId);
        sb.append("&timestamp=").append(payReq.timeStamp);
        return sb.toString();
    }

    private void validateOrderFail() {
        ToastUtil.showToast(mContext, "服务器正忙,请稍后再试!");
        PayOrderActivity.this.finish();
    }

    private Response.ErrorListener mOrderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(PayOrderActivity.this)
                    .setTitle("订单提交出现问题!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmMutex = true;
                            dialog.dismiss();
                            PayOrderActivity.this.finish();
                        }
                    })
                    .show();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler alipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    confirmMutex = true;
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功!", Toast.LENGTH_SHORT).show();
                        LogUtils.e("subject", subject);
                        if (subject.equals("全站vip")) {
                            ConfigManager.Instance().putInt("isvip", 1);
                            AccountManager.getInstance().vipStatus = "1";
                            Log.e("temp_", ConfigManager.Instance().loadInt("isvip") + "");
                            Log.e("temp_temp", AccountManager.getInstance().getVipStatus());

                        } else if (subject.equals("黄金vip")) {
                            ConfigManager.Instance().putInt("isvip", 8);
                            AccountManager.getInstance().vipStatus = "8";
                            Log.e("temp_", ConfigManager.Instance().loadInt("isvip") + "");
                            Log.e("temp_temp", AccountManager.getInstance().getVipStatus());
                        } else if (subject.equals("永久vip")) {
                            ConfigManager.Instance().putInt("isvip", 10);
                            AccountManager.getInstance().vipStatus = "10";
                        }

                        finish();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(mContext, "您已取消支付", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
