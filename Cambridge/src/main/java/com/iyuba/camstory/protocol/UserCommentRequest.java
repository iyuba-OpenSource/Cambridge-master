package com.iyuba.camstory.protocol;

import com.iyuba.camstory.utils.LogUtils;
import com.iyuba.camstory.lycam.util.MD5;
import com.iyuba.configation.Constant;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.BaseJSONRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivotsm on 2017/3/13.
 */

public class UserCommentRequest extends BaseJSONRequest {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public UserCommentRequest(String uid, String topic, String voaId) {
        String sign = MD5.getMD5ofStr(uid + "getWorksByUserId" + df.format(new Date()));
        setAbsoluteURI("http://voa."+Constant.IYBHttpHead+"/voa/getWorksByUserId.jsp?uid=" +
                uid +
                "&topic=" +
                topic +
                "&shuoshuoType=2,4&sign=" +
                sign +
                "&topicId=" +
                voaId);
        LogUtils.e("userComment", "http://voa."+Constant.IYBHttpHead+"/voa/getWorksByUserId.jsp?uid=" +
                uid +
                "&topic=" +
                topic +
                "&shuoshuoType=2,4&sign=" +
                sign +
                "&topicId=" +
                voaId);
    }



    @Override
    public BaseHttpResponse createResponse() {
        return new UserCommentResponse();
    }
}
