/**
 *
 */
package com.iyuba.camstory.listener;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.sqlite.mode.MessageLetterContent;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 发私信
 */
public class RequestSendMessageLetter extends BaseJsonObjectRequest {
    private static final String protocolCode = "60002";
    private String result;// 返回代码
    public String message;// 返回信息
    public MessageLetterContent letter = new MessageLetterContent();
    public JSONArray data;
    public ArrayList<MessageLetterContent> list;
    public int num;
    public String plid;

    public RequestSendMessageLetter(int uid, String username, String context, final RequestCallBack rc) {
        // TODO Auto-generated constructor stub
        super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode
                + "&uid=" + uid
                + "&username=" + TextAttr.encode(username)
                + "&context=" + TextAttr.encode(TextAttr.encode(context))
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
        setResListener(new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObjectRootRoot) {
                // TODO 自动生成的方法存根
                list = new ArrayList<MessageLetterContent>();
                try {
                    result = jsonObjectRootRoot.getString("result");
                    message = jsonObjectRootRoot.getString("message");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                rc.requestResult(RequestSendMessageLetter.this);
            }
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        // TODO 自动生成的方法存根
        if ("611".equals(result)) {
            Log.d("tag", "私信发送成功");
            return true;
        }
        return false;
    }

}

