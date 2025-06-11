package com.iyuba.camstory.listener;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by 10202 on 2015/9/30.
 */
public class SignBeanRequest extends BaseJsonObjectRequest {



    public JSONObject json;


    public SignBeanRequest(String url, final RequestCallBack rc) {
        super(url);
        setResListener(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObjectRootRoot) {
                json = jsonObjectRootRoot;
                rc.requestResult(SignBeanRequest.this);
            }
        });
    }


    @Override
    public boolean isRequestSuccessful() {
        return true;
    }
}
