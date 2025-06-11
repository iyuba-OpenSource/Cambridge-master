package com.iyuba.basichdsfavorlibrary.util;

/**
 * 作者：renzhy on 17/8/12 15:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHeadlineJudgeVideoCatg {

    public static boolean isVideoCatg(String type){
        switch ( type ){
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                return true;
            default:
                return false;
        }
    }
}
