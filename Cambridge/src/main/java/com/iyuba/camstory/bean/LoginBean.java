package com.iyuba.camstory.bean;

/**
 * Created by yq on 2017/6/20.
 */

public class LoginBean {
    /**
     * uid : 3685339
     * expireTime : 1526536904
     * result : 101
     * Amount : 0
     * vipStatus : 1
     * credits : 1435
     * message : success
     * username : yqboom
     * email : yaoqiang@iyuba.cn
     * jiFen : 0
     * imgSrc : http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=big&uid=3685339
     * money : 0
     * mobile : 0
     * isteacher : 0
     */

    private int uid;
    private int expireTime;
    private String result;
    private String Amount;
    private String vipStatus;
    private int credits;
    private String message;
    private String username;
    private String email;
    private int jiFen;
    private String imgSrc;
    private String money;
    private String mobile;
    private String isteacher;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getJiFen() {
        return jiFen;
    }

    public void setJiFen(int jiFen) {
        this.jiFen = jiFen;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "uid=" + uid +
                ", expireTime=" + expireTime +
                ", result='" + result + '\'' +
                ", Amount='" + Amount + '\'' +
                ", vipStatus='" + vipStatus + '\'' +
                ", credits=" + credits +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", jiFen=" + jiFen +
                ", imgSrc='" + imgSrc + '\'' +
                ", money='" + money + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isteacher='" + isteacher + '\'' +
                '}';
    }

    public String getIsteacher() {
        return isteacher;
    }

    public void setIsteacher(String isteacher) {
        this.isteacher = isteacher;
    }
}
