package org.linphone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebFragment extends Fragment implements OnClickListener {
	
	private LayoutInflater mInflater;
	private WebView webView;
	
	public static String WEBLINK = "weblink";
	
	private TextView backbtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        if (savedInstanceState != null) {
        	webView.restoreState(savedInstanceState);
        }
        
		mInflater = inflater;
		View view = inflater.inflate(R.layout.web, container, false);
		
		backbtn = (TextView)view.findViewById(R.id.backbtn);		
		if(LinphoneActivity.instance().onTablet()){
			backbtn.setVisibility(View.INVISIBLE);
		}else{
			backbtn.setOnClickListener(this);
		}
		
		webView = (WebView) view.findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        view.loadUrl(url);
		        return false;
		   }
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		
		String weburl = getArguments().getString(WEBLINK);
		webView.loadUrl(weburl);
		return view;
	}
	
	public void updateweburl(String url){
		webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        view.loadUrl(url);
		        return false;
		   }
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.loadUrl(url);
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		
		if (LinphoneActivity.isInstanciated()) {
//			LinphoneActivity.instance().selectMenu(FragmentsAvailable.SETTINGS);
			
			if (getResources().getBoolean(R.bool.show_statusbar_only_on_dialer)) {
				LinphoneActivity.instance().hideStatusBar();
			}
		}
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backbtn){
			getFragmentManager().popBackStackImmediate();
		}
		
	}
    
}
