package com.paperpad.mybox.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.j256.ormlite.table.TableUtils;
import com.paperpad.mybox.ApplicationInit;
import com.paperpad.mybox.R;
import com.paperpad.mybox.controllers.DatabaseController;
import com.paperpad.mybox.helpers.ImageUtils;
import com.paperpad.mybox.helpers.UrlHelpers;
import com.paperpad.mybox.helpers.Utils;
import com.paperpad.mybox.models.CategoriesGetter;
import com.paperpad.mybox.models.Console;
import com.paperpad.mybox.models.Illustration;
import com.paperpad.mybox.models.MyBox;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.todddavies.components.progressbar.ProgressWheel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class SplashActivity extends Activity {

	private RequestQueue mRequestQueue;
//	private ProgressDialog pd;
	private ProgressWheel pw;
	private DatabaseController appController;
	protected Console console;
	protected boolean update = false;
	private SharedPreferences wmbPreference;
	protected int last_update;
	private boolean langChanged = false;
	
	public static final int rowCount = 3;
	public static final int columnCount = 4;
	private static final String DIRECTORY_IMAGES = "MyBox";
	protected static final String TAG = "SplashActivity";
	public static int[][] imageRowColumn;
	
	public int count = 0;
	public boolean[] b = new boolean[]{false, false, false, false,
								false, false, false, false,
								false, false, false, false};
	private LinearLayout gridImgDownloaded;
	private int versionCodePrecedent;
	private boolean updated_version = false;

    protected Context context;
    GoogleCloudMessaging gcm;
    public String regid;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        context = getApplicationContext();

		if (!getResources().getBoolean(R.bool.isTablet)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		}
		
		imageRowColumn = new int[][]{{R.id.img_column00, R.id.img_column01, R.id.img_column02, R.id.img_column03}, 
				{R.id.img_column10, R.id.img_column11, R.id.img_column12, R.id.img_column13},
				{R.id.img_column20, R.id.img_column21, R.id.img_column22, R.id.img_column23}};

        gridImgDownloaded = (LinearLayout) findViewById(R.id.downloadedImageLayout);
		
		mRequestQueue = ((ApplicationInit)getApplication()).mRequestQueue;
		mRequestQueue.getCache().clear();


        wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(this);
		// version code from preference to know if an update was carried out
		versionCodePrecedent = wmbPreference.getInt("VersionCode", 0);


        /** GCM Registration **/

//        if (servicesConnected()) {
//            gcm = GoogleCloudMessaging.getInstance(this);
//            regid = getRegistrationId(getApplicationContext());
//			sendRegistrationIdToBackend(regid);
////			registerInBackground();
//            if (regid.isEmpty()) {
//                registerInBackground();
//            }
//        } else {
//            Log.i(SplashActivity.class.getCanonicalName(), "No valid Google Play Services APK found.");
//        }

                /** **/

		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager()
					.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			if (versionCode > versionCodePrecedent) {
				updated_version = true;
			}
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		if(android.os.Build.VERSION.SDK_INT >= 11){
			getActionBar().hide();
		}
		
		Bundle b = getIntent().getExtras();
		if (b != null && b.getString(Utils.LANG)!=null && !b.getString(Utils.LANG).isEmpty()) {
			langChanged  = true;
		}

		String language_code = Locale.getDefault().getDisplayName();
		Log.e(" language_code DisplayName: ", " "+language_code);
		Log.e(" language_code DisplayCountry : ", " "+Locale.getDefault().getDisplayCountry());
		Log.e(" language_code DisplayLanguage : ", " "+Locale.getDefault().getDisplayLanguage());
		Log.e(" language_code ISO3Country : ", " "+Locale.getDefault().getISO3Country());


		UrlHelpers.LANG_EXTRA = wmbPreference.getString(UrlHelpers.LANG_EXTRA, "&langue=fr");

		Utils.changeLocale(UrlHelpers.LANG_EXTRA.split("=")[1], this);

        UrlHelpers.CONSOLE_ID = getString(R.string.console_id);

		UrlHelpers.updateUrlHelpers();
		
		//String url = "http://consolemybox.apicius.com/services_ios/get_categorie_console?console_id=60b1a93aa5681eaf431a66368f2c00a8"+UrlHelpers.LANG_EXTRA;

		//		pd = ProgressDialog.show(this, "Please Wait...", "Please Wait...");
		pw = (ProgressWheel)findViewById(R.id.pw_spinner);
		pw.startSpinning();
		appController = new DatabaseController(getApplicationContext());
		console = new Console();


		final JsonArrayRequest boxsRequest = new JsonArrayRequest(UrlHelpers.GET_COFFRET_URL, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				Log.i("TAG response coffrets", response.toString());
				ObjectMapper mapper = new ObjectMapper();
				List<MyBox> coffrets = new ArrayList<MyBox>();
				try {
					pw.incrementProgress();
					coffrets = mapper.readValue(response.toString(), new TypeReference<List<MyBox>>() {});
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (coffrets != null){
					Log.i("TAG size coffrets", coffrets.size()+ "");
				}
				if (coffrets != null) {
					appController.saveToDBMybox(coffrets);
					pw.incrementProgress();
				}
				DownloadAsync async = new DownloadAsync();
				async.execute();
//				downloadAllImages();

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG error boxsRequest", "an error" );
				error.fillInStackTrace();
				pw.stopSpinning();
				pw.setVisibility(View.GONE);

                appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
                Intent intent = new Intent(getApplicationContext(), BoxsMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

			}
		});
		

		
		final JsonObjectRequest consoleRequest = new JsonObjectRequest(Request.Method.GET, UrlHelpers.GET_CONSOLE_URL,
				null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.i("TAG response console", response.toString());
				ObjectMapper mapper = new ObjectMapper();
				pw.incrementProgress();
				try {
					console = mapper.readValue(
							response.toString(), Console.class);
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

				if (console != null){
					try {
						TableUtils.dropTable(appController.getConnectionSource(), Console.class, true);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						TableUtils.createTable(appController.getConnectionSource(), Console.class);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (last_update != 0) {
						console.setLast_update(last_update);
					}
					appController.saveToDBConsole(console);
					pw.incrementProgress();
					Log.i("TAG url web console", console.getUrl_web_console() + "");
				}

				/*mRequestQueue.add(lastUpdateRequest).setRetryPolicy(new DefaultRetryPolicy(
						15000,
						3,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

				//						pd.dismiss();


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG error consoleRequest", "an error" );
				error.fillInStackTrace();
				//						pd.dismiss();
				//pw.stopSpinning();
				//pw.setVisibility(View.GONE);
                mRequestQueue.add(boxsRequest);
			}
		});

        final JsonArrayRequest categoriesRequest = new JsonArrayRequest(UrlHelpers.GET_CATEGORIES_URL, new Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("TAG response categories", response.toString());
                ObjectMapper mapper = new ObjectMapper();
                List<CategoriesGetter> cats = new ArrayList<CategoriesGetter>();
                try {
                    pw.incrementProgress();
                    cats = mapper.readValue(response.toString(), new TypeReference<List<CategoriesGetter>>() {});
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

                if (cats != null){
                    appController.saveToDBMyboxCategories(cats);
                    Log.i("TAG size cats", cats.size()+ "");
                    pw.incrementProgress();
                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG error categoriesRequest", "an error" );
                error.fillInStackTrace();
                //pw.stopSpinning();
                //pw.setVisibility(View.GONE);

                mRequestQueue.add(consoleRequest);
            }
        });

		//mRequestQueue.add(consoleRequest);

		StringRequest lastUpdateRequest = new StringRequest(UrlHelpers.GET_LAST_UPDATE_URL, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (console != null) {


					if (response != null) {
						response = response.replaceAll("\\D+","");
						try {
							last_update = Integer.parseInt(response);
//							console.setLast_update(Integer.parseInt(response));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						List<Console> list = appController.getConsoleDao().queryForAll();
						Console consoleDB = list.size()>0?list.get(0) : null;
						if (consoleDB != null && consoleDB.getLast_update() != null && Integer.parseInt(response) != 0) {
							if (updated_version || langChanged || consoleDB.getLast_update() < Integer.parseInt(response)) {
								update = true;
								appController.dropAllTables(appController.getConnectionSource());
                                appController.createAllTables(appController.getConnectionSource());
                                mRequestQueue.add(consoleRequest).setRetryPolicy(new DefaultRetryPolicy(
										15000,
										2,
										DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                mRequestQueue.add(boxsRequest).setRetryPolicy(new DefaultRetryPolicy(
                                        15000,
                                        2,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

								mRequestQueue.add(categoriesRequest).setRetryPolicy(new DefaultRetryPolicy(
										15000,
										2,
										DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
							}else {
								DownloadAsync async = new DownloadAsync();
								async.execute();
							}
						}else {
							appController.dropAllTables(appController.getConnectionSource());
                            appController.createAllTables(appController.getConnectionSource());
							mRequestQueue.add(consoleRequest).setRetryPolicy(new DefaultRetryPolicy(
									15000,
									2,
									DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
							mRequestQueue.add(boxsRequest).setRetryPolicy(new DefaultRetryPolicy(
									15000,
									2,
									DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

							mRequestQueue.add(categoriesRequest).setRetryPolicy(new DefaultRetryPolicy(
									15000,
									2,
									DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

						}
					} catch (SQLException e) {

						e.printStackTrace();

						appController.dropAllTables(appController.getConnectionSource());
						appController.createAllTables(appController.getConnectionSource());
						mRequestQueue.add(consoleRequest).setRetryPolicy(new DefaultRetryPolicy(
								15000,
								2,
								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
						mRequestQueue.add(boxsRequest).setRetryPolicy(new DefaultRetryPolicy(
								15000,
								2,
								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

						mRequestQueue.add(categoriesRequest).setRetryPolicy(new DefaultRetryPolicy(
								15000,
								2,
								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					}

				}
				Log.i("TAG last update", console.getLast_update() + "");

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG error", "an error" );
				error.fillInStackTrace();
				pw.stopSpinning();
				pw.setVisibility(View.GONE);

                appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
                Intent intent = new Intent(getApplicationContext(), BoxsMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

			}
		});


		mRequestQueue.add(lastUpdateRequest).setRetryPolicy(new DefaultRetryPolicy(
				15000,
				2,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		ImageRequest imageRequest = new ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener)

	}

	public class DownloadAsync extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			downloadPictures();
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result) {
			pw.stopSpinning();
			pw.setVisibility(View.GONE);
			appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
			Intent intent = new Intent(getApplicationContext(), BoxsMainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			super.onPostExecute(result);
		}
		
	}
	
	protected void downloadPictures() {

		List<Illustration> illustrations = new ArrayList<Illustration>();
		try {
			illustrations = appController.getIllustrationDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("", ""+illustrations.size());
		for (Iterator<Illustration> iterator = illustrations.iterator(); iterator.hasNext();) {
			Illustration illustration = (Illustration) iterator.next();
			if (illustration != null && ((illustration.getLink() != null && !illustration.getLink().isEmpty()) && !illustration.getDownloaded())) {

				saveIllustrationToDeviceOptimized(illustration, illustration.getLink(), false);

//				if (illustration.getFullLink() != null && !illustration.getFullLink().isEmpty()) {
//					saveIllustrationToDeviceOptimized(illustration,
//							illustration.getFullLink(), true);
//				}

				pw.incrementProgress();

			}else {
				/*try {
					appController.getIllustrationDao().delete(illustration);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}


		}


	
		
	}
	
	
	private void saveIllustrationToDevice(Illustration illustration,
			String link, boolean isFull) {
		if (link != null) {
			String path = link;
			path.replaceAll("http://backoffice.paperpad.fr/", "");
			String[] temp = path.split("/");
			String imageName = temp[temp.length - 1];
			//Log.i("Image Name", imageName);
			path.replaceAll(imageName, "");
			String newFilePath = "";
			for (int j = 0; j < temp.length - 1; j++) {
				newFilePath = newFilePath + temp[j] + "/";
			}
			//Log.i("le path sans image", path);
			//Setting up cache directory to store the image
			File cacheDir = new File(
					Environment.getExternalStorageDirectory(), "Paperpad/"
							+ newFilePath);
			
			/** Uness Modif ==> stockage des image sur le cache dir **/
			cacheDir = new File(getApplicationContext()
					.getCacheDir(), "Paperpad/"+newFilePath);
			
			
			// Check if cache folder exists, otherwise create folder. 
			if (!cacheDir.exists())
				cacheDir.mkdirs();
			Log.i("path image", cacheDir.getAbsolutePath());
			// Setting up file to write the image to. 
			try {
				File f = new File(cacheDir, imageName);
				//Log.i("path file to be created", f.getAbsolutePath());
					
				//CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	
				// If the response does not enclose an entity, there is no need
				// to worry about connection release
//				boolean IknowDimensions = false;
				if (!f.exists()) {
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.getParams().setParameter(
							ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	
					//InputStream is = new URL("http://backoffice.paperpad.fr/"+listMedias.get(i).getPath()).openStream();
					//Point point;
					
					if (link != null && !link.isEmpty()) {
//						link = Utils.encodeUrl(link);
						link = link.replaceAll(" ", "%20");
						String[] tmp = link.split("/");
						String nameImage = tmp[tmp.length-1];
						String correctName = URLEncoder.encode(nameImage);
						link = link.replace(nameImage, correctName);
						// Prepare a request object
						HttpGet httpget = new HttpGet(link);
						// Execute the request
						HttpResponse response = httpclient.execute(httpget);
						// Examine the response status
						System.out.println(response.getStatusLine());
						// Get hold of the response entity
						HttpEntity entity = response.getEntity();
						// Open InputStream to download the image. 
						//URLConnection connection = new URL("http://backoffice.paperpad.fr/"+listMedias.get(i).getPath()).openConnection();
						InputStream is = entity.getContent();
						OutputStream os = new FileOutputStream(f);
						ImageUtils.CopyStream(is, os);
						
						final File file_illustration= f;
						runOnUiThread(new Runnable() {
							Random r = new Random();
							int index, row, column;
							//boolean isFilled = false;
							
							public void run() {
								///((ImageView)table.findViewById(imageRowColumn[r.nextInt(3)][r.nextInt(3)]));
								
								index = r.nextInt(rowCount*columnCount);
//								row = (int) (index/4);
//								column = (int) (row/3);
								//	if(b[index] == false || (count >= rowCount*columnCount)){
										
										count++;
										b[index] = true;
										
										if(index >= 0 && index <4)
										{
											row = 0;
											column = 3 - index;
										}
										else if(index >=4  && index <8)
										{
											row = 1;
											column = 7 - index;
										}
										else if(index >= 8)
										{
											row = 2;
											column = 11 - index;
										}
										
										Glide.with(getApplicationContext()).load(file_illustration).into((ImageView)gridImgDownloaded.findViewById(imageRowColumn[row][column]));
							//	}
								
								
							}
						});
						
						
					} else {
						
					}
	
				}
				
	            
	            
	            
	            
				if (!isFull) {
					if (f.exists() && illustration.getLink() != null
							&& !illustration.getLink().isEmpty()) {
						FileInputStream fis = new FileInputStream(f);
						if (!(illustration.getOriginalWidth() > 0)) {
							Point point = ImageUtils.getSizeOfImage(fis);
							illustration.setOriginalWidth(point.x);
							illustration.setOriginalHeight(point.y);
						}
						
						illustration.setPath(f.getAbsolutePath());
						illustration.setDownloaded(true);
						
						
						try {
							appController.getIllustrationDao()
									.createOrUpdate(illustration);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
					}
				} else {
					if (f.exists() && link != null && !link.isEmpty()) {
						illustration.setFullPath(f.getAbsolutePath());
						illustration.setDownloaded(true);
						try {
							appController.getIllustrationDao()
									.createOrUpdate(illustration);
							
							/** Maybe Modif here **/
							
							
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
					}
				}
	
	
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {/*
			try {
				appController.getIllustrationDao().delete(illustration);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		*/}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	Crashlytics.log(Log.INFO, getClass().getCanonicalName(), "Media Mounted");
	        return true;
	    }
	    
	    return false;
	}
	
	
	private boolean saveImageToExternalStorage(Bitmap finalBitmap, String fname) {
		boolean downloaded = false;
//	    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
		String root = getFirstWritableDirectory().toString();
	    File myDir = new File(root + "/"+DIRECTORY_IMAGES);
	    myDir.mkdirs();
	   /* Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-" + n + ".jpg";*/
	    File file = new File(myDir, fname);
	    if (file.exists())
	        file.delete();
	    try {
	        FileOutputStream out = new FileOutputStream(file);
	        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	        out.flush();
	        out.close();
	        downloaded = true;
	        
	        final File file_illustration = file;
	        runOnUiThread(new Runnable() {
				Random r = new Random();
				int index, row, column;
				//boolean isFilled = false;
				
				public void run() {
					///((ImageView)table.findViewById(imageRowColumn[r.nextInt(3)][r.nextInt(3)]));
					
					index = r.nextInt(rowCount*columnCount);
//					row = (int) (index/4);
//					column = (int) (row/3);
					//	if(b[index] == false || (count >= rowCount*columnCount)){
							
							count++;
							b[index] = true;
							
							if(index >= 0 && index <4)
							{
								row = 0;
								column = 3 - index;
							}
							else if(index >=4  && index <8)
							{
								row = 1;
								column = 7 - index;
							}
							else if(index >= 8)
							{
								row = 2;
								column = 11 - index;
							}
							
							Glide.with(getApplicationContext()).load(file_illustration).into((ImageView)gridImgDownloaded.findViewById(imageRowColumn[row][column]));
				//	}
					
					
				}
			});
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        downloaded = false;
	    }


	    // Tell the media scanner about the new file so that it is
	    // immediately available to the user.
	    MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
	            new MediaScannerConnection.OnScanCompletedListener() {
	                public void onScanCompleted(String path, Uri uri) {
	                    Log.i("ExternalStorage", "Scanned " + path + ":");
	                    Log.i("ExternalStorage", "-> uri=" + uri);
	                }
	    });
	    return downloaded;
	}
	
	
	protected File getFirstWritableDirectory() {
		File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
		//File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
		File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
	    if (file1.exists()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		}/*else if (file2.exists()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		}*/else if (file3.exists()) {
			return Environment.getExternalStorageDirectory();
		}else {
			return Environment.getExternalStorageDirectory();
		}
	}
	
	public void downloadImage(final String url_image, final boolean end) {
//		final  url_image_tmp = url_image.replaceAll(" ", "%20");
//		String url_image_tmp = Uri.encode(url_image);
		String[] temp1 = url_image.split("/");
		String nameFile1 = temp1[temp1.length - 1];
		String url_image_tmp = url_image;
		url_image_tmp = url_image_tmp.replaceAll(nameFile1, "");
		url_image_tmp = url_image_tmp+ Uri.encode(nameFile1);
		pw.setVisibility(View.VISIBLE);
		pw.startSpinning();
		ImageRequest imageRequest = new ImageRequest(url_image_tmp, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				//Log.i(getLocalClassName(), "bitmap laoded");
				/*int i = url_image.lastIndexOf('/');
				int end = getEnd();
				String nameFile = "file_tmp";
				if (i >= 0) {
					nameFile = url_image.substring(i+1, end+4);
				}*/
				String[] temp = url_image.split("/");
				String nameFile = temp[temp.length - 1];
				
				String path = getFirstWritableDirectory().toString();
				Crashlytics.log(Log.INFO, TAG, path);
//				String nameFile = url_image.lastIndexOf("/");
				
				boolean downloaded = saveImageToExternalStorage(response, nameFile)/*saveBitmapPNG(path+"/"+nameFile, response)*/;
				if (downloaded) {
					
					Log.i(getLocalClassName(), "bitmap downloaded");
				}else {
					if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+nameFile).exists()) {
//						downloadNotification(path+DIRECTORY_IMAGES+"/"+nameFile, true);
					}else {
						Log.e("MyBox Image ", "Non enregistrée");
					}
				}
				if (end) {
					pw.stopSpinning();
					pw.setVisibility(View.GONE);
					appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
					Intent intent = new Intent(getApplicationContext(), BoxsMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				
			}

			
		}, 0, 0, null , new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				pw.setVisibility(View.GONE);
				Log.e("MyBox Image ", "Non téléchargé");
//				Toast.makeText(getApplicationContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
//				Crashlytics.log(Log.INFO, TAG, getFirstWritableDirectory().toString());
				if (end) {
					pw.stopSpinning();
					pw.setVisibility(View.GONE);
					appController.createIfNotExistsCart(); // create cart_items table if it doesn't exists
					Intent intent = new Intent(getApplicationContext(), BoxsMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
		mRequestQueue.add(imageRequest);
		
	
	}
	
	public void downloadAllImages() {
		List<Illustration> illustrations = new ArrayList<Illustration>();
		try {
			illustrations = appController.getIllustrationDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("", ""+illustrations.size());
		for (Iterator<Illustration> iterator = illustrations.iterator(); iterator
				.hasNext();) {
			Illustration illustration = (Illustration) iterator.next();
			if (illustration!= null && !illustration.getLink().isEmpty()) {
				String root = getFirstWritableDirectory().toString();
				String[] temp = illustration.getLink().split("/");
				String nameFile = temp[temp.length - 1];
				
			    File file = new File(root + DIRECTORY_IMAGES+"/"+nameFile);
			    if (!file.exists()) {
			    	if (iterator.hasNext()) {
			    		downloadImage(illustration.getLink(), false);
					}else {
						downloadImage(illustration.getLink(), true);
					}
					
				}
				
			}
		}
	}
	
	
	private void saveIllustrationToDeviceOptimized(Illustration illustration,
			String link, boolean isFull) {
		if (link != null) {
			String path = link;
			path.replaceAll("http://backoffice.paperpad.fr/", "");
			String[] temp = path.split("/");
			String imageName = temp[temp.length - 1];
			//Log.i("Image Name", imageName);
			path.replaceAll(imageName, "");
			String newFilePath = "";
			for (int j = 0; j < temp.length - 1; j++) {
				newFilePath = newFilePath + temp[j] + "/";
			}
			//Log.i("le path sans image", path);
			//Setting up cache directory to store the image
			/*File cacheDir = new File(
					Environment.getExternalStorageDirectory(), "Paperpad/"
							+ newFilePath);
			
			/** Uness Modif ==> stockage des image sur le cache dir *
			cacheDir = new File(getApplicationContext()
					.getCacheDir(), "Paperpad/"+newFilePath);*/
			
			String root = getFirstWritableDirectory().toString();
		    File myDir = new File(root + "/"+DIRECTORY_IMAGES);
		    myDir.mkdirs();
			// Check if cache folder exists, otherwise create folder. 
//			if (!myDir.exists())
//				myDir.mkdirs();
			//Log.i("path image", myDir.getAbsolutePath());
			// Setting up file to write the image to. 
			try {
				File f = new File(myDir, imageName);
				//Log.i("path file to be created", f.getAbsolutePath());
					
				//CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	
				// If the response does not enclose an entity, there is no need
				// to worry about connection release
//				boolean IknowDimensions = false;
				if (!f.exists()) {
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.getParams().setParameter(
							ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	
					//InputStream is = new URL("http://backoffice.paperpad.fr/"+listMedias.get(i).getPath()).openStream();
					//Point point;
					
					if (link != null && !link.isEmpty()) {
//						link = Utils.encodeUrl(link);
						link = link.replaceAll(" ", "%20");
						String[] tmp = link.split("/");
						String nameImage = tmp[tmp.length-1];
						String correctName = URLEncoder.encode(nameImage);
						link = link.replace(nameImage, correctName);
						// Prepare a request object
						HttpGet httpget = new HttpGet(link);
						// Execute the request
						HttpResponse response = httpclient.execute(httpget);
						// Examine the response status
						System.out.println(response.getStatusLine());
						// Get hold of the response entity
						HttpEntity entity = response.getEntity();
						// Open InputStream to download the image. 
						//URLConnection connection = new URL("http://backoffice.paperpad.fr/"+listMedias.get(i).getPath()).openConnection();
						InputStream is = entity.getContent();
						OutputStream os = new FileOutputStream(f);
						ImageUtils.CopyStream(is, os);
						
						final File file_illustration= f;
						runOnUiThread(new Runnable() {
							Random r = new Random();
							int index, row, column;
							//boolean isFilled = false;
							
							public void run() {
								///((ImageView)table.findViewById(imageRowColumn[r.nextInt(3)][r.nextInt(3)]));
								
								index = r.nextInt(rowCount*columnCount);
								row = (int) (index/4);
								column = (int) (row/3);
									if(b[index] == false || (count >= rowCount*columnCount)){
										
										count++;
										b[index] = true;
										
										if(index >= 0 && index <4)
										{
											row = 0;
											column = 3 - index;
										}
										else if(index >=4  && index <8)
										{
											row = 1;
											column = 7 - index;
										}
										else if(index >= 8)
										{
											row = 2;
											column = 11 - index;
										}
										
										Glide.with(getApplicationContext()).load(file_illustration).into((ImageView)gridImgDownloaded.findViewById(imageRowColumn[row][column]));
								}
								
								
							}
						});
						
						
					} else {
						
					}
	
				}
				
	            
	            
	            
	            
				if (!isFull) {
					if (f.exists() && illustration.getLink() != null
							&& !illustration.getLink().isEmpty()) {
						FileInputStream fis = new FileInputStream(f);
						if (!(illustration.getOriginalWidth() > 0)) {
						Point point = ImageUtils.getSizeOfImage(fis);
						illustration.setOriginalWidth(point.x);
						illustration.setOriginalHeight(point.y);
						}
						
						illustration.setPath(f.getAbsolutePath());
						illustration.setDownloaded(true);
						
						
						try {
							appController.getIllustrationDao()
									.createOrUpdate(illustration);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
					}
				} else {
					if (f.exists() && link != null && !link.isEmpty()) {
						illustration.setFullPath(f.getAbsolutePath());
						illustration.setDownloaded(true);
						try {
							appController.getIllustrationDao()
									.createOrUpdate(illustration);
							
							/** Maybe Modif here **/
							
							
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
					}
				}
	
	
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				appController.getIllustrationDao().delete(illustration);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getPrivatePreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /** GCM Functions **/

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */

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

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(" SERVICE AVAILABLE ", "GOOGLE SERVICES ARE AVAILABLE");

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                dialog.show();
            }
            return false;
        }
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
        final SharedPreferences prefs = getPrivatePreferences(context);
        String registrationId = prefs.getString("registration_id", getString(R.string.sender_id));//;getString(R.string.sender_id);
        if (registrationId.isEmpty()) {
            Log.i(SplashActivity.class.getCanonicalName(), "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.

        int registeredVersion = prefs.getInt("appVersion", Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(SplashActivity.class.getCanonicalName(), "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getPrivatePreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(SplashActivity.class.getCanonicalName(), "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regId);
        editor.putInt("appVersion", appVersion);
        editor.commit();
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
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(getString(R.string.sender_id));
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend(regid);

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
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //                mDisplay.append(msg + "\n");
                Log.i(SplashActivity.class.getCanonicalName(), msg);
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(final String token ) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                UrlHelpers.SERVER_URL, new Listener<String>() {

            @Override
            public void onResponse(String response) {
//						Toast.makeText(context, getResources().getString(R.string.registed_gcm), Toast.LENGTH_SHORT).show();

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("platform", "android");
                return params;
            };
        };

        mRequestQueue.add(myReq);
    }

    /** **/

}
