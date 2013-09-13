package org.nhnnext.android.day1;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class TextViewSample extends Activity implements OnClickListener {

	private Toaster toaster;
	
	private Button button1;
	private Button button2;
	
	private TextView textView1;
	
	private int touchCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview_sample);
		
		toaster = new Toaster(getApplicationContext());
		
		button1 = (Button)findViewById(R.id.tv_button_getText);
		button2 = (Button)findViewById(R.id.tv_button_setText);
		
		textView1 = (TextView)findViewById(R.id.tv_textView_plain);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.tv_button_getText:
			toaster.toastMake(textView1.getText().toString());
			break;
		case R.id.tv_button_setText:
			++touchCount;
			textView1.setText(touchCount+"번 터치하셨습니다.");
			break;
		}
	}

}
