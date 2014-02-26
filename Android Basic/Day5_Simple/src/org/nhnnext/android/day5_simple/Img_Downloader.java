/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nhnnext.android.day5_simple;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;


public class Img_Downloader {
	private final Context context;

	// 임시로 전에 만들었던 클래스 붙여서 테스트중임
	public Img_Downloader(Context context) {
		this.context = context;
	}

	public void copy_img(String url, String save_name) {

		File img_cache_path;

		img_cache_path = new File(HomeViewer.FILES_DIR + save_name);
		if (!img_cache_path.exists()) {

			// ************************************
			try {
				URL Url = new URL(url);
				URLConnection conn = Url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();

				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(50);

				int current = 0;

				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				FileOutputStream fos = context.openFileOutput(save_name, 0);
				fos.write(baf.toByteArray());
				fos.close();
			} catch (IOException e) {

			}

		}

	}

}
