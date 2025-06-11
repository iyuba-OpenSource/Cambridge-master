package com.iyuba.camstory.protocol;

import android.util.Log;

import com.iyuba.configation.Constant;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.BaseJSONRequest;

public class AgreeAgainstRequest extends BaseJSONRequest {


    public AgreeAgainstRequest(String protocol, String commnetId, int type) {
        super();
        if (type != 2) {
            setAbsoluteURI("http://daxue."+Constant.IYBHttpHead+"/appApi//UnicomApi?"
                    + "protocol=" + protocol
                    + "&id=" + commnetId);
            Log.e("zanRequest", "http://daxue."+Constant.IYBHttpHead+"/appApi//UnicomApi?"
                    + "protocol=" + protocol
                    + "&id=" + commnetId);
        } else {
            setAbsoluteURI("http://voa."+Constant.IYBHttpHead+"/voa/UnicomApi?id=" + commnetId + "&protocol=" + protocol);
            Log.e("zanRequest", "http://voa."+Constant.IYBHttpHead+"/voa/UnicomApi?id=" + commnetId + "&protocol=" + protocol);
        }
    }


    @Override
    public BaseHttpResponse createResponse() {
        return new AgreeAgainstResponse();
    }

}
