package org.nhnnext.android.basic;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeViewAdapter extends CursorAdapter {
	private Context context;
	private int layoutResourceId;
	private LayoutInflater mLayoutInflater;

	// private ArrayList<ArticleDTO> articleData;
	private Cursor cursor;
	private SharedPreferences pref;

	/*
	 * step3 커스텀 어댑터는 컨택스트정보, 레이아웃Id, 리스트에 표시할 데이터가 필요합니다. 레이아Id는 리스트의 칸 하나의
	 * 레이아웃을 구성하는 것으로 이 예제에서는 res/layout.custom_list_row
	 * (R.layout.custom_list_row)을 사용합니다. 커스텀 어댑터는 이렇게 자신이 직접 레이아웃을 만들 수 있습니다.
	 */

	public HomeViewAdapter(Context context, Cursor c, int layoutId) {
		super(context, c, true);
		this.context = context;
		this.layoutResourceId = layoutId;
		this.cursor = c;
		pref = context.getSharedPreferences(
				context.getString(R.string.pref_name), context.MODE_PRIVATE);
		mLayoutInflater = LayoutInflater.from(context);
	}

	static class ViewHolderItem {
		ImageView imageView;
		TextView tvWriter;
		TextView tvTitle;
		String articleNumber;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View row = mLayoutInflater.inflate(layoutResourceId, parent, false);
		ViewHolderItem viewHolder;
		viewHolder = new ViewHolderItem();
		viewHolder.imageView = (ImageView) row
				.findViewById(R.id.custom_row_imageView1);
		viewHolder.tvTitle = (TextView) row
				.findViewById(R.id.custom_row_textView1);
		viewHolder.tvWriter = (TextView) row
				.findViewById(R.id.custom_row_textView2);
		row.setTag(viewHolder);

		return row;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String writer = cursor.getString(cursor
				.getColumnIndex(NextgramContract.Articles.WRITER));
		String title = cursor.getString(cursor
				.getColumnIndex(NextgramContract.Articles.TITLE));
		String imgName = cursor.getString(cursor
				.getColumnIndex(NextgramContract.Articles.IMAGE_NAME));
		String articleNumber = cursor.getString(cursor
				.getColumnIndex(NextgramContract.Articles._ID));
		
		ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
		viewHolder.articleNumber = articleNumber;
		viewHolder.tvWriter.setText(writer);
		viewHolder.tvTitle.setText(title);

		String img_path = pref.getString(
				context.getString(R.string.files_directory), "")
				+ imgName;
		File img_load_path = new File(img_path);

		if (img_load_path.exists()) {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(img_path, options);
			Util util = new Util();
			viewHolder.imageView.setImageBitmap(util
					.resizeBitmapImage(bitmap, pref.getInt(
							context.getString(R.string.display_width), 1) / 4));

			// 아래의 두 명령은 사용하지 않습니다.
			// bitmap.recycle()
			// bitmap=null
		}

	}

}