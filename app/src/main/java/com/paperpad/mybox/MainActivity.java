package com.paperpad.mybox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperpad.mybox.models.CategoriesGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private RequestQueue mRequestQueue;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String url = "http://consolemybox.apicius.com/services_ios/get_categorie_console?console_id=60b1a93aa5681eaf431a66368f2c00a8&langue=fr";

		mRequestQueue = Volley.newRequestQueue(this);
		pd = ProgressDialog.show(this, "Please Wait...", "Please Wait...");
		try {
			Thread.sleep(2000);
		} catch (Exception e) {

		}
		JsonArrayRequest jarray = new JsonArrayRequest(url, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				Log.i("TAG response", response.toString());
				ObjectMapper mapper = new ObjectMapper();
				List<CategoriesGetter> cats = new ArrayList<CategoriesGetter>();
				try {
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
					Log.i("TAG size cats", cats.size()+ "");
				}
					
				pd.dismiss();
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("TAG error", error.getMessage());
				
			}
		});
		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i("TAG response", response.toString());
						ObjectMapper mapper = new ObjectMapper();
						List<CategoriesGetter> cats = new ArrayList<CategoriesGetter>();
						try {
							cats = mapper.readValue(
									response.toString(),
									new TypeReference<List<CategoriesGetter>>() {
									});
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
							Log.i("TAG size cats", cats.size()+ "");
						}
							
						pd.dismiss();
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("TAG error", error.getMessage());
					}
				});
		mRequestQueue.add(jarray);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
