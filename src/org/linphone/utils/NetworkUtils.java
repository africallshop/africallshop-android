package org.linphone.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkUtils {
	public static String TAG = "NetworkUtils";
	
	/**
	 * Ping an URL 
	 * @param ping_url
	 * @return boolean
	 */
	public static boolean PingUrl(String url){
		try{
			HttpURLConnection urlc = (HttpURLConnection) new URL(url).openConnection();
			urlc.setRequestProperty("User-Agent", "Android Application:");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(2000); // mTimeout is in seconds
			urlc.connect();
			if(urlc.getResponseCode() == 200){
				Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "Ping Ok");
				return true;
			}
		} catch (MalformedURLException e1){
			Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "MAlformeed Ping Error " + e1.toString());
		} catch (IOException e) {
	        Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "IO Ping Error " + e.toString());
		}
		return false;
	}
	
	/**
	 * Check whether the PHone is connected to a network
	 * @param context
	 * @return boolean
	 */
	public static boolean IsOnline(Context context){
        boolean connected = false;
  
        ConnectivityManager cm = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null){
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo){
                if ((ni.getTypeName().equalsIgnoreCase("WIFI")
                        || ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        && ni.isConnected() && ni.isAvailable()) {
                    connected = true;
                }
            }
        }
        return connected;
	}
	
	
}
