package com.iyuba.camstory.lycam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lycamandroid on 16/6/3.
 * 视频信息
 */
public class StreamsModel implements Parcelable {

    private int totalItems;
    private int resultsPerPage;
    private int pageNumber;
    private boolean nextPageAvailable;
    private List<Stream> items;

    public StreamsModel() {
    }

    public StreamsModel(int totalItems, List<Stream> items, boolean nextPageAvailable, int pageNumber, int resultsPerPage) {
        this.totalItems = totalItems;
        this.items = items;
        this.nextPageAvailable = nextPageAvailable;
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
    }

    protected StreamsModel(Parcel in) {
        totalItems = in.readInt();
        resultsPerPage = in.readInt();
        pageNumber = in.readInt();
        nextPageAvailable = in.readByte() != 0;
        items = in.createTypedArrayList(Stream.CREATOR);
    }

    public static final Creator<StreamsModel> CREATOR = new Creator<StreamsModel>() {
        @Override
        public StreamsModel createFromParcel(Parcel in) {
            return new StreamsModel(in);
        }

        @Override
        public StreamsModel[] newArray(int size) {
            return new StreamsModel[size];
        }
    };

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isNextPageAvailable() {
        return nextPageAvailable;
    }

    public void setNextPageAvailable(boolean nextPageAvailable) {
        this.nextPageAvailable = nextPageAvailable;
    }

    public List<Stream> getItems() {
        return items;
    }

    public void setItems(List<Stream> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totalItems);
        parcel.writeInt(resultsPerPage);
        parcel.writeInt(pageNumber);
        parcel.writeByte((byte) (nextPageAvailable ? 1 : 0));
        parcel.writeTypedList(items);
    }

    public static class Stream implements Parcelable {
        private String user;
        private String streamId;
        private String status;
        private String streamUrl;
        private String thumbnailUrl;
        private String timeStarted;
        private boolean privacy;
        private String description;
        private String timeFinished;
        private String id;
        private int audiences;
        //消息总数
        private int chatMessageCount;
        //点赞总数
        private int likeCount;
        //弹幕次数
        private int barrageCount;
        //观看总数
        private int watchedCount;
        //时间戳
        private long timeStamp;
        private boolean isSubscribe;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getChatMessageCount() {
            return chatMessageCount;
        }

        public void setChatMessageCount(int chatMessageCount) {
            this.chatMessageCount = chatMessageCount;
        }

        public boolean isSubscribe() {
            return isSubscribe;
        }

        public void setIsSubscribe(boolean subscribe) {
            isSubscribe = subscribe;
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

        public Stream() {
        }

        public Stream(String streamId){
            this.streamId = streamId;
        }

        public int getAudiences() {
            return audiences;
        }

        public void setAudiences(int audiences) {
            this.audiences = audiences;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
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

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTimeFinished() {
            return timeFinished;
        }

        public void setTimeFinished(String timeFinished) {
            this.timeFinished = timeFinished;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        protected Stream(Parcel in) {
            user = in.readString();
            streamId = in.readString();
            status = in.readString();
            streamUrl = in.readString();
            thumbnailUrl = in.readString();
            timeStarted = in.readString();
            privacy = in.readByte() != 0;
            description = in.readString();
            timeFinished = in.readString();
            id = in.readString();
            audiences = in.readInt();
            chatMessageCount=in.readInt();
            likeCount=in.readInt();
            barrageCount=in.readInt();
            watchedCount=in.readInt();
            timeStamp=in.readLong();
            isSubscribe = in.readByte() != 0;
            title = in.readString();
        }

        public static final Creator<Stream> CREATOR = new Creator<Stream>() {
            @Override
            public Stream createFromParcel(Parcel in) {
                return new Stream(in);
            }

            @Override
            public Stream[] newArray(int size) {
                return new Stream[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user);
            dest.writeString(streamId);
            dest.writeString(status);
            dest.writeString(streamUrl);
            dest.writeString(thumbnailUrl);
            dest.writeString(timeStarted);
            dest.writeByte((byte) (privacy ? 1 : 0));
            dest.writeString(description);
            dest.writeString(timeFinished);
            dest.writeString(id);
            dest.writeInt(audiences);
            dest.writeInt(chatMessageCount);
            dest.writeInt(likeCount);
            dest.writeInt(barrageCount);
            dest.writeInt(watchedCount);
            dest.writeLong(timeStamp);
            dest.writeByte((byte) (isSubscribe ? 1 : 0));
            dest.writeString(title);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null){
                return false;
            }
            if(obj instanceof Stream){
                return this.streamId.equals(((Stream)obj).getStreamId());
            }else{
                return false;
            }
        }
    }
}
