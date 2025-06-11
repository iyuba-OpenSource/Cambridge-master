package tv.lycam.gift.entity.mqtt;

import java.util.List;

/**
 * Created by su on 16/6/25.
 */
public class StreamStatus {

    private String type;
    private Content content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public static class Content {
        private String status;
        private Stream stream;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Stream getStream() {
            return stream;
        }

        public void setStream(Stream stream) {
            this.stream = stream;
        }

        public static class Stream {
            private String streamId;
            private String user;
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
            private String description;
            private String createdAt;
            private String updatedAt;
            private boolean deleted;
            private int audiences;
            private int chatMessageCount;
            private int popularityRise;
            private int likeCount;
            private String timeFinished;
            private String id;
            private List<StreamUrls> streamUrls;

            public String getStreamId() {
                return streamId;
            }

            public void setStreamId(String streamId) {
                this.streamId = streamId;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public List<StreamUrls> getStreamUrls() {
                return streamUrls;
            }

            public void setStreamUrls(List<StreamUrls> streamUrls) {
                this.streamUrls = streamUrls;
            }

            public static class StreamUrls {
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
            }
        }
    }
}
