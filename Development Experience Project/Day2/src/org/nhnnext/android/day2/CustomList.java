package org.nhnnext.android.day2;

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
						 "06.jpg", "07.jpg", "08.jpg", "09.jpg", "10.jpg",
						 "11.jpg", "12.jpg", "13.jpg", "14.jpg", "15.jpg",
						 "16.jpg", "17.jpg", "18.jpg", "19.jpg", "20.jpg",
						 "21.jpg", "22.jpg" };


	private ListView listView;
	ArrayList<Professor> listCustom = new ArrayList<Professor>();
	
	/*
	 * 안드로이드에서 기본 제공해주는 어댑터 외에
	 * 사진을 집어넣는 다던지 다른 효과들을 집어 넣을려면
	 * 직접 어댑터를 만들어서 구현을 해야 합니다.
	 * 이 예제에서는 글 2줄과 이미지를 표시하는 리스트를 구현합니다.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		listView =  (ListView)findViewById(R.id.listView1);
		
		setData();
		viewList();
	}
	
	/*
	 * step1
	 * 데이터를 한 종류의 객체로 모아주는 과정
	 */
	private void setData() {
		for (int i = 0; i < names.length; i++) {
			Professor prof = new Professor(names[i], village[i], imgPath[i]);
			listCustom.add(prof);
		}
	}
	
	/*
	 * step2
	 * 직접 만든 커스텀 어댑터를 적용하는 과정
	 * CustomAdapter의 소스를 봐야합니다.
	 */
	private void viewList() {
		CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, listCustom);
		listView.setAdapter(customAdapter);
	}


}
