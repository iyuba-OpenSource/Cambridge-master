package com.iyuba.camstory.protocol.dao;

interface IUpVoteDAO {
    String TABLE_NAME = "CommentAgreeCount";

    String USERID = "userid";
    String CID = "cid";

    boolean isClickZAN(String uid, String id);

    void saveClickZan(String uid, String id);
}
