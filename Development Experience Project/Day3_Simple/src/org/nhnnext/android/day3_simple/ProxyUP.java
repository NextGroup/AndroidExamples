package org.nhnnext.android.day3_simple;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.util.Log;

/**
 * HttpURLConnection을 이용해 POST방식으로 게시글을 업로드 하는 클래스
 */
public class ProxyUP {

	public void uploadArticle(final Article article, final String filePath) {
		
		String uploadMessage = postToServer(article, filePath);
		Log.i("test", "uploadMessage:" + uploadMessage);
	
	}

	private String postToServer(Article article, String filePath) {
		
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		
		String message = "";
		
		
		String deviceID = MainActivity.DEVICE_ID;
		long uploadTime = Util.getMilliTime();
		
		String fileName = deviceID + uploadTime + "_" + article.getImgName();
		Log.i("test", "ProxyUpFileName:"+fileName);
		try {
			
			FileInputStream fis = new FileInputStream(filePath);
			
			URL url = new URL(MainActivity.SERVER_ADDRESS + "upload.php");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);

			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			
			
		    dos.writeBytes(getPostData("title",article.getTitle()));
		    dos.writeBytes(getPostData("writer",article.getWriter()));
		    dos.writeBytes(getPostData("id",article.getId()));
		    dos.writeBytes(getPostData("content",article.getContent()));
		    dos.writeBytes(getPostData("writeDate",article.getWriteDate()));
		    dos.writeBytes(getPostData("imgName",fileName));
		    
		    
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);


			
			int bytesAvailable = fis.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = fis.read(buffer, 0, bufferSize);

			Log.d("Test", "image byte is " + bytesRead+"|"+bytesAvailable);

			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e("Test", "File is written");
			fis.close();
			dos.flush(); // finish upload...

			int status = conn.getResponseCode();
			Log.i("test", "status:" + status);
			switch (status) {
			case 200:
			case 201:
				message = conn.getResponseMessage();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				Log.i("test", "upRESULT:"+URLDecoder.decode(sb.toString(),"UTF-8"));
				break;
			}

			dos.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("test", "ERROR:" + e);
		}

		return message;
	}
	
	private String getPostData(String key, String value) {
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		
		String result = twoHyphens + boundary + lineEnd;
		result += "Content-Disposition: form-data; name=\""+key+"\""+ lineEnd;
		result += lineEnd;
		try {
			result += URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		result += lineEnd;
		Log.i("test", "!result:"+result);
		return result;
	}

}