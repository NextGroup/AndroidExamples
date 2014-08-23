package org.nhnnext.android.basic;



import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleView extends Activity {
	
	private Bitmap bitmap;
	private SharedPreferences pref;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_article);
		
		context = getApplicationContext();
		
		pref = context.getSharedPreferences(context.getString(R.string.pref_name), context.MODE_PRIVATE);
		
		ProviderDao dao = new ProviderDao(getApplicationContext());
		
		
		TextView tvTitle = (TextView)findViewById(R.id.view_article_textView_title);
		TextView tvWriter = (TextView)findViewById(R.id.view_article_textView_writer);
		TextView tvContent = (TextView)findViewById(R.id.view_article_textView_content);
		TextView tvWriteDate = (TextView)findViewById(R.id.view_article_textView_write_time);
		
		ImageView ivImage = (ImageView)findViewById(R.id.view_article_imageView_photo);
		
		String articleNumber = getIntent().getExtras().getString("ArticleNumber");
		
		
		ArticleDTO article = dao.getArticle(Integer.parseInt(articleNumber));
		
		tvTitle.setText(article.getTitle());
		tvWriter.setText(article.getWriter());
		tvContent.setText(article.getContent());
		tvWriteDate.setText(article.getWriteDate());
		
		String img_path = pref.getString(context.getString(R.string.files_directory), "") + article.getImgName();
        File img_load_path = new File(img_path);
        
        if (img_load_path.exists()) {
        	
        	int sampleSize = Util.getOutWidthSize(img_path)/pref.getInt(context.getString(R.string.display_width), 0);
        	
  			BitmapFactory.Options options = new BitmapFactory.Options();
  			options.inPurgeable = true;
  			options.inSampleSize = sampleSize;
  			
  			
        	bitmap = BitmapFactory.decodeFile(img_path, options);
			Util util = new Util();
			ivImage.setImageBitmap(util.resizeBitmapImage(bitmap, pref.getInt(context.getString(R.string.display_width), 0)));
			
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
