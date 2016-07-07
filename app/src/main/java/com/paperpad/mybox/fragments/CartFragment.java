/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.adapters.ListOrderHistoryAdapter;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.UrlHelpers;
import com.paperpad.mybox.models.CartItem;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.models.OrderHistory;
import com.paperpad.mybox.models.UserAccount;
import com.paperpad.mybox.models.UserCreateAccount;
import com.paperpad.mybox.widgets.ScrollViewListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author euphordev02
 *
 */
public class CartFragment extends Fragment implements ScrollViewListener{

	private static PostCommentResponseListener mPostCommentResponse;
	private String fill_fields, invalidate_pwd;
	private LinearLayout cart_space;
	private LayoutInflater inflater;
	private DatabaseController appController;
	private TextView total_txt;
	private View view;
	//protected PopupWindow pw;
	private static Colors colors;
	private static String countryCode;
	private static SharedPreferences wmbPreference;
	public static int total;
	public View tmpView;
	private EditText email;
	private EditText pwd;
	private EditText first_name;
	private EditText last_name;
	private EditText address;
	private EditText postal_code;
	private EditText city;
	private EditText fix_number_indicatif;
	private EditText fix_phone;
	private EditText mobile_number_indicatif;
	private EditText mobile_phone;
	private EditText complement;
	private UserCreateAccount account;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		wmbPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
		appController = new DatabaseController(getActivity());
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fill_fields = getString(R.string.fill_field);
		invalidate_pwd = getString(R.string.invalide_pwd_confirmation);
		super.onCreate(savedInstanceState);
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View 
		view = inflater.inflate(R.layout.cart_fragment, container, false);
		this.inflater = inflater;

		DatabaseController appController = new DatabaseController(getActivity());

		try {
			colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if (view.findViewById(R.id.header) != null) {
			view.findViewById(R.id.header).setBackgroundDrawable(colors.makeGradientToColor(colors.getDark_navigation_color()));
		}
		//.setBackgroundColor(colors.getColor(colors.getTitle_color()));
		view.findViewById(R.id.cartBackground).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		// load image
		try {
			// get input stream
			InputStream ims = getActivity().getAssets().open("ribbon.png");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to Background
			view.findViewById(R.id.orders_container).setBackgroundDrawable(d);
		}
		catch(IOException ex) {
		}

		view.findViewById(R.id.orders_container).setAlpha(0.8f);
		view.findViewById(R.id.validate_order).setBackgroundColor(colors.getColor(colors.getAlternate_background_color(), "EE"));

		LinearLayout auth_container = (LinearLayout)view.findViewById(R.id.authContainer);

		((TextView)view.findViewById(R.id.accountDiscription)).setTextColor(colors.getColor(colors.getText_color()));


		RoundRectShape rect = new RoundRectShape(new float[] {10, 10, 10, 10, 10 ,10, 10, 10}, null, null);

		ShapeDrawable bg = new ShapeDrawable(rect);
		bg.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		auth_container.setBackgroundDrawable(bg);

		RoundRectShape rect_ = new RoundRectShape(
				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
				null,
				null);

		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		view.findViewById(R.id.createAccount).setBackgroundDrawable(bgBtn);

		((TextView)view.findViewById(R.id.auth_account_tag)).setTextColor(colors.getColor(colors.getTitle_color()));

		wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		if(wmbPreference.getString("token_id","-1").compareToIgnoreCase("-1") !=0 ) {

			view.findViewById(R.id.auth_space).setVisibility(View.GONE);
			view.findViewById(R.id.create_account_space).setVisibility(View.GONE);
			view.findViewById(R.id.authentication_home).setVisibility(View.VISIBLE);
			TextView  userAccount = (TextView) view.findViewById(R.id.userAccount);
			userAccount.setText(wmbPreference.getString("mail","?"));
			userAccount.setTextColor(colors.getColor(colors.getText_color()));

			ShapeDrawable bgOrders = new ShapeDrawable(rect_);
			bgOrders.getPaint().setColor(colors.getColor(colors.getTitle_color()));
			view.findViewById(R.id.orders).setBackgroundDrawable(bgOrders);
			view.findViewById(R.id.orders).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					getOrderHistory(getActivity(), v.getRootView());

				}
			});

			ShapeDrawable bgLogout = new ShapeDrawable(rect_);
			bgLogout.getPaint().setColor(colors.getColor(colors.getTitle_color()));
			view.findViewById(R.id.logout).setBackgroundDrawable(bgLogout);
			view.findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					//					SharedPreferences wmbPreference = PreferenceManager
					//							.getDefaultSharedPreferences(getActivity());
					SharedPreferences.Editor editor = wmbPreference
							.edit();
					editor.remove("token_id");
					editor.remove("mail");
					editor.commit();


					getActivity().findViewById(R.id.auth_space).setVisibility(View.VISIBLE);
					getActivity().findViewById(R.id.create_account_space).setVisibility(View.VISIBLE);
					getActivity().findViewById(R.id.authentication_home).setVisibility(View.GONE);

				}
			});
		}

		//cart layout init
		cart_space = (LinearLayout)view.findViewById(R.id.cart_space);
		total_txt = (TextView)view.findViewById(R.id.total);

		RoundRectShape rectBtn = new RoundRectShape(
				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
				null,
				null);

		ShapeDrawable bgBtn_ = new ShapeDrawable(rectBtn);
		bgBtn_.getPaint().setColor(colors.getColor(colors.getTitle_color()));

		LinearLayout order_btn = (LinearLayout)view.findViewById(R.id.order_btn);
		order_btn.setBackgroundDrawable(bgBtn_);
		final LayoutInflater inflater2 = inflater;
		final LinearLayout auth_space = (LinearLayout)view.findViewById(R.id.auth_space);
