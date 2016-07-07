package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.models.AddressAccount;
import com.paperpad.mybox.models.BillingAddress;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.ShippingAddress;
import com.paperpad.mybox.models.UserAccount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author euphordev04
 * 
 */

public class OrderAddressFragment extends Fragment {

	private UserAccount infos;
	private Colors colors;
	private int type = 0;
	private AddressAccount addressAccount = new AddressAccount();
	private DatabaseController appController;

	
	public void setUserAccount(UserAccount infos) {
		
		this.infos = infos/*new UserAccount("test@test.com", "Driss", "bou", "adresse", "complement_adresse", 1, "ville", 1020, "283888383", "283888383", "1")*/;// infos;
		
	}
	  
	
	public UserAccount getUserAccount() {
		return infos;
	}
	
	
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
		
		if (getArguments()!= null) {
			type  = getArguments().getInt("TYPE");
		}
		DatabaseController appController = new DatabaseController(getActivity());
		List<UserAccount> accounts = new ArrayList<UserAccount>();
		try {
			accounts = appController.getUserDao().queryForAll();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (accounts.size()>0) {
			setUserAccount(accounts.get(0)); 
		}
		
		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    
		View view = inflater.inflate(R.layout.order_address_form, container, false);
		
		
		
		view.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		view.findViewById(R.id.createNewAccountBar).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

		((TextView)view.findViewById(R.id.titleAccount)).setTextColor(colors.getColor(colors.getBackground_color()));
		
		RoundRectShape rectName = new RoundRectShape(
				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
				null,
				null);
		
		ShapeDrawable bgName = new ShapeDrawable(rectName);
		bgName.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		view.findViewById(R.id.nameContainer).setBackground(bgName);
		
		RoundRectShape rectCoord = new RoundRectShape(
				new float[] {10, 10, 10, 10, 10,10, 10, 10},
				null,
				null);
		
		ShapeDrawable bgCoord = new ShapeDrawable(rectCoord);
		bgCoord.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		view.findViewById(R.id.cordinatesContainer).setBackground(bgCoord);
		
		RoundRectShape rectPhone = new RoundRectShape(
				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
				null,
				null);
		
		ShapeDrawable bgPhone = new ShapeDrawable(rectPhone);
		bgPhone.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		view.findViewById(R.id.phoneContainer).setBackground(bgPhone);
		
		
		((TextView)view.findViewById(R.id.fill_field)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.coordinates)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.phoneNumber)).setTextColor(colors.getColor(colors.getText_color()));
		
		
