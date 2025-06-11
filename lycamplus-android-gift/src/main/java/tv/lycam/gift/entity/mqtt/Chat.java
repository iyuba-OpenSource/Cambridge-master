package tv.lycam.gift.entity.mqtt;

/**
 * Created by su on 16/6/25.
 */
public class Chat {


    /**
     * type : message
     * metaInfo : {"content":"11","tid":"1212","uid":"1","avatarUrl":"111","nickname":"1"}
     * messageId : 57920622KL7I
     */

    private String type;
    /**
     * content : 11
     * tid : 1212
     * uid : 1
     * avatarUrl : 111
     * nickname : 1
     */

    private MetaInfoBean metaInfo;
    private String messageId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MetaInfoBean getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfoBean metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public static class MetaInfoBean {
        private String content;
        private String tid;
        private String uid;
        private String avatarUrl;
        private String nickname;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
