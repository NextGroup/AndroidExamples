package org.nhnnext.android.androidservice;
import java.io.File;
import java.util.ArrayList;

import org.nhnnext.android.androidservice.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeViewAdapter extends ArrayAdapter<ArticleDTO> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<ArticleDTO> articleData;

	/*
	 * step3
	 * 커스텀 어댑터는 컨택스트정보, 레이아웃Id, 리스트에 표시할 데이터가 필요합니다.
	 * 레이아Id는 리스트의 칸 하나의 레이아웃을 구성하는 것으로 이 예제에서는
	 * res/layout.custom_list_row (R.layout.custom_list_row)을 사용합니다.
	 * 커스텀 어댑터는 이렇게 자신이 직접 레이아웃을 만들 수 있습니다.
	 */
	public HomeViewAdapter(Context context, int layoutResourceId, ArrayList<ArticleDTO> articleData) {
		super(context, layoutResourceId, articleData);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.articleData = articleData;
	}

	/*
	 * step4
	 * getView를 오버라이딩하여 리스트가 어떻게 보여지게 될 지를 정의합니다.  
	 * 리스트의 각 칸이 보일때 res/layout.custom_list_row를 inflate하여
	 * 데이터를 표시해 줍니다.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//row는 리스트의 각각의 칸이됩니다.
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		}

		//row.findViewById로 row안의 레이아웃을 설정합니다.
		ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView1);
		TextView tvName = (TextView) row.findViewById(R.id.custom_row_textView1);
		TextView tvVillage = (TextView) row.findViewById(R.id.custom_row_textView2);

		//int position은 리스트의 순서값으로 profData리스트에서 Professor객체를 가져와 get을 이용해 데이터를 가져와 setText해줍니다.
		tvName.setText(articleData.get(position).getWriter());
		tvVillage.setText(articleData.get(position).getTitle());
		
    	String img_path = HomeView.FILES_DIR + articleData.get(position).getImgName();
        File img_load_path = new File(img_path);
        
        if (img_load_path.exists()) {
        	
        	BitmapFactory.Options options = new BitmapFactory.Options();
  			options.inPurgeable = true;
  			
        	Bitmap bitmap = BitmapFactory.decodeFile(img_path, options);
        	Util util = new Util();
 			imageView.setImageBitmap(util.resizeBitmapImage(bitmap,HomeView.displayW/4));
			bitmap.recycle();
			bitmap = null;
			
		}
        
        row.setTag(articleData.get(position).getArticleNumber());
        
		return row;
	}

}