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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.models.Colors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * @author euphordev04
 * 
 */
public class WebViewFragment extends Fragment {

	/**
	 * You'll need this in your class to cache the helper in the class.
	 */


	private Colors colors;
	private String link;
	private boolean isRss;
	private long time;
	

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

		View view = inflater.inflate(R.layout.webview_layout_fragment, container,
				false);
		
		view.findViewById(R.id.cgvContainer).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

		((TextView)view.findViewById(R.id.titleAccount)).setTextColor(colors.getColor(colors.getBackground_color()));

		String mimeType = "text/html";
		String encoding = "utf-8";
		
		WebView webView = (WebView) view.findViewById(R.id.webview);
		webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));   	  

		webView.getSettings().setJavaScriptEnabled(true);				
		webView.loadDataWithBaseURL(null, ReadFromfile("cgv.html",getActivity()), mimeType, encoding, null);//.loadData(ReadFromfile("cgv.html",getActivity()), mimeType, encoding);
		webView.getSettings().setDefaultFontSize(10);
		webView.getSettings().setTextZoom( (int)(webView.getSettings().getTextZoom() * 1.1) );
		//webView.getSettings().setTextZoom(120);
	
//		RoundRectShape rect_ = new RoundRectShape(
//				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//				null,
//				null);
//
//		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
//		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));

		((TextView)view.findViewById(R.id.titleAccount)).setTextColor(colors.getColor(colors.getTitle_color()));

		Button confirm = (Button) view.findViewById(R.id.validate);
		//confirm.setBackgroundDrawable(bgBtn);
		
		final View finalView = view;
		
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SummaryFragment fragment = new SummaryFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("AGREE", true);
				((OrderValidationFragment)getParentFragment()).AGREE_CGV = true;
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
				.replace(R.id.frame_content, fragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
					
			}
		});

		Button cancel = (Button) view.findViewById(R.id.cancel);
		//cancel.setBackgroundDrawable(bgBtn);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SummaryFragment fragment = new SummaryFragment();
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
				.replace(R.id.frame_content, fragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
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

}
