package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.NetworkUtils;
import org.linphone.utils.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class InscriptionTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "InscriptionTask";
	private ProgressDialog progressBar;
	private int CONNECTION_STATE = 0;
	Context context;
	private int http_status_code = 200;
	private String msg= "";
	private String title = "";
	private Intent broadcastIntent;
	
	public InscriptionTask(Context context) {
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
		this.progressBar.setCanceledOnTouchOutside(false);
		progressBar.show();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);		
		progressBar.dismiss();
		if(!result){
			if(msg.length()>0){
				new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.verification_error))
		        .setMessage(msg)
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}else{
//				Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
				new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.error_failure))
		        .setMessage(msg)
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}
		}
		
//		sendBroadcast(Config.ACTION_QUIT_PROGRESS_BAR, "");
//		Log.e(TAG, result+" "+msg+" "+CONNECTION_STATE);
		
//		if(!result){
//			switch (CONNECTION_STATE) {
//				case ConnectionState.NO_CONNECTION:
//					Toast.makeText(context, context.getString(R.string.error_no_wifi), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.SERVER_NOT_AVAILABLE:
//					Toast.makeText(context, context.getString(R.string.error_sever_not_available), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.TIMEOUT:
//					Toast.makeText(context, context.getString(R.string.error_timetout), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.INTERNAL_ERROR:
//					Utils.showToast(context, context.getString(R.string.error_failure)+". "+msg);
//					//Toast.makeText(context, context.getString(R.string.error_failure)+". "+msg, Toast.LENGTH_LONG).show();
//					break;
//				default:
//					break;
//			}
//		}
	}
	
	private RestClient buildClient(String... params){
		RestClient client = new RestClient(Config.API_SUBSCRIBE);
		client.addParam("language", params[0]);
		client.addParam("adresseMail", params[1]);
		client.addParam("motDePasse", params[2]);
		client.addParam("motDePasseConfirmation", params[3]);
		return client;
	}
	
	private boolean mainProcess(String... params){
		RestClient client = buildClient(params);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response =  client.getResponse();
//		    Log.e(TAG, response);
		    if(http_status_code == 200){
		    	return hasSubscribe(response);
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
	 
	private String badField(String json_string){
		try {
//			Log.e(TAG, json_string);
			JSONObject json = new JSONArray("["+json_string+"]").getJSONObject(0);
			
			if(json.getString("AdresseMail").equalsIgnoreCase("ko")){
				return context.getString(R.string.error_bad_field_email);
			}			
			if(json.getString("MotDePasse").equalsIgnoreCase("ko")){
				return context.getString(R.string.error_bad_field_password);
			}
			if(json.getString("MotDePasseConfirmation").equalsIgnoreCase("ko")){
				return context.getString(R.string.error_bad_field_confirm_password);
			}			
			if(json.getString("CompteExiste").equalsIgnoreCase("ko")){
				return context.getString(R.string.error_bad_compte_exist);
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
		return "";
	}
	
	private boolean hasSubscribe(String json_string){
		if(savesubscription(json_string)){
			sendBroadcast(Config.ACTION_PHONE_VALIDATION, "");
			return true;
		}else{
			msg = badField(json_string);
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
		}
		return false;
	}
	
	private boolean savesubscription(String response){
		try {
			JSONObject json = new JSONObject(response);
			String inscription = json.getString("inscription");
			String id= json.getString("id");
			Utils.putStringToPreference(context, Config.JSON_ID, id);
			return true;
		}catch (Exception e){ 
			e.printStackTrace();
		}
		return false;
	}
	
		
    private void sendBroadcast(int action, String message){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("message", message);
    	context.sendBroadcast(broadcastIntent);
    }
}
