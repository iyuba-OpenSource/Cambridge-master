package com.iyuba.camstory.lycam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频时间线
 */
public class TimelineModel implements Parcelable {

    private int totalItems;
    private int resultsPerPage;
    private int pageNumber;
    private boolean nextPageAvailable;
    private List<Items> items;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
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

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public static class Items implements Parcelable {
        private User user;
        private String streamId;
        private String status;
        private String thumbnailUrl;
        private String timeStarted;
        private boolean privacy;
        private int audiences;
        private int chatMessageCount;
        private int popularityRise;
        private int likeCount;
        private int barrageCount;
        private int watchedCount;
        private String id;
        private String description;
        private String city;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        private List<StreamUrls> streamUrls;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
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
        public List<StreamUrls> getStreamUrls() {
            return streamUrls;
        }

        public void setStreamUrls(List<StreamUrls> streamUrls) {
            this.streamUrls = streamUrls;
        }

        public static class User implements Parcelable {
            private String displayName;
            private String avatarUrl;
            private boolean gender;
            private String type;
            private int level;
            private String id;
            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public boolean isGender() {
                return gender;
            }

            public void setGender(boolean gender) {
                this.gender = gender;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
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
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.displayName);
                dest.writeString(this.avatarUrl);
                dest.writeByte(this.gender ? (byte) 1 : (byte) 0);
                dest.writeString(this.type);
                dest.writeInt(this.level);
                dest.writeString(this.id);
            }

            public User() {
            }

            protected User(Parcel in) {
                this.displayName = in.readString();
                this.avatarUrl = in.readString();
                this.gender = in.readByte() != 0;
                this.type = in.readString();
                this.level = in.readInt();
                this.id = in.readString();
            }

            public static final Creator<User> CREATOR = new Creator<User>() {
                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };
        }

        public static class StreamUrls implements Parcelable {
            private String type;
            private String url;

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.type);
                dest.writeString(this.url);
            }

            public StreamUrls() {
            }

            protected StreamUrls(Parcel in) {
                this.type = in.readString();
                this.url = in.readString();
            }

            public static final Creator<StreamUrls> CREATOR = new Creator<StreamUrls>() {
                @Override
                public StreamUrls createFromParcel(Parcel source) {
                    return new StreamUrls(source);
                }

                @Override
                public StreamUrls[] newArray(int size) {
                    return new StreamUrls[size];
                }
            };
        }

        public Items() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.user, flags);
            dest.writeString(this.streamId);
            dest.writeString(this.status);
            dest.writeString(this.thumbnailUrl);
            dest.writeString(this.timeStarted);
            dest.writeByte(this.privacy ? (byte) 1 : (byte) 0);
            dest.writeInt(this.audiences);
            dest.writeInt(this.chatMessageCount);
            dest.writeInt(this.popularityRise);
            dest.writeInt(this.likeCount);
            dest.writeInt(this.barrageCount);
            dest.writeInt(this.watchedCount);
            dest.writeString(this.id);
            dest.writeString(this.description);
            dest.writeString(this.city);
            dest.writeTypedList(this.streamUrls);
            dest.writeString(this.title);
        }

        protected Items(Parcel in) {
            this.user = in.readParcelable(User.class.getClassLoader());
            this.streamId = in.readString();
            this.status = in.readString();
            this.thumbnailUrl = in.readString();
            this.timeStarted = in.readString();
            this.privacy = in.readByte() != 0;
            this.audiences = in.readInt();
            this.chatMessageCount = in.readInt();
            this.popularityRise = in.readInt();
            this.likeCount = in.readInt();
            this.barrageCount = in.readInt();
            this.watchedCount = in.readInt();
            this.id = in.readString();
            this.description = in.readString();
            this.city = in.readString();
            this.streamUrls = in.createTypedArrayList(StreamUrls.CREATOR);
            this.title = in.readString();
        }

        public static final Creator<Items> CREATOR = new Creator<Items>() {
            @Override
            public Items createFromParcel(Parcel source) {
                return new Items(source);
            }

            @Override
            public Items[] newArray(int size) {
                return new Items[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalItems);
        dest.writeInt(this.resultsPerPage);
        dest.writeInt(this.pageNumber);
        dest.writeByte(this.nextPageAvailable ? (byte) 1 : (byte) 0);
        dest.writeList(this.items);
    }

    public TimelineModel() {
    }

    protected TimelineModel(Parcel in) {
        this.totalItems = in.readInt();
        this.resultsPerPage = in.readInt();
        this.pageNumber = in.readInt();
        this.nextPageAvailable = in.readByte() != 0;
        this.items = new ArrayList<Items>();
        in.readList(this.items, Items.class.getClassLoader());
    }

    public static final Creator<TimelineModel> CREATOR = new Creator<TimelineModel>() {
        @Override
        public TimelineModel createFromParcel(Parcel source) {
            return new TimelineModel(source);
        }

        @Override
        public TimelineModel[] newArray(int size) {
            return new TimelineModel[size];
        }
    };
}