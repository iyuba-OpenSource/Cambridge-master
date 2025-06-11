package com.iyuba.camstory.lycam.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liujunhong on 16-5-15.
 * 用户信息
 */

public class UserModel implements Parcelable {
	/**
	 * apiToken : {"value":"sUjawxnzrSzf39MZtPVAbtTdAwFvCVdhKyXauKRWFPXYSPBBmOlRIAPzEYhSWHl9","expiresTime":1466656612908}
	 * token : ahyTKuCZMLRZll3vgoDRV7i2sKoiJ8Npkfut8xOSpWPkVLH16BXLhx2le5iWFzmV
	 */

	private TokenBean token;
	/**
	 * token : {"apiToken":{"value":"sUjawxnzrSzf39MZtPVAbtTdAwFvCVdhKyXauKRWFPXYSPBBmOlRIAPzEYhSWHl9","expiresTime":1466656612908},"token":"ahyTKuCZMLRZll3vgoDRV7i2sKoiJ8Npkfut8xOSpWPkVLH16BXLhx2le5iWFzmV"}
	 * username : html5.
	 * uuid : b8fc9430-36da-11e6-93a9-8b99a3e66d85
	 * sex : 0
	 * description : 哦哦哦
	 * gender : false
	 * level : 1
	 * avatarUrl :
	 * followersCount : 5
	 * friendsCount : 0
	 * statusCount : 0
	 * balance : 0
	 * contribution : 0
	 * popularity : 0
	 * experience : 0
	 * type : default
	 * createdAt : 2016-06-20T11:32:51.663Z
	 * displayName : html5.
	 * birthday : 2013-1-1
	 * id : 5767d4636cb2b60e0066f190
	 */

	private String userName;
	private String username;
	private String uuid;
	private int sex;
	private String description;
	private boolean gender;
	private int level;
	private String avatarUrl;
	private int followersCount;
	private int friendsCount;
	private int statusCount;
	private int balance;
	private int contribution;
	private int popularity;
	private int experience;
	private String type;
	private String createdAt;
	private String displayName;
	private String birthday;
	private String id;

	public TokenBean getToken() {
		return token;
	}

	public void setToken(TokenBean token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static class TokenBean implements Parcelable {
		/**
		 * value : sUjawxnzrSzf39MZtPVAbtTdAwFvCVdhKyXauKRWFPXYSPBBmOlRIAPzEYhSWHl9
		 * expiresTime : 1466656612908
		 */

		private ApiTokenBean apiToken;
		private String token;

		public ApiTokenBean getApiToken() {
			return apiToken;
		}

		public void setApiToken(ApiTokenBean apiToken) {
			this.apiToken = apiToken;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public static class ApiTokenBean implements Parcelable {
			private String value;
			private long expiresTime;

			public ApiTokenBean(String value, Long expiresTime) {
				this.value = value;
				this.expiresTime = expiresTime;
			}

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}

			public long getExpiresTime() {
				return expiresTime;
			}

			public void setExpiresTime(long expiresTime) {
				this.expiresTime = expiresTime;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(this.value);
				dest.writeLong(this.expiresTime);
			}

			public ApiTokenBean() {
			}

			protected ApiTokenBean(Parcel in) {
				this.value = in.readString();
				this.expiresTime = in.readLong();
			}

			public static final Creator<ApiTokenBean> CREATOR = new Creator<ApiTokenBean>() {
				@Override
				public ApiTokenBean createFromParcel(Parcel source) {
					return new ApiTokenBean(source);
				}

				@Override
				public ApiTokenBean[] newArray(int size) {
					return new ApiTokenBean[size];
				}
			};
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeParcelable(this.apiToken, flags);
			dest.writeString(this.token);
		}

		public TokenBean() {
		}

		protected TokenBean(Parcel in) {
			this.apiToken = in.readParcelable(ApiTokenBean.class.getClassLoader());
			this.token = in.readString();
		}

		public static final Creator<TokenBean> CREATOR = new Creator<TokenBean>() {
			@Override
			public TokenBean createFromParcel(Parcel source) {
				return new TokenBean(source);
			}

			@Override
			public TokenBean[] newArray(int size) {
				return new TokenBean[size];
			}
		};
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.token, flags);
		dest.writeString(this.userName);
		dest.writeString(this.username);
		dest.writeString(this.uuid);
		dest.writeInt(this.sex);
		dest.writeString(this.description);
		dest.writeByte(this.gender ? (byte) 1 : (byte) 0);
		dest.writeInt(this.level);
		dest.writeString(this.avatarUrl);
		dest.writeInt(this.followersCount);
		dest.writeInt(this.friendsCount);
		dest.writeInt(this.statusCount);
		dest.writeInt(this.balance);
		dest.writeInt(this.contribution);
		dest.writeInt(this.popularity);
		dest.writeInt(this.experience);
		dest.writeString(this.type);
		dest.writeString(this.createdAt);
		dest.writeString(this.displayName);
		dest.writeString(this.birthday);
		dest.writeString(this.id);
	}

	public UserModel() {
	}

	protected UserModel(Parcel in) {
		this.token = in.readParcelable(TokenBean.class.getClassLoader());
		this.userName = in.readString();
		this.username = in.readString();
		this.uuid = in.readString();
		this.sex = in.readInt();
		this.description = in.readString();
		this.gender = in.readByte() != 0;
		this.level = in.readInt();
		this.avatarUrl = in.readString();
		this.followersCount = in.readInt();
		this.friendsCount = in.readInt();
		this.statusCount = in.readInt();
		this.balance = in.readInt();
		this.contribution = in.readInt();
		this.popularity = in.readInt();
		this.experience = in.readInt();
		this.type = in.readString();
		this.createdAt = in.readString();
		this.displayName = in.readString();
		this.birthday = in.readString();
		this.id = in.readString();
	}

	public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
		@Override
		public UserModel createFromParcel(Parcel source) {
			return new UserModel(source);
		}

		@Override
		public UserModel[] newArray(int size) {
			return new UserModel[size];
		}
	};
}
