package com.iyuba.camstory.listener;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class UserInfoRefreshRequest extends BaseJsonObjectRequest {
    private static final String TAG = UserInfoRefreshRequest.class.getSimpleName();

    public String result;
    public long expireTime;
    public int uid;
    public String message;
    public int amount;
    public String name;
    public boolean vipStatus;

    private static final String url = "http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10009";

    public UserInfoRefreshRequest(String username, String appid, final RequestCallBack rc) {
        super(url + "&username=" + encodeName(username) + "&appid=" + appid);
        setResListener(new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getString("result");
                    message = response.getString("message");
                    int iVipStatus = Integer.parseInt(response.getString("vipStatus"));

                    vipStatus = iVipStatus >= 1;
                    expireTime = Long.parseLong(response.getString("expireTime"));
                    amount = Integer.parseInt(response.getString("Amount"));
                    Log.e(TAG, "info : " + vipStatus + expireTime + amount);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                rc.requestResult(UserInfoRefreshRequest.this);
            }
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        return result.equals("101");
    }

    private static String encodeName(String username) {
        String result;
        try {
            result = URLEncoder.encode(username, "UTF-8");
        } catch (Exception e) {
            result = username;
        }
        return result;
    }

}
