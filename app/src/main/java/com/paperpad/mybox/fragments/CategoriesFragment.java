/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.activities.SplashActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.UrlHelpers;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.CategoriesGetter;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Console;
import com.paperpad.mybox.widgets.AViewFlipper;
import com.paperpad.mybox.widgets.AutoResizeTextView;
import com.paperpad.mybox.widgets.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class CategoriesFragment extends Fragment {

	
	protected List<CategoriesGetter> categories;
	private LinearLayout choiceHolder;
	private DatabaseController appController;
	private int current;
	private Console console;
	
	private SharedPreferences wmbPreference;
	
	protected PopupWindow pw;
	//private ImageView languageIcon;
	private RoundedImageView languageIcon;
	private boolean showFrench = true;
	private boolean showEnglish = true;
	private View view;
	
	public static String applicationTitle = "Hôtel Alpaga Magève";
	Bitmap bm;

	private Typeface font;//, font_discript;
	private static String POLICE;//, POLICE_DISCRIPT;
	
	CallbackCategory callbackCategory = new CallbackCategory() {
		
		@Override
		public void selectCategory(int id_cat) {
			Log.i(getTag(), "Dummy callback");
			
		}
	};
	private Colors colors;
	private boolean isTablet;

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		applicationTitle = getString(R.string.bandeau);
		appController = new DatabaseController(getActivity());
		try {
			categories = appController.getMyBoxCategoriesDao().queryForAll();
			/*if (categories.size()>0) {
				CategoriesGetter aCategory = categories.get(categories.size()-1);
				categories.add(0, aCategory);
				categories.remove(categories.size()-1);
			}*/
//			Collections.reverse(categories);;
//			Collections.sort(categories);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isTablet = getResources().getBoolean(R.bool.isTablet);
		
		wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		
		UrlHelpers.LANG_EXTRA = wmbPreference.getString(UrlHelpers.LANG, "&langue=fr");

		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		POLICE = "fonts/OpenSans-Semibold.ttf"; //gill-sans-light.ttf";
//		POLICE_DISCRIPT = "fonts/OpenSans-Regular.ttf"; //"fonts/gill-sans-light.ttf"; //mt-italic.ttf";
//
		font = Typeface.createFromAsset(getActivity().getAssets(), POLICE); 
//		font_discript = Typeface.createFromAsset(getActivity().getAssets(), POLICE_DISCRIPT);
		
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View 
		view = inflater.inflate(R.layout.categories_scroller_fragment, container, false);
		
		DatabaseController appController = new DatabaseController(getActivity());

		try {
			colors = appController.getColorsDao().queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		view.findViewById(R.id.SVChoiceContanair).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		view.findViewById(R.id.header).setBackgroundDrawable(colors.makeGradientToColor(colors.getNavigation_color()));//.setBackgroundColor(colors.getColor(colors.getNavigation_color()));

		AutoResizeTextView appTitle = (AutoResizeTextView) view.findViewById(R.id.appTitle);
		appTitle.setTypeface(font, Typeface.BOLD);
		appTitle.setTextColor(Color.parseColor("#CC000000")/*colors.getColor(colors.getText_color())*/);
		appTitle.setText(getString(R.string.bandeau));
		
		if(!getActivity().getResources().getBoolean(R.bool.isTablet))
			view.findViewById(R.id.header).setVisibility(View.GONE);
		
		int id_cat_init = 0;
		/*if (getArguments() != null) {
			id_cat_init = getArguments().getInt("id_category");
			id_cat_init = getIdInList(id_cat_init);
//			List<CategoriesGetter> cats = null;
//			
//			try {
//				cats = appController.getMyBoxCategoriesDao().queryForEq("id_categorie", id_cat_init);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (cats != null && cats.size()>0) {
//				id_cat_init = cats.get(0).getId();
//			}
		}*/
		id_cat_init = getIdInList(((BoxsMainActivity)getActivity()).id_selected_cat);
		choiceHolder = (LinearLayout)view.findViewById(R.id.choicesHolder);
		if (categories.size()>0) {
			categories.get(id_cat_init).setSelected(true);
			for (int i = 0; i < categories.size(); i++) {
				fillNavigationBar(categories.get(i));
			}
		}
		
		final View rootView = view;
		
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_5_27);
		@SuppressWarnings("deprecation")
		BitmapDrawable mDrawable = new BitmapDrawable(bm);
		if(getResources().getBoolean(R.bool.isTablet)) {
            ImageView about = (ImageView) view.findViewById(R.id.about);
			mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#CC000000")/*colors.getColor(colors.getTitle_color())*/, PorterDuff.Mode.MULTIPLY));
			//about.setBackgroundColor(Color.TRANSPARENT);
			//about.setBackgroundResource(R.drawable.icon_5_27);//
            about.setBackgroundDrawable(mDrawable);
			about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {									
				getAboutPage(rootView);
			}
		});
			
			languageIcon = (RoundedImageView)view.findViewById(R.id.languageIcon);
			languageIcon.setColor(colors.getColor(colors.getNavigation_color()));
			

			if (UrlHelpers.LANG_EXTRA.split("=")[1].equals("fr")) {
				languageIcon.setImageDrawable(getResources().getDrawable(
						R.drawable.fr));
				// change the locale to use other languages
				Utils.changeLocale("fr", getActivity());
			} else if (UrlHelpers.LANG_EXTRA.split("=")[1].equals("en2")) {
				languageIcon.setImageDrawable(getResources().getDrawable(
						R.drawable.en));

				Utils.changeLocale("en",getActivity());
			}
			
//			View maskIcon = view.findViewById(R.id.maskIcon);
//			maskIcon.setDrawingCacheEnabled(true);
//			//maskIcon.setBackgroundColor(colors.getColor(colors.getNavigation_color()));
//			Paint paint = new Paint();
//			paint.setColor(0x00000000);
//			paint.setStyle(Paint.Style.FILL);
//			Canvas canvas = new Canvas();
//			
//			maskIcon.draw(canvas);
//			canvas.drawCircle(maskIcon.getX() + maskIcon.getWidth()/2, maskIcon.getY() + maskIcon.getHeight()/2, maskIcon.getHeight()/2, paint);
//			
//			
//			new RoundedImageView(getActivity(), colors.getColor(colors.getNavigation_color()));
			
			UrlHelpers.updateUrlHelpers();
			
			final LayoutInflater tmpInflater = inflater;
			
			

			languageIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {									
				

//				mCallBack.onLanguageChanged();
				View layout = tmpInflater.inflate(
						R.layout.language_dialog, null);
				
				LinearLayout frenchContainer = (LinearLayout) layout
						.findViewById(R.id.frenchContainer);
				if (!showFrench) {
				
					frenchContainer.setVisibility(View.GONE);
				}
				LinearLayout englishContainer = (LinearLayout) layout
						.findViewById(R.id.englishContainer);

				if (showFrench) {
					frenchContainer.setVisibility(View.VISIBLE);

					frenchContainer
							.setOnClickListener(new OnClickListener() {


								@Override
								public void onClick(View v) {
									languageIcon.setImageDrawable(getResources()
											.getDrawable(
													R.mipmap.french_r));
									//langTxt.setTypeface(FONT_REGULAR);
									SharedPreferences.Editor editor = wmbPreference
											.edit();
									editor.putString(Utils.LANG, "&langue=fr");
									editor.commit();

									
									Intent intent = new Intent(getActivity(),SplashActivity.class);
									Bundle b = new Bundle();
									b.putString(Utils.LANG, "&langue=fr");

									intent.putExtras(b);
									startActivity(intent);
									pw.dismiss();
								}

								
							});
				} else {
					frenchContainer.setVisibility(View.GONE);
				}

				if (showEnglish) {
					englishContainer.setVisibility(View.VISIBLE);

					englishContainer
							.setOnClickListener(new OnClickListener() {


								@Override
								public void onClick(View v) {
									languageIcon.setImageDrawable(getResources()
											.getDrawable(
													R.mipmap.english_r));
									SharedPreferences.Editor editor = wmbPreference.edit();
									editor.putString(UrlHelpers.LANG, "&langue=en2");
									editor.commit();
						


//									Intent intent = new Intent();
									Intent intent = new Intent(getActivity(),SplashActivity.class);
									Bundle b = new Bundle();
									b.putString(UrlHelpers.LANG, "&langue=en2");


									intent.putExtras(b);
									startActivity(intent);
									pw.dismiss();
								}
							});
				} else {
					englishContainer.setVisibility(View.GONE);
				}

				
				pw = new PopupWindow(layout,
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, true);
				// display the popup in the center
				pw.setOutsideTouchable(true);
				pw.setSplitTouchEnabled(true);
//				pw.setBackgroundDrawable(new ColorDrawable(
//						android.R.color.transparent));
				pw.setFocusable(true);
				pw.setTouchInterceptor(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
//						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//							pw.dismiss();
//							return true;
//						}
						pw.dismiss();
						return true;
					}
				});
