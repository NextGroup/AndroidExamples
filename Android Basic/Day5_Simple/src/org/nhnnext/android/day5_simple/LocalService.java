package org.nhnnext.android.day5_simple;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service {
	private Proxy proxy;
	private NextgramController mainController;
	
	private TimerTask mTask;
	private Timer mTimer;
	
	int MINUTE;
	int countForTest=0;
	// 원격 서비스로서 작동시킬때 구현해야 하는 메서드임
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		proxy = new Proxy();
		mainController = NextgramController.getInstance();
		
		MINUTE = 60*1000;
		Log.i("Service", "Service onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// proxy를 실행시키고 Dao를 실행 ->
		Log.i("Service", "Service onStartCommand");
		
		mTask = new TimerTask() {
			public void run() {
				countForTest++;
				mainController.insertDataToDatabase();
				Log.i("Service", "TimerTask Start : " + countForTest);
			}
		};
		
		mTimer = new Timer();
		// MainActivity onResume에서 refreshData 에서 한번 실행하기 때문에 5분 Delay 후에 5분 주기로 실행 
		mTimer.schedule(mTask, 5*MINUTE, 5*MINUTE);
		// Just for TEST
		//mTimer.schedule(mTask, 10000, 10000);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mTimer.cancel();
		super.onDestroy();
	}

}
