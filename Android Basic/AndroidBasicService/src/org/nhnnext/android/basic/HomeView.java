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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(getResources().getString(R.string.server_url), getResources().getString(R.string.server_url_value));
		
		editor.putString(getResources().getString(R.string.pref_article_number), "0");
		editor.commit();
		// 아이디가 들어갈 것을 가정을하자
		editor.putString(getResources().getString(R.string.files_directory), getApplicationContext().getFilesDir().getPath() + "/");
		String androidID = Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		editor.putString(getResources().getString(R.string.device_id), Util.getMD5Hash(androidID));
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		editor.putInt(getResources().getString(R.string.display_width), display.getWidth());
		editor.commit();
		
		mainListView1 = (ListView)findViewById(R.id.main_listView1);
		
		proxy = new Proxy(getApplicationContext());
		dao = new ProviderDao(getApplicationContext());
		
		/*
		 * 서비스를 실행하는 코드 startService(Intent)를 이용해 시작한다.
		 */
		//Intent intent = new Intent(this, SyncDataService.class);
		//startService(intent);
		
		/*
		 * 암시적 인텐트를 활용한 서비스 시작 예제
		 */
		Intent implictIntent = new Intent();
		implictIntent.setAction("org.nhnnext.android.android.basic.SyncDataService");
		startService(implictIntent);
		
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
			intent.setAction("org.nhnnext.android.basic.WritingArticle");
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
				
				/* CursorAdapter를 사용할때는 매번 재갱신할 필요가 없습니다.
				mHandler.post(new Runnable(){
                     public void run() {
                    	 listViewSimple1(dao.getArticleList());
                     }
				});
				*/
				
			}
		}.start();
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