//		final LinearLayout pwd = (LinearLayout)view.findViewById(R.id.pwd);
		order_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String token_id = null;
				try {
					token_id = wmbPreference.getString("token_id", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (token_id != null && token_id.isEmpty()) {
					View layout = inflater2.inflate(R.layout.info_popup, null, false);
					Drawable popover  = getResources().getDrawable(R.drawable.bonuspack_bubble_white_mod2);
					popover.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getNavigation_color()), PorterDuff.Mode.MULTIPLY));
					layout.setBackgroundDrawable(popover);
					
					final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, true);
					// display the popup in the center
					pw.setOutsideTouchable(true);
					pw.setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
					pw.setFocusable(true);
					pw.setTouchInterceptor(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							//							if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
							//								pw.dismiss();
							//								return true;
							//							}
							//							return false;
							pw.dismiss();
							return true;
						}
					});
					pw.showAsDropDown(auth_space);

//					Handler handler = new Handler();
//					long delay = 1000;
//					handler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							pw.dismiss();
//						}
//					}, delay);
				}else {
					OrderValidationFragment fragment = new OrderValidationFragment();
					fragment.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog );
					fragment.show(getActivity().getSupportFragmentManager()	, "OrderValidationFragment");
				}
			}
		});
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {




		final EditText email = (EditText) view.findViewById(R.id.email);
		final EditText pwd = (EditText) view.findViewById(R.id.password);
		view.findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Toast.makeText(getActivity(), "Parameters ( email : "+email.getText().toString()+" , Password : "+pwd.getText().toString()+" ) are corrects and can be sent to a server!", 1000).show();

				if(isEmailValid(email.getText().toString()) && !pwd.getText().toString().isEmpty()) {
					//Toast.makeText(getActivity(), "Parameters ( email : "+email.getText().toString()+" , Password : "+pwd.getText().toString()+" ) are corrects and can be sent to a server!", 1000).show();				

					postNewRequest(getActivity(),  email.getText().toString(), pwd.getText().toString(), appController);

				}else {
					//					Toast.makeText(getActivity(), "Parameters are not corrects and can't be sent to a server!", 1000).show();
					//
					String email = "bounouar.driss@gmail.com"; //"loic.bruniquel@euphor.ma";
					String pwd = "drissbounouar666";// "sponge";

					postNewRequest(getActivity(),   email, pwd, appController);

				}

			}
		});

		//final View view_ = view;
		view.findViewById(R.id.createAccount).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "Create a new Account", 1000).show();

				createNewAccount(v.getRootView());

			}
		});

		fillCart();
		super.onViewCreated(view, savedInstanceState);
	}



	public boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}



	public void createNewAccount(View rootView) {

		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		final View view = mInflater.inflate(R.layout.create_account_form, null, false);
		tmpView = view;
		
		try {
			account = appController.getUserCreateAccountDao().queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}

		email = (EditText)view.findViewById(R.id.email);
		pwd = (EditText)view.findViewById(R.id.pwd);
		first_name = (EditText)view.findViewById(R.id.first_name);
		last_name = (EditText)view.findViewById(R.id.last_name);
		address = (EditText)view.findViewById(R.id.address);
		complement = (EditText)view.findViewById(R.id.complement);
		postal_code = (EditText)view.findViewById(R.id.postal_code);
		city = (EditText)view.findViewById(R.id.city);
		fix_number_indicatif = (EditText)view.findViewById(R.id.fix_number_indicatif);
		fix_phone = (EditText)view.findViewById(R.id.fix_phone);
		mobile_number_indicatif = (EditText)view.findViewById(R.id.mobile_number_indicatif);
		mobile_phone = (EditText)view.findViewById(R.id.mobile_phone);
		
		if (account != null) {
			try {
				email.setText(account.getEmail());
				pwd.setText(account.getPwd());
				first_name.setText(account.getFirst_name());
				last_name.setText(account.getLast_name());
				address.setText(account.getAddress());
				complement.setText(account.getComplement());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				postal_code.setText(account.getPostal_code());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				city.setText(account.getCity());
				fix_number_indicatif.setText(account.getFix_number_indicatif());
				fix_phone.setText(account.getFix_phone());
				mobile_number_indicatif.setText(account.getMobile_number_indicatif());
				mobile_phone.setText(account.getMobile_phone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			account = new UserCreateAccount();
		}
		
		com.paperpad.mybox.widgets.ObservableScrollView scrollView = (com.paperpad.mybox.widgets.ObservableScrollView) view.findViewById(R.id.scrollView1);
		scrollView.setScrollViewListener(this);

		view.findViewById(R.id.createNewAccountBar).setBackgroundColor(colors.getColor(colors.getNavigation_color()));
		((TextView)view.findViewById(R.id.titleAccount)).setTextColor(colors.getColor(colors.getTitle_color()));



		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
		dialog.getWindow().setLayout( (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height));
		//dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		//dialog.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//dialog.getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

		dialog.setCanceledOnTouchOutside(false); 
		dialog.show();

		//		RoundRectShape rect_ = new RoundRectShape(
		//				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
		//				null,
		//				null);
		//
		//		ShapeDrawable bgBtn = new ShapeDrawable(rect_);
		//		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));

		ColorStateList listColor1 = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getText_color()), 
				colors.getColor(colors.getBackground_color())});

		Button inscription = (Button) view.findViewById(R.id.inscription);
		//inscription.setBackground(bgBtn);
		inscription.setTextColor(listColor1);
		inscription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Log.e("  Champs valides ? "," "+validateAllFields(view));
				if(validateAllFields(view))
					postNewAccountRequest(getActivity(), view, appController, dialog);
			}
		});

		ColorStateList listColor2 = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}}, 
				new int[]{colors.getColor(colors.getText_color()), 
				colors.getColor(colors.getBackground_color())});

		Button cancel = (Button) view.findViewById(R.id.cancel);
		//cancel.setBackground(bgBtn);
		cancel.setTextColor(listColor2);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//pw.dismiss();
				try {
					account.setEmail(email.getText().toString());
					account.setPwd(pwd.getText().toString());
					account.setFirst_name(first_name.getText().toString());
					account.setLast_name(last_name.getText().toString());
					account.setAddress(address.getText().toString());
					account.setComplement(complement.getText().toString());
					account.setCity(city.getText().toString());
					account.setFix_phone(fix_phone.getText().toString());
					try {
						account.setPostal_code(Integer.parseInt(postal_code.getText().toString()));
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						account.setFix_number_indicatif(Integer.parseInt(fix_number_indicatif.getText().toString()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					account.setMobile_number_indicatif(mobile_number_indicatif.getText().toString());
					account.setMobile_phone(mobile_phone.getText().toString());
					
					appController.saveUserTemporary(account);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				dialog.dismiss();
			}
		});

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
		//		ShapeDrawable bgName = new ShapeDrawable(rectName);
		//		bgName.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		//		view.findViewById(R.id.nameContainer).setBackground(bgName);
		view.findViewById(R.id.nameContainer).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		//
		//		RoundRectShape rectCoord = new RoundRectShape(
		//				new float[] {10, 10, 10, 10, 10,10, 10, 10},
		//				null,
		//				null);
		//
		//		ShapeDrawable bgCoord = new ShapeDrawable(rectCoord);
		//		bgCoord.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		//		view.findViewById(R.id.cordinatesContainer).setBackground(bgCoord);
		view.findViewById(R.id.cordinatesContainer).setBackgroundColor(colors.getColor(colors.getBackground_color()));

		//
		//		RoundRectShape rectPhone = new RoundRectShape(
		//				new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
		//				null,
		//				null);
		//
		//		ShapeDrawable bgPhone = new ShapeDrawable(rectPhone);
		//		bgPhone.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		//		view.findViewById(R.id.phoneContainer).setBackground(bgPhone);
		view.findViewById(R.id.phoneContainer).setBackgroundColor(colors.getColor(colors.getBackground_color()));


		((TextView)view.findViewById(R.id.identification)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.name)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.coordinates)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.phoneNumber)).setTextColor(colors.getColor(colors.getText_color()));


		getCountriesChoice(getActivity(), view);

	}


	public boolean validateAllFields(View view) {
		boolean error = false;
		String value;

		String ErrorMsg = ""; //getString(R.string.fields_msg_error);

		EditText email = (EditText) view.findViewById(R.id.email);
		EditText pwd = (EditText) view.findViewById(R.id.pwd);
		EditText pwd_confirm = (EditText) view.findViewById(R.id.pwd_confirm);

		//		EditText firstName = (EditText) view.findViewById(R.id.first_name);
		//		EditText lastName = (EditText) view.findViewById(R.id.last_name);
		//		
		//		EditText address = (EditText) view.findViewById(R.id.address);
		//		EditText postalCode = (EditText) view.findViewById(R.id.postal_code);
		//		EditText city = (EditText) view.findViewById(R.id.city);
		//		Button Countries = (Button) view.findViewById(R.id.countries);
		//		
		//		EditText fixePhone = (EditText) view.findViewById(R.id.fix_phone);
		//		
		//		EditText mobilePhone = (EditText) view.findViewById(R.id.mobile_phone);




		//		value = firstName.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			firstName.setError(fill_fields);
		////			return false;
		//			ErrorMsg += getString(R.string.first_name);
		//		}
		//		
		//		value = lastName.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			lastName.setError(fill_fields);
		////			return false;
		//			ErrorMsg += getString(R.string.last_name);
		//		}
		//		
		//		value = address.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			address.setError(fill_fields);
		////			return false;
		//			ErrorMsg += getString(R.string.address);
		//		}
		//		
		//		value = postalCode.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			postalCode.setError(fill_fields);
		////			return false;
		//			ErrorMsg += getString(R.string.postal_code);
		//		}
		//		
		//		value = city.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			city.setError(fill_fields);
		////			return false;
		//			ErrorMsg = getString(R.string.city);
		//		}
		//		
		//		
		//		value = Countries.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			Countries.setError(fill_fields);
		////			return false;
		//			ErrorMsg = getString(R.string.countries);
		//
		//		}
		//		
		//		value = fixePhone.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			fixePhone.setError(fill_fields);
		////			return false;
		//			ErrorMsg = getString(R.string.fix_phone);
		//
		//		}
		//		
		//		value = mobilePhone.getText().toString();
		//		if (value.isEmpty()) {
		//			error = true;
		////			mobilePhone.setError(fill_fields);
		////			return false;
		//			ErrorMsg = getString(R.string.mobile_phone);
		//		}

		value = pwd.getText().toString();
		if (value.isEmpty()) {
			error = true;
			//pwd.setError(fill_fields);
			ErrorMsg = getString(R.string.pwd_error);

		}

		value = pwd_confirm.getText().toString();
		if (value.isEmpty()) {
			error = true;
			//pwd_confirm.setError(fill_fields);
			//return false;

		}



		if((pwd_confirm.getText().toString().isEmpty() && pwd.getText().toString().isEmpty()) || pwd_confirm.getText().toString().compareTo(pwd.getText().toString()) != 0) {
			//pwd_confirm.setError(invalidate_pwd);
			error = true;
			ErrorMsg = invalidate_pwd;			
		}

		value = email.getText().toString();
		if (value.isEmpty() || !isEmailValid(value)) {
			error = true;
			//email.setError(fill_fields);
			ErrorMsg = getString(R.string.email_error);
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
			if(ErrorMsg.compareTo(invalidate_pwd) == 0) {
				alert.setTitle(getString(R.string.pwd_error));
				alert.setMessage(getString(R.string.pwd_msg_error));
				alert.show();
			}
			else {
				alert.setTitle(getString(R.string.fileds_error));
				alert.setMessage(ErrorMsg);
				alert.show();
			}

		}

		return !error;
	}

	public static void postNewRequest(final Context context, final String email, final String pwd, final DatabaseController appController){
		mPostCommentResponse = new PostCommentResponseListener() {

			ProgressDialog dialog ;

			@Override
			public void requestStarted() {
				Log.e(" requestStarted "," <<<>>>>");
				this.dialog = new ProgressDialog(context);
				this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				this.dialog.setIndeterminate(false);
				this.dialog.setCancelable(false);
				this.dialog.setMessage(context.getString(R.string.loading));
				this.dialog.show();

			}

			@Override
			public void requestEndedWithError(VolleyError error) {
				Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");
				this.dialog.dismiss();
			}

			@Override
			public void requestCompleted() {
				Log.e(" requestCompleted "," <<<>>>>");
				this.dialog.dismiss();
			}
		};
		mPostCommentResponse.requestStarted();
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest sr = new StringRequest(Request.Method.POST,UrlHelpers.GET_AUTHENTICATION_URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mPostCommentResponse.requestCompleted();
				Log.e(" onResponse : ","  "+response);

				ObjectMapper mapper = new ObjectMapper();
				try {
					UserAccount account = mapper.readValue(
							response.toString(), UserAccount.class);
					appController.saveUserToDB(account);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JsonNode input;
				JsonNode results = null;
				String token_id = "-1";
				try {
					input = mapper.readTree(response);
					results = input.get("token_id");

					if(results != null) {
						token_id = results.textValue();		            

						((Activity) context).findViewById(R.id.auth_space).setVisibility(View.GONE);
						((Activity) context).findViewById(R.id.create_account_space).setVisibility(View.GONE);
						((Activity) context).findViewById(R.id.authentication_home).setVisibility(View.VISIBLE);
						TextView  userAccount = (TextView) ((Activity) context).findViewById(R.id.userAccount);
						userAccount.setText(input.get("mail").textValue());
						userAccount.setTextColor(0xCC52514F);

						wmbPreference = PreferenceManager
								.getDefaultSharedPreferences(context);

						SharedPreferences.Editor editor = wmbPreference.edit();
						editor.putString("token_id", token_id);
						editor.putString("mail", input.get("mail").textValue());
						editor.commit();

						RoundRectShape rect = new RoundRectShape(
								new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
								null,
								null);

						ShapeDrawable bgOrders = new ShapeDrawable(rect);
						bgOrders.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						((Activity) context).findViewById(R.id.orders).setBackgroundDrawable(bgOrders);
						((Activity) context).findViewById(R.id.orders).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								getOrderHistory(context, v.getRootView());

							}
						});


						ShapeDrawable bgLogout = new ShapeDrawable(rect);
						bgLogout.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						((Activity) context).findViewById(R.id.logout).setBackgroundDrawable(bgLogout);
						((Activity) context).findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								wmbPreference = PreferenceManager
										.getDefaultSharedPreferences(context);
								SharedPreferences.Editor editor = wmbPreference
										.edit();
								editor.remove("token_id");
								editor.remove("mail");
								editor.commit();


								((Activity) context).findViewById(R.id.auth_space).setVisibility(View.VISIBLE);
								((Activity) context).findViewById(R.id.create_account_space).setVisibility(View.VISIBLE);
								((Activity) context).findViewById(R.id.authentication_home).setVisibility(View.GONE);

							}
						});



					}else {

						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setIcon(android.R.drawable.ic_dialog_info);
						alert.setTitle(context.getString(R.string.form_error));


						alert.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {

							@Override 
							public void onClick(DialogInterface dialog, int which) {


								dialog.dismiss();
							}
						});  
						alert.setMessage(" Echéc d'authentification ").show();

					}


				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			          


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mPostCommentResponse.requestEndedWithError(error);
			}
		}){
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("email",email);
				params.put("pwd",pwd);
				//	            params.put("comment", Uri.encode(comment));
				//	            params.put("comment_post_ID",String.valueOf(postId));
				//	            params.put("blogId",String.valueOf(blogId));

				return params;
			}

			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		queue.add(sr);
	}

	private static String mail;
	
