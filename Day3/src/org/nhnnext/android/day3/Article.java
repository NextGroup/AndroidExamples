package org.nhnnext.android.day3;

import android.os.Parcel;
import android.os.Parcelable;

public class Article {
	private int articleNumber;
	private String title;
	private String writer;
	private String writeDate;
	
	Article(int articleNumber, String title, String writer, String writeDate) {
		this.articleNumber = articleNumber;
		this.title = title;
		this.writer = writer;
		this.writeDate = writeDate;
	}
	
	public int getArticleNumber() {
		return articleNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getWriter() {
		return writer;
	}

	public String getWriteDate() {
		return writeDate;
	}
   
}
