package com.iyuba.camstory;

/**
 * 手机注册界面
 *
 * @author czf
 * @version 1.0
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.RequestCallBack;
import com.iyuba.camstory.listener.RequestSubmitMessageCode;
import com.iyuba.camstory.utils.TelNumMatch;
import com.iyuba.camstory.widget.CustomDialog;
import com.iyuba.camstory.widget.CustomToast;
import com.iyuba.camstory.widget.ProtocolText;
import com.iyuba.camstory.widget.WaittingDialog;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@SuppressLint("HandlerLeak")
public class RegistByPhoneActivity extends Activity {
    private static final String TAG = RegistByPhoneActivity.class.getSimpleName();
    private Context mContext;

    private Button backBtn;
    private EditText phoneNum, messageCode;
    private Button getCodeButton, nextBtn;
    private TextView toEmailButton;
    //private TextView toWeb;

    private String phoneNumString = "", messageCodeString = "";
    private EventHandler eventHandler;
    private Timer timer;
    private TimerTask timerTask;
    //private SmsContent smsContent;
    private CustomDialog waittingDialog;
    private CheckBox protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.regist_layout_phone);
        //toWeb = (TextView) findViewById(R.id.regist_web);
        messageCode = findViewById(R.id.regist_phone_code);
        phoneNum = findViewById(R.id.regist_phone_numb);
        getCodeButton = findViewById(R.id.regist_getcode);
        nextBtn = findViewById(R.id.register_phone_next);
        nextBtn.setEnabled(false);
        //toWeb.setOnClickListener(this);
        waittingDialog = new WaittingDialog().wettingDialog(mContext);
//		SMSSDK.initSDK(this,"15812f538a4c4", "52496a74798c15b02f042c475802f03c");
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.v("eventHandler", "开始执行eventHandler1");
                Message msg = new Message();
                Log.v("eventHandler", "开始执行eventHandler2");
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                Log.v("eventHandler", "开始执行eventHandler3");
                handlerSms.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

        protocol = findViewById(R.id.protocol);
        ProtocolText.setText(protocol, mContext);

        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toEmailButton = findViewById(R.id.regist_email);
        toEmailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        getCodeButton.setOnClickListener(v -> {
            if (verifyPhoneNumber()) {
                handler_waitting.sendEmptyMessage(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                phoneNumString = phoneNum.getText().toString();
                RequestSubmitMessageCode request = new RequestSubmitMessageCode(
                        phoneNumString, new RequestCallBack() {
                    @Override
                    public void requestResult(Request result) {
                        RequestSubmitMessageCode rs = (RequestSubmitMessageCode) result;
                        if (rs.isRequestSuccessful()) {
                            handler_verify.sendEmptyMessage(1);
                            //RegistByPhoneActivity.this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"),true, smsContent);
                            Log.v("rs.isRequestSuccessfu()", "isRequestSuccessful()3");
                        } else if (rs.isPhonenumAlreadyRegistered()) {
                            CustomToast.showToast(mContext, "手机号已注册，请换一个号码试试~", 2000);
                        }
                        Log.v("rs.isRequestSuccessfu()", "isRequestSuccessful()4");
                        handler_waitting.sendEmptyMessage(2);
                    }
                });
                Log.v("rs.isRequestSuccessfu()", "isRequestSuccessful()5");
                CrashApplication.getInstance().getQueue().add(request);
                Log.v("rs.isRequestSuccessfu()", "isRequestSuccessful()6");
            } else {
                CustomToast.showToast(mContext, "电话不能为空", 1000);
            }
        });
        nextBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (verification()) {
                    if (protocol.isChecked()) {
                        SMSSDK.submitVerificationCode("86", phoneNumString, messageCode.getText().toString());
                    } else {
                        CustomToast.showToast(mContext, "请阅读并同意使用条款和隐私政策", 1000);
                    }
                } else {
                    CustomToast.showToast(mContext, "验证码不能为空", 1000);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        if (timer != null) {
            timer.cancel();
        }
    }

    public boolean verification() {
        if (!verifyPhoneNumber()) {
            return false;
        }
        messageCodeString = messageCode.getText().toString();
        if (messageCodeString.length() == 0) {
            messageCode.setError("验证码不能为空");
            return false;
        }

        return true;
    }

    /**
     * 验证
     */
    public boolean verifyPhoneNumber() {
        phoneNumString = phoneNum.getText().toString();
        if (phoneNumString.length() == 0) {
            phoneNum.setError("手机号不能为空");
            return false;
        }
        if (!checkPhoneNum(phoneNumString)) {
            phoneNum.setError("手机号输入错误");
            return false;
        }

        return true;
    }

    public boolean checkPhoneNum(String phoneNumber) {
        return TelNumMatch.isPhonenumberLegal(phoneNumber);
    }

    Handler handlerSms = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Log.v("handlerSms event", "event=" + event);
            Log.v("handlerSms result", "result=" + result);
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    CustomToast.showToast(mContext, "验证成功", 1000);
                    Intent intent = new Intent();
                    intent.setClass(mContext, RegistSubmitActivity.class);
                    intent.putExtra("phoneNumb", phoneNumString);
                    startActivity(intent);
                    finish();
                } else {
                    //依然走短信验证
                    CustomToast.showToast(mContext, "验证码已经发送，请等待接收", 1000);
                    nextBtn.setEnabled(true);
                }

            } else {
                CustomToast.showToast(mContext, "验证失败，请输入正确的验证码！", 1000);
                getCodeButton.setText("获取验证码");
                getCodeButton.setEnabled(true);
            }
        }
    };

    Handler handler_time = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what > 0) {
                getCodeButton.setText("重新发送(" + msg.what + "s)");
            } else {
                timer.cancel();
                getCodeButton.setEnabled(true);
                getCodeButton.setText("获取验证码");
            }
        }
    };

    Handler handler_waitting = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    waittingDialog.show();
                    break;
                case 2:
                    waittingDialog.dismiss();
                    break;
            }
        }
    };

    Handler handler_verify = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String vcode = (String) msg.obj;
                    messageCode.setText(vcode);
                    break;
                case 1:
                    Log.v("handler_verify", "handler_verify1");
                    SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
                    Log.v("handler_verify", "handler_verify2");
                    timer = new Timer();
                    timerTask = new TimerTask() {
                        int i = 60;

                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = i--;
                            handler_time.sendMessage(msg);
                        }
                    };
                    Log.v("handler_verify", "handler_verify3");
                    timer.schedule(timerTask, 1000, 1000);
                    getCodeButton.setTextColor(Color.WHITE);
                    Log.v("handler_verify", "handler_verify4");
                    getCodeButton.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

}
