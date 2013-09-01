package org.next.android.basichttpjsonsqlite1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 인터넷 상에 있는 JSON파일을 읽어서
 * SQLite에 저장을 하고
 * 저장된 데이터를 읽어서 리스트로 표시하는 예
 * 
 * @author flashscope
 * @version 1.0
 */
public class MainActivity extends Activity {

	private SQLiteDatabase database1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sqLiteInitialize();
		jsonLoad();

	}
	
	/**
	 * sqlite DB 초기화
	 */
	private void sqLiteInitialize() {
		database1 = openOrCreateDatabase("sqliteTest.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		database1.setLocale(Locale.getDefault());
		database1.setVersion(3);
		
		//테이블이 생성되었는지 확인
		String searchTable = "select DISTINCT tbl_name from sqlite_master where tbl_name = 'articles';";
		Cursor cursor = database1.rawQuery(searchTable, null);
		
		if(cursor.getCount()==0) {
			Log.i("test","no column so Make Table");
			String sql = "create table articles(_id integer primary key autoincrement, ArticleNumber integer UNIQUE not null, Title text not null, Writer text not null, View integer not null, Like integer not null, WriteDate text not null, ModifyDate text not null);";
			database1.execSQL(sql);
		}
		
		cursor.close();
	}

	
	/**
	 * JSON데이터를 받아오는 부분
	 * 스레드를 생성해서 JSON을 받아오는 함수, JSON을 DB에 넣어주는 부분을 실행시킨다.
	 * 아ICS?쯤부터 네트워킹은 스레드로 빼주지 않으면 안되서 더 복잡해짐
	 */
	private ProgressDialog progressDialog;

	private void jsonLoad() {

		final Handler mHandler = new Handler();

		new Thread() {

			public void run() {
				try {

					mHandler.post(new Runnable() {
						public void run() {
							progressDialog = ProgressDialog.show(MainActivity.this, "로딩중", "JSON데이터를 불러오고 있습니다.");
						}

					});
					
					//스레드에서 작업할 내용 
					String jsonData = readJsonFile();
					saveJsonToDB(jsonData);

					
					mHandler.post(new Runnable() {
						public void run() {
							progressDialog.cancel();
							showList();
						}
					});

				} catch (Exception e) {
					Log.e("test", "ERROR-" + e);
				}
			}
		}.start();
	}

	/**
	 * 인터넷에 있는 JSON파일을 읽어와서 String으로 반환해준다.
	 * @return jsonData
	 */
	private String readJsonFile() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://scope.hosting.bizfree.kr/next/android/jsonSqlite/boardData.json");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader( new InputStreamReader(content));
				
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				
				reader.close();
				content.close();
				
			} else {
				Log.e("test", "Failed to download file");
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
		
	}
	
	/**
	 * JSON을 파싱해서 DB에 하나씩 집어 넣어준다.
	 * @param jsonData
	 * @throws JSONException
	 */
	private void saveJsonToDB(String jsonData) throws JSONException {
		
		JSONArray jArr = new JSONArray(jsonData);
		
		//데이터가 중복으로 들어가는 것을 막기 위해 ArticleNumber를 UNIQUE로 테이블을 만들었는데
		//오히려 기존 데이터가 갱신이 안되는 문제 발생
		//아예 데이터를 날려버리고 다시 쓰는 방법도 있고 값을 비교해서 update 하는 방법도 있는데
		//어느 방법이 좋을지 고민중
		for (int i = 0; i < jArr.length(); ++i) {
			JSONObject jObj = jArr.getJSONObject(i);
			ContentValues values = new ContentValues();
			
			values.put("ArticleNumber", jObj.getInt("ArticleNumber"));
			values.put("Title", jObj.getString("Title"));
			values.put("Writer", jObj.getString("Writer"));
			values.put("View", jObj.getInt("View"));
			values.put("Like", jObj.getInt("Like"));
			values.put("WriteDate", jObj.getString("WriteDate"));
			values.put("ModifyDate", jObj.getString("ModifyDate"));
			
			database1.insert("articles", null, values);
		}
		
		
		
	}
	
	/**
	 * DB에 들어간 데이터를 꺼내서 리스트에 표시 
	 * simple_list_item_2를 사용해서 간단하게 2줄 표시
	 */
	private void showList() {
		ListView listView1 = (ListView)findViewById(R.id.listView1);
		ArrayList<HashMap<String, String>> listData1 = new ArrayList<HashMap<String, String>>(2);
		
		Cursor cursor = database1.query("articles", null, null, null, null, null, "_id");
		
		if (cursor != null) {
			cursor.moveToFirst();
			while(!(cursor.isAfterLast())){
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("line1", cursor.getString(2));
				map.put("line2", cursor.getString(3));
				listData1.add(map);
				
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		
		String[] from = { "line1", "line2" };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listData1,
				android.R.layout.simple_list_item_2, from, to);
		
		
		listView1.setAdapter(simpleAdapter);
	}
	
}
