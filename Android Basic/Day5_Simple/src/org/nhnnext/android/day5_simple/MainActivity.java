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

public class MainActivity extends ActionBarActivity {
	public static final String SERVER_ADDRESS = "http://10.73.44.93/~stu20/";

	private ListView mainListView1;

	public static String FILES_DIR;
	public static String DEVICE_ID;
	public static int displayW;

	private Proxy proxy;
	private MainController mainController;

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
		mainController = MainController.getInstance();
		mainController.setContext(getApplicationContext());
		mainController.initializeDatabase();
	
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

}
