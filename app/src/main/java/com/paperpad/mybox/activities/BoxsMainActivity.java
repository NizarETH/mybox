package com.paperpad.mybox.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.fragments.CartFragment;
import com.paperpad.mybox.fragments.CategoriesFragment;
import com.paperpad.mybox.fragments.MainContentFragment;
import com.paperpad.mybox.helpers.UrlHelpers;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.CategoriesGetter;
import com.paperpad.mybox.models.Colors;
import com.paperpad.mybox.models.Console;
import com.paperpad.mybox.util.Installation;
import com.paperpad.mybox.util.SystemUiHider;
import com.paperpad.mybox.util.jsonUtils.AppJsonWriter;
import com.paperpad.mybox.util.jsonUtils.AppSession;
import com.paperpad.mybox.util.slidingMenu.SlidingMenu;
import com.paperpad.mybox.widgets.AViewFlipper;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.paperpad.mybox.helpers.UrlHelpers.SERVER_URL;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class BoxsMainActivity extends FragmentActivity{//                               implements CallbackCategory{

    public static final String BOXS_LIST_FRAGMENT = "BoxsListFragment";
    public static final String BOXS_GRID_FRAGMENT = "BoxsGridFragment";
    public static final String CATEGORIES_FRAGMENT = "categoriesFragment";
    public static final String CART_FRAGMENT = "cartFragment";
    public static final String FOOTER_FRAGMENT = "footerFragment";
    public static final String FAVORITE_FRAGMENT = "favoriteFragment";
    public static final String CONTENT_FRAGMENT = "contentFragment";
    public static final String BOXS_PAGER_FRAGMENT = "BoxsPagerFragment";
    private static PostCommentResponseListener mPostCommentResponse;

    public static SlidingMenu menu;

    public int id_selected_cat;
    public boolean isTablet;
    private Colors colors;
    public String bodyFragment;
    private DatabaseController appController;
    public static String currency = "€";

    public static Map<String, Object> map;

    private final String LOG_TAG = getClass().getSimpleName();
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "expiration_time";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    public String regid;
    private long tsLong;
    private String session_id;
    int index = 1;


    @Override
    public Map<String, Object> onRetainCustomNonConfigurationInstance() {
        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("id_selected_cat", id_selected_cat);
        map.put("bodyFragment", bodyFragment);
        Bundle extras = new Bundle();
        extras.putInt("id_selected_cat", id_selected_cat);
        extras.putInt("minPrice", MainContentFragment.minPrice);
        extras.putInt("maxPrice", MainContentFragment.maxPrice);
        map.put("EXTRAS", extras);
        return map;
    }

    //	@Override
//		public Map<String, Object> getLastCustomNonConfigurationInstance() {
//
//			return this.getLastCustomNonConfigurationInstance();
//		}
//
    @Override
    protected void onResume() {
        //mainActivityWeakRef = new WeakReference<BoxsMainActivity>(this);
        super.onResume();

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * Checks if the registration has expired.
     *
     * <p>To avoid the scenario where the device sends the registration to the
     * server but the server loses it, the app developer may choose to re-register
     * after REGISTRATION_EXPIRY_TIME_MS.
     *
     * @return true if the registration has expired.
     */
    private boolean isRegistrationExpired() {
        final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
        // checks if the information is not stale
        long expirationTime =
                prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
        return System.currentTimeMillis() > expirationTime+ (604800000);// 7*24*3600*1000 = 604800000
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(LOG_TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(LOG_TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(getString(R.string.sender_id));
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    Looper.prepare();
                    Handler handler = new Handler();
                    if (index < 70) {
                        index = index*2;
                        int delayMillis = (index*1000);
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                registerInBackground();

                            }
                        }, delayMillis);
                    }else {
                        registerReceiver(receiver, filter);
                    }
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //                mDisplay.append(msg + "\n");
                Log.i(LOG_TAG, msg);
            }
        }.execute(null, null, null);
    }

    IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.REGISTRATION");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG + " GCM", "Got into onReceive for manual GCM retrieval. "
                    + intent.getExtras());
            regid = intent.getStringExtra("registration_id");

            if (regid != null && !regid.isEmpty()) {
                sendRegistrationIdToBackend();

            } else {
                //                theResult.add("");
            }
        }
    };

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG_TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app.
     */
    private void sendRegistrationIdToBackend() {
        int id_menu = Integer.parseInt(getString(R.string.app_id));

        String application_unique_identifier = Installation.id(getApplicationContext());
        String application_version = "0.2.8";
        try {
            application_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        };

        String device_type = "";
        if (isTablet) {
            device_type = "tablet";
        }else {
            device_type = "smartphone";
        }
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            display.getSize(size);
        }else {
            size.x = display.getWidth();  // deprecated
            size.y = display.getHeight();
        }


        String device_screen_resolution = size.x+"x"+size.y;


        AppSession appSession = new AppSession(id_menu,
                "production" ,  //production
                "custom",
                application_unique_identifier,
                application_version ,
                regid,
                android.os.Build.MANUFACTURER,
                android.os.Build.MODEL,
                "android",
                device_screen_resolution,
                5,
                android.os.Build.VERSION.SDK_INT+"",
                device_type,
                session_id,
                tsLong,
                System.currentTimeMillis()/1000 ,
                null );
        ArrayList<AppSession> appSessions = new ArrayList<AppSession>();
        appSessions.add(appSession);
        AppJsonWriter appJsonWriter = new AppJsonWriter();
        String str = appJsonWriter.writeJson(appSessions);
        String endpoint = SERVER_URL;
        String body = str;
        int status = 0;
        try {
            status = AppJsonWriter.post(endpoint, body);
        } catch (IOException e) {
            Log.e(LOG_TAG, "request couldn't be sent "+status);
            e.printStackTrace();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(this);
        appController = new DatabaseController(getApplicationContext());

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);

            final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
            // checks if the information is not stale
