package org.nhnnext.android.day3_simple;

import java.util.ArrayList;

import org.nhnnext.android.day3_simple.R;


import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

/*
 *    Activity                  Class 
 *   ---Main----             ---Proxy---
 *   |         |   request   |  HTTP   |
 *   |         |   1.---->   |  DOWN   |
 *   |         |   getData   |         |
 *   |         |   2.<----   -----------
 *   |         |   
 *   |         |                Class    
 *   |         |             ----Dao----
 *   |         |  jsonString | Parsing |
 *   |         |   3.---->   |   JSON  |
 *   |         |             |---------|
 *   |         |             |  insert |
 *   |         |             |    DB   |
 *   |         |             |         |
 *   |  LIST   |  getArticle | getData |
 *   |  SHOW   |   4.<----   | (Select)|
 *   -----------             -----------
 * 
 * 	 이 예제의 구조는 위와 같습니다.
 *   이 구조보다 더 좋은 방법은 많고 정답이라고 할 수는 없습니다.
 * 
 *   동작 구조
 *   1.Main Activity에서 Json파일을 파싱하는 Service인 Proxy를 실행합니다.
 *   여기서 Service를 사용하는 이유는 지속적으로 데이터를 갱신 할 수 있게 하기 위해서입니다.
 *   
 *   2.Dao는 DB의 입출력을 관리해주는 클래스입니다.
 *   Proxy가 파싱을 다 끝내면 파싱된 데이터를 Dao에게 보내 DB에 저장합니다.
 *   
 *   3.DB에 저장이 완료되면 Dao는 BroadCast를 보내 데이터 저장이 완료되었음을 알립니다.
 *   Main Activity에 BroadCastReceiver를 달아 완료 여부를 수신을 합니다.
 *   
 *   4.Main은 Dao로부터 DB에 담겨져있는 데이터들을 받아와서
 *   리스트를 갱신 합니다.
 */
public class MainActivity extends Activity implements OnClickListener {

	private ListView mainListView1;
	
	public static String SERVER_ADDRESS = "http://scope.hosting.bizfree.kr/next/android/jsonSqlite/";
	public static String FILES_DIR;
	public static String DEVICE_ID;
	public static int displayW;
	
	private Proxy proxy;
	private Dao dao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buWrite = (Button) findViewById(R.id.main_button_write);
		Button buRefresh = (Button) findViewById(R.id.main_button_refresh);
		
		buWrite.setOnClickListener(this);
		buRefresh.setOnClickListener(this);
		
		FILES_DIR = getApplicationContext().getFilesDir().getPath() + "/";
		
		String androidID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		DEVICE_ID = Util.getMD5Hash(androidID);
		
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		displayW = display.getWidth();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		proxy = new Proxy();
		dao = new Dao(getApplicationContext());
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.main_button_write:
			Intent intent = new Intent();
    		intent.setClass(MainActivity.this, ArticleWriter.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
			break;
		case R.id.main_button_refresh:
			refreshData();
			break;
		}
	}
	
	
	
	private final Handler mHandler = new Handler();
	private void refreshData() {
		
		new Thread() {
			public void run() {
				String jsonData = proxy.getJSON();
				dao.insertJsonData(jsonData);
				
				mHandler.post(new Runnable(){
                     public void run() {
                    	 listViewSimple1(dao.getArticleList());
                     }
				});
				
			}
		}.start();
	}
	
	/*
	 * 이 예제에서는 Day2에서 사용하였던 Custom List를 사용합니다.
	 */
	private void listViewSimple1(ArrayList<Article> articles) {
		CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, articles);
		mainListView1.setAdapter(customAdapter);
		mainListView1.setOnItemClickListener(itemClickListener);
		
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
        	Intent intent = new Intent();
    		intent.setClass(MainActivity.this, ArticleViewer.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("ArticleNumber",view.getTag().toString());
    		
    		startActivity(intent);
        }
    };


}
