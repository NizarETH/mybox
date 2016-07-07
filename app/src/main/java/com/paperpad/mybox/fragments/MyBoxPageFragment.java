package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.Beneficiary;
import com.paperpad.mybox.models.CartItem;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Illustration;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.models.StringValidityBox;
import com.paperpad.mybox.util.Flip3dAnimation;
import com.paperpad.mybox.util.FlipAnimation;
import com.paperpad.mybox.widgets.AViewFlipper;
import com.paperpad.mybox.widgets.ObservableScrollView;
import com.paperpad.mybox.widgets.ScrollViewListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyBoxPageFragment extends Fragment implements ScrollViewListener{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
//		applyRotation(0, 180, flip2);
//		flip2.setVisibility(View.GONE);
		super.onViewCreated(view, savedInstanceState);
	}



//	private static int IS_HEADER_ADDED = 1;
	private DatabaseController appController;
//	private BoxsMainActivity mainActivity;
//	private LayoutInflater layoutInflater;
	MyBox box;
	private LinearLayout flip2;
	private RelativeLayout itemOrder;
	private LinearLayout flip1;

	

	private int current;
	private AViewFlipper viewFlipper;
	private int downX,upX;
	private Colors colors;
	
	private Typeface font;
	private static String POLICE;
	
	public View tmpView;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		
		((BoxsMainActivity)getActivity()).bodyFragment = "MyBoxPageFragment";

		appController = new DatabaseController(getActivity());
		if (getArguments() != null) {
			int box_id = getArguments().getInt("box_id");
			
			if (box_id != 0) {
				try {
					box = appController.getMyBoxDao().queryForId(box_id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		super.onAttach(activity);
	}
	
	public static MyBoxPageFragment create(MyBox box){
		
		MyBoxPageFragment fragment = new MyBoxPageFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("box_id", box.getId());
		bundle.putInt("category_id", box.getId_categorie());
		fragment.setArguments(bundle);
		return fragment  ;
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		layoutInflater = inflater;
		View view;

		if(getActivity().getResources().getBoolean(R.bool.isTablet)){
			view = inflater.inflate(R.layout.mybox_page_fragment, container, false);
		}
		else{
			view = inflater.inflate(R.layout.smart_mybox_page_fragment, container, false);
		}
		
		
		DatabaseController appController = new DatabaseController(getActivity());
		String validity = box.getDuree_de_validitee();
		TextView valid = (TextView)view.findViewById(R.id.valid);
		try {
			int length = validity.length();
			
			
			if(length > 0 && length <= 3) {
				valid.setText(String.format(getString(R.string.order_validity), validity+" "+getString(R.string.day)+" "+getString(R.string.date_sale)+" "));
			}else if(length > 10) {
				
				String from, to;
				
						Log.e(" validity : "," "+validity);
						if(validity.contains("du")) {
							
							from = "du";
							to = "à";
							
						}else {
							
							from = "from";
							to = "to";
						}
						
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(validity.split(from)[1]);//.split("to")[0]);
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date1);
				
				Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(validity.split(to)[1]);//.split("to")[1]);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(date2);

				
//				page.setLocalDate(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(), date.getTime(), 20));

				valid.setText(String.format(getString(R.string.order_validity),
						getString(R.string.from)+" "+c1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
								Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(),
										date1.getTime(), 20)+" "+getString(R.string.to)+" "+
								c2.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
										Locale.getDefault())+" "+DateUtils.formatDateTime(getActivity(),
												date2.getTime(), 20)));

			}else {
				valid.setVisibility(View.GONE);
			}
			//((TextView)view.findViewById(R.id.valid)).setText(String.format(getString(R.string.order_validity), validity));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		view.findViewById(R.id.picHolder).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));

		//		if (box != null) {
		//view.setBackgroundColor(Color.parseColor("#ffffff")); // todo
		drawValidity(view);
		colorDays(view);
		TextView myboxInfoTV = (TextView)view.findViewById(R.id.myboxInfoTV);
		myboxInfoTV.setText(box.getTitre_coffret());
		//			myboxInfoTV.setTextColor(colors.getColor(colors.getTitle_color())); // todo
		//myboxInfoTV.setTextSize(20);
		myboxInfoTV.setTextColor(colors.getColor(colors.getTitle_color()));
		myboxInfoTV.setTypeface(font, Typeface.BOLD);
		int htmlSize = (int)getResources().getDimension(R.dimen.html_txt);
		
		if(!getResources().getBoolean(R.bool.isTablet)) {
			htmlSize = 12;
		}
		else if(htmlSize > 16)
			htmlSize = 15;
		else if(htmlSize >= 13 && htmlSize <= 16)
			htmlSize = 16;
		
		valid.setTextSize(htmlSize);
		valid.setTextColor(colors.getColor(colors.getText_color()));
		Log.e(" (int)getResources().getDimension(R.dimen.html_txt) ",""+htmlSize);
		
		WebView myboxInfoWV = (WebView)view.findViewById(R.id.myboxInfoWV);
		myboxInfoWV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		myboxInfoWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		myboxInfoWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		StringBuilder htmlString = new StringBuilder();
		int[] colorText = Utils.hex2Rgb(colors.getText_color());  // todo
		myboxInfoWV.setBackgroundColor(Color.TRANSPARENT);
		htmlString.append("<html lang='fr'><head>" +
				"<meta charset='utf-8'>" +
				" <title>paperpad</title>  "
				+ "<style type=\"text/css\" media=\"screen\">" +
				"body {font-size: "+htmlSize+"px; "
				+ "background-color: transparent; color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 1); " //
				+ "font-family:gill-sans-light;"
				+ "} a { color: rgba("+colorText[0]+", "+colorText[1]+", "+colorText[2]+", 1);  } "
				+ "p:first-child, div:first-child { margin-top: 0; }    </style></head><body id=\"body_element\"><div id=\"content\">    ");
		htmlString.append(box.getDescription());
		myboxInfoWV.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null); 
		TextView personne = (TextView)view.findViewById(R.id.personne);
		Resources res = getResources();
		personne.setTextColor(colors.getColor(colors.getTitle_color()));//Color.parseColor("#000000")); // todo
		String text = String.format(res.getString(R.string.people), box.getNombre_personnes());
		personne.setText(text);
		
		TextView personneBox = (TextView)view.findViewById(R.id.personneBox);
		personneBox.setTextColor(colors.getColor(colors.getTitle_color()));//Color.parseColor("#000000")); // todo
		personneBox.setText(text);
		
		TextView boxPrice = (TextView)view.findViewById(R.id.boxPrice);
		boxPrice.setText(box.getPrix()+ ""+BoxsMainActivity.currency);
		boxPrice.setTextColor(colors.getColor(colors.getTitle_color()));//Color.parseColor("#000000")); // todo
		
		TextView boxPriceBox = (TextView)view.findViewById(R.id.boxPriceBox);
		boxPriceBox.setText(box.getPrix()+ ""+BoxsMainActivity.currency);
		boxPriceBox.setTextColor(colors.getColor(colors.getTitle_color()));//Color.parseColor("#000000"));
		
		itemOrder = (RelativeLayout)view.findViewById(R.id.itemOrder);
		
		RoundRectShape rect = new RoundRectShape(
				new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
				null,
				null);

		ShapeDrawable bgBtnSelected = new ShapeDrawable(rect);
		bgBtnSelected.getPaint().setColor(colors.getColor(colors.getNavigation_color()));

		ShapeDrawable bgBtn = new ShapeDrawable(rect);
		bgBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
		
		
		
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, bgBtnSelected);
		drawable.addState(new int[]{android.R.attr.state_focused}, bgBtnSelected);
		drawable.addState(new int[]{}, bgBtn);
		
		itemOrder.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		LinearLayout orderBox = (LinearLayout)view.findViewById(R.id.orderBox);
		orderBox.setBackgroundDrawable(drawable);
		LinearLayout infoBox = (LinearLayout)view.findViewById(R.id.infoBox);
		infoBox.setBackgroundColor(Color.parseColor("#1f000000"));;