//		    long expirationTime =
//		            prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
//		    return System.currentTimeMillis() > expirationTime;

            if (isRegistrationExpired()) {
                registerInBackground();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, System.currentTimeMillis());
                editor.commit();
            } else {
                regid = getRegistrationId(getApplicationContext());
                    //registerInBackground();
                if (regid.isEmpty()) {
                    registerInBackground();
                }
            }

        } else {
            Log.i(LOG_TAG, "No valid Google Play Services APK found.");
        }

        console = null;
        try {
            console = appController.getConsoleDao().queryForId(1);//.queryForAll().get(0);
            if (console != null) {
                try {
                    if (console.getCurrency().contains("EUR")) {
                        currency = "€";
                    }else {
                        currency = console.getCurrency();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String language_code = Locale.getDefault().getDisplayName();
        Log.e(" language_code DisplayName: ", " "+language_code);
        Log.e(" language_code DisplayCountry : ", " "+Locale.getDefault().getDisplayCountry());
        Log.e(" language_code DisplayLanguage : ", " "+Locale.getDefault().getDisplayLanguage());

        UrlHelpers.LANG_EXTRA = wmbPreference.getString(UrlHelpers.LANG, "&langue=fr");

        Utils.changeLocale(UrlHelpers.LANG_EXTRA.split("=")[1], this);

        UrlHelpers.updateUrlHelpers();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            List<CategoriesGetter> cats = appController.getMyBoxCategoriesDao().queryForAll();
//			List<CategoriesGetter> cats2 = controller.getMyBoxCategoriesDao().queryForAll();
            for (int i = 0; i < cats.size(); i++) {
                if ((appController.getAllCategoryBoxs(cats.get(i).getId_categorie()).size() == 0)) {
                    appController.getMyBoxCategoriesDao().delete(cats.get(i));
                }
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            colors = mapper.readValue(getApplicationContext().getResources().getAssets()
                    .open("color_scheme.json", 1), Colors.class);//color_scheme_alpaga.json
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

        if (colors != null){
            DatabaseController appController = new DatabaseController(getApplicationContext());
            appController.addColors(colors);
        }

        map = (HashMap<String, Object>) getLastCustomNonConfigurationInstance();


        if(isTablet) {
            setContentView(R.layout.activity_boxs_main);

            findViewById(R.id.appBackground).setBackgroundColor(0x55000000);
            getActionBar().hide();
            findViewById(R.id.box_page_fragment).setVisibility(View.GONE);


            MainContentFragment contentFragment = new MainContentFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, contentFragment, CONTENT_FRAGMENT).commit(); //.addToBackStack(CONTENT_FRAGMENT).commit();

            CartFragment cartFragment = new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.cart_fragment, cartFragment, CART_FRAGMENT).commit(); //.addToBackStack(CART_FRAGMENT).commit();

        }
        else {

            setContentView(R.layout.test_smart_activity_boxs_main);

            if(android.os.Build.VERSION.SDK_INT >= 11){
                android.app.ActionBar actionBar = getActionBar();
                if(actionBar != null) {
                    actionBar.setDisplayShowHomeEnabled(false);
                    actionBar.setDisplayUseLogoEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(false);
                    actionBar.setCustomView(R.layout.bar_view);
                    actionBar.setDisplayShowCustomEnabled(true);

                    final View customView = actionBar.getCustomView();
                    if (customView != null) {
                        customView.findViewById(R.id.barView).setBackgroundColor(colors.getColor(colors.getNavigation_color()));
                        TextView appTitle = (TextView) customView.findViewById(R.id.titleBar);
                        appTitle.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
                        appTitle.setTextColor(Color.parseColor("#CC000000")/*colors.getColor(colors.getTitle_color())*/);
                        appTitle.setText(getString(R.string.gift_boxes));

                        customView.findViewById(R.id.cartGetter).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                menu.toggle();
                                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag(CART_FRAGMENT);
                                if (cartFragment != null) {
                                    cartFragment.fillCart();
                                }

                            }
                        });
                        customView.findViewById(R.id.about).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                getAboutPage(customView);
                            }
                        });
                    }
                }
            }



            findViewById(R.id.appBackground).setBackgroundColor(colors.getColor(colors.getNavigation_color()));

            MainContentFragment contentFragment = new MainContentFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, contentFragment, CONTENT_FRAGMENT).commit(); //.addToBackStack(CONTENT_FRAGMENT).commit();



            menu = new SlidingMenu(this);
            menu.setMode(SlidingMenu.RIGHT);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
            menu.setFadeDegree(0.35f);
            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);


            menu.setMenu(R.layout.cart_test);
            CartFragment cartFragment = new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.test_cart, cartFragment, CART_FRAGMENT).commit();  //.addToBackStack(CART_FRAGMENT).commit();
			/*//
			fillCartView();
			//Resources r = getResources();

			//deprecated code...
			 * DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, dm);
			if (dm.widthPixels > 800 ) {
				px = (float) ((float)((float)dm.widthPixels*(float)40)/100.0);
			}else if (dm.widthPixels >600 && dm.widthPixels < 800) {
				px = (float) ((float)((float)dm.widthPixels*(float)50)/100.0);
			}else {
				px = (float) ((float)((float)dm.widthPixels*(float)75)/100.0);
			}

			menu.setBehindWidth((int) px);*/