//	@SuppressWarnings("unused")
//	private static String escapeNonAscii(String str) {
//
//		  StringBuilder retStr = new StringBuilder();
//		  for(int i=0; i<str.length(); i++) {
//		    int cp = Character.codePointAt(str, i);
//		    int charCount = Character.charCount(cp);
//		    if (charCount > 1) {
//		      i += charCount - 1; // 2.
//		      if (i >= str.length()) {
//		        throw new IllegalArgumentException("truncated unexpectedly");
//		      }
//		    }
//
//		    if (cp < 128) {
//		      retStr.appendCodePoint(cp);
//		    } else {
//		      retStr.append(String.format("\\u%x", cp));
//		    }
//		  }
//		  return retStr.toString();
//		}
//	
//	public static String escapeUnicode(String input) {
//		  StringBuilder b = new StringBuilder(input.length());
//		  Formatter f = new Formatter(b);
//		  for (char c : input.toCharArray()) {
//		    if (c < 128) {
//		      b.append(c);
//		    } else {
//		      f.format("\\u%04x", (int) c);
//		    }
//		  }
//		  return b.toString();
//		}

	public static void postNewAccountRequest(final Context context, View v, final DatabaseController appController, final Dialog window){

		final View view = v;

		mPostCommentResponse = new PostCommentResponseListener() {

			ProgressDialog dialog ;

			@Override
			public void requestStarted() {
				Log.e(" requestStarted "," <<<>>>>");
				this.dialog = new ProgressDialog(context);
				this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				this.dialog.setIndeterminate(false);
				this.dialog.setCancelable(false);
				this.dialog.setMessage(context.getString(R.string.loading));
				this.dialog.show();

			}

			@Override
			public void requestEndedWithError(VolleyError error) {
				Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");
				this.dialog.dismiss();
			}

			@Override
			public void requestCompleted() {
				Log.e(" requestCompleted "," <<<>>>>");
				this.dialog.dismiss();
				window.dismiss();
			}
		};
		mPostCommentResponse.requestStarted();
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest sr = new StringRequest(Request.Method.POST,UrlHelpers.GET_CREAT_ACCOUNT_URL, new Response.Listener<String>() {



			@Override
			public void onResponse(String response) {
				mPostCommentResponse.requestCompleted();
				Log.e(" onResponse : ","  "+response);

				wmbPreference = PreferenceManager
						.getDefaultSharedPreferences(context);

				SharedPreferences.Editor editor = wmbPreference
						.edit();

				String token = response.replace("[\"", "").replace("\"]", "");

				Log.e(" token : ","  "+token+"  token.contains(?[0-9]) : "+token.contains("?[0-9]"));
				
				boolean val = token.length()>=30 && token.length()<=38;
			    Log.e(" token : ","  "+token+"  num charachters "+val+" pas d'espace "+ !token.contains(" "));

			    if(!token.isEmpty() && val && !token.contains(" ")) {

				//if(!token.isEmpty() && token.contains("?[0-9]")) {

					editor.putString("token_id", token);
					editor.putString("mail", mail);
					editor.commit();
					
//					JsonNode input;
//					JsonNode results = null;
//					String token_id = "-1";
//
//						input = mapper.readTree(response);
//						results = input.get("token_id");
//
//
//							token_id = token.textValue();		            

							((Activity) context).findViewById(R.id.auth_space).setVisibility(View.GONE);
							((Activity) context).findViewById(R.id.create_account_space).setVisibility(View.GONE);
							((Activity) context).findViewById(R.id.authentication_home).setVisibility(View.VISIBLE);
							TextView  userAccount = (TextView) ((Activity) context).findViewById(R.id.userAccount);
							userAccount.setText(mail);//input.get("mail").textValue());
							userAccount.setTextColor(0xCC52514F);


					RoundRectShape rect = new RoundRectShape(
							new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
							null,
							null);

					ShapeDrawable bgOrders = new ShapeDrawable(rect);
					bgOrders.getPaint().setColor(colors.getColor(colors.getTitle_color()));
					((Activity) context).findViewById(R.id.orders).setBackgroundDrawable(bgOrders);
					((Activity) context).findViewById(R.id.orders).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							getOrderHistory(context, v.getRootView());

						}
					});


					ShapeDrawable bgLogout = new ShapeDrawable(rect);
					bgLogout.getPaint().setColor(colors.getColor(colors.getTitle_color()));
					((Activity) context).findViewById(R.id.logout).setBackgroundDrawable(bgLogout);
					((Activity) context).findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							wmbPreference = PreferenceManager
									.getDefaultSharedPreferences(context);
							SharedPreferences.Editor editor = wmbPreference
									.edit();
							editor.remove("token_id");
							editor.remove("mail");
							editor.commit();


							((Activity) context).findViewById(R.id.auth_space).setVisibility(View.VISIBLE);
							((Activity) context).findViewById(R.id.create_account_space).setVisibility(View.VISIBLE);
							((Activity) context).findViewById(R.id.authentication_home).setVisibility(View.GONE);

						}
					});


				}
				else {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setIcon(android.R.drawable.ic_dialog_info);
					alert.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {

						@Override 
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
						}
					}); 

					alert.setTitle(context.getString(R.string.fileds_error));

					String MSG = response.replace("[\"", "").replace("\"]", "").replace("\\u00e9", "é").replace("\\u00e0", "à");
					
					alert.setMessage(MSG);
					alert.show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mPostCommentResponse.requestEndedWithError(error);
			}
		}){


			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();

				EditText email = (EditText) view.findViewById(R.id.email);
				EditText pwd = (EditText) view.findViewById(R.id.pwd);

				EditText firstName = (EditText) view.findViewById(R.id.first_name);
				EditText lastName = (EditText) view.findViewById(R.id.last_name);

				EditText address = (EditText) view.findViewById(R.id.address);
				EditText complement = (EditText) view.findViewById(R.id.complement);
				EditText postalCode = (EditText) view.findViewById(R.id.postal_code);
				EditText city = (EditText) view.findViewById(R.id.city);
				//				Button Countries = (Button) view.findViewById(R.id.countries);

				EditText fixeIndicatif = (EditText) view.findViewById(R.id.fix_number_indicatif);
				EditText fixePhone = (EditText) view.findViewById(R.id.fix_phone);

				EditText mobileIndicatif = (EditText) view.findViewById(R.id.mobile_number_indicatif);
				EditText mobilePhone = (EditText) view.findViewById(R.id.mobile_phone);

				RadioGroup rGrp = (RadioGroup) view.findViewById(R.id.rGrpTitle);
				RadioButton selectRadio = (RadioButton)view.findViewById(rGrp.getCheckedRadioButtonId());
				//String civility = selectRadio.getTag().toString();


				//				params.put("adresse","23 rue Al Akbar");
				//				params.put("civilite",civility);
				//				params.put("code_postal","10020");
				//				params.put("complement_adresse","");
				//				params.put("email","loicbr@gmail.com");
				//				params.put("indice_tel_fixe","212");
				//				params.put("indice_tel_mobile","212");
				//				params.put("mot_de_passe","sponge");
				//				params.put("nom","Bruniquel");
				//				params.put("pays","1020");
				//				params.put("pays_tel_fixe",""+250);
				//				params.put("pays_tel_mobile",""+250);
				//				params.put("prenom","Loic");
				//				params.put("tel_fixe","123123123");
				//				params.put("tel_mobile","1231231234");
				//				params.put("ville","rabat");

				String value;


				value = address.getText().toString();
				if (!value.isEmpty()) {
					params.put("adresse",address.getText().toString());
				}


				if(selectRadio!= null) {
					String civility = selectRadio.getTag().toString();
					params.put("civilite",civility);
				}


				value = postalCode.getText().toString();
				if (!value.isEmpty()) {
					params.put("code_postal",postalCode.getText().toString());
				}

				params.put("email",email.getText().toString());  mail = email.getText().toString();

				value = fixeIndicatif.getText().toString();
				if (!value.isEmpty()) {
					params.put("indice_tel_fixe",fixeIndicatif.getText().toString());
				}

				value = mobileIndicatif.getText().toString();
				if (!value.isEmpty()) {
					params.put("indice_tel_mobile",mobileIndicatif.getText().toString());
				}

				params.put("mot_de_passe",pwd.getText().toString());

				value = lastName.getText().toString();
				if (!value.isEmpty()) {
					params.put("nom",lastName.getText().toString());
				}

				if (countryCode != null && !countryCode.isEmpty()) {
					params.put("pays",countryCode);
				}

				params.put("pays_tel_fixe",""+250);
				params.put("pays_tel_mobile",""+250);

				value = firstName.getText().toString();
				if (!value.isEmpty()) {
					params.put("prenom",firstName.getText().toString());
				}

				value = fixePhone.getText().toString();
				if (!value.isEmpty()) {
					params.put("tel_fixe",fixePhone.getText().toString());
				}

				value = mobilePhone.getText().toString();
				if (!value.isEmpty()) {
					params.put("tel_mobile",mobilePhone.getText().toString());
				}

				value = city.getText().toString();
				if (!value.isEmpty()) {
					params.put("ville",city.getText().toString());
				}

				//				params.put("adresse",address.getText().toString());
				//				params.put("civilite",civility);
				//				params.put("code_postal",postalCode.getText().toString());
				//				params.put("complement_adresse",complement.getText().toString());
				//				params.put("email",email.getText().toString());  mail = email.getText().toString();
				//				params.put("indice_tel_fixe",fixeIndicatif.getText().toString());
				//				params.put("indice_tel_mobile",mobileIndicatif.getText().toString());
				//				params.put("mot_de_passe",pwd.getText().toString());
				//				params.put("nom",lastName.getText().toString());
				//				params.put("pays",countryCode);
				//				params.put("pays_tel_fixe",""+250);
				//				params.put("pays_tel_mobile",""+250);
				//				params.put("prenom",firstName.getText().toString());
				//				params.put("tel_fixe",fixePhone.getText().toString());
				//				params.put("tel_mobile",mobilePhone.getText().toString());
				//				params.put("ville",city.getText().toString());



				return params;
			}

			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		queue.add(sr);
	}

	public static StringRequest postOrderNotification(final Context context,final String coffretId, final String CmdId, final String authToken){



		mPostCommentResponse = new PostCommentResponseListener() {

			//			ProgressDialog dialog ;

			@Override
			public void requestStarted() {
				Log.e(" requestStarted "," <<<>>>>");
				//				this.dialog = new ProgressDialog(context);
				//				this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				//				this.dialog.setIndeterminate(false);
				//				this.dialog.setCancelable(false);
				//				this.dialog.setMessage(context.getString(R.string.loading));
				//				this.dialog.show();
			}

			@Override
			public void requestEndedWithError(VolleyError error) {
				Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");
				//				this.dialog.dismiss();
				Toast.makeText(context, error+ " << "+ context.getString(R.string.resend_notification) +" # "+CmdId + " >> ", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void requestCompleted() {
				Log.e(" requestCompleted "," <<<>>>>");
				//				this.dialog.dismiss();
			}
		};
		mPostCommentResponse.requestStarted();

		StringRequest sr = new StringRequest(Request.Method.POST,UrlHelpers.GET_NOTIFICATION_ORDER_URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mPostCommentResponse.requestCompleted();
				Log.e(" onResponse : ","  "+response);

				Toast.makeText(context, context.getString(R.string.resend_notification) +" # "+CmdId, Toast.LENGTH_SHORT).show();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mPostCommentResponse.requestEndedWithError(error);
			}
		}){
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();

				params.put("id_coffret", coffretId);
				params.put("id_commande", CmdId);
				params.put("token_authen", authToken);

				return params;
			}

			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};

		return sr;
	}

	static ProgressDialog dialog ;

	public static void getOrderHistory(final Context context, final View rootView){


		mPostCommentResponse = new PostCommentResponseListener() {



			@Override
			public void requestStarted() {
				Log.e(" requestStarted "," <<<>>>>");
				dialog = new ProgressDialog(context);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setIndeterminate(false);
				dialog.setCancelable(false);
				dialog.setMessage(context.getString(R.string.loading));
				dialog.show();

			}

			@Override
			public void requestEndedWithError(VolleyError error) {
				Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");
				dialog.dismiss();
			}

			@Override
			public void requestCompleted() {
				Log.e(" requestCompleted ","<<<<>>>>");

			}
		};
		mPostCommentResponse.requestStarted();
		RequestQueue queue = Volley.newRequestQueue(context);

		StringRequest sr = new StringRequest(Request.Method.POST,UrlHelpers.GET_ORDER_HISTORY_URL/*"http://backoffice.paperpad.fr/install/BusinessAndroid/orders_test.json"/*+"commandes.json"*/, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				mPostCommentResponse.requestCompleted();
				Log.e(" onResponse : ","  "+response);

				LayoutInflater mInflater = LayoutInflater.from(context);

				LinearLayout container = new LinearLayout(context);
				container.setOrientation(LinearLayout.VERTICAL);

				ScrollView sc = new ScrollView(context);
				LinearLayout globalView = new LinearLayout(context);
				globalView.setOrientation(LinearLayout.VERTICAL);

				//LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				ObjectMapper mapper = new ObjectMapper();

				//OrderHistories orderHistories = null;
				List<OrderHistory> listOrderHistory = null;

				try {
					//					orderHistories = mapper.readValue(
					//								response.toString(), OrderHistories.class);
					listOrderHistory = mapper.readValue(
							response.toString(), new TypeReference<List<OrderHistory>>() {});


					//					if(orderHistories != null && orderHistories.getOrderHistory().size() > 0) {

					if(listOrderHistory != null && listOrderHistory.size() > 0) {

						RoundRectShape rect_ = new RoundRectShape(
								new float[] {5, 5, 5, 5, 5, 5, 5, 5},
								null,
								null);

						ShapeDrawable bgBtn = new ShapeDrawable(rect_);

						bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						View titleBarView = mInflater.inflate(R.layout.cancel_popup_order_history, null, false);
						titleBarView.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

						container.addView(titleBarView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


						for(int i = 0 ; i< listOrderHistory.size(); i++) {

							LayoutInflater inflater = LayoutInflater.from(context);
							final View view = inflater.inflate(R.layout.order_history, null, false);

							view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
							ListOrderHistoryAdapter adapter = new ListOrderHistoryAdapter(listOrderHistory.get(i).getProducts(), context);
							ListView list = new ListView(context);//(ListView)view.findViewById(android.R.id.boxsList);
							list.setAdapter(adapter);

							View v = mInflater.inflate(R.layout.footer_history_list, null, false);
							((TextView)v.findViewById(R.id.totalBoxPrice)).setText(""+adapter.getTotalPrice()+ " "+BoxsMainActivity.currency);
							((TextView)v.findViewById(R.id.totalBoxPrice)).setTextColor(colors.getColor(colors.getTitle_color()));
							v.setBackgroundColor(colors.getColor(colors.getBackground_color()));
							list.addFooterView(v);
							//adapter.setReelCount();
							((TextView)view.findViewById(R.id.orderId)).setTextColor(colors.getColor(colors.getTitle_color()));
							((TextView)view.findViewById(R.id.orderId)).setText(context.getString(R.string.commande)+""+listOrderHistory.get(i).getId_commande());

							Button resendNotif = (Button) view.findViewById(R.id.notificationOrderId);
							resendNotif.setBackgroundDrawable(bgBtn);
							final OrderHistory order = listOrderHistory.get(i);
							resendNotif.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									RequestQueue queue = Volley.newRequestQueue(context);

									for(int i = 0; i< order.getProducts().size(); i++){
										//Log.e("  Paramètres de requete à envoyé "," id_coffret : "+order.getProducts().get(i).getId_product()+" id_commande :  "+order.getId_commande()+" token_authen : "+wmbPreference.getString("token_id","-1"));
										queue.add(postOrderNotification(context, ""+order.getProducts().get(i).getId_product(), ""+order.getId_commande(), wmbPreference.getString("token_id","-1")));

									}
								}
							});

							View separatorView = new View(context);
							separatorView.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
							separatorView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));

							globalView.addView(separatorView, LinearLayout.LayoutParams.MATCH_PARENT, 50);

							globalView.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
							globalView.addView(list, LinearLayout.LayoutParams.MATCH_PARENT, getListViewHeightBasedOnChildren(list));
						}


						sc.addView(globalView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						container.addView(sc, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//						final PopupWindow pw = new PopupWindow(container, (int) context.getResources().getDimension(R.dimen.window_width), (int) context.getResources().getDimension(R.dimen.window_height), true);
//
//						pw.setOutsideTouchable(true);
//						pw.setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
//						pw.setFocusable(true);
//						pw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
						
						final Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
						  dialog.setContentView(container);
						  dialog.getWindow().setGravity(Gravity.CENTER);
						  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
						  dialog.getWindow().setLayout( (int) context.getResources().getDimension(R.dimen.window_width), (int) context.getResources().getDimension(R.dimen.window_height));
						  //dialog.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
						  dialog.getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
						  dialog.show();
						  
						titleBarView.setBackgroundDrawable(colors.makeGradientToColor(colors.getNavigation_color()));
						titleBarView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								//pw.dismiss();
								dialog.dismiss();
							}
						});


					}else {

						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setIcon(android.R.drawable.ic_dialog_info);
						alert.setTitle(context.getString(R.string.form_error));


						alert.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {

							@Override 
							public void onClick(DialogInterface dialog, int which) {


								dialog.dismiss();
							}
						});  
						alert.setMessage(" Echéc de récuperation d'historique de commandes ").show();

					}


				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				dialog.dismiss();


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mPostCommentResponse.requestEndedWithError(error);
			}
		}){
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				String token_authen = wmbPreference.getString("token_id","-1");
				//				token_authen = "29921001f2f04bd3baee84a12e98098f";  // for test
				params.put("token_authen",token_authen);
				return params;
			}

			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		queue.add(sr);
	}



	public interface PostCommentResponseListener {
		public void requestStarted();
		public void requestCompleted();
		public void requestEndedWithError(VolleyError error);
	}


	public void addOrderToCart(CartItem item){
		View cart_view = inflater.inflate(R.layout.cart_item, null, false);
		cart_view.setBackgroundColor(colors.getColor(colors.getAlternate_background_color(), "CC"));
		TextView box_name = (TextView)cart_view.findViewById(R.id.box_name);
		TextView recipient_name = (TextView)cart_view.findViewById(R.id.name_recipient);
		TextView box_price = (TextView)cart_view.findViewById(R.id.price_box);		

		box_name.setTextColor(colors.getColor(colors.getTitle_color()));
		recipient_name.setTextColor(colors.getColor(colors.getText_color()));
		box_price.setTextColor(colors.getColor(colors.getText_color()));

		try {
			box_name.setText(item.getMybox().getTitre_coffret());
		} catch (NullPointerException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String text = getString(R.string.care_of, item.getFirst_name(), item.getLast_name());
		recipient_name.setText(text);
		int price = 0;
		try {	
			price = Integer.parseInt(item.getPrice())*Integer.parseInt(item.getQuantity());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		total = total + price;
		box_price.setText(price+" "+BoxsMainActivity.currency);

		LinearLayout cart_item = (LinearLayout)cart_view.findViewById(R.id.cart_item);
		try {
			cart_item.setTag(item.getMybox());
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cart_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					BoxsPagerFragment  boxsPagerFragment = new BoxsPagerFragment();
					Bundle bundle = new Bundle();
					MyBox box = (MyBox)v.getTag();
					((BoxsMainActivity)getActivity()).id_selected_cat = box.getId();
					bundle.putInt("box_id", box.getId());
					bundle.putInt("category_id", box.getId_categorie());
					boxsPagerFragment.setArguments(bundle);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxsPagerFragment).addToBackStack(null).commit();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		ImageView close = (ImageView)cart_view.findViewById(R.id.close);
		close.setTag(item);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					final CartItem itemToDelete = (CartItem)v.getTag();
					final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
					alertDialog.setTitle("Confirmation");
					alertDialog.setMessage("Êtes-vous sûr de vouloir supprimer ce coffret de votre sélection?");
					DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							if (which == AlertDialog.BUTTON_POSITIVE) {
								alertDialog.dismiss();
							}else if (which == AlertDialog.BUTTON_NEGATIVE) {
								try {
									appController.getCartItemDao().delete(itemToDelete);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//								appController.getCartItemDao().delete(itemTmp);
								fillCart();
								alertDialog.dismiss();
							}

						}
					};
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Enlever", listener );
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Annuler", listener);
					alertDialog.show();




				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		cart_space.addView(cart_view);
		View line = new View(getActivity());
		line.setBackgroundColor(Color.parseColor("#88777777"));
		cart_space.addView(line, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));

	}


	public void fillCart(){
		total = 0;
		cart_space.removeAllViews();

		List<CartItem> items = new ArrayList<CartItem>();
		try {
			items = appController.getCartItemDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		TextView emptySelection = (TextView) view.findViewById(R.id.emptySelection);

		if(items == null || (items != null & items.size() == 0)) {

			emptySelection.setVisibility(View.VISIBLE);
			emptySelection.setBackgroundColor(colors.getColor(colors.getAlternate_background_color(),"CC"));
			emptySelection.setTextColor(colors.getColor(colors.getTitle_color()));
			view.findViewById(R.id.validate_order).setVisibility(View.GONE);

		}else {
			view.findViewById(R.id.validate_order).setVisibility(View.VISIBLE);
			emptySelection.setVisibility(View.GONE);
		}

		for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
			CartItem cartItem = (CartItem) iterator.next();
			addOrderToCart(cartItem);
		}
		View space_dummy = new View(getActivity());
		cart_space.addView(space_dummy, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.order_dim)));// TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.order_dim)/getResources().getDisplayMetrics().density));		
		total_txt.setText(total + " "+BoxsMainActivity.currency);
		//total_txt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        total_txt.setTextColor(colors.getColor(colors.getTitle_color()));

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
				//Log.e("  Country : "+countriesCode[i][0], " Country Code : "+countriesCode[i][1]+" Phone Code : "+countriesCode[i][2]);

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

						countryCode = items[(Integer) btn.getTag()][1];
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

	public static int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return 180;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;

		if(listAdapter.getCount() == 0)
			return 180;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		//ViewGroup.LayoutParams params = listView.getLayoutParams();
		// params.height 
		totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		//	    listView.setLayoutParams(params);
		//	    listView.requestLayout();
		if(totalHeight < 150)
			return 180;
		return totalHeight;
	}

	@Override
	public void onScrollChanged(
			com.paperpad.mybox.widgets.ObservableScrollView observableScrollView,
			int x, int y, int oldx, int oldy) {

		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(tmpView.getWindowToken(), 0);

	}



}
