/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperpad.mybox.ApplicationInit;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.UrlHelpers;
import com.paperpad.mybox.helpers.jsonUtil.JsonWriter;
import com.paperpad.mybox.models.BillingAddress;
import com.paperpad.mybox.models.CartItem;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.ResponseCommand;
import com.paperpad.mybox.models.ShippingAddress;
import com.paperpad.mybox.widgets.ArrowImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author euphordev02
 *
 */
public class SummaryFragment extends Fragment{

	private DatabaseController appController;
	
	/**
	 *  0 send just mail or sms  1 : send a post package
	 */
	private int mode_order;
	private boolean cgv;

	private RequestQueue mRequestQueue;
	private ShippingAddress shippin_address;
	private BillingAddress billing_address;
	private Colors colors;
	private boolean isTablet;
	/**
	 * 
	 */
	public SummaryFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		appController = new DatabaseController(activity);
		//		if (getArguments() != null) {
		//			mode_order  = getArguments().getInt("MODE_ORDER");
		//		}
		mode_order = ((OrderValidationFragment)getParentFragment()).mode_order;
		cgv = ((OrderValidationFragment)getParentFragment()).AGREE_CGV ;
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		isTablet = getResources().getBoolean(R.bool.isTablet);

		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		if(isTablet)
			view = inflater.inflate(R.layout.order_summary_fragment, container, false);
		else
			view = inflater.inflate(R.layout.smart_order_summary_fragment, container, false);
		appController = new DatabaseController(getActivity());

		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.findViewById(R.id.scrollSummary).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		view.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getText_color()));

		((ArrowImageView)view.findViewById(R.id.billing_arrow)).setPaint(paint);
		((ArrowImageView)view.findViewById(R.id.shipping_arrow)).setPaint(paint);

		TableLayout tableLayout = (TableLayout)view.findViewById(R.id.table);
		List<CartItem> items = new ArrayList<CartItem>();
		try {
			items = appController.getCartItemDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (items.size()>0) {

			RoundRectShape rect_ = new RoundRectShape(
					new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
					null,
					null);

			ShapeDrawable bgBtn = new ShapeDrawable(rect_);
			bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
			int totalPrice = 0;
			for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
				CartItem cartItem = (CartItem) iterator.next();
				TableRow row;
				if(isTablet)
					row = (TableRow)inflater.inflate(R.layout.row_summary, null, false);
				else
					row = (TableRow)inflater.inflate(R.layout.smart_row_sammary, null, false);

				try {
					totalPrice = totalPrice + Integer.parseInt(cartItem.getPrice())*Integer.parseInt(cartItem.getQuantity());
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					TextView coffret_name = (TextView)row.findViewById(R.id.coffret_name);
					coffret_name.setText(cartItem.getMybox().getTitre_coffret());

					TextView coffret_desc = (TextView)row.findViewById(R.id.coffret_desc);
					coffret_desc.setText(Html.fromHtml(cartItem.getMybox().getIntroduction()).toString());
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				TextView text_name_benificiary = (TextView)row.findViewById(R.id.text_name_benificiary);
				text_name_benificiary.setText(cartItem.getFirst_name()+" "+cartItem.getLast_name());

				text_name_benificiary.setTextColor(colors.getColor(colors.getText_color()));

				TextView price_coffret = (TextView)row.findViewById(R.id.price_coffret);
				price_coffret.setText(cartItem.getPrice()+""+BoxsMainActivity.currency);

				TextView change_btn = (TextView) row.findViewById(R.id.change_btn_order_popup);
				change_btn.findViewById(R.id.change_btn_order_popup).setBackground(bgBtn);
				change_btn.setTextColor(colors.getColor(colors.getBackground_color()));
				final int item_id = cartItem.getId();
				change_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						BenificiaryFormFragment fragment = new BenificiaryFormFragment();
						Bundle bundle = new Bundle();
						bundle.putInt("CartItem", item_id);
						fragment.setArguments(bundle);
						getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
						replace(R.id.frame_content, fragment).addToBackStack(null).commit();

					}
				});

				row.findViewById(R.id.verticalSeparator1).setBackgroundColor(colors.getColor(colors.getText_color()));
				row.findViewById(R.id.verticalSeparator2).setBackgroundColor(colors.getColor(colors.getText_color()));

				View v  = new View(getActivity());
				v.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) 1.5));
				v.setBackgroundColor(colors.getColor(colors.getText_color(), "AA"));

				tableLayout.addView(row);
				tableLayout.addView(v);
			}

			TableRow total_row = (TableRow)inflater.inflate(R.layout.total_row, null, false);
			((TextView)total_row.findViewById(R.id.Total)).setTextColor(colors.getColor(colors.getText_color()));
			TextView total = (TextView)total_row.findViewById(R.id.price_total);
			total.setTextColor(colors.getColor(colors.getTitle_color()));
			total.setText(totalPrice+" "+BoxsMainActivity.currency);

			tableLayout.addView(total_row);
		}

		final ImageView radio_btn = (ImageView)view.findViewById(R.id.check_mark1);
		radio_btn.setOnClickListener(new OnClickListener() {

			private boolean isOff = true;;

			@Override
			public void onClick(View v) {

				if(isOff ) {
					cgv = true;
					((OrderValidationFragment)getParentFragment()).AGREE_CGV = true;
					radio_btn.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_on));
					isOff = false;
				}
				else {
					cgv = false;
					((OrderValidationFragment)getParentFragment()).AGREE_CGV = false;
					radio_btn.setImageDrawable(getResources().getDrawable(R.drawable.checkmark_off));
					isOff = true;
				}

			}
		});

		LinearLayout conditions_holder = (LinearLayout)view.findViewById(R.id.conditions_holder);
		conditions_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WebViewFragment fragment = new WebViewFragment();
				getParentFragment().getChildFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).addToBackStack("WebViewFragment").commit();

			}
		});
		((ArrowImageView)view.findViewById(R.id.cgv_arrow)).setPaint(paint);
				if (cgv) {
					((ImageView)view.findViewById(R.id.check_mark1)).setImageDrawable(getResources().getDrawable(R.drawable.checkmark_on));
				}
		LinearLayout billing_holder = (LinearLayout)view.findViewById(R.id.billing_holder);
		billing_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderAddressFragment fragment = new OrderAddressFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("TYPE", 0); // 1 is billing ; 2 is shipping
				bundle.putInt("MODE_ORDER", mode_order);
				fragment.setArguments(bundle);
				getParentFragment().getChildFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.frame_content, fragment, "OrderAddressFragment").addToBackStack("OrderAddressFragment").commit();

			}
		});

		LinearLayout shipping_holder = (LinearLayout)view.findViewById(R.id.shipping_holder);
		shipping_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderAddressFragment fragment = new OrderAddressFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("TYPE", 1); // 1 is billing ; 2 is shipping
				bundle.putInt("MODE_ORDER", mode_order);
				fragment.setArguments(bundle);
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
				.replace(R.id.frame_content, fragment, "OrderAddressFragment").addToBackStack("OrderAddressFragment").commit();

			}
		});

		if (mode_order == 0) {
			shipping_holder.setVisibility(View.GONE);
		}

		try {
			if (appController.getBillingAddressDao().queryForAll().size() >0) {
				billing_address = appController.getBillingAddressDao().queryForAll().get(0);
				TextView billing_txt = (TextView) view.findViewById(R.id.billing_txt);
				try {
					String txtt =  String.format(getString(R.string.text_address), "", billing_address.getPrenom()!=null?billing_address.getPrenom():"", billing_address.getNom()!=null?billing_address.getNom():"", 
							billing_address.getAdresse()!=null?billing_address.getAdresse():"", billing_address.getVille()!=null?billing_address.getVille():"", billing_address.getCode_postale()+"" );
					
					String firstLine = billing_address.getCivilite()+txtt.split("\n")[0];
					String secondLine = txtt.split("\n")[1];
									    
				    Spannable wordtoSpan = new SpannableString(firstLine+"\n"+secondLine);        
				    wordtoSpan.setSpan(new ForegroundColorSpan(colors.getColor(colors.getNavigation_color())), 0, firstLine.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				    wordtoSpan.setSpan(new ForegroundColorSpan(colors.getColor(colors.getText_color(), "77")), firstLine.length(), firstLine.length() + secondLine.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				    billing_txt.setText(wordtoSpan);
					
//					billing_txt.setText(txtt/*getString(R.string.text_address, address.getPrenom()!=null?address.getPrenom():"", address.getNom()!=null?address.getNom():"", 
//							address.getAdresse()!=null?address.getAdresse():"", address.getVille()!=null?address.getVille():"", address.getCode_postale()+"" )*/);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (mode_order == 1 && appController.getShippingAddressDao().queryForAll().size() >0) {
				shippin_address = appController.getShippingAddressDao().queryForAll().get(0);	
				TextView shipping_txt = (TextView) view.findViewById(R.id.shipping_txt);
				try {
					String txtt =  String.format(getString(R.string.text_address), "", shippin_address.getPrenom()!=null?shippin_address.getPrenom():"", shippin_address.getNom()!=null?shippin_address.getNom():"", 
							shippin_address.getAdresse()!=null?shippin_address.getAdresse():"", shippin_address.getVille()!=null?shippin_address.getVille():"", shippin_address.getCode_postale()+"" );

					String firstLine = shippin_address.getCivilite()+txtt.split("\n")[0];
					String secondLine = txtt.split("\n")[1];
					
				    Spannable wordtoSpan = new SpannableString(firstLine+"\n"+secondLine);        
				    wordtoSpan.setSpan(new ForegroundColorSpan(colors.getColor(colors.getNavigation_color())), 0, firstLine.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				    wordtoSpan.setSpan(new ForegroundColorSpan(colors.getColor(colors.getText_color(), "77")), firstLine.length(), firstLine.length() + secondLine.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				    shipping_txt.setText(wordtoSpan);
					
				    
					
					//shipping_txt.setText(txtt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Button cancel = (Button)view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				if (!getChildFragmentManager().popBackStackImmediate()) {
				//	            }
				//				FragmentManager fm = getChildFragmentManager();
				//			    fm.popBackStack();
				ValidationModeFragment fragment = new ValidationModeFragment();
				getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
				.replace(R.id.frame_content, fragment).addToBackStack(null).commit();


			}
		});
		
		((TextView)view.findViewById(R.id.beneficiary_title)).setTextColor(colors.getColor(colors.getTitle_color()));

		Button confirm = (Button)view.findViewById(R.id.confirm);
		confirm.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				if (verify_complete_infos()) {
					mRequestQueue = ((ApplicationInit)getParentFragment().getActivity().getApplication()).mRequestQueue;
					StringRequest myReq = new StringRequest(Method.POST,
							UrlHelpers.SVAE_COMMANDE, new Listener<String>() {

						@Override
						public void onResponse(String response) {
							//									Toast.makeText(getParentFragment().getActivity(), response, Toast.LENGTH_SHORT).show();
							//									Log.i("SummaryFragment", response);
							ObjectMapper mapper = new ObjectMapper();
							try {
								ResponseCommand resp = mapper.readValue(response, ResponseCommand.class);
								Log.i(getTag(), resp.getPaiemant_http());
								if (resp != null && !resp.getPaiemant_http().isEmpty()) {
									PayementFragment fragment = new PayementFragment();
									Bundle bundle = new Bundle();
									bundle.putString("link", resp.getPaiemant_http());
									bundle.putInt("total_command", resp.getTotal_commande());
									fragment.setArguments(bundle);
									getParentFragment().getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
									.replace(R.id.frame_content, fragment).addToBackStack(null).commit();
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
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(getParentFragment().getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

						}
					}) {

						protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
							Map<String, String> params = new HashMap<String, String>();
							SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(getParentFragment().getActivity());
							String token_id = wmbPreference.getString("token_id", "");
							params.put("token_authen", token_id);
							params.put("langue", "fr");
							if (mode_order == 1) {
								if (shippin_address != null) {
									params.put("adresse_livraison", JsonWriter.writeAddressAccount(shippin_address.getAddressAccount()));
								}else {
									getParentFragment().getActivity().runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											Toast.makeText(getParentFragment().getActivity(), "Adresse de livraison manquante", Toast.LENGTH_SHORT).show();
										}
									});
									
								}
								
							}
							if (billing_address != null) {
								params.put("adresse_facturation", JsonWriter.writeAddressAccount(billing_address.getAddressAccount()));
							}else {
								getParentFragment().getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(getParentFragment().getActivity(), "Adresse de facturation manquante", Toast.LENGTH_SHORT).show();
									}
								});
								
							}
							
							try {
								List<CartItem> items = appController.getCartItemDao().queryForAll();
								if (items.size() > 0) {
									params.put("products", JsonWriter.writeProducts(items));
								}

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							params.put("mode_envoi", "1");
							return params;
						};
					};

					mRequestQueue.add(myReq);
				}else {
					Toast.makeText(getParentFragment().getActivity(), "Remplissez toutes les informations", Toast.LENGTH_SHORT).show();
				}

			}
		});
		return view;
	}

	private boolean verify_complete_infos(){
		boolean isFilled = false;
		try {
			if (!(appController.getBillingAddressDao().queryForAll().size() >0)) {
				isFilled = false;
			}else {
				isFilled = true;
			}
			if (mode_order == 0) {
				
			}else if(mode_order == 1) {
				if (appController.getShippingAddressDao().queryForAll().size() >0) {
					isFilled = isFilled && true;
				}else  {
					isFilled = isFilled && false;
				}
			}
			if (cgv) {
				isFilled = isFilled && true;
			}else {
				isFilled = isFilled && false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isFilled;

	}

}
