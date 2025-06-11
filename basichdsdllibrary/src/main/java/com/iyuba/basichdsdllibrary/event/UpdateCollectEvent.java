package com.iyuba.basichdsdllibrary.event;

import com.iyuba.basichdsdllibrary.db.BasicHDsDLPart;

/**
 * 作者：renzhy on 17/10/24 00:24
 * 邮箱：renzhongyigoo@gmail.com
 */
public class UpdateCollectEvent {

    public BasicHDsDLPart basicHDsDLPart;

    public UpdateCollectEvent(BasicHDsDLPart basicHDsDLPart) {
        this.basicHDsDLPart = basicHDsDLPart;
    }
}
