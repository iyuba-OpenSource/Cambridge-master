package com.iyuba.camstory.lycam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lycamandroid on 16/6/4.
 * 视频信息
 */
public class StreamInfoModel implements Parcelable {
    /**
     * username : 18508242165
     * displayName : 小李子
     * roomId : 000013
     * uuid : a8232cb0-4cc3-11e6-b559-9f7e28db045c
     * role : teacher
     * authentication : true
     * historyCount : 0
     * historyList : {}
     * subcribeCount : 0
     * subcribeList : {}
     * education : [{"time":"2009--2013","school":"Northeast Normal University"}]
     * workExperience : [{"time":"2011-2015","content":"asdfkasdfasdf"},{"time":"2011-2016","content":"gasdfasdfadgs"}]
     * IDNumber : 5107251999111111
     * major : Physics
     * sex : 0
     * description :
     * gender : false
     * level : 1
     * avatarUrl :
     * followersCount : 1
     * friendsCount : 0
     * statusCount : 0
     * balance : 0
     * lycamBeans : 0
     * contribution : 0
     * experience : 0
     * blacklist : {}
     * type : default
     * createdAt : 2016-07-18T08:43:10.662Z
     * phone : 18508242165
     * subscribeList : {"dev-882bee90-4cde-11e6-925f-453e8ead406d":{"user":"578c969edddfbfaa256c67c2","streamId":"dev-882bee90-4cde-11e6-925f-453e8ead406d","roomId":"000013","title":null,"description":null,"streamUrl":"rtmp://rtmp.ws.lycam.tv/lycam/dev-882bee90-4cde-11e6-925f-453e8ead406d","thumbnailUrl":"http://resource-aws.lycam.tv/images/thumb.jpg"}}
     * subscribeCount : 1
     * id : 578c969edddfbfaa256c67c2
     */

    private UserBean user;
    /**
     * user : {"username":"18508242165","displayName":"小李子","roomId":"000013","uuid":"a8232cb0-4cc3-11e6-b559-9f7e28db045c","role":"teacher","authentication":true,"historyCount":0,"historyList":{},"subcribeCount":0,"subcribeList":{},"education":[{"time":"2009--2013","school":"Northeast Normal University"}],"workExperience":[{"time":"2011-2015","content":"asdfkasdfasdf"},{"time":"2011-2016","content":"gasdfasdfadgs"}],"IDNumber":"5107251999111111","major":"Physics","sex":0,"description":"","gender":false,"level":1,"avatarUrl":"","followersCount":1,"friendsCount":0,"statusCount":0,"balance":0,"lycamBeans":0,"contribution":0,"experience":0,"blacklist":{},"type":"default","createdAt":"2016-07-18T08:43:10.662Z","phone":"18508242165","subscribeList":{"dev-882bee90-4cde-11e6-925f-453e8ead406d":{"user":"578c969edddfbfaa256c67c2","streamId":"dev-882bee90-4cde-11e6-925f-453e8ead406d","roomId":"000013","title":null,"description":null,"streamUrl":"rtmp://rtmp.ws.lycam.tv/lycam/dev-882bee90-4cde-11e6-925f-453e8ead406d","thumbnailUrl":"http://resource-aws.lycam.tv/images/thumb.jpg"}},"subscribeCount":1,"id":"578c969edddfbfaa256c67c2"}
     * startTime : 2016-11-11
     * duration : 1h
     * ifCharge : true
     * charge : 11000
     * streamId : dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69
     * status : ready
     * streamUrl : rtmp://rtmp.ws.lycam.tv/lycam/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69
     * audioStreamUrl : rtmp://rtmp.ws.lycam.tv/lycam/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69_audio
     * streamUrls : [{"type":"RTMP","url":"rtmp://rtmp.ws.lycam.tv/lycam/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69","bitrate":1500},{"type":"HLS","url":"http://resource-s3.lycam.tv/apps/XPQKR06444/a8232cb0-4cc3-11e6-b559-9f7e28db045c/streams/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69/hls/index.m3u8","bitrate":1500},{"type":"RTMP/audio","url":"rtmp://rtmp.ws.lycam.tv/lycam/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69_audio","bitrate":128}]
     * thumbnailUrl : http://resource-aws.lycam.tv/images/thumb.jpg
     * resourceUrl : http://api.appserver.lycam.tv:1337/streams/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69/player
     * uploadUrl : rtmp://rtmp-push-aws-dev.ws.lycam.tv/lycam?token=sronFwoLNcWYark2XlOkMbjViF7lgMN4vOQLVNU5kUsikb0BO4qNMoV0IkcKvmK2
     * chatUrl : mqtt.lycam.tv
     * chatChannel : dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69
     * maxBitrate : 1500
     * maxKeyframe : 150
     * useAdaptiveBitrate : true
     * timeStarted : 2016-07-20T03:50:00.000Z
     * privacy : false
     * createdAt : 2016-07-20T03:50:01.028Z
     * updatedAt : 2016-07-20T05:00:55.120Z
     * roomId : 000013
     * reservationCount : 1
     * reservationList : {"578ccf706946349f2fcc599c":{"id":"578ccf706946349f2fcc599c","uuid":"864995d0-4ce5-11e6-925f-453e8ead406d","username":"13684069908","displayName":"哎哟美女哦gg","avatarUrl":""}}
     * courseware : {}
     * deleted : false
     * audiences : 0
     * chatMessageCount : 3
     * popularityRise : 0
     * likeCount : 0
     * barrageCount : 10
     * watchedCount : 0
     * id : 578ef4e9d85fd86b0917801f
     * timeStamp : 1468986601
     * chatToken : Q0s3QAEf3e7gtHgD6DgLEDmIjAemDIthptjE4060fVSqyk0HSYcDYFaOKAP58FDG
     * follow : true
     * followed : false
     */

