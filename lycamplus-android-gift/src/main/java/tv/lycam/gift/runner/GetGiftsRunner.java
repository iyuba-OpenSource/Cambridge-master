package tv.lycam.gift.runner;

import java.io.IOException;

import retrofit2.Response;
import tv.lycam.gift.Info;
import tv.lycam.gift.core.Event;
import tv.lycam.gift.entity.GiftInfo;

/**
 * Created by lycamandroid on 16/5/31.
 */
public class GetGiftsRunner extends EngineRunner {

    @Override
    public void onEventRun(Event event) throws Exception {

        String userToken = Info.ApiToken();
        Response<GiftInfo> response = API_ENGINE.getGiftList(userToken).execute();
        int code = response.code();
        if (code == 200) {
//            gifts = response.body();
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
