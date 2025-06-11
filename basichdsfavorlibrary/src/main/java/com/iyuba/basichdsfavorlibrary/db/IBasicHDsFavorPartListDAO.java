package com.iyuba.basichdsfavorlibrary.db;

import java.util.List;

/**
 * 作者：renzhy on 17/9/20 21:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface IBasicHDsFavorPartListDAO {

    void insertOrReplaceFavorPart(BasicHDsFavorPart basicFavorPart);

    void insertBasicHDsFavorPart(BasicHDsFavorPart basicFavorPart);

    void insertBasicHDsFavorPart(List<BasicHDsFavorPart> basicDLPartList);

    void deleteBasicHDsFavorPart(String iyubaId, String userId,String type);

    void updateBasicHDsFavorPart(BasicHDsFavorPart basicDLPart);

    BasicHDsFavorPart queryBasicHDsFavorPart(String iyubaId, String userId,String type);

    List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId);
    List<BasicHDsFavorPart> queryAllBasicHDsFavorPart(String userId,String flag);
    List<BasicHDsFavorPart> queryAllSyznBasicHDsFavorPart(String userId,String Synflg);
    List<BasicHDsFavorPart> queryBasicHDsFavorPartByCategory(String userId,String category);

    List<BasicHDsFavorPart> queryBasicHDsFavorPartByType(String userId,String type);

    List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId,int count, int offset);
    List<BasicHDsFavorPart> queryBasicHDsFavorPartByPage(String userId,String flag,int count, int offset);
    List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndCate(String userId,String category, int count, int offset);

    List<BasicHDsFavorPart> queryBasicHDsFavorPartByPageAndType(String userId,String type, int count, int offset);
}
