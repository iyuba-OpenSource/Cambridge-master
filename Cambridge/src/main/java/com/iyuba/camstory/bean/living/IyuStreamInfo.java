package com.iyuba.camstory.bean.living;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者：renzhy on 16/8/11 18:58
 * 邮箱：renzhongyigoo@gmail.com
 */
public class IyuStreamInfo implements Parcelable{

	/**
	 * displayName : Diane258
	 * avatarUrl : http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=big&uid=1039140
	 * role : teacher
	 * authentication : true
	 * roomId : 000001
	 */

	private UserBean user;
	/**
	 * user : {"displayName":"Diane258","avatarUrl":"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&size=big&uid=1039140","role":"teacher","authentication":"true","roomId":"000001"}
	 * startTime : 2016-08-11 14:12:00
	 * duration : 0.5h
	 * title : FIRST TEST
	 * oid : 1
	 * pid : 1
	 * cid : 1
	 * tid : 1212
	 * status : ready
	 * streamUrl : rtmp://rtmp.ws.lycam.tv/lycam/dev-6b0a6df1-5f8a-11e6-8d31-752fef8a4b37
	 * streamUrls : [{"type":"RTMP","url":"rtmp://rtmp.ws.lycam.tv/lycam/dev-6b0a6df1-5f8a-11e6-8d31-752fef8a4b37","bitrate":1500},{"type":"HLS","url":"http://hls.ws.lycam.tv/lycam/dev-6b0a6df1-5f8a-11e6-8d31-752fef8a4b37/playlist.m3u8","bitrate":1500},{"type":"RTMP/audio","url":"rtmp://rtmp.ws.lycam.tv/lycam/dev-6b0a6df1-5f8a-11e6-8d31-752fef8a4b37_audio","bitrate":128}]
	 * thumbnailUrl : http://resource-aws.lycam.tv/images/thumb.jpg
	 * resourceUrl : http://sxs-app-server-dev-382060582.cn-north-1.elb.amazonaws.com.cn//streams/undefined/player
	 * timeStarted : 2016-08-11T06:11:19.000Z
	 * roomId : 000001
	 * ifCharge : false
	 * charge : 0.0
	 * chatMessageCount : 0
	 * likeCount : 0
	 * watchedCount : 0
	 */

	private String startTime;
	private String duration;
	private String title;
	private String description;
	private int oid;
	private int pid;
	private int cid;
	private String tid;
	private String streamId;
	private String status;
	private String streamUrl;
	private String thumbnailUrl;
	private String resourceUrl;
	private String chatUrl;
	private String chatChannel;
	private String timeStarted;
	private String roomId;
	private boolean ifCharge;
	private String charge;
	private int chatMessageCount;
	private int likeCount;
	private int watchedCount;
	private String timeFinished;
	private String chatToken;
	private String topic;

	/**
	 * type : RTMP
	 * url : rtmp://rtmp.ws.lycam.tv/lycam/dev-6b0a6df1-5f8a-11e6-8d31-752fef8a4b37
	 * bitrate : 1500
	 */

	private List<StreamUrlsBean> streamUrls;

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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
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

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
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

	public String getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(String timeStarted) {
		this.timeStarted = timeStarted;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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

	public int getChatMessageCount() {
		return chatMessageCount;
	}

	public void setChatMessageCount(int chatMessageCount) {
		this.chatMessageCount = chatMessageCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getWatchedCount() {
		return watchedCount;
	}

	public void setWatchedCount(int watchedCount) {
		this.watchedCount = watchedCount;
	}

	public String getTimeFinished() {
		return timeFinished;
	}

	public void setTimeFinished(String timeFinished) {
		this.timeFinished = timeFinished;
	}

	public String getChatToken() {
		return chatToken;
	}

	public void setChatToken(String chatToken) {
		this.chatToken = chatToken;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<StreamUrlsBean> getStreamUrls() {
		return streamUrls;
	}

	public void setStreamUrls(List<StreamUrlsBean> streamUrls) {
		this.streamUrls = streamUrls;
	}

	public IyuStreamInfo(){

	}

	protected IyuStreamInfo(Parcel in){
		user = in.readParcelable(UserBean.class.getClassLoader());
		startTime = in.readString();
		duration = in.readString();
		title = in.readString();
		description = in.readString();
		oid = in.readInt();
		pid = in.readInt();
		cid = in.readInt();
		tid = in.readString();
		streamId = in.readString();
		status = in.readString();
		streamUrl = in.readString();
		thumbnailUrl = in.readString();
		resourceUrl = in.readString();
		chatUrl = in.readString();
		chatChannel = in.readString();
		timeStarted = in.readString();
		roomId = in.readString();
		ifCharge = in.readByte() != 0;
        charge  = in.readString();
		chatMessageCount = in.readInt();
		likeCount = in.readInt();
		watchedCount = in.readInt();
		timeFinished = in.readString();
		chatToken = in.readString();
		topic = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeParcelable(user,i);
		parcel.writeString(startTime);
		parcel.writeString(duration);
		parcel.writeString(title);
		parcel.writeString(description);
		parcel.writeInt(oid);
		parcel.writeInt(pid);
		parcel.writeInt(cid);
		parcel.writeString(tid);
		parcel.writeString(streamId);
		parcel.writeString(status);
		parcel.writeString(streamUrl);
		parcel.writeString(thumbnailUrl);
		parcel.writeString(resourceUrl);
		parcel.writeString(chatUrl);
		parcel.writeString(chatChannel);
		parcel.writeString(timeStarted);
		parcel.writeString(roomId);
		parcel.writeByte((byte)(ifCharge ? 1 : 0));
		parcel.writeString(charge);
		parcel.writeInt(chatMessageCount);
		parcel.writeInt(likeCount);
		parcel.writeInt(watchedCount);
		parcel.writeString(timeFinished);
		parcel.writeString(chatToken);
		parcel.writeString(topic);
	}

	public static final Creator<IyuStreamInfo> CREATOR = new Creator<IyuStreamInfo>() {
		@Override
		public IyuStreamInfo createFromParcel(Parcel parcel) {
			return new IyuStreamInfo(parcel);
		}

		@Override
		public IyuStreamInfo[] newArray(int i) {
			return new IyuStreamInfo[0];
		}
	};

	public static class UserBean implements Parcelable{
		private String displayName;
		private String avatarUrl;
		private String role;
		private String authentication;
		private String roomId;

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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getAuthentication() {
			return authentication;
		}

		public void setAuthentication(String authentication) {
			this.authentication = authentication;
		}

		public String getRoomId() {
			return roomId;
		}

		public void setRoomId(String roomId) {
			this.roomId = roomId;
		}

		public UserBean(){

		}

		protected UserBean(Parcel in){
			displayName = in.readString();
			avatarUrl = in.readString();
			role = in.readString();
			authentication = in.readString();
			roomId = in.readString();
		}

		public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
			@Override
			public UserBean createFromParcel(Parcel parcel) {
				return new UserBean(parcel);
			}

			@Override
			public UserBean[] newArray(int i) {
				return new UserBean[0];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int i) {
			parcel.writeString(displayName);
			parcel.writeString(avatarUrl);
			parcel.writeString(role);
			parcel.writeString(authentication);
			parcel.writeString(roomId);
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
}
