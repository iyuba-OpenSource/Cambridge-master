package com.iyuba.camstory.sqlite.op;

import android.os.Parcel;
import android.os.Parcelable;

public class Chapter implements Parcelable {

	public int level;
	public int bookorder;
	public String chapterName;
	public int chapterorder;
	@SuppressWarnings("unchecked")
	public static final Creator<Chapter> CREATOR = new Creator() {
		@Override
		public Chapter createFromParcel(Parcel paramAnonymousParcel) {
			Chapter localChapter = new Chapter();
			localChapter.level = paramAnonymousParcel.readInt();

			localChapter.bookorder = paramAnonymousParcel.readInt();
			localChapter.chapterorder = paramAnonymousParcel.readInt();
			localChapter.chapterName = paramAnonymousParcel.readString();
			return localChapter;
		}

		@Override
		public Chapter[] newArray(int paramAnonymousInt) {
			return new Chapter[paramAnonymousInt];
		}
	};

	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel paramParcel, int paramInt) {
		paramParcel.writeInt(this.level);
		paramParcel.writeInt(this.bookorder);
		paramParcel.writeInt(this.chapterorder);
		paramParcel.writeString(this.chapterName);
	}

	@Override
	public String toString() {
		return "Chapter{" +
				"level=" + level +
				", bookorder=" + bookorder +
				", chapterName='" + chapterName + '\'' +
				", chapterorder=" + chapterorder +
				'}';
	}

	public int getLevel() {
		return level;
	}

	public int getBookorder() {
		return bookorder;
	}

	public String getChapterName() {
		return chapterName;
	}

	public int getChapterorder() {
		return chapterorder;
	}

	public static Creator<Chapter> getCREATOR() {
		return CREATOR;
	}
}
