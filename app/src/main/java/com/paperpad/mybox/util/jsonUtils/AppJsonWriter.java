/**
 * 
 */
package com.paperpad.mybox.util.jsonUtils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author euphordev02
 *
 */
public class AppJsonWriter {
	
	//JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	public String writeJson(List<AppSession> appSessions) {
		Map<String, JSONArray> map = new HashMap<String, JSONArray>();
		map.put("sessions", writeSessions(appSessions));
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toJSONString();
		
	}
	public JSONArray writeSessions(List<AppSession> appSessions) {
		JSONArray jsonArray = new JSONArray();

		for (AppSession appSession : appSessions) {
			jsonArray.add(writeSession(appSession));
		}
		return jsonArray;
		
	}

	private JSONObject writeSession(AppSession appSession) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("application_mode", appSession.application_mode);
		map.put("application_id", appSession.application_id);
		map.put("application_type", appSession.application_type);
		map.put("application_unique_identifier", appSession.application_unique_identifier);
		map.put("application_version", appSession.application_version);
		map.put("application_push_token", appSession.application_push_token);
		map.put("device_manufacturer", appSession.device_manufacturer);
		map.put("device_model", appSession.device_model);
		map.put("device_platform", appSession.device_platform);
		map.put("device_screen_resolution", appSession.device_screen_resolution);
		map.put("device_screen_size", appSession.device_screen_size);
		map.put("device_system_version", appSession.device_system_version);
		map.put("device_type", appSession.device_type);
		map.put("session_unique_identifier", appSession.session_unique_identifier);
		map.put("start", appSession.start);
		map.put("end", appSession.end);
		try {
			if (appSession.hits != null) {
				map.put("hits", writeHits(appSession.hits));
			}else {
				map.put("hits", "[]");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}

	private JSONArray writeHits(ArrayList<AppHit> hits) {
		JSONArray jsonArray = new JSONArray();

		try {
			for (AppHit hit : hits) {
				jsonArray.add(writeHit(hit));
			}
		} catch (ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return jsonArray;
	}

	private JSONObject writeHit(AppHit hit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("end", hit.end);
		map.put("id", hit.id);
		map.put("start", hit.start);
		map.put("type", hit.type);
	
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject;
	}
	
	public static class postSendAsyncTask extends AsyncTask<String, Integer, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			String endpoint = params[0];
			String body = params[1];
			int status = 0;
			try {
				status = post(endpoint, body);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		}
		
	}
	
	/**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param body request parameters.
     *
     * @throws IOException propagated from POST.
     */
    public static int post(String endpoint, String body)
            throws IOException {    
    	
    	
        URL url;
        
        

        try {
            url = new URL(endpoint);
//            url = new URL("http://posttestserver.com/post.php?dir=drissbounouar");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
       
        Log.v("AppJsonWriter", "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");//http://posttestserver.com/post.php?dir=example
            
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
            	Log.i("Post failed, error code","" + status);
              throw new IOException("Post failed with error code " + status);
            }else {
				Log.i("File sent successfully", ""+status);
			}
            return status;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
    
    public static int sendPostJson(String endpoint, String body){
    	
  
    	URI url;
//    	URI uri =  Uri.parse( endpoint );
    	URI uri = null;
		try {
			uri = new URI(endpoint);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        
        
    	HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
//        JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(uri);
//            json.put("email", email);
//            json.put("password", pwd);
            StringEntity se = new StringEntity( body);  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            /*Checking response */
            if(response!=null){
            	
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
            }

        } catch(Exception e) {
            e.printStackTrace();
//            createDialog("Error", "Cannot Estabilish Connection");
        }
		return 0;
    	
    }

}
