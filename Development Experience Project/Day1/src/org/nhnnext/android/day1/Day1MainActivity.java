package org.nhnnext.android.day1;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class Day1MainActivity extends Activity implements OnClickListener {

	private Button bu1;
	private Button bu2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day1_main);
		bu1 = (Button)findViewById(R.id.main_button_linear);
		bu2 = (Button)findViewById(R.id.main_button_relative);
		
		bu1.setOnClickListener(this);
		bu2.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		
		switch(view.getId()) {
		case R.id.main_button_linear:
			intent.setClass(Day1MainActivity.this, LinearLayoutSample.class);
			break;
		case R.id.main_button_relative:
			intent.setClass(Day1MainActivity.this, RelativeLayoutSample.class);
			break;
		}
		
		startActivity(intent);
		
	}

}
