package org.nhnnext.android.basiclistview;

import java.util.ArrayList;


import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class CustomList extends Activity {


	String[] names = { "김정", "구승모", "오동우", "유재은", "윤지수",
			   		   "조혜연", "김종규", "박재성", "배영현", "서경진",
			   		   "손영수", "이남영", "이익훈", "정호영", "주형철",
			   		   "김동진", "박민근", "박은종", "박현정", "임석현",
			   		   "함석진", "홍은택" };
	
	String[] village = {"4마을", "3마을", "4마을", "4마을", "4마을",
	   		   			"4마을", "3마을", "3마을", "1마을", "2마을",
	   		   			"4마을", "2마을", "4마을", "1마을", "3마을",
	   		   			"4마을", "3마을", "4마을", "4마을", "2마을",
	   		   			"1마을", "카카오" };
	
	String[] imgPath = { "01.jpg", "02.jpg", "03.jpg", "04.jpg", "05.jpg",
						 "06.jpg", "07.jpg", "08.jpg", "09.jpg", "10.jpg", "11.jpg",
						 "12.jpg", "13.jpg", "14.jpg", "15.jpg", "16.jpg", "17.jpg",
						 "18.jpg", "19.jpg", "20.jpg", "21.jpg", "22.jpg" };
	
	private ListView listView;
	ArrayList<Professor> listCustom = new ArrayList<Professor>();
	
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
			Professor prof = new Professor(names[i], village[i], imgPath[i]);
			listCustom.add(prof);
		}
	}
	
	private void viewList() {
		CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, listCustom);
		listView.setAdapter(customAdapter);
	}


}
