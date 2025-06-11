package tv.lycam.gift.runner;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;
import tv.lycam.gift.Info;
import tv.lycam.gift.core.Event;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class SendChatRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String topic = event.getParamAtIndex(0);
        String msg = event.getParamAtIndex(1);
        String receiver = event.getParamAtIndex(2);
        Response<JSONObject> response;
        if (Info.UseOnlyApiServer()) {
            response = API_ENGINE.sendMessage(Info.ApiToken(), topic, msg, Info.Userid()).execute();
        } else {
//            AppMessage model = new AppMessage(Info.Userid(), msg, "chat", receiver, topic, "2", 0, 0);
            response = APP_ENGINE.sendMessage(Info.AppToken(), topic, msg).execute();
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
