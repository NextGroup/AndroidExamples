package org.nhnnext.android.day5_simple;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * SQLite의 입출력을 담당하는 클래스
 */
public class ArticleProvider extends ContentProvider{
	public static final String AUTHORITY = "org.nhnnext.android.day5_simple.Article";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/Articles");
    private static final String TABLE_NAME = "Articles";
	
    private SQLiteDatabase database;
	private Context context;
	
	private static UriMatcher sUriMatcher;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, TABLE_NAME, 0);
		sUriMatcher.addURI(AUTHORITY, TABLE_NAME+"/#", 1);
	}
	
	@Override
	public boolean onCreate() {
		try {
			createDatabase();
			tableCreate();
			Log.i("test", "Create Databases!!!");
		} catch (Exception e) {
			Log.i("test", "Don't Create Databases!!!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//SQLite 초기화
	private boolean createDatabase() {
		try {
			database = context.openOrCreateDatabase("sqliteTest.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			database.setLocale(Locale.getDefault());
			database.setVersion(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return database != null? true: false;
	}
	
	//테이블 생성
	private void tableCreate() {
		String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, ArticleNumber integer UNIQUE not null, Title text not null, Writer text not null, Id text not null, Content text not null, WriteDate text not null, ImgName text UNIQUE not null);";
		database.execSQL(sql);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		/*
		String tableName;
		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;
		*/
		long row_id;
		Log.i("test", "[AP]++++++++++++++++++++++++++++Uri : "+uri.toString());
		Log.i("test", "[AP]++++++++++++++++++++++++++++values : "+values.toString());
		/*
		switch (sUriMatcher.match(uri)) {
		case 0:

			tableName = TABLE_NAME;
			articleNumber = values.getAsInteger("ArticleNumber");
			title = values.getAsString("title");
			writer = values.getAsString("Writer");
			id = values.getAsString("Id");
			content = values.getAsString("Content");
			writeDate = values.getAsString("WriteDate");
			imgName = values.getAsString("ImgName");
						
			break;
		default:
			tableName = TABLE_NAME;
			articleNumber = values.getAsInteger("ArticleNumber");
			title = values.getAsString("title");
			writer = values.getAsString("Writer");
			id = values.getAsString("Id");
			content = values.getAsString("Content");
			writeDate = values.getAsString("WriteDate");
			imgName = values.getAsString("ImgName");
		}
		*/
		//database.insert(TABLE_NAME, null, values);
		try {
			row_id = database.insert(TABLE_NAME, null, values);
			if(row_id > 0){

				Uri itemUri = ContentUris.withAppendedId(CONTENT_URI, row_id);
				getContext().getContentResolver().notifyChange(itemUri, null);
				return itemUri;
				
			}else
				throw new SQLException();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uri;
		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case 1:
			if(TextUtils.isEmpty(sortOrder))
				sortOrder = "_ID ASC";
			break;

		default:
			// 만약 추가되지 않은 Uri면 여기서 처리
			break;
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}	
	

}
