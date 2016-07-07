/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.fragments.CategoriesFragment.CallbackCategory;
import com.paperpad.mybox.fragments.FooterFragment.CallbackFooter;
import com.paperpad.mybox.models.Colors;

import java.sql.SQLException;

/**
 * @author euphordev02
 *
 */
public class MainContentFragment extends Fragment implements CallbackCategory, CallbackFooter {

	private boolean isTablet;
	private BoxsMainActivity activity;
	private DatabaseController appController;
	private int id_cat;
	private Colors colors;
	public static int maxPrice = -1;
	public static int minPrice = -1;


	/**
	 * 0 pour favoris (initial) ; 1 pour Grid ; 2 pour liste
	 */
	public static int displayMode = 0; 

	/**
	 * 0 pour date (initial) ; 1 pour titre ; 2 pour prix
	 */
	public static int orderMode = 0;

	/**
	 * 
	 */
	public MainContentFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		this.activity = (BoxsMainActivity)activity;
		isTablet = getResources().getBoolean(R.bool.isTablet);
		appController = new DatabaseController(activity);
		try {
			//colors = appController.getColorsDao().queryForId(1);//.queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxPrice = appController.getMax(0);
		minPrice = appController.getMin(0);
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
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
		View view = inflater.inflate(R.layout.content_fragment, container, false);
		if (activity.id_selected_cat==0) {
			if (isTablet) {
				view = inflater.inflate(R.layout.content_fragment, container, false);
				String body = "";
				Bundle extras = null;
				int id_selected_cat = 0;

				if(BoxsMainActivity.map != null) {
					body = (String) BoxsMainActivity.map.get("bodyFragment");
					extras =  (Bundle) BoxsMainActivity.map.get("EXTRAS");
					id_selected_cat = (Integer)extras.get("id_selected_cat");
				}

				if(body.compareToIgnoreCase("MyBoxPageFragment") != 0) {


					if(body.compareToIgnoreCase("BoxsGridFragment") == 0){


						BoxsGridFragment boxsListFragment = new BoxsGridFragment();
						//boxsListFragment.selectCategory(id_selected_cat);
						getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsListFragment , BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
						MainContentFragment.displayMode = 1;

					}
					else if(body.compareToIgnoreCase("BoxsListFragment") == 0) {

						BoxsListFragment boxsListFragment = new BoxsListFragment();
						//boxsListFragment.selectCategory(id_selected_cat);
						getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsListFragment , BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
						//boxsListFragment.selectCategory(id_cat);
						MainContentFragment.displayMode = 2;

					}
					else {
                        ((BoxsMainActivity)getActivity()).id_selected_cat = id_selected_cat;
						FavoriteFragment favoriteFragment = new FavoriteFragment();
						getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, favoriteFragment , BoxsMainActivity.FAVORITE_FRAGMENT).addToBackStack(BoxsMainActivity.FAVORITE_FRAGMENT).commit();

					}

					FooterFragment footerFragment = new FooterFragment();
					getChildFragmentManager().beginTransaction().replace(R.id.footer_fragment, footerFragment , BoxsMainActivity.FOOTER_FRAGMENT).addToBackStack(BoxsMainActivity.FOOTER_FRAGMENT).commit();
					footerFragment.setCallbackFooter(MainContentFragment.this);

					CategoriesFragment categoriesFragment = new CategoriesFragment();
					getChildFragmentManager().beginTransaction().replace(R.id.header_fragment, categoriesFragment, BoxsMainActivity.CATEGORIES_FRAGMENT).addToBackStack(BoxsMainActivity.CATEGORIES_FRAGMENT).commit();
					categoriesFragment.setCallbackCategory(MainContentFragment.this);

				} else {

					BoxsPagerFragment  boxsPagerFragment = new BoxsPagerFragment();
					Bundle bundle = new Bundle();
					//((BoxsMainActivity)getActivity()).id_selected_cat = box.getId();
					bundle.putInt("box_id", id_selected_cat);
					bundle.putInt("category_id", id_selected_cat);
					boxsPagerFragment.setArguments(bundle);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, boxsPagerFragment).addToBackStack(null).commit();


				}
			}else {

				view = inflater.inflate(R.layout.smart_content_fragment, container, false);
				BoxsListFragment boxsListFragment = new BoxsListFragment();
				getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsListFragment , BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
				MainContentFragment.displayMode = 2;
				
				FooterFragment footerFragment = new FooterFragment();
				getChildFragmentManager().beginTransaction().replace(R.id.footer_fragment, footerFragment , BoxsMainActivity.FOOTER_FRAGMENT).addToBackStack(BoxsMainActivity.FOOTER_FRAGMENT).commit();
				footerFragment.setCallbackFooter(MainContentFragment.this);

				CategoriesFragment categoriesFragment = new CategoriesFragment();
				getChildFragmentManager().beginTransaction().replace(R.id.header_fragment, categoriesFragment, BoxsMainActivity.CATEGORIES_FRAGMENT).addToBackStack(BoxsMainActivity.CATEGORIES_FRAGMENT).commit();
				categoriesFragment.setCallbackCategory(MainContentFragment.this);

				LinearLayout footerGetter = (LinearLayout)view.findViewById(R.id.footerGetter);
				footerGetter.setBackgroundColor(colors.getColor(colors.getNavigation_color(),"CC"));
				final FrameLayout fl = (FrameLayout)view.findViewById(R.id.footer_fragment);
				footerGetter.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(fl.getVisibility() == View.GONE)
							fl.setVisibility(View.VISIBLE);
						else
							fl.setVisibility(View.GONE);

					}
				});
			}
		}else {
			if (isTablet) {
				view = inflater.inflate(R.layout.content_fragment, container, false);

				BoxsGridFragment boxsGridFragment = new BoxsGridFragment();
				getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsGridFragment , BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
				MainContentFragment.displayMode = 1;
			}else {
				view = inflater.inflate(R.layout.smart_content_fragment, container, false);
				LinearLayout footerGetter = (LinearLayout)view.findViewById(R.id.footerGetter);
				footerGetter.setBackgroundColor(colors.getColor(colors.getNavigation_color(),"CC"));

				BoxsListFragment boxsListFragment = new BoxsListFragment();
				getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsListFragment , BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
				MainContentFragment.displayMode = 2;


				final FrameLayout fl = (FrameLayout)view.findViewById(R.id.footer_fragment);
                //fl.setLayoutParams(new FrameLayout.LayoutParams((int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() - 50), 70));
				footerGetter.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(fl.getVisibility() == View.GONE)
							fl.setVisibility(View.VISIBLE);
						else
							fl.setVisibility(View.GONE);

					}
				});

			}
			//selectCategory(activity.id_selected_cat);
			FooterFragment footerFragment = new FooterFragment();
			getChildFragmentManager().beginTransaction().replace(R.id.footer_fragment, footerFragment , BoxsMainActivity.FOOTER_FRAGMENT).addToBackStack(BoxsMainActivity.FOOTER_FRAGMENT).commit();
			footerFragment.setCallbackFooter(MainContentFragment.this);



			CategoriesFragment categoriesFragment = new CategoriesFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("id_category", activity.id_selected_cat);
			categoriesFragment.setArguments(bundle);
			getChildFragmentManager().beginTransaction().replace(R.id.header_fragment, categoriesFragment, BoxsMainActivity.CATEGORIES_FRAGMENT).addToBackStack(BoxsMainActivity.CATEGORIES_FRAGMENT).commit();
			categoriesFragment.setCallbackCategory(MainContentFragment.this);
		}

		return view;
	}



	@Override
	public void selectCategory(int id_cat) {
		this.id_cat = id_cat;
		BoxsListFragment boxsListFragment = (BoxsListFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_LIST_FRAGMENT);
		BoxsGridFragment boxsGridFragment = (BoxsGridFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_GRID_FRAGMENT);
		//FavoriteFragment favoriteFragment = (FavoriteFragment)getChildFragmentManager().findFragmentByTag(BoxsMainActivity.FAVORITE_FRAGMENT);
		FooterFragment footerFragment = (FooterFragment)getChildFragmentManager().findFragmentByTag(BoxsMainActivity.FOOTER_FRAGMENT);
		//		if (favoriteFragment != null) {
		//			getSupportFragmentManager().beginTransaction().remove(favoriteFragment);
		//		}else{	
		//
		//			favoriteFragment = new FavoriteFragment();
		//
		//			getSupportFragmentManager().beginTransaction().replace(R.id.boxs_fragment, favoriteFragment, FAVORITE_FRAGMENT).addToBackStack(null).commit();
		//			
		//		}

		if (boxsListFragment != null) {
			boxsListFragment.selectCategory(id_cat);
			getChildFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
		}
		//		else if(favoriteFragment != null)
		//		else {
		//			boxsListFragment = new BoxsListFragment();
		//			Bundle args = new Bundle();
		//			args.putInt("id_categorie", id_cat);
		//			boxsListFragment.setArguments(args );
		//			getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BOXS_FRAGMENT).addToBackStack(BOXS_FRAGMENT).commit();
		//		}

		else if (boxsGridFragment != null) {
			boxsGridFragment.selectCategory(id_cat);
			getChildFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();

		}
		//		else{
		//			boxsGridFragment = new BoxsGridFragment();
		//			Bundle args = new Bundle();
		//			args.putInt("id_categorie", id_cat);
		//			boxsGridFragment.setArguments(args );
		//			getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
		//
		//			
		//		}

		maxPrice = appController.getMax(id_cat);
		minPrice = appController.getMin(id_cat);
		if (footerFragment != null) {
            getChildFragmentManager().beginTransaction().remove(footerFragment).add(R.id.footer_fragment, footerFragment, BoxsMainActivity.FOOTER_FRAGMENT).addToBackStack(BoxsMainActivity.FOOTER_FRAGMENT).commit();
            footerFragment.setCallbackFooter(MainContentFragment.this);

			//footerFragment.setRangeSeekerValues(minPrice, maxPrice);
		}else{
            footerFragment = new FooterFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.footer_fragment, footerFragment , BoxsMainActivity.FOOTER_FRAGMENT).addToBackStack(BoxsMainActivity.FOOTER_FRAGMENT).commit();
            footerFragment.setCallbackFooter(MainContentFragment.this);
        }

		if (isTablet) {
			if (footerFragment != null) {
				if (MainContentFragment.displayMode != 2) {
					if (boxsGridFragment != null) {
						boxsGridFragment.selectCategory(id_cat);
						getChildFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
					}else {
						boxsGridFragment = new BoxsGridFragment();
						Bundle args = new Bundle();
						args.putInt("id_categorie", id_cat);
						boxsGridFragment.setArguments(args );
						getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
					}

				}else {

					if (boxsListFragment != null) {
						boxsListFragment.selectCategory(id_cat);
						getChildFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();

					}else {
						boxsListFragment = new BoxsListFragment();
						Bundle args = new Bundle();
						args.putInt("id_categorie", id_cat);
						boxsListFragment.setArguments(args );
						getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
					}
				}
				footerFragment.setDisplayMode(MainContentFragment.displayMode);
			}else {
				if (boxsGridFragment != null) {
					boxsGridFragment.selectCategory(id_cat);
					getChildFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();

				}else {
					boxsGridFragment = new BoxsGridFragment();
					Bundle args = new Bundle();
					args.putInt("id_categorie", id_cat);
					boxsGridFragment.setArguments(args );
					getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
				}
				MainContentFragment.displayMode = 1;
			}
		}else {
			if (boxsListFragment != null) {
				boxsListFragment.selectCategory(id_cat);
				getChildFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
			}else {
				boxsListFragment = new BoxsListFragment();
				Bundle args = new Bundle();
				args.putInt("id_categorie", id_cat);
				boxsListFragment.setArguments(args );
				getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
			}
			MainContentFragment.displayMode = 2;
		}




	}

	@Override
	public void setOrder(int order, int mode) {
		BoxsListFragment boxsListFragment = (BoxsListFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_LIST_FRAGMENT);

		BoxsGridFragment boxsGridFragment = (BoxsGridFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_GRID_FRAGMENT);
		if (mode == 1) {

            if(boxsGridFragment == null){

                boxsGridFragment = new BoxsGridFragment();
                Bundle args = new Bundle();
                args.putInt("id_categorie", id_cat);
                boxsGridFragment.setArguments(args );
                getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();

            }

			if (order == 0) {
				//if (boxsGridFragment != null) {
					boxsGridFragment.filterByDate();
				//}
			}else if (order == 1) {
				//if (boxsGridFragment != null) {
					boxsGridFragment.filterByTitle();
				//}
			}else if (order == 2) {
				//if (boxsGridFragment != null) {
					boxsGridFragment.filterByPrice();
				//}
			}
		}else if (mode == 2) {

            if(boxsListFragment == null){

                boxsListFragment = new BoxsListFragment();
                Bundle args = new Bundle();
                args.putInt("id_categorie", id_cat);
                boxsListFragment.setArguments(args );
                getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();

            }

			if (order == 0) {
				//if (boxsListFragment != null) {
					boxsListFragment.filterByDate();
				//}
			}else if (order == 1) {
				//if (boxsListFragment != null) {
					boxsListFragment.filterByTitle();;
				//}
			}else if (order == 2) {
				//if (boxsListFragment != null) {
					boxsListFragment.filterByPrice();
				//}
			}
		}



	}

	@Override
	public void setDisplayMode(int mode) {
		if (mode == 0) {
			FavoriteFragment favoriteFragment = new FavoriteFragment();
			getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, favoriteFragment, BoxsMainActivity.FAVORITE_FRAGMENT).addToBackStack(BoxsMainActivity.FAVORITE_FRAGMENT).commit();
		}else if (mode == 1) {
			BoxsGridFragment boxsGridFragment = new BoxsGridFragment();
			//			Bundle bundle = new Bundle();
			//			bundle.putInt("orderMode", orderMode);
			//			boxsGridFragment.setArguments(bundle);
			getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
		}else if (mode == 2) {
			BoxsListFragment BoxsFragment = new BoxsListFragment();
			//			Bundle bundle = new Bundle();
			//			bundle.putInt("orderMode", orderMode);
			//			BoxsFragment.setArguments(bundle);
			getChildFragmentManager().beginTransaction().replace(R.id.boxs_fragment, BoxsFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
		}

	}

	@Override
	public void setPriceRange(int min, int max, int mode) {
		BoxsListFragment boxsFragment = (BoxsListFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_LIST_FRAGMENT);

		BoxsGridFragment boxsGridFragment = (BoxsGridFragment) getChildFragmentManager().findFragmentByTag(BoxsMainActivity.BOXS_GRID_FRAGMENT);

		if (isTablet) {
			if (mode == 1 || mode == 0) {
				if (boxsGridFragment != null) {
					boxsGridFragment.priceRange(min, max);
				}else {
					boxsGridFragment = new BoxsGridFragment();
					Bundle args = new Bundle();
					args.putInt("id_categorie", id_cat);
					boxsGridFragment.setArguments(args );
					getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BoxsMainActivity.BOXS_GRID_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_GRID_FRAGMENT).commit();
				}

			}else if (mode == 2) {
				if (boxsFragment != null) {
					boxsFragment.priceRange(min, max);
				}else {
					boxsFragment = new BoxsListFragment();
					Bundle args = new Bundle();
					args.putInt("id_categorie", id_cat);
					boxsFragment.setArguments(args );
					getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
				}
			}
			MainContentFragment.displayMode = mode;


		}else {
			if (boxsFragment != null) {
				boxsFragment.priceRange(min, max);
			}else {
				boxsFragment = new BoxsListFragment();
				Bundle args = new Bundle();
				args.putInt("id_categorie", id_cat);
				boxsFragment.setArguments(args );
				getChildFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
			}

			MainContentFragment.displayMode = 2;
		}

/*        FooterFragment footerFragment = (FooterFragment)getChildFragmentManager().findFragmentByTag(BoxsMainActivity.FOOTER_FRAGMENT);
        if(footerFragment != null){
            footerFragment.setRangeSeekerValues(minPrice, maxPrice);
        }*/

	}

}
