package org.nhnnext.android.basiclistview;

import java.util.ArrayList;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;

public class SimpleList1 extends Activity {

	String[] names = { "김정", "구승모", "오동우", "유재은", "윤지수",
					   "조혜연", "김종규", "박재성", "배영현", "서경진",
					   "손영수", "이남영", "이익훈", "정호영", "주형철",
					   "김동진", "박민근", "박은종", "박현정", "임석현",
					   "함석진", "홍은택" };

	private ListView listView;
	ArrayList<String> list1 = new ArrayList<String>();
	
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
			list1.add(names[i]);
		}
	}
	
	private void viewList() {
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}


}
