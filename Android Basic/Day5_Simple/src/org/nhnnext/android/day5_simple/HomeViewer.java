package org.nhnnext.android.day5_simple;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HomeViewer extends ActionBarActivity {
	// 서버주소를 관리
	//public static final String SERVER_ADDRESS = "http://10.73.44.93/~stu20/";
	public static final String SERVER_ADDRESS = "http://scope.hosting.bizfree.kr/next/android/jsonSqlite/";
	// ListView를 담기위한 변수
	private ListView mainListView1;

	// 파일주소를 얻기 위한 변수
	public static String FILES_DIR;
	// 사용자별 고유한 식별값을 얻기 위한 변수
	public static String DEVICE_ID;
	// display의 Width를 저장하기 위한 변수 [ 의미 없어 보임 ]
	public static int displayW;

	//mainController를 담기위한 변수
	private NextgramController mainController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FILES_DIR = getApplicationContext().getFilesDir().getPath() + "/";

		String androidID = Secure.getString(getApplicationContext()
				.getContentResolver(), Settings.Secure.ANDROID_ID);
		DEVICE_ID = Util.getMD5Hash(androidID);

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		displayW = display.getWidth();

		mainListView1 = (ListView) findViewById(R.id.main_listView1);
		
		/*
		 *  mainController의 인스턴스를 가져오고 Context를 현재의 Context로 설정 후 
		 *  mainController의 메서드를 이용해 Database초기화 
		 */
		mainController = NextgramController.getInstance();
		mainController.setContext(getApplicationContext());
		mainController.initializeDatabase();
		
		/*
		 * 서비스를 실행하는 코드 startService(Intent)를 이용해 시작한다.
		 */
		Intent intent = new Intent(this, LocalService.class);
		startService(intent);

	}

	/*
	 * ActionBar Compat을 사용하기 위해 onCreateOptionsMenu를 구현한다.
	 * R.menu.main은 직접 구현한 xml 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * onOptionsItemSelected를 사용해 Actionbar에 있는 아이템의 이벤트를 받을 수 있다.
	 * MenuItem item.getItemId()를 통해 어떠한 버튼이 눌렸는지 확인할 수 있음
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String text = "";

		switch (item.getItemId()) {
		case R.id.action_item_refresh:
			text = "refresh";
			refreshData();
			break;
		case R.id.action_item_write:
			//Write item을 클릭할 경우 ArticleWriter로 넘어간다.
			text = "write";
			Intent intent = new Intent();
			intent.setClass(HomeViewer.this, ArticleWriter.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		default:
			return false;
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

		return true;
	}
	
	/*
	 * refreshData를 onResume()에 할당하여 다른 액티비티를 호출한 후 다시 복귀할 때 매번 실행시킬 수 있도록한다. 
	 */
	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	
	/*
	 * refreshData는 mainController의 insertDataToDatabase를 통해 Database로 정보를 넣은 후에  
	 * Handler에게 UI정보를 갱신하도록 합니다.
	 */
	private final Handler mHandler = new Handler();
	private void refreshData() {

		new Thread() {
			public void run() {
				mainController.insertDataToDatabase();
				mHandler.post(new Runnable() {
					public void run() {
						listViewSimple1(mainController.getArticleList());
					}
				});

			}
		}.start();
	}

	/*
	 * 이 예제에서는 Day2에서 사용하였던 Custom List를 사용합니다.
	 */
	private void listViewSimple1(ArrayList<Article> articles) {
		CustomAdapter customAdapter = new CustomAdapter(this,
				R.layout.custom_list_row, articles);
		mainListView1.setAdapter(customAdapter);
		mainListView1.setOnItemClickListener(itemClickListener);

	}
	
	/*
	 * ListView의 아이템이 클릭되었을때 ArticleViewer로 이동합니다.
	 */
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			Intent intent = new Intent();
			intent.setClass(HomeViewer.this, ArticleViewer.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("ArticleNumber", view.getTag().toString());

			startActivity(intent);
		}
	};
}
