package org.nhnnext.android.day4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BasicActionBar extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}
	
	/*
	 * Step1
	 * 액션바에 들어갈 내용을 res/menu/bar_action에 정의합니다.(R.menu.bar_action)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bar_action, menu);
		return true;
	}
	
	/*
	 * Step2
	 * 액션바가 터치되었을 경우 이벤트가 돌아옵니다.
	 * id로 구분하여 원하는 내용을 지정 할 수 있습니다.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String text = "";
		
		switch(item.getItemId()){
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
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return true;
	}
}
