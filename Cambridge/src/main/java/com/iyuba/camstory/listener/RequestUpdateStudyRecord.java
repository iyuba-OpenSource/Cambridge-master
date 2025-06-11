package com.iyuba.camstory.listener;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.utils.MacAddressUtil;
import com.iyuba.voa.activity.setting.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class RequestUpdateStudyRecord extends BaseJsonObjectRequest {
    private static final String format = "json";
    private String result = "";
    public String message = "";

    public RequestUpdateStudyRecord(String uid, String BeginTime, String EndTime,
                                    String Lesson, int LessonId, int EndFlg, final RequestCallBack rc)
            throws NumberFormatException, UnsupportedEncodingException {


        super("http://daxue."+Constant.IYBHttpHead+"/ecollege/updateStudyRecordNew.jsp?"
                + "uid="
                + uid
                + "&BeginTime="
                + URLEncoder.encode(BeginTime, "UTF-8")
                + "&EndTime="
                + URLEncoder.encode(EndTime, "UTF-8")
                + "&Lesson="
                + URLEncoder.encode(URLEncoder.encode(Lesson, "UTF-8"), "UTF-8")
                + "&LessonId="
                + LessonId
                + "&EndFlg="
                + EndFlg
                + "&platform="
                + URLEncoder.encode(android.os.Build.BRAND + " "
                + android.os.Build.MODEL, "UTF-8") + "&appName="
                + Constant.getAppfile() + "&appId=" + Constant.getAppid()
                + "&DeviceId=" + MacAddressUtil.getMacAddress() + "&format=" + format);

        Log.e("updateStudyRecord", getUrl());
        setResListener(new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObjectRoot) {
                Log.e("updateStudyRecord", "response : " + jsonObjectRoot.toString());
                try {
                    result = jsonObjectRoot.getString("result");
                    message = jsonObjectRoot.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                rc.requestResult(RequestUpdateStudyRecord.this);
            }
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        return "1".equals(result);
    }

}