//		itemOrder.setFocusable(false);
//		itemOrder.setClickable(false);
//		itemOrder.setPadding(10, 0, 10, 0);

		final View rootView = view;
		orderBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				createFormRecipient(rootView);
				
			}
		});
		
		
		final TextView quantity = (TextView) view.findViewById(R.id.quantityNumber);
		quantity.setTextColor(colors.getColor(colors.getText_color()));
		
		TextView btnPlus = (TextView) view.findViewById(R.id.btnPlus);
		final TextView btnMinus = (TextView) view.findViewById(R.id.btnMinus);
		
		RoundRectShape rect_ = new RoundRectShape(
				new float[] {3, 3, 3, 3, 3 , 3, 3, 3},
				null,
				null);

//		ShapeDrawable bgBtnSelect = new ShapeDrawable(rect_);
//		bgBtnSelect.getPaint().setColor(colors.getColor(colors.getTitle_color()));

		final ShapeDrawable bgBtnNormal = new ShapeDrawable(rect_);
		bgBtnNormal.getPaint().setColor(colors.getColor(colors.getNavigation_color()));

//		final StateListDrawable drawablePlus = new StateListDrawable();
//		drawable.addState(new int[]{android.R.attr.state_pressed}, bgBtnSelect);
//		drawable.addState(new int[]{android.R.attr.state_focused}, bgBtnSelect);
//		drawable.addState(new int[]{}, bgBtnNormal);
//		
//		final StateListDrawable drawableMinus = new StateListDrawable();
//		drawable.addState(new int[]{android.R.attr.state_pressed}, bgBtnSelect);
//		drawable.addState(new int[]{android.R.attr.state_focused}, bgBtnSelect);
//		drawable.addState(new int[]{}, bgBtnNormal);
//		
//		btnPlus.setBackground(drawablePlus);
//		btnMinus.setBackground(drawableMinus);

		btnPlus.setBackgroundDrawable(bgBtnNormal);

		btnPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TextView quantity = (TextView) rootView.findViewById(R.id.quantityNumber);
				int q = Integer.parseInt((String) quantity.getText().toString());
				quantity.setText(""+(++q));
				if(q > 1) {
					//btnMinus.setBackground(drawableMinus);
					btnMinus.setBackgroundDrawable(bgBtnNormal);
				}
			}
		});
		
		btnMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TextView quantity = (TextView) rootView.findViewById(R.id.quantityNumber);
				int q = Integer.parseInt((String) quantity.getText().toString());
				quantity.setText(""+(--q));
				if(q <= 1) {
					quantity.setText("1");
					btnMinus.setBackgroundColor(colors.getColor(colors.getText_color(),"CC"));
				}				
			}
		});

		
