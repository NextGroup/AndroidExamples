package org.nhnnext.android.day5_simple;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class Util {

	public static String getMD5Hash(String s) {
		MessageDigest m = null;
		String hash = null;

		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			hash = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public static String getDate() {
		Date date = new Date();
		return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
				.format(date);
	}

	public static long getMilliTime() {
		return System.currentTimeMillis();
	}

	public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
		int width = source.getWidth();
		int height = source.getHeight();
		int newWidth = width;
		int newHeight = height;
		float rate = 0.0f;

		if (width > height) {
			if (maxResolution < width) {
				rate = maxResolution / (float) width;
				newHeight = (int) (height * rate);
				newWidth = maxResolution;
			}
		} else {
			if (maxResolution < height) {
				rate = maxResolution / (float) height;
				newWidth = (int) (width * rate);
				newHeight = maxResolution;
			}
		}
		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	}

	public static Uri getRealPathUri(Context context, Uri uri) {
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = context.getContentResolver().query(uri, null, null,
					null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(cursor.getString(column_index));
			}
		}
		return filePathUri;
	}

	public static int getSampleSize(String bitmapPath) {
		BitmapFactory.Options optionsCheck = new BitmapFactory.Options();
		optionsCheck.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapPath, optionsCheck);

		return optionsCheck.outWidth / NextgramController.displayW;
	}

}
