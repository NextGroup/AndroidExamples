package org.nhnnext.android.day5_simple;



import java.io.File;

import org.nhnnext.android.day5_simple.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ArticleViewer extends Activity {
	
	private Bitmap bitmap;
	private NextgramController mainController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_article);
		
		mainController = NextgramController.getInstance();
		
		TextView tvTitle = (TextView)findViewById(R.id.view_article_textView_title);
		TextView tvWriter = (TextView)findViewById(R.id.view_article_textView_writer);
		TextView tvContent = (TextView)findViewById(R.id.view_article_textView_content);
		TextView tvWriteDate = (TextView)findViewById(R.id.view_article_textView_write_time);
		
		ImageView ivImage = (ImageView)findViewById(R.id.view_article_imageView_photo);
		
		String articleNumber = getIntent().getExtras().getString("ArticleNumber");
		
		
		ArticleDTO article = mainController.getArticle(Integer.parseInt(articleNumber));
		
		tvTitle.setText(article.getTitle());
		tvWriter.setText(article.getWriter());
		tvContent.setText(article.getContent());
		tvWriteDate.setText(article.getWriteDate());
		
		String img_path = NextgramController.FILES_DIR + article.getImgName();
        File img_load_path = new File(img_path);
        
        if (img_load_path.exists()) {
        	
        	int sampleSize = Util.getSampleSize(img_path);
        	
  			BitmapFactory.Options options = new BitmapFactory.Options();
  			options.inPurgeable = true;
  			options.inSampleSize = sampleSize;
  			
  			
        	bitmap = BitmapFactory.decodeFile(img_path, options);
			Util util = new Util();
			ivImage.setImageBitmap(util.resizeBitmapImage(bitmap,NextgramController.displayW));
			
		}
        
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("test", "viewer STOP");
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

	}
	
}
