package org.nhnnext.android.day3_simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/**
 *  HTTP로 외부의 데이터를 반환하는 클래스
 */
public class Proxy {
 
	
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
	


}