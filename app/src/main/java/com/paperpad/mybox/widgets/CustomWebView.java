/**
 * 
 */
package com.paperpad.mybox.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author euphordev02
 *
 */
public class CustomWebView extends WebView {

	/* (non-Javadoc)
	 * @see android.webkit.WebView#onScrollChanged(int, int, int, int)
	 */
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public CustomWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	

}
