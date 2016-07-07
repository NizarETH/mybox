/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Console;
import com.paperpad.mybox.models.IdCoffret;
import com.paperpad.mybox.models.MyBox;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author euphordev02
 *
 */
public class FavoriteFragment extends Fragment {

	private DatabaseController appController;
	List<MyBox> boxs;
	private Console console; 
	private Typeface font, font_discript;
	private Colors colors;
	private static String POLICE, POLICE_DISCRIPT;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
        appController = new DatabaseController(activity);
		((BoxsMainActivity)getActivity()).bodyFragment = "FavoriteFragment";
        if(MainContentFragment.minPrice != -1 && MainContentFragment.maxPrice != -1) {
            MainContentFragment.maxPrice = appController.getMax(((BoxsMainActivity) getActivity()).id_selected_cat);
            MainContentFragment.minPrice = appController.getMin(((BoxsMainActivity) getActivity()).id_selected_cat);
        }

		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		getFavoriteBoxs();
		
		POLICE = "fonts/OpenSans-Bold.ttf"; //gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/OpenSans-Regular.ttf"; //"fonts/gill-sans-light.ttf"; //mt-italic.ttf";

		font = Typeface.createFromAsset(getActivity().getAssets(), POLICE); 
		font_discript = Typeface.createFromAsset(getActivity().getAssets(), POLICE_DISCRIPT);
		
		try {
			console = appController.getConsoleDao().queryForId(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_favorite_fragment, container, false);
		
		DatabaseController appController = new DatabaseController(getActivity());

		try {
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
            //colors = appController.getColorsDao().queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

		
		view.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		((TextView)view.findViewById(R.id.favoriteBox)).setTextColor(colors.getColor(colors.getText_color()));
		
		LinearLayout fav1 = (LinearLayout)view.findViewById(R.id.favorite1);
		LinearLayout fav2 = (LinearLayout)view.findViewById(R.id.favorite2);
		//LinearLayout fav3 = (LinearLayout)view.findViewById(R.id.favorite3);
		int index = 0;
		for (int i = 0; i < boxs.size() && index < 2; i++) {
			final MyBox box = boxs.get(i);
			ImageView target = null;
			TextView favorite_txt = null; 
			TextView desc = null;
			TextView moreInfo = null;
			TextView price = null;
			
			if (i==0) {
				target = (ImageView) view.findViewById(R.id.favorite_img1);
				favorite_txt = (TextView)view.findViewById(R.id.favorite_txt1);
				desc = (TextView)view.findViewById(R.id.desc1);
				moreInfo =  (TextView)view.findViewById(R.id.info_txt1);
				price = (TextView)view.findViewById(R.id.price1);
			}else if (i == 1) {
				target = (ImageView) view.findViewById(R.id.favorite_img2);
				favorite_txt = (TextView)view.findViewById(R.id.favorite_txt2);
				desc = (TextView)view.findViewById(R.id.desc2);
				moreInfo =  (TextView)view.findViewById(R.id.info_txt2);
				price = (TextView)view.findViewById(R.id.price2);
			}/*else if (i == 2) {
				target = (ImageView) view.findViewById(R.id.favorite_img3);
				favorite_txt = (TextView)view.findViewById(R.id.favorite_txt3);
				desc = (TextView)view.findViewById(R.id.desc3);
				moreInfo =  (TextView)view.findViewById(R.id.info_txt3);
				price = (TextView)view.findViewById(R.id.price3);
			}*/
			
			

			favorite_txt.setTypeface(font, Typeface.BOLD);
			favorite_txt.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
			favorite_txt.setBackgroundColor(colors.getColor(colors.getBackground_color(), "AA"));
			//favorite_txt.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			//favorite_txt.setTextSize((int)getResources().getDimension(R.dimen.label_window_small));
			favorite_txt.setTextColor(colors.getColor(colors.getTitle_color()));

			desc.setTypeface(font_discript);
			desc.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
			desc.setTextColor(colors.getColor(colors.getText_color()));

			//desc.setTypeface(Typeface.MONOSPACE);
			//desc.setTextSize((int)getResources().getDimension(R.dimen.html_txt));
			
			RoundRectShape rect = new RoundRectShape(new float[] {10, 10, 10, 10, 10 ,10, 10, 10}, null, null);

			final ShapeDrawable bg = new ShapeDrawable(rect);
			bg.getPaint().setColor(colors.getColor(colors.getAlternate_background_color()));

			//moreInfo.setTextSize(12);
			moreInfo.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
			moreInfo.setBackgroundDrawable(bg);

			price.setTypeface(font, Typeface.BOLD);
			price.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
			price.setTextColor(Color.parseColor("#777777")/*colors.getColor(colors.getTitle_color())*/);

			/*if(getActivity().getResources().getBoolean(R.bool.isTablet)) {
				RelativeLayout target_container = (RelativeLayout) target.getParent();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.5f);
				params.gravity = Gravity.CENTER;
				target_container.setLayoutParams(params);
			}*/

			if (box.getIllustrations().size() > 0 && target != null) {
				if (!box.getIllustrations().iterator().next().getPath().isEmpty()) {
					Glide.with(getActivity()).load(new File(box.getIllustrations().iterator().next().getPath())).into(target);
				}else {
					Glide.with(getActivity()).load(box.getIllustrations().iterator().next().getLink()).into(target);
				}
			}
			if (box.getTitre_coffret() != null && favorite_txt != null) {
				favorite_txt.setText(box.getTitre_coffret());
			}
			if (desc != null && box.getIntroduction() != null) {
				
				desc.setText(Utils.removeHtmlTags(box.getIntroduction())/*Html.fromHtml(box.getIntroduction()).toString()*/);
			}
			if (price !=null && box.getPrix() != null) {
//				String currency = "€";
//				if (console != null && console.getCurrency() != null) {
//					if (console.getCurrency().contains("EUR")) {
//						currency = "€";
//					}
//				}
				price.setText(box.getPrix()+" "+BoxsMainActivity.currency);
			}
			if (i == 0) {
				fav1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						((TextView)v.findViewById(R.id.info_txt1)).setTextColor(colors.getColor(colors.getBackground_color()));
						//bg.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						//((TextView)v.findViewById(R.id.info_txt1)).setBackgroundDrawable(bg);

						/*MyBoxPageFragment boxPageFragment = new MyBoxPageFragment();
						Bundle bundle = new Bundle();
						bundle.putInt("box_id", box.getId());
						boxPageFragment.setArguments(bundle);
//						getActivity().findViewById(R.id.box_page_fragment).setVisibility(View.VISIBLE);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxPageFragment).addToBackStack(null).commit();*/
						
						BoxsPagerFragment  boxsPagerFragment = new BoxsPagerFragment();
						Bundle bundle = new Bundle();
						((BoxsMainActivity)getActivity()).id_selected_cat = box.getId();
						bundle.putInt("box_id", box.getId());
						bundle.putInt("category_id", box.getId_categorie());
						boxsPagerFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxsPagerFragment).addToBackStack(null).commit();
						
					}
				});
			}
			if (i == 1) {
				fav2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						((TextView)v.findViewById(R.id.info_txt2)).setTextColor(colors.getColor(colors.getBackground_color()));
						//bg.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						//((TextView)v.findViewById(R.id.info_txt2)).setBackgroundDrawable(bg);

						/*MyBoxPageFragment boxPageFragment = new MyBoxPageFragment();
						Bundle bundle = new Bundle();
						bundle.putInt("box_id", box.getId());
						boxPageFragment.setArguments(bundle);
//						getActivity().findViewById(R.id.box_page_fragment).setVisibility(View.VISIBLE);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxPageFragment).addToBackStack(null).commit();*/
						
						BoxsPagerFragment  boxsPagerFragment = new BoxsPagerFragment();
						Bundle bundle = new Bundle();
						((BoxsMainActivity)getActivity()).id_selected_cat = box.getId();
						bundle.putInt("box_id", box.getId());
						bundle.putInt("category_id", box.getId_categorie());
						boxsPagerFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxsPagerFragment).addToBackStack(null).commit();
						
					}
				});
			}
			/*if (i == 2) {
				fav3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						((TextView)v.findViewById(R.id.info_txt3)).setTextColor(colors.getColor(colors.getBackground_color()));
						bg.getPaint().setColor(colors.getColor(colors.getTitle_color()));
						((TextView)v.findViewById(R.id.info_txt3)).setBackgroundDrawable(bg);

						*//*MyBoxPageFragment boxPageFragment = new MyBoxPageFragment();
						Bundle bundle = new Bundle();
						bundle.putInt("box_id", box.getId());
						boxPageFragment.setArguments(bundle);
//						getActivity().findViewById(R.id.box_page_fragment).setVisibility(View.VISIBLE);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxPageFragment).addToBackStack(null).commit();*//*
						
						
						BoxsPagerFragment  boxsPagerFragment = new BoxsPagerFragment();
						Bundle bundle = new Bundle();
						((BoxsMainActivity)getActivity()).id_selected_cat = box.getId();
						bundle.putInt("box_id", box.getId());
						bundle.putInt("category_id", box.getId_categorie());
						boxsPagerFragment.setArguments(bundle);
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxsPagerFragment).addToBackStack(null).commit();
					
						
					}
				});
			}*/
			index++;
		}
		if (boxs.size()>0) {
			
		}
		
