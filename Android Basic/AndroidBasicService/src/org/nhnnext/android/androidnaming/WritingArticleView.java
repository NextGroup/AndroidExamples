package org.nhnnext.android.androidnaming;


import java.io.File;
import java.io.FileOutputStream;

import org.nhnnext.android.androidservice.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class WritingArticleView extends Activity {

	private static final int REQUEST_PHOTO_ALBUM = 1;
	
	private EditText etWriter;
	private EditText etTitle;
	private EditText etContent;
	private ImageButton ibPhoto;
	
	private ProgressDialog progressDialog;
	
	private String filePath="";
	private String fileName="";
	
	private Bitmap bitmap;
	private Bitmap bitmap2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_article);
		
		etWriter = (EditText)findViewById(R.id.write_article_editText_writer);
		etTitle = (EditText)findViewById(R.id.write_article_editText_title);
		etContent = (EditText)findViewById(R.id.write_article_editText_content);
		
		ibPhoto = (ImageButton)findViewById(R.id.write_article_imageButton_photo);
		
		ibPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//photo Album 호출 intent 생성
		  		Intent intent = new Intent(Intent.ACTION_PICK);
		  		
		  		intent.setType(Images.Media.CONTENT_TYPE);
		  		intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
		  		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
			}
		});
		
		Button buUpload = (Button) findViewById(R.id.write_article_button1);
		buUpload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Handler handler = new Handler();
				
				new Thread() {
					public void run() {
						
						handler.post(new Runnable() {
							public void run() {
								progressDialog = ProgressDialog.show(WritingArticleView.this, "","업로드 중입니다.");
							}
						});

						ArticleDTO article = new ArticleDTO(0,
								etTitle.getText().toString(),
								etWriter.getText().toString(),
								HomeView.DEVICE_ID,
								etContent.getText().toString(),
								Util.getDate(),
								fileName);
						
			        	ArticleWritingProxy proxyUP = new ArticleWritingProxy();
			        	proxyUP.uploadArticle(article, filePath);
			        	
						handler.post(new Runnable() {
							public void run() {
								progressDialog.cancel();
								
								//
								//
								//
								
					        	finish();
							}
						});
			        	
	        	
					}
				}.start();
			}
		});
	}
	
	
	
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  		super.onActivityResult(requestCode, resultCode, data);
  		
  		if(resultCode != RESULT_OK)
  			return;
  		
  		if(requestCode == REQUEST_PHOTO_ALBUM) {
  			
  	        
  			Uri uri= Util.getRealPathUri(getApplicationContext(), data.getData());
  			filePath = uri.toString();
  			fileName = uri.getLastPathSegment();
  			
  			Log.i("test", "uri1:"+uri.getPath());
  			Log.i("test", "uri2:"+uri.getLastPathSegment());
  			Log.i("test", "uri3:"+uri.toString());
  			
  			recycleBitmap();
  			
  			
  			int sampleSize = Util.getSampleSize(uri.toString());
  			BitmapFactory.Options options = new BitmapFactory.Options();
  			options.inPurgeable = true;
  			options.inSampleSize = sampleSize;
			
		
			
			
			try {
				
				bitmap = BitmapFactory.decodeFile(uri.toString(), options);
				
				Util util = new Util();
				bitmap = util.resizeBitmapImage(bitmap,HomeView.displayW);
				
				File file = new File(HomeView.FILES_DIR+".temp.jpg");
				
				if(file.exists()) {
					file.delete();
				}
				
				FileOutputStream fos = new FileOutputStream(file);
				
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
				filePath = HomeView.FILES_DIR+".temp.jpg";
				Log.i("test","save Comp");
			} catch (Exception e) {
				Log.e("test", "OutOfMemoryError:"+e);
				bitmap.recycle();
				bitmap = null;
				e.printStackTrace();
			}
			
			//bitmap = null;
			
			bitmap2 = BitmapFactory.decodeFile(filePath);
			ibPhoto.setImageBitmap(bitmap2);
			
			//bitmap2 = null;
  		}
  	}
  	
  	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ibPhoto.setImageDrawable(null);
		Log.i("test", "viewer STOP");
		recycleBitmap();

	}
	
	private void recycleBitmap() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}
	}
  	
}