package tv.lycam.gift.runner;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;
import tv.lycam.gift.Info;
import tv.lycam.gift.core.Event;
import tv.lycam.gift.entity.Result;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class SendBarrageRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String topic = event.getParamAtIndex(0);
        String msg = event.getParamAtIndex(1);
        String receiver = event.getParamAtIndex(2);
        //Response<Result> response = null;
        Response<JSONObject> response = null;
        if (Info.UseOnlyApiServer()) {
            response = API_ENGINE.sendBarrage(Info.ApiToken(), topic, msg, Info.Userid()).execute();
        } else {
            try {
                response = APP_ENGINE.sendBarrage(Info.AppToken(), topic, msg).execute();
            }catch (Exception e){

            }
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