//		MyBoxPageFragment boxPageFragment = new MyBoxPageFragment();
//		Bundle bundle = new Bundle();
//		bundle.putInt("box_id", boxs.get(0).getId());
//		boxPageFragment.setArguments(bundle);
//		getActivity().findViewById(R.id.box_page_fragment).setVisibility(View.VISIBLE);
//		getActivity().getSupportFragmentManager().beginTransaction().add(R.id.box_page_fragment, boxPageFragment).addToBackStack(null).commit();

		return view;
	}
	
	private void getFavoriteBoxs(){
		boxs = new ArrayList<MyBox>();
		
		int index = 0;
		List<IdCoffret> idCoffrets = new ArrayList<IdCoffret>();
		try {
			idCoffrets = appController.getIdCoffretDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < idCoffrets.size() && index < 2; i++) {
			if (appController.getBoxByIdServer(idCoffrets.get(i).getId_coffret()) != null) {
				boxs.add(appController.getBoxByIdServer(idCoffrets.get(i).getId_coffret()));
				index++;
//				if (index == 2) {
//					return;
//				}
			}
		}
		while (index < 2) {
			List<MyBox> tmpBoxs = new ArrayList<MyBox>();
			try {
				tmpBoxs = appController.getMyBoxDao().queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Random  rd = new Random();
			int choix = 0;
			try {
				choix = rd.nextInt(tmpBoxs.size());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				try {
					boxs.add(tmpBoxs.get(choix));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}
