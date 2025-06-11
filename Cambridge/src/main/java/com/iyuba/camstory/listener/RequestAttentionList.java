package com.iyuba.camstory.listener;

import com.android.volley.Response.Listener;
import com.iyuba.camstory.bean.AttentionBean;
import com.iyuba.camstory.sqlite.mode.Fans;
import com.iyuba.camstory.utils.MD5;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.configation.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestAttentionList extends BaseJsonObjectRequest {
    private static final String protocolCode = "51001";
    private String md5Status = "1"; // 0=未加密,1=加密
    private static final int pageCounts = 30;


    private String result;// 返回代码
    public String message;// 返回信息
    public Fans fan = new Fans();
    public JSONArray data;
    public ArrayList<AttentionBean.DataBean> fansList;
    public int num;

    public RequestAttentionList(int uid, int pageNumber, final RequestCallBack rc) {
        super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode
                + "&uid=" + uid
                + "&pageNumber=" + pageNumber
                + "&pageCounts=" + pageCounts
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));

        setResListener(new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObjectRootRoot) {
                // TODO 自动生成的方法存根
                fansList = new ArrayList<AttentionBean.DataBean>();
                try {
                    result = jsonObjectRootRoot.getString("result");
                    message = TextAttr.decode(jsonObjectRootRoot.getString("message"));
                    if (result.equals("550")) {
                        num = jsonObjectRootRoot.getInt("num");
                        data = jsonObjectRootRoot.getJSONArray("data");
                        if (data != null && data.length() != 0) {
                            for (int i = 0; i < data.length(); i++) {
                                AttentionBean.DataBean fans = new AttentionBean.DataBean();
                                JSONObject jsonObject = ((JSONObject) data.opt(i));
//								fans.bkname = jsonObject.getString("bkname");
                                fans.setMutual(jsonObject.getString("mutual"));
                                fans.setFollowuid(jsonObject.getString("followuid"));
                                fans.setDateline(jsonObject.getString("dateline"));
                                fans.setFusername(jsonObject.getString("fusername"));
                                fans.setDoing(jsonObject.getString("doing"));
                                fansList.add(fans);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                rc.requestResult(RequestAttentionList.this);
            }
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        // TODO 自动生成的方法存根
        return "550".equals(result);
    }

}

