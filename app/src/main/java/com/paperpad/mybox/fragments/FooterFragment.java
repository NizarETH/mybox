/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.widgets.RangeSeekBar;
import com.paperpad.mybox.widgets.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.paperpad.mybox.widgets.WindowedSeekBar.SeekBarChangeListener;

import java.sql.SQLException;

/**
 * @author euphordev02
 *
 */
public class FooterFragment extends Fragment implements SeekBarChangeListener{

	private static final String TAG = "FooterFragment";
    private Colors colors;
    RangeSeekBar<Integer> seekBar;
    private LinearLayout starLayout;
    private LinearLayout gridLayout;
    private LinearLayout list;
	//private WindowedSeekBar wsb;
	private CallbackFooter  callbackFooter = new CallbackFooter() {



		@Override
		public void setDisplayMode(int mode) {
			Log.d(getTag(), "Dummy setDisplayMode");

		}

		@Override
		public void setOrder(int order, int mode) {
			Log.d(getTag(), "Dummy setOrder");

		}

		@Override
		public void setPriceRange(int min, int max, int mode) {
            Log.d(getTag(), "Dummy setPriceRange");
			
		}
	};



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
		final View view = inflater.inflate(R.layout.footer_fragment, container, false);


		DatabaseController appController = new DatabaseController(getActivity());
		colors = null;
		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.findViewById(R.id.footerBackground).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));


		
		seekBar = new RangeSeekBar<Integer>(MainContentFragment.minPrice, MainContentFragment.maxPrice, getActivity());
		
		seekBar.setSeekBarCololors(colors.getBackground_color(), "55"+colors.getText_color());

		seekBar.attachToRelativeLayout((RelativeLayout) view.findViewById(R.id.RelativeLayout1), colors);
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		        @Override
		        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
		                //handle changed range values
		                //Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                        MainContentFragment.minPrice = minValue;
                        MainContentFragment.maxPrice = maxValue;

		                callbackFooter.setPriceRange(minValue, maxValue, MainContentFragment.displayMode);
		        }
		});


		LinearLayout layout = (LinearLayout) view.findViewById(R.id.rangeBarContainer);
		layout.addView(seekBar);

		

		if(getActivity().getResources().getBoolean(R.bool.isTablet)) {
			//display mode
			starLayout = (LinearLayout)view.findViewById(R.id.star);
			gridLayout = (LinearLayout)view.findViewById(R.id.favoris);
			list = (LinearLayout)view.findViewById(R.id.list);

			RoundRectShape rectL = new RoundRectShape(new float[] {5, 5, 0, 0, 0 ,0, 5, 5}, null, null);
			ShapeDrawable bgL = new ShapeDrawable(rectL);
			bgL.getPaint().setColor(colors.getColor(colors.getNavigation_color()));

			RoundRectShape rectR = new RoundRectShape(new float[] {0, 0, 5, 5, 5, 5, 0, 0}, null, null);
			ShapeDrawable bgR = new ShapeDrawable(rectR);
			bgR.getPaint().setColor(colors.getColor(colors.getNavigation_color()));

			starLayout.setBackgroundDrawable(bgL);
			gridLayout.setBackgroundColor(colors.getColor(colors.getNavigation_color()));
			list.setBackgroundDrawable(bgR);
			
			if (MainContentFragment.displayMode == 0) {
				starLayout.setAlpha(1);
				list.setAlpha((float) 0.7);
				gridLayout.setAlpha((float) 0.7);
			}else if (MainContentFragment.displayMode == 1) {
				starLayout.setAlpha((float)0.7);
				list.setAlpha((float) 0.7);
				gridLayout.setAlpha(1);
			}else if (MainContentFragment.displayMode == 2) {
				starLayout.setAlpha((float)0.7);
				list.setAlpha( 1);
				gridLayout.setAlpha((float) 0.7);
			}
			
			starLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					v.setAlpha(1);
					list.setAlpha((float) 0.7);
					gridLayout.setAlpha((float) 0.7);
					MainContentFragment.displayMode = 0;
					callbackFooter.setDisplayMode(MainContentFragment.displayMode);
				}
			});


			gridLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setAlpha(1);
					list.setAlpha((float) 0.7);
					starLayout.setAlpha((float) 0.7);
					MainContentFragment.displayMode = 1;
					callbackFooter.setDisplayMode(MainContentFragment.displayMode);
				}
			});



			list.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setAlpha(1);
					gridLayout.setAlpha((float) 0.7);
					starLayout.setAlpha((float) 0.7);
					MainContentFragment.displayMode = 2;
					callbackFooter.setDisplayMode(MainContentFragment.displayMode);
					//				BoxsListFragment BoxsFragment = new BoxsListFragment();
					//				Bundle bundle = new Bundle();
					//				bundle.putInt("orderMode", orderMode);
					//				BoxsFragment.setArguments(bundle);
					//				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.boxs_fragment, BoxsFragment, BoxsMainActivity.BOXS_LIST_FRAGMENT).addToBackStack(BoxsMainActivity.BOXS_LIST_FRAGMENT).commit();
				}
			});

