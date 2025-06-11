package com.iyuba.camstory.listener;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.iyuba.camstory.bean.VoaDetail;
import com.iyuba.camstory.utils.TextAttr;
import com.iyuba.voa.activity.setting.Constant;


/**
 * 获取voa详细信息
 * 
 * @author chentong
 * 
 */
public class DetailRequest extends BaseJsonObjectRequest {
	private static final String TAG = DetailRequest.class.getSimpleName();

	public List<VoaDetail> voaDetailsTemps = new ArrayList<VoaDetail>();
	public String result = "";
	public String message = "";

	public DetailRequest(final int voaid, ErrorListener errorListener,
			final RequestCallBack rc) {
		super(Constant.getDetailurl() + voaid + "&format=json", errorListener);
		setResListener(new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObjectRoot) {
				try {
					if (jsonObjectRoot.has("data")) {
						JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
						if (JsonArrayData != null) {
							for (int i = 0; i < JsonArrayData.length(); i++) {
								JSONObject jsonObjectData = JsonArrayData.getJSONObject(i);
								VoaDetail voaDetailTemp = new VoaDetail();
								voaDetailTemp.startTime = Double.parseDouble(jsonObjectData
										.getString("Timing"));
								if (!Constant.getAppid().equals("215")
										&& !Constant.getAppid().equals("221")) {
									voaDetailTemp.endTime = Double.parseDouble(jsonObjectData
											.getString("EndTiming"));
								}
								voaDetailTemp.sentence_cn = jsonObjectData
										.getString("sentence_cn");
								voaDetailTemp.imgwords = jsonObjectData.getString("ImgWords");
								voaDetailTemp.paraid = jsonObjectData.getString("ParaId");
								voaDetailTemp.idindex = jsonObjectData.getString("IdIndex");
								voaDetailTemp.sentence = jsonObjectData.getString("Sentence");
								voaDetailTemp.imgpath = jsonObjectData.getString("ImgPath");
								voaDetailTemp.voaid = voaid;
								voaDetailsTemps.add(voaDetailTemp);
							}
						}
					} else if (jsonObjectRoot.has("result")) {
						result = jsonObjectRoot.getString("result");
						message = TextAttr.decode(jsonObjectRoot.getString("message"));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				rc.requestResult(DetailRequest.this);
			}
		});
	}

	@Override
	public boolean isRequestSuccessful() {
        return !"0".equals(result);
    }

}
