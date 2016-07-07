/**
 * 
 */
package com.paperpad.mybox.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author euphordev02
 *
 */
public class Utils {
	private static final int timeout = 20000;
	public static String LANG = "LANG";
	
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
	
	
	/**this method is used to change in the course of the application the {@link Locale} we do this because we have two places 
     * where we get the I18N resources ... from the BO and from android I18N strings
     * @param string this string is the locale name for example "it" for Italic "fr" for French ...
	 * @param context 
     */
    public static void changeLocale(String string, Context context) {
		Configuration config = new Configuration();
		Locale locale = new Locale(string);
		Locale.setDefault(locale);

		config.locale = locale;
		context.getResources()
				.updateConfiguration(
						config, null);
		
	}
    
    
	public static String retrieveJson(String url) {
		HttpGet getRequest = new HttpGet(url);
	
	    try {
	    	DefaultHttpClient httpClient = new DefaultHttpClient();
	    	HttpParams httpParams = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    	httpClient.setParams(httpParams);
	        HttpResponse getResponse = null;
			try {
				getResponse = httpClient.execute(getRequest);
			} catch (ClientProtocolException e) {
				Log.w("get Response error", ""+e.getMessage());
				e.printStackTrace();
			}
	        final int statusCode = getResponse.getStatusLine().getStatusCode();
	
	        if (statusCode != HttpStatus.SC_OK) {
	            return null;
	        }
	
	        HttpEntity getResponseEntity = getResponse.getEntity();
	
	        if (getResponseEntity != null) {
	        	
	        	String s = EntityUtils.toString(getResponseEntity, HTTP.UTF_8 );;
	        	
	            return s;//EntityUtils.toString(getResponseEntity, HTTP.UTF_8 );
	        }
	
	    } catch (ConnectTimeoutException e) {
	    	Log.w("Timeout exception "+timeout,   url, e);
		}catch (IOException e) {
	        getRequest.abort();
	        Log.w("Error in URL ",   url, e);
	    }
	
	    return null;
		
	}
	
	

	public static String encode(String input) {
		StringBuilder resultStr = new StringBuilder();
		for (char ch : input.toCharArray()) {
			if (isUnsafe(ch)) {
				resultStr.append('%');
				resultStr.append(toHex(ch / 16));
				resultStr.append(toHex(ch % 16));
			} else {
				resultStr.append(ch);
			}
		}
		return resultStr.toString();
	}

	private static char toHex(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	private static boolean isUnsafe(char ch) {
		if (ch > 128 || ch < 0)
			return true;
		return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
	}
	
	public static String encodeUrl(String url) {
		String[] parts = url.split("/");
		URI uri;
		String pathTmp = "";
		if (parts.length>3) {
			for (int i = 3; i < parts.length; i++) {
				pathTmp = pathTmp+"/"+ parts[i];
			}
		}
		try {
			uri = new URI(
			        "http",
			        null, // this is for userInfo
			        parts[2],
			        null, // port number as int
			        "pathTmp");
			
			String request = uri.toASCIIString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
		
	}
	
	/**Check if the supplied string is a valide email (checks only the format )
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email) {
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
	
	public static Date ConvertToDate(String dateString, String format){
		if (format.isEmpty()) {
			format = "dd/MM/yyyy hh:mm";
		}
	    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	    Date convertedDate = new Date();
	    try {
	        convertedDate = dateFormat.parse(dateString);
	        
	    } catch (ParseException e) {
	        e.printStackTrace();
	        convertedDate = null;
	    }
	    return convertedDate;
	}
	
	
	public static float spToPixels(Context context, Float sp) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return sp*scaledDensity;
	}

    public static float dpToPixels(Context context, Float dp) {
        float dpDensity = context.getResources().getDisplayMetrics().scaledDensity;
        if(context.getResources().getDisplayMetrics().densityDpi > 160)
            dpDensity = 1.5f;
        return dp*dpDensity;
    }
	
	public static int hsvInterpolate ( float mix ) {
	    float[] hsv0 = new float[3];
	    float[] hsv1 = new float[3];

	    float alt = 1.0f - mix;
	    Color.RGBToHSV( 255, 0,0 , hsv0 );
	    Color.RGBToHSV( 0, 255, 0, hsv1 );

	    float h = mix * hsv0 [ 0 ] +  alt * hsv1 [ 0 ];
	    float s = mix * hsv0 [ 1 ] +  alt * hsv1 [ 1 ];
	    float v = mix * hsv0 [ 2 ] +  alt * hsv1 [ 2 ];
	    float[] hsv = new float[]{h,s,v};

	    return Color.HSVToColor(hsv);
	}
	
	public static String progressiveColor(int value, int all){
		
		int red = 255 - (int)((float)(value*255)/(float)all);
		int green = (int)((float)(value*255)/(float)all);
		return String.format("#%06X", (0xFFFFFF & Color.argb(255, red, green, 0)));
		
	}
	
	private static String makeFragmentName(int viewId, int index)
	{
	     return "android:switcher:" + viewId + ":" + index;
	}
	
	public static String removeHtmlTags(String html){
        html = html.replaceAll("<(.*?)\\>","");//Removes all items in brackets
        html = html.replaceAll("<(.*?)\\\n","");//Must be undeneath
        html = html.replaceFirst("(.*?)\\>", "");//Removes any connected item to the last bracket
        html = html.replaceAll("’", "'");
        html = html.replaceAll("&nbsp;"," ");
        html = html.replaceAll("&amp;","&");
//		html = change_encoding(html); 
		return html;
	}


	/**
	 * @param html
	 * @return
	 */
	public static String change_encoding(String html) {
		byte ptext[] = html.getBytes(ISO_8859_1); 
		html = new String(ptext, UTF_8);
		return html;
	}
	
	public static String removeUnderScore(String chaine){
		chaine = chaine.replaceAll("\n\n","");//Removes all items in brackets
//		chaine = chaine.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
//		chaine = chaine.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
//		chaine = chaine.replaceAll("&nbsp;"," ");
//		chaine = chaine.replaceAll("&amp;"," ");
		return chaine;
	}
	
	
	/**
	 * 
	 * @param colorStr e.g. "RRGGBB"
	 * @return 
	 */
	public static int[] hex2Rgb(String colorStr) {
	    return new int[]{
	            Integer.valueOf( colorStr.substring( 0, 2 ), 16 ),
	            Integer.valueOf( colorStr.substring( 2, 4 ), 16 ),
	            Integer.valueOf( colorStr.substring( 4, 6 ), 16 ) };
	}

}
