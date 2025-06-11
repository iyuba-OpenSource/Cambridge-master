package tv.lycam.gift.runner;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import tv.lycam.gift.core.Event;
import tv.lycam.gift.util.FlashUtil;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class DownloadRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {

        Context context = event.getParamAtIndex(0);
        String url = event.getParamAtIndex(1);
        String filename = event.getParamAtIndex(2);
        if (TextUtils.isEmpty(url)) return;
        Response<ResponseBody> response = API_ENGINE.downloadZip(url).execute();
        if (!FlashUtil.isZipExist(context, filename)) {
            FlashUtil.writeResponseBodyToDisk(context, response.body(), filename);
        }
        int code = response.code();
        if (code == 200) {
            event.addReturnParam(response.body());
            event.setSuccess(true);
        } else {
            try {
                String errorBody = response.errorBody().string();
                event.setFailException(new Exception(errorBody));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