//		ArrowImageView arrowImg = (ArrowImageView)view.findViewById(R.id.imgArrow);
//		Paint paint = new Paint();
//		paint.setColor(Color.parseColor("#88ffffff"));
//		arrowImg.setPaint(paint);

		TextView orderOnline = (TextView)view.findViewById(R.id.orderOnline);

//		ImageView imageBox = (ImageView)view.findViewById(R.id.imageBox);

//		ColorStateList colorStateList = new ColorStateList(
//				new int[][] {new int[] { android.R.attr.state_pressed }, new int[] {} },
//				new int[] {Color.parseColor("#d560d1"), Color.parseColor("#000000")});

		orderOnline.setTextColor(colors.getColor(colors.getBackground_color()));//Color.parseColor("#ffffff"));
		//			orderOnline.setTextColor(colors.getColor(colors.getBackground_color()));
		
		final Collection<Illustration> images = box.getIllustrations();
		String path = "";
		
		if (images.size() > 0) {

			/**  Uness Modif **/
			///ImageSwitcher  
			viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);
			
			if(viewFlipper != null)
			viewFlipper.setFlipperColor(colors.getColor(colors.getTitle_color()));

			current = 0;

			Illustration illust = null;
			//int i = 0;
			for(Iterator<Illustration> iterator = images.iterator(); iterator.hasNext();){
				
				if(viewFlipper == null)break;

				illust = iterator.next();

				ImageView imageView = new ImageView(getActivity().getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

				viewFlipper.addView(imageView);

				if (!illust.getPath().isEmpty()) {
					path = illust.getPath();
					//				paths[i] = illust.getPath();
					Glide.with(getActivity()).load(new File(path)).fitCenter().into(imageView);
				}else {
					path = illust.getLink();
					Glide.with(getActivity()).load(path).fitCenter().into(imageView);
				}

				current++;
			}	


			/** Uness Modif **/
			if(current > 1)
				viewFlipper.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {


						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							downX = (int) event.getX(); 
							return true;
						} 

						else if (event.getAction() == MotionEvent.ACTION_UP) {
							upX = (int) event.getX(); 
							
							if (upX - downX > 100) {

								current--;
								if (current < 0) {
									current = images.size() - 1;
								}

								viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_left));
								viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_right));

								viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
							} 

							else if (downX - upX > - 100) {

								current++;
								if (current > images.size() - 1) {
									current = 0;
								}


								viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_in_right));
								viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_left));

								viewFlipper.showNext(); //.setImageResource(imgs[current]);
							}
							return true;
						}

						return false;

					}

				});

		}

		
