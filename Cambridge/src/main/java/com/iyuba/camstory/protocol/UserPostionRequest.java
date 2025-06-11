package com.iyuba.camstory.protocol;

import android.util.Log;

import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.configation.Constant;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.BaseJSONRequest;

/**
 * Created by ivotsm on 2017/2/28.
 */

public class UserPostionRequest extends BaseJSONRequest {

    public UserPostionRequest(String uid) {
        setAbsoluteURI("http://m."+Constant.IYBHttpHead+"/mall/getAddressInfo.jsp?uid=" +
                uid +
                "&sign=" +
                MD5.getMD5ofStr("get" + uid + "addressInfo"));
        Log.e("UserPositon","http://m."+Constant.IYBHttpHead+"/mall/getAddressInfo.jsp?uid=" +
                uid +
                "&sign=" +
                MD5.getMD5ofStr("get" + uid + "addressInfo"));
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new UserPositionResponse();
    }
}
