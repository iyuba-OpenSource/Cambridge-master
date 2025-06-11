package com.iyuba.camstory.vip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.iyuba.camstory.LoginActivity;
import com.iyuba.camstory.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import timber.log.Timber;

class Util {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());

    static String buildOutTradeNo() {
        String key = SDF.format(new Date());
        Random r = new Random();
        key = key + Math.abs(r.nextInt());
        key = key.substring(0, 15);
        Timber.i("Universal Trade No: %s", key);
        return key;
    }

    static void showTempUserHint(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.hint)
                .setMessage(R.string.temp_user_buy_hint)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.startActivity(new Intent(context,LoginActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }
}
