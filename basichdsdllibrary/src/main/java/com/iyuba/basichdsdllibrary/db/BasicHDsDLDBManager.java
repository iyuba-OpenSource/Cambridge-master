package com.iyuba.basichdsdllibrary.db;

import android.content.Context;

import java.util.List;

/**
 * 作者：renzhy on 17/9/21 20:25
 * 邮箱：renzhongyigoo@gmail.com
 */
public final class BasicHDsDLDBManager implements IBasicHDsDLPartListDAO {

    private static BasicHDsDLDBManager sManager;

    private BasicHDsDLPartListDAO daoBasicHDsDLPartList;

    private BasicHDsDLDBManager(Context context) {
        daoBasicHDsDLPartList = new BasicHDsDLPartListDAO(context);
    }

    public static BasicHDsDLDBManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new BasicHDsDLDBManager(context);
        }
        return sManager;
    }

    @Override
    public void insertBasicHDsDLPart(BasicHDsDLPart basicDLPart) {
        daoBasicHDsDLPartList.insertBasicHDsDLPart(basicDLPart);
    }

    @Override
    public void insertBasicHDsDLPart(List<BasicHDsDLPart> basicDLPartList) {
        daoBasicHDsDLPartList.insertBasicHDsDLPart(basicDLPartList);
    }

    @Override
    public void deleteBasicHDsDLPart(String iyubaId, String type) {
        daoBasicHDsDLPartList.deleteBasicHDsDLPart(iyubaId,type);
    }

    @Override
    public void updateBasicHDsDLPart(String iyubaId, String type, BasicHDsDLPart basicDLPart) {
        daoBasicHDsDLPartList.updateBasicHDsDLPart(iyubaId,type,basicDLPart);
    }

    @Override
    public BasicHDsDLPart queryBasicHDsDLPart(String iyubaId, String type) {
        return daoBasicHDsDLPartList.queryBasicHDsDLPart(iyubaId,type);
    }

    @Override
    public List<BasicHDsDLPart> queryAllBasicHDsDLPart() {
        return daoBasicHDsDLPartList.queryAllBasicHDsDLPart();
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByCategory(String category) {
        return daoBasicHDsDLPartList.queryBasicHDsDLPartByCategory(category);
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByType(String type) {
        return daoBasicHDsDLPartList.queryBasicHDsDLPartByType(type);
    }

    @Override
    public List<BasicHDsDLPart> queryByPage(int count, int offset) {
        return daoBasicHDsDLPartList.queryByPage(count,offset);
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndCate(String category, int count, int offset) {
        return daoBasicHDsDLPartList.queryBasicHDsDLPartByPageAndCate(category,count,offset);
    }

    @Override
    public List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndType(String type, int count, int offset) {
        return daoBasicHDsDLPartList.queryBasicHDsDLPartByPageAndType(type,count,offset);
    }
}
