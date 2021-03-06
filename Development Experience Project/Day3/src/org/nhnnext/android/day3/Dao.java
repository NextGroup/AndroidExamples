package org.nhnnext.android.day3;

import java.util.ArrayList;
import java.util.Locale;

import org.nhnnext.android.day3.MainActivity.BroadCastReceiverForList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * SQLite의 입출력을 담당하는 클래스
 */
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

	//SQLite 초기화
	private void sqLiteInitialize() {
		database = context.openOrCreateDatabase("sqliteTest.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		database.setLocale(Locale.getDefault());
		database.setVersion(1);
	}
	
	//테이블 생성
	private void tableCreate() {
		String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, ArticleNumber integer UNIQUE not null, Title text not null, Writer text not null, Id text not null, Content text not null, WriteDate text not null, ImgName text UNIQUE not null);";
		database.execSQL(sql);
	}
	
	
	
	//DB에 입력후 완료 여부를 BroadCast로 알려준다.
	public void insertData(ArrayList<Article> articles) {
		for (int i = 0; i < articles.size(); i++) {
			dbInsert(articles.get(i));
		}

		Log.i("test", "Send BroadCast For Refresh List");
		Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BroadCastReceiverForList.REFRESH_LIST);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        context.sendBroadcast(broadcastIntent);
        
	}
	
	//DB에 저장하기
	private void dbInsert(Article article) {
		ContentValues values = new ContentValues();
		values.put("ArticleNumber", article.getArticleNumber());
		values.put("Title", article.getTitle());
		values.put("Writer", article.getWriter());
		values.put("Id", article.getId());
		values.put("Content", article.getContent());
		values.put("WriteDate", article.getWriteDate());
		values.put("ImgName", article.getImgName());
		database.insert(TABLE_NAME, null, values);
		
	}

	
	//DB의 내용을 꺼내서 ArrayList<Article>형태로 반환한다.
	public ArrayList<Article> getArticleList() {
		ArrayList<Article> articleList = new ArrayList<Article>();
		
		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		if(isTableExist()) {
			Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, "_id");
			if (cursor != null) {
				cursor.moveToFirst();
				while (!(cursor.isAfterLast())) {
					articleNumber = cursor.getInt(1);
					title = cursor.getString(2);
					writer = cursor.getString(3);
					id = cursor.getString(4);
					content = cursor.getString(5);
					writeDate = cursor.getString(6);
					imgName = cursor.getString(7);
					
					articleList.add(new Article(articleNumber, title, writer, id, content, writeDate, imgName));
					cursor.moveToNext();
				}
			}

			cursor.close();
		}
		
		return articleList;
	}
	
	public Article getArticle(int articleNumber) {
		
		Article article = null;
		
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		if(isTableExist()) {
			Cursor cursor = database.query(TABLE_NAME, null, "ArticleNumber="+articleNumber, null, null, null, "_id");
			if (cursor != null) {
				cursor.moveToFirst();
				
				articleNumber = cursor.getInt(1);
				title = cursor.getString(2);
				writer = cursor.getString(3);
				id = cursor.getString(4);
				content = cursor.getString(5);
				writeDate = cursor.getString(6);
				imgName = cursor.getString(7);
				
				article = new Article(articleNumber, title, writer, id, content, writeDate, imgName);
			}

			cursor.close();
		}
		
		return article;
	}
	
	
	//DB에 테이블이 생성되어 있는지 확인
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
