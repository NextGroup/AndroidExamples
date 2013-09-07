package org.nhnnext.android.basiclistview;
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

	public CustomAdapter(Context context, int layoutResourceId, ArrayList<Professor> profData) {
		super(context, layoutResourceId, profData);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.profData = profData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		}

		// init view
		ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView1);
		TextView tvName = (TextView) row.findViewById(R.id.custom_row_textView1);
		TextView tvVillage = (TextView) row.findViewById(R.id.custom_row_textView2);

		tvName.setText(profData.get(position).getName());
		tvVillage.setText(profData.get(position).getVillage());

        // load image
        try {
            InputStream ims = context.getAssets().open(profData.get(position).getImgPath());
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        }
        catch(IOException e) {
            Log.e("ERROR", "ERROR:"+e);
        }
        
		return row;
	}

}