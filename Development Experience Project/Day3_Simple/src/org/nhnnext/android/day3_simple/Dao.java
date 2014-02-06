package org.nhnnext.android.day3_simple;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	


	public void insertJsonData(String jsonData) {
		
		JSONArray jArr;
		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		
		try {
			jArr = new JSONArray(jsonData);
		
		Img_Downloader imgDownLoader = new Img_Downloader(context);
		for (int i = 0; i < jArr.length(); ++i) {
			JSONObject jObj = jArr.getJSONObject(i);
			
			articleNumber = jObj.getInt("ArticleNumber");
			title = jObj.getString("Title");
			writer = jObj.getString("Writer");
			id = jObj.getString("Id");
			content = jObj.getString("Content");
			writeDate = jObj.getString("WriteDate");
			imgName = jObj.getString("ImgName");
			
			try {
				title = URLDecoder.decode(title,"UTF-8");
				writer = URLDecoder.decode(writer,"UTF-8");
				id = URLDecoder.decode(id,"UTF-8");
				content = URLDecoder.decode(content,"UTF-8");
				writeDate = URLDecoder.decode(writeDate,"UTF-8");
				imgName = URLDecoder.decode(imgName,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			ContentValues values = new ContentValues();
			values.put("ArticleNumber", articleNumber);
			values.put("Title", title);
			values.put("Writer", writer);
			values.put("Id", id);
			values.put("Content", content);
			values.put("WriteDate", writeDate);
			values.put("ImgName", imgName);
			database.insert(TABLE_NAME, null, values);
			
			imgDownLoader.copy_img(MainActivity.SERVER_ADDRESS + "image/" + imgName, imgName);
			
		}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
