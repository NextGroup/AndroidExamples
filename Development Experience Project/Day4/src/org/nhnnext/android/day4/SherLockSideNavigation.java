package org.nhnnext.android.day4;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * ActionBarSherlock을 이용하여 사이드 네비게이션을 이용하는 예제입니다.
 * 
 * ActionBarSherlock과 이를 사용하기 쉽게 만들어 주는 오픈소스 두개를 같이 이용해서 구현합니다.
 * 
 * 먼저 
 * http://actionbarsherlock.com/
 * 에서 ActionBarSherlock을 다운받아 압축을 푼 후
 * 
 * 이클립스 File - New - Other - Android Project From Existing Code을 선택하여
 * 압축을 푼 폴더안에 있는 actionbarsherlock을 불러온다.
 * 
 * 그 다음
 * https://github.com/johnkil/SideNavigation
 * 의 소스에서 library를 위의 방법으로 이클립스로 불러온다.
 * 
 * 마지막으로 작업하는 프로젝트의 Properties-Android-Library에서 Add를 눌러
 * 위에서 불러온 2개의 프로젝트를 선택을 하여 추가하면 기본 설정이 완료된다.
 * 
 */
public class SherLockSideNavigation extends Activity {
	/*
	 * Step1
	 * 사이드 네비게이션에 들어갈 내용을 res/menu/side_menu 정의합니다.(R.menu.side_menu)
	 * 
	 * Step2
	 * res/layout/activity_side_navigaion에 사이드 네비게이션을 정의해야합니다. (R.layout.activity_side_navigaion)
	 * (com.devspark.sidenavigation.SideNavigationView)
	 * 
	 * Step3
	 * 사이드 네비게이션을 초기화 합니다.
	 */
	private SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_side_navigaion);
		
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
		//사이드 네비게이션의 메뉴가 들어있는 위치를 설정합니다.
	    sideNavigationView.setMenuItems(R.menu.side_menu);
	    //콜백함수를 지정합니다.
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	    //네비게이션이 왼쪽에서 나올지, 오른쪽에서 나올지를 선택합니다.
	    sideNavigationView.setMode(Mode.LEFT);
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	}
	
	//일반 액션바
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bar_action, menu);
		return true;
	}
	
	//일반 액션바의 이벤트 처리
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String text = "";
		
		switch(item.getItemId()){
		
		case android.R.id.home:
			text = "Side Navigation toggle";
	        sideNavigationView.toggleMenu();
	        break;
	        
		case R.id.action_item1:
			text = "Action item, with text, displayed if room exists";
			break;
			
		case R.id.action_item2:
			text = "Action item, icon only, always displayed";
			break;
			
		case R.id.action_item3:
			text = "Normal menu item";
			break;
			
		default:
			return false;
		}
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
		return true;
	}
	
	
	//Step4 사이드 네비게이션의 이벤트 처리
	ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {

		@Override
		public void onSideNavigationItemClick(int itemId) {
			// Validation clicking on side navigation item
			String text = "";
			switch (itemId) {
			case R.id.side_navigation_menu_add:
				text = "add";
				break;
			case R.id.side_navigation_menu_call:
				text = "call";
				break;
			case R.id.side_navigation_menu_camera:
				text = "camera";
				break;
			case R.id.side_navigation_menu_delete:
				text = "delete";
				break;
			case R.id.side_navigation_menu_text:
				text = "text";
				break;
			default:
				text = "";
			}
			Toast.makeText(getApplicationContext(), "side menu:"+text, Toast.LENGTH_SHORT).show();
		}
		
	};
	
}
