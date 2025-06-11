package com.iyuba.basichdsfavorlibrary.util;

/**
 * 作者：renzhy on 17/8/12 15:45
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHeadlineJudgeAudioCatg {

    public static boolean isAudioCatg(String type){
        switch ( type ){
            case "bbc":
            case "voa":
            case "csvoa":
            case "song":
                return true;
            default:
                return false;
        }
    }
}
