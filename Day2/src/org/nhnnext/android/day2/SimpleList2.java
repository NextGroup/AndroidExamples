package org.nhnnext.android.day2;

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
						"4마을", "3마을", "3마을", "1마을", "2마을",
						"4마을", "2마을", "4마을", "1마을", "3마을",
						"4마을", "3마을", "4마을", "4마을", "2마을",
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
	
	/*
	 * Step1
	 * 리스트에 집어넣을 데이터를 리스트형태로 만들어 주는 과정
	 * simple_list_item_2은 리스트 하나에 두 문장이 출력되는것으로
	 * 한줄만 출력되던 simple_list_item_1과 달리 데이터가 2개씩 들어가야한다.
	 * 이를 위해 리스트에 해시 맵 형태로 데이터를 입력한다.
	 */
	private void setData() {
		for (int i = 0; i < names.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("line1", names[i]);
			map.put("line2", village[i]);
			list2.add(map);
		}
	}
	
	/*
	 * Step2
	 * 리스트뷰에 어댑터를 설정을 해준다.
	 * 여기서 사용한 어댑터는 안드로이드에서 기본 제공해 주는 것으로
	 * simple_list_item_2로 두 줄씩 표현을 해주는 것을 사용한다.
	 * 데이터로는 위에서 만든 리스트인 list2을 넣어주었다.
	 * simple_list_item_1과 달리 해시맵의 키값이 from배열과(변경 가능)
	 * id을 지정할 to배열이 필요하다.
	 */
	private void viewList() {
		String[] from = { "line1", "line2" };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list2,
				android.R.layout.simple_list_item_2, from, to);
		
		listView.setAdapter(simpleAdapter);
	}


}