//		if (box.getIllustrations().size()>0) {
//			Illustration illust = box.getIllustrations().iterator().next();
//			imageBox.setScaleType(ScaleType.CENTER_CROP);
//			if (!illust.getPath().isEmpty()) {
//				Glide.with(getActivity()).load(new File(illust.getPath())).into(imageBox);
//			}else {
//				Glide.with(getActivity()).load(illust.getLink()).into(imageBox);
//			}
//		}


		// coloring
		((TextView) view.findViewById(R.id.jours_de_disponibilite)).setTextColor(colors.getColor(colors.getText_color()));//Color.parseColor("#000000"));
		((TextView) view.findViewById(R.id.offrez)).setTextColor(colors.getColor(colors.getText_color()));//Color.parseColor("#000000"));//ofrrez
		TextView pers_num = (TextView)view.findViewById(R.id.pers_number);
		TextView price_num = (TextView)view.findViewById(R.id.pers_price);
		pers_num.setText(text);
		price_num.setText(box.getPrix()+ ",00 "+BoxsMainActivity.currency);
		
		pers_num.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
		price_num.setBackgroundColor(colors.getColor(colors.getTitle_color()));

		pers_num.setTextColor(colors.getColor(colors.getTitle_color()));
		price_num.setTextColor(colors.getColor(colors.getBackground_color()));

		
		flip2 = (LinearLayout)view.findViewById(R.id.flip2);
		flip1 = (LinearLayout)view.findViewById(R.id.flip1);
		ViewTreeObserver vto = flip2.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				flipCard(flip2, flip1, itemOrder);
				
				ViewTreeObserver obs = flip2.getViewTreeObserver();
				
		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		            obs.removeOnGlobalLayoutListener(this);
		        } else {
		            obs.removeGlobalOnLayoutListener(this);
		        }
			}
		});
		return view;


	}

	private void colorDays(View view) {
		((TextView)view.findViewById(R.id.sundayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.saturdayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.fridayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.thursdayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.wednesdayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.tuesdayTxt)).setTextColor(Color.parseColor("#777777"));
		((TextView)view.findViewById(R.id.mondayTxt)).setTextColor(Color.parseColor("#777777"));
		
	}

	private void drawValidity(View view) {
		Collection<StringValidityBox> validity = box.getValiditee1();
		
		for (Iterator iterator = validity.iterator(); iterator.hasNext();) {
			StringValidityBox stringValidityBox = (StringValidityBox) iterator
					.next();
			if (stringValidityBox.getString().equals("monday")) {
				ImageView imgMonday = (ImageView)view.findViewById(R.id.imgMonday);
				imgMonday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("tuesday")) {
				ImageView imgTuesday = (ImageView)view.findViewById(R.id.imgTuesday);
				imgTuesday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("wednesday")) {
				ImageView imgWednesday = (ImageView)view.findViewById(R.id.imgWednesday);
				imgWednesday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("thursday")) {
				ImageView imgThursday = (ImageView)view.findViewById(R.id.imgThursday);
				imgThursday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("friday")) {
				ImageView imgFriday = (ImageView)view.findViewById(R.id.imgFriday);
				imgFriday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("saturday")) {
				ImageView imgSaturday = (ImageView)view.findViewById(R.id.imgSaturday);
				imgSaturday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}else if (stringValidityBox.getString().equals("sunday")) {
				ImageView imgSunday = (ImageView)view.findViewById(R.id.imgSunday);
				imgSunday.setImageDrawable(getResources().getDrawable(R.drawable.available));
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
		
		POLICE = "fonts/OpenSans-Regular.ttf"; //gill-sans-light.ttf";
		font = Typeface.createFromAsset(getActivity().getAssets(), POLICE); 

		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	/**
	 * 
	 */
	public MyBoxPageFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	public Double total = 0.0;
	private Beneficiary beneficiary;
	private EditText first_name;
	private EditText last_name;
	private EditText email;
	private EditText message;
	
	private View getNewDivider() {
		View divider = new View(getActivity());
		divider.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		divider.setBackgroundColor(Color.parseColor("#777777"));
		return divider;
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	
	private void applyRotation(float start, float end, View view) {
		// Find the center of image
		final float centerX = view.getWidth() / 2.0f;
		final float centerY = view.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(180, 0, centerX, centerY);
		final Flip3dAnimation rotation2 = new Flip3dAnimation(0, 180, centerX, centerY);
		rotation.setStartOffset(1000);
		rotation.setDuration(1500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation2.setStartOffset(1000);
		rotation2.setDuration(1500);
		rotation2.setFillAfter(true);
		rotation2.setInterpolator(new AccelerateInterpolator());
//		rotation.setAnimationListener(new DisplayNextView(isFirstImage, image1, image2));

//		if (isFirstImage)
//		{
		view.startAnimation(rotation); // "YourLayout"
		flip1.startAnimation(rotation2);
//		} else {
//			image2.startAnimation(rotation); // "Your Other Layout"
//		}

	}
	
	
	private void flipCard(View cardFace, View cardBack, View rootLayout)
	{
	    FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);
	 
	    if (cardFace.getVisibility() == View.GONE)
	    {
	        flipAnimation.reverse();
	    }
	    rootLayout.startAnimation(flipAnimation);
	}
	

	
	public void createFormRecipient(final View rootView) {

		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		final View view = mInflater.inflate(R.layout.beneficiaire, null, false);
		first_name = (EditText)view.findViewById(R.id.first_name);
		last_name = (EditText)view.findViewById(R.id.last_name);
		email = (EditText)view.findViewById(R.id.email);
		message = (EditText)view.findViewById(R.id.complement);
		appController = new DatabaseController(getActivity());
		try {
			beneficiary = appController.getBeneficiaryDao().queryForAll().get(0);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (beneficiary != null) {
			try {
				first_name.setText(beneficiary.getFirst_name());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				last_name.setText(beneficiary.getLast_name());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				email.setText(beneficiary.getEmail());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				message.setText(beneficiary.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			beneficiary = new Beneficiary();
		}
		
		tmpView = view;
		com.paperpad.mybox.widgets.ObservableScrollView scrollView = (com.paperpad.mybox.widgets.ObservableScrollView) view.findViewById(R.id.scrollView1);
		scrollView.setScrollViewListener(this);


		view.findViewById(R.id.createFormRecipient).setBackgroundColor(colors.getColor(colors.getNavigation_color()));
		((TextView)view.findViewById(R.id.beneficiary_title)).setTextColor(colors.getColor(colors.getTitle_color()));
		//		final PopupWindow pw = new PopupWindow(view, (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height), true);
		//		
		//		pw.setOutsideTouchable(true);
		//		pw.setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
		//		pw.setFocusable(true);
		//		
		//		pw.showAtLocation(rootView, Gravity.CENTER, 0, 0);

		final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getAlternate_background_color())));
		dialog.getWindow().setLayout( (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height));
		//dialog.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//dialog.getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		//dialog.getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

		Button confirm = (Button) view.findViewById(R.id.confirm);
		//confirm.setBackground(bgBtn);
		confirm.setTextColor(listColor1);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				Context context = getActivity().getApplicationContext();
				//				Toast.makeText(getActivity() , " Benificiary data", 1000).show();
//				Log.e("  Champs valides ? "," "+validateAllFields(view));
				if(validateAllFields(view)) {

					EditText firstName = (EditText) view.findViewById(R.id.first_name);
					EditText lastName = (EditText) view.findViewById(R.id.last_name);
					EditText email = (EditText) view.findViewById(R.id.email);
					EditText comment = (EditText) view.findViewById(R.id.complement);
					//					Toast.makeText(getActivity(), " Benificiary data : firstName : "+firstName.getText().toString()
					//							+", LastName : "+lastName.getText().toString()+" Email : "+email.getText().toString()
					//							+" Comment : "+comment.getText().toString()+"  quantity : "+((TextView)rootView.findViewById(R.id.quantityNumber)).getText().toString(), 1000).show();

					CartItem cartItem = new CartItem(box, firstName.getText().toString(), lastName.getText().toString(), 
							email.getText().toString(),comment.getText().toString(), ((TextView)rootView.findViewById(R.id.quantityNumber)).getText().toString());

					appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
					appController.addCartItem(cartItem);
					CartFragment cartFragment = (CartFragment) getActivity().getSupportFragmentManager().findFragmentByTag(BoxsMainActivity.CART_FRAGMENT);
					if (cartFragment != null) {
						cartFragment.fillCart();
					}
					//pw.dismiss();
					dialog.dismiss();
					try {
						int i = 0;
						List<CartItem> list = appController.getCartItemDao().queryForAll();
						for(Iterator<CartItem> iter = list.iterator(); iter.hasNext();) {
							Log.e("  cartItem "+ i++ , " : "+iter.next().toString());
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
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
				beneficiary.setFirst_name(first_name.getText().toString());
				beneficiary.setLast_name(last_name.getText().toString());
				beneficiary.setEmail(email.getText().toString());
				beneficiary.setMessage(message.getText().toString());
				
				appController.saveBenificiary(beneficiary);
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
		//		ShapeDrawable bgCom = new ShapeDrawable(rectName);
		//		bgCom.getPaint().setColor(colors.getColor(colors.getBackground_color()));
		//		view.findViewById(R.id.commentaire).setBackground(bgCom);
		view.findViewById(R.id.commentaire).setBackgroundColor(colors.getColor(colors.getBackground_color()));


		((TextView)view.findViewById(R.id.who_offer_msg)).setTextColor(colors.getColor(colors.getText_color()));
		((TextView)view.findViewById(R.id.msg_to_beneficiary)).setTextColor(colors.getColor(colors.getText_color()));

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

	public boolean validateAllFields(View view) {
		boolean error = false;
		String value;

		String ErrorMsg = getString(R.string.fields_msg_error);
		
		EditText email = (EditText) view.findViewById(R.id.email);
		EditText firstName = (EditText) view.findViewById(R.id.first_name);
		EditText lastName = (EditText) view.findViewById(R.id.last_name);
		

		
		
		
		value = email.getText().toString();
		if (value.isEmpty() || !isEmailValid(value)) {
			error = true;
			ErrorMsg = getString(R.string.email_error);


		}
		
		value = lastName.getText().toString();
		if (value.isEmpty()) {
			error = true;

			ErrorMsg = getString(R.string.benificiary_msg_error_begin)+" "+getString(R.string.last_name).toLowerCase()+" "+getString(R.string.benificiary_msg_error_end);


		}
		
		value = firstName.getText().toString();
		if (value.isEmpty()) {
			error = true;

			ErrorMsg = getString(R.string.benificiary_msg_error_begin)+" "+getString(R.string.first_name).toLowerCase()+" "+getString(R.string.benificiary_msg_error_end);
			

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


				alert.setTitle(getString(R.string.form_error));
				alert.setMessage(ErrorMsg);
				alert.show();
						


		}
		
		return !error;
	}

	@Override
	public void onScrollChanged(ObservableScrollView observableScrollView,
			int x, int y, int oldx, int oldy) {
		
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(tmpView.getWindowToken(), 0);
		
	}


	
}

/*
 * 
				CartFragment cartFragment = (CartFragment) getActivity().getSupportFragmentManager().findFragmentByTag(BoxsMainActivity.CART_FRAGMENT);
				if (cartFragment != null ) {
					
					CartItem item = new CartItem("366", "2", "test enjoy", "Driss", "Bounouar", "mail@mail.com", box);
					cartFragment.addOrderToCart(item );
					
				}
			*/
