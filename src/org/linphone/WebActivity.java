package org.linphone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends Activity implements OnClickListener{
	private WebView webView;
	private TextView backbtn;
	
	public static String WEBLINK = "weblink";
	 
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		
		backbtn = (TextView) findViewById(R.id.backbtn);
		backbtn.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.webview);
		
		String weburl = this.getIntent().getStringExtra(WEBLINK);
		webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        view.loadUrl(url);
		        return false;
		   }
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);		
		webView.loadUrl(weburl);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backbtn){
			finish();
		}
		
	}
}
