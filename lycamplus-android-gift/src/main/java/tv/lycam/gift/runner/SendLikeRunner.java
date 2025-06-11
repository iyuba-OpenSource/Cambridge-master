package tv.lycam.gift.runner;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;
import tv.lycam.gift.Info;
import tv.lycam.gift.core.Event;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class SendLikeRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String topic = event.getParamAtIndex(0);
        Response<JSONObject> response;
        if (Info.UseOnlyApiServer()) {
            response = API_ENGINE.sendLike(Info.ApiToken(), topic, Info.Userid()).execute();
        } else {
            response = APP_ENGINE.sendLike(Info.AppToken(), topic, Info.Userid()).execute();
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
