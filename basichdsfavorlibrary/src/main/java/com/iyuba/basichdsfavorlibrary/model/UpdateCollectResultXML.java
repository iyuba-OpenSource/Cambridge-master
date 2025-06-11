package com.iyuba.basichdsfavorlibrary.model;

import androidx.annotation.Keep;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 作者：renzhy on 17/10/20 09:46
 * 邮箱：renzhongyigoo@gmail.com
 */

@Keep
@Root(name = "response",strict = false)
public class UpdateCollectResultXML {
    @Element(required = false)
    public String result;
    @Element(required = false)
    public String msg;
    @Element(required = false)
    public String type;
    @Element(required = false)
    public String topic;
}
