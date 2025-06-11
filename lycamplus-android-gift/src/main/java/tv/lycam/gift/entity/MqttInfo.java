package tv.lycam.gift.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */

public class MqttInfo implements Parcelable {

    private boolean isAudience = true;
    private String chatUrl;
    private String chatChannel;
    private String chatToken;
    private String mReceiverUuid;
    private String status = "live";

    public MqttInfo(String chatUrl, String chatChannel, String chatToken, String receiverId) {
        this.chatUrl = chatUrl;
        this.chatChannel = chatChannel;
        this.chatToken = chatToken;
        this.mReceiverUuid = receiverId;
    }

    public MqttInfo(String chatUrl, String chatChannel, String chatToken, String receiverId, boolean isAudience) {
        this(chatUrl, chatChannel, chatToken, receiverId);
        this.isAudience = isAudience;
    }

    public MqttInfo(String chatUrl, String chatChannel, String chatToken, String receiverId, boolean isAudience, String status) {
        this(chatUrl, chatChannel, chatToken, receiverId, isAudience);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReceiverUuid(String receiverUuid) {
        mReceiverUuid = receiverUuid;
    }

    public String getChatUrl() {
        return chatUrl;
    }

    public void setChatUrl(String chatUrl) {
        this.chatUrl = chatUrl;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public boolean isAudience() {
        return isAudience;
    }

    public void setAudience(boolean audience) {
        this.isAudience = audience;
    }

    public String getReceiverUuid() {
        return mReceiverUuid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isAudience ? (byte) 1 : (byte) 0);
        dest.writeString(this.chatUrl);
        dest.writeString(this.chatChannel);
        dest.writeString(this.chatToken);
        dest.writeString(this.mReceiverUuid);
    }

    protected MqttInfo(Parcel in) {
        this.isAudience = in.readByte() != 0;
        this.chatUrl = in.readString();
        this.chatChannel = in.readString();
        this.chatToken = in.readString();
        this.mReceiverUuid = in.readString();
    }

    public static final Parcelable.Creator<MqttInfo> CREATOR = new Parcelable.Creator<MqttInfo>() {
        @Override
        public MqttInfo createFromParcel(Parcel source) {
            return new MqttInfo(source);
        }

        @Override
        public MqttInfo[] newArray(int size) {
            return new MqttInfo[size];
        }
    };
}