/**
 * 
 */
package com.paperpad.mybox;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.*;
import com.crashlytics.android.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * @author euphordev02
 *
 */
public class ApplicationInit extends Application {
	public RequestQueue mRequestQueue;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(BuildConfig.BUILD_TYPE.equals("release"))
			Fabric.with(this, new Crashlytics());
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	}
	//Initialize TestFlight with your app token.
//    TestFlight.takeOff(this, "5ffff0e0-ac03-40b2-9c69-e15c66a1472e");
	
}
