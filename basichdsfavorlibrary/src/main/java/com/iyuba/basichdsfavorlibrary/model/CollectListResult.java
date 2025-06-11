package com.iyuba.basichdsfavorlibrary.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;

import java.util.List;

/**
 * 作者：renzhy on 17/2/19 15:14
 * 邮箱：renzhongyigoo@gmail.com
 */
@Keep
public class CollectListResult {
    public String result;
    public String total;

    @SerializedName("data")
    public List<BasicHDsFavorPart> hDsFavorList;
}
