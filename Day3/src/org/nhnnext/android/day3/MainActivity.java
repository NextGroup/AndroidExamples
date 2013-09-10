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

/*
 *    Activity                 Service
 *   ---Main----             ---Proxy---
 *   |  start  |   1.---->   |  JSON   |
 *   | service |             | Parsing |
 *   |         |             |         |
 *   |         |             -----------
 *   |         |                  |   
 *   |         |                  2.   
 *   |         |             Parsed Data    
 *   |         |              (Article)    
 *   |         |                  |    
 *   |         |                  |    
 *   | Broad   |                Class    
 *   | Cast    |             ----Dao----
 *   | Receiver|             |Insert DB|
 *   | --------|   Send      |---------|
 *   | |Load  ||   BroadCast |         |
 *   | |Finish||   3.<----   |         |
 *   | |      ||             |         |
 *   | |Refresh|             |         |
 *   | |List  ||   getData   | getData |
 *   | --------|   4.<--->   | (Select)|
 *   -----------             -----------
 * 
 * 	 이 예제의 구조는 위와 같습니다.
 *   이 구조보다 더 좋은 방법은 많고 정답이라고 할 수는 없습니다.
 * 
 *   동작 구조
 *   1.Main Activity에서 Json파일을 파싱하는 Service인 Proxy를 실행합니다.
 *   여기서 Service를 사용하는 이유는 지속적으로 데이터를 갱신 할 수 있게 하기 위해서입니다.
 *   
 *   2.Dao는 DB의 입출력을 관리해주는 클래스입니다.
 *   Proxy가 파싱을 다 끝내면 파싱된 데이터를 Dao에게 보내 DB에 저장합니다.
 *   
 *   3.DB에 저장이 완료되면 Dao는 BroadCast를 보내 데이터 저장이 완료되었음을 알립니다.
 *   Main Activity에 BroadCastReceiver를 달아 완료 여부를 수신을 합니다.
 *   
 *   4.Main은 Dao로부터 DB에 담겨져있는 데이터들을 받아와서
 *   리스트를 갱신 합니다.
 */
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
	
	/*
	 * BroadCastReciever를 동적으로 등록해줍니다.
	 */
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(BasicBroadCastReceiver.PROCESS_RESPONSE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new BasicBroadCastReceiver();
		registerReceiver(receiver, filter);
		super.onResume();
	}
	
	/*
	 * BroadCastReciever를 해제합니다.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	
	/*
	 * 데이터를 받아오기 위해 Proxy Service를 실행합니다.
	 */
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.button1:
			Intent intent = new Intent(this, Proxy.class);
            startService(intent);
			break;
		}
		
	}
	
	/*
	 * 이 예제에서는 Day2에서 사용하였던 Simple list1을 사용합니다.
	 */
	private void listViewSimple1(ArrayList<Article> articles) {
		ArrayList<String> list1 = new ArrayList<String>();
		
		for (int i = 0; i < articles.size(); i++) {
			list1.add(articles.get(i).getTitle());
		}
		mainListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}
	

	/*
	 * Dao로부터 저장이 완료되었다는 BroadCast(PROCESS_RESPONSE)를 수신하게 되면
	 * Dao로부터 데이터를 가져와 리스트를 새로 갱신을 합니다.
	 */
	public class BasicBroadCastReceiver extends BroadcastReceiver {
		public static final String PROCESS_RESPONSE = "org.nhnnext.android.PROCESS_RESPONSE";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("test", "receive broad!");
			Dao dao = new Dao(context);
			listViewSimple1(dao.getData());
		}
	}
}
