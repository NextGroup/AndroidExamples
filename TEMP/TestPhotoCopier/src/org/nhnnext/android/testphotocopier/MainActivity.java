package org.nhnnext.android.testphotocopier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button buCopy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buCopy = (Button) findViewById(R.id.button1);

		buCopy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fileSave("photo1.jpg");
				fileSave("photo2.jpg");
				fileSave("photo3.jpg");
				fileSave("photo4.jpg");
				fileSave("photo5.jpg");
				
				forceMediaScan();
				
			}
		});
	}

	private void fileSave(String fileName) {
		InputStream is = null;
		FileOutputStream fos = null;
		File outDir = new File(Environment.getExternalStorageDirectory().toString() + "/NEXTAGRAM");
		
		if ( !outDir.exists() ) {
			outDir.mkdirs();
        }
		
		try {
			is = getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			File outfile = new File(outDir + "/" + fileName);
			fos = new FileOutputStream(outfile);
			for (int c = is.read(buffer); c != -1; c = is.read(buffer)) {
				fos.write(buffer, 0, c);
			}
			is.close();
			fos.close();
			Toast.makeText(this, "file save success ! ", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, "file copy error :"+e, Toast.LENGTH_SHORT).show();
		}
	}

	
	private void forceMediaScan() {
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                .parse("file://" + Environment.getExternalStorageDirectory())));

	}
}
