package org.nhnnext.android.day1;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
	private Context context;

	Toaster(Context context) {
		this.context = context;
	}

	public void toastMake(String bread) {
		Toast toast = Toast.makeText(context, bread, Toast.LENGTH_SHORT);
		toast.show();
	}

}
