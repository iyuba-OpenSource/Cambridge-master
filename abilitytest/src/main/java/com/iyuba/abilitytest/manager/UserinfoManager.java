package com.iyuba.abilitytest.manager;


/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.manager
 * @class describe
 * @time 2018/6/25 16:16
 * @change
 * @chang time
 * @class describe
 */
public class UserinfoManager {

    public String getAPP_ID() {
        return APP_ID;
    }

    public String getUSERID() {
        return USERID;
    }




    public boolean getVIPSTATUS() {
        return !VIPSTATUS.equals("0");
    }

    public boolean getLoginState() {
        return LOGIN_STATE;
    }

    public String getUserName() {
        return USER_NAME;
    }


    private String APP_ID ;
    private String USERID ;
    private String VIPSTATUS ;
    private Boolean LOGIN_STATE;
    private String  USER_NAME;
    private static UserinfoManager instance;

    public static UserinfoManager getInstance() {
        if (instance == null) {
            instance = new UserinfoManager();
        }
        return instance;
    }

    public void init(String APP_ID,String USERID,String VIPSTATUS ,boolean login , String username){
        this.APP_ID = APP_ID;
        this.USERID = USERID;
        this.VIPSTATUS = VIPSTATUS;
        this.LOGIN_STATE = login;
        this.USER_NAME = username;

    }



}
