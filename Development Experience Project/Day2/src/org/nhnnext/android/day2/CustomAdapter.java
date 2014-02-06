package org.nhnnext.android.day2;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Professor> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<Professor> profData;

	/*
	 * step3
	 * 커스텀 어댑터는 컨택스트정보, 레이아웃Id, 리스트에 표시할 데이터가 필요합니다.
	 * 레이아Id는 리스트의 칸 하나의 레이아웃을 구성하는 것으로 이 예제에서는
	 * res/layout.custom_list_row (R.layout.custom_list_row)을 사용합니다.
	 * 커스텀 어댑터는 이렇게 자신이 직접 레이아웃을 만들 수 있습니다.
	 */
	public CustomAdapter(Context context, int layoutResourceId, ArrayList<Professor> profData) {
		super(context, layoutResourceId, profData);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.profData = profData;
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
		tvName.setText(profData.get(position).getName());
		tvVillage.setText(profData.get(position).getVillage());

        //이미지를 읽어와 리스트에 표시해 주는 것으로 여기에서는 assets폴더에 사진을 집어넣고 가져오는 방법을 사용합니다.
        try {
        	//이미지 파일의 이름을 가져옵니다.
            InputStream ims = context.getAssets().open(profData.get(position).getImgPath());
            //이미지를 불러와 Drawable로 만들고
            Drawable d = Drawable.createFromStream(ims, null);
            //이미지 뷰에 표시해줍니다.
            imageView.setImageDrawable(d);
        }
        catch(IOException e) {
            Log.e("ERROR", "ERROR:"+e);
        }
        
		return row;
	}

}