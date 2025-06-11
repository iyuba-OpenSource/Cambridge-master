package tv.lycam.gift.entity;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

@Table(name = "gift")
public class GiftItem {
	@Column(name = "giftId")
	private String giftId;
	@Column(name = "name")
	private String name;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createAt")
	private String createAt;
	@Column(name = "updateAt")
	private String updateAt;
	@Column(name = "imageUrl")
	private String imageUrl;
	@Column(name = "displayName")
	private String displayName;
	@Column(name = "description")
	private String description;
	@Column(name = "price")
	private int price;
	@Column(name = "level")
	private int level;
	@Column(name = "_id", isId = true)
	private int id;

	private Animator mAnimator;

	// 一对一
	public Animator getGiftAnimator(DbManager db) throws DbException {
		return db.selector(Animator.class).where("parentId", "=", this.id).findFirst();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Animator getAnimator() {
		return mAnimator;
	}

	public void setAnimator(Animator animator) {
		this.mAnimator = animator;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GiftItem giftItem = (GiftItem) o;

		if (active != giftItem.active) return false;
		if (price != giftItem.price) return false;
		if (level != giftItem.level) return false;
		if (id != giftItem.id) return false;
		if (giftId != null ? !giftId.equals(giftItem.giftId) : giftItem.giftId != null)
			return false;
		if (name != null ? !name.equals(giftItem.name) : giftItem.name != null) return false;
		if (createAt != null ? !createAt.equals(giftItem.createAt) : giftItem.createAt != null)
			return false;
		if (updateAt != null ? !updateAt.equals(giftItem.updateAt) : giftItem.updateAt != null)
			return false;
		if (imageUrl != null ? !imageUrl.equals(giftItem.imageUrl) : giftItem.imageUrl != null)
			return false;
		if (displayName != null ? !displayName.equals(giftItem.displayName) : giftItem.displayName != null)
			return false;
		if (description != null ? !description.equals(giftItem.description) : giftItem.description != null)
			return false;
		return mAnimator != null ? mAnimator.equals(giftItem.mAnimator) : giftItem.mAnimator == null;

	}

	@Override
	public int hashCode() {
		int result = giftId != null ? giftId.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (active ? 1 : 0);
		result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
		result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
		result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
		result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + price;
		result = 31 * result + level;
		result = 31 * result + id;
		result = 31 * result + (mAnimator != null ? mAnimator.hashCode() : 0);
		return result;
	}
}