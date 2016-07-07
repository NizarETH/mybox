/**
 * 
 */
package com.paperpad.mybox.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paperpad.mybox.R;
import com.paperpad.mybox.activities.BoxsMainActivity;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.CategoriesGetter;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Illustration;
import com.paperpad.mybox.models.MyBox;
import com.paperpad.mybox.widgets.ArrowImageView;
import com.paperpad.mybox.widgets.AutoResizeTextView;
import com.paperpad.mybox.widgets.VerticalViewPager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author euphordev02
 *
 */
public class BoxsPagerFragment extends Fragment {

	// Data
	private BoxsPagerAdapter mAdapter = null;

	// Views
	private int layoutRes;
	private View mRootView;
	private VerticalViewPager mPager;

	// Handlers
	private final Handler handler = new Handler();
	private Runnable runPager;
	private boolean mCreated = false;

	private DatabaseController appController;

	private List<MyBox> boxs;

	private TextView category;

	private List<CategoriesGetter> cats;

	private boolean isTablet;

	private Colors colors;

	private BoxsMainActivity activity;

	public LinearLayout viewCategory;

	/**
	 * 
	 */
	public BoxsPagerFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.paperpad.mybox.fragments.BasePagerFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(layoutRes, container, false);

		//DatabaseController 
		appController = new DatabaseController(getActivity());

