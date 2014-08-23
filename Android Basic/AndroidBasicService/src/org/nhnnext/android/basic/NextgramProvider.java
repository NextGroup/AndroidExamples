package org.nhnnext.android.basic;

import java.util.Locale;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class NextgramProvider extends ContentProvider {
	private SQLiteDatabase database;
	private final String TABLE_NAME = "Articles";
	private Context context;

	// URI Matcher를 위한 상수.
	private static final int ARTICLE_LIST = 1;
	private static final int ARTICLE_ID = 2;
	private static final UriMatcher URI_MATCHER;

	// Uri Matcher를 준비합니다.
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER
				.addURI(NextgramContract.AUTHORITY, "Articles", ARTICLE_LIST);
		URI_MATCHER
				.addURI(NextgramContract.AUTHORITY, "Articles/#", ARTICLE_ID);
	}

	@Override
	public boolean onCreate() {
		// Application의 Context를 기억.
		this.context = getContext();
		// Database 생성
		sqLiteInitialize();
		// Table 존재유무를 판단한 후 테이블 생성
		if (!isTableExist()) {
			tableCreate();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		switch (URI_MATCHER.match(uri)) {
		case ARTICLE_LIST:
			if(TextUtils.isEmpty(sortOrder)){
				sortOrder = NextgramContract.Articles.SORT_ORDER_DEFAULT;
			}
			
			break;
		case ARTICLE_ID:
			
			if(TextUtils.isEmpty(sortOrder)){
				sortOrder = NextgramContract.Articles.SORT_ORDER_DEFAULT;
			}
			if(selection == null){
				selection = "_ID = ?";
				selectionArgs = new String[] {uri.getLastPathSegment()};
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		Cursor cursor = database.query(TABLE_NAME,
				NextgramContract.Articles.PROJECTION_ALL, selection,
				selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;

	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		/**
		 * URI에 대한 유효성 검사
		 */
		if (URI_MATCHER.match(uri) != ARTICLE_LIST) {
			throw new IllegalArgumentException("Insertion을 지원하지 않는 URI 입니다 : "
					+ uri);
		}

		// ARTICLE_LIST에 대한 URI요청이 들어왔을때
		if (URI_MATCHER.match(uri) == ARTICLE_LIST) {

			// Database에 Insert를 하고 ID를 리턴받음.
			long id = database.insert("Articles", null, values);

			// 리턴받은 ID를 통해 ContentUris에 등록
			Uri itemUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(itemUri, null);

			return itemUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void sqLiteInitialize() {
		database = context.openOrCreateDatabase("sqliteTest.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		database.setLocale(Locale.getDefault());
		database.setVersion(1);
	}

	private void tableCreate() {
		String sql = "create table "
				+ TABLE_NAME
				+ "(_id integer primary key autoincrement, ArticleNumber integer UNIQUE not null, Title text not null, Writer text not null, Id text not null, Content text not null, WriteDate text not null, ImgName text UNIQUE not null);";
		database.execSQL(sql);

	}

	private boolean isTableExist() {
		String searchTable = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
				+ TABLE_NAME + "';";
		Cursor cursor = database.rawQuery(searchTable, null);

		if (cursor.getCount() == 0) {
			return false;
		}

		cursor.close();

		return true;
	}

}
