package com.iyuba.basichdsdllibrary.db;

import java.util.List;

/**
 * 作者：renzhy on 17/9/20 21:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface IBasicHDsDLPartListDAO {
    void insertBasicHDsDLPart(BasicHDsDLPart basicDLPart);

    void insertBasicHDsDLPart(List<BasicHDsDLPart> basicDLPartList);

    void deleteBasicHDsDLPart(String iyubaId,String type);

    void updateBasicHDsDLPart(String iyubaId,String type,BasicHDsDLPart basicDLPart);

    BasicHDsDLPart queryBasicHDsDLPart(String iyubaId,String type);

    List<BasicHDsDLPart> queryAllBasicHDsDLPart();

    List<BasicHDsDLPart> queryBasicHDsDLPartByCategory(String category);

    List<BasicHDsDLPart> queryBasicHDsDLPartByType(String type);

    List<BasicHDsDLPart> queryByPage(int count,int offset);

    List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndCate(String category,int count,int offset);

    List<BasicHDsDLPart> queryBasicHDsDLPartByPageAndType(String type,int count,int offset);
}
