package com.iyuba.abilitytest.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录已经做过的题目年份，做过的字体颜色改变
 */
public class TestedUtil {
    private static final String TAG = "TestedUtil";
    private static final String KEY = "ability-test-lessonId";

    public static void saveLessonId(String lessonId) {
        Log.e(TAG, "saveLessonId : " + lessonId);
        String lessonIdStr = ConfigManager.Instance().loadString(KEY);
        if (TextUtils.isEmpty(lessonIdStr)) {
            ArrayList<String> list = new ArrayList<>();
            list.add(lessonId);
            ConfigManager.Instance().putString(KEY, new Gson().toJson(list));
        } else {
            List<String> lessonIds = new Gson().fromJson(lessonIdStr, new TypeToken<List<String>>() {
            }.getType());
            if (lessonIds.contains(lessonId)) {
                return;
            }
            lessonIds.add(lessonId);
            ConfigManager.Instance().putString(KEY, new Gson().toJson(lessonIds));
        }
    }

    public static boolean isTested(String lessonId) {
        Log.e(TAG, "isTested : " + lessonId);
        String lessonIdStr = ConfigManager.Instance().loadString(KEY);
        if (TextUtils.isEmpty(lessonIdStr)) {
            return false;
        }
        List lessonIds = new Gson().fromJson(lessonIdStr, new TypeToken<List<String>>() {
        }.getType());

        if (lessonIds == null || lessonIds.size() <= 0) {
            return false;
        }

        return lessonIds.contains(lessonId);
    }


    public static void removeLessonId(String lessonId) {
        Log.e(TAG, "removeLessonId : " + lessonId);
        String lessonIdStr = ConfigManager.Instance().loadString(KEY);
        if (TextUtils.isEmpty(lessonIdStr)) {
            return;
        }
        List lessonIds = new Gson().fromJson(lessonIdStr, new TypeToken<List<String>>() {
        }.getType());

        if (lessonIds == null || lessonIds.size() <= 0) {
            return;
        }

        if (lessonIds.contains(lessonId)) {
            lessonIds.remove(lessonId);
        }

        ConfigManager.Instance().putString(KEY, new Gson().toJson(lessonIds));
    }

}
