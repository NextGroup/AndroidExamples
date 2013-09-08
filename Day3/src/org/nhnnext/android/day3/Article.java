package org.nhnnext.android.day3;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
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
	
	Article(Parcel in) {
		readFromParcel(in);
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeInt(articleNumber);
		dest.writeString(title);
		dest.writeString(writer);
		dest.writeString(writeDate);
	}
	private void readFromParcel(Parcel in) {
		this.articleNumber = in.readInt();
		this.title = in.readString();
		this.writer = in.readString();
		this.writeDate = in.readString();
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Article createFromParcel(Parcel in) {
             return new Article(in);
       }

       public Article[] newArray(int size) {
            return new Article[size];
       }
   };
   
}
