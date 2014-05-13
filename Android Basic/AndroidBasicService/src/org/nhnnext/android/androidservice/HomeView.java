package org.nhnnext.android.androidservice;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeView extends ActionBarActivity {

	private ListView mainListView1;
	
	public static String SERVER_ADDRESS = "http://10.73.44.93/~stu20/";
	public static String FILES_DIR;
	public static String DEVICE_ID;
	public static int displayW;
	
	private SharedPreferences pref;
	private Proxy proxy;
	private Dao dao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//strings.xml에서 값 가져오는 방법도 피피티에 넣기
		pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(getResources().getString(R.string.server_url), getResources().getString(R.string.server_url_value));
		
		editor.putString(getResources().getString(R.string.pref_article_number), "0");
		editor.commit();
		// 아이디가 들어갈 것을 가정을하자
		
		editor.putString(getResources().getString(R.string.files_directory), getApplicationContext().getFilesDir().getPath() + "/");
		//FILES_DIR = getApplicationContext().getFilesDir().getPath() + "/";
		
		String androidID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		editor.putString(getResources().getString(R.string.device_id), Util.getMD5Hash(androidID));
		editor.commit();
		
		//DEVICE_ID = Util.getMD5Hash(androidID);
		
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		displayW = display.getWidth();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		proxy = new Proxy(getApplicationContext());
		dao = new Dao(getApplicationContext());
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_item_refresh:
			refreshData();
			break;
		case R.id.action_item_write:
			Intent intent = new Intent();
    		intent.setClass(HomeView.this, WritingArticleView.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
			break;
		default:
			return false;
		}
		return true;
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
	
	private void listViewSimple1(ArrayList<ArticleDTO> articles) {
		HomeViewAdapter customAdapter = new HomeViewAdapter(this, R.layout.custom_list_row, articles);
		mainListView1.setAdapter(customAdapter);
		mainListView1.setOnItemClickListener(itemClickListener);
		
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
        	Intent intent = new Intent();
    		intent.setClass(HomeView.this, ArticleView.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("ArticleNumber",view.getTag().toString());
    		
    		startActivity(intent);
        }
    };


}