//				final OnTouchListener onTouch = new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						pw.dismiss();
//						return false;
//					}
//				};
//				languageIcon.setOnTouchListener(onTouch);
				pw.showAsDropDown(languageIcon);
//				pw.showAtLocation(layout, Gravity.LEFT, -500, 100);
//				
			
				
			}
		});

		}
		else {
			view.findViewById(R.id.about).setVisibility(View.GONE);
			view.findViewById(R.id.languageIcon).setVisibility(View.GONE);
			
			
			
		}
		return view;
	}
	
	
	private int getIdInList(int id) {
		for (int i = 0; i < categories.size(); i++) {
			if (id == categories.get(i).getId_categorie()) {
				return i;
			}
		}
		
		return 0;
	}

	/** a method to fill the upper bar where we choose the {@link CategoriesGetter}
	 * @param category
	 */
	private void fillNavigationBar(CategoriesGetter category) {
		
		RoundRectShape rect, selectRect;
		rect = new RoundRectShape(
				  new float[] {5, 5, 5, 5, 5 ,5, 5, 5},
				  null,
				  null);
		
		selectRect = new RoundRectShape(
				  new float[] {5, 5, 5, 5, 5 ,5, 5, 5},
				  null,
				  null);
		
		ShapeDrawable bg, selectBg;
		bg= new ShapeDrawable(rect);
		bg.getPaint().setColor(colors.getColor(colors.getNavigation_color(), "33"));
		
		selectBg = new ShapeDrawable(selectRect);
		selectBg.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		
	
		LinearLayout linLayout = new LinearLayout(getActivity());
		linLayout.setOrientation(LinearLayout.VERTICAL);
		ImageView arrow_down = new ImageView(getActivity());
		TextView categoryTxt = new TextView(getActivity());
		arrow_down.setScaleType(ScaleType.FIT_XY);
		Drawable empty_drawable = getResources().getDrawable(R.drawable.arrow_box_empty_c);
		
		Drawable arrow_d = getResources().getDrawable(R.drawable.arrow_box_cat_new);
		arrow_d.setColorFilter(colors.getColor(colors.getNavigation_color(), "33"), Mode.MULTIPLY);
		
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1);

