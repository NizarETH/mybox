/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.models.Colors;

import java.sql.SQLException;

/**
 * @author euphordev02
 *
 */
public class ValidationModeFragment extends Fragment {

	private Colors colors;

	protected int mode = 1;

	/**
	 * 
	 */
	public ValidationModeFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.valdation_mode_fragment, container, false);
		
		DatabaseController appController = new DatabaseController(getActivity());

		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		view.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		view.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));
 		
		Drawable back = getResources().getDrawable(R.drawable.back_order_validation);
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, back);
		drawable.addState(new int[]{android.R.attr.state_focused}, back);
		drawable.addState(new int[]{android.R.attr.state_selected}, back);
		drawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT)); 
		final LinearLayout mode1 = (LinearLayout)view.findViewById(R.id.mode1);
		mode1.setSelected(true);
		mode1.setBackground(back);
		final LinearLayout mode2 = (LinearLayout)view.findViewById(R.id.mode2);
//		mode2.setBackground(drawable);
		
		final ImageView check_mark1 = (ImageView)view.findViewById(R.id.check_mark1);
		final ImageView check_mark2 = (ImageView)view.findViewById(R.id.check_mark2);
		check_mark1.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_on));
		check_mark2.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_off));
		mode1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				v.setSelected(true);
//				mode2.setSelected(false);
				v.setBackground(getResources().getDrawable(R.drawable.back_order_validation));
				mode2.setBackground(new ColorDrawable(Color.TRANSPARENT));
				check_mark1.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_on));
				check_mark2.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_off));
				((OrderValidationFragment)getParentFragment()).mode_order = 0;
			}
		});
		
		mode2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				v.setSelected(true);
//				mode1.setSelected(false);
				v.setBackground(getResources().getDrawable(R.drawable.back_order_validation));
				mode1.setBackground(new ColorDrawable(Color.TRANSPARENT));
				check_mark1.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_off));
				check_mark2.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_on));
				((OrderValidationFragment)getParentFragment()).mode_order  = 1;
			}
		});
		
		((TextView)view.findViewById(R.id.beneficiary_title)).setTextColor(colors.getColor(colors.getTitle_color()));
		
		Button cancel = (Button)view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((DialogFragment)getParentFragment()).getDialog().dismiss();
			}
		});
		
		Button confirm = (Button)view.findViewById(R.id.confirm);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				SummaryFragment summaryFragment = new SummaryFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("MODE_ORDER", mode);
				summaryFragment.setArguments(bundle);
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(R.id.frame_content, summaryFragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
				
			}
		});
		
		return view;
	}

}
