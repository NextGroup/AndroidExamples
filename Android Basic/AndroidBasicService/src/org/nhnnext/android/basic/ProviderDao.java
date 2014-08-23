package org.nhnnext.android.basic;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

/**
 * SQLite의 입출력을 담당하는 클래스
 */
public class ProviderDao {

	private SQLiteDatabase database;
	private final String TABLE_NAME = "Articles";
	private Context context;
	private SharedPreferences pref;
	private String serverUrl;

	public ProviderDao(Context context) {
		this.context = context;
		this.serverUrl = context.getResources().getString(
				R.string.server_url_value);
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
		Log.i("[Info]", "json Data : " + jsonData);
		try {
			jArr = new JSONArray(jsonData);
			Log.i("Dao", "Hi Dao");
			ImageDownload imgDownLoader = new ImageDownload(context);
			for (int i = 0; i < jArr.length(); ++i) {
				JSONObject jObj = jArr.getJSONObject(i);

				articleNumber = jObj.getInt("ArticleNumber");
				title = jObj.getString("Title");
				writer = jObj.getString("Writer");
				id = jObj.getString("Id");
				content = jObj.getString("Content");
				writeDate = jObj.getString("WriteDate");
				imgName = jObj.getString("ImgName");

				// 이곳에 코드 추가
				if (i == jArr.length() - 1) {
					String prefName = context.getResources().getString(
							R.string.pref_name);
					pref = context.getSharedPreferences(prefName,
							context.MODE_PRIVATE);

					String prefArticleNumberKey = context.getResources()
							.getString(R.string.pref_article_number);

					SharedPreferences.Editor editor = pref.edit();
					editor.putString(prefArticleNumberKey, "" + articleNumber);
					editor.commit();
				}

				try {
					title = URLDecoder.decode(title, "UTF-8");
					writer = URLDecoder.decode(writer, "UTF-8");
					id = URLDecoder.decode(id, "UTF-8");
					content = URLDecoder.decode(content, "UTF-8");
					writeDate = URLDecoder.decode(writeDate, "UTF-8");
					imgName = URLDecoder.decode(imgName, "UTF-8");
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

				// 기존의 Dao.java에서 작성된 코드.
				// database.insert(TABLE_NAME, null, values);
				
				context.getContentResolver().insert(
						NextgramContract.Articles.CONTENT_URI, values);

				imgDownLoader.copy_img(serverUrl + "image/" + imgName, imgName);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// DB의 내용을 꺼내서 ArrayList<Article>형태로 반환한다.
	public ArrayList<ArticleDTO> getArticleList() {
		ArrayList<ArticleDTO> articleList = new ArrayList<ArticleDTO>();

		int articleNumber;
		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;

		// 1. isTableExist() 를 사용하지 않습니다.
		
		// 2. 기존의 database.query를 provider를 활용하도록 변경합니다. 
		//Cursor cursor = database.query(TABLE_NAME, null, null, null, null,
		//		null, "_id");
		
		Cursor cursor = context.getContentResolver().query(
				NextgramContract.Articles.CONTENT_URI,
				NextgramContract.Articles.PROJECTION_ALL, null, null,
				NextgramContract.Articles._ID + " asc");

		if (cursor != null) {
			cursor.moveToFirst();
			while (!(cursor.isAfterLast())) {
				//3.  column의 index정보가 변경되었으니 0번부터 값을 가져오도록 합시다.
				articleNumber = cursor.getInt(0);
				title = cursor.getString(1);
				writer = cursor.getString(2);
				id = cursor.getString(3);
				content = cursor.getString(4);
				writeDate = cursor.getString(5);
				imgName = cursor.getString(6);

				articleList.add(new ArticleDTO(articleNumber, title, writer,
						id, content, writeDate, imgName));
				cursor.moveToNext();
			}
		}

		cursor.close();

		return articleList;
	}

	public ArticleDTO getArticle(int articleNumber) {

		ArticleDTO article = null;

		String title;
		String writer;
		String id;
		String content;
		String writeDate;
		String imgName;

		
		// 1. isTableExist()를 사용하지 않습니다.
		
		// 2. 원하는 데이터에 접근하는 uri를 지정합니다. 
		Uri contentUri = Uri.parse(NextgramContract.Articles.CONTENT_URI
				.toString() + "/" + articleNumber);

		// 3. 기존의 database.query를 provider를 활용하도록 변경합니다.
		Cursor cursor = context.getContentResolver().query(contentUri,
				NextgramContract.Articles.PROJECTION_ALL, null,
				null, NextgramContract.Articles._ID + " asc");

		if (cursor != null) {
			cursor.moveToFirst();

			articleNumber = cursor.getInt(0);
			title = cursor.getString(1);
			writer = cursor.getString(2);
			id = cursor.getString(3);
			content = cursor.getString(4);
			writeDate = cursor.getString(5);
			imgName = cursor.getString(6);

			article = new ArticleDTO(articleNumber, title, writer, id, content,
					writeDate, imgName);
		}

		cursor.close();

		return article;
	}

}
