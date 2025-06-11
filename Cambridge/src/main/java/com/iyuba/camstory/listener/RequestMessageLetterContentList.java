/**
 *
 */
package com.iyuba.camstory.listener;

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
 * @author
 */
public class RequestMessageLetterContentList extends BaseJsonObjectRequest {
    private static final String protocolCode = "60004";

    private String result;// 返回代码
    public String message;// 返回信息
    public MessageLetterContent letter = new MessageLetterContent();
    public JSONArray data;
    public ArrayList<MessageLetterContent> list;
    public int num;
    public String plid;

    public RequestMessageLetterContentList(int uid, String friendid, final RequestCallBack rc) {
        super("http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=" + protocolCode
                + "&uid=" + uid
                + "&friendid=" + friendid
                //+ "&pageNumber=" + 50
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
        setResListener(new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObjectRootRoot) {
                // TODO 自动生成的方法存根
                list = new ArrayList<MessageLetterContent>();
                try {
                    result = jsonObjectRootRoot.getString("result");
                    message = TextAttr.decode(jsonObjectRootRoot.getString("message"));
                    //plid = jsonObjectRootRoot.getString("plid");
                    if (result.equals("631")) {
                        data = jsonObjectRootRoot.getJSONArray("data");
                        if (data != null && data.length() != 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = ((JSONObject) data.opt(i));
                                MessageLetterContent letter = new MessageLetterContent();
                                letter.message = TextAttr.decode(jsonObject.getString("message"));
                                letter.authorid = jsonObject.getInt("authorid");
                                letter.dateline = jsonObject.getString("dateline");
                                letter.pmid = jsonObject.getString("authorid");
                                list.add(letter);
                            }
                            System.out.println("changdu===" + list.size());
                        }
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                rc.requestResult(RequestMessageLetterContentList.this);
            }
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        // TODO 自动生成的方法存根
        return "631".equals(result);
    }

}

