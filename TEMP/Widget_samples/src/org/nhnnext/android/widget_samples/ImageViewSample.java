package org.nhnnext.android.widget_samples;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public class ImageViewSample extends Activity implements OnClickListener {

	private Button button1;

	private ImageView imageView1;

	private boolean changeImage = true;
	private String imgPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view_sample);

		button1 = (Button) findViewById(R.id.imv_button1);

		imageView1 = (ImageView) findViewById(R.id.imv_imageView1);

		button1.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		if (changeImage) {
			changeImage = false;
			imgPath = "gyunbin.jpg";
		} else {
			changeImage = true;
			imgPath = "singer.jpg";
		}

		switch (view.getId()) {

		case R.id.imv_button1:

			// 이미지를 불러와 Drawable로 만들고
			InputStream ims;
			try {
				ims = getAssets().open(imgPath);
				Drawable d = Drawable.createFromStream(ims, null);

				// 이미지 뷰에 표시해줍니다.
				imageView1.setImageDrawable(d);
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		}
	}

}
