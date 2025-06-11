package com.iyuba.basichdsfavorlibrary.manager;

import com.iyuba.basichdsfavorlibrary.util.Constant;

/**
 * 作者：renzhy on 17/10/10 11:08
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsFavorConstantManager {
    public static String HEADLINES_COLLECT_ENDPOINT = "http://apps."+Constant.IYBHttpHead+"/";
    public static String IYUBA_CMS_ENDPOINT = "http://cms."+Constant.IYBHttpHead+"/";
    public static String GET_COLLECT_LIST_TOPIC = "all";
    public static String GET_COLLECT_NOT_SENTENCE_FLG = "0";
    public static String GSON_FORMAT = "json";
    public static String IYUBA_FLAG = "iyuba";
    public static String COLLECT_GroupName_FLAG = "Iyuba";

    public static String BASICHDS_FAVOR_INSERTFROM = "basichdsfavorlibrary";
    public static String HDS_COLLECT_YES = "1";
    public static String HDS_COLLECT_NO = "0";
    public static String HDS_COLLECT_SYNC_YES = "1";
    public static String HDS_COLLECT_SYNC_NO = "0";
    public static String HDS_COLLECT_TYPE_INSERT = "insert";
    public static String HDS_COLLECT_TYPE_DEL = "del";
}
