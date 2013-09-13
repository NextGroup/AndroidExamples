package org.nhnnext.android.day1;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class ButtonSample extends Activity implements OnClickListener {

	private Toaster toaster;

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;

	private int touchCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button_sample);

		toaster = new Toaster(getApplicationContext());

		button1 = (Button) findViewById(R.id.bu_button1);
		button2 = (Button) findViewById(R.id.bu_button2);
		button3 = (Button) findViewById(R.id.bu_button3);
		button4 = (Button) findViewById(R.id.bu_button4);

		//OnClickListener를 implements해서 사용
		button1.setOnClickListener(this);
		
		//직접 OnClickListener를 생성1
		button2.setOnClickListener(mClickListener);

		//직접 OnClickListener를 생성2
		button3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				toaster.toastMake("==bu3==");
			}

		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bu_button1:
			toaster.toastMake("==bu1==");
			break;
		}
	}
	
	
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.bu_button2:
				toaster.toastMake("==bu2==");
				break;
			}
		}
	};
	
	
	public void onClickFromXML(View view) {
		toaster.toastMake("==bu4==");
	}

}
