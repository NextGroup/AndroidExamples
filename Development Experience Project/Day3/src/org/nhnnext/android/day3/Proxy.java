package org.nhnnext.android.day3;

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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 *  HTTP로 외부의 JSON파일을 읽어와서 파싱을 하는 서비스
 */
public class Proxy extends Service {
 
    private BroadCastReceiverForParsing receiver;
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
		
		IntentFilter filter = new IntentFilter(BroadCastReceiverForParsing.REFRESH_PARSING);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new BroadCastReceiverForParsing();
		registerReceiver(receiver, filter);
		
		runProxy();
		
	  return START_REDELIVER_INTENT;
	  
	 }
	
	/*
	 * BroadCastReciever를 해제합니다.
	 */
	@Override
	public void onDestroy() {
		Log.i("test", "DEstory1");
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	private void runProxy() {
		new Thread() {
			public void run() {
				String jsonData = getJSON();
				Log.i("test", "jsonData:" + jsonData);
				try {
					saveJsonToArticle(jsonData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	public String getJSON() {
		try {
			URL url = new URL(MainActivity.SERVER_ADDRESS + "loadData.php");
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
			Log.i("test", "status:"+status);
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
			Log.i("test", "ERROR:"+e);
		}
		
		return null;
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
		String id;
		String content;
		String writeDate;
		String imgName;
		
		JSONArray jArr = new JSONArray(jsonData);
		
		Img_Downloader imgDownLoader = new Img_Downloader(getBaseContext());
		
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
			Log.i("test", "titleDecode:"+title);
			Log.i("test", "imgNameDecode:"+imgName);
			
			articleList.add(new Article(articleNumber, title, writer, id, content, writeDate, imgName));
			imgDownLoader.copy_img(MainActivity.SERVER_ADDRESS + "image/" + imgName, imgName);
		}
		
		Dao dao = new Dao(getBaseContext());
		dao.insertData(articleList);
		
		
	}

	public class BroadCastReceiverForParsing extends BroadcastReceiver {
		public static final String REFRESH_PARSING = "org.nhnnext.android.REFRESH_PARSING";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("test", "receive broad Proxy!");
			runProxy();
		}
		
	}


}