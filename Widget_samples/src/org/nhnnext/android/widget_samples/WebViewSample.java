package org.nhnnext.android.widget_samples;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;

public class WebViewSample extends Activity {
	
	private WebView webView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_sample);
		
		webView1 = (WebView) findViewById(R.id.wv_webView1);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.loadUrl("http://m.blog.naver.com/nhnnext");
		webView1.setWebViewClient(new MyWebViewClient());
		
	}
	
	//스마트폰의 백버튼을 누르면 액티비티가 종료되지 않고 웹뷰가 뒤로 가도록 오버라이딩
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
			webView1.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//웹뷰 내부의 url을 이동
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
