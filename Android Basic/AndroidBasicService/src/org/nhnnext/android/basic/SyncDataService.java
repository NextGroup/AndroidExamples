package org.nhnnext.android.basic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncDataService extends Service{
	// 로그를 위한 태그를 각 클래스 이름으로 설정하기 위해 아래와 같이 TAG를 설정
	private static final String TAG = SyncDataService.class.getSimpleName();
	private static final int MINUTE = 60*1000;
	// Server에서 Article들을 받아오기 위한 Proxy
	private Proxy proxy;
	// 받아온 Article들을 DB에 저장하기 위한 Dao
	//private Dao dao;
	private ProviderDao dao;

	private TimerTask mTask;
	private Timer mTimer;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		proxy = new Proxy(getApplicationContext());
		//dao = new Dao(getApplicationContext());
		dao = new ProviderDao(getApplicationContext());
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		
		mTask = new TimerTask() {
			@Override
			public void run() {
				/* 변경된 부분 */
				// Proxy 클래스의 getJSON() 을 통해 json 데이터를 받아온 후
				String jsonData = proxy.getJSON();
				// Dao 클래스를 통해 Database에 저장하도록 합니다.
				dao.insertJsonData(jsonData);
			}
		};
		
		mTimer = new Timer();
		mTimer.schedule(mTask, 1*MINUTE, 1*MINUTE);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
}


