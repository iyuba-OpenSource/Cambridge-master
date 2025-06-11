package com.iyuba.camstory.sqlite.mode;


public class UserInfo {
	public String icoins;//爱语币
	public int credits;
	public int uid;//1
	public String username;// 用户名 2
	public int doings;// 发布的心情数 3
	public String views;// 访客数 4
	public String gender;// 性别 5
	public String text;// 最近的心情签名 6
	public int follower;// 粉丝数 7
	public String email;// 用户邮箱
	public int relation;//与当前用户关系    百位：我是否关注他 ，十位：特别关注  ：个位他是否关注我。自己看自己是0 8
	public int following;// 关注数 9
	public int iyubi; // 10
	public String vipStatus;  //11
	public String distance; //12
	public String notification; // 13
	public int studytime; // 14
	public String position;
	public String expireTime;//过期时间
	
	public UserInfo() {
		icoins = "0";
		uid = 0;
		username = "";
		doings = 0;
		views = "0";
		gender = "0";
		follower = 0;// 粉丝
		relation = 0;// 与当前用户关系 百位我是否关注他十位特别关注 个位他是否关注我
		following = 0;// 关注
		iyubi = 0;
		vipStatus = "0";
		notification = "0";
		studytime = 0;
		position = "100000";
		expireTime = "";
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"icoins='" + icoins + '\'' +
				", credits=" + credits +
				", uid=" + uid +
				", username='" + username + '\'' +
				", doings=" + doings +
				", views='" + views + '\'' +
				", gender='" + gender + '\'' +
				", text='" + text + '\'' +
				", follower=" + follower +
				", email='" + email + '\'' +
				", relation=" + relation +
				", following=" + following +
				", iyubi=" + iyubi +
				", vipStatus='" + vipStatus + '\'' +
				", distance='" + distance + '\'' +
				", notification='" + notification + '\'' +
				", studytime=" + studytime +
				", position='" + position + '\'' +
				", expireTime='" + expireTime + '\'' +
				'}';
	}
}
