package org.nhnnext.android.nextagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Proxy extends BroadcastReceiver {
 
	private Context context;
	
    @Override
    public void onReceive(final Context context, Intent intent) {
    	final Handler mHandler = new Handler();
		new Thread() {

			public void run() {
		    	try {
		    		String jsonData = readJsonFile();
		    		saveJsonToArticle(jsonData);
		    		
		    		mHandler.post(new Runnable() {
						public void run() {
							Toast.makeText(context, "데이터 불러오기를 완료했습니다." , Toast.LENGTH_SHORT).show();
						}
					});
		    		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		this.context = context;
    	
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
	private void saveJsonToArticle(String jsonData) throws JSONException {
		
		ArrayList<Article> articleList = new ArrayList<Article>();
		
		int articleNumber;
		String title;
		String writer;
		String writeDate;
		
		JSONArray jArr = new JSONArray(jsonData);
		
		for (int i = 0; i < jArr.length(); ++i) {
			JSONObject jObj = jArr.getJSONObject(i);
			
			articleNumber = jObj.getInt("ArticleNumber");
			title = jObj.getString("Title");
			writer = jObj.getString("Writer");
			writeDate = jObj.getString("WriteDate");
			
			articleList.add(new Article(articleNumber, title, writer, writeDate));
			
		}
		
		Dao dao = new Dao(context);
		dao.insertData(articleList);
		
		
	}
}