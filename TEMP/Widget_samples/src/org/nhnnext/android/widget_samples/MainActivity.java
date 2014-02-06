package org.nhnnext.android.widget_samples;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button bu3;
	private Button bu4;
	private Button bu5;
	private Button bu6;
	private Button bu7;
	private Button bu8;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day1_main);
		bu3 = (Button)findViewById(R.id.main_button_textview);
		bu4 = (Button)findViewById(R.id.main_button_edit_text);
		bu5 = (Button)findViewById(R.id.main_button_button);
		bu6 = (Button)findViewById(R.id.main_button_imageButton);
		bu7 = (Button)findViewById(R.id.main_button_imageView);
		bu8 = (Button)findViewById(R.id.main_button_webView);
		
		bu3.setOnClickListener(this);
		bu4.setOnClickListener(this);
		bu5.setOnClickListener(this);
		bu6.setOnClickListener(this);
		bu7.setOnClickListener(this);
		bu8.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		
		switch(view.getId()) {
		case R.id.main_button_textview:
			intent.setClass(MainActivity.this, TextViewSample.class);
			break;
		case R.id.main_button_edit_text:
			intent.setClass(MainActivity.this, EditTextSample.class);
			break;
		case R.id.main_button_button:
			intent.setClass(MainActivity.this, ButtonSample.class);
			break;
		case R.id.main_button_imageButton:
			intent.setClass(MainActivity.this, ImageButtonSample.class);
			break;
		case R.id.main_button_imageView:
			intent.setClass(MainActivity.this, ImageViewSample.class);
			break;
		case R.id.main_button_webView:
			intent.setClass(MainActivity.this, WebViewSample.class);
			break;
		}
		
		startActivity(intent);
		
	}

}
