package com.iyuba.basichdsfavorlibrary.db;

import android.content.Context;

import java.util.List;

/**
 * 作者：renzhy on 17/9/21 20:25
 * 邮箱：renzhongyigoo@gmail.com
 */
public final class BasicHDsFavorDBManager implements IBasicHDsFavorPartListDAO {

    private static BasicHDsFavorDBManager sManager;

    private BasicHDsFavorPartListDAO daoBasicHDsFavorPartList;

    private BasicHDsFavorDBManager(Context context) {
        daoBasicHDsFavorPartList = new BasicHDsFavorPartListDAO(context);
    }

    public static BasicHDsFavorDBManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new BasicHDsFavorDBManager(context);
        }
        return sManager;
    }

    @Override
    public void insertOrReplaceFavorPart(BasicHDsFavorPart basicFavorPart) {
        daoBasicHDsFavorPartList.insertOrReplaceFavorPart(basicFavorPart);
    }

    @Override
    public void insertBasicHDsFavorPart(BasicHDsFavorPart basicFavorPart) {
        daoBasicHDsFavorPartList.insertBasicHDsFavorPart(basicFavorPart);
    }

    @Override
    public void insertBasicHDsFavorPart(List<BasicHDsFavorPart> basicFavorPartList) {
        daoBasicHDsFavorPartList.insertBasicHDsFavorPart(basicFavorPartList);
    }

    @Override
    public void deleteBasicHDsFavorPart(String iyubaId, String userId,String type) {
        daoBasicHDsFavorPartList.deleteBasicHDsFavorPart(iyubaId,userId,type);
    }

    @Override
    public void updateBasicHDsFavorPart(BasicHDsFavorPart basicFavorPart) {
        daoBasicHDsFavorPartList.updateBasicHDsFavorPart(basicFavorPart);
    }

    @Override
    public BasicHDsFavorPart queryBasicHDsFavorPart(String iyubaId, String userId,String type) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPart(iyubaId,userId,type);
    }

    @Override
    public List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId) {
        return daoBasicHDsFavorPartList.queryAllBasicHDsFavorPart(userId);
    }

    @Override
    public List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId, String flag) {
        return daoBasicHDsFavorPartList.queryAllBasicHDsFavorPart(userId,flag);
    }

    @Override
    public List<BasicHDsFavorPart> queryAllSyznBasicHDsFavorPart(String userId, String Synflg) {
        return daoBasicHDsFavorPartList.queryAllSyznBasicHDsFavorPart(userId,Synflg);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByCategory(String userId,String category) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByCategory(userId,category);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByType(String userId,String type) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByType(userId,type);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId,int count, int offset) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByPage(userId,count,offset);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId, String flag, int count, int offset) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByPage(userId,flag,count,offset);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndCate(String userId,String category, int count, int offset) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByPageAndCate(userId,category,count,offset);
    }

    @Override
    public List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndType(String userId,String type, int count, int offset) {
        return daoBasicHDsFavorPartList.queryBasicHDsFavorPartByPageAndType(userId,type,count,offset);
    }
}
