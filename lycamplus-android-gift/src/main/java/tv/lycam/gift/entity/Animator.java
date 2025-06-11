package tv.lycam.gift.entity;

import android.text.TextUtils;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

@Table(name = "animator")
public class Animator {
	@Column(name = "fileName")
	private String fileName;
	@Column(name = "animName")
	private String animName;
	@Column(name = "url")
	private String url;
	@Column(name = "_id", isId = true)
	private int id;
	@Column(name = "parentId", property = "UNIQUE") //如果是一对一加上唯一约束
	private long parentId; // 外键表id

	public GiftItem getGiftItem(DbManager db) throws DbException {
		return db.findById(GiftItem.class, parentId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAnimName() {
		return animName;
	}

	public void setAnimName(String animName) {
		this.animName = animName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 该动画是否可用
	 *
	 * @return available if true, otherwise, ...
	 */
	public boolean isAvailable() {
		return !(TextUtils.isEmpty(fileName) || TextUtils.isEmpty(animName) || TextUtils.isEmpty(url));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Animator that = (Animator) o;

		if (parentId != that.parentId) return false;
		if (!fileName.equals(that.fileName)) return false;
		if (!animName.equals(that.animName)) return false;
		return url.equals(that.url);

	}

	@Override
	public int hashCode() {
		int result = fileName.hashCode();
		result = 31 * result + animName.hashCode();
		result = 31 * result + url.hashCode();
		result = 31 * result + (int) (parentId ^ (parentId >>> 32));
		return result;
	}
}