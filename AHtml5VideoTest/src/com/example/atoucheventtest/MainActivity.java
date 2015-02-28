package com.example.atoucheventtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

 
public class MainActivity extends Activity {
	 
	private FrameLayout fra_root;
	private WebView mWebView;
	 
	private MyWebChromeClient chromeClient;
	private WebChromeClient.CustomViewCallback myCallBack = null;
	private View mCustomView = null;
	private FrameLayout mFullscreenContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main );
		initWidget();
	}

	@SuppressLint("NewApi")
	private void initWidget() {
		fra_root = (FrameLayout)findViewById( R.id.dynrsswebview_webview_root);
		mWebView = (WebView)findViewById( R.id.dynrsswebview_webView );
 
		mWebView.clearCache(true);
		mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);

		WebSettings settings = mWebView.getSettings();
		settings.setPluginState(PluginState.ON);
 		settings.setPluginsEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
 		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setAllowFileAccess(true);
		 

		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.clearCache(true);
		mWebView.setWebViewClient(new MyWebViewClient());
		chromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(chromeClient);
		mWebView.loadUrl("http://v.youku.com/player/getM3U8/vid/63337937/type/mp4/sid/130102475520012/K/fb9bf1a9fad2f18c182699ea/video.m3u8");
	}

	class MyWebChromeClient extends WebChromeClient {
		private int mOriginalOrientation = 1;
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			onShowCustomView(view, mOriginalOrientation, callback);
			super.onShowCustomView(view, callback);
		}

		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			//if ( HQCHApplication.SDK >= 14) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			mFullscreenContainer.addView(view);
			mCustomView = view;
			myCallBack = callback;
			mOriginalOrientation = getRequestedOrientation();
			fra_root.setVisibility(View.GONE);
			mFullscreenContainer.setVisibility(View.VISIBLE);
			mFullscreenContainer.bringToFront();
			 
			mFullscreenContainer.requestFocus();
		 	setRequestedOrientation(mOriginalOrientation);
			// }

		}

		public void onHideCustomView() {
			quitFullScreen();
			fra_root.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomView);
			mCustomView = null;
			mFullscreenContainer.setVisibility(View.GONE);
			try {
				myCallBack.onCustomViewHidden();
			} catch (Exception e) {
			}
			setRequestedOrientation(mOriginalOrientation);	// Show the content view.
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	private void quitFullScreen(){
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
  
 
	@Override
	protected void onDestroy() {
		if ( mCustomView != null ) {
			mCustomView = null;
		}
		if ( mWebView != null) {
			chromeClient.onHideCustomView();
			mWebView.clearCache(true);
			mWebView.onPause();
			mWebView.destroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mWebView.saveState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	private void back() {
		if(mCustomView == null){
			super.onBackPressed();
		} else{
			chromeClient.onHideCustomView();
		}
	}

	protected void editSendSms(String phoneNumber, String content) {
		try {
			String strMessage = content;
			String uriString = "smsto:" + phoneNumber;
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", strMessage);
			MainActivity.this.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void callTel(String phoneNumber) {
		try {
			Intent mIntent = new Intent(Intent.ACTION_DIAL, Uri.parse( phoneNumber ) );
			MainActivity.this.startActivity(mIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}