package org.nhnnext.android.day1;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;

public class EditTextSample extends Activity implements OnClickListener {

	private Toaster toaster;
	
	private Button button1;
	private Button button2;
	
	private EditText editText1;
	
	private int touchCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittext_sample);
		
		toaster = new Toaster(getApplicationContext());
		
		button1 = (Button)findViewById(R.id.ets_button_getText);
		button2 = (Button)findViewById(R.id.ets_editText_setText);
		
		editText1 = (EditText)findViewById(R.id.ets_editText_plain);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.ets_button_getText:
			toaster.toastMake(editText1.getText().toString());
			break;
		case R.id.ets_editText_setText:
			++touchCount;
			editText1.setText(touchCount+"번 터치하셨습니다.");
			break;
		}
	}

}
