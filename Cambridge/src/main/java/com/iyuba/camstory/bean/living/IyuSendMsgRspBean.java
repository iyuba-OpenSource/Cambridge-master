package com.iyuba.camstory.bean.living;

/**
 * 作者：renzhy on 16/8/12 16:14
 * 邮箱：renzhongyigoo@gmail.com
 */
public class IyuSendMsgRspBean {

	/**
	 * messageId : 8874367HU1R4
	 * metaInfo : {"content":"hello","tid":"1212","uid":"1603656","avatarUrl":"http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=1603656&size=middle","nickname":"missing19911231"}
	 */

	private String messageId;
	/**
	 * content : hello
	 * tid : 1212
	 * uid : 1603656
	 * avatarUrl : http://api."+Constant.IYBHttpHead2+"/v2/api.iyuba?protocol=10005&uid=1603656&size=middle
	 * nickname : missing19911231
	 */

	private MetaInfoBean metaInfo;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public MetaInfoBean getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(MetaInfoBean metaInfo) {
		this.metaInfo = metaInfo;
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