		try {
			//colors = appController.getColorsDao().queryForAll().get(0);
            colors = (appController.getColorsDao().queryForId(1) == null) ? new Colors() : appController.getColorsDao().queryForId(1);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mRootView.setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));

		mRootView.findViewById(R.id.separatorPager).setBackgroundColor(colors.getColor(colors.getText_color()));
		if(isTablet) {

			mRootView.findViewById(R.id.header).setBackgroundDrawable(colors.makeGradientToColor(colors.getNavigation_color()));//.setBackgroundColor(colors.getColor(colors.getNavigation_color()));

			/*TextView appTitle = (TextView) mRootView.findViewById(R.id.appTitle);
			appTitle.setTextColor(colors.getColor(colors.getBackground_color()));
			appTitle.setText(CategoriesFragment.applicationTitle);*/
			AutoResizeTextView appTitle = (AutoResizeTextView) mRootView.findViewById(R.id.appTitle);
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold.ttf");
			appTitle.setTypeface(font, Typeface.BOLD);
			appTitle.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Large);
			appTitle.setTextColor(Color.parseColor("#CC000000")/*colors.getColor(colors.getText_color())*/);
			appTitle.setText(getString(R.string.bandeau));
		}
		else {
			mRootView.findViewById(R.id.header).setVisibility(View.GONE);
		}

		final TextView share = (TextView) mRootView.findViewById(R.id.share_btn);
		share.setTextColor(colors.getColor(colors.getTitle_color()));



		mPager = (VerticalViewPager) mRootView.findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(0);


		category = (TextView)mRootView.findViewById(R.id.category);
		category.setTextColor(colors.getColor(colors.getTitle_color()));


		viewCategory  = (LinearLayout)mRootView.findViewById(R.id.back_to_boxs);
		if (!isTablet) {
			mRootView.findViewById(R.id.back_to_boxs).setVisibility(View.GONE);
		}
		if (boxs.size()>0) {
			CategoriesGetter cat = appController.getCategoryById(boxs.get(0).getId_categorie());
			category.setText(getString(R.string.back_to_category, cat!=null?cat.getName_categorie().toUpperCase():""));
			viewCategory.setTag(cat.getId_categorie());
			viewCategory.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int cat = (Integer) v.getTag();
					activity.id_selected_cat =cat;
					MainContentFragment contentFragment = new MainContentFragment();
					activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, contentFragment, BoxsMainActivity.CONTENT_FRAGMENT).addToBackStack(BoxsMainActivity.CONTENT_FRAGMENT).commit();
					MainContentFragment.displayMode = 1;
				}
			});

			share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent share = new Intent(Intent.ACTION_SEND);
					MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype

					Uri uri = null;
					String path = ".jpg";
					//					    if (path.contains("Paperpad/")) {
					//							uri = Uri.fromFile(new File(path));
					//						}else {
					if(boxs.get(0).getIllustrations() != null && boxs.get(0).getIllustrations().size()>0) {
						ArrayList<Illustration> illusts = new ArrayList<Illustration>(boxs.get(0).getIllustrations());
						Illustration illust = illusts.get(0);
						//    							uri =  Uri.parse(illust.getLink());

						path = illust.getPath();
						uri = Uri.fromFile(new File(path));
					}


					String ext = path.substring(path.lastIndexOf('.') + 1);
					String mime = map.getMimeTypeFromExtension(ext);
					share.setType(mime); // might be text, sound, whatever
					//    						MyProvider f = new MyProvider(path);

					share.putExtra(Intent.EXTRA_SUBJECT,boxs.get(0).getTitre_coffret());
					share.putExtra(Intent.EXTRA_TEXT,Utils.removeHtmlTags(boxs.get(0).getIntroduction()));
					share.putExtra(Intent.EXTRA_STREAM,uri);//using a string here didnt work for me

					//    						File root=android.os.Environment.getDownloadCacheDirectory();//.getExternalStorageDirectory();
					// 
					//    						share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+root.getAbsolutePath()+"/"+path));
					Log.d("", "share " + uri + "\n ext:" + ext + "\n mime:" + mime);//+"\n path : "+"file:///"+root.getAbsolutePath()+"/"+path);
					startActivity(Intent.createChooser(share, "share"));


					//				String path = "/mnt/sdcard/dir1/sample_1.jpg";


				}
			});



		}
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getNavigation_color()));


		ArrowImageView up_box_page = (ArrowImageView)mRootView.findViewById(R.id.up_box_page);
		up_box_page.setPaint(paint);
		up_box_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPager.getCurrentItem() >= boxs.size() - 1) {
					mPager.setCurrentItem(0);
				}else {
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}

			}
		});

		ArrowImageView down_box_page = (ArrowImageView)mRootView.findViewById(R.id.down_box_page);

		down_box_page.setPaint(paint);
		down_box_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPager.getCurrentItem() == 0) {
					mPager.setCurrentItem(boxs.size() - 1);
				}else {
					mPager.setCurrentItem(mPager.getCurrentItem() - 1);
				}

			}
		});


		ArrowImageView back_to_bow_arrow = (ArrowImageView)mRootView.findViewById(R.id.back_to_bow_arrow);       
		back_to_bow_arrow.setPaint(paint);


		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				try {
					DatabaseController controller = new DatabaseController(getActivity());
					final CategoriesGetter cat = controller.getCategoryById(boxs.get(position).getId_categorie());
					category.setText(getString(R.string.back_to_category, cat!=null?cat.getName_categorie().toUpperCase():""));
					viewCategory.setTag(cat.getId_categorie());
					viewCategory.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int cat = (Integer) v.getTag();
							activity.id_selected_cat =cat;
							MainContentFragment contentFragment = new MainContentFragment();
							activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, contentFragment, BoxsMainActivity.CONTENT_FRAGMENT).addToBackStack(BoxsMainActivity.CONTENT_FRAGMENT).commit();
							MainContentFragment.displayMode = 1;
						}
					});
					final int pos = position;
					share.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent share = new Intent(Intent.ACTION_SEND);
							MimeTypeMap map = MimeTypeMap.getSingleton(); //mapping from extension to mimetype
							//		    						String ext = path.substring(path.lastIndexOf('.') + 1);
							//		    						String mime = map.getMimeTypeFromExtension(ext);
							//		    						share.setType(mime); // might be text, sound, whatever
							Uri uri = null;
							String path = ".jpg";

							if(boxs.get(0).getIllustrations() != null && boxs.get(0).getIllustrations().size()>0) {
								ArrayList<Illustration> illusts = new ArrayList<Illustration>(boxs.get(0).getIllustrations());
								Illustration illust = illusts.get(0);
								//		    							uri =  Uri.parse(illust.getLink());
								path = illust.getPath();
								uri = Uri.fromFile(new File(path));
							}

							String ext = path.substring(path.lastIndexOf('.') + 1);
							String mime = map.getMimeTypeFromExtension(ext);
							share.setType(mime);
							//						}
							share.putExtra(Intent.EXTRA_SUBJECT,boxs.get(pos).getTitre_coffret());
							share.putExtra(Intent.EXTRA_TEXT,boxs.get(pos).getIntroduction());
							share.putExtra(Intent.EXTRA_STREAM,uri);//using a string here didnt work for me


							//Log.d("", "share " + uri + " ext:" + ext + " mime:" + mime);
							startActivity(Intent.createChooser(share, "share"));


							//				String path = "/mnt/sdcard/dir1/sample_1.jpg";


						}
					});


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});



		return mRootView;
	}

	/* (non-Javadoc)
	 * @see com.paperpad.mybox.fragments.BasePagerFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (runPager != null) handler.post(runPager);
		mCreated = true;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		setLayoutRes(R.layout.boxs_pager_fragment);
		appController = new DatabaseController(getActivity());
		if (getArguments() != null) {
			int box_id = getArguments().getInt("box_id");
			int category_id = getArguments().getInt("category_id");
			try {
				boxs = appController.getMyBoxDao().queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			makeFirst(box_id);
			setAdapter(new BoxsPagerAdapter(getChildFragmentManager()));
			//			if (box_id != 0) {
			//				try {
			//					box = appController.getMyBoxDao().queryForId(box_id);
			//				} catch (SQLException e) {
			//					e.printStackTrace();
			//				}
			//			}
		}
		cats = appController.getAllCategoriesOrderById();
		isTablet = getResources().getBoolean(R.bool.isTablet);
		this.activity = (BoxsMainActivity)activity;
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
	 * @see com.paperpad.mybox.fragments.BasePagerFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(runPager);
	}


	/**
	 * Set the ViewPager adapter from this or from a subclass.
	 * 
	 * @author chris.jenkins
	 * @param mAdapter2 This is a FragmentStatePagerAdapter due to the way it creates the TAG for the 
	 * Fragment.
	 */
	protected void setAdapter(BoxsPagerAdapter mAdapter2)
	{
		mAdapter = mAdapter2;
		runPager = new Runnable() {

			@Override
			public void run()
			{
				mPager.setAdapter(mAdapter);
			}
		};
		if (mCreated)
		{
			handler.post(runPager);
		}
	}


	/**
	 * Has to be set before onCreateView
	 * 
	 * @author chris.jenkins
	 * @param layoutRes
	 */
	protected void setLayoutRes(int layoutRes)
	{
		this.layoutRes = layoutRes;
	}


	public class BoxsPagerAdapter extends FragmentStatePagerAdapter {

		public BoxsPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {


			return MyBoxPageFragment.create(boxs.get(position));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return boxs.size();
		}



	}

	private void makeFirst(int box_id) {
		if (boxs.size()>0) {
			for (int i = 0; i < boxs.size(); i++) {
				if (boxs.get(i).getId() == box_id) {
					MyBox box = boxs.get(i);
					boxs.remove(i);
					boxs.add(0, box);
					break;
				}
			}
		}

	}

}
