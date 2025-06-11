package tv.lycam.gift.entity;

/**
 * Created by su on 16/5/24.
 */
public class GiftReceived {

    private String userId;
    private String avatarUrl;
    private String userName;
    private String giftId;

    public GiftReceived(String avatarUrl, String userName, String giftId) {
        this.userName = userName;
        this.giftId = giftId;
        this.avatarUrl = avatarUrl;
    }

    public GiftReceived(String userId, String avatarUrl, String userName, String giftId) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.giftId = giftId;
    }

    public String getUserId(){
        return userId;
    }

    public String getGiftId() {
        return giftId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUserName() {
        return userName;
    }
}
