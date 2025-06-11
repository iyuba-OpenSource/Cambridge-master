package com.iyuba.camstory.protocol.bookstoreui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.camstory.R;
import com.iyuba.camstory.manager.AccountManager;
import com.iyuba.camstory.protocol.ModifyAddressRequest;
import com.iyuba.camstory.protocol.ModifyAddressResponse;
import com.iyuba.camstory.protocol.UserPositionResponse;
import com.iyuba.camstory.protocol.UserPostionRequest;
import com.iyuba.camstory.protocol.dao.UserAddrInfo;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ModifyAddressActivity extends SwipeBackActivity {
    private Context mContext;
    private UserAddrInfo userAddrInfo = new UserAddrInfo();
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.telephone)
    EditText phone;
    @BindView(R.id.qq)
    EditText qq;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.iv_title_back)
    ImageButton backBtn;
    @BindView(R.id.titlebar_title)
    TextView title;
    @BindView(R.id.titlebar_overflow_button)
    ImageButton overflowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_addr_modify);

        mContext = this;
        ButterKnife.bind(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText("我的地址");
        overflowBtn.setVisibility(View.GONE);

        handler.sendEmptyMessage(1);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || phone.getText().toString().equals("")
                        || qq.getText().toString().equals("") ||
                        email.getText().toString().equals("") ||
                        address.getText().toString().equals("")) {
                    Toast.makeText(mContext, "请完善个人信息", Toast.LENGTH_SHORT).show();
                } else {
                    userAddrInfo.name = name.getText().toString();
                    userAddrInfo.phone = phone.getText().toString();
                    userAddrInfo.qq = phone.getText().toString();
                    userAddrInfo.email = email.getText().toString();
                    userAddrInfo.address = address.getText().toString();

                    try {
                        ExeProtocol.exe(
                                new ModifyAddressRequest(userAddrInfo, AccountManager.getInstance().userId+""),
                                new ProtocolResponse() {

                                    @Override
                                    public void finish(BaseHttpResponse bhr) {
                                        ModifyAddressResponse response = (ModifyAddressResponse) bhr;
                                        if (response.result.equals("1")) {

                                            Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("userInfo", userAddrInfo);
                                            intent.putExtra("userInfo", bundle);
                                            setResult(0, intent);
                                            handler.sendEmptyMessage(0);
                                        }
                                    }

                                    @Override
                                    public void error() {

                                    }
                                });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext,"修改成功!",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    ExeProtocol.exe(
                            new UserPostionRequest(AccountManager.getInstance().userId+""),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    UserPositionResponse response = (UserPositionResponse) bhr;
                                    userAddrInfo = response.userInfo;
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                }
                            });

                    break;
                case 2:
                    name.setText(userAddrInfo.realName);
                    phone.setText(userAddrInfo.phone);
                    qq.setText(userAddrInfo.qq);
                    email.setText(userAddrInfo.email);
                    address.setText(userAddrInfo.address);
                    break;
            }
        }
    };
}
