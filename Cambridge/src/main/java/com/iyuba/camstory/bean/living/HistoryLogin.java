package com.iyuba.camstory.bean.living;

/**
 * Created by 10202 on 2015/11/23.
 */
public class HistoryLogin {
    private int userid;
    private String userName;
    private String userPwd;
    private String userPwdHash;
    private String loginTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwdHash() {
        return userPwdHash;
    }

    public void setUserPwdHash(String userPwdHash) {
        this.userPwdHash = userPwdHash;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "HistoryLogin{" +
                "userid=" + userid +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userPwdHash='" + userPwdHash + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }
}
