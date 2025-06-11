package com.iyuba.abilitytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iyuba.abilitytest.activity.ImoocSplashActivity;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest
 * @class describe
 * @time 2018/6/20 15:46
 * @change
 * @chang time
 * @class describe
 */
public class StartTestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         Intent intentNew = new Intent(context, ImoocSplashActivity.class);
         intentNew.putExtra("lessonId",intent.getStringExtra("lessonId"));
        intentNew.putExtra("lessonType",intent.getStringExtra("lessonType"));
        intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentNew);
    }
}
