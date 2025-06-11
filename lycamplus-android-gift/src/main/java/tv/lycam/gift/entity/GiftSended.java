package tv.lycam.gift.entity;

/**
 * Created by su on 16/6/6.
 */
public class GiftSended {
    private String giftId;
    private String receiver;
    private String topic;
    private int count = 1;

    public GiftSended(String giftId, String receiver, String topic) {
        this.giftId = giftId;
        this.receiver = receiver;
        this.topic = topic;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