//			LinearLayout footerGetter = (LinearLayout)findViewById(R.id.footerGetter);
//
//			footerGetter.setBackgroundColor(colors.getColor(colors.getNavigation_color(),"CC"));
//
//			footerGetter.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					FrameLayout fl = (FrameLayout)findViewById(R.id.footer_fragment);
//					if(fl.getVisibility() == View.GONE)
//						fl.setVisibility(View.VISIBLE);
//					else
//						fl.setVisibility(View.GONE);
//
//				}
//			});






        }

        //color colors;


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

//	@Override
//	public void selectCategory(int id_cat) {
//		BoxsListFragment boxsListFragment = (BoxsListFragment) getSupportFragmentManager().findFragmentByTag(BOXS_LIST_FRAGMENT);
//		BoxsGridFragment boxsGridFragment = (BoxsGridFragment) getSupportFragmentManager().findFragmentByTag(BOXS_GRID_FRAGMENT);
////		FavoriteFragment favoriteFragment = (FavoriteFragment)getSupportFragmentManager().findFragmentByTag(FAVORITE_FRAGMENT);
//		FooterFragment footerFragment = (FooterFragment)getSupportFragmentManager().findFragmentByTag(FOOTER_FRAGMENT);
////		if (favoriteFragment != null) {
////			getSupportFragmentManager().beginTransaction().remove(favoriteFragment);
////		}else{
////
////			favoriteFragment = new FavoriteFragment();
////
////			getSupportFragmentManager().beginTransaction().replace(R.id.boxs_fragment, favoriteFragment, FAVORITE_FRAGMENT).addToBackStack(null).commit();
////
////		}
//
//		if (boxsListFragment != null) {
//			boxsListFragment.selectCategory(id_cat);
//			getSupportFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BOXS_LIST_FRAGMENT).addToBackStack(BOXS_LIST_FRAGMENT).commit();
//		}
////		else if(favoriteFragment != null)
////		else {
////			boxsListFragment = new BoxsListFragment();
////			Bundle args = new Bundle();
////			args.putInt("id_categorie", id_cat);
////			boxsListFragment.setArguments(args );
////			getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BOXS_FRAGMENT).addToBackStack(BOXS_FRAGMENT).commit();
////		}
//
//		else if (boxsGridFragment != null) {
//			boxsGridFragment.selectCategory(id_cat);
//			getSupportFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
//
//		}
////		else{
////			boxsGridFragment = new BoxsGridFragment();
////			Bundle args = new Bundle();
////			args.putInt("id_categorie", id_cat);
////			boxsGridFragment.setArguments(args );
////			getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
////
////
////		}
//
//		if (isTablet) {
//			if (footerFragment != null) {
//				if (footerFragment.displayMode != 2) {
//					if (boxsGridFragment != null) {
//						boxsGridFragment.selectCategory(id_cat);
//						getSupportFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
//
//					}else {
//						boxsGridFragment = new BoxsGridFragment();
//						Bundle args = new Bundle();
//						args.putInt("id_categorie", id_cat);
//						boxsGridFragment.setArguments(args );
//						getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
//					}
//				}else {
//
//					if (boxsListFragment != null) {
//						boxsListFragment.selectCategory(id_cat);
//						getSupportFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BOXS_LIST_FRAGMENT).addToBackStack(BOXS_LIST_FRAGMENT).commit();
//					}else {
//						boxsListFragment = new BoxsListFragment();
//						Bundle args = new Bundle();
//						args.putInt("id_categorie", id_cat);
//						boxsListFragment.setArguments(args );
//						getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BOXS_LIST_FRAGMENT).addToBackStack(BOXS_LIST_FRAGMENT).commit();
//					}
//
//				}
//			}else {
//				if (boxsGridFragment != null) {
//					boxsGridFragment.selectCategory(id_cat);
//					getSupportFragmentManager().beginTransaction().remove(boxsGridFragment).add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
//
//				}else {
//					boxsGridFragment = new BoxsGridFragment();
//					Bundle args = new Bundle();
//					args.putInt("id_categorie", id_cat);
//					boxsGridFragment.setArguments(args );
//					getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsGridFragment, BOXS_GRID_FRAGMENT).addToBackStack(BOXS_GRID_FRAGMENT).commit();
//				}
//			}
//		}else {
//			if (boxsListFragment != null) {
//				boxsListFragment.selectCategory(id_cat);
//				getSupportFragmentManager().beginTransaction().remove(boxsListFragment).add(R.id.boxs_fragment, boxsListFragment, BOXS_LIST_FRAGMENT).addToBackStack(BOXS_LIST_FRAGMENT).commit();
//			}else {
//				boxsListFragment = new BoxsListFragment();
//				Bundle args = new Bundle();
//				args.putInt("id_categorie", id_cat);
//				boxsListFragment.setArguments(args );
//				getSupportFragmentManager().beginTransaction().add(R.id.boxs_fragment, boxsListFragment, BOXS_LIST_FRAGMENT).addToBackStack(BOXS_LIST_FRAGMENT).commit();
//			}
//		}
//
//
//	}
//
//

    private EditText email, pwd;
    private AViewFlipper viewFlipper;
    private int current;
    private Console console;

    public View fillCartView() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = menu;
        view.findViewById(R.id.cartBackground).setBackgroundColor(0xFFE0DCCC);
        LinearLayout auth_container = (LinearLayout)view.findViewById(R.id.authContainer);

        RoundRectShape rect = new RoundRectShape(new float[] {10, 10, 10, 10, 10 ,10, 10, 10}, null, null);
        ShapeDrawable bg = new ShapeDrawable(rect);
        bg.getPaint().setColor(0x90ffffff);
        auth_container.setBackgroundDrawable(bg);


