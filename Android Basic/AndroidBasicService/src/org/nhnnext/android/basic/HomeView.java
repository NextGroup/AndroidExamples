package org.nhnnext.android.basic;

import java.util.ArrayList;

import org.nhnnext.android.basic.HomeViewAdapter.ViewHolderItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class HomeView extends ActionBarActivity {

	private ListView mainListView1;
	private SharedPreferences pref;
	private Proxy proxy;
	private ProviderDao dao;
	//private Dao dao;
	
	// HomeView의 Controller 역할을 하는 객체
	HomeController homeController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// HomeController에게 인스턴스 할당
		homeController = new HomeController(getApplicationContext());
		homeController.initSharedPreferences();
		homeController.startSyncDataService();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		dao = new ProviderDao(getApplicationContext());
		
		listViewSimple1(dao.getArticleList());
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onResume() {
		super.onResume();
		homeController.refreshData();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_item_refresh:
			homeController.refreshData();
			break;
		case R.id.action_item_write:
			Intent intent = new Intent();
			intent.setAction("org.nhnnext.android.basic.WritingArticle");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
			break;
		default:
			return false;
		}
		return true;
	}
	
	private void listViewSimple1(ArrayList<ArticleDTO> articles) {

		// 이전의 customAdapter
		//HomeViewAdapter customAdapter = new HomeViewAdapter(this, R.layout.custom_list_row, articles);
		
		Cursor mCursor = getContentResolver().query(
				NextgramContract.Articles.CONTENT_URI,
				NextgramContract.Articles.PROJECTION_ALL, null, null,
				NextgramContract.Articles._ID + " asc");
		
		HomeViewAdapter customAdapter = new HomeViewAdapter(this, mCursor, R.layout.custom_list_row);
		
		mainListView1.setAdapter(customAdapter);
		mainListView1.setOnItemClickListener(itemClickListener);
		
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
        	Intent intent = new Intent();
        	
        	Log.e("TEST", "Aritlce Number : " +((ViewHolderItem)view.getTag()).articleNumber);
    		intent.setAction("org.nhnnext.android.basic.Article");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("ArticleNumber",((ViewHolderItem)view.getTag()).articleNumber);
    		
    		startActivity(intent);
        }
    };


}
