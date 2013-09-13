package org.nhnnext.android.nextagram;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	
	private SQLiteDatabase database;
	private final String TABLE_NAME = "Articles";
	private Context context;
	public Dao(Context context) {
		
		this.context = context;
		
		sqLiteInitialize();
		if(!isTableExist()) {
			tableCreate();
		}
	}


	public void insertData(ArrayList<Article> articles) {
		for (int i = 0; i < articles.size(); i++) {
			dataInsert(articles.get(i));
		}
	}
	

	private void sqLiteInitialize() {
		database = context.openOrCreateDatabase("sqliteTest.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		database.setLocale(Locale.getDefault());
		database.setVersion(1);
	}
	
	
	private void tableCreate() {
		String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, ArticleNumber integer UNIQUE not null, Title text not null, Writer text not null, WriteDate text not null);";
		database.execSQL(sql);
	}
	
	
	private void dataInsert(Article article) {
		ContentValues values = new ContentValues();
		values.put("ArticleNumber", article.getArticleNumber());
		values.put("Title", article.getTitle());
		values.put("Writer", article.getWriter());
		values.put("WriteDate", article.getWriteDate());
		database.insert(TABLE_NAME, null, values);
		
	}
	
	public ArrayList<Article> getData() {
		
		ArrayList<Article> articleList = new ArrayList<Article>();
		
		int articleNumber;
		String title;
		String writer;
		String writeDate;
		
		if(isTableExist()) {
			Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, "_id");
			if (cursor != null) {
				cursor.moveToFirst();
				while (!(cursor.isAfterLast())) {
					articleNumber = cursor.getInt(1);
					title = cursor.getString(2);
					writer = cursor.getString(3);
					writeDate = cursor.getString(4);
					
					articleList.add(new Article(articleNumber, title, writer, writeDate));
					cursor.moveToNext();
				}
			}

			cursor.close();
		}
		
		return articleList;
	}
	
	
	
	private boolean isTableExist() {
		String searchTable = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "';";
		Cursor cursor = database.rawQuery(searchTable, null);
		
		if(cursor.getCount()==0) {
			return false;
		}
		
		cursor.close();
		
		return true;
	}
	
	
}
