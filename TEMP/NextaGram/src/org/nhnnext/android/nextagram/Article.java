package org.nhnnext.android.nextagram;

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

	public void setArticleNumber(int articleNumber) {
		this.articleNumber = articleNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
}
