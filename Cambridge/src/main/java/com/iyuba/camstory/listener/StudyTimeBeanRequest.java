package com.iyuba.camstory.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.iyuba.camstory.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 10202 on 2015/9/30.
 */
public class StudyTimeBeanRequest extends BaseJsonObjectRequest {


    private static final String protocolCode = "62001";

    public JSONObject json;


    public StudyTimeBeanRequest(String url, final RequestCallBack rc) {
        super(url);
        setResListener(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObjectRootRoot) {
                json = jsonObjectRootRoot;
                rc.requestResult(StudyTimeBeanRequest.this);
            }
        });
    }


    @Override
    public boolean isRequestSuccessful() {
        return true;
    }
}