//		categoryTxt.setAllCaps(true);
		LinearLayout.LayoutParams holderParams;
		holderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		if (category.isSelected() /*|| (category.getId_categorie()==0 && allselected)*/) {
			arrow_down.setImageDrawable(arrow_d);
			holderParams.gravity = Gravity.CENTER;
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setTextSize(categoryTxt.getTextSize());
			holderParams.setMargins(5, 0, 5, 0);
			categoryTxt.setPadding(7, 7, 7, 7);
			if (category.getId_categorie() == 0) {
				categoryTxt.setText(getResources().getString(R.string.all_boxs));
			}else {
				categoryTxt.setText(category.getName_categorie());
			} 
			categoryTxt.setTypeface(font, Typeface.BOLD);
			//categoryTxt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			categoryTxt.setTextSize(16);
			categoryTxt.setTextColor(colors.getColor(colors.getTitle_color()));
			//categoryTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_categories_selected));
			categoryTxt.setBackgroundDrawable(selectBg);//.setBackgroundColor(0xffffffff);
			categoryTxt.setTag(category);						
		}else {
			empty_drawable.setColorFilter(colors.getColor(colors.getBackground_color(), "00"), Mode.CLEAR);
			arrow_down.setImageDrawable(empty_drawable);
			textParams.setMargins(5, 5, 5, 5);
			holderParams.setMargins(5, 5, 5, 5);
			categoryTxt.setGravity(Gravity.CENTER);
			categoryTxt.setPadding(7, 7, 7, 7);
			holderParams.gravity = Gravity.CENTER;
			if (category.getId_categorie() == 0) {
				categoryTxt.setText(getResources().getString(R.string.all_boxs));
			}else {
				categoryTxt.setText((category.getName_categorie()));
			}
			//categoryTxt.setTextColor(Color.GRAY);
			//categoryTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_categories));
			categoryTxt.setTypeface(font, Typeface.BOLD);
			//categoryTxt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			categoryTxt.setTextColor(colors.getColor(colors.getText_color()));
			categoryTxt.setBackgroundDrawable(bg);//.setBackgroundColor(0x11000000);
			categoryTxt.setTag(category);
		}
		
		linLayout.addView(categoryTxt, textParams);
		
		LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 15);
		linLayout.addView(arrow_down, arrowParams);
		
		choiceHolder.addView(linLayout, holderParams);
		
