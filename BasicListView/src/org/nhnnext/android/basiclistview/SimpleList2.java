package org.nhnnext.android.basiclistview;

import java.util.ArrayList;
import java.util.HashMap;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;

public class SimpleList2 extends Activity {

	String[] names = { "김정", "구승모", "오동우", "유재은", "윤지수",
			   		   "조혜연", "김종규", "박재성", "배영현", "서경진",
			   		   "손영수", "이남영", "이익훈", "정호영", "주형철",
			   		   "김동진", "박민근", "박은종", "박현정", "임석현",
			   		   "함석진", "홍은택" };
	
	String[] village = {"4마을", "3마을", "4마을", "4마을", "4마을",
	   		   			"-", "3마을", "3마을", "1마을", "2마을",
	   		   			"4마을", "2마을", "-", "1마을", "3마을",
	   		   			"4마을", "3마을", "-", "-", "2마을",
	   		   			"1마을", "카카오" };
	
	private ListView listView;
	ArrayList<HashMap<String, String>> list2 = new ArrayList<HashMap<String, String>>(2);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		listView =  (ListView)findViewById(R.id.listView1);
		setData();
		viewList();
	}
	
	private void setData() {
		for (int i = 0; i < names.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("line1", names[i]);
			map.put("line2", village[i]);
			list2.add(map);
		}
	}
	
	private void viewList() {
		String[] from = { "line1", "line2" };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list2,
				android.R.layout.simple_list_item_2, from, to);
		
		listView.setAdapter(simpleAdapter);
	}


}
