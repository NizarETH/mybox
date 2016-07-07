/**
 * 
 */
package com.paperpad.mybox.helpers;



import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @author euphordev02
 *
 */
public class MyWebViewClient extends WebViewClient {

	private ProgressBar progess;

	/**
	 * 
	 */
	public MyWebViewClient(ProgressBar progess) {
		this.progess = progess;
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		progess.setVisibility(View.GONE);
		super.onPageFinished(view, url);
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		progess.setVisibility(View.VISIBLE);
		super.onPageStarted(view, url, favicon);
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#doUpdateVisitedHistory(android.webkit.WebView, java.lang.String, boolean)
	 */
	@Override
	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload) {
		callbackWebView.getCurrentUrl(url);
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.e("l'url", url);
		callbackWebView.getCurrentUrl(url);
		return true;
	}

	public interface CallbackWebView{
		void getCurrentUrl(String url);
	}

	private CallbackWebView callbackWebView = new CallbackWebView() {

		@Override
		public void getCurrentUrl(String url) {
			// TODO Auto-generated method stub

		}
	};
	
	/**
	 * @return the callbackWebView
	 */
	public CallbackWebView getCallbackWebView() {
		return callbackWebView;
	}

	/**
	 * @param callbackWebView the callbackWebView to set
	 */
	public void setCallbackWebView(CallbackWebView callbackWebView) {
		this.callbackWebView = callbackWebView;
	}

	/* (non-Javadoc)
	 * @see android.webkit.WebViewClient#onLoadResource(android.webkit.WebView, java.lang.String)
	 */
	@Override
	public void onLoadResource(WebView view, String url) {
		callbackWebView.getCurrentUrl(url);
		super.onLoadResource(view, url);
	}

}
