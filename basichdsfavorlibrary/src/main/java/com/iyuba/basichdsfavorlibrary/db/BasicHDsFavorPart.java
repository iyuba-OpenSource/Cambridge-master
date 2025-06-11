package com.iyuba.basichdsfavorlibrary.db;

/**
 * 作者：renzhy on 17/9/20 21:00
 * 邮箱：renzhongyigoo@gmail.com
 */
public class BasicHDsFavorPart {
    private String Id;
    private String UserId;
    private String Type;
    private String Category;
    private String CategoryName;
    private String CreateTime;
    private String Pic;
    private String Title_cn;
    private String Title;
    private String Synflg;
    private String InsertFrom;

    private String Other1;
    private String Other2;
    private String Other3;
    private String Flag;
    private String CollectTime;
    private String Sound;
    private String Source;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getTitle_cn() {
        return Title_cn;
    }

    public void setTitle_cn(String title_cn) {
        Title_cn = title_cn;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSynflg() {
        return Synflg;
    }

    public void setSynflg(String synflg) {
        Synflg = synflg;
    }

    public String getInsertFrom() {
        return InsertFrom;
    }

    public void setInsertFrom(String insertFrom) {
        InsertFrom = insertFrom;
    }

    public String getOther1() {
        return Other1;
    }

    public void setOther1(String other1) {
        Other1 = other1;
    }

    public String getOther2() {
        return Other2;
    }

    public void setOther2(String other2) {
        Other2 = other2;
    }

    public String getOther3() {
        return Other3;
    }

    public void setOther3(String other3) {
        Other3 = other3;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getCollectTime() {
        return CollectTime;
    }

    public void setCollectTime(String collectTime) {
        CollectTime = collectTime;
    }

    public String getSound() {
        return Sound;
    }

    public void setSound(String sound) {
        Sound = sound;
    }

    public String getSource() {
        return Source;
    }

    @Override
    public String toString() {
        return "BasicHDsFavorPart{" +
                "Id='" + Id + '\'' +
                ", UserId='" + UserId + '\'' +
                ", Type='" + Type + '\'' +
                ", Category='" + Category + '\'' +
                ", CategoryName='" + CategoryName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", Pic='" + Pic + '\'' +
                ", Title_cn='" + Title_cn + '\'' +
                ", Title='" + Title + '\'' +
                ", Synflg='" + Synflg + '\'' +
                ", InsertFrom='" + InsertFrom + '\'' +
                ", Other1='" + Other1 + '\'' +
                ", Other2='" + Other2 + '\'' +
                ", Other3='" + Other3 + '\'' +
                ", Flag='" + Flag + '\'' +
                ", CollectTime='" + CollectTime + '\'' +
                ", Sound='" + Sound + '\'' +
                ", Source='" + Source + '\'' +
                '}';
    }

    public void setSource(String source) {
        Source = source;
    }
}
