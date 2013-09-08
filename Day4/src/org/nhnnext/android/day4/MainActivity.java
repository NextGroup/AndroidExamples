package org.nhnnext.android.day4;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button bu1;
	private Button bu2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bu1 =  (Button)findViewById(R.id.main_button1);
		bu2 =  (Button)findViewById(R.id.main_button2);
		
		bu1.setOnClickListener(this);
		bu2.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.main_button1:
			Intent intentSimpleList1 = new Intent();
			intentSimpleList1.setClass(MainActivity.this, BasicActionBar.class);
    		startActivity(intentSimpleList1);
			break;
		case R.id.main_button2:
			Intent intentSimpleList2 = new Intent();
			intentSimpleList2.setClass(MainActivity.this, BasicActionBar.class);
    		startActivity(intentSimpleList2);
			break;
		}
		
	}

}