//				final EditText email = (EditText) view.findViewById(R.id.email);
//				final EditText pwd = (EditText) view.findViewById(R.id.password);

        email = (EditText) view.findViewById(R.id.email);
        pwd = (EditText) view.findViewById(R.id.password);

        view.findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean b = isEmailValid(email.getText().toString());
                int length = pwd.getText().toString().length();

//						if(isEmailValid(email.getText().toString()) && pwd.getText().toString().length() > 0) {
                if(b && length > 0) {

                    Toast.makeText(getApplicationContext(), "Parameters ( email : "+email.getText().toString()+" , Password : "+pwd.getText().toString()+" ) are corrects and can be sent to a server!", Toast.LENGTH_SHORT).show();


                    postNewRequest(getApplicationContext(), email.getText().toString(), pwd.getText().toString());

                }else {
                    Toast.makeText(getApplicationContext(), "Parameters are not corrects and can't be sent to a server!", Toast.LENGTH_SHORT).show();

                    String email = "outoun.uness@gmail.com"; //"loic.bruniquel@euphor.ma";
                    String pwd = "holz&EDOJAWA12";// "sponge";

                    postNewRequest(getApplicationContext(), email, pwd);
                }


            }
        });


        return view;
    }


    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|( [a-zA-Z]{1}|[\\w-]{2,}))@"
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


    public static void postNewRequest(final Context context, final String email, final String pwd){
        mPostCommentResponse = new PostCommentResponseListener() {

            ProgressDialog dialog ;

            @Override
            public void requestStarted() {
                Log.e(" requestStarted "," <<<>>>>");
                this.dialog = new ProgressDialog(menu.getContext());
                this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                this.dialog.setIndeterminate(false);
                this.dialog.setCancelable(false);
                this.dialog.show();

            }

            @Override
            public void requestEndedWithError(VolleyError error) {
                Log.e(" requestEndedWithError "," <<<< error : "+error+" >>>>");

            }

            @Override
            public void requestCompleted() {
                Log.e(" requestCompleted "," <<<>>>>");
                this.dialog.dismiss();
            }
        };
        mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, UrlHelpers.GET_AUTHENTICATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mPostCommentResponse.requestCompleted();
                Log.e(" onResponse : ","  "+response);

                ObjectMapper mapper = new ObjectMapper();

                mapper = new ObjectMapper();
                JsonNode input;
                JsonNode results = null;
                String token_id = "-1";
                try {
                    input = mapper.readTree(response);
                    results = input.get("token_id");

                    if(results != null) {
                        token_id = results.textValue();

                        SharedPreferences wmbPreference = PreferenceManager
                                .getDefaultSharedPreferences(context);

                        SharedPreferences.Editor editor = wmbPreference.edit();
                        editor.putString("token_id", token_id);
                        editor.putString("mail", input.get("mail").textValue());
                        editor.commit();

                        menu.getRootView().findViewById(R.id.auth_space).setVisibility(View.GONE);
                        menu.getRootView().findViewById(R.id.create_account_space).setVisibility(View.GONE);
                        menu.getRootView().findViewById(R.id.authentication_home).setVisibility(View.VISIBLE);
                        TextView  userAccount = (TextView) menu.getRootView().findViewById(R.id.userAccount);
                        userAccount.setText(input.get("mail").textValue());
                        userAccount.setTextColor(0xCC52514F);

                        RoundRectShape rect = new RoundRectShape(
                                new float[] {10, 10, 10, 10, 10 ,10, 10, 10},
                                null,
                                null);
                        ShapeDrawable bgOrders = new ShapeDrawable(rect);
                        bgOrders.getPaint().setColor(0xFF52514F);
                        menu.getRootView().findViewById(R.id.orders).setBackgroundDrawable(bgOrders);
                        menu.getRootView().findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub

                            }
                        });

                        ShapeDrawable bgLogout = new ShapeDrawable(rect);
                        bgLogout.getPaint().setColor(0xFF52514F);
                        menu.getRootView().findViewById(R.id.logout).setBackgroundDrawable(bgLogout);
                        menu.getRootView().findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                SharedPreferences wmbPreference = PreferenceManager
                                        .getDefaultSharedPreferences(context);

                                SharedPreferences.Editor editor = wmbPreference
                                        .edit();
                                editor.remove("token_id");
                                editor.remove("mail");
                                editor.commit();

                                menu.getRootView().findViewById(R.id.auth_space).setVisibility(View.VISIBLE);
                                menu.getRootView().findViewById(R.id.create_account_space).setVisibility(View.VISIBLE);
                                menu.getRootView().findViewById(R.id.authentication_home).setVisibility(View.GONE);


                            }
                        });

                    }else {

                        AlertDialog.Builder alert = new AlertDialog.Builder(menu.getContext());
                        alert.setIcon(android.R.drawable.ic_dialog_info);
                        alert.setTitle(" Erreur ");


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


    public void getAboutPage(View rootView) {


        LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        View view = mInflater.inflate(R.layout.about_page, null, false);

        DatabaseController appController = new DatabaseController(getApplicationContext());
        console = null;
        try {
            List<Console> consoles = appController.getConsoleDao().queryForAll();
            if(consoles != null && consoles.size() > 0)
                console = consoles.get(0);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final PopupWindow pw = new PopupWindow(view, (int) getResources().getDimension(R.dimen.window_width), (int) getResources().getDimension(R.dimen.window_height), true);

        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new ColorDrawable(colors.getColor(colors.getBackground_color())));
        pw.setFocusable(true);

        try {
            // get input stream
            InputStream ims = getAssets().open("ribbon.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to Background
            view.findViewById(R.id.about_page_bg).setBackgroundDrawable(d);
        }
        catch(IOException ex) {
        }

        view.findViewById(R.id.aboutBtnContainer).setBackgroundColor(colors.getColor(colors.getAlternate_background_color()));
        view.findViewById(R.id.aboutConditionBtn).setBackgroundDrawable(colors.makeGradientToColor(colors.getTitle_color()));//.setBackgroundColor(colors.getColor(colors.getTitle_color()));
        view.findViewById(R.id.aboutContactBtn).setBackgroundColor(colors.getColor(colors.getTitle_color()));

        ((TextView)view.findViewById(R.id.aboutConditionBtn)).setTextSize(10);


//		((TextView)view.findViewById(R.id.aboutTitle1)).setTextColor(colors.getColor(colors.getTitle_color()));
        ((TextView)view.findViewById(R.id.aboutTitle2)).setTextColor(colors.getColor(colors.getTitle_color()));
        ((TextView)view.findViewById(R.id.aboutTitle3)).setTextColor(colors.getColor(colors.getTitle_color()));

        ((TextView)view.findViewById(R.id.aboutDescription)).setTextColor(colors.getColor(colors.getText_color()));

        viewFlipper = (AViewFlipper) view.findViewById(R.id.viewFlipper);
        viewFlipper.setFlipperColor(colors.getColor(colors.getTitle_color()));
        current = 3;


        TextView title = (TextView) view.findViewById(R.id.aboutTitle1);
        title.setTextColor(colors.getColor(colors.getTitle_color()));
        title.setText(CategoriesFragment.applicationTitle);
        //title.setText(String.format(getString(R.string.about_screen1), applicationTitle));

        ImageView imageView = (ImageView)view.findViewById(R.id.aboutImg_);//.findViewById(R.id.aboutImg);
        //imageView.setTag(R.id.about, 919191);

        TextView discrip = (TextView)view.findViewById(R.id.aboutDescription);
        discrip.setTextColor(colors.getColor(colors.getText_color()));
        discrip.setText(Utils.removeHtmlTags(console.getPresentation_console()));

        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        Glide.with(getApplicationContext()).load(console.getLien_image_presentation_console()).into(imageView);



        if(current > 1)
            viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    int upX, downX = 0;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = (int) event.getX();
                        return true;
                    }

                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        upX = (int) event.getX();

                        if (upX - downX > 100) {

                            current--;
                            if (current < 0) {
                                current = 2;
                            }

                            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_left));
                            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right));

                            viewFlipper.showPrevious(); //.setImageResource(imgs[current]);
                        }

                        else if (downX - upX > - 100) {

                            current++;
                            if (current > 2) {
                                current = 0;
                            }


                            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right));
                            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_left));

                            viewFlipper.showNext(); //.setImageResource(imgs[current]);
                        }
                        return true;
                    }

                    return false;

                }

            });

        pw.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        Button condBtn = (Button) view.findViewById(R.id.aboutConditionBtn);
        Button contactBtn = (Button) view.findViewById(R.id.aboutContactBtn);

        RoundRectShape rectCondBtn = new RoundRectShape(
                new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
                null,
                null);

        ShapeDrawable bgCondBtn = new ShapeDrawable(rectCondBtn);
        bgCondBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
        condBtn.setBackgroundDrawable(bgCondBtn);
        //contactBtn.setBackgroundDrawable(bgCondBtn);

        RoundRectShape rectContactBtn = new RoundRectShape(
                new float[] {5, 5, 5, 5, 5 , 5, 5, 5},
                null,
                null);

        ShapeDrawable bgContactBtn = new ShapeDrawable(rectContactBtn);
        bgContactBtn.getPaint().setColor(colors.getColor(colors.getTitle_color()));
        contactBtn.setBackgroundDrawable(bgContactBtn);
        final Context context = this;

        condBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String mimeType = "text/html";
                String encoding = "utf-8";

                WebView webView = new WebView(context);
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


                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",console.getEmail_contact(), BoxsMainActivity.BOXS_LIST_FRAGMENT));

                startActivity(emailIntent);
            }
        });



        view.findViewWithTag("aboutClose")/*.findViewById(R.id.aboutClose)*/.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();

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

}
