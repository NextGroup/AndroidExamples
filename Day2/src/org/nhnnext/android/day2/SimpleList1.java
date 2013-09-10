package org.nhnnext.android.day2;

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
	
	
	/*
	 * Step1
	 * 리스트에 집어넣을 데이터를 리스트형태로 만들어 주는 과정
	 * simple_list_item_1은 리스트 하나에 한 문장만 출력되므로
	 * ArrayList<String>의 형태로 만들었다.
	 */
	private void setData() {
		for (int i = 0; i < names.length; i++) {
			list1.add(names[i]);
		}
	}
	
	/*
	 * Step2
	 * 리스트뷰에 어댑터를 설정을 해준다.
	 * 여기서 사용한 어댑터는 안드로이드에서 기본 제공해 주는 것으로
	 * simple_list_item_1 한 줄씩 표현을 해주는 것을 사용한다.
	 * 데이터로는 위에서 만든 리스트인 list1을 넣어주었다.
	 */
	private void viewList() {
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list1));
	}


}
