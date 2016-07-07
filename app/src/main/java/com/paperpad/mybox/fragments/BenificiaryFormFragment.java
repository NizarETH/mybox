package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.CartItem;
import com.paperpad.mybox.models.Colors;

import java.sql.SQLException;


/**
 * @author euphordev04
 * 
 */

public class BenificiaryFormFragment extends Fragment {

	private CartItem infos;
	private Colors colors;
	private DatabaseController appController;
	private EditText first_name;
	private EditText last_name;
	private EditText email;
	private EditText complement;

	
	
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    
		View view = inflater.inflate(R.layout.beneficiaire, container, false);
		view.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		view.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));
		
		first_name = (EditText)view.findViewById(R.id.first_name);
		last_name = (EditText)view.findViewById(R.id.last_name);
		email = (EditText)view.findViewById(R.id.email);
		complement = (EditText)view.findViewById(R.id.complement);
		
		((TextView)view.findViewById(R.id.beneficiary_title)).setTextColor(colors.getColor(colors.getTitle_color()));
		
		
//		RoundRectShape rectIdentif = new RoundRectShape(
//				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
//				null,
//				null);
//
//		ShapeDrawable bgIdentif = new ShapeDrawable(rectIdentif);
//		bgIdentif.getPaint().setColor(colors.getColor(colors.getBackground_color()));	
//		view.findViewById(R.id.identificationContainer).setBackground(bgIdentif);
		view.findViewById(R.id.identificationContainer).setBackgroundColor(colors.getColor(colors.getBackground_color()));

//		
//		RoundRectShape rectName = new RoundRectShape(
//				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
//				null,
//				null);
//		
//		ShapeDrawable bgCom = new ShapeDrawable(rectName);
//		bgCom.getPaint().setColor(colors.getColor(colors.getBackground_color()));
//		view.findViewById(R.id.commentaire).setBackground(bgCom);
		view.findViewById(R.id.commentaire).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		
		((TextView)view.findViewById(R.id.who_offer_msg)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.msg_to_beneficiary)).setTextColor(colors.getColor(colors.getText_color()));
		
		final View finalView = view;
		
//		RoundRectShape rect_ = new RoundRectShape(
//				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//				null,
//				null);
//
//		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
//		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		
		Button confirm = (Button) view.findViewById(R.id.confirm);
		//confirm.setBackground(bgBtn);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (validateAllFields(finalView)) {
					try {
						appController.getCartItemDao().createOrUpdate(infos);
						Log.i("update cartItem", "fait");
						SummaryFragment fragment = new SummaryFragment();
						getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.frame_content, fragment).addToBackStack(null).commit();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		Button cancel = (Button) view.findViewById(R.id.cancel);
		//cancel.setBackground(bgBtn);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SummaryFragment fragment = new SummaryFragment();
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.frame_content, fragment).addToBackStack(null).commit();
			}
				
		});

						
		
		return fillBenificiaryInformation(view, infos);
	}
	

	
	public View fillBenificiaryInformation(View v, CartItem infos) {

		try {
			((EditText)v.findViewById(R.id.email)).setText(infos.getEmail());
			((EditText)v.findViewById(R.id.first_name)).setText(infos.getFirst_name());
			((EditText)v.findViewById(R.id.last_name)).setText(infos.getLast_name());
			((EditText)v.findViewById(R.id.complement)).setText(infos.getMessage());
		} catch (Exception e) {
			Crashlytics.logException(e);
			e.printStackTrace();
		}
	

		return v;
	}
	
	public boolean validateAllFields(View view) throws NullPointerException{
		boolean error = false;
		String value;

		String ErrorMsg = getString(R.string.fields_msg_error);

		EditText email = (EditText) view.findViewById(R.id.email);
		EditText firstName = (EditText) view.findViewById(R.id.first_name);
		EditText lastName = (EditText) view.findViewById(R.id.last_name);
		EditText message = (EditText) view.findViewById(R.id.complement);
		
			

		value = email.getText().toString();
		infos.setEmail(value);
		if (value.isEmpty() || !Utils.isEmailValid(value)) {
			error = true;
			//email.setError(fill_fields);
			ErrorMsg += "email, ";
		}

		
		value = firstName.getText().toString();
		infos.setFirst_name(value);
		if (value.isEmpty()) {
			error = true;
//			firstName.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.first_name)+", ";
		}
		
		value = lastName.getText().toString();
		infos.setLast_name(value);
		if (value.isEmpty()) {
			error = true;
//			lastName.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.last_name)+", ";
		}
		
		value = message.getText().toString();
		
		if (value.isEmpty()) {
			value ="     ";
//			error = true;
//			lastName.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.last_name)+", ";
		}
		infos.setMessage(value);

		



		
		if(error) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setIcon(android.R.drawable.ic_dialog_info);
			alert.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {

				@Override 
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
			}); 

				alert.setTitle(getString(R.string.fileds_error));
				alert.setMessage(ErrorMsg);
				alert.show();
		
		
		}
		
		return !error;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		appController = new DatabaseController(getActivity());
		if (getArguments() != null) {
			int item_id = getArguments().getInt("CartItem");
			try {
				infos = appController.getCartItemDao().queryForId(item_id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onAttach(activity);
	}

	
	
}
