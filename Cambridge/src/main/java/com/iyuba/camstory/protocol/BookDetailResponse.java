package com.iyuba.camstory.protocol;


import com.iyuba.camstory.utils.GsonUtils;
import com.iyuba.http.toolbox.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivotsm on 2017/2/22.
 */

public class BookDetailResponse extends BaseJSONResponse {
    public String result = "";
    public BookDetail bookDetail;
    public String message = "";
    @Override
    protected boolean extractBody(JSONObject headerElement, String bodyElement) {
        try {
            JSONObject jsonRoot = new JSONObject(bodyElement);
            result = jsonRoot.getString("result");
            message = jsonRoot.getString("message");
            bookDetail = GsonUtils.toObject(jsonRoot.getString("data"), BookDetail.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}
