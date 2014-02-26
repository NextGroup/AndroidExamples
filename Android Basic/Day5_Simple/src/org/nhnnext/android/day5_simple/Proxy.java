package org.nhnnext.android.day5_simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

/**
 * HTTP로 외부의 데이터를 반환하는 클래스
 */
public class Proxy {

	public ArrayList<ContentValues> getArticlesAsContentValues(){
		ArrayList<ContentValues> contentValuesArr;
		String jsonData;
		try {
			jsonData = this.getJSON();
			contentValuesArr = this.parseJsonToContentValuesArr(jsonData);
			return contentValuesArr;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	private String getJSON() {
		try {
			URL url = new URL(HomeViewer.SERVER_ADDRESS + "loadData.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-length", "0");
			conn.setRequestProperty("Accept", "application/json");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);

			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.connect();

			int status = conn.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("test", "ERROR:" + e);
		}

		return null;
	}
	
	public ArrayList<ContentValues> parseJsonToContentValuesArr(String jsonData) {
		ArrayList<ContentValues> contentValuesArr = new ArrayList<ContentValues>(); 
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

				contentValuesArr.add(values);
			}
			return contentValuesArr;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}