package tv.lycam.gift;

import android.text.TextUtils;

/**
 * Created by su on 16/6/1.
 */
public class Info {
    private static String appName;
    private static String apiToken;
    private static String userid;
    private static String username;
    private static String appToken;

    public static boolean UseOnlyApiServer() {
        return TextUtils.isEmpty(appToken);
    }

    public static String AppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        Info.appName = appName;
    }

    public static String ApiToken() {
        return apiToken;
    }

    public static void setApiToken(String apiToken) {
        Info.apiToken = apiToken;
    }

    public static String Userid() {
        return userid;
    }

    public static void setUserid(String userid) {
        Info.userid = userid;
    }

    public static String AppToken() {
        return appToken;
    }

    public static void setAppToken(String appToken) {
        Info.appToken = appToken;
    }

    public static void setUsername(String username) {
        Info.username = username;
    }

    public static String Username() {
        return username;
    }

    /**
     * 快速集成使用该方法
     *
     * @param appName  应用名
     * @param userid   用户ID
     * @param username 用户名
     * @param apiToken 聊天Token
     */
    public static void init(String appName, String userid, String username, String apiToken) {
        Info.appName = appName;
        Info.userid = userid;
        Info.username = username;
        Info.apiToken = apiToken;
    }

    /**
     * 用户Token存在的时候, 除获取礼物列表及发送礼物外, 其余都使用APP_SERVER
     *
     * @param appName  应用名
     * @param userid   用户ID
     * @param username 用户名
     * @param apiToken 聊天Token
     * @param appToken 用户Token
     */
    public static void init(String appName, String userid, String username, String apiToken, String appToken) {
        Info.init(appName, userid, username, apiToken);
        Info.appToken = appToken;
    }
}
