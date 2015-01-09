package org.linphone.asycntasks;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.utils.Config;
import org.linphone.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class GetpriceTask extends AsyncTask<String, Void, Boolean>{

	public static String TAG = "GetpriceTask";
	private String login;
	private String password; 
	private String codepays; 
	private String langue; 
	Context context;
	private int http_status_code = 200;
	
	
	private Intent broadcastIntent;
	private boolean contact_row_view;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utils.putStringToPreference(context, Config.LOCK_PRICE_TASK, "yes");
	}
	
	public GetpriceTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
	}

	public GetpriceTask(Context context, boolean v) {
		this.context = context;
		contact_row_view = v;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		login = Utils.getStringFromPreference(context, Config.USER);
		password = Utils.getStringFromPreference(context, Config.PWD);
		codepays = params[0];
		langue = params[1];
		return mainProcess();
	}

	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_PRICE);
		client.addParam("login", login);
		client.addParam("password", password);
		client.addParam("codePays", codepays);
		client.addParam("language", langue);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response = client.getResponse();
//		    Log.e(TAG, response);
		    if(http_status_code == 200){
		    	if(!response.trim().equalsIgnoreCase("KO")){
		    		if(contact_row_view){
		    			fillView(response);
		    		}else{
		    			sendBroadcast(Config.ACTION_GOT_PRICE, response);
		    		}
		    	}else{
		    		sendBroadcast(Config.ACTION_GOT_PRICE_NUMERROR, context.getString(R.string.incorrect_phonenumber));
		    	}
		    	return true;
		    }
		}catch(Exception e){
//			Log.e(TAG, " Error network "+e.toString());
		}
		sendBroadcast(Config.ACTION_GOT_PRICE_NETWORKERROR, context.getString(R.string.error_failure));
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result){
		super.onPostExecute(result);
		Utils.putStringToPreference(context, Config.LOCK_PRICE_TASK, "no");
	} 
	
	
	private void fillView(String response){ 
		if(contact_row_view){
			try {
				JSONObject json = (JSONObject) new JSONObject(response);
				sendBroadcastContact(Config.ACTION_GOT_PHONE_PRICE, codepays ,"€"+json.getString(Config.JSON_PRICE) + "|€"+ json.getString("sms"));
			} catch (JSONException e) {
			}
		}
	}
	
    private void sendBroadcast(int action, String message){
    	if(broadcastIntent !=null){
	    	broadcastIntent.putExtra("action", action);
	    	broadcastIntent.putExtra("message", message);
	    	context.sendBroadcast(broadcastIntent);
    	}
    }
    private void sendBroadcastContact(int action, String number, String price){
    	if(broadcastIntent !=null){
	    	broadcastIntent.putExtra("action", action);
	    	broadcastIntent.putExtra("number", number);
	    	broadcastIntent.putExtra("price", price);
	    	context.sendBroadcast(broadcastIntent);
    	}
    }
    
    @Override
    protected void onCancelled() {
        running = false;
    }
    private boolean running = true;
}
