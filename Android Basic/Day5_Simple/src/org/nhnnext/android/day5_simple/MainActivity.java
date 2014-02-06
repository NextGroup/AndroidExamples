package org.nhnnext.android.day5_simple;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity{

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

		FILES_DIR = getApplicationContext().getFilesDir().getPath() + "/";

		String androidID = Secure.getString(getApplicationContext()
				.getContentResolver(), Settings.Secure.ANDROID_ID);
		DEVICE_ID = Util.getMD5Hash(androidID);

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		displayW = display.getWidth();

		mainListView1 = (ListView) findViewById(R.id.main_listView1);

		proxy = new Proxy();
		dao = new Dao(getApplicationContext());
		
		// Service를 실행 시킨다.
		// Service를 실행 시키는 타이밍은 언제?
		// Service를 하나만 실행시키도록 어떻게 보장할까.
		// * flag를 설치한다, 
		Intent intent = new Intent(this, LocalService.class);
		startService(intent);

	}

	/*
	 * ActionBar Compat을 사용하기 위해 onCreateOptionsMenu를 구현한다.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * onOptionsItemSelected를 사용해 Actionbar에 있는 아이템의 이벤트를 받을 수 있다.
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
			text = "write";
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ArticleWriter.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		default:
			return false;
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	private final Handler mHandler = new Handler();

	private void refreshData() {

		new Thread() {
			public void run() {
				//String jsonData = getJsonTestData(); 
				proxy.getJSON();
				Log.i("test", jsonData);
				dao.insertJsonData(jsonData);

				mHandler.post(new Runnable() {
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
		CustomAdapter customAdapter = new CustomAdapter(this,
				R.layout.custom_list_row, articles);
		mainListView1.setAdapter(customAdapter);
		mainListView1.setOnItemClickListener(itemClickListener);

	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ArticleViewer.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("ArticleNumber", view.getTag().toString());

			startActivity(intent);
		}
	};
    public String getJsonTestData() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("");
       
        sb.append("[");
       /*
        * 
        * (
			_id integer primary key autoincrement, 
			ArticleNumber integer UNIQUE not null, 
			Title text not null, Writer text not null, 
			Id text not null, 
			Content text not null, 
			WriteDate text not null, 
			ImgName text UNIQUE not null
		);
        */
        sb.append("      {");
        sb.append("         'ArticleNumber':'1',");
        sb.append("         'Title':'오늘도 좋은 하루',");
        sb.append("         'Writer':'학생1',");
        sb.append("         'Id':'6613d02f3e2153283f23bf621145f877',");
        sb.append("         'Content':'하지만 곧 기말고사지...',");
        sb.append("         'WriteDate':'2013-09-23-10-10',");
        sb.append("         'ImgName':'photo1.jpg'");
        sb.append("      },");
        sb.append("      {");
        sb.append("         'ArticleNumber':'2',");
        sb.append("         'Title':'대출 최고 3000만원',");
        sb.append("         'Writer':'김미영 팀장',");
        sb.append("         'Id':'6326d02f3e2153266f23bf621145f734',");
        sb.append("         'Content':'김미영팀장입니다. 고갱님께서는 최저이율로 최고 3000만원까지 30분 이내 통장입금가능합니다.',");
        sb.append("         'WriteDate':'2013-09-24-11-22',");
        sb.append("         'ImgName':'photo2.jpg'");
        sb.append("      },");
        sb.append("      {");
        sb.append("         'ArticleNumber':'3',");
        sb.append("         'Title':'MAC등록신청',");
        sb.append("         'Writer':'학생2',");
        sb.append("         'Id':'8426d02f3e2153283246bf6211454262',");
        sb.append("         'Content':'1a:2b:3c:4d:5e:6f',");
        sb.append("         'WriteDate':'2013-09-25-12-33',");
        sb.append("         'ImgName':'photo3.jpg'");
        sb.append("      }");
       
        sb.append("]");
         
         return sb.toString();
}
}
