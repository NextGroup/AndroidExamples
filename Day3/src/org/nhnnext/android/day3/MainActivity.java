package org.nhnnext.android.day3;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity implements OnClickListener {

	private Button testBu1;
	private ListView mainListView1;
	
	
	BasicBroadCastReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		testBu1 =  (Button)findViewById(R.id.button1);
		testBu1.setOnClickListener(this);
		
		mainListView1 = (ListView)findViewById(R.id.listView1);
	}
	
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(BasicBroadCastReceiver.PROCESS_RESPONSE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new BasicBroadCastReceiver();
		registerReceiver(receiver, filter);
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.button1:
			Log.i("test", "button press");
			Intent intent = new Intent(this, Proxy.class);
            startService(intent);
			break;
		}
		
	}
	
	private void listViewSimple1(ArrayList<Article> articles) {
		ArrayList<String> list1 = new ArrayList<String>();
		
		for (int i = 0; i < articles.size(); i++) {
			list1.add(articles.get(i).getTitle());
		}
		mainListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}
	

	public class BasicBroadCastReceiver extends BroadcastReceiver {
		public static final String PROCESS_RESPONSE = "org.nhnnext.android.PROCESS_RESPONSE";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("test", "receive broad!");
			Dao dao = new Dao(context);
			ArrayList<Article> articleList = intent.getExtras().getParcelableArrayList("Articles");
			dao.insertData(articleList);
			listViewSimple1(dao.getData());
		}
	}
}