//		RoundRectShape rect_ = new RoundRectShape(
//				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
//				null,
//				null);
//
//		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
//		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));

		((TextView)view.findViewById(R.id.titleAccount)).setTextColor(colors.getColor(colors.getTitle_color()));
		
		Button inscription = (Button) view.findViewById(R.id.inscription);
		//inscription.setBackgroundDrawable(bgBtn);
		
		final View finalView = view;
		
		inscription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("  Champs valides ? "," "+validateAllFields(finalView));
				//if(validateAllFields(finalView))
					if (validateAllFields(finalView)) {
						if (type == 0) {//BillingAddress
							BillingAddress address = addressAccount.getBillingAddress();
							appController.addBillingAddress(address);
						}else if (type == 1) {
							ShippingAddress address = addressAccount.getShippingAddress();
							appController.addShippingAddress(address);
						}
						SummaryFragment summaryFragment = new SummaryFragment();
						if (getArguments() != null) {
							summaryFragment.setArguments(getArguments());

						}
						getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.frame_content, summaryFragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
					}
					
					
			}
		});

		Button cancel = (Button) view.findViewById(R.id.cancel);
		//cancel.setBackgroundDrawable(bgBtn);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SummaryFragment summaryFragment = new SummaryFragment();
				if (getArguments() != null) {
					summaryFragment.setArguments(getArguments());
					
				}
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
				.replace(R.id.frame_content, summaryFragment, "SummaryFragment").addToBackStack("SummaryFragment").commit();
			}
		});

		getCountriesChoice(getActivity(), view);
		
		return fillAccountInformation(view, getUserAccount());
		
		
	
	}
	
	public void getCountriesChoice(final Context context, final View view) {


		Button selectBtn = (Button)view.findViewById(R.id.countries);
		selectBtn.setHint("  "+getString(R.string.countries)+"...");

		selectBtn.setTag(-1);
		selectBtn.setOnClickListener(new OnClickListener() {
			Dialog myDialog = null;
			String selectedItem = "";

			@Override
			public void onClick(View v) {

				final String[][] items = ReadFromfile("countries_code.txt", context);
				final String[] countries = new String[items.length];

				for(int i = 0; i< items.length; i++)
					countries[i] = items[i][0];

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(getString(R.string.countries));
				//builder.setIcon(android.R.drawable.ic_dialog_info);
				selectedItem = countries[0];
				v.setTag(0);
				final Button btn = (Button)v;

				builder.setSingleChoiceItems(countries, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedItem = countries[which];

						btn.setTag(which);
						btn.setText(selectedItem);


					}
				});

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {



					@Override
					public void onClick(DialogInterface dialog, int which) {

						btn.setText(selectedItem);

						Log.e(" OK selected country item : "+(Integer) btn.getTag()+" is : "+selectedItem," country code : "+items[(Integer) btn.getTag()][1]
								+" country phone code "+items[(Integer) btn.getTag()][2]);

						//countryCode = items[(Integer) btn.getTag()][1];
						addressAccount.setPays(Integer.parseInt(items[(Integer) btn.getTag()][1]));
						EditText fixeIndicatif = (EditText) view.findViewById(R.id.fix_number_indicatif);
						EditText mobileIndicatif = (EditText) view.findViewById(R.id.mobile_number_indicatif);

						fixeIndicatif.setText(items[(Integer) btn.getTag()][2]);
						mobileIndicatif.setText(items[(Integer) btn.getTag()][2]);


						myDialog.hide();
					}
				});


				builder.setCancelable(true);
				myDialog = builder.create();
				myDialog.show();


			}
		});

	}



	
	public View fillAccountInformation(View v, UserAccount infos) {
		
		try {
			try {
				switch(Integer.parseInt((infos!=null && infos.getCivilite()!=null)?infos.getCivilite():"1")) {  // infos[3] c'est l'attribut qui stocke la civilité
				
				case 1 :
					((RadioButton)v.findViewById(R.id.mrs)).setChecked(true);

				case 2 :
					((RadioButton)v.findViewById(R.id.miss)).setChecked(true);

				case 3 :	
					((RadioButton)v.findViewById(R.id.mr)).setChecked(true);

				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			((EditText)v.findViewById(R.id.first_name)).setText(infos.getPrenom());
			((EditText)v.findViewById(R.id.last_name)).setText(infos.getNom());
			((EditText)v.findViewById(R.id.address)).setText(infos.getAdresse());
			((EditText)v.findViewById(R.id.complement)).setText(infos.getComplement_adresse());
			try {
				if ((EditText)v.findViewById(R.id.postal_code) != null) {
					((EditText) v.findViewById(R.id.postal_code)).setText(infos
							.getCode_postale().toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((EditText)v.findViewById(R.id.city)).setText(infos.getVille());		
			
			final String[][] items = ReadFromfile("countries_code.txt", getActivity());

			for(int i = 0; i < items.length; i++) {
				if(items[i][1].compareTo(infos.getPays().toString()) == 0) {    //   Driss infos[10] c'est l'attribut qui stocke le code pays retourner par le serveur
					((Button)v.findViewById(R.id.countries)).setText(items[i][0]);
					((EditText)v.findViewById(R.id.fix_number_indicatif)).setText(items[i][2]);
					((EditText)v.findViewById(R.id.mobile_number_indicatif)).setText(items[i][2]);
					break;
				}
			}	

			
			((EditText)v.findViewById(R.id.fix_phone)).setText(infos.getTel_fixe());
			((EditText)v.findViewById(R.id.mobile_phone)).setText(infos.getTel_mobile());

			
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	
	
	public String[][] ReadFromfile(String fileName, Context context) {
	    //StringBuilder returnString = new StringBuilder();
        String[][] countriesCode = new String[32][3];

	    InputStream fIn = null;
	    InputStreamReader isr = null;
	    BufferedReader input = null;
	    
	    int i = 0;
	    try {
	        fIn = context.getResources().getAssets()
	                .open(fileName, Context.MODE_WORLD_READABLE);
	        isr = new InputStreamReader(fIn,"ISO-8859-1");
	        input = new BufferedReader(isr);
	        String line = "";

	        while ((line = input.readLine()) != null) {

	            countriesCode[i][0] = line.split(" : ")[0];
	            countriesCode[i][1] = line.split(" : ")[1];
	            countriesCode[i][2] = line.split(" : ")[2];
	            Log.e("  Country : "+countriesCode[i][0], " Country Code : "+countriesCode[i][1]+" Phone Code : "+countriesCode[i][2]);

	            i++;
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
	    return countriesCode; //returnString.toString();
	}


	
	public boolean validateAllFields(View view) {
		boolean error = false;
		String value;

		String ErrorMsg = getString(R.string.fields_msg_error);

		EditText firstName = (EditText) view.findViewById(R.id.first_name);
		EditText lastName = (EditText) view.findViewById(R.id.last_name);
		
		EditText address = (EditText) view.findViewById(R.id.address);
		EditText postalCode = (EditText) view.findViewById(R.id.postal_code);
		EditText city = (EditText) view.findViewById(R.id.city);
		Button Countries = (Button) view.findViewById(R.id.countries);
		
		EditText fixePhone = (EditText) view.findViewById(R.id.fix_phone);
		
		EditText mobilePhone = (EditText) view.findViewById(R.id.mobile_phone);
		RadioGroup rgp = (RadioGroup) view.findViewById(R.id.rGrpTitle);

		
		
		switch(rgp.getCheckedRadioButtonId()) {
		case R.id.mrs :
			addressAccount.setCivilite(" "+getString(R.string.mrs));
			break;
			
		case R.id.miss :
			addressAccount.setCivilite(" "+getString(R.string.miss));
			break;
			
		case R.id.mr :
			addressAccount.setCivilite(" "+getString(R.string.mr));
			break;
			
		default :
			ErrorMsg += getString(R.string.title)+", ";
			break;
		}
		
		value = firstName.getText().toString();
		addressAccount.setPrenom(value);
		if (value.isEmpty()) {
			error = true;
//			firstName.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.first_name)+", ";
		}
		
		value = lastName.getText().toString();
		addressAccount.setNom(value);
		if (value.isEmpty()) {
			error = true;
//			lastName.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.last_name)+", ";
		}
		
		value = address.getText().toString();
		addressAccount.setAdresse(value);
		if (value.isEmpty()) {
			error = true;
//			address.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.address)+", ";
		}
		
		value = postalCode.getText().toString();
		try {
			addressAccount.setCode_postale(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (value.isEmpty()) {
			error = true;
//			postalCode.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.postal_code)+", ";
		}
		
		value = city.getText().toString();
		addressAccount.setVille(value);
		if (value.isEmpty()) {
			error = true;
//			city.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.city)+", ";
		}
		
		
		value = Countries.getText().toString();
		try {
			addressAccount.setPays(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (value.isEmpty()) {
			error = true;
//			Countries.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.countries)+", ";

		}
		
		value = fixePhone.getText().toString();
		addressAccount.setTel_fixe(value);
		if (value.isEmpty()) {
			error = true;
//			fixePhone.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.fix_phone)+", ";

		}
		
		value = mobilePhone.getText().toString();
		addressAccount.setTel_mobile(value);
		if (value.isEmpty()) {
			error = true;
//			mobilePhone.setError(fill_fields);
//			return false;
			ErrorMsg += getString(R.string.mobile_phone)+", ";
		}


		
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
		super.onAttach(activity);
	}


	

}