    private String startTime;
    private String duration;
    private boolean ifCharge;
    private String charge;
    private String streamId;
    private String status;
    private String streamUrl;
    private String audioStreamUrl;
    private String thumbnailUrl;
    private String resourceUrl;
    private String uploadUrl;
    private String chatUrl;
    private String chatChannel;
    private int maxBitrate;
    private int maxKeyframe;
    private boolean useAdaptiveBitrate;
    private String timeStarted;
    private boolean privacy;
    private String createdAt;
    private String updatedAt;
    private String roomId;
    private int reservationCount;
    private boolean deleted;
    private int audiences;
    private int chatMessageCount;
    private int popularityRise;
    private int likeCount;
    private int barrageCount;
    private int watchedCount;
    private String id;
    private int timeStamp;
    private String chatToken;
    private boolean follow;
    private boolean followed;
    private String description;
    private boolean isSubscribe;
    private String title;

    public StreamInfoModel(){

    }

    protected StreamInfoModel(Parcel in) {
        user = in.readParcelable(UserBean.class.getClassLoader());
        startTime = in.readString();
        duration = in.readString();
        ifCharge = in.readByte() != 0;
        charge = in.readString();
        streamId = in.readString();
        status = in.readString();
        streamUrl = in.readString();
        audioStreamUrl = in.readString();
        thumbnailUrl = in.readString();
        resourceUrl = in.readString();
        uploadUrl = in.readString();
        chatUrl = in.readString();
        chatChannel = in.readString();
        maxBitrate = in.readInt();
        maxKeyframe = in.readInt();
        useAdaptiveBitrate = in.readByte() != 0;
        timeStarted = in.readString();
        privacy = in.readByte() != 0;
        createdAt = in.readString();
        updatedAt = in.readString();
        roomId = in.readString();
        reservationCount = in.readInt();
        deleted = in.readByte() != 0;
        audiences = in.readInt();
        chatMessageCount = in.readInt();
        popularityRise = in.readInt();
        likeCount = in.readInt();
        barrageCount = in.readInt();
        watchedCount = in.readInt();
        id = in.readString();
        timeStamp = in.readInt();
        chatToken = in.readString();
        follow = in.readByte() != 0;
        followed = in.readByte() != 0;
        description = in.readString();
        isSubscribe = in.readByte() != 0;
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(startTime);
        dest.writeString(duration);
        dest.writeByte((byte) (ifCharge ? 1 : 0));
        dest.writeString(charge);
        dest.writeString(streamId);
        dest.writeString(status);
        dest.writeString(streamUrl);
        dest.writeString(audioStreamUrl);
        dest.writeString(thumbnailUrl);
        dest.writeString(resourceUrl);
        dest.writeString(uploadUrl);
        dest.writeString(chatUrl);
        dest.writeString(chatChannel);
        dest.writeInt(maxBitrate);
        dest.writeInt(maxKeyframe);
        dest.writeByte((byte) (useAdaptiveBitrate ? 1 : 0));
        dest.writeString(timeStarted);
        dest.writeByte((byte) (privacy ? 1 : 0));
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(roomId);
        dest.writeInt(reservationCount);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeInt(audiences);
        dest.writeInt(chatMessageCount);
        dest.writeInt(popularityRise);
        dest.writeInt(likeCount);
        dest.writeInt(barrageCount);
        dest.writeInt(watchedCount);
        dest.writeString(id);
        dest.writeInt(timeStamp);
        dest.writeString(chatToken);
        dest.writeByte((byte) (follow ? 1 : 0));
        dest.writeByte((byte) (followed ? 1 : 0));
        dest.writeString(description);
        dest.writeByte((byte) (isSubscribe ? 1 : 0));
        dest.writeString(title);
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    @Override

    public int describeContents() {
        return 0;
    }

    public static final Creator<StreamInfoModel> CREATOR = new Creator<StreamInfoModel>() {
        @Override
        public StreamInfoModel createFromParcel(Parcel in) {
            return new StreamInfoModel(in);
        }

        @Override
        public StreamInfoModel[] newArray(int size) {
            return new StreamInfoModel[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * type : RTMP
     * url : rtmp://rtmp.ws.lycam.tv/lycam/dev-08a0f581-4e2d-11e6-8543-b7bfcafcbf69
     * bitrate : 1500
     */

    private List<StreamUrlsBean> streamUrls;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isIfCharge() {
        return ifCharge;
    }

    public void setIfCharge(boolean ifCharge) {
        this.ifCharge = ifCharge;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getAudioStreamUrl() {
        return audioStreamUrl;
    }

    public void setAudioStreamUrl(String audioStreamUrl) {
        this.audioStreamUrl = audioStreamUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
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

    public int getMaxBitrate() {
        return maxBitrate;
    }

    public void setMaxBitrate(int maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    public int getMaxKeyframe() {
        return maxKeyframe;
    }

    public void setMaxKeyframe(int maxKeyframe) {
        this.maxKeyframe = maxKeyframe;
    }

    public boolean isUseAdaptiveBitrate() {
        return useAdaptiveBitrate;
    }

    public void setUseAdaptiveBitrate(boolean useAdaptiveBitrate) {
        this.useAdaptiveBitrate = useAdaptiveBitrate;
    }

    public String getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(String timeStarted) {
        this.timeStarted = timeStarted;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getAudiences() {
        return audiences;
    }

    public void setAudiences(int audiences) {
        this.audiences = audiences;
    }

    public int getChatMessageCount() {
        return chatMessageCount;
    }

    public void setChatMessageCount(int chatMessageCount) {
        this.chatMessageCount = chatMessageCount;
    }

    public int getPopularityRise() {
        return popularityRise;
    }

    public void setPopularityRise(int popularityRise) {
        this.popularityRise = popularityRise;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getBarrageCount() {
        return barrageCount;
    }

    public void setBarrageCount(int barrageCount) {
        this.barrageCount = barrageCount;
    }

    public int getWatchedCount() {
        return watchedCount;
    }

    public void setWatchedCount(int watchedCount) {
        this.watchedCount = watchedCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public List<StreamUrlsBean> getStreamUrls() {
        return streamUrls;
    }

    public void setStreamUrls(List<StreamUrlsBean> streamUrls) {
        this.streamUrls = streamUrls;
    }

    public static class UserBean implements Parcelable {
        private String username;
        private String displayName;
        private String roomId;
        private String uuid;
        private String role;
        private boolean authentication;
        private int historyCount;
        private int subcribeCount;
        private String IDNumber;
        private String major;
        private int sex;
        private String description;
        private boolean gender;
        private int level;
        private String avatarUrl;
        private int followersCount;
        private int friendsCount;
        private int statusCount;
        private int balance;
        private int lycamBeans;
        private int contribution;
        private int experience;
        private String type;
        private String createdAt;
        private String phone;
        private int subscribeCount;
        private String id;

        public UserBean(){

        }

        protected UserBean(Parcel in) {
            username = in.readString();
            displayName = in.readString();
            roomId = in.readString();
            uuid = in.readString();
            role = in.readString();
            authentication = in.readByte() != 0;
            historyCount = in.readInt();
            subcribeCount = in.readInt();
            IDNumber = in.readString();
            major = in.readString();
            sex = in.readInt();
            description = in.readString();
            gender = in.readByte() != 0;
            level = in.readInt();
            avatarUrl = in.readString();
            followersCount = in.readInt();
            friendsCount = in.readInt();
            statusCount = in.readInt();
            balance = in.readInt();
            lycamBeans = in.readInt();
            contribution = in.readInt();
            experience = in.readInt();
            type = in.readString();
            createdAt = in.readString();
            phone = in.readString();
            subscribeCount = in.readInt();
            id = in.readString();
        }

        public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel in) {
                return new UserBean(in);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean isAuthentication() {
            return authentication;
        }

        public void setAuthentication(boolean authentication) {
            this.authentication = authentication;
        }

        public int getHistoryCount() {
            return historyCount;
        }

        public void setHistoryCount(int historyCount) {
            this.historyCount = historyCount;
        }

        public int getSubcribeCount() {
            return subcribeCount;
        }

        public void setSubcribeCount(int subcribeCount) {
            this.subcribeCount = subcribeCount;
        }

        public String getIDNumber() {
            return IDNumber;
        }

        public void setIDNumber(String IDNumber) {
            this.IDNumber = IDNumber;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(int followersCount) {
            this.followersCount = followersCount;
        }

        public int getFriendsCount() {
            return friendsCount;
        }

        public void setFriendsCount(int friendsCount) {
            this.friendsCount = friendsCount;
        }

        public int getStatusCount() {
            return statusCount;
        }

        public void setStatusCount(int statusCount) {
            this.statusCount = statusCount;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public int getLycamBeans() {
            return lycamBeans;
        }

        public void setLycamBeans(int lycamBeans) {
            this.lycamBeans = lycamBeans;
        }

        public int getContribution() {
            return contribution;
        }

        public void setContribution(int contribution) {
            this.contribution = contribution;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSubscribeCount() {
            return subscribeCount;
        }

        public void setSubscribeCount(int subscribeCount) {
            this.subscribeCount = subscribeCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(username);
            parcel.writeString(displayName);
            parcel.writeString(roomId);
            parcel.writeString(uuid);
            parcel.writeString(role);
            parcel.writeByte((byte) (authentication ? 1 : 0));
            parcel.writeInt(historyCount);
            parcel.writeInt(subcribeCount);
            parcel.writeString(IDNumber);
            parcel.writeString(major);
            parcel.writeInt(sex);
            parcel.writeString(description);
            parcel.writeByte((byte) (gender ? 1 : 0));
            parcel.writeInt(level);
            parcel.writeString(avatarUrl);
            parcel.writeInt(followersCount);
            parcel.writeInt(friendsCount);
            parcel.writeInt(statusCount);
            parcel.writeInt(balance);
            parcel.writeInt(lycamBeans);
            parcel.writeInt(contribution);
            parcel.writeInt(experience);
            parcel.writeString(type);
            parcel.writeString(createdAt);
            parcel.writeString(phone);
            parcel.writeInt(subscribeCount);
            parcel.writeString(id);
        }
    }

    public static class StreamUrlsBean {
        private String type;
        private String url;
        private int bitrate;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }
    }

    /*
    private String streamId;
    private TimelineModel.Items.User user;
    private String status;
    private String title;
    private String description;
    private int audiences;
    private int popularityRise;
    private int likeCount;
    private int chatMessageCount;
    private String resourceUrl;
    private int watchedCount;

    public int getWatchedCount() {
        return watchedCount;
    }

    public void setWatchedCount(int watchedCount) {
        this.watchedCount = watchedCount;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public TimelineModel.Items.User getUser() {
        return user;
    }

    public void setUser(TimelineModel.Items.User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAudiences() {
        return audiences;
    }

    public void setAudiences(int audiences) {
        this.audiences = audiences;
    }

    public int getPopularityRise() {
        return popularityRise;
    }

    public void setPopularityRise(int popularityRise) {
        this.popularityRise = popularityRise;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getChatMessageCount() {
        return chatMessageCount;
    }

    public void setChatMessageCount(int chatMessageCount) {
        this.chatMessageCount = chatMessageCount;
    }*/





}
