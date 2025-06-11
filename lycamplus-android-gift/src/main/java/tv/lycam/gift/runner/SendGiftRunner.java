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
public class SendGiftRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String giftId = event.getParamAtIndex(0);
        String receiver = event.getParamAtIndex(1);
        String topic = event.getParamAtIndex(2);
        Response<Result> response = API_ENGINE.sendGift(Info.ApiToken(), Info.Userid(), giftId, receiver, topic).execute();
        int code = response.code();
        String balance = null;
        if (code == 200) {
            Result body = response.body();
            balance = String.valueOf(body.getBalance());
            event.setSuccess(true);
        } else {
            try {
                String errorBody = response.errorBody().string();
                JSONObject obj = new JSONObject(errorBody);
                balance = obj.optString("error_description");
                event.setSuccess(true);
            } catch (IOException e) {
                e.printStackTrace();
                event.setFailException(new Exception(e));
            }
        }
        event.addReturnParam(balance);
    }

}
