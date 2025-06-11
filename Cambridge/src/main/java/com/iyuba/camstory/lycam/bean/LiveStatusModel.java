package com.iyuba.camstory.lycam.bean;

/**
 * 作者：renzhy on 16/9/14 16:57
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveStatusModel {

	/**
	 * message :
	 * status : stream.pause
	 * stream : {"anchorStatus":"pause","audiences":0,"audioStreamUrl":"rtmp://rtmp.ws.lycam.tv/lycam/d48b9d90-7636-11e6-9676-47ef790cbfad_audio","barrageCount":0,"bucketName":"hls-live","charge":"0.0","chatChannel":"room/d48b9d90-7636-11e6-9676-47ef790cbfad","chatMessageCount":0,"chatUrl":"mqtt-prod.lycam.tv","cid":4044,"createdAt":"2016-09-09T02:40:54.774Z","deleted":0,"description":" ","duration":"0.5h","endTime":1473403558000,"endpoint":"http://oss-cn-shenzhen.aliyuncs.com","id":"57d22136cbace90e00012e42","ifCharge":0,"likeCount":0,"maxBitrate":1000,"maxKeyframe":150,"oid":61,"pid":813,"popularityRise":0,"prefix":"apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/","privacy":0,"protocol":"oss","resourceUrl":"http://sxs-app-server-dev-382060582.cn-north-1.elb.amazonaws.com.cn//streams/d48b9d90-7636-11e6-9676-47ef790cbfad/player","roomId":"000003","sort":3,"start":1473401758000,"startTime":"2016-09-09 14:15:58","status":"over","streamId":"d48b9d90-7636-11e6-9676-47ef790cbfad","streamResourceRoot":"http://resource-s3.lycam.tv","streamType":"RTMP","streamUrl":"http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8","streamUrls":{"type":"HLS","url":"http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8"},"thumbnailUrl":"http://sbkt.oss-cn-shenzhen.aliyuncs.com/iyuba/thumbnails/d48b9d90-7636-11e6-9676-47ef790cbfad/1473388854924.jpg","tid":8040,"timeFinished":"2016-09-14T06:30:03.000Z","timeStarted":"2016-09-14T06:28:50.000Z","title":"BBC","updatedAt":"2016-09-14T06:30:03.831Z","uploadUrl":"rtmp://rtmp-push-aws.ws.lycam.tv/lycam?token=4N7IaGU1HGRrYlvLXAOOMDjvKPpvOfIMEWlsfmQjw01o4uVXMj26N1xWYNVE9UOF","useAdaptiveBitrate":1,"user":"57bfa1c0cceaf40f007ed764","watchedCount":0}
	 */

	private ContentBean content;
	/**
	 * content : {"message":"","status":"stream.pause","stream":{"anchorStatus":"pause","audiences":0,"audioStreamUrl":"rtmp://rtmp.ws.lycam.tv/lycam/d48b9d90-7636-11e6-9676-47ef790cbfad_audio","barrageCount":0,"bucketName":"hls-live","charge":"0.0","chatChannel":"room/d48b9d90-7636-11e6-9676-47ef790cbfad","chatMessageCount":0,"chatUrl":"mqtt-prod.lycam.tv","cid":4044,"createdAt":"2016-09-09T02:40:54.774Z","deleted":0,"description":" ","duration":"0.5h","endTime":1473403558000,"endpoint":"http://oss-cn-shenzhen.aliyuncs.com","id":"57d22136cbace90e00012e42","ifCharge":0,"likeCount":0,"maxBitrate":1000,"maxKeyframe":150,"oid":61,"pid":813,"popularityRise":0,"prefix":"apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/","privacy":0,"protocol":"oss","resourceUrl":"http://sxs-app-server-dev-382060582.cn-north-1.elb.amazonaws.com.cn//streams/d48b9d90-7636-11e6-9676-47ef790cbfad/player","roomId":"000003","sort":3,"start":1473401758000,"startTime":"2016-09-09 14:15:58","status":"over","streamId":"d48b9d90-7636-11e6-9676-47ef790cbfad","streamResourceRoot":"http://resource-s3.lycam.tv","streamType":"RTMP","streamUrl":"http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8","streamUrls":{"type":"HLS","url":"http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8"},"thumbnailUrl":"http://sbkt.oss-cn-shenzhen.aliyuncs.com/iyuba/thumbnails/d48b9d90-7636-11e6-9676-47ef790cbfad/1473388854924.jpg","tid":8040,"timeFinished":"2016-09-14T06:30:03.000Z","timeStarted":"2016-09-14T06:28:50.000Z","title":"BBC","updatedAt":"2016-09-14T06:30:03.831Z","uploadUrl":"rtmp://rtmp-push-aws.ws.lycam.tv/lycam?token=4N7IaGU1HGRrYlvLXAOOMDjvKPpvOfIMEWlsfmQjw01o4uVXMj26N1xWYNVE9UOF","useAdaptiveBitrate":1,"user":"57bfa1c0cceaf40f007ed764","watchedCount":0}}
	 * type : stream.status
	 */

	private String type;

	public ContentBean getContent() {
		return content;
	}

	public void setContent(ContentBean content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class ContentBean {
		private String message;
		private String status;
		/**
		 * anchorStatus : pause
		 * audiences : 0
		 * audioStreamUrl : rtmp://rtmp.ws.lycam.tv/lycam/d48b9d90-7636-11e6-9676-47ef790cbfad_audio
		 * barrageCount : 0
		 * bucketName : hls-live
		 * charge : 0.0
		 * chatChannel : room/d48b9d90-7636-11e6-9676-47ef790cbfad
		 * chatMessageCount : 0
		 * chatUrl : mqtt-prod.lycam.tv
		 * cid : 4044
		 * createdAt : 2016-09-09T02:40:54.774Z
		 * deleted : 0
		 * description :
		 * duration : 0.5h
		 * endTime : 1473403558000
		 * endpoint : http://oss-cn-shenzhen.aliyuncs.com
		 * id : 57d22136cbace90e00012e42
		 * ifCharge : 0
		 * likeCount : 0
		 * maxBitrate : 1000
		 * maxKeyframe : 150
		 * oid : 61
		 * pid : 813
		 * popularityRise : 0
		 * prefix : apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/
		 * privacy : 0
		 * protocol : oss
		 * resourceUrl : http://sxs-app-server-dev-382060582.cn-north-1.elb.amazonaws.com.cn//streams/d48b9d90-7636-11e6-9676-47ef790cbfad/player
		 * roomId : 000003
		 * sort : 3
		 * start : 1473401758000
		 * startTime : 2016-09-09 14:15:58
		 * status : over
		 * streamId : d48b9d90-7636-11e6-9676-47ef790cbfad
		 * streamResourceRoot : http://resource-s3.lycam.tv
		 * streamType : RTMP
		 * streamUrl : http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8
		 * streamUrls : {"type":"HLS","url":"http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8"}
		 * thumbnailUrl : http://sbkt.oss-cn-shenzhen.aliyuncs.com/iyuba/thumbnails/d48b9d90-7636-11e6-9676-47ef790cbfad/1473388854924.jpg
		 * tid : 8040
		 * timeFinished : 2016-09-14T06:30:03.000Z
		 * timeStarted : 2016-09-14T06:28:50.000Z
		 * title : BBC
		 * updatedAt : 2016-09-14T06:30:03.831Z
		 * uploadUrl : rtmp://rtmp-push-aws.ws.lycam.tv/lycam?token=4N7IaGU1HGRrYlvLXAOOMDjvKPpvOfIMEWlsfmQjw01o4uVXMj26N1xWYNVE9UOF
		 * useAdaptiveBitrate : 1
		 * user : 57bfa1c0cceaf40f007ed764
		 * watchedCount : 0
		 */

		private StreamBean stream;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public StreamBean getStream() {
			return stream;
		}

		public void setStream(StreamBean stream) {
			this.stream = stream;
		}

		public static class StreamBean {
			private String anchorStatus;
			private int audiences;
			private String audioStreamUrl;
			private int barrageCount;
			private String bucketName;
			private String charge;
			private String chatChannel;
			private int chatMessageCount;
			private String chatUrl;
			private int cid;
			private String createdAt;
			private int deleted;
			private String description;
			private String duration;
			private long endTime;
			private String endpoint;
			private String id;
			private int ifCharge;
			private int likeCount;
			private int maxBitrate;
			private int maxKeyframe;
			private int oid;
			private int pid;
			private int popularityRise;
			private String prefix;
			private int privacy;
			private String protocol;
			private String resourceUrl;
			private String roomId;
			private int sort;
			private long start;
			private String startTime;
			private String status;
			private String streamId;
			private String streamResourceRoot;
			private String streamType;
			private String streamUrl;
			/**
			 * type : HLS
			 * url : http://resource-s3.lycam.tv/apps/OCX3LBO15L/46712040-6b30-11e6-b385-c3e3c79ee73d/streams/d48b9d90-7636-11e6-9676-47ef790cbfad/hls/vod.m3u8
			 */

			private StreamUrlsBean streamUrls;
			private String thumbnailUrl;
			private int tid;
			private String timeFinished;
			private String timeStarted;
			private String title;
			private String updatedAt;
			private String uploadUrl;
			private int useAdaptiveBitrate;
			private String user;
			private int watchedCount;

			public String getAnchorStatus() {
				return anchorStatus;
			}

			public void setAnchorStatus(String anchorStatus) {
				this.anchorStatus = anchorStatus;
			}

			public int getAudiences() {
				return audiences;
			}

			public void setAudiences(int audiences) {
				this.audiences = audiences;
			}

			public String getAudioStreamUrl() {
				return audioStreamUrl;
			}

			public void setAudioStreamUrl(String audioStreamUrl) {
				this.audioStreamUrl = audioStreamUrl;
			}

			public int getBarrageCount() {
				return barrageCount;
			}

			public void setBarrageCount(int barrageCount) {
				this.barrageCount = barrageCount;
			}

			public String getBucketName() {
				return bucketName;
			}

			public void setBucketName(String bucketName) {
				this.bucketName = bucketName;
			}

			public String getCharge() {
				return charge;
			}

			public void setCharge(String charge) {
				this.charge = charge;
			}

			public String getChatChannel() {
				return chatChannel;
			}

			public void setChatChannel(String chatChannel) {
				this.chatChannel = chatChannel;
			}

			public int getChatMessageCount() {
				return chatMessageCount;
			}

			public void setChatMessageCount(int chatMessageCount) {
				this.chatMessageCount = chatMessageCount;
			}

			public String getChatUrl() {
				return chatUrl;
			}

			public void setChatUrl(String chatUrl) {
				this.chatUrl = chatUrl;
			}

			public int getCid() {
				return cid;
			}

			public void setCid(int cid) {
				this.cid = cid;
			}

			public String getCreatedAt() {
				return createdAt;
			}

			public void setCreatedAt(String createdAt) {
				this.createdAt = createdAt;
			}

			public int getDeleted() {
				return deleted;
			}

			public void setDeleted(int deleted) {
				this.deleted = deleted;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getDuration() {
				return duration;
			}

			public void setDuration(String duration) {
				this.duration = duration;
			}

			public long getEndTime() {
				return endTime;
			}

			public void setEndTime(long endTime) {
				this.endTime = endTime;
			}

			public String getEndpoint() {
				return endpoint;
			}

			public void setEndpoint(String endpoint) {
				this.endpoint = endpoint;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public int getIfCharge() {
				return ifCharge;
			}

			public void setIfCharge(int ifCharge) {
				this.ifCharge = ifCharge;
			}

			public int getLikeCount() {
				return likeCount;
			}

			public void setLikeCount(int likeCount) {
				this.likeCount = likeCount;
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

			public int getOid() {
				return oid;
			}

			public void setOid(int oid) {
				this.oid = oid;
			}

			public int getPid() {
				return pid;
			}

			public void setPid(int pid) {
				this.pid = pid;
			}

			public int getPopularityRise() {
				return popularityRise;
			}

			public void setPopularityRise(int popularityRise) {
				this.popularityRise = popularityRise;
			}

			public String getPrefix() {
				return prefix;
			}

			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}

			public int getPrivacy() {
				return privacy;
			}

			public void setPrivacy(int privacy) {
				this.privacy = privacy;
			}

			public String getProtocol() {
				return protocol;
			}

			public void setProtocol(String protocol) {
				this.protocol = protocol;
			}

			public String getResourceUrl() {
				return resourceUrl;
			}

			public void setResourceUrl(String resourceUrl) {
				this.resourceUrl = resourceUrl;
			}

			public String getRoomId() {
				return roomId;
			}

			public void setRoomId(String roomId) {
				this.roomId = roomId;
			}

			public int getSort() {
				return sort;
			}

			public void setSort(int sort) {
				this.sort = sort;
			}

			public long getStart() {
				return start;
			}

			public void setStart(long start) {
				this.start = start;
			}

			public String getStartTime() {
				return startTime;
			}

			public void setStartTime(String startTime) {
				this.startTime = startTime;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getStreamId() {
				return streamId;
			}

			public void setStreamId(String streamId) {
				this.streamId = streamId;
			}

			public String getStreamResourceRoot() {
				return streamResourceRoot;
			}

			public void setStreamResourceRoot(String streamResourceRoot) {
				this.streamResourceRoot = streamResourceRoot;
			}

			public String getStreamType() {
				return streamType;
			}

			public void setStreamType(String streamType) {
				this.streamType = streamType;
			}

			public String getStreamUrl() {
				return streamUrl;
			}

			public void setStreamUrl(String streamUrl) {
				this.streamUrl = streamUrl;
			}

			public StreamUrlsBean getStreamUrls() {
				return streamUrls;
			}

			public void setStreamUrls(StreamUrlsBean streamUrls) {
				this.streamUrls = streamUrls;
			}

			public String getThumbnailUrl() {
				return thumbnailUrl;
			}

			public void setThumbnailUrl(String thumbnailUrl) {
				this.thumbnailUrl = thumbnailUrl;
			}

			public int getTid() {
				return tid;
			}

			public void setTid(int tid) {
				this.tid = tid;
			}

			public String getTimeFinished() {
				return timeFinished;
			}

			public void setTimeFinished(String timeFinished) {
				this.timeFinished = timeFinished;
			}

			public String getTimeStarted() {
				return timeStarted;
			}

			public void setTimeStarted(String timeStarted) {
				this.timeStarted = timeStarted;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getUpdatedAt() {
				return updatedAt;
			}

			public void setUpdatedAt(String updatedAt) {
				this.updatedAt = updatedAt;
			}

			public String getUploadUrl() {
				return uploadUrl;
			}

			public void setUploadUrl(String uploadUrl) {
				this.uploadUrl = uploadUrl;
			}

			public int getUseAdaptiveBitrate() {
				return useAdaptiveBitrate;
			}

			public void setUseAdaptiveBitrate(int useAdaptiveBitrate) {
				this.useAdaptiveBitrate = useAdaptiveBitrate;
			}

			public String getUser() {
				return user;
			}

			public void setUser(String user) {
				this.user = user;
			}

			public int getWatchedCount() {
				return watchedCount;
			}

			public void setWatchedCount(int watchedCount) {
				this.watchedCount = watchedCount;
			}

			public static class StreamUrlsBean {
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
