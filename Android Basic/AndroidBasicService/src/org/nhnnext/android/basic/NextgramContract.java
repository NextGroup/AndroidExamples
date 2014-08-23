package org.nhnnext.android.basic;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NextgramContract {
	/**
	 * Nextgram Provider의 AUTHORITY
	 */
	public static final String AUTHORITY = "org.nhnnext.nextgram.provider";

	/**
	 * 최상위 아이템의 AUTHORITY를 위한 CONTENT URI
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public static final class Articles implements BaseColumns {
		
		public static final String _ID = "_id";
		public static final String TITLE = "Title";
		public static final String WRITER = "Writer";
		public static final String ID = "Id";
		public static final String CONTENT = "Content";
		public static final String WRITER_DATE = "WriteDate";
		public static final String IMAGE_NAME = "ImgName";
		/**
		 * The content URI for this table.
		 */
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				NextgramContract.CONTENT_URI, Articles.class.getSimpleName());

		public static final String[] PROJECTION_ALL = { _ID, TITLE, WRITER, ID, CONTENT, WRITER_DATE, IMAGE_NAME };
		
		/**
		 * _ID를 기준으로 정렬이 기본으로 설정. 
		 */
		public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
	}

}
