package com.iyuba.basichdsfavorlibrary.util;

import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;
import com.iyuba.basichdsfavorlibrary.model.SyncCollect;
import com.iyuba.basichdsfavorlibrary.model.SyncCollectResutl;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.BASICHDS_FAVOR_INSERTFROM;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_SYNC_YES;
import static com.iyuba.basichdsfavorlibrary.manager.BasicHDsFavorConstantManager.HDS_COLLECT_YES;

/**
 * 作者：renzhy on 17/2/16 17:51
 * 邮箱：renzhongyigoo@gmail.com
 */
public class SyncCollectResultToItemsMapper implements Func1<SyncCollectResutl,List<BasicHDsFavorPart>> {
    private static SyncCollectResultToItemsMapper instance = new SyncCollectResultToItemsMapper();
    private String userId;

    private SyncCollectResultToItemsMapper(){}

    public static SyncCollectResultToItemsMapper getInstance(){
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public List<BasicHDsFavorPart> call(SyncCollectResutl syncCollectResutl) {
        List<SyncCollect> syncCollectList = syncCollectResutl.getSyncCollectList();
        List<BasicHDsFavorPart> basicHDsFavorPartList = new ArrayList<>();
        for(SyncCollect syncCollect:syncCollectList){
            if(syncCollect.getId()!=null||!"".equals(syncCollect.getId())||!"NULL".equals(syncCollect.getId())
                    ||!"null".equals(syncCollect.getId())){
            BasicHDsFavorPart basicHDsFavorPart = new BasicHDsFavorPart();
            basicHDsFavorPart.setId(syncCollect.getId());
            basicHDsFavorPart.setUserId(getUserId());
            basicHDsFavorPart.setType(syncCollect.getType());
            basicHDsFavorPart.setCategory(syncCollect.getCategory());
            basicHDsFavorPart.setCategoryName(syncCollect.getCategoryName());
            basicHDsFavorPart.setCreateTime(syncCollect.getCreateTime());
            basicHDsFavorPart.setPic(syncCollect.getPic());
            basicHDsFavorPart.setTitle_cn(syncCollect.getTitle_cn());
            basicHDsFavorPart.setTitle(syncCollect.getTitle());
            basicHDsFavorPart.setSynflg(HDS_COLLECT_SYNC_YES);
            basicHDsFavorPart.setInsertFrom(BASICHDS_FAVOR_INSERTFROM);
            basicHDsFavorPart.setOther1("");
            basicHDsFavorPart.setOther2("");
            basicHDsFavorPart.setOther3("");
            basicHDsFavorPart.setFlag(HDS_COLLECT_YES);
            basicHDsFavorPart.setCollectTime(syncCollect.getCollectDate());
            basicHDsFavorPart.setSound(syncCollect.getSound());
            basicHDsFavorPartList.add(basicHDsFavorPart);
            }
        }
        return basicHDsFavorPartList;
    }
}