//		view.findViewById(R.id.flagView).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 15));
		view.findViewById(R.id.flagView).setBackgroundColor(colors.getColor(colors.getBackground_color()));
		
		categoryTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CategoriesGetter cat = (CategoriesGetter)v.getTag();
				((BoxsMainActivity)getActivity()).id_selected_cat = cat.getId_categorie();
				for (int i = 0; i < categories.size(); i++) {
					categories.get(i).setSelected(false);
				}
				cat.setSelected(true);
				choiceHolder.removeAllViews();
				for (int i = 0; i < categories.size(); i++) {
					
					fillNavigationBar(categories.get(i));
				}
				if (MainContentFragment.displayMode == 0) {
					if (isTablet) {
						MainContentFragment.displayMode = 1;
					}
				}
				callbackCategory.selectCategory(((BoxsMainActivity)getActivity()).id_selected_cat);
				
				/*MainContentFragment.displayMode = 1;
				FooterFragment footerFragment = (FooterFragment)getChildFragmentManager().findFragmentByTag(BoxsMainActivity.FOOTER_FRAGMENT);
				if (footerFragment != null) {
					footerFragment.setDisplayMode(MainContentFragment.displayMode);
				}*/
//				boxs = getAllCategoryBoxs(cat);
//				myboxAdapter = new MyBoxCategoriesAdapter(boxs , getActivity());
//				list.setAdapter(myboxAdapter);
				
				
				
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public interface CallbackCategory{
		void selectCategory(int id_cat);
	}

	/**
	 * @return the callbackCategory
	 */
	public CallbackCategory getCallbackCategory() {
		return callbackCategory;
	}

	/**
	 * @param callbackCategory the callbackCategory to set
	 */
	public void setCallbackCategory(CallbackCategory callbackCategory) {
		this.callbackCategory = callbackCategory;
	}

	public void getAboutPage(View rootView) {
		
		
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View view = mInflater.inflate(R.layout.about_page, null, false);

		DatabaseController appController = new DatabaseController(getActivity());
		console = null;
		try {
			if (appController.getConsoleDao().queryForAll().size() == 1) {
				console = appController.getConsoleDao().queryForAll().get(0);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// get input stream
			InputStream ims = getActivity().getAssets().open("ribbon.png");
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);
			// set image to Background
			view.findViewById(R.id.about_page_bg).setBackgroundDrawable(d);
		}
		catch(IOException ex) {
		}
		
//		final PopupWindow pw = new PopupWindow(view, (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height), true);
//		
//		pw.setOutsideTouchable(true);
//		pw.setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
//		pw.setFocusable(true);

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);
		  dialog.setContentView(view);
		  dialog.getWindow().setGravity(Gravity.CENTER);
		  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
		  dialog.getWindow().setLayout( (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height));
		  //dialog.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		  //dialog.getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

		
		((TextView)view.findViewById(R.id.aboutTitle2)).setTextColor(colors.getColor(colors.getTitle_color()));
		((TextView)view.findViewById(R.id.aboutTitle3)).setTextColor(colors.getColor(colors.getTitle_color()));

		((TextView)view.findViewById(R.id.aboutDescription)).setTextColor(colors.getColor(colors.getText_color()));

		
		final AViewFlipper viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);
		viewFlipper.setFlipperColor(colors.getColor(colors.getTitle_color()));

		current = 0;

		TextView title = (TextView) view.findViewById(R.id.aboutTitle1);
		title.setTextColor(colors.getColor(colors.getTitle_color()));	
		title.setText(getString(R.string.app_name));
		//title.setText(String.format(getString(R.string.about_screen1), applicationTitle));
		
		ImageView imageView = (ImageView)view.findViewById(R.id.aboutImg_);//.findViewById(R.id.aboutImg);

		TextView discrip = (TextView)view.findViewById(R.id.aboutDescription);
		discrip.setTextColor(colors.getColor(colors.getText_color()));
		discrip.setText(Utils.removeHtmlTags(console.getPresentation_console()));

		//((TextView)view.findViewById(R.id.aboutDescription)).setText(console.getPresentation_console());

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


		Glide.with(getActivity()).load(console.getLien_image_presentation_console()).into(imageView);




			viewFlipper.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {


					int upX, downX = 0;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						downX = (int) event.getX();
						upX = (int) event.getX(); 
						return true;
					} 

					else if (event.getAction() == MotionEvent.ACTION_UP) {
						upX = (int) event.getX(); 
						
						if (upX - downX > 150 && current > 0) {

							current--;
//							if (current < 0) {
//								current = 2;
//							}

							viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left));
							viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right));

							viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
						} 

						else if (downX - upX > - 150 && current < 2) {

							current++;
//							if (current > 2) {
//								current = 0;
//							}


							viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right));
							viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_left));

							viewFlipper.showNext(); //.setImageResource(imgs[current]);
						}
						return true;
					}
					
					
					

					return false;

				}

			});
		
		//pw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
		dialog.show();
		
		view.findViewById(R.id.aboutBtnContainer).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
