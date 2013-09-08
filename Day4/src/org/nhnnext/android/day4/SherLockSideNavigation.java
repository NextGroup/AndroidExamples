package org.nhnnext.android.day4;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SherLockSideNavigation extends Activity {

	private SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_side_navigaion);
		
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	    sideNavigationView.setMenuItems(R.menu.side_menu);
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	    sideNavigationView.setMode(Mode.LEFT);

	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
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
	
	ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {

		@Override
		public void onSideNavigationItemClick(int itemId) {
			// Validation clicking on side navigation item
			String text = "";
			switch (itemId) {
			case R.id.side_navigation_menu_item1:
				text = "1";
				break;
			case R.id.side_navigation_menu_item2:
				text = "2";
				break;
			case R.id.side_navigation_menu_item3:
				text = "3";
				break;
			case R.id.side_navigation_menu_item4:
				text = "4";
				break;
			case R.id.side_navigation_menu_item5:
				text = "5";
				break;
			default:
				text = "";
			}
			Toast.makeText(getApplicationContext(), "side menu:"+text, Toast.LENGTH_SHORT).show();
		}
		
	};
	
}
