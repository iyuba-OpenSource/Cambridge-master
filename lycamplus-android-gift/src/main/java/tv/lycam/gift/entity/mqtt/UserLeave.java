package tv.lycam.gift.entity.mqtt;

/**
 * Created by su on 16/6/25.
 */
public class UserLeave {

//    private Content content;
//    private String type;
//
//    public Content getContent() {
//        return content;
//    }
//
//    public void setContent(Content content) {
//        this.content = content;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public static class Content {
//        private String username;
//        private String uuid;
//        private String description;
//        private boolean gender;
//        private int level;
//        private String avatarUrl;
//        private int followersCount;
//        private int friendsCount;
//        private int statusCount;
//        private int balance;
//        private int contribution;
//        private int popularity;
//        private int experience;
//        private String createdAt;
//        private String id;
//        private String displayName;
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getUuid() {
//            return uuid;
//        }
//
//        public void setUuid(String uuid) {
//            this.uuid = uuid;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public boolean isGender() {
//            return gender;
//        }
//
//        public void setGender(boolean gender) {
//            this.gender = gender;
//        }
//
//        public int getLevel() {
//            return level;
//        }
//
//        public void setLevel(int level) {
//            this.level = level;
//        }
//
//        public String getAvatarUrl() {
//            return avatarUrl;
//        }
//
//        public void setAvatarUrl(String avatarUrl) {
//            this.avatarUrl = avatarUrl;
//        }
//
//        public int getFollowersCount() {
//            return followersCount;
//        }
//
//        public void setFollowersCount(int followersCount) {
//            this.followersCount = followersCount;
//        }
//
//        public int getFriendsCount() {
//            return friendsCount;
//        }
//
//        public void setFriendsCount(int friendsCount) {
//            this.friendsCount = friendsCount;
//        }
//
//        public int getStatusCount() {
//            return statusCount;
//        }
//
//        public void setStatusCount(int statusCount) {
//            this.statusCount = statusCount;
//        }
//
//        public int getBalance() {
//            return balance;
//        }
//
//        public void setBalance(int balance) {
//            this.balance = balance;
//        }
//
//        public int getContribution() {
//            return contribution;
//        }
//
//        public void setContribution(int contribution) {
//            this.contribution = contribution;
//        }
//
//        public int getPopularity() {
//            return popularity;
//        }
//
//        public void setPopularity(int popularity) {
//            this.popularity = popularity;
//        }
//
//        public int getExperience() {
//            return experience;
//        }
//
//        public void setExperience(int experience) {
//            this.experience = experience;
//        }
//
//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getDisplayName() {
//            return displayName;
//        }
//
//        public void setDisplayName(String displayName) {
//            this.displayName = displayName;
//        }
//    }

    private String type;
    private MetaInfo metaInfo;

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
