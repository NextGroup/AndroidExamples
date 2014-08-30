package org.nhnnext.android.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.view.Display;
import android.view.WindowManager;

public class HomeController {
	Context context;
	//refreshData를 위한 Proxy와 ProviderDao 선언
	private Proxy proxy;
	private ProviderDao dao;
	private SharedPreferences pref;

	public HomeController(Context context) {
		this.context = context;
		
		proxy = new Proxy(context);
		dao = new ProviderDao(context);
	}

	public void startSyncDataService(){
		Intent implictIntent = new Intent();
		implictIntent.setAction("org.nhnnext.android.android.basic.SyncDataService");
		context.startService(implictIntent);
	}
	
	public void refreshData() {
		
		new Thread() {
			public void run() {
				String jsonData = proxy.getJSON();
				dao.insertJsonData(jsonData);
			}
		}.start();
	}
	
	
	public void initSharedPreferences() {
		pref = context.getSharedPreferences(
				context.getResources().getString(R.string.pref_name),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString(context.getResources().getString(R.string.server_url),
				context.getResources().getString(R.string.server_url_value));

		editor.putString(
				context.getResources().getString(R.string.pref_article_number),
				"0");
		editor.commit();
		// 아이디가 들어갈 것을 가정을하자
		editor.putString(
				context.getResources().getString(R.string.files_directory),
				context.getApplicationContext().getFilesDir().getPath() + "/");
		String androidID = Secure.getString(context.getApplicationContext()
				.getContentResolver(), Settings.Secure.ANDROID_ID);
		editor.putString(context.getResources().getString(R.string.device_id),
				Util.getMD5Hash(androidID));
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		editor.putInt(context.getResources().getString(R.string.display_width),
				display.getWidth());
		editor.commit();
	}

}
