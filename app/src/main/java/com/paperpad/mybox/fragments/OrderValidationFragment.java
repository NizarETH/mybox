/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.paperpad.mybox.R;

/**
 * @author euphordev02
 *
 */
public class OrderValidationFragment extends DialogFragment {
	
	public boolean AGREE_CGV = false;
	
	/**
	 *  0 send just mail or sms  1 : send a post package
	 */
	public int mode_order = 0; 
	
	/**
	 * 
	 */
	public OrderValidationFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_fragment, container, false);
		ValidationModeFragment fragment = new ValidationModeFragment();
		getChildFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack(null).commit();
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//	    lp.copyFrom(getDialog().getWindow().getAttributes());
//	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//	    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//	    getDialog().getWindow().setAttributes(lp);
		
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Window window = getDialog().getWindow();
	    LayoutParams attributes = window.getAttributes();
	    //must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
	    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	    this.setCancelable(false);
	    //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    //window.setLayout((int)getResources().getDimension(R.dimen.window_width), (int)getResources().getDimension(R.dimen.window_height));
	    attributes.width = (int)getResources().getDimension(R.dimen.window_width);
	    attributes.height = (int)getResources().getDimension(R.dimen.window_height);
	    
	    //window.setLayout((int)getResources().getDimension(R.dimen.window_width), 500);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		  if (getDialog() != null && getRetainInstance())
			    getDialog().setOnDismissListener(null);
			  super.onDestroyView();
	}
}
