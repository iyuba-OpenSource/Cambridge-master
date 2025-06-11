package com.iyuba.camstory.sqlite.mode;
import com.iyuba.voa.activity.setting.Constant;

public class Voa implements Comparable<Voa> {
	public int voaid; // 新闻ID
	public String title = ""; // 标题
	public String desccn = ""; // 中文描述
	public String title_cn = ""; // 中文标题
	public int category; // 分类
	public String sound = ""; // 声音
	public String url = "";
	public String pic = ""; // 图片
	public String creattime = ""; // 创建时间
	public int readcount = 0; // 阅读量
	public String hotflg = ""; // 热门标签
	public String titleFind = "";
	public String textFind = "";
	public boolean favourite;
	public long isRead;
	public boolean isDownloaded = false;
	public boolean isClickDownload = false;

	public boolean isDelete = false;
	public int downLoadPercentage; // 下载百分比

	public Voa() {
		this.voaid = 0;
	}

	@Override
	public int compareTo(Voa vob) {
		if (!(vob instanceof Voa)) {
			return 1;
		}
		if (this.voaid > vob.voaid)
			return 1;
		if (this.voaid < vob.voaid)
			return -1;
		return 0;
	}

	public boolean isNewerThan(Voa vob) {
		if (!(vob instanceof Voa) || vob.isNullObject())
			return true;
		if (this.voaid == vob.voaid)
			return false;
		if (Constant.getAppid().equals("221")) {
			return (this.creattime.compareTo(vob.creattime)) > 0;
		} else {
			if (this.creattime.equals(vob.creattime)) {
				return (this.voaid > vob.voaid);
			} else {
				return (this.creattime.compareTo(vob.creattime)) > 0;
			}
		}
	}

	public boolean isNullObject() {
		return (0 == this.voaid);
	}

	public boolean isReaded() {
		return (isRead != 0);
	}

	/*@Override
	public String getShareUrl() {
		String url;
		if (Constant.getAppid().equals("229") || Constant.getAppid().equals("213")) {
			url = CrashApplication.getInstance().getString(R.string.sharesdk_url);
		} else {
			url = CrashApplication.getInstance().getString(R.string.sharesdk_url) + this.voaid;
		}
		return url;
	}

	@Override
	public String getShareImageUrl() {
		return this.pic;
	}

	@Override
	public String getShareAudioUrl() {
		return Constant.getAudiourl() + this.sound;
	}

	@Override
	public String getShareTitle() {
		return this.title_cn;
	}

	@Override
	public String getShareLongText() {
		return "#" + Constant.getAppname() + "#《" + this.title_cn + "》[" + getShareUrl() + "]"
				+ this.desccn;
	}

	@Override
	public String getShareShortText() {
		return this.title;
	}*/

}
