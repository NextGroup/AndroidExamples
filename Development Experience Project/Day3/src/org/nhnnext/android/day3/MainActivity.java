package org.nhnnext.android.day3;

import java.util.ArrayList;

import org.nhnnext.android.day3.Proxy.BroadCastReceiverForParsing;


import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/*
 *    Activity                 Service
 *   ---Main----             ---Proxy---
 *   |  start  |   1.---->   |  JSON   |
 *   | service |             | Parsing |
 *   |         |             |         |
 *   |         |             -----------
 *   |         |                  |   
 *   |         |                  2.   
 *   |         |             Parsed Data    
 *   |         |              (Article)    
 *   |         |                  |    
 *   |         |                  |    
 *   | Broad   |                Class    
 *   | Cast    |             ----Dao----
 *   | Receiver|             |Insert DB|
 *   | --------|   Send      |---------|
 *   | |Load  ||   BroadCast |         |
 *   | |Finish||   3.<----   |         |
 *   | |      ||             |         |
 *   | |Refresh|             |         |
 *   | |List  ||   getData   | getData |
 *   | --------|   4.<--->   | (Select)|
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
public class MainActivity extends Activity {

	private ListView mainListView1;
	
	private BroadCastReceiverForList receiver;
	
	public static String SERVER_ADDRESS = "http://scope.hosting.bizfree.kr/next/android/jsonSqlite/";
	public static String FILES_DIR;
	public static String DEVICE_ID;
	public static int displayW;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FILES_DIR = getApplicationContext().getFilesDir().getPath() + "/";
		
		String androidID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		DEVICE_ID = Util.getMD5Hash(androidID);
		
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		displayW = display.getWidth();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		Intent intent = new Intent(this, Proxy.class);
        startService(intent);
        
	}
	
	/*
	 * BroadCastReciever를 동적으로 등록해줍니다.
	 */
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(BroadCastReceiverForList.REFRESH_LIST);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new BroadCastReceiverForList();
		registerReceiver(receiver, filter);
		
		super.onResume();
	}
	
	/*
	 * BroadCastReciever를 해제합니다.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	//action Bar Day4에서 다시 배웁니다.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_action, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.main_action_write:
        	Intent intent = new Intent();
    		intent.setClass(MainActivity.this, ArticleWriter.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
			break;
		case R.id.main_action_refresh:
			sendRefreshBroad();
			break;
			
			
		default:
			return false;
		}
		return true;
	}
	
	private void sendRefreshBroad() {
		Log.i("test", "Send BroadCast For Parsing");
		Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BroadCastReceiverForParsing.REFRESH_PARSING);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        getApplicationContext().sendBroadcast(broadcastIntent);
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



	/*
	 * Dao로부터 저장이 완료되었다는 BroadCast(PROCESS_RESPONSE)를 수신하게 되면
	 * Dao로부터 데이터를 가져와 리스트를 새로 갱신을 합니다.
	 */
	public class BroadCastReceiverForList extends BroadcastReceiver {
		public static final String REFRESH_LIST = "org.nhnnext.android.REFRESH_LIST";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("test", "receive broad!");
			Dao dao = new Dao(context);
			listViewSimple1(dao.getArticleList());
		}
	}
}
