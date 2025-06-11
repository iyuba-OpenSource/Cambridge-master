package com.iyuba.camstory.sqlite.op;

import android.os.Parcel;
import android.os.Parcelable;

//import com.iyuba.voa.activity.sqlite.mode.Shareable;

public class BookInfo implements Parcelable{

	public String aboutAuthor;
	public String aboutBook;
	public String author;
	public String bookName_cn;
	public String bookName_en;
	public int level;
	public int bookorder;
	public String chapter1_cn;
	public String chapter1_en;
	public String interpreter;

	public String wordcount;
	public int collect;
	public int download;
	public String publicationdate;

	public String aboutinterpreter;

	public boolean isChecked;

	public String getAboutAuthor() {
		return aboutAuthor;
	}
    
	public void setAboutAuthor(String aboutAuthor) {
		this.aboutAuthor = aboutAuthor;
	}
	public String getDate() {
		return publicationdate;
	}
	
	public void setDate(String publicationdate) {
		this.publicationdate = publicationdate;
	}
	public int getCollect() {
		return collect;
	}
	public int getDownload(){ return download; }
	
	public void setCollect(int collect) {
		this.collect = collect;
	}
	public void setDownload(int download) { this.download = download; }


	public String getAboutBook() {
		return aboutBook;
	}

	public void setAboutBook(String aboutBook) {
		this.aboutBook = aboutBook;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookName_cn() {
		return bookName_cn;
	}

	public void setBookName_cn(String bookName_cn) {
		this.bookName_cn = bookName_cn;
	}

	public String getBookName_en() {
		return bookName_en;
	}

	public void setBookName_en(String bookName_en) {
		this.bookName_en = bookName_en;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getBookorder() {
		return bookorder;
	}

	public void setBookorder(int bookorder) {
		this.bookorder = bookorder;
	}

	public String getChapter1_cn() {
		return chapter1_cn;
	}

	public void setChapter1_cn(String chapter1_cn) {
		this.chapter1_cn = chapter1_cn;
	}

	public String getChapter1_en() {
		return chapter1_en;
	}

	public void setChapter1_en(String chapter1_en) {
		this.chapter1_en = chapter1_en;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	public String getAboutInterpreter() {
		return aboutinterpreter;
	}

	public void setAboutInterpreter(String aboutinterpreter) {
		this.aboutinterpreter = aboutinterpreter;
	}

	public String getWordcount() {
		return wordcount;
	}

	public void setWordcount(String wordcount) {
		this.wordcount = wordcount;
	}

	public static Creator<BookInfo> getCreator() {
		return CREATOR;
	}

	@SuppressWarnings("unchecked")
	public static final Creator<BookInfo> CREATOR = new Creator() {
		@Override
		public BookInfo createFromParcel(Parcel paramAnonymousParcel) {
			int i = 1;
			BookInfo localBookInfo = new BookInfo();
			localBookInfo.level = paramAnonymousParcel.readInt();
			localBookInfo.bookorder = paramAnonymousParcel.readInt();
			localBookInfo.bookName_cn = paramAnonymousParcel.readString();
			localBookInfo.bookName_en = paramAnonymousParcel.readString();
			localBookInfo.wordcount = paramAnonymousParcel.readString();
			localBookInfo.author = paramAnonymousParcel.readString();
			localBookInfo.interpreter = paramAnonymousParcel.readString();
			localBookInfo.aboutAuthor = paramAnonymousParcel.readString();
			localBookInfo.aboutinterpreter = paramAnonymousParcel.readString();
			localBookInfo.aboutBook = paramAnonymousParcel.readString();
			localBookInfo.chapter1_cn = paramAnonymousParcel.readString();
			localBookInfo.chapter1_en = paramAnonymousParcel.readString();
			localBookInfo.publicationdate = paramAnonymousParcel.readString();
			localBookInfo.collect= paramAnonymousParcel.readInt();
			localBookInfo.download = paramAnonymousParcel.readInt();
		

			return localBookInfo;

		}

		@Override
		public BookInfo[] newArray(int paramAnonymousInt) {
			return new BookInfo[paramAnonymousInt];
		}
	};

	public int describeContents() {
		return 0;
	}

	public int hashCode() {
		return (this.level + "_" + this.bookorder).hashCode();
	}

	@Override
	public void writeToParcel(Parcel paramParcel, int paramInt) {
		paramParcel.writeInt(this.level);
		paramParcel.writeInt(this.bookorder);
		paramParcel.writeString(this.bookName_cn);
		paramParcel.writeString(this.bookName_en);
		paramParcel.writeString(this.wordcount);
		paramParcel.writeString(this.author);
		paramParcel.writeString(this.interpreter);
		paramParcel.writeString(this.aboutAuthor);
		paramParcel.writeString(this.aboutinterpreter);
		paramParcel.writeString(this.aboutBook);
		paramParcel.writeString(this.chapter1_cn);
		paramParcel.writeString(this.chapter1_en);
		paramParcel.writeString(this.publicationdate);
		paramParcel.writeInt(this.collect);
		paramParcel.writeInt(this.download);
        
	}

	@Override
	public String toString() {
		return "BookInfo{" +
				"level=" + level +
				", bookorder=" + bookorder +
				'}';
	}

	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public String getShareUrl() {
		// TODO Auto-generated method stub
		return "http://camstory.cn/bookInfo.jsp?bid=" + this.bookorder
				+ "&levelid=" + this.level;
	}

	@Override
	public String getShareImageUrl() {
		// TODO Auto-generated method stub
		return "http://static2."+Constant.IYBHttpHead+"/camstory/images/"+this.bookorder+"_"+this.level+".jpg";
	}

	@Override
	public String getShareAudioUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShareTitle() {
		// TODO Auto-generated method stub
		return this.bookName_cn;
	}

	@Override
	public String getShareLongText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShareShortText() {
		// TODO Auto-generated method stub
		return this.bookName_en;
	}*/
}
