package com.example.actionbartest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class MainActivity extends ActionBarActivity {

	private SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
		// 사이드 네비게이션의 메뉴가 들어있는 위치를 설정합니다.
		sideNavigationView.setMenuItems(R.menu.side_menu);
		// 콜백함수를 지정합니다.
		sideNavigationView.setMenuClickCallback(sideNavigationCallback);
		// 네비게이션이 왼쪽에서 나올지, 오른쪽에서 나올지를 선택합니다.
		sideNavigationView.setMode(Mode.LEFT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    /*
     * 액션바가 터치되었을 경우 이벤트가 돌아옵니다.
     * id로 구분하여 원하는 내용을 지정 할 수 있습니다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            String text = "";
            
            switch(item.getItemId()){
    		case android.R.id.home:
    			text = "Side Navigation toggle";
    			sideNavigationView.toggleMenu();
            case R.id.action_item_add:
                    text = "Action item, with text, displayed if room exists";
                    break;
            case R.id.action_item_search:
                    text = "Action item, icon only, always displayed";
                    break;
            case R.id.action_item_normal:
                    text = "Normal menu item";
                    break;
            default:
                    return false;
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return true;
    }
    

	// 사이드 네비게이션의 이벤트 처리
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
			Toast.makeText(getApplicationContext(), "side menu:" + text,
					Toast.LENGTH_SHORT).show();
		}

	};
}