//		view.findViewById(R.id.aboutConditionBtn).setBackground(colors.makeGradientToColor(colors.getTitle_color()));//.setBackgroundColor(colors.getColor(colors.getTitle_color()));
//		view.findViewById(R.id.aboutContactBtn).setBackgroundColor(colors.getColor(colors.getTitle_color()));

		
		((TextView)view.findViewById(R.id.aboutTitle1)).setTextColor(colors.getColor(colors.getTitle_color()));
		((TextView)view.findViewById(R.id.aboutTitle2)).setTextColor(colors.getColor(colors.getTitle_color()));
		((TextView)view.findViewById(R.id.aboutTitle3)).setTextColor(colors.getColor(colors.getTitle_color()));
		((TextView)view.findViewById(R.id.aboutDescription)).setTextColor(colors.getColor(colors.getText_color()));

		
		Button condBtn = (Button) view.findViewById(R.id.aboutConditionBtn);
		Button contactBtn = (Button) view.findViewById(R.id.aboutContactBtn);
		
		RoundRectShape rectCondBtn = new RoundRectShape(
				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
				null,
				null);
		
		ShapeDrawable bgCondBtn = new ShapeDrawable(rectCondBtn);
		bgCondBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		condBtn.setBackgroundDrawable(bgCondBtn);
		contactBtn.setBackgroundDrawable(bgCondBtn);
		
		final Context context = getActivity();
		condBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mimeType = "text/html";
				String encoding = "utf-8";
				
				WebView webView = new WebView(getActivity());
				webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	        	  
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setIcon(android.R.drawable.ic_dialog_info);
					alert.setTitle(getString(R.string.general_condition_sale));
					
					
					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override 
						public void onClick(DialogInterface dialog, int which) {


							dialog.dismiss();
						}
					});  
					alert.setView(webView);
					alert.show();


				webView.getSettings().setJavaScriptEnabled(true);				
				webView.loadDataWithBaseURL(null, ReadFromfile("cgv.html",context), mimeType, encoding, null);//.loadData(ReadFromfile("cgv.html",getActivity()), mimeType, encoding);
				


			}
		});
		
		contactBtn.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				

				/* Create the Intent */
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				/* Fill it with Data */
				emailIntent.setType("plain/text");
				try {
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{console.getEmail_contact()});

					/* Send it off to the Activity-Chooser */
					getActivity().startActivity(Intent.createChooser(emailIntent, "Contact us..."));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto",console.getEmail_contact(), BoxsMainActivity.CATEGORIES_FRAGMENT));*/
				
			}
		});
		
		view.findViewWithTag("aboutClose").setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//pw.dismiss();
				dialog.dismiss();
			}
		});

		
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
	public void onDetach() {
		if(bm != null)
			bm.recycle();
		super.onDetach();
	}

}