//			gridLayout.setAlpha((float) 0.7);
//			list.setAlpha((float) 0.7);

			//order by title date or price
			final LinearLayout date = (LinearLayout)view.findViewById(R.id.date);
			final LinearLayout title = (LinearLayout)view.findViewById(R.id.title);
			final LinearLayout price = (LinearLayout)view.findViewById(R.id.price);

			date.setBackgroundDrawable(bgL);
			title.setBackgroundColor(colors.getColor(colors.getNavigation_color()));
			price.setBackgroundDrawable(bgR);



			date.setAlpha((float) 0.7);
			title.setAlpha((float) 0.7);
			price.setAlpha((float) 0.7);

            switch (MainContentFragment.orderMode){
                case 0 :
                    date.setAlpha(1.0f);
                    break;

                case 1 :
                    title.setAlpha(1.0f);
                    break;

                case 2 :
                    price.setAlpha(1.0f);
                    break;
            }

			date.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setAlpha(1);
					title.setAlpha((float) 0.7);
					price.setAlpha((float) 0.7);
					MainContentFragment.orderMode = 0;

					callbackFooter.setOrder(MainContentFragment.orderMode, MainContentFragment.displayMode);


				}
			});
			title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					v.setAlpha(1);
					date.setAlpha((float) 0.7);
					price.setAlpha((float) 0.7);
					MainContentFragment.orderMode = 1;
					callbackFooter.setOrder(MainContentFragment.orderMode, MainContentFragment.displayMode);

				}
			});
			price.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					v.setAlpha(1);
					title.setAlpha((float) 0.7);
					date.setAlpha((float) 0.7);
					MainContentFragment.orderMode = 2;
					callbackFooter.setOrder(MainContentFragment.orderMode, MainContentFragment.displayMode);

				}
			});
		}else {
			view.findViewById(R.id.display).setVisibility(View.GONE);
			view.findViewById(R.id.ordering).setVisibility(View.GONE);

		}

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void SeekBarValueChanged(int ThumbMinValue, int thumblX,
			int ThumbMaxValue, int thumbrX, int width, int thumbY) {
		//Log.i(TAG, "Thumb1Value :"+Thumb1Value+" Thumb2Value :"+Thumb2Value+"");
        //callbackFooter.setPriceRange(ThumbMinValue, ThumbMaxValue, MainContentFragment.displayMode);
	}

	public interface CallbackFooter{
		public void setOrder(int order, int mode); 
		public void setDisplayMode(int mode); 
		public void setPriceRange(int min, int max, int mode);
	}

	/**
	 * @return the callbackFooter
	 */
	public CallbackFooter getCallbackFooter() {
		return callbackFooter;
	}

	/**
	 * @param callbackFooter the callbackFooter to set
	 */
	public void setCallbackFooter(CallbackFooter callbackFooter) {
		this.callbackFooter = callbackFooter;
	}

	public int randomFromInterval(int from,int to)
	{
		return (int) Math.floor(Math.random()*(to-from+1)+from);
	}


    public void setRangeSeekerValues(int min, int max) {
		seekBar.setAbsoluteMaxValuePrim(max);
		seekBar.setAbsoluteMinValuePrim(min);
		seekBar.invalidate();
	}
	
	public void setDisplayMode(int dMode) {
		MainContentFragment.displayMode = dMode;
		if (starLayout != null && list != null && gridLayout != null) {
			if (dMode == 0) {
				starLayout.setAlpha(1);
				list.setAlpha((float) 0.7);
				gridLayout.setAlpha((float) 0.7);
			}else if (dMode == 1) {
				starLayout.setAlpha((float)0.7);
				list.setAlpha((float) 0.7);
				gridLayout.setAlpha(1);
			}else if (dMode == 2) {
				starLayout.setAlpha((float)0.7);
				list.setAlpha(1);
				gridLayout.setAlpha((float) 0.7);
			}
			
			
		}
		
	}

}
