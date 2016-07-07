/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.MyWebViewClient;
import com.paperpad.mybox.helpers.MyWebViewClient.CallbackWebView;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.widgets.CustomWebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * @author euphordev04
 * 
 */
public class PayementFragment extends Fragment implements CallbackWebView{

	/**
	 * You'll need this in your class to cache the helper in the class.
	 */


	private Colors colors;
	private String link;
	private boolean isRss;
	private long time;
	private DatabaseController appController;
	private int total_command;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);
//		setRetainInstance(true);

		DatabaseController appController = new DatabaseController(getActivity());

		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		if (getArguments() != null) {
			link = getArguments().getString("link");
			total_command = getArguments().getInt("total_command");
		}
		
		appController = new DatabaseController(getActivity());

		try {
			colors = appController.getColorsDao().queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onAttach(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.webview_fragment, container,
				false);
		TextView summary_price = (TextView)view.findViewById(R.id.summary_price);
		summary_price.setText(total_command+" "+BoxsMainActivity.currency);
		summary_price.setTextColor(colors.getColor(colors.getTitle_color()));
		((TextView)view.findViewById(R.id.summary_txt)).setTextColor(colors.getColor(colors.getText_color()));
		view.findViewById(R.id.summary_pay).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		
		CustomWebView webView = (CustomWebView) view.findViewById(R.id.webview);
		//WebViewClient webView_ = new WebViewClient();

		final ProgressBar progess = (ProgressBar) view
				.findViewById(R.id.ProgressBar);
		MyWebViewClient wvClient = new MyWebViewClient(progess);
		wvClient.setCallbackWebView(PayementFragment.this);
		webView.setWebViewClient(wvClient );
//		WebSettings settings = webView.getSettings();
//		settings.setJavaScriptEnabled(true);
//		settings.setSupportZoom(true);
//		settings.setBuiltInZoomControls(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setSupportMultipleWindows(false);
		webView.getSettings().setSupportZoom(true);
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(true);
		webView.onScrollChanged(webView.getScrollX(), webView.getScrollY(), webView.getScrollX(), webView.getScrollY());
		
//		if(isRss)
//		{
//			settings.setUserAgentString("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");
//		}
		
//		webView.invokeZoomPicker();
		webView.loadUrl(link);
		
		view.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

//		RoundRectShape rect_ = new RoundRectShape(
//				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//				null,
//				null);
//
//		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
//		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		
		((TextView)view.findViewById(R.id.beneficiary_title)).setTextColor(colors.getColor(colors.getTitle_color()));
		
		Button cancel = (Button) view.findViewById(R.id.cancel);
		//cancel.setBackgroundDrawable(bgBtn);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SummaryFragment summaryFragment = new SummaryFragment();
				if (getArguments() != null) {
					summaryFragment.setArguments(getArguments());
					
				}
				getParentFragment().getChildFragmentManager().beginTransaction()
				.replace(R.id.frame_content, summaryFragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
			}
		});

		return view;
	}
	
	@Override
	public void onStop() {

		super.onStop();
	}

	
	public String ReadFromfile(String fileName, Context context) {
	    StringBuilder returnString = new StringBuilder();


	    InputStream fIn = null;
	    InputStreamReader isr = null;
	    BufferedReader input = null;
	    

	    try {
	        fIn = context.getResources().getAssets()
	                .open(fileName, Context.MODE_WORLD_READABLE);
	        isr = new InputStreamReader(fIn);//,"ISO-8859-1");
	        input = new BufferedReader(isr);
	        String line = "";

	        while ((line = input.readLine()) != null) {
	        	returnString.append(line);
	            //Log.e(" File content :  "," ::  "+returnString);


	        }
	    } catch (Exception e) {
	        e.getMessage();
	    } finally {
	        try {
	            if (isr != null)
	                isr.close();
	            if (fIn != null)
	                fIn.close();
	            if (input != null)
	                input.close();
	        } catch (Exception e2) {
	            e2.getMessage();
	        }
	    }
	    return returnString.toString();
	}

	@Override
	public void getCurrentUrl(String url) {
		if (url.contains("ios_ogone_cancel")) {
			Toast.makeText(getParentFragment().getActivity(), "Payement non réussi", Toast.LENGTH_SHORT).show();
		}else if (url.contains("ios_ogone_return_ok")) {
			((OrderValidationFragment)getParentFragment()).getDialog().dismiss();
		}
		
	}

}
