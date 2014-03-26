package org.nhnnext.android.androidnaming;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomeView extends ActionBarActivity {

	private ListView mainListView1;
	
	public static String SERVER_ADDRESS = "http://10.73.44.93/~stu20/";
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
		
		String androidID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		DEVICE_ID = Util.getMD5Hash(androidID);
		
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		displayW = display.getWidth();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		proxy = new Proxy();
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
