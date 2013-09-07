package org.nhnnext.android.nextagram;


import java.util.ArrayList;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {

	private Button testBu1;
	private Button testBu2;
	private ListView mainListView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		testBu1 =  (Button)findViewById(R.id.button1);
		testBu2 =  (Button)findViewById(R.id.button2);
		
		testBu1.setOnClickListener(this);
		testBu2.setOnClickListener(this);
		
		mainListView1 = (ListView)findViewById(R.id.listView1);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.button1:
			//Intent i = new Intent(this, Proxy.class);
			//startActivityForResult(i, 0);
			
			Intent intent = new Intent("org.nhnnext.android.DataRefresh");
	        sendBroadcast(intent);
			break;
		case R.id.button2:
			Dao dao = new Dao(this);
			listViewSimple1(dao.getData());
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}


}
