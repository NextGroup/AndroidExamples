package org.nhnnext.android.day4;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BasicActionBar extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bar_action, menu);
		return true;
	}
	
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
