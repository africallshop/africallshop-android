package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.linphone.R;
import org.linphone.mediastream.Log;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class ValidatePhoneTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "ValidatePhoneTask";
	private ProgressDialog progressBar;
	private int CONNECTION_STATE = 0;
	Context context;
	private int http_status_code = 200;
	private String msg= "";
	private Intent broadcastIntent;

	
	public ValidatePhoneTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
		progressBar = new ProgressDialog(context);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
				return mainProcess(params);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.setMessage(context.getString(R.string.task_status_processing));
		progressBar.setCancelable(false);
		progressBar.show();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result){
			sendBroadcast(Config.ACTION_CODE_VALIDATION, "");
		}else{
			if(msg.equals("KO")){
				new AlertDialog.Builder(context)
		        .setMessage(context.getString(R.string.validate_phone_ko))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}else{
				Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
			}
			
		}
		progressBar.dismiss();
		
	} 
	
	private RestClient buildClient(String... params){
		RestClient client = new RestClient(Config.API_CODEVALIDATION);
		client.addParam("enregistrerNumero", "1");
		client.addParam("language", context.getString(R.string.applang));		
		client.addParam("mobile-number", params[0]);
		client.addParam("id", Utils.getStringFromPreference(context, Config.JSON_ID));
//		Log.e(context.getString(R.string.applang) + "  "+ params[0] + " " +Utils.getStringFromPreference(context, Config.JSON_ID));
		return client;
	}
	
	private boolean mainProcess(String... params){
		RestClient client = buildClient(params);
	
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response  = client.getResponse();
//		    Log.e(TAG, client.getResponse());
		    msg = response != null? response.trim(): "";
		    if(http_status_code == 200){
		    	if(response != null && response.trim().equals("OK")){
		    		return true;
		    	}
		    }
		}catch(ConnectionPoolTimeoutException e) {
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(ConnectTimeoutException e){	
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(SocketTimeoutException e){
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(Exception e){
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
			//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " Error login "+e.toString());
		} 
		return false;
	}
	
    private void sendBroadcast(int action, String message){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("message", message);
    	context.sendBroadcast(broadcastIntent);
    }
}
